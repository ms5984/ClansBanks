/*
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
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

import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public enum BanksPermission {
    STAR("clans.banks.*"),
    USE("clans.banks.use"),
    USE_STAR("clans.banks.use.*"),
    USE_BALANCE("clans.banks.use.balance"),
    USE_DEPOSIT("clans.banks.use.deposit"),
    USE_WITHDRAW("clans.banks.use.withdraw"),
    LENDING("clans.banks.lending"), // User access to lending (can borrow)
    LENDING_STAR("clans.banks.lending.*"), // Full access to lending (can make/take loans)
    USER_CAN_BORROW("clans.banks.lending.borrow"), // Can ask for loans
    USER_CAN_LEND("clans.banks.lending.lend"), // Can can initiate loans
    LOAN_VOTE("clans.banks.lending.loan"), // Can vote on clan loans
    USER_LOAN_UNDERWRITE("clans.banks.lending.loan.underwrite"), // Can make loans decisions
    USE_LOAN("clans.banks.use.loan"), // loan subcommand = /clan bank loan <player> <amount>
    USE_BORROW("clans.banks.use.borrow"); // borrow subcommand = /clan bank borrow <amount>

    private static boolean init;
    public final String node;

    BanksPermission(String s) {
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
        val balance = new Permission(USE_BALANCE.node);
        val deposit = new Permission(USE_DEPOSIT.node);
        val withdraw = new Permission(USE_WITHDRAW.node);
        val use = new Permission(USE.node);
        balance.addParent(use, true);
        val useStar = new Permission(USE_STAR.node);
        use.addParent(useStar, true);
        deposit.addParent(useStar, true);
        withdraw.addParent(useStar, true);
        val star = new Permission(STAR.node);
        useStar.addParent(star, true);
        // add nodes
        pm.addPermission(star);
        pm.addPermission(useStar);
        pm.addPermission(use);
        pm.addPermission(deposit);
        pm.addPermission(withdraw);
        pm.addPermission(balance);
        // Lending
        val lending = new Permission(LENDING.node);
        val lendingStar = new Permission(LENDING_STAR.node);
        lending.addParent(lendingStar, true);
        val canBorrow = new Permission(USER_CAN_BORROW.node);
        val useBorrow = new Permission(USE_BORROW.node);
        useBorrow.addParent(canBorrow, true);
        canBorrow.addParent(lending, true);
        val canLend = new Permission(USER_CAN_LEND.node);
        val useLoan = new Permission(USE_LOAN.node);
        useLoan.addParent(canLend, true);
        canLend.addParent(lendingStar, true);
        val voteLoan = new Permission(LOAN_VOTE.node);
        voteLoan.addParent(lending, true);
        val underwriteLoan = new Permission(USER_LOAN_UNDERWRITE.node);
        underwriteLoan.addParent(lendingStar, true);
        pm.addPermission(lending);
        pm.addPermission(lendingStar);
        pm.addPermission(canBorrow);
        pm.addPermission(useBorrow);
        pm.addPermission(canLend);
        pm.addPermission(useLoan);
        pm.addPermission(voteLoan);
        pm.addPermission(underwriteLoan);
        init = true;
    }
}
