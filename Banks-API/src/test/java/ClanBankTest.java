import com.github.ms5984.clans.clansbanks.api.ClanBank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ClanBankTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    ClanBank clanBank;

    @Test
    public void testDefaultSetBalanceDouble() {
        // test zero
        assertDoesNotThrow(() -> clanBank.setBalanceDouble(0d));
        // test negative
        assertThrows(IllegalArgumentException.class, () -> clanBank.setBalanceDouble(-1d));
        // test positive
        assertDoesNotThrow(() -> clanBank.setBalanceDouble(1d));
    }

    @Test
    public void testDefaultSetBalance() {
        // test zero
        assertDoesNotThrow(() -> clanBank.setBalance(BigDecimal.ZERO));
        // test negative
        assertThrows(IllegalArgumentException.class, () -> clanBank.setBalance(BigDecimal.ONE.negate()));
        // test positive
        assertDoesNotThrow(() -> clanBank.setBalance(BigDecimal.ONE));
    }
}
