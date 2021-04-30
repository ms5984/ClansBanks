package com.github.ms5984.clans.clansbanks.messaging;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

@Disabled
@ExtendWith(MockitoExtension.class)
class MessageTestKeys {
    final Field keyField;

    {
        try {
            keyField = Message.class.getDeclaredField("key");
            keyField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void printAllKeys() {
        for (Message message : Message.values()) {
            try {
                System.out.println(keyField.get(message));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

/*    @Disabled
    @Test
    void testPrintKeyAsGet(@Mock FileConfiguration configMock) {
        final MessageProvider provider = new MessageProvider(configMock) {};
        try(MockedStatic<MessageProvider> mocked = mockStatic(MessageProvider.class)) {
            // replace service-based instance getter with faked instance
            mocked.when(MessageProvider::getInstance).thenReturn(provider);
            // configure faked instance config mock to output provided key
            when(configMock.getString(anyString())).thenAnswer(i -> i.getArguments()[0]);

            for (Message message : Message.values()) {
                System.out.println(message.get());
            }
        }
    }*/
}
