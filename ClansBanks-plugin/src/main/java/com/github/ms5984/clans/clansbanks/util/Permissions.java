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
package com.github.ms5984.clans.clansbanks.util;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public enum Permissions {
    BANKS_STAR("clans.banks.*"),
    BANKS_USE("clans.banks.use"),
    BANKS_USE_STAR("clans.banks.use.*"),
    BANKS_BALANCE("clans.banks.use.balance"),
    BANKS_DEPOSIT("clans.banks.use.deposit"),
    BANKS_WITHDRAW("clans.banks.use.withdraw"),
    BANKS_LENDING("clans.banks.lending"), // User access to lending (can borrow)
    BANKS_LENDING_STAR("clans.banks.lending.*"), // Full access to lending (can make/take loans)
    BANKS_CAN_BORROW("clans.banks.lending.borrow"), // Can ask for loans
    BANKS_CAN_LEND("clans.banks.lending.lend"), // Can can initiate loans
    BANKS_LOAN_VOTE("clans.banks.lending.loan"), // Can vote on clan loans
    BANKS_LOAN_UNDERWRITE("clans.banks.lending.loan.underwrite"), // Can make loans decisions
    BANKS_LOAN("clans.banks.use.loan"), // loan subcommand = /clan bank loan <player> <amount>
    BANKS_BORROW("clans.banks.use.borrow"); // borrow subcommand = /clan bank borrow <amount>

    private static boolean init;
    public final String node;

    Permissions(String s) {
        this.node = s;
    }

    public boolean not(CommandSender sender) {
        return !sender.hasPermission(node);
    }

    /**
     * Setup plugin permissions.
     *
     * @param pm plugin manager object
     * @throws IllegalStateException if permissions already added
     */
    public static void setup(PluginManager pm) throws IllegalStateException {
        if (init) throw new IllegalStateException("Permissions already added!");
        // setup nodes
        final Permission balance = new Permission(BANKS_BALANCE.node);
        final Permission deposit = new Permission(BANKS_DEPOSIT.node);
        final Permission withdraw = new Permission(BANKS_WITHDRAW.node);
        final Permission use = new Permission(BANKS_USE.node);
        balance.addParent(use, true);
        final Permission useStar = new Permission(BANKS_USE_STAR.node);
        use.addParent(useStar, true);
        deposit.addParent(useStar, true);
        withdraw.addParent(useStar, true);
        final Permission star = new Permission(BANKS_STAR.node);
        useStar.addParent(star, true);
        // add nodes
        pm.addPermission(star);
        pm.addPermission(useStar);
        pm.addPermission(use);
        pm.addPermission(deposit);
        pm.addPermission(withdraw);
        pm.addPermission(balance);
        // Lending
        final Permission lending = new Permission(BANKS_LENDING.node);
        final Permission lendingStar = new Permission(BANKS_LENDING_STAR.node);
        lending.addParent(lendingStar, true);
        final Permission canBorrow = new Permission(BANKS_CAN_BORROW.node);
        final Permission useBorrow = new Permission(BANKS_BORROW.node);
        useBorrow.addParent(canBorrow, true);
        canBorrow.addParent(lending, true);
        final Permission canLend = new Permission(BANKS_CAN_LEND.node);
        final Permission useLoan = new Permission(BANKS_LOAN.node);
        useLoan.addParent(canLend, true);
        canLend.addParent(lendingStar, true);
        final Permission voteLoan = new Permission(BANKS_LOAN_VOTE.node);
        voteLoan.addParent(lending, true);
        final Permission underwriteLoan = new Permission(BANKS_LOAN_UNDERWRITE.node);
        underwriteLoan.addParent(lendingStar, true);
        pm.addPermission(lending);
        pm.addPermission(lendingStar);
        pm.addPermission(canBorrow);
        pm.addPermission(useBorrow);
        pm.addPermission(canLend);
        pm.addPermission(useLoan);
        pm.addPermission(voteLoan);
        pm.addPermission(underwriteLoan);
    }
}
