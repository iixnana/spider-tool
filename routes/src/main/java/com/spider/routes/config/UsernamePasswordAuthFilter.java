package com.spider.routes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spider.routes.dto.CredentialsDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final UserAuthenticationProvider userAuthenticationProvider;

    public UsernamePasswordAuthFilter(UserAuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        if ("/api/signIn".equals(httpServletRequest.getServletPath())
                && HttpMethod.POST.matches(httpServletRequest.getMethod())) {
            CredentialsDto credentialsDto = MAPPER.readValue(httpServletRequest.getInputStream(), CredentialsDto.class);

            try {
                SecurityContextHolder.getContext().setAuthentication(
                        userAuthenticationProvider.validateCredentials(credentialsDto));
            } catch (RuntimeException e) {
                SecurityContextHolder.clearContext();
                throw e;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
