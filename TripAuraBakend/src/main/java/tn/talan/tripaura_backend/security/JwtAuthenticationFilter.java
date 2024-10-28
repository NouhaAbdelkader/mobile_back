package tn.talan.tripaura_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
//Ce filtre personnalisé gère l'authentification JWT en interceptant les requêtes HTTP pour assurer l'accès
// aux ressources protégées uniquement avec un JWT valide, et est positionné
// avant le UsernamePasswordAuthenticationFilter
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter, garantissant que le filtre s'exécute une fois par requête.
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final HandlerExceptionResolver handlerExceptionResolver;


    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        logger.info("JwtAuthenticationFilter called for request: {}", request.getRequestURI());

        if (request.getRequestURI().equals("/TripAuraUsers/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().equals("/TripAuraUsers/Add/ROLE_USER")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().equals("/auth/forgotPassword")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().equals("/auth/loginSuccess")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("http://localhost:4200/homeN")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            processAuthentication(userEmail, request, response);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.info("JWT expired, generating a new token.");
            final String userEmail = e.getClaims().getSubject();
            processAuthentication(userEmail, request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }


        filterChain.doFilter(request, response);
    }

    private void processAuthentication(String userEmail, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            if (jwtService.isTokenExpired(jwtService.generateToken(userDetails))) {
                String newToken = jwtService.generateToken(userDetails);
                response.setHeader("X-New-Token", newToken);
            }
        }
    }

}