package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
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
        if (cancel && success) { // don't flip cancel, this is meant to set success to false on cancel = true
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
    public String toString() {
        switch (type) {
            case DEPOSIT:
                return (cancelled ? Messages.TRANSACTION_DEPOSIT_PRE_CANCELLED : Messages.TRANSACTION_DEPOSIT_PRE).toString()
                        .replace("{0}", (success ? Messages.PRETRANSACTION_PENDING.toString() : Messages.PRETRANSACTION_FAILURE.toString()))
                        .replace("{1}", player.getName())
                        .replace("{2}", amount.toString())
                        .replace("{3}", getClan().getClanTag());
            case WITHDRAWAL:
                return (cancelled ? Messages.TRANSACTION_WITHDRAW_PRE_CANCELLED : Messages.TRANSACTION_WITHDRAW_PRE).toString()
                        .replace("{0}", (success ? Messages.PRETRANSACTION_PENDING.toString() : Messages.PRETRANSACTION_FAILURE.toString()))
                        .replace("{1}", player.getName())
                        .replace("{2}", amount.toString())
                        .replace("{3}", getClan().getClanTag());
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
