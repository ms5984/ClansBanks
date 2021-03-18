package com.github.ms5984.clans.clansbanks.events.lending;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NewLoanEventTest {

    final String fakeClanID = "9183-0091-3830";

    @SuppressWarnings("ConstantConditions")
    @Test
    public void constructor(@Mock ClanBank clanBank, @Mock Loan loan) {
        // Test throws on null loan
        assertThrows(Exception.class, () -> new NewLoanEvent(clanBank, fakeClanID, null));
        // Test successful on non-null loan
        assertDoesNotThrow(() -> new NewLoanEvent(clanBank, fakeClanID, loan));
    }
}