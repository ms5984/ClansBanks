package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.events.BankPreTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class Bank implements ClanBank, Listener {
    private static final Economy ECO = ClansBanks.getEconomy();
    private static final PluginManager PM = Bukkit.getServer().getPluginManager();
    private BigDecimal balance;
    private boolean enabled;
    private String clanId;

    public Bank(@NotNull String clanId) {
        this.balance = Objects.requireNonNull(ClansBanks.getAPI()).defaultBalance();
        this.enabled = true;
        this.clanId = clanId;
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(Bank.class));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    protected void onDeposit(BankPreTransactionEvent event) {
        if (event.getClanBank() != this) return;
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = ECO.withdrawPlayer(player, player.getWorld().getName(),
                amount.doubleValue()).transactionSuccess();
        if (success) balance = balance.add(amount);
        PM.callEvent(new BankTransactionEvent(player, this, amount, clanId, success, BankTransactionEvent.Type.DEPOSIT));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    protected void onWithdrawal(BankPreTransactionEvent event) {
        if (event.getClanBank() != this) return;
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = ECO.depositPlayer(player, player.getWorld().getName(), amount.doubleValue()).transactionSuccess();
        if (success) balance = balance.subtract(amount);
        PM.callEvent(new BankTransactionEvent(player, this, amount, clanId, success, BankTransactionEvent.Type.WITHDRAWAL));
    }

    @Override
    public boolean deposit(Player player, BigDecimal amount) {
        if (!enabled) return false;
        final boolean has = ECO.has(player, player.getWorld().getName(), amount.doubleValue());
        final BankPreTransactionEvent preTransactionEvent =
                new BankPreTransactionEvent(player, this, amount, clanId, has, BankTransactionEvent.Type.DEPOSIT);
        PM.callEvent(preTransactionEvent);
        return preTransactionEvent.isSuccess();
    }

    @Override
    public boolean withdraw(Player player, BigDecimal amount) {
        if (!enabled) return false;
        final boolean has = this.balance.compareTo(amount) < 0;
        final BankPreTransactionEvent preTransactionEvent =
                new BankPreTransactionEvent(player, this, amount, clanId, has, BankTransactionEvent.Type.WITHDRAWAL);
        PM.callEvent(preTransactionEvent);
        return preTransactionEvent.isSuccess();
    }

    @Override
    public boolean has(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    @Override
    public double getBalanceDouble() {
        return balance.doubleValue();
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }
}
