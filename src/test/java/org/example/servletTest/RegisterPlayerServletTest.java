package org.example.servletTest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servlet.RegisterPlayerServlet;

import static org.mockito.Mockito.mock;


public class RegisterPlayerServletTest {
    RegisterPlayerServlet registerPlayerServlet = new RegisterPlayerServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    public void setUp() {
        
    }




}
