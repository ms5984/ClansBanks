package com.github.ms5984.clans.clansbanks.lending;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.HasFee;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Flat fee at origination/end. No periodic interest.
 */
public class FeeLoan extends AbstractLoan implements HasFee {

    private final BigDecimal fee;
    private boolean paid = false;

    protected FeeLoan(ClanBank clanBank, BigDecimal principal, BigDecimal fee) {
        super(clanBank, principal);
        this.fee = fee;
    }

    @Override
    public BigDecimal getFee() {
        return fee;
    }

    @Override
    public void payFee(OfflinePlayer offlinePlayer, Consumer<Boolean> callback) {
        if (paid) {
            return;
        }
        final boolean success = ClansBanks.getEconomy().withdrawPlayer(offlinePlayer, fee.doubleValue()).transactionSuccess();
        if (success) paid = true;
        callback.accept(success);
    }

    @Override
    public boolean feePaid() {
        return paid;
    }
}
