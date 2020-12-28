package com.github.ms5984.clans.clansbanks.lending;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.HasInterest;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;

/**
 * Interest collected per period.
 */
public class PeriodicInterestLoan extends AbstractLoan implements HasInterest {

    protected BigDecimal interestRate;
    protected long period;
    protected long lastInterest = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    protected long tempInterest = 0L;

    protected PeriodicInterestLoan(ClanBank clanBank, BigDecimal principal, BigDecimal interestRate, long periodInSeconds) {
        super(clanBank, principal);
        this.interestRate = interestRate;
        this.period = periodInSeconds;
    }

    @Override
    public BigDecimal calculateInterest() {
        return currentBalance.multiply(interestRate);
    }

    @Override
    public void collectInterest(OfflinePlayer offlinePlayer, Consumer<Boolean> callback) {
        final BigDecimal interest = calculateInterest();
        if (ClansBanks.getEconomy().withdrawPlayer(offlinePlayer, interest.doubleValue()).transactionSuccess()) {
            callback.accept(true);
            return;
        }
        callback.accept(false); // unable to withdraw interest, add on to loan
        currentBalance = currentBalance.add(calculateInterest());
    }

    @Override
    public long getPeriodInSeconds() {
        return period;
    }
}
