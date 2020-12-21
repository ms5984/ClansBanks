package com.github.ms5984.clans.clansbanks.util;

import org.bukkit.command.CommandSender;

public enum Permissions {
    BANKS_STAR("clans.banks.*"),
    BANKS_USE("clans.banks.use"),
    BANKS_USE_STAR("clans.banks.use.*"),
    BANKS_BALANCE("clans.banks.use.balance"),
    BANKS_DEPOSIT("clans.banks.use.deposit"),
    BANKS_WITHDRAW("clans.banks.use.withdraw");

    public final String node;

    Permissions(String s) {
        this.node = s;
    }

    public boolean test(CommandSender sender) {
        return sender.hasPermission(node);
    }
}
