package com.github.ms5984.clans.clansbanks.api;

import com.youtube.hempfest.clans.util.construct.Clan;

import java.math.BigDecimal;

public interface BanksAPI {
    /**
     * Describes a level of output logged to the console
     */
    enum LogLevel {
        VERBOSE, QUIET, SILENT
    }

    /**
     * Gets the bank associated with a clan
     * @param clan the desired clan
     * @return a Bank; if it does not exist it is created
     */
    default ClanBank getBank(Clan clan) {
        return null;
    }

    /**
     * Set the default balance of newly-created Banks
     * @return the starting balance of new banks
     */
    default BigDecimal defaultBalance() {
        return BigDecimal.ZERO;
    }

    /**
     * Set the transaction logging level
     * @return a {@link LogLevel} representing desired verbosity
     */
    default LogLevel logToConsole() {
        return LogLevel.VERBOSE;
    }
}
