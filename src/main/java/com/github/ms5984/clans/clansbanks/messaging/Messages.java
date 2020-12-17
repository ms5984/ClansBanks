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
    BANK_HELP_PREFIX("bank.help.prefix"),
    BANK_HELP_BALANCE("bank.help.balance"),
    BANK_HELP_AMOUNT_COMMANDS("bank.help.amount_commands"),
    BANKS_CURRENT_BALANCE("banks.current_balance"),
    BANKS_COMMAND_LIST("banks.command_list"),
    DEPOSIT("deposit"),
    WITHDRAW("withdraw"),
    AMOUNT("amount"),
    HOVER_DEPOSIT("banks.hover.deposit"),
    HOVER_WITHDRAW("banks.hover.withdraw");

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
