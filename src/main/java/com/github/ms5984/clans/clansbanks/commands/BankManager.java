package com.github.ms5984.clans.clansbanks.commands;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import com.youtube.hempfest.clans.HempfestClans;
import com.youtube.hempfest.clans.util.construct.Clan;
import com.youtube.hempfest.clans.util.events.CommandHelpEvent;
import com.youtube.hempfest.clans.util.events.SubCommandEvent;
import com.youtube.hempfest.clans.util.events.TabInsertEvent;
import com.youtube.hempfest.hempcore.formatting.component.Text;
import com.youtube.hempfest.hempcore.formatting.component.Text_R2;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BankManager implements Listener {

    private final Text textLib1_16 = new Text();
    private final List<String> tab2 = new LinkedList<>(Arrays.asList("balance", "deposit", "withdraw"));

    @EventHandler
    private void onClansHelp(CommandHelpEvent e) {
        e.insert(Messages.CLANS_HELP_PREFIX + " " + Messages.BANK_HELP_PREFIX + " " + Messages.BANK_HELP_BALANCE);
        e.insert(Messages.CLANS_HELP_PREFIX + " " + Messages.BANK_HELP_PREFIX + " " + Messages.BANK_HELP_AMOUNT_COMMANDS.toString()
                .replace("{banks.deposit}", Messages.DEPOSIT.toString())
                .replace("{banks.withdraw}", Messages.WITHDRAW.toString())
                .replace("{banks.amount}", Messages.AMOUNT.toString()));
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
            sendMessage(sender, Messages.BANKS_HEADER.toString());
            switch (length) {
                case 1: // "bank" print instructions
                    sendMessage(sender,Messages.BANKS_CURRENT_BALANCE.toString()
                            + ClansBanks.getAPI().getBank(HempfestClans.clanManager(sender)).getBalance());
                    sendMessage(sender, Messages.BANKS_COMMAND_LIST.toString());
                    final List<BaseComponent> textComponents = new LinkedList<>();
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        textComponents.add(textLib1_16.textSuggestable(
                                Messages.BANK_HELP_PREFIX + " &f<",
                                "&a" + Messages.DEPOSIT, Messages.HOVER_DEPOSIT.toString(),
                                "clan bank deposit 1"
                        ));
                        textComponents.add(textLib1_16.textSuggestable(
                                "&7,",
                                "&c" + Messages.WITHDRAW, Messages.HOVER_WITHDRAW.toString(),
                                "clan bank withdraw 1"
                        ));
                    } else {
                        textComponents.add(Text_R2.textSuggestable(
                                Messages.BANK_HELP_PREFIX + " &f<",
                                "&a" + Messages.DEPOSIT, Messages.HOVER_DEPOSIT.toString(),
                                "clan bank deposit 1"
                        ));
                        textComponents.add(Text_R2.textSuggestable(
                                "&7,",
                                "&c" + Messages.WITHDRAW, Messages.HOVER_WITHDRAW.toString(),
                                "clan bank withdraw 1"
                        ));
                    }
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
                                // msg usage (need amount param)
                                sendMessage(sender, Messages.BANK_USAGE.toString());
                                if (Bukkit.getServer().getVersion().contains("1.16")) {
                                    sender.spigot().sendMessage(textLib1_16.textHoverable(
                                            Messages.BANK_HELP_PREFIX + " ",
                                            "&7<&f" + Messages.DEPOSIT + "&7>",
                                            " ",
                                            "&7<&c" + Messages.AMOUNT + "&7>",
                                            Messages.HOVER_DEPOSIT.toString(),
                                            Messages.HOVER_NO_AMOUNT.toString()
                                    ));
                                } else {
                                    sender.spigot().sendMessage(Text_R2.textHoverable(
                                            Messages.BANK_HELP_PREFIX + " ",
                                            "&7<&f" + Messages.DEPOSIT + "&7>",
                                            " ",
                                            "&7<&c" + Messages.AMOUNT + "&7>",
                                            Messages.HOVER_DEPOSIT.toString(),
                                            Messages.HOVER_NO_AMOUNT.toString()
                                    ));
                                }
                                return;
                            case "withdraw":
                                // msg usage (need amount param)
                                sendMessage(sender, Messages.BANK_USAGE.toString());
                                if (Bukkit.getServer().getVersion().contains("1.16")) {
                                    sender.spigot().sendMessage(textLib1_16.textHoverable(
                                            Messages.BANK_HELP_PREFIX + " ",
                                            "&7<&f" + Messages.WITHDRAW + "&7>",
                                            " ",
                                            "&7<&c" + Messages.AMOUNT + "&7>",
                                            Messages.HOVER_WITHDRAW.toString(),
                                            Messages.HOVER_NO_AMOUNT.toString()
                                    ));
                                } else {
                                    sender.spigot().sendMessage(Text_R2.textHoverable(
                                            Messages.BANK_HELP_PREFIX + " ",
                                            "&7<&f" + Messages.WITHDRAW + "&7>",
                                            " ",
                                            "&7<&c" + Messages.AMOUNT + "&7>",
                                            Messages.HOVER_WITHDRAW.toString(),
                                            Messages.HOVER_NO_AMOUNT.toString()
                                    ));
                                }
                                return;
                            default:
                                // msg usage (invalid subcommand)
                                sendMessage(sender, "&c" + Messages.BANK_INVALID_SUBCOMMAND);
                                return;
                        }
                    }
                    sender.sendMessage(bank.getBalance().toString()); // TODO: make this pretty
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
                                        if (theBank.deposit(sender, amount)) {
                                            sender.sendMessage("Success " + amount); // TODO: message
                                        }
                                        break;
                                    case "withdraw":
                                        if (theBank.withdraw(sender, amount)) {
                                            sender.sendMessage("Success " + amount.negate()); // TODO: message
                                        }
                                        break;
                                }
                            } catch (NumberFormatException exception) {
                                // TODO: send message "invalid number"
                            }
                            return;
                        default: // yell at the user and send usage msg
                            break;
                    }
                    break;
            }
            // msg usage (invalid subcommand)
            sendMessage(sender, "&c" + Messages.BANK_INVALID_SUBCOMMAND);
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
        final Clan clan = HempfestClans.clanManager(sender);
        if (clan == null) return null; // TODO: msg "u don't have a clan"
        return ClansBanks.getAPI().getBank(clan);
    }

    private void sendMessage(Player player, String message) {
        if (message.isEmpty()) {
            return;
        }
        player.sendMessage(new ColoredString(message, ColoredString.ColorType.MC).toString());
    }
}
