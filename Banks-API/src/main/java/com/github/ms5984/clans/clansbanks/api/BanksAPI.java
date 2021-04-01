/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  This file is part of Banks-API.
 *
 *  Banks-API is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  Banks-API is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.clans.clansbanks.api;

import com.youtube.hempfest.clans.util.construct.Clan;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public interface BanksAPI {
    /**
     * Describes a level of output logged to the console
     */
    enum LogLevel {
        SILENT, QUIET, VERBOSE
    }

    /**
     * Gets the bank associated with a clan
     * @param clan the desired clan
     * @return a Bank; if it does not exist it is created
     */
    ClanBank getBank(Clan clan);

    /**
     * Set the default balance of newly-created Banks
     * @return the starting balance of new banks
     */
    default BigDecimal startingBalance() {
        return BigDecimal.ZERO;
    }

    /**
     * This value reflects the maximum balance of Banks if configured.
     * Returns null if no set maximum
     * @return the maximum balance or null
     */
    default @Nullable BigDecimal maxBalance() {
        return null;
    }

    /**
     * Set the transaction logging level
     * @return a {@link LogLevel} representing desired verbosity
     */
    default LogLevel logToConsole() {
        return LogLevel.QUIET;
    }

    /**
     * Retrieve the API instance via Bukkit's ServicesManager.
     * @return BanksAPI provider
     */
    static BanksAPI getInstance() {
        final RegisteredServiceProvider<BanksAPI> rsp = Bukkit.getServicesManager().getRegistration(BanksAPI.class);
        if (rsp == null) throw new IllegalStateException("Clans[Banks] is not loaded!");
        return rsp.getProvider();
    }
}
