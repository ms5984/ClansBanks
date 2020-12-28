package com.github.ms5984.clans.clansbanks.util;

import org.bukkit.command.CommandSender;

public enum Permissions {
    BANKS_STAR("clans.banks.*"),
    BANKS_USE("clans.banks.use"),
    BANKS_USE_STAR("clans.banks.use.*"),
    BANKS_BALANCE("clans.banks.use.balance"),
    BANKS_DEPOSIT("clans.banks.use.deposit"),
    BANKS_WITHDRAW("clans.banks.use.withdraw"),
    BANKS_LENDING("clans.banks.lending"), // User access to lending (can borrow)
    BANKS_LENDING_STAR("clans.banks.lending.*"), // Full access to lending (can make/take loans)
    BANKS_CAN_BORROW("clans.banks.lending.borrow"), // Can ask for loans
    BANKS_CAN_LEND("clans.banks.lending.lend"), // Can can initiate loans
    BANKS_LOAN_VOTE("clans.banks.lending.loan"), // Can vote on clan loans
    BANKS_LOAN_UNDERWRITE("clans.banks.lending.loan.underwrite"), // Can make loans decisions
    BANKS_LOAN("clans.banks.use.loan"), // loan subcommand = /clan bank loan <player> <amount>
    BANKS_BORROW("clans.banks.use.borrow"); // borrow subcommand = /clan bank borrow <amount>

    public final String node;

    Permissions(String s) {
        this.node = s;
    }

    public boolean not(CommandSender sender) {
        return !sender.hasPermission(node);
    }
}
