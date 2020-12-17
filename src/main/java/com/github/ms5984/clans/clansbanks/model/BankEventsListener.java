package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.MetaObject;
import com.github.ms5984.clans.clansbanks.events.BankPreTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.youtube.hempfest.clans.metadata.PersistentClan;
import com.youtube.hempfest.hempcore.library.HUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;

public class BankEventsListener implements Listener {

    private static final JavaPlugin P = JavaPlugin.getProvidingPlugin(Bank.class);

    @EventHandler
    public void onCreate(NewBankEvent e) {
        final HUID huid = e.getClan().getId(ClansBanks.BANKS_META_ID);
        final PersistentClan persistentClan;
        if (huid != null) {
            PersistentClan.deleteInstance(huid);
        }
        final Bank bank = (Bank) e.getClanBank();
        persistentClan = new PersistentClan(bank.clanId);
        persistentClan.setValue(bank, MetaObject.BANK.id);
        persistentClan.storeTemp();
        new BukkitRunnable() {
            @Override
            public void run() {
                persistentClan.saveMeta(ClansBanks.BANKS_META_ID);
            }
        }.runTask(P);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTransaction(BankTransactionEvent e) {
        if (e instanceof BankPreTransactionEvent) return;
        final Bank bank = (Bank) e.getClanBank();
        final PersistentClan persistentClan = bank.getMeta();
        persistentClan.setValue(bank, MetaObject.BANK.id);
        persistentClan.storeTemp();
        new BukkitRunnable() {
            @Override
            public void run() {
                persistentClan.saveMeta(ClansBanks.BANKS_META_ID);
            }
        }.runTask(P);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeposit(BankPreTransactionEvent event) {
        if (event.getType() != BankTransactionEvent.Type.DEPOSIT) return;
        final Bank bank = (Bank) event.getClanBank();
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = Bank.ECO.withdrawPlayer(player, player.getWorld().getName(),
                amount.doubleValue()).transactionSuccess();
        if (success) bank.balance = bank.balance.add(amount);
        if (!success) event.setSuccess(false);
        final BankTransactionEvent event1 = new BankTransactionEvent(player, bank, amount, bank.clanId, success, BankTransactionEvent.Type.DEPOSIT);
        Bank.PM.callEvent(event1);
        System.out.println(event1.toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWithdrawal(BankPreTransactionEvent event) {
        if (event.getType() != BankTransactionEvent.Type.WITHDRAWAL) return;
        final Bank bank = (Bank) event.getClanBank();
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = Bank.ECO.depositPlayer(player, player.getWorld().getName(), amount.doubleValue()).transactionSuccess();
        if (success) bank.balance = bank.balance.subtract(amount);
        if (!success) event.setSuccess(false);
        Bank.PM.callEvent(new BankTransactionEvent(player, bank, amount, bank.clanId, success, BankTransactionEvent.Type.WITHDRAWAL));
    }

}
