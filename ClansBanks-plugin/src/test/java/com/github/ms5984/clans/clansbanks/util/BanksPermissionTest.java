package com.github.ms5984.clans.clansbanks.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void testSetup(@Mock PluginManager pluginManager, @Mock Server server) {
        // preconfigure mock
        doAnswer(invocation -> Collections.emptySet()).when(pluginManager).getPermissionSubscriptions(any());
        // stub into server
        doReturn(pluginManager).when(server).getPluginManager();
        // setup fake server
        doReturn("fakeServer").when(server).getName();
        doReturn("fakeVersion").when(server).getVersion();
        doReturn("fakeBukkitVersion").when(server).getBukkitVersion();
        doReturn(Logger.getLogger("Test")).when(server).getLogger();
        Bukkit.setServer(server);

        // test success, should set init to true internally
        BanksPermission.setup(pluginManager);
        // verify attempts were made to add each permission to the PluginManager mock
        Arrays.stream(BanksPermission.values())
                .forEach(permission -> verify(pluginManager).addPermission(argThat(p -> p.getName().equals(permission.node))));

        // assert setup throws IllegalStateException once permissions have already been added
        assertThrows(IllegalStateException.class, () -> BanksPermission.setup(pluginManager));
    }
}
