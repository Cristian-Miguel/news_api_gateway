package com.user.api_gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.api_gateway.models.GenericErrorResponse;
import com.user.api_gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRoleFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final SecurityConstraintsConfig securityConfig;
    private final ObjectMapper objectMapper;
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();

        // 1. VALIDAR WHITELIST (Rutas Públicas)
        boolean isPublic = securityConfig.getWhiteList().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));

        String token = extractToken(request);

        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        // Si no es público y no trae token -> 401
        if (token == null) {
            sendErrorResponse(response, "Unauthorized: No token found.", HttpStatus.UNAUTHORIZED, uri);
            return;
        }

        try {
            // 2. VALIDAR TOKEN (Claims)
            Claims claims = jwtUtils.getAllClaims(token);
            String roleName = claims.get("role", String.class);

            // 3. VALIDACIÓN DE ROLES (RBAC)
            Optional<SecurityConstraintsConfig.RouteConstraint> matchedConstraint = securityConfig.getRoleConstraints().stream()
                    .filter(c -> pathMatcher.match(c.getPath(), uri)) // Coincide ruta
                    .filter(c -> c.getMethod() == null || c.getMethod().equalsIgnoreCase(httpMethod)) // Coincide método
                    .findFirst();

            if (matchedConstraint.isPresent()) {
                List<String> allowedRoles = matchedConstraint.get().getRoles();
                if (!allowedRoles.contains(roleName)) {
                    sendErrorResponse(response, "Forbidden: Insufficient permissions.", HttpStatus.FORBIDDEN, uri);
                    return;
                }
            } else if (!isPublic) {
                // Política de seguridad: Si no está en whitelist y no tiene regla explícita -> Bloquear
                sendErrorResponse(response, "Forbidden: No access rule defined.", HttpStatus.FORBIDDEN, uri);
                return;
            }

            // 4. GENERAR TOKEN INTERNO
            String internalToken = jwtUtils.getToken(
                    roleName,
                    claims.get("email", String.class),
                    claims.get("uuid", String.class),
                    claims.getSubject()
            );

            // 5. INYECTAR TOKEN INTERNO (Usando el Wrapper del Paso 2)
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
            requestWrapper.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + internalToken);

            filterChain.doFilter(requestWrapper, response);

        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, "Token expired.", HttpStatus.UNAUTHORIZED, uri);
        } catch (MalformedJwtException | SignatureException ex) {
            sendErrorResponse(response, "Invalid token signature.", HttpStatus.UNAUTHORIZED, uri);
        } catch (Exception ex) {
            log.error("Security Filter Error", ex);
            sendErrorResponse(response, "Unauthorized access: " + ex.getMessage(), HttpStatus.UNAUTHORIZED, uri);
        }
    }

    // --- Métodos Auxiliares ---

    private String extractToken(HttpServletRequest request) {
        // Prioridad 1: Cookie
        if (request.getCookies() != null) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst();
            if (cookie.isPresent()) return cookie.get().getValue();
        }

        // Prioridad 2: Header (Fallback)
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Método corregido para escribir JSON en HttpServletResponse
    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status, String uri) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // Usamos Jackson para convertir tu objeto a JSON String
        GenericErrorResponse errorResponse = GenericErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(uri)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}