package com.github.ms5984.clans.clansbanks.events.lending;

import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NewLoanEventTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void constructor(@Mock Loan loan) {
        // Test throws on null loan
        assertThrows(Exception.class, () -> new NewLoanEvent(null, null, null));
        // Test successful on non-null loan
        assertDoesNotThrow(() -> new NewLoanEvent(null, null, loan));
    }

    @Test
    void getLoan(@Mock Loan loan) {
        assertSame(loan, new NewLoanEvent(null, null, loan).loan);
    }
}