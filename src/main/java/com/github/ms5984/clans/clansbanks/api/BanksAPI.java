package com.github.ms5984.clans.clansbanks.api;

import com.youtube.hempfest.clans.util.construct.Clan;
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
    default BigDecimal defaultBalance() {
        return BigDecimal.ZERO;
    }

    /**
     * This value reflects the maximum balance of Banks if configured.
     * Returns null if no set maximum
     * @return the maximum balance or null
     */
    @Nullable
    default BigDecimal maxBalance() {
        return null;
    }

    /**
     * Set the transaction logging level
     * @return a {@link LogLevel} representing desired verbosity
     */
    default LogLevel logToConsole() {
        return LogLevel.QUIET;
    }
}
