import com.github.ms5984.clans.clansbanks.messaging.Messages;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MessagesTest {

    @BeforeAll
    public static void setupMessages(@Mock JavaPlugin javaPlugin) {
        final InputStream inputStream = new ByteArrayInputStream(String.join(System.lineSeparator(),
                Arrays.asList("level=level", "banks.header=&fBanks")).getBytes(StandardCharsets.UTF_8));
        doReturn(inputStream).when(javaPlugin).getResource("messages.properties");
        doReturn(Logger.getLogger("Test")).when(javaPlugin).getLogger();
        Messages.setup(javaPlugin, null);
    }

    @Test
    public void testGet() {
        // try message
        assertEquals("level", Messages.LEVEL.get());
        // try null
        assertNull(Messages.PERM.get());
    }

    @Test
    public void testToString() {
        // try message
        assertEquals("level", Messages.LEVEL.toString());
        // try null
        assertEquals("null", Messages.PERM.toString());
    }
}
