package com.github.ms5984.clans.clansbanks.events.lending;

import com.github.ms5984.clans.clansbanks.api.lending.Loan;
import com.github.ms5984.clans.clansbanks.api.lending.LoanDraft;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PendingLoanEventTest {
//    fakeClanID "1283-3190-1312"

    @Test
    void getLoanDraft(@Mock LoanDraft loanDraft) {
        assertSame(loanDraft, new PendingLoanEvent(null, null, loanDraft).getLoanDraft());
    }

    @Test
    void setLoan(@Mock Loan loan) {
        final PendingLoanEvent pendingLoanEvent = new PendingLoanEvent(null, null, null);
        pendingLoanEvent.setLoan(loan);
        assertSame(loan, pendingLoanEvent.loan);
    }

    @Test
    void isCancelled() {
        // test default
        final PendingLoanEvent pendingLoanEvent = new PendingLoanEvent(null, null, null);
        assertFalse(pendingLoanEvent.isCancelled());
        // set cancelled
        pendingLoanEvent.setCancelled(true);
        assertTrue(pendingLoanEvent.isCancelled());
        // un-cancel
        pendingLoanEvent.setCancelled(false);
        assertFalse(pendingLoanEvent.isCancelled());
    }

    @Test
    void setCancelled() {
        // test default
        final PendingLoanEvent pendingLoanEvent = new PendingLoanEvent(null, null, null);
        // set cancelled
        pendingLoanEvent.setCancelled(true);
        assertTrue(pendingLoanEvent.isCancelled());
        // un-cancel
        pendingLoanEvent.setCancelled(false);
        assertFalse(pendingLoanEvent.isCancelled());
    }
}