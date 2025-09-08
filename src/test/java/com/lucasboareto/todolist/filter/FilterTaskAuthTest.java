package com.lucasboareto.todolist.filter;


import com.lucasboareto.todolist.user.IUserRepository;
import com.lucasboareto.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FilterTaskAuthTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private FilterTaskAuth filterTaskAuth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String buildBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @Test
    void shouldAuthenticateAndProceed_WhenValidCredentials() throws ServletException, IOException {
        String username = "john";
        String password = "123";
        String encodedAuth = buildBasicAuth(username, password);

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setUserName(username);
        user.setPassword(BCrypt.withDefaults().hashToString(12, password.toCharArray()));

        when(request.getServletPath()).thenReturn("/tasks/123");
        when(request.getHeader("Authorization")).thenReturn(encodedAuth);
        when(userRepository.findByUserName(username)).thenReturn(user);

        filterTaskAuth.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("idUser", user.getId());
        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendError(anyInt());
    }

    @Test
    void shouldReturnUnauthorized_WhenUserNotFound() throws ServletException, IOException {
        String username = "ghost";
        String password = "123";
        String encodedAuth = buildBasicAuth(username, password);

        when(request.getServletPath()).thenReturn("/tasks/abc");
        when(request.getHeader("Authorization")).thenReturn(encodedAuth);
        when(userRepository.findByUserName(username)).thenReturn(null);

        filterTaskAuth.doFilterInternal(request, response, filterChain);

        verify(response).sendError(401);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldReturnUnauthorized_WhenPasswordIsInvalid() throws ServletException, IOException {
        String username = "john";
        String password = "wrong";
        String encodedAuth = buildBasicAuth(username, password);

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setUserName(username);
        user.setPassword(BCrypt.withDefaults().hashToString(12, "realpass".toCharArray()));

        when(request.getServletPath()).thenReturn("/tasks/1");
        when(request.getHeader("Authorization")).thenReturn(encodedAuth);
        when(userRepository.findByUserName(username)).thenReturn(user);

        when(request.getServletPath()).thenReturn("/tasks/1");
        when(request.getHeader("Authorization")).thenReturn(encodedAuth);
        when(userRepository.findByUserName(username)).thenReturn(user);

        filterTaskAuth.doFilterInternal(request, response, filterChain);

        verify(response).sendError(eq(401), eq("Unauthorized"));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldBypassAuth_WhenNotTaskPath() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/other/path");

        filterTaskAuth.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendError(anyInt());
    }

}