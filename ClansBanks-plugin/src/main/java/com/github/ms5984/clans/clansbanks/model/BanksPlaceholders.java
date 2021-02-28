package com.github.ms5984.clans.clansbanks.model;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.youtube.hempfest.clans.HempfestClans;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

public final class BanksPlaceholders extends PlaceholderExpansion {

    private final Plugin plugin;
    private final BanksAPI api;

    public BanksPlaceholders(ClansBanks plugin) {
        this.plugin = plugin;
        this.api = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "clansbanks";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        final Optional<Player> optionalPlayer = Optional.ofNullable(player);
        return optionalPlayer.map(p -> {
            if ("balance".equals(params)) {
                final Optional<BigDecimal> optionalBalance = Optional.ofNullable(HempfestClans.getInstance().playerClan.get(player.getUniqueId()))
                        .map(s -> HempfestClans.clanManager(player)).map(api::getBank).map(ClanBank::getBalance);
                if (optionalBalance.isPresent()) {
                    return optionalBalance.get().toString();
                }
            }
            return null;
        }).orElse(null);
    }
}
