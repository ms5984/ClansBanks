package com.github.ms5984.clans.clansbanks.api;

import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.function.Consumer;

public interface HasFee {
    /**
     * Get the amount of the fee
     * @return fee as BigDecimal
     */
    BigDecimal getFee();

    /**
     * Pay the fee. This method does nothing if feePaid is true
     * @param callback method to run with completion status
     */
    void payFee(OfflinePlayer offlinePlayer, Consumer<Boolean> callback);

    /**
     * Returns true if the fee has already been paid
     * @return true; otherwise false
     */
    boolean feePaid();
}
