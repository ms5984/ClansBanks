package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.MetaObject;
import com.github.ms5984.clans.clansbanks.events.BankPreTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import com.github.ms5984.clans.clansbanks.events.NewBankEvent;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
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
        if (!(e.getClanBank() instanceof Bank)) return; // Only react on our ClanBank implementation
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
    public void onPreTransactionMonitor(BankPreTransactionEvent event) {
        switch (ClansBanks.getAPI().logToConsole()) {
            case SILENT:
                return;
            case QUIET:
                if (event.isCancelled()) ClansBanks.log().info(event.toString());
                return;
            case VERBOSE:
                ClansBanks.log().info(event.toString() + " " +
                        Messages.TRANSACTION_VERBOSE_CLAN_ID.toString()
                        .replace("{0}", event.getClanId())
                );
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTransaction(BankTransactionEvent e) {
        if (e instanceof BankPreTransactionEvent) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (ClansBanks.getAPI().logToConsole()) {
                    case SILENT:
                        break;
                    case QUIET:
                        ClansBanks.log().info(e.toString());
                        break;
                    case VERBOSE:
                        ClansBanks.log().info(e.toString() + " " +
                                Messages.TRANSACTION_VERBOSE_CLAN_ID.toString()
                                        .replace("{0}", e.getClanId())
                        );
                }
            }
        }.runTask(P);
        if (!(e.getClanBank() instanceof Bank)) return; // Only react on our ClanBank implementation
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
        if (!(event.getClanBank() instanceof Bank)) return; // Only react on our ClanBank implementation
        if (!event.isSuccess()) {
            event.setCancelled(true);
            return; // The player didn't have enough money or is not allowed, no transaction
        }
        final Bank bank = (Bank) event.getClanBank();
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = Bank.ECO.withdrawPlayer(player, player.getWorld().getName(),
                amount.doubleValue()).transactionSuccess();
        if (success) bank.balance = bank.balance.add(amount);
        if (!success) event.setSuccess(false);
        final BankTransactionEvent event1 = new BankTransactionEvent(player, bank, amount, bank.clanId, success, BankTransactionEvent.Type.DEPOSIT);
        Bank.PM.callEvent(event1);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWithdrawal(BankPreTransactionEvent event) {
        if (event.getType() != BankTransactionEvent.Type.WITHDRAWAL) return;
        if (!(event.getClanBank() instanceof Bank)) return; // Only react on our ClanBank implementation
        if (!event.isSuccess()) {
            event.setCancelled(true);
            return; // The bank didn't have enough money or is not allowed, no transaction
        }
        final Bank bank = (Bank) event.getClanBank();
        final Player player = event.getPlayer();
        final BigDecimal amount = event.getAmount();
        final boolean success = Bank.ECO.depositPlayer(player, player.getWorld().getName(), amount.doubleValue()).transactionSuccess();
        if (success) bank.balance = bank.balance.subtract(amount);
        if (!success) event.setSuccess(false);
        Bank.PM.callEvent(new BankTransactionEvent(player, bank, amount, bank.clanId, success, BankTransactionEvent.Type.WITHDRAWAL));
    }

}
