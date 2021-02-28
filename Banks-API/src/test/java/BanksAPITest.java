import com.github.ms5984.clans.clansbanks.api.BanksAPI;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BanksAPITest {
    @Mock
    BanksAPI api;

    @Test
    public void testGetInstance(@Mock Server server, @Mock ServicesManager servicesManager, @Mock(answer = Answers.RETURNS_MOCKS) Plugin plugin) {
        when(server.getLogger()).thenAnswer(Answers.RETURNS_MOCKS);
        // set fake server
        Bukkit.setServer(server);
        // return our mock services manager
        doReturn(servicesManager).when(server).getServicesManager();
        // construct fake rsp
        final RegisteredServiceProvider<BanksAPI> rsp = new RegisteredServiceProvider<>(BanksAPI.class, api, ServicePriority.Normal, plugin);
        doReturn(rsp, null, null).when(servicesManager).getRegistration(BanksAPI.class);
        // test mock api
        assertSame(api, BanksAPI.getInstance());
        // test null throws IllegalStateException
        assertThrows(IllegalStateException.class, BanksAPI::getInstance);
    }
}
