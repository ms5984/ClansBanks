package com.github.ms5984.clans.clansbanks.gui.util;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public enum MenuText {
    GLOBAL_EXIT("global.EXIT"),
    MAIN_TITLE("main.title");

    private static Properties properties;

    private final String key;

    MenuText(String key) {
        this.key = key;
    }

    public static void setup(ClansBanks clansBanks, String locale) {
        properties = new Properties();
        final InputStream inputStream = (locale == null) ?
                clansBanks.getResource("default_menu.properties") :
                clansBanks.getResource("menu/" + locale + "_menu.properties");
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(inputStream)));
            System.out.println("Loaded " + ((locale == null) ? "default" : "\"" + locale + "\"") + " lang file for menu.");
        } catch (IOException | NullPointerException e) {
            try {
                properties.load(clansBanks.getResource("default_menu.properties"));
                System.out.println("Something went wrong, loading default menu.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            throw new IllegalArgumentException("Invalid region: " + locale, e);
        }
    }

    @Nullable
    public String getRaw() {
        return properties.getProperty(key);
    }

    @Override
    public String toString() {
        final String s = new ColoredString(getRaw(), ColoredString.ColorType.MC).toString();
        return (s != null) ? s : "null";
    }

}
