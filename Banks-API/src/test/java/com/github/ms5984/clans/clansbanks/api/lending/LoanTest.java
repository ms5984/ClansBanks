package com.github.ms5984.clans.clansbanks.api.lending;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    final BigDecimal testPrincipal = BigDecimal.TEN;

    @Test
    void principalDouble(@Mock(answer = CALLS_REAL_METHODS) Loan loan) {
        // stub main method
        doReturn(testPrincipal).when(loan).principal();
        // test double
        assertEquals(testPrincipal.doubleValue(), loan.principalDouble());
    }
}