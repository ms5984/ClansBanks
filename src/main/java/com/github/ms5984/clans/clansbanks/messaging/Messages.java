package com.github.ms5984.clans.clansbanks.messaging;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public enum Messages {
    BANKS_HEADER("banks.header"),
    CLANS_HELP_PREFIX("clans.help.prefix"),
    BANK_HELP_PREFIX("bank.help.prefix"),
    BANK_HELP_AMOUNT_COMMANDS("bank.help.amount_commands"),
    BANKS_CURRENT_BALANCE("banks.current_balance"),
    BANKS_COMMAND_LIST("banks.command_list"),
    BALANCE("balance"),
    DEPOSIT("deposit"),
    WITHDRAW("withdraw"),
    AMOUNT("amount"),
    HOVER_BALANCE("banks.hover.balance"),
    HOVER_DEPOSIT("banks.hover.deposit"),
    HOVER_WITHDRAW("banks.hover.withdraw"),
    HOVER_NO_AMOUNT("banks.hover.no_amount"),
    BANK_USAGE("bank.usage"),
    BANK_INVALID_SUBCOMMAND("bank.invalid_subcommand"),
    NOT_ON_CLAN_LAND("not_on_clan_land"),
    DEPOSIT_MSG_PLAYER("deposit.message.player"),
    DEPOSIT_MSG_ANNOUNCE("deposit.message.announce"),
    WITHDRAW_MSG_PLAYER("withdraw.message.player"),
    WITHDRAW_MSG_ANNOUNCE("withdraw.message.announce"),
    DEPOSIT_ERR_PLAYER("deposit.error.player"),
    WITHDRAW_ERR_PLAYER("withdraw.error.player"),
    BANK_INVALID_AMOUNT("bank.invalid_amount"),
    PLAYER_NO_CLAN("player.no_clan"),
    TRANSACTION_DEPOSIT_PRE("transaction.deposit_pre"),
    TRANSACTION_WITHDRAW_PRE("transaction.withdraw_pre"),
    TRANSACTION_DEPOSIT("transaction.deposit"),
    TRANSACTION_WITHDRAW("transaction.withdraw"),
    TRANSACTION_VERBOSE_CLAN_ID("transaction.verbose.id"),
    SUCCESS_YES("success.yes"),
    SUCCESS_NO("success.no"),
    SUCCESS_PENDING("success.pending"),
    SUCCESS_DENIED("success.denied"),
    CANCELLED_YES("cancelled.yes"),
    CANCELLED_NO("cancelled.no");

    private static Properties properties;

    private final String s;

    Messages(String key) {
        s = key;
    }

    public static void setup(ClansBanks clansBanks, String locale) {
        properties = new Properties();
        final InputStream inputStream = (locale == null) ?
                clansBanks.getResource("messages.properties") :
                clansBanks.getResource("lang/messages" + locale + ".properties");
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(inputStream)));
            System.out.println("Loaded " + ((locale == null) ? "default" : "\"" + locale + "\"") + " lang file.");
        } catch (IOException | NullPointerException e) {
            try {
                properties.load(clansBanks.getResource("messages.properties"));
                System.out.println("Something went wrong, loading default lang.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            throw new IllegalArgumentException("Invalid region: " + locale, e);
        }
    }

    @Nullable
    public String get() {
        return properties.getProperty(s);
    }

    @Override
    public String toString() {
        final String s = get();
        return (s != null) ? s : "null";
    }

}
