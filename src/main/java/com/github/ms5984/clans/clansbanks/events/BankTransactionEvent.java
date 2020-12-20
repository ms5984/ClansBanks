package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.youtube.hempfest.clans.util.construct.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public class BankTransactionEvent extends ClansBanksEvent {

    public enum Type {
        DEPOSIT, WITHDRAWAL
    }

    protected final Player player;
    protected final BigDecimal amount;
    protected final String clanId;
    protected final boolean success;
    protected final Type type;

    public BankTransactionEvent(Player player, ClanBank clanBank, BigDecimal amount, String clanId, boolean success, Type type) {
        super(clanBank);
        this.player = player;
        this.amount = amount;
        this.clanId = clanId;
        this.success = success;
        this.type = type;
    }
    public BankTransactionEvent(BankTransactionEvent event) {
        super(event.clanBank);
        this.player = event.player;
        this.amount = event.amount;
        this.clanId = event.clanId;
        this.success = event.success;
        this.type = event.type;
    }

    /**
     * Get the player associated with this transaction
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the ClanBank associated with this transaction
     * @return the ClanBank
     */
    @Override
    public ClanBank getClanBank() {
        return super.getClanBank();
    }

    /**
     * Get the amount involved with this transaction
     * @return a BigDecimal amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Get the Clan associated with this bank
     * @return the clan whose bank this is
     */
    public Clan getClan() {
        return CompletableFuture.supplyAsync(() -> Clan.clanUtil.getClan(clanId)).join();
    }

    /**
     * Denotes whether or not the transaction was successful
     * @return true if successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * A transaction may constitute a deposit or withdrawal
     * @return {@link Type} representing this transaction
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() { // TODO: lang
        switch (type) {
            case DEPOSIT:
                return "Transaction [" + (success ? "SUCCESS" : "FAILED") + "]: " +
                        player.getName() + " deposited " + amount + " with clanId=" + clanId;
            case WITHDRAWAL:
                return "Transaction [" + (success ? "SUCCESS" : "FAILED") + "]: " +
                        player.getName() + " withdraw " + amount + " from clanId=" + clanId;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
