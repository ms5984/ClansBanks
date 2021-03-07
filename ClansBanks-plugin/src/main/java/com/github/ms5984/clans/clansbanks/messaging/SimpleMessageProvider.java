/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *
 *  This file is part of ClansBanks.
 *
 *  ClansBanks is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  ClansBanks is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.ms5984.clans.clansbanks.messaging;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import lombok.val;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

/**
 * A simple MessageProvider.
 */
public final class SimpleMessageProvider extends MessageProvider {
    private SimpleMessageProvider(Properties properties, ClansBanks clansBanks) {
        super(properties);
        register(clansBanks);
    }

    public static void setup(ClansBanks clansBanks, String locale) {
        val properties = new Properties();
        val inputStream = (locale == null) ?
                clansBanks.getResource("messages.properties") :
                clansBanks.getResource("lang/messages_" + locale + ".properties");
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(inputStream)));
            clansBanks.getLogger().info(() -> "Loaded " + ((locale == null) ? "default" : "\"" + locale + "\"") + " lang file.");
        } catch (IOException | NullPointerException e) {
            try {
                properties.load(clansBanks.getResource("messages.properties"));
                clansBanks.getLogger().info("Something went wrong, loading default lang.");
            } catch (IOException ioException) {
                throw new IllegalStateException("Unable to load messages from jar.", ioException);
            }
            throw new IllegalArgumentException("Invalid region: " + locale, e);
        }
        new SimpleMessageProvider(properties, clansBanks);
    }
}
