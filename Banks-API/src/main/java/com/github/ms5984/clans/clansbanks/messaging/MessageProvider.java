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

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;

/**
 * Provide the Properties backing for the Message class.
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
