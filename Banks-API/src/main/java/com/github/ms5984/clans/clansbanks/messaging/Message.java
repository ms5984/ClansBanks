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
    // Clans-related messages
    @Section("clans") CLANS_HELP_PREFIX("help-prefix"),
    @Section("clans") NOT_ON_CLAN_LAND,
    @Section("clans") PLAYER_NO_CLAN,
    // Banks-related messages
    BANKS_HEADER("header"),
    @Dot_Replace({0}) HELP_PREFIX,
    @Dot_Replace({0}) HELP_AMOUNT_COMMANDS,
    CURRENT_BALANCE,
    COMMAND_LISTING,
    USAGE,
    GREETING,
    GREETING_HOVER,
    INVALID_SUBCOMMAND,
    INVALID_AMOUNT,
    @Dot_Replace({0}) HOVER_BALANCE,
    @Dot_Replace({0}) HOVER_DEPOSIT,
    @Dot_Replace({0}) HOVER_WITHDRAW,
    @Dot_Replace({0}) HOVER_VIEW_LOG,
    @Dot_Replace({0}) HOVER_SET_PERM,
    @Dot_Replace({0}) HOVER_NO_AMOUNT,
    VALID_OPTIONS("hover.valid-options-header"),
    @SubSection("words") AMOUNT,
    @SubSection("words") PERM,
    @SubSection("words") LEVEL,
    VALID_LEVELS("levels.valid"),
    INVALID_LEVEL("levels.invalid"),
    SETTING_LEVEL("levels.setting"),
    @Dot_Replace({0,1}) DEPOSIT_MESSAGE_PLAYER,
    @Dot_Replace({0,1}) DEPOSIT_MESSAGE_ANNOUNCE,
    @Dot_Replace({0,1}) DEPOSIT_ERROR_PLAYER,
    @Dot_Replace({0,1}) WITHDRAW_MESSAGE_PLAYER,
    @Dot_Replace({0,1}) WITHDRAW_MESSAGE_ANNOUNCE,
    @Dot_Replace({0,1}) WITHDRAW_ERROR_PLAYER,
    // Banks event-related messages
    @Section("events") @Dot_Replace({0}) TRANSACTION_DEPOSIT_PRE,
    @Section("events") @Dot_Replace({0}) TRANSACTION_DEPOSIT_PRE_CANCELLED,
    @Section("events") @Dot_Replace({0}) TRANSACTION_WITHDRAW_PRE,
    @Section("events") @Dot_Replace({0}) TRANSACTION_WITHDRAW_PRE_CANCELLED,
    @Section("events") @Dot_Replace({0}) TRANSACTION_DEPOSIT,
    @Section("events") @Dot_Replace({0}) TRANSACTION_WITHDRAW,
    @Section("events") @Dot_Replace({0}) TRANSACTION_VERBOSE_CLAN_ID,
    @Section("events") @Dot_Replace({0}) TRANSACTION_SUCCESS,
    @Section("events") @Dot_Replace({0}) TRANSACTION_FAILED,
    @Section("events") @SubSection("transaction") PRETRANSACTION_PENDING,
    @Section("events") @SubSection("transaction") PRETRANSACTION_FAILURE,
    // No permission messages
    @Section("no-permission") NO_PERM_PLAYER_COMMAND("player.command"),
    @Section("no-permission") NO_PERM_PLAYER_ACTION("player.action");

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
                    if (annotation.annotationType().isInstance(Section.class)) {
                        return ((Section) annotation).value();
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
     * An annotation to detail message location
     * relative to the root node.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Section {
        /**
         * A section key.
         *
         * @return section key
         */
        @NotNull String value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SubSection {
        @NotNull String value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Dot_Replace {
        int[] value();
    }

}
