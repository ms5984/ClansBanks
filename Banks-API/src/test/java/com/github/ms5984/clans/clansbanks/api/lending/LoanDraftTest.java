package com.github.ms5984.clans.clansbanks.api.lending;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LoanDraftTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private static LoanDraft loanDraft;
    final BigDecimal testAmount = BigDecimal.TEN;
    final double testDouble = 100d;

    @Test
    void principalDouble() {
        // stub main method
        doReturn(testAmount).when(loanDraft).principal();
        // test double
        assertEquals(testAmount.doubleValue(), loanDraft.principalDouble());
    }

    @Test
    void setPrincipalDouble() {
        // test method
        loanDraft.setPrincipalDouble(testDouble);
        // verify call
        verify(loanDraft).setPrincipal(BigDecimal.valueOf(testDouble));
    }
}