/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;

/**
 * Provide the Properties backing object for the Message class.
 */
public abstract class MessageProvider {
    private static MessageProvider instance;
    protected final Properties properties;

    protected MessageProvider(Properties properties) {
        this.properties = properties;
    }

    protected void register(JavaPlugin plugin) {
        register(plugin, ServicePriority.Normal);
    }

    @SuppressWarnings("SameParameterValue")
    protected void register(JavaPlugin plugin, ServicePriority priority) {
        Bukkit.getServicesManager().register(MessageProvider.class, this, plugin, priority);
    }

    protected static MessageProvider getInstance() {
        if (instance != null) return instance;
        final MessageProvider provider = Bukkit.getServicesManager().load(MessageProvider.class);
        if (provider == null) throw new IllegalStateException("Banks Free is not loaded!");
        return instance = provider;
    }
}
