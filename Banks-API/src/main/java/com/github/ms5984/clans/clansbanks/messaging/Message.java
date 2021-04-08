/*
 *  This file is part of Banks-API.
 *
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.ms5984.clans.clansbanks.messaging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents plugin text that may be localized.
 */
public enum Message {
    BANKS_HEADER("banks.header"),
    CLANS_HELP_PREFIX("clans.help.prefix"),
    BANK_HELP_PREFIX("bank.help.prefix"),
    BANK_HELP_AMOUNT_COMMANDS("bank.help.amount_commands"),
    BANKS_CURRENT_BALANCE("banks.current_balance"),
    BANKS_COMMAND_LISTING("banks.command_listing"),
    AMOUNT("amount"),
    PERM("perm"),
    LEVEL("level"),
    BANKS_GREETING("banks.greeting"),
    BANKS_GREETING_HOVER("banks.greeting.hover"),
    HOVER_BALANCE("banks.hover.balance"),
    HOVER_DEPOSIT("banks.hover.deposit"),
    HOVER_WITHDRAW("banks.hover.withdraw"),
    HOVER_VIEW_LOG("banks.hover.view_log"),
    HOVER_SET_PERM("banks.hover.set_perm"),
    HOVER_NO_AMOUNT("banks.hover.no_amount"),
    VALID_LEVELS("valid.levels"),
    VALID_OPTIONS("valid.options"),
    INVALID_LEVEL("invalid.level"),
    SETTING_LEVEL("setting.level"),
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

    private final String s;

    Message(String key) {
        s = key;
    }

    @Nullable
    public String get() {
        return MessageProvider.getInstance().properties.getProperty(s);
    }

    @NotNull
    public String replace(Object... replacements) {
        int i = 0;
        String toString = toString();
        for (Object obj : replacements) {
            toString = toString.replaceAll("\\{" + i++ + "}", String.valueOf(obj));
        }
        return toString;
    }

    @Override
    public String toString() {
        final String s = get();
        return (s != null) ? s : "null";
    }

}
