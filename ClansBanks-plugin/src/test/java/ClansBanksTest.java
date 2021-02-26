import com.github.ms5984.clans.clansbanks.ClansBanks;
import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ClansBanksTest {

    @Mock
    ClansBanks clansBanks;
    @Mock
    FileConfiguration config;
    Logger logger = Logger.getLogger("Test");

    /**
     * This test cannot use Mockito.spy because JavaPlugin's super()
     */
    @Before
    public void initMocks() {
        // initialize config mock
        when(config.getString("starting-balance"))
                .thenReturn(null, null, null, "100", "jd8wa9a");
        when(config.getString("default-balance"))
                .thenReturn(null, " ", BigDecimal.ONE.toString());
        when(config.getString("maximum-balance"))
                .thenReturn(null, "8jhaw", "-100", "100");
        when(config.getInt("log-level"))
                .thenReturn(-1, 4, 2);
        when(clansBanks.getConfig()).thenReturn(config);
        when(clansBanks.getLogger()).thenReturn(logger);
        when(clansBanks.startingBalance()).thenCallRealMethod();
        when(clansBanks.maxBalance()).thenCallRealMethod();
        when(clansBanks.logToConsole()).thenCallRealMethod();
    }

    @Test
    public void testStartingBalance() {
        // test null return
        assertEquals(BigDecimal.ZERO, clansBanks.startingBalance());
        // test null return but present default-balance
        assertEquals(BigDecimal.ZERO, clansBanks.startingBalance());
        // test null return with valid default-balance
        assertEquals(BigDecimal.ONE, clansBanks.startingBalance());
        // test valid return
        assertEquals(new BigDecimal("100"), clansBanks.startingBalance());
        // test invalid content
        assertEquals(BigDecimal.ZERO, clansBanks.startingBalance());
    }

    @Test
    public void testMaxBalance() {
        // test null
        assertNull(clansBanks.maxBalance());
        // test invalid
        assertNull(clansBanks.maxBalance());
        // test negative
        assertNull(clansBanks.maxBalance());
        // test valid
        assertEquals(new BigDecimal("100"), clansBanks.maxBalance());
    }

    @Test
    public void testLogToConsole() {
        // test out-of-range (negative)
        assertEquals(BanksAPI.LogLevel.QUIET, clansBanks.logToConsole());
        // test out-of-range (positive)
        assertEquals(BanksAPI.LogLevel.QUIET, clansBanks.logToConsole());
        // test in-range
        assertEquals(BanksAPI.LogLevel.VERBOSE, clansBanks.logToConsole());
    }
}
