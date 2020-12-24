package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.youtube.hempfest.clans.util.construct.Clan;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class BankActionEvent extends ClansBanksEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    protected final String clanId;

    protected BankActionEvent(ClanBank clanBank, String clanId) {
        super(clanBank);
        this.clanId = clanId;
    }

    /**
     * Get the direct clanId for this event
     * @return clanId as String
     */
    public String getClanId() {
        return clanId;
    }

    /**
     * Get the clan associated with this bank event
     * @return the Clan whose bank this is
     */
    public Clan getClan() {
        return CompletableFuture.supplyAsync(() -> Clan.clanUtil.getClan(clanId)).join();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
