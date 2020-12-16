package com.github.ms5984.clans.clansbanks.api;

import com.youtube.hempfest.clans.util.construct.Clan;

import java.math.BigDecimal;

public interface BanksAPI {
    default ClanBank getBank(Clan clan) {
        return null;
    }
    default BigDecimal defaultBalance() {
        return BigDecimal.ZERO;
    }
}
