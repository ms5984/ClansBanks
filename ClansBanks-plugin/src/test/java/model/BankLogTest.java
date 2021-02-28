package model;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.events.BankTransactionEvent;
import com.github.ms5984.clans.clansbanks.model.BankLog;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankLogTest {

    @Spy
    BankLog bankLog = new BankLog();
    @Mock(lenient = true)
    Player player;
    final String fakePlayerName = "Bob";
    @Mock
    ClanBank clanBank;
    final BigDecimal testAmount = BigDecimal.TEN;
    final BigDecimal testAmount2 = BigDecimal.ONE;
    final String testId = "test";
    final BankTransactionEvent.Type transactionType = BankTransactionEvent.Type.DEPOSIT;
    final LocalDateTime aTime = LocalDateTime.now();

    @BeforeEach
    public void initMocks() {
        // override call to BankMeta
        lenient().doNothing().when(bankLog).saveForClan(any());
        // setup fake player display name
        when(player.getDisplayName()).thenReturn(fakePlayerName);
    }

    @Test
    public void testAddTransaction() {
        // initialize events
        bankLog.addTransaction(new BankTransactionEvent(player, clanBank, testAmount, testId, true, transactionType));
        assertTrue(bankLog.getTransactions().stream().anyMatch(transaction -> {
            if (!transaction.entity.equals(fakePlayerName)) return false;
            if (!transaction.type.equals(transactionType)) return false;
            return transaction.amount.equals(testAmount);
        }));
    }

    @Test
    public void testAddTransactionWithTime() {
        bankLog.addTransaction(new BankTransactionEvent(player, clanBank, testAmount2, testId, true, transactionType), aTime);
        assertTrue(bankLog.getTransactions().stream().anyMatch(transaction -> {
            if (!transaction.entity.equals(fakePlayerName)) return false;
            if (!transaction.type.equals(transactionType)) return false;
            if (!transaction.amount.equals(testAmount2)) return false;
            return transaction.localDateTime.equals(aTime);
        }));
    }

    @Test
    public void testTransactionToString() {
        final BankLog.Transaction transaction = new BankLog.Transaction(fakePlayerName, transactionType, testAmount, aTime);
        assertEquals(ChatColor.translateAlternateColorCodes('&', String.format("&6%s %s &f%s &7at &f%s",
                fakePlayerName, "&adeposited", testAmount,
                aTime.format(DateTimeFormatter.ofPattern("h:mma '&7on&f' MMM dd',' yyyy")))), transaction.toString());
    }
}
