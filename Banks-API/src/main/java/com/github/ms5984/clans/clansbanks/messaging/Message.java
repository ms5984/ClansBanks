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
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents plugin text that may be localized.
 */
public enum Message implements ProvidedMessage {
    // Clans-related messages
    @Section("clans") CLANS_HELP_PREFIX("help-prefix"),
    @Section("clans") NOT_ON_CLAN_LAND,
    @Section("clans") PLAYER_NO_CLAN,
    // Banks-related messages
    BANKS_HEADER("header"),
    @Dot_Replace HELP_PREFIX,
    @Dot_Replace HELP_AMOUNT_COMMANDS,
    CURRENT_BALANCE,
    COMMAND_LISTING,
    USAGE,
    GREETING,
    GREETING_HOVER,
    INVALID_SUBCOMMAND,
    INVALID_AMOUNT,
    @Dot_Replace HOVER_BALANCE,
    @Dot_Replace HOVER_DEPOSIT,
    @Dot_Replace HOVER_WITHDRAW,
    @Dot_Replace HOVER_VIEW_LOG,
    @Dot_Replace HOVER_SET_PERM,
    @Dot_Replace HOVER_NO_AMOUNT,
    VALID_OPTIONS("hover.valid-options-header"),
    @SubSection("words") AMOUNT,
    @SubSection("words") PERM,
    @SubSection("words") LEVEL,
    VALID_LEVELS("levels.valid"),
    INVALID_LEVEL("levels.invalid"),
    SETTING_LEVEL("levels.setting"),
    @Dot_Replace(1) DEPOSIT_MESSAGE_PLAYER,
    @Dot_Replace(1) DEPOSIT_MESSAGE_ANNOUNCE,
    @Dot_Replace(1) DEPOSIT_ERROR_PLAYER,
    @Dot_Replace(1) WITHDRAW_MESSAGE_PLAYER,
    @Dot_Replace(1) WITHDRAW_MESSAGE_ANNOUNCE,
    @Dot_Replace(1) WITHDRAW_ERROR_PLAYER,
    // Banks event-related messages
    @Section("events") @Dot_Replace TRANSACTION_DEPOSIT_PRE,
    @Section("events") @Dot_Replace TRANSACTION_DEPOSIT_PRE_CANCELLED,
    @Section("events") @Dot_Replace TRANSACTION_WITHDRAW_PRE,
    @Section("events") @Dot_Replace TRANSACTION_WITHDRAW_PRE_CANCELLED,
    @Section("events") @Dot_Replace TRANSACTION_DEPOSIT,
    @Section("events") @Dot_Replace TRANSACTION_WITHDRAW,
    @Section("events") @Dot_Replace TRANSACTION_VERBOSE_CLAN_ID,
    @Section("events") @Dot_Replace TRANSACTION_SUCCESS,
    @Section("events") @Dot_Replace TRANSACTION_FAILED,
    @Section("events") @SubSection("transaction") PRETRANSACTION_PENDING,
    @Section("events") @SubSection("transaction") PRETRANSACTION_FAILURE,
    // No permission messages
    @Section("no-permission") NO_PERM_PLAYER_COMMAND("player.command"),
    @Section("no-permission") NO_PERM_PLAYER_ACTION("player.action");

    private final String key;

    Message(String key) {
        // convert MESSAGE_EXAMPLE into message-example
        String resolvedPrefix = "banks";
        final StringBuilder resolvedSubSection = new StringBuilder(".");
        try {
            final Field field = getDeclaringClass().getField(name());
            field.setAccessible(true);
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType == Section.class) {
                    resolvedPrefix = ((Section) annotation).value();
                } else if (annotationType == SubSection.class) {
                    resolvedSubSection.append(((SubSection) annotation).value()).append(".");
                }
            }
            this.key = "Messages." + resolvedPrefix + resolvedSubSection + key;
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Impossible.", e);
        }
    }
    Message() {
        // convert MESSAGE_EXAMPLE into message-example
        final AtomicReference<String> resolvedKey = new AtomicReference<>(name().toLowerCase().replaceAll("_", "-"));
        String resolvedPrefix = "banks";
        final StringBuilder resolvedSubSection = new StringBuilder(".");
        try {
            final Field field = getClass().getField(name());
            field.setAccessible(true);
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType == Section.class) {
                    resolvedPrefix = ((Section) annotation).value();
                } else if (annotationType == SubSection.class) {
                    resolvedSubSection.append(((SubSection) annotation).value()).append(".");
                } else if (annotationType == Dot_Replace.class) {
                    for (int i = 0; i <= ((Dot_Replace) annotation).value(); ++i) {
                        resolvedKey.updateAndGet(k -> k.replaceFirst("-", "."));
                    }
                }
            }
            this.key = "Messages." + resolvedPrefix + resolvedSubSection + resolvedKey.get();
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Impossible.", e);
        }
    }

    @Override
    @Nullable
    public String get() {
        return MessageProvider.getInstance().fileConfiguration.getString(key);
    }

    @Override
    public String toString() {
        return String.valueOf(get());
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
        int value() default 0;
    }

}
