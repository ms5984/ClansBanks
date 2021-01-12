/*
 *  Copyright 2020 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  This file is part of ClansBanks.
 *
 *  ClansBanks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  ClansBanks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
    BANKS_COMMAND_LISTING("banks.command_listing"),
    AMOUNT("amount"),
    BANKS_GREETING("banks.greeting"),
    BANKS_GREETING_HOVER("banks.greeting.hover"),
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
    TRANSACTION_DEPOSIT_PRE_CANCELLED("transaction.deposit_pre_cancelled"),
    TRANSACTION_WITHDRAW_PRE("transaction.withdraw_pre"),
    TRANSACTION_WITHDRAW_PRE_CANCELLED("transaction.withdraw_pre_cancelled"),
    TRANSACTION_DEPOSIT("transaction.deposit"),
    TRANSACTION_WITHDRAW("transaction.withdraw"),
    TRANSACTION_VERBOSE_CLAN_ID("transaction.verbose.id"),
    PRETRANSACTION_PENDING("transaction.success.pre"),
    PRETRANSACTION_FAILURE("transaction.failure.pre"),
    TRANSACTION_SUCCESS("transaction.success"),
    TRANSACTION_FAILED("transaction.failed"),
    PERM_NOT_PLAYER_COMMAND("permissions.not.player.command"),
    PERM_NOT_PLAYER_ACTION("permissions.not.player.action");

    private static Properties properties;

    private final String s;

    Messages(String key) {
        s = key;
    }

    public static void setup(ClansBanks clansBanks, String locale) {
        properties = new Properties();
        final InputStream inputStream = (locale == null) ?
                clansBanks.getResource("messages.properties") :
                clansBanks.getResource("lang/messages_" + locale + ".properties");
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
