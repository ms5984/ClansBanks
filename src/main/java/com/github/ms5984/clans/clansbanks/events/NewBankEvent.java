package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.youtube.hempfest.clans.util.construct.Clan;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class NewBankEvent extends ClansBanksEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Clan clan;
    private final BigDecimal startingBalance;

    public NewBankEvent(Clan clan, ClanBank clanBank) {
        super(clanBank);
        this.clan = clan;
        this.startingBalance = clanBank.getBalance();
    }

    /**
     * Get the clan whose bank was just created
     * @return the Clan
     */
    public Clan getClan() {
        return clan;
    }

    /**
     * Get the starting balance of the bank (usually 0 per default configuration).
     * @return the initial balance
     */
    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
