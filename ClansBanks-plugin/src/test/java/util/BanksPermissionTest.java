package util;

import com.github.ms5984.clans.clansbanks.util.BanksPermission;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BanksPermissionTest {

    final BanksPermission PERMISSION = BanksPermission.USE_BALANCE;
    @Mock
    CommandSender commandSender;

    @Test
    public void testNode() {
        assertEquals("clans.banks.use.balance", PERMISSION.node);
    }

    @Test
    public void testNot() {
        // define return for method
        when(commandSender.hasPermission(PERMISSION.node)).thenReturn(false);
        // test method
        assertFalse(commandSender.hasPermission(PERMISSION.node));
        assertTrue(PERMISSION.not(commandSender));
    }
}
