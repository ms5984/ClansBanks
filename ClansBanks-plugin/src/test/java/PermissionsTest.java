import com.github.ms5984.clans.clansbanks.util.Permissions;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class PermissionsTest {

    @Mock
    CommandSender commandSender;

    @Before
    public void initMocks() {
        // define return for method
        when(commandSender.hasPermission(Permissions.BANKS_BALANCE.node)).thenReturn(false);
    }

    @Test
    public void testNode() {
        assertEquals("clans.banks.use.balance", Permissions.BANKS_BALANCE.node);
    }

    @Test
    public void testNot() {
        // test method
        assertTrue(Permissions.BANKS_BALANCE.not(commandSender));
    }
}
