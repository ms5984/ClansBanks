/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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
package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.youtube.hempfest.clans.HempfestClans;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class BanksPlaceholders extends PlaceholderExpansion {

    private final Plugin plugin;
    private final BanksAPI api;

    public BanksPlaceholders(ClansBanks plugin) {
        this.plugin = plugin;
        this.api = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clansbanks";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        val optionalPlayer = Optional.ofNullable(player);
        return optionalPlayer.map(p -> {
            if ("balance".equals(params)) {
                val optionalBalance = Optional.ofNullable(HempfestClans.getInstance().playerClan.get(player.getUniqueId()))
                        .map(s -> HempfestClans.clanManager(player)).map(api::getBank).map(ClanBank::getBalance);
                if (optionalBalance.isPresent()) {
                    return optionalBalance.get().toString();
                }
            }
            return null;
        }).orElse(null);
    }
}
