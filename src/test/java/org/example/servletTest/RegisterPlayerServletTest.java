package org.example.servletTest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import org.example.core.domain.Player;
import org.example.manager.PlayerManager;
import org.example.servlet.RegisterPlayerServlet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class RegisterPlayerServletTest {

    private RegisterPlayerServlet servlet;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PlayerManager playerManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new RegisterPlayerServlet();
        servlet.setObjectMapper(objectMapper);
        servlet.setPlayerManager(playerManager);
    }

    @Test
    public void testDoPostRegistrationSuccessful() throws Exception {
        // Создаем тестового игрока и настраиваем моки
        Player testPlayer = new Player();
        testPlayer.setUsername("testUser");
        testPlayer.setPassword("testPassword");

        String json = """
                {
                    "username": "qaisar",
                    "password": "qwerty123"
                }         
                """;

        // Создаем ServletInputStream с использованием ByteArrayInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());

        // Настраиваем моки
        when(request.getInputStream()).thenReturn(new TestServletInputStream(inputStream));
        when(objectMapper.readValue((JsonParser) any(), eq(Player.class))).thenReturn(testPlayer);
        when(playerManager.registerPlayer(eq("testUser"), eq("testPassword"))).thenReturn(true);

        servlet.doPost(request, response);

        // Проверяем, что статус и сообщение корректные
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter(), times(1)).write("{\"message\": \"Registration successful\"}");
    }

    private class TestServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        public TestServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        // Для остальных методов ServletInputStream можно возвращать значения по умолчанию
        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}
