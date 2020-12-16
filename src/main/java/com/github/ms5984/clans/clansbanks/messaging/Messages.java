package com.github.ms5984.clans.clansbanks.messaging;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public enum Messages {
    Message("");

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
        } catch (IOException e) {
            try {
                properties.load(clansBanks.getResource("messages.properties"));
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
}
