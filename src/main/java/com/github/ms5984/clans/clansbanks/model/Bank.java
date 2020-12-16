package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.Objects;

public class Bank implements ClanBank {
    private static final Economy ECO = ClansBanks.getEconomy();
    private BigDecimal balance;
    private boolean enabled;

    public Bank() {
        this.balance = Objects.requireNonNull(ClansBanks.getAPI()).defaultBalance();
        this.enabled = true;
    }

    @Override
    public boolean deposit(Player player, BigDecimal amount) {
        if (!enabled) return false;
        final boolean success = ECO.withdrawPlayer(player, player.getWorld().getName(),
                amount.doubleValue()).transactionSuccess();
        if (success) balance = balance.add(amount);
        return success;
    }

    @Override
    public boolean withdraw(Player player, BigDecimal amount) {
        if (!enabled || this.balance.compareTo(amount) < 0) {
            return false;
        }
        final boolean success = ECO.depositPlayer(player, player.getWorld().getName(), amount.doubleValue()).transactionSuccess();
        if (success) balance = balance.subtract(amount);
        return success;
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
