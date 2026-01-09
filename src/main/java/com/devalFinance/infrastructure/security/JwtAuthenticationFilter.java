package com.devalFinance.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        String servletPath = request.getServletPath();
        String contextPath = request.getContextPath();
        
        // Permitir acceso sin autenticación a Swagger y endpoints públicos
        if (path != null && (path.contains("/swagger-ui") || path.contains("/v3/api-docs") || 
                             path.contains("/swagger-resources") || path.contains("/webjars") ||
                             path.contains("/api-docs") || path.contains("/swagger-config") ||
                             path.contains("/auth/register") || path.contains("/auth/login"))) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Para rutas protegidas, requerir token válido
        String authHeader = request.getHeader("Authorization");
        String token = getTokenFromRequest(request);
        
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Token de autenticación requerido\",\"message\":\"Debes incluir un token JWT en el header Authorization: Bearer <token>\",\"debug\":{\"path\":\"" + path + "\",\"authHeader\":\"" + (authHeader != null ? "present" : "null") + "\"}}");
            return;
        }
        
        String email = extractEmailFromToken(token);
        if (email == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Token inválido\",\"message\":\"No se pudo extraer el email del token\"}");
            return;
        }
        
        if (!jwtTokenProvider.validateToken(token, email)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"Token inválido o expirado\",\"message\":\"El token proporcionado no es válido o ha expirado\"}");
            return;
        }
        
        // Token válido, establecer autenticación
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String extractEmailFromToken(String token) {
        try {
            return jwtTokenProvider.getEmailFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }
}

