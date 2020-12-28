package com.github.ms5984.clans.clansbanks.api;

import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.function.Consumer;

public interface HasInterest {
    /**
     * Calculate the interest for the loan
     * @return calculated interest
     */
    BigDecimal calculateInterest();

    /**
     * Depends on perspective; could be income or expense
     * @param offlinePlayer Player to test
     * @param callback method to run on success/fail
     */
    void collectInterest(OfflinePlayer offlinePlayer, Consumer<Boolean> callback);

    /**
     * Return the frequency in seconds of interest accrual
     */
    long getPeriodInSeconds();
}
