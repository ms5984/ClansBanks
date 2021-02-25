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
import com.github.ms5984.clans.clansbanks.model.BankAction;
import com.github.ms5984.clans.clansbanks.model.BankLog;
import com.github.ms5984.clans.clansbanks.util.Permissions;
import com.github.sanctum.labyrinth.formatting.string.ColoredString;
import com.github.sanctum.labyrinth.library.TextLib;
import com.youtube.hempfest.clans.HempfestClans;
import com.youtube.hempfest.clans.util.StringLibrary;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.clans.util.events.CommandHelpEvent;
import com.youtube.hempfest.clans.util.events.SubCommandEvent;
import com.youtube.hempfest.clans.util.events.TabInsertEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.*;

public class BankManager implements Listener {

    private final TextLib textLib = TextLib.getInstance();
    private final String clans_prefix = new StringLibrary().getPrefix();

    @EventHandler
    private void onClansHelp(CommandHelpEvent e) {
        e.insert(Messages.CLANS_HELP_PREFIX + " " + Messages.BANK_HELP_PREFIX + " &fbalance");
        e.insert(Messages.CLANS_HELP_PREFIX + " " + Messages.BANK_HELP_PREFIX + " " + Messages.BANK_HELP_AMOUNT_COMMANDS.toString()
                .replace("{amount}", Messages.AMOUNT.toString()));
    }

    @EventHandler
    private void onBank(SubCommandEvent e) {
        final String[] args = e.getArgs();
        final int length = args.length;
        if (length >= 1) {
            if (!args[0].equalsIgnoreCase("bank")) {
                return;
            }
            e.setReturn(true);
            final Player sender = e.getSender();
            if (Permissions.BANKS_USE.not(sender)) {
                sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                return;
            }
            final Optional<Clan> optionalClan = testClan(sender);
            if (!optionalClan.isPresent()) return;
            final Clan clan = optionalClan.get();
            final Optional<ClanBank> testBank = optionalClan.map(ClansBanks.getAPI()::getBank);
            sendMessage(sender, clans_prefix + Messages.BANKS_HEADER);
            if (length == 1) { // "bank" print instructions
                final String[] split = Messages.BANKS_GREETING.toString().split("\\{0}");
                final String greetingHover = Messages.BANKS_GREETING_HOVER.toString();
                if (Permissions.BANKS_BALANCE.not(sender)) {
                    sender.spigot().sendMessage(textLib.textHoverable(
                            split[0], "&o" + sender.getName(), split[1],
                            greetingHover.substring(0, greetingHover.indexOf("\n"))
                                    .replace("{0}", clan.getClanTag()))
                    );
                } else {
                    sender.spigot().sendMessage(textLib.textRunnable(
                            split[0], "&o" + sender.getName(), split[1],
                            greetingHover.replace("{0}", clan.getClanTag()),
                            "clan bank balance")
                    );
                }
                sendMessage(sender, Messages.BANKS_COMMAND_LISTING.toString());
                final List<BaseComponent> textComponents = new ArrayList<>();
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
                if (BankAction.VIEW_LOG.testForPlayer(clan, sender)) {
                    sender.spigot().sendMessage(textLib.textSuggestable(
                            Messages.BANK_HELP_PREFIX + " ",
                            "&7viewlog", Messages.HOVER_VIEW_LOG.toString(),
                            "clan bank viewlog"
                    ));
                }
                if (BankAction.SET_PERM.testForPlayer(clan, sender)) {
                    sender.spigot().sendMessage(textLib.textSuggestable(
                            Messages.BANK_HELP_PREFIX + " ",
                            "&7setperm", Messages.HOVER_SET_PERM.toString(),
                            "clan bank viewlog"
                    ));
                }
                return;
            } else if (length == 2) { // "bank x" check if deposit/withdraw/balance/viewlog/setperm
                if (!testBank.isPresent()) return;
                final String arg = args[1];
                if (!arg.equalsIgnoreCase("balance")) {
                    if ("deposit".equalsIgnoreCase(arg)) {
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
                    } else if ("withdraw".equalsIgnoreCase(arg)) {
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
                    } else if ("viewlog".equalsIgnoreCase(arg)) {
                        // display log
                        if (!BankAction.VIEW_LOG.testForPlayer(clan, sender)) {
                            sendMessage(sender, Messages.PERM_NOT_PLAYER_ACTION.toString());
                            return;
                        }
                        sender.sendMessage(BankLog.getForClan(clan).getTransactions().stream().map(Object::toString).toArray(String[]::new));
                        return;
                    } else if ("viewperms".equalsIgnoreCase(arg)) {
                        // display perms
                        sendMessage(sender, "&6Bank perm levels:");
                        sendMessage(sender, "Balance&e=&7[&f" + BankAction.BALANCE.getValueInClan(clan) + "&7]");
                        sendMessage(sender, "Deposit&e=&7[&f" + BankAction.DEPOSIT.getValueInClan(clan) + "&7]");
                        sendMessage(sender, "Withdraw&e=&7[&f" + BankAction.WITHDRAW.getValueInClan(clan) + "&7]");
                        sendMessage(sender, "ViewLog&e=&7[&f" + BankAction.VIEW_LOG.getValueInClan(clan) + "&7]");
                        return;
                    }// msg usage (invalid subcommand)
                    sendMessage(sender, Messages.BANK_INVALID_SUBCOMMAND.toString());
                    return;
                }
                if (Permissions.BANKS_BALANCE.not(sender) || !BankAction.BALANCE.testForPlayer(clan, sender)) {
                    sendMessage(sender, Messages.PERM_NOT_PLAYER_COMMAND.toString());
                    return;
                }
                sendMessage(sender, Messages.BANKS_CURRENT_BALANCE.toString() + ": &a" + testBank.get().getBalance());
                return;
            } else if (length == 3) {
                final String arg1 = args[1].toLowerCase();
                switch (arg1) {
                    case "deposit":
                    case "withdraw":
                        try {
                            final BigDecimal amount = new BigDecimal(args[2]);
                            if (!testBank.isPresent()) return;
                            final ClanBank theBank = testBank.get();
                            if ("deposit".equals(arg1)) {
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
                            } else {
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
                            }
                        } catch (NumberFormatException exception) {
                            sendMessage(sender, Messages.BANK_INVALID_AMOUNT.toString());
                        }
                        return;
                    case "setperm":
                        final String arg2 = args[2].toLowerCase();
                        switch (arg2) {
                            case "balance":
                            case "deposit":
                            case "withdraw":
                            case "viewlog":
                            case "setperm":
                                sender.spigot().sendMessage(textLib.textHoverable(
                                        Messages.BANK_HELP_PREFIX + " setperm " + arg2,
                                        "&7<&c" + Messages.LEVEL + "&7>",
                                        Messages.VALID_LEVELS.toString()
                                ));
                                break;
                            default:
                                sender.spigot().sendMessage(textLib.textHoverable(
                                        Messages.BANK_HELP_PREFIX + " setperm &7<&c",
                                        Messages.PERM.toString(),
                                        "&7> &7<&f" + Messages.LEVEL + "&7>",
                                        "&6" + Messages.VALID_OPTIONS + "&7\n&o*&f balance&7\n&o*&f deposit&7\n&o*&f withdraw&7\n&o*&f viewlog"
                                ));
                        }
                        return;
                    default: // yell at the user and send usage msg
                }
            } else if (length == 4) {
                if (args[1].equalsIgnoreCase("setperm")) {
                    int level;
                    try {
                        level = Integer.parseInt(args[3]);
                    } catch (NumberFormatException ex) {
                        level = -1;
                    }
                    if (level < 0 || level > 3) {
                        sendMessage(sender, Messages.INVALID_LEVEL + " " + Messages.VALID_LEVELS);
                        return;
                    }
                    switch (args[2].toLowerCase()) {
                        // "&7Setting &6xyz &7level to &a" + level
                        case "balance":
                            sendMessage(sender, Messages.SETTING_LEVEL.toString()
                                    .replace("{0}", "balance")
                                    .replace("{1}", String.valueOf(level))
                            );
//                            sendMessage(sender, "&7Setting &6balance &7level to &a" + level);
                            BankAction.BALANCE.setRankForActionInClan(clan, level);
                            return;
                        case "deposit":
                            sendMessage(sender, Messages.SETTING_LEVEL.toString()
                                    .replace("{0}", "deposit")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.DEPOSIT.setRankForActionInClan(clan, level);
                            return;
                        case "withdraw":
                            sendMessage(sender, Messages.SETTING_LEVEL.toString()
                                    .replace("{0}", "withdraw")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.WITHDRAW.setRankForActionInClan(clan, level);
                            return;
                        case "setperm":
                            sendMessage(sender, Messages.SETTING_LEVEL.toString()
                                    .replace("{0}", "setperm")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.SET_PERM.setRankForActionInClan(clan, level);
                            return;
                        case "viewlog":
                            sendMessage(sender, Messages.SETTING_LEVEL.toString()
                                    .replace("{0}", "viewlog")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.VIEW_LOG.setRankForActionInClan(clan, level);
                            return;
                        default:
                            sendMessage(sender, Messages.BANK_USAGE.toString());
                            sender.spigot().sendMessage(textLib.textHoverable(
                                    Messages.BANK_HELP_PREFIX + " setperm ",
                                    "&7<&c" + Messages.PERM + "&7>",
                                    " &7<&f" + Messages.LEVEL + "&7>",
                                    "&6" + Messages.VALID_OPTIONS + "&7\n&o*&f balance&7\n&o*&f deposit&7\n&o*&f withdraw&7\n&o*&f viewlog"
                            ));
                            return;
                    }
                }
            }
            // msg usage (invalid subcommand)
            sendMessage(sender, Messages.BANK_INVALID_SUBCOMMAND.toString());
        }
    }

    @EventHandler
    private void onBankTab(TabInsertEvent e) {
        final String[] commandArgs = e.getCommandArgs();
        final int length = commandArgs.length;
        if (length == 1) {
            if (e.getArgs(1).contains("bank")) return;
            e.add(1, "bank");
        } else if (length == 2) {
            if (!commandArgs[0].equalsIgnoreCase("bank")) return;
            if (!e.getArgs(2).contains("balance")) {
                e.add(2, "balance");
            }
            if (!e.getArgs(2).contains("deposit")) {
                e.add(2, "deposit");
            }
            if (!e.getArgs(2).contains("withdraw")) {
                e.add(2, "withdraw");
            }
            if (!e.getArgs(2).contains("viewlog")) {
                e.add(2, "viewlog");
            }
            if (!e.getArgs(2).contains("setperm")) {
                e.add(2, "setperm");
            }
            if (!e.getArgs(2).contains("viewperms")) {
                e.add(2, "viewperms");
            }
        } else if (length == 3) {
            if (!commandArgs[0].equalsIgnoreCase("bank")) return;
            final String firstArg = commandArgs[1].toLowerCase();
            if ("deposit".equals(firstArg) || "withdraw".equals(firstArg)) {
                if (!e.getArgs(3).contains("10")) {
                    e.add(3, "10");
                }
            } else if ("setperm".equals(firstArg)) {
                if (!e.getArgs(3).contains("balance")) {
                    e.add(3, "balance");
                }
                if (!e.getArgs(3).contains("deposit")) {
                    e.add(3, "deposit");
                }
                if (!e.getArgs(3).contains("withdraw")) {
                    e.add(3, "withdraw");
                }
                if (!e.getArgs(3).contains("viewlog")) {
                    e.add(3, "viewlog");
                }
                if (!e.getArgs(3).contains("setperm")) {
                    e.add(3, "setperm");
                }
            }
        }
    }

    private Optional<Clan> testClan(Player sender) {
        final Optional<Clan> clanOptional = optionalClan(sender);
        if (!clanOptional.isPresent()) {
            sendMessage(sender, Messages.PLAYER_NO_CLAN.toString());
            return Optional.empty();
        }
        return clanOptional;
    }

    private Optional<Clan> optionalClan(Player player) {
        return Optional.ofNullable(HempfestClans.getInstance().playerClan.get(player.getUniqueId()))
                .map(s -> HempfestClans.clanManager(player));
    }

    private void sendMessage(Player player, String message) {
        if (message.isEmpty()) {
            return;
        }
        player.sendMessage(new ColoredString(message, ColoredString.ColorType.MC).toString());
    }
}
