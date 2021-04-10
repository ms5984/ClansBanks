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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * A simple MessageProvider.
 */
public final class SimpleMessageProvider extends MessageProvider {
    private SimpleMessageProvider(FileConfiguration fileConfiguration, ClansBanks clansBanks) {
        super(fileConfiguration);
        register(clansBanks);
    }

    public static void setup(ClansBanks clansBanks, String locale) {
        final YamlConfiguration defaults = new YamlConfiguration();
        val inputStream = (locale == null) ?
                clansBanks.getResource("messages.yml") :
                clansBanks.getResource("lang/messages_" + locale + ".yml");
        try {
            defaults.load(new InputStreamReader(Objects.requireNonNull(inputStream)));
            clansBanks.getLogger().info(() -> "Loaded " + ((locale == null) ? "default" : "\"" + locale + "\"") + " lang file.");
            final Optional<File> userConfig = Arrays
                    .stream(clansBanks.getDataFolder().listFiles(f -> f.isFile() && f.getName().endsWith(".yml")))
                    .filter(f -> {
                        final String fileName = f.getName();
                        if (!fileName.startsWith("messages"))
                        if (locale != null) {
                            if (fileName.startsWith("messages_")) {
                                // Check if it's the configured region
                                return fileName.equals("messages_" + locale + ".yml");
                            }
                        } else {
                            return fileName.equals("messages.yml");
                        }
                        return false;
                    }).findAny();
            if (!userConfig.isPresent()) {
                // Use internal defaults but also save file out for user to modify if desired.
                final StringBuilder newFilename = new StringBuilder("messages");
                if (locale != null) {
                    newFilename.append("_").append(locale);
                }
                newFilename.append(".yml");
                clansBanks.saveResource(newFilename.toString(), false);
                new SimpleMessageProvider(defaults, clansBanks);
            } else {
                // Load the user's config
                final YamlConfiguration userYmlConfig = YamlConfiguration.loadConfiguration(userConfig.get());
                userYmlConfig.addDefaults(defaults);
                new SimpleMessageProvider(userYmlConfig, clansBanks);
            }
        } catch (IOException | NullPointerException | InvalidConfigurationException e) {
            try {
                final YamlConfiguration alternate = new YamlConfiguration();
                alternate.load(new InputStreamReader(Objects.requireNonNull(clansBanks.getResource("messages.yml"))));
                clansBanks.getLogger().info("Something went wrong, loading default lang.");
                new SimpleMessageProvider(alternate, clansBanks);
            } catch (IOException | InvalidConfigurationException ioException) {
                throw new IllegalStateException("Unable to load messages from jar.", ioException);
            }
            throw new IllegalArgumentException("Invalid region: " + locale, e);
        }
    }
}
