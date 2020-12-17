package com.github.ms5984.clans.clansbanks.commands;

import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.messaging.Messages;
import com.youtube.hempfest.clans.HempfestClans;
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

import java.util.LinkedList;
import java.util.List;

public class BankManager implements Listener {

    private final Text textLib1_16 = new Text();

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
            switch (length) {
                case 1: // "bank" print instructions
                    e.setReturn(true);
                    final Player sender = e.getSender();
                    sendMessage(sender, Messages.BANKS_HEADER.toString());
                    sendMessage(sender,Messages.BANKS_CURRENT_BALANCE.toString()
                            + ClansBanks.getAPI().getBank(HempfestClans.clanManager(sender)).getBalance());
                    sendMessage(sender, Messages.BANKS_COMMAND_LIST.toString());
                    final List<BaseComponent> textComponents = new LinkedList<>();
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        textComponents.add(textLib1_16.textSuggestable(
                                Messages.BANK_HELP_PREFIX + " &f<",
                                "&a" + Messages.DEPOSIT, Messages.HOVER_DEPOSIT.toString(),
                                "clan bank deposit "
                        ));
                        textComponents.add(textLib1_16.textSuggestable(
                                "&7,",
                                "&c" + Messages.WITHDRAW, Messages.HOVER_WITHDRAW.toString(),
                                "clan bank withdraw "
                        ));
                    } else {
                        textComponents.add(Text_R2.textSuggestable(
                                Messages.BANK_HELP_PREFIX + " &f<",
                                "&a" + Messages.DEPOSIT, Messages.HOVER_DEPOSIT.toString(),
                                "clan bank deposit "
                        ));
                        textComponents.add(Text_R2.textSuggestable(
                                "&7,",
                                "&c" + Messages.WITHDRAW, Messages.HOVER_WITHDRAW.toString(),
                                "clan bank withdraw "
                        ));
                    }
                    textComponents.add(new ColoredString("&f> <&7" + Messages.AMOUNT + "&f>",
                            ColoredString.ColorType.MC_COMPONENT).toComponent());
                    sender.spigot().sendMessage(textComponents.toArray(new BaseComponent[0]));
                    break;
            }
        }
    }

    @EventHandler
    private void onBankTab(TabInsertEvent e) {
        final int length = e.getCommandArgs().length;
        switch (length) {
            case 1:
                e.add(1, "bank");
                break;
            case 2:
                e.add(2, "deposit");
                e.add(2, "withdraw");
                break;
            case 3:
                e.add(3, "1");
        }
    }

    private void sendMessage(Player player, String message) {
        if (message.isEmpty()) {
            return;
        }
        player.sendMessage(new ColoredString(message, ColoredString.ColorType.MC).toString());
    }
}
