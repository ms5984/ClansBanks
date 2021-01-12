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

import org.bukkit.command.CommandSender;

public enum Permissions {
    BANKS_STAR("clans.banks.*"),
    BANKS_USE("clans.banks.use"),
    BANKS_USE_STAR("clans.banks.use.*"),
    BANKS_BALANCE("clans.banks.use.balance"),
    BANKS_DEPOSIT("clans.banks.use.deposit"),
    BANKS_WITHDRAW("clans.banks.use.withdraw");

    public final String node;

    Permissions(String s) {
        this.node = s;
    }

    public boolean not(CommandSender sender) {
        return !sender.hasPermission(node);
    }
}
