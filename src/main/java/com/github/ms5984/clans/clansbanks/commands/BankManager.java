/*
 *  Copyright 2020 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
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
package com.github.ms5984.clans.clansbanks.commands;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import com.github.ms5984.clans.clansbanks.util.Permissions;
import com.github.ms5984.clans.clansbanks.util.TextLib;
import com.youtube.hempfest.clans.HempfestClans;
import com.youtube.hempfest.clans.util.StringLibrary;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.clans.util.events.CommandHelpEvent;
import com.youtube.hempfest.clans.util.events.SubCommandEvent;
import com.youtube.hempfest.clans.util.events.TabInsertEvent;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BankManager implements Listener {

    private final TextLib textLib = TextLib.getInstance();
    private final List<String> tab2 = new LinkedList<>(Arrays.asList("balance", "deposit", "withdraw"));
    private final String clans_prefix = new StringLibrary().getPrefix();

    private Optional<Clan> optionalClan(Player player) {
        return Optional.ofNullable(HempfestClans.getInstance().playerClan.get(player.getUniqueId()))
                .map(s -> HempfestClans.clanManager(player));
    }

    @EventHandler
    private void onClansHelp(CommandHelpEvent e) {
        e.insert(Messages.CLANS_HELP_PREFIX + " " + Messages.BANK_HELP_PREFIX + " &fbalance");
        e.insert(Messages.CLANS_HELP_PREFIX + " " + Messages.BANK_HELP_PREFIX + " " + Messages.BANK_HELP_AMOUNT_COMMANDS.toString()
                .replace("{amount}", Messages.AMOUNT.toString()));
    }

    @EventHandler
    private void onBank(SubCommandEvent e) {
        final int length = e.getArgs().length;
        if (length >= 1) {
            if (!e.getArgs()[0].equalsIgnoreCase("bank")) {
                return;
            }
            e.setReturn(true);
            final Player sender = e.getSender();
            if (Permissions.BANKS_USE.not(sender)) {
                sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                return;
            }
            sendMessage(sender, clans_prefix + Messages.BANKS_HEADER);
            switch (length) {
                case 1: // "bank" print instructions
                    final Optional<Clan> optionalClan = optionalClan(sender);
                    if (!optionalClan.isPresent()) {
                        sendMessage(sender, Messages.PLAYER_NO_CLAN.toString());
                        return;
                    }
                    final String[] split = Messages.BANKS_GREETING.toString().split("\\{0}");
                    final String greetingHover = Messages.BANKS_GREETING_HOVER.toString();
                    if (Permissions.BANKS_BALANCE.not(sender)) {
                        sender.spigot().sendMessage(textLib.textHoverable(
                                split[0], "&o" + sender.getName(), split[1],
//                                    greetingHover.replaceFirst("(\\\\n).*", "")
                                greetingHover.substring(0, greetingHover.indexOf("\n"))
                                        .replace("{0}", optionalClan.get().getClanTag()))
                        );
                    } else {
                        sender.spigot().sendMessage(textLib.textRunnable(
                                split[0], "&o" + sender.getName(), split[1],
                                greetingHover.replace("{0}", optionalClan.get().getClanTag()),
                                "clan bank balance")
                        );
                    }
                    sendMessage(sender, Messages.BANKS_COMMAND_LISTING.toString());
                    final List<BaseComponent> textComponents = new LinkedList<>();
                    sender.spigot().sendMessage(textLib.textSuggestable(Messages.BANK_HELP_PREFIX + " ",
                            "&7balance", Messages.HOVER_BALANCE.toString(),
                            "clan bank balance"));
                    textComponents.add(textLib.textSuggestable(
                            Messages.BANK_HELP_PREFIX + " &f<",
                            "&adeposit", Messages.HOVER_DEPOSIT.toString(),
                            "clan bank deposit 1"
                    ));
                    textComponents.add(textLib.textSuggestable(
                            "&7,",
                            "&cwithdraw", Messages.HOVER_WITHDRAW.toString(),
                            "clan bank withdraw 1"
                    ));
                    textComponents.add(new ColoredString("&f> <&7" + Messages.AMOUNT + "&f>",
                            ColoredString.ColorType.MC_COMPONENT).toComponent());
                    sender.spigot().sendMessage(textComponents.toArray(new BaseComponent[0]));
                    return;
                case 2: // "bank x" check if deposit/withdraw/balance
                    final ClanBank bank = testClan(sender);
                    if (bank == null) return;
                    final String arg = e.getArgs()[1];
                    if (!arg.equalsIgnoreCase("balance")) {
                        switch (arg.toLowerCase()) {
                            case "deposit":
                                if (Permissions.BANKS_DEPOSIT.not(sender)) {
                                    sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                                    return;
                                }
                                // msg usage (need amount param)
                                sendMessage(sender, Messages.BANK_USAGE.toString());
                                sender.spigot().sendMessage(textLib.textHoverable(
                                        Messages.BANK_HELP_PREFIX + " ",
                                        "&7<&fdeposit&7>",
                                        " ",
                                        "&7<&c" + Messages.AMOUNT + "&7>",
                                        Messages.HOVER_DEPOSIT.toString(),
                                        Messages.HOVER_NO_AMOUNT.toString()
                                ));
                                return;
                            case "withdraw":
                                if (Permissions.BANKS_WITHDRAW.not(sender)) {
                                    sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                                    return;
                                }
                                // msg usage (need amount param)
                                sendMessage(sender, Messages.BANK_USAGE.toString());
                                sender.spigot().sendMessage(textLib.textHoverable(
                                        Messages.BANK_HELP_PREFIX + " ",
                                        "&7<&fwithdraw&7>",
                                        " ",
                                        "&7<&c" + Messages.AMOUNT + "&7>",
                                        Messages.HOVER_WITHDRAW.toString(),
                                        Messages.HOVER_NO_AMOUNT.toString()
                                ));
                                return;
                            default:
                                // msg usage (invalid subcommand)
                                sendMessage(sender, Messages.BANK_INVALID_SUBCOMMAND.toString());
                                return;
                        }
                    }
                    if (Permissions.BANKS_BALANCE.not(sender)) {
                        sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                        return;
                    }
                    sendMessage(sender,Messages.BANKS_CURRENT_BALANCE.toString()
                            .replace("{0}", bank.getBalance().toPlainString()));
                    return;
                case 3:
                    final String arg1 = e.getArgs()[1].toLowerCase();
                    switch (arg1) {
                        case "deposit":
                        case "withdraw":
                            try {
                                final BigDecimal amount = new BigDecimal(e.getArgs()[2]);
                                final ClanBank theBank = testClan(sender);
                                if (theBank == null) return;
                                switch (arg1) {
                                    case "deposit":
                                        if (Permissions.BANKS_DEPOSIT.not(sender)) {
                                            sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                                            return;
                                        }
                                        if (theBank.deposit(sender, amount)) {
                                            sendMessage(sender, Messages.DEPOSIT_MSG_PLAYER.toString()
                                            .replace("{0}", amount.toString()));
                                        } else {
                                            sendMessage(sender, Messages.DEPOSIT_ERR_PLAYER.toString()
                                                    .replace("{0}", amount.toString()));
                                        }
                                        break;
                                    case "withdraw":
                                        if (Permissions.BANKS_WITHDRAW.not(sender)) {
                                            sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                                            return;
                                        }
                                        if (theBank.withdraw(sender, amount)) {
                                            sendMessage(sender, Messages.WITHDRAW_MSG_PLAYER.toString()
                                                    .replace("{0}", amount.toString()));
                                        } else {
                                            sendMessage(sender, Messages.WITHDRAW_ERR_PLAYER.toString()
                                                    .replace("{0}", amount.toString()));
                                        }
                                        break;
                                }
                            } catch (NumberFormatException exception) {
                                sendMessage(sender, Messages.BANK_INVALID_AMOUNT.toString());
                            }
                            return;
                        default: // yell at the user and send usage msg
                            break;
                    }
                    break;
            }
            // msg usage (invalid subcommand)
            sendMessage(sender, Messages.BANK_INVALID_SUBCOMMAND.toString());
        }
    }

    @EventHandler
    private void onBankTab(TabInsertEvent e) {
        final String[] commandArgs = e.getCommandArgs();
        final int length = commandArgs.length;
        switch (length) {
            case 1:
                if (e.getArgs(1).contains("bank")) return;
                e.add(1, "bank");
                break;
            case 2:
                if (!commandArgs[0].equalsIgnoreCase("bank")) return;
                for (String suggest : tab2) {
                    if (!e.getArgs(2).contains(suggest)) {
                        e.add(2, suggest);
                    }
                }
                break;
            case 3:
                if (!commandArgs[0].equalsIgnoreCase("bank")) return;
                if (tab2.subList(1, 3).contains(commandArgs[1].toLowerCase())) {
                    if (e.getArgs(3).contains("10")) return;
                    e.add(3, "10");
                }
        }
    }

    private ClanBank testClan(Player sender) {
        final Optional<Clan> clanOptional = optionalClan(sender);
        if (!clanOptional.isPresent()) {
            sendMessage(sender, Messages.PLAYER_NO_CLAN.toString());
            return null;
        }
        return ClansBanks.getAPI().getBank(clanOptional.get());
    }

    private void sendMessage(Player player, String message) {
        if (message.isEmpty()) {
            return;
        }
        player.sendMessage(new ColoredString(message, ColoredString.ColorType.MC).toString());
    }
}
