package com.github.ms5984.clans.clansbanks.lending;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.Loan;

import java.math.BigDecimal;
import java.util.function.Consumer;

public abstract class AbstractLoan implements Loan {

    protected final ClanBank clanBank;
    protected final BigDecimal principal;
    protected BigDecimal currentBalance;

    public AbstractLoan(ClanBank clanBank, BigDecimal principal) {
        this.clanBank = clanBank;
        this.currentBalance = this.principal = principal;
    }

    public ClanBank getBank() {
        return clanBank;
    }

    @Override
    public BigDecimal principal() {
        return principal;
    }

    @Override
    public void makePayment(BigDecimal amount, Consumer<Boolean> callback) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException();
    }

    @Override
    public BigDecimal remainingBalance() {
        return currentBalance;
    }
}
