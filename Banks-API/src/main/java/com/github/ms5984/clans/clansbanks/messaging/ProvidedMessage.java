/*
 *  This file is part of Banks-API.
 *
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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
 * Represents a message that may be provided.
 */
public interface ProvidedMessage {
    /**
     * Get this message, if present.
     *
     * @return message if present or null
     */
    @Nullable String get();

    /**
     * Replace simple placeholders within the localized messages.
     *
     * @param replacements Objects that will replace "{0}", "{1}"...
     * @return message with replacements
     */
    @NotNull
    default String replace(Object... replacements) {
        int i = 0;
        String toString = toString();
        for (Object obj : replacements) {
            toString = toString.replaceAll("\\{" + i++ + "}", String.valueOf(obj));
        }
        return toString;
    }
}
