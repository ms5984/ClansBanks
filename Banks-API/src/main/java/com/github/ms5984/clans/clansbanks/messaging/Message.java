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

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.EnumMap;

/**
 * Represents plugin text that may be localized.
 */
public enum Message {
    BANKS_HEADER,
    CLANS_HELP_PREFIX,
    BANK_HELP_PREFIX,
    @Key("bank.help.amount_commands") BANK_HELP_AMOUNT_COMMANDS,
    @Key("banks.current_balance") BANKS_CURRENT_BALANCE,
    @Key("banks.command_listing") BANKS_COMMAND_LISTING,
    AMOUNT,
    PERM,
    LEVEL,
    BANKS_GREETING,
    BANKS_GREETING_HOVER,
    HOVER_BALANCE,
    HOVER_DEPOSIT,
    HOVER_WITHDRAW,
    @Key("hover.view_log") HOVER_VIEW_LOG,
    @Key("hover.set_perm") HOVER_SET_PERM,
    @Key("hover.no_amount") HOVER_NO_AMOUNT,
    VALID_LEVELS,
    VALID_OPTIONS,
    INVALID_LEVEL,
    SETTING_LEVEL,
    BANK_USAGE,
    @Key("bank.invalid_subcommand") BANK_INVALID_SUBCOMMAND,
    @Key("not_on_clan_land") NOT_ON_CLAN_LAND,
    @Key("deposit.message.player") DEPOSIT_MSG_PLAYER,
    @Key("deposit.message.announce") DEPOSIT_MSG_ANNOUNCE,
    @Key("withdraw.message.player") WITHDRAW_MSG_PLAYER,
    @Key("withdraw.message.announce") WITHDRAW_MSG_ANNOUNCE,
    @Key("deposit.error.player") DEPOSIT_ERR_PLAYER,
    @Key("withdraw.error.player") WITHDRAW_ERR_PLAYER,
    @Key("bank.invalid_amount") BANK_INVALID_AMOUNT,
    @Key("player.no_clan") PLAYER_NO_CLAN,
    @Key("transaction.deposit_pre") TRANSACTION_DEPOSIT_PRE,
    @Key("transaction.deposit_pre_cancelled") TRANSACTION_DEPOSIT_PRE_CANCELLED,
    @Key("transaction.withdraw_pre") TRANSACTION_WITHDRAW_PRE,
    @Key("transaction.withdraw_pre_cancelled") TRANSACTION_WITHDRAW_PRE_CANCELLED,
    TRANSACTION_DEPOSIT,
    TRANSACTION_WITHDRAW,
    @Key("transaction.verbose.id") TRANSACTION_VERBOSE_CLAN_ID,
    @Key("transaction.success.pre") PRETRANSACTION_PENDING,
    @Key("transaction.failure.pre") PRETRANSACTION_FAILURE,
    TRANSACTION_SUCCESS,
    TRANSACTION_FAILED,
    @Key("permissions.not.player.command") PERM_NOT_PLAYER_COMMAND,
    @Key("permissions.not.player.action") PERM_NOT_PLAYER_ACTION;

    private static final EnumMap<Message, String> RESOLVED_KEYS = new EnumMap<>(Message.class);
    private final String s;

    Message(String key) {
        s = key;
    }
    Message() {
        this("blah");
    }

    @Nullable
    public String get() {
        final String key = RESOLVED_KEYS.computeIfAbsent(this, m -> {
            try {
                final Field field = getClass().getField(m.name());
                field.setAccessible(true);
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation.annotationType().isInstance(Key.class)) {
                        return ((Key) annotation).value();
                    }
                }
                // convert MESSAGE_EXAMPLE into message.example
                return m.name().toLowerCase().replaceAll("_", ".");
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("Impossible.", e);
            }
        });
        return MessageProvider.getInstance().properties.getProperty(key);
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

    /**
     * An annotation to detail messages whose enum signature does not
     * map naturally with the format MESSAGE_HERE -&gt; message.here
     * that is used for key resolution.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Key {
        /**
         * A custom key.
         *
         * @return custom key
         */
        @NotNull String value();
    }

}
