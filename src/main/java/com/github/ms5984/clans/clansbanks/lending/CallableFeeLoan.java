package com.github.ms5984.clans.clansbanks.lending;

import com.github.ms5984.clans.clansbanks.api.CallableLoan;
import com.github.ms5984.clans.clansbanks.api.ClanBank;

import java.math.BigDecimal;

public class CallableFeeLoan extends FeeLoan implements CallableLoan {
    protected CallableFeeLoan(ClanBank clanBank, BigDecimal principal, BigDecimal fee) {
        super(clanBank, principal, fee);
    }

    @Override
    public boolean call() {
        return false;
    }
}
