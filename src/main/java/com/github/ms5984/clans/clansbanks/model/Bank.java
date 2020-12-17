package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.MetaObject;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.events.BankPreTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.hempcore.library.HUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Bank implements ClanBank, Listener, Serializable {
    private static final long serialVersionUID = -5283828168295980464L;
    private static final Economy ECO = ClansBanks.getEconomy();
    private static final PluginManager PM = Bukkit.getServer().getPluginManager();
    private static final JavaPlugin P = JavaPlugin.getProvidingPlugin(Bank.class);
    private BigDecimal balance;
    private boolean enabled;
    private final String clanId;
    private transient PersistentClan meta = null;

    {
        Bukkit.getServer().getPluginManager().registerEvents(this, P);
    }

    public Bank(@NotNull String clanId) {
        this.balance = Objects.requireNonNull(ClansBanks.getAPI()).defaultBalance();
        this.enabled = true;
        this.clanId = clanId;
    }

    @EventHandler
    protected void onCreate(NewBankEvent e) {
        final HUID huid = e.getClan().getId(ClansBanks.BANKS_META_ID);
        if (e.getClanBank() != this) return;
        final PersistentClan persistentClan;
        if (huid != null) {
            PersistentClan.deleteInstance(huid);
        }
        persistentClan = new PersistentClan(clanId);
        persistentClan.setValue(this, MetaObject.BANK.id);
        persistentClan.storeTemp();
        new BukkitRunnable() {
            @Override
            public void run() {
                persistentClan.saveMeta(ClansBanks.BANKS_META_ID);
            }
        }.runTask(P);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    protected void onTransaction(BankTransactionEvent e) {
        if (e instanceof BankPreTransactionEvent) return;
        if (e.getClanBank() != this) return;
        final PersistentClan persistentClan = getMeta();
        persistentClan.setValue(this, MetaObject.BANK.id);
        persistentClan.storeTemp();
        new BukkitRunnable() {
            @Override
            public void run() {
                persistentClan.saveMeta(ClansBanks.BANKS_META_ID);
            }
        }.runTask(P);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    protected void onDeposit(BankPreTransactionEvent event) {
        if (event.getClanBank() != this) return;
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = ECO.withdrawPlayer(player, player.getWorld().getName(),
                amount.doubleValue()).transactionSuccess();
        if (success) balance = balance.add(amount);
        if (!success) event.setSuccess(false);
        PM.callEvent(new BankTransactionEvent(player, this, amount, clanId, success, BankTransactionEvent.Type.DEPOSIT));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    protected void onWithdrawal(BankPreTransactionEvent event) {
        if (event.getClanBank() != this) return;
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = ECO.depositPlayer(player, player.getWorld().getName(), amount.doubleValue()).transactionSuccess();
        if (success) balance = balance.subtract(amount);
        if (!success) event.setSuccess(false);
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

    private PersistentClan getMeta() {
        if (meta != null) {
            return meta;
        }
        final HUID huid = Clan.clanUtil.getClan(clanId).getId(ClansBanks.BANKS_META_ID);
        if (huid != null) {
            return meta = Objects.requireNonNull(PersistentClan.loadSavedInstance(huid));
        }
        return meta = new PersistentClan(clanId);
    }
}
