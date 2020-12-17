package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import org.bukkit.event.Event;

public abstract class ClansBanksEvent extends Event {

    protected final ClanBank clanBank;

    protected ClansBanksEvent(ClanBank clanBank) {
        this.clanBank = clanBank;
    }

    /**
     * Get the ClanBank associated with this event
     * @return the ClanBank
     */
    public ClanBank getClanBank() {
        return clanBank;
    }
}
