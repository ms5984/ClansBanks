package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class BankPreTransactionEvent extends BankTransactionEvent implements Cancellable {

    private boolean success;
    private boolean cancelled;

    public BankPreTransactionEvent(Player player, ClanBank clanBank, BigDecimal amount, String clanId, boolean success, BankTransactionEvent.Type type) {
        super(player, clanBank, amount, clanId, success, type);
        this.success = success;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
        if (!cancel && success) {
            success = false;
        }
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() { // TODO: lang
        switch (type) {
            case DEPOSIT:
                return "Transaction " + (cancelled ? "-CANCELLED" : "PRE-ACCEPTED") + " [" +
                        (success ? "SUCCESS" : "FAILED") + "]: " +
                        player.getName() + " deposited " + amount + " with clanId=" + clanId;
            case WITHDRAWAL:
                return "Transaction " + (cancelled ? "-CANCELLED" : "PRE-ACCEPTED") + " [" +
                        (success ? "SUCCESS" : "FAILED") + "]: " +
                        player.getName() + " withdrawn " + amount + " from clanId=" + clanId;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
