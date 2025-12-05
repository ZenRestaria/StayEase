package com.stayease.security;

import com.stayease.domain.user.entity.User;
import com.stayease.domain.user.repository.UserRepository;
import com.stayease.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * Get the current authenticated user's publicId
     */
    public UUID getCurrentUserPublicId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user found");
        }
        
        Object principal = authentication.getPrincipal();
        
        // For JWT-based auth
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            String email = jwt.getClaimAsString("email");
            
            if (email == null) {
                throw new UnauthorizedException("Email not found in JWT token");
            }
            
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("User not found with email: " + email));
            
            return user.getPublicId();
        }
        
        // For UserPrincipal-based auth
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getId();
        }
        
        throw new UnauthorizedException("Invalid authentication principal type");
    }

    /**
     * Get the current authenticated user's ID (internal ID)
     */
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getId();
        }
        
        throw new IllegalStateException("Invalid principal type");
    }

    /**
     * Get the current authenticated user's email
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return jwt.getClaimAsString("email");
        }
        
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getEmail();
        }
        
        return null;
    }

    /**
     * Check if current user is authenticated
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Check if current user has a specific authority
     */
    public boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }
}