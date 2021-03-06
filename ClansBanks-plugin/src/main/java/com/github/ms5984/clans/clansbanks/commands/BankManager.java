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
import com.github.ms5984.clans.clansbanks.messaging.Message;
import com.github.ms5984.clans.clansbanks.model.BankAction;
import com.github.ms5984.clans.clansbanks.model.BankLog;
import com.github.ms5984.clans.clansbanks.util.BanksPermission;
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
        e.insert(Message.CLANS_HELP_PREFIX + " " + Message.BANK_HELP_PREFIX + " &fbalance");
        e.insert(Message.CLANS_HELP_PREFIX + " " + Message.BANK_HELP_PREFIX + " " + Message.BANK_HELP_AMOUNT_COMMANDS.toString()
                .replace("{amount}", Message.AMOUNT.toString()));
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
            if (BanksPermission.USE.not(sender)) {
                sendMessage(sender, Message.PERM_NOT_PLAYER_COMMAND.toString());
                return;
            }
            final Optional<Clan> optionalClan = testClan(sender);
            if (!optionalClan.isPresent()) return;
            final Clan clan = optionalClan.get();
            final Optional<ClanBank> testBank = optionalClan.map(ClansBanks.getAPI()::getBank);
            sendMessage(sender, clans_prefix + Message.BANKS_HEADER);
            if (length == 1) { // "bank" print instructions
                final String[] split = Message.BANKS_GREETING.toString().split("\\{0}");
                final String greetingHover = Message.BANKS_GREETING_HOVER.toString();
                if (BanksPermission.USE_BALANCE.not(sender)) {
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
                sendMessage(sender, Message.BANKS_COMMAND_LISTING.toString());
                final List<BaseComponent> textComponents = new ArrayList<>();
                sender.spigot().sendMessage(textLib.textSuggestable(Message.BANK_HELP_PREFIX + " ",
                        "&7balance", Message.HOVER_BALANCE.toString(),
                        "clan bank balance"));
                textComponents.add(textLib.textSuggestable(
                        Message.BANK_HELP_PREFIX + " &f<",
                        "&adeposit", Message.HOVER_DEPOSIT.toString(),
                        "clan bank deposit 1"
                ));
                textComponents.add(textLib.textSuggestable(
                        "&7,",
                        "&cwithdraw", Message.HOVER_WITHDRAW.toString(),
                        "clan bank withdraw 1"
                ));
                textComponents.add(new ColoredString("&f> <&7" + Message.AMOUNT + "&f>",
                        ColoredString.ColorType.MC_COMPONENT).toComponent());
                sender.spigot().sendMessage(textComponents.toArray(new BaseComponent[0]));
                if (BankAction.VIEW_LOG.testForPlayer(clan, sender)) {
                    sender.spigot().sendMessage(textLib.textSuggestable(
                            Message.BANK_HELP_PREFIX + " ",
                            "&7viewlog", Message.HOVER_VIEW_LOG.toString(),
                            "clan bank viewlog"
                    ));
                }
                if (BankAction.SET_PERM.testForPlayer(clan, sender)) {
                    sender.spigot().sendMessage(textLib.textSuggestable(
                            Message.BANK_HELP_PREFIX + " ",
                            "&7setperm", Message.HOVER_SET_PERM.toString(),
                            "clan bank viewlog"
                    ));
                }
                return;
            } else if (length == 2) { // "bank x" check if deposit/withdraw/balance/viewlog/setperm
                if (!testBank.isPresent()) return;
                final String arg = args[1];
                if (!arg.equalsIgnoreCase("balance")) {
                    if ("deposit".equalsIgnoreCase(arg)) {
                        if (BanksPermission.USE_DEPOSIT.not(sender)) {
                            sendMessage(sender, Message.PERM_NOT_PLAYER_COMMAND.toString());
                            return;
                        }
                        // msg usage (need amount param)
                        sendMessage(sender, Message.BANK_USAGE.toString());
                        sender.spigot().sendMessage(textLib.textHoverable(
                                Message.BANK_HELP_PREFIX + " ",
                                "&7<&fdeposit&7>",
                                " ",
                                "&7<&c" + Message.AMOUNT + "&7>",
                                Message.HOVER_DEPOSIT.toString(),
                                Message.HOVER_NO_AMOUNT.toString()
                        ));
                        return;
                    } else if ("withdraw".equalsIgnoreCase(arg)) {
                        if (BanksPermission.USE_WITHDRAW.not(sender)) {
                            sendMessage(sender, Message.PERM_NOT_PLAYER_COMMAND.toString());
                            return;
                        }
                        // msg usage (need amount param)
                        sendMessage(sender, Message.BANK_USAGE.toString());
                        sender.spigot().sendMessage(textLib.textHoverable(
                                Message.BANK_HELP_PREFIX + " ",
                                "&7<&fwithdraw&7>",
                                " ",
                                "&7<&c" + Message.AMOUNT + "&7>",
                                Message.HOVER_WITHDRAW.toString(),
                                Message.HOVER_NO_AMOUNT.toString()
                        ));
                        return;
                    } else if ("viewlog".equalsIgnoreCase(arg)) {
                        // display log
                        if (!BankAction.VIEW_LOG.testForPlayer(clan, sender)) {
                            sendMessage(sender, Message.PERM_NOT_PLAYER_ACTION.toString());
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
                    sendMessage(sender, Message.BANK_INVALID_SUBCOMMAND.toString());
                    return;
                }
                if (BanksPermission.USE_BALANCE.not(sender) || !BankAction.BALANCE.testForPlayer(clan, sender)) {
                    sendMessage(sender, Message.PERM_NOT_PLAYER_COMMAND.toString());
                    return;
                }
                sendMessage(sender, Message.BANKS_CURRENT_BALANCE.toString() + ": &a" + testBank.get().getBalance());
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
                                if (BanksPermission.USE_DEPOSIT.not(sender)) {
                                    sendMessage(sender, Message.PERM_NOT_PLAYER_COMMAND.toString());
                                    return;
                                }
                                if (theBank.deposit(sender, amount)) {
                                    sendMessage(sender, Message.DEPOSIT_MSG_PLAYER.toString()
                                            .replace("{0}", amount.toString()));
                                } else {
                                    sendMessage(sender, Message.DEPOSIT_ERR_PLAYER.toString()
                                            .replace("{0}", amount.toString()));
                                }
                            } else {
                                if (BanksPermission.USE_WITHDRAW.not(sender)) {
                                    sendMessage(sender, Message.PERM_NOT_PLAYER_COMMAND.toString());
                                    return;
                                }
                                if (theBank.withdraw(sender, amount)) {
                                    sendMessage(sender, Message.WITHDRAW_MSG_PLAYER.toString()
                                            .replace("{0}", amount.toString()));
                                } else {
                                    sendMessage(sender, Message.WITHDRAW_ERR_PLAYER.toString()
                                            .replace("{0}", amount.toString()));
                                }
                            }
                        } catch (NumberFormatException exception) {
                            sendMessage(sender, Message.BANK_INVALID_AMOUNT.toString());
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
                                        Message.BANK_HELP_PREFIX + " setperm " + arg2,
                                        "&7<&c" + Message.LEVEL + "&7>",
                                        Message.VALID_LEVELS.toString()
                                ));
                                break;
                            default:
                                sender.spigot().sendMessage(textLib.textHoverable(
                                        Message.BANK_HELP_PREFIX + " setperm &7<&c",
                                        Message.PERM.toString(),
                                        "&7> &7<&f" + Message.LEVEL + "&7>",
                                        "&6" + Message.VALID_OPTIONS + "&7\n&o*&f balance&7\n&o*&f deposit&7\n&o*&f withdraw&7\n&o*&f viewlog"
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
                        sendMessage(sender, Message.INVALID_LEVEL + " " + Message.VALID_LEVELS);
                        return;
                    }
                    switch (args[2].toLowerCase()) {
                        // "&7Setting &6xyz &7level to &a" + level
                        case "balance":
                            sendMessage(sender, Message.SETTING_LEVEL.toString()
                                    .replace("{0}", "balance")
                                    .replace("{1}", String.valueOf(level))
                            );
//                            sendMessage(sender, "&7Setting &6balance &7level to &a" + level);
                            BankAction.BALANCE.setRankForActionInClan(clan, level);
                            return;
                        case "deposit":
                            sendMessage(sender, Message.SETTING_LEVEL.toString()
                                    .replace("{0}", "deposit")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.DEPOSIT.setRankForActionInClan(clan, level);
                            return;
                        case "withdraw":
                            sendMessage(sender, Message.SETTING_LEVEL.toString()
                                    .replace("{0}", "withdraw")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.WITHDRAW.setRankForActionInClan(clan, level);
                            return;
                        case "setperm":
                            sendMessage(sender, Message.SETTING_LEVEL.toString()
                                    .replace("{0}", "setperm")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.SET_PERM.setRankForActionInClan(clan, level);
                            return;
                        case "viewlog":
                            sendMessage(sender, Message.SETTING_LEVEL.toString()
                                    .replace("{0}", "viewlog")
                                    .replace("{1}", String.valueOf(level))
                            );
                            BankAction.VIEW_LOG.setRankForActionInClan(clan, level);
                            return;
                        default:
                            sendMessage(sender, Message.BANK_USAGE.toString());
                            sender.spigot().sendMessage(textLib.textHoverable(
                                    Message.BANK_HELP_PREFIX + " setperm ",
                                    "&7<&c" + Message.PERM + "&7>",
                                    " &7<&f" + Message.LEVEL + "&7>",
                                    "&6" + Message.VALID_OPTIONS + "&7\n&o*&f balance&7\n&o*&f deposit&7\n&o*&f withdraw&7\n&o*&f viewlog"
                            ));
                            return;
                    }
                }
            }
            // msg usage (invalid subcommand)
            sendMessage(sender, Message.BANK_INVALID_SUBCOMMAND.toString());
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
            setPermTabComplete(e, 2);
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
                setPermTabComplete(e, 3);
            }
        }
    }

    private void setPermTabComplete(TabInsertEvent e, int index) {
        if (!e.getArgs(index).contains("balance")) {
            e.add(index, "balance");
        }
        if (!e.getArgs(index).contains("deposit")) {
            e.add(index, "deposit");
        }
        if (!e.getArgs(index).contains("withdraw")) {
            e.add(index, "withdraw");
        }
        if (!e.getArgs(index).contains("viewlog")) {
            e.add(index, "viewlog");
        }
        if (!e.getArgs(index).contains("setperm")) {
            e.add(index, "setperm");
        }
    }

    private Optional<Clan> testClan(Player sender) {
        final Optional<Clan> clanOptional = optionalClan(sender);
        if (!clanOptional.isPresent()) {
            sendMessage(sender, Message.PLAYER_NO_CLAN.toString());
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
