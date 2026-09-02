package org.Jtech.config;

import org.Jtech.Entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // No authentication available
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // Anonymous user
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        // Ensure the principal is your User entity
        if (!(principal instanceof User user)) {
            return Optional.empty();
        }

        return Optional.of(user.getUserId());
    }
}
