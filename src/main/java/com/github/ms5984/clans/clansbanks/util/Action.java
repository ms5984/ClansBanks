/*
 *  Copyright 2020 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  This file is part of ClansBanks.
 *
 *  ClansBanks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  ClansBanks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.clans.clansbanks.util;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.MetaObject;
import com.youtube.hempfest.clans.metadata.ClanMeta;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.hempcore.library.HFEncoded;
import com.youtube.hempfest.hempcore.library.HUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public enum Action {
    BALANCE("balance"),
    DEPOSIT("deposit"),
    WITHDRAW("withdraw");

    protected static class AccessMap {
        private final Map<Action, Integer> acl = new HashMap<>();

        AccessMap() {
            for (Action action : Action.values()) {
                acl.computeIfAbsent(action, Action::getConfigDefault);
            }
        }

        private void setForClan(Clan clan) {
            final HUID huid = clan.getId(ClansBanks.BANKS_META_ID);
            final PersistentClan persistentClan;
            if (huid == null) {
                persistentClan = new PersistentClan(clan.getClanID());
            } else {
                persistentClan = PersistentClan.loadSavedInstance(huid);
            }
            persistentClan.setValue(this, MetaObject.RANK_ACCESS.id);
            persistentClan.storeTemp();
            new BukkitRunnable() {
                @Override
                public void run() {
                    persistentClan.saveMeta(ClansBanks.BANKS_META_ID);
                }
            }.runTask(JavaPlugin.getProvidingPlugin(Action.class));
        }
    }

    private final String line;

    Action(String line) {
        this.line = line;
    }

    public int getConfigDefault() {
        return JavaPlugin.getProvidingPlugin(Action.class).getConfig().getInt(line, 2);
    }

    public boolean testPlayer(Clan clan, Player player) {
        return Clan.clanUtil.getRankPower(player) >= getClanAccess(clan).join().acl.get(this);
    }

    public void setRankInClan(Clan clan, int level) {
        final AccessMap join = getClanAccess(clan).join();
        join.acl.put(this, level);
        join.setForClan(clan);
    }

    private static CompletableFuture<AccessMap> getClanAccess(Clan clan) {
        return CompletableFuture.supplyAsync(() -> {
            final HUID huid = clan.getId(ClansBanks.BANKS_META_ID);
            if (huid != null) {
                ClanMeta meta = PersistentClan.loadTempInstance(huid);
                if (meta == null) {
                    meta = PersistentClan.loadSavedInstance(huid);
                }
                if (meta != null) {
                    try {
                        return (AccessMap) new HFEncoded(meta.value(MetaObject.RANK_ACCESS.id)).deserialized();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return new AccessMap();
        });
    }
}
