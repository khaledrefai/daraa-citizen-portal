package gov.daraa.citizen.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

/**
 * Extracts Keycloak realm and client roles into Spring Security authorities.
 */
public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String clientId;

    public KeycloakRealmRoleConverter(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return getRoles(jwt)
            .stream()
            .filter(StringUtils::hasText)
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase(Locale.ROOT))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }

    public Set<String> getRoles(Jwt jwt) {
        Set<String> roles = new HashSet<>();
        roles.addAll(extractRealmRoles(jwt));
        roles.addAll(extractResourceRoles(jwt));
        return roles;
    }

    private Collection<String> extractRealmRoles(Jwt jwt) {
        Object realmAccess = jwt.getClaims().get("realm_access");
        if (realmAccess instanceof Map<?, ?> realmMap) {
            Object realmRoles = realmMap.get("roles");
            if (realmRoles instanceof Collection<?> roleCollection) {
                return roleCollection.stream().map(Object::toString).toList();
            }
        }
        return Collections.emptyList();
    }

    private Collection<String> extractResourceRoles(Jwt jwt) {
        if (!StringUtils.hasText(clientId)) {
            return Collections.emptyList();
        }
        Object resourceAccess = jwt.getClaims().get("resource_access");
        if (resourceAccess instanceof Map<?, ?> resourceMap) {
            Object clientEntry = resourceMap.get(clientId);
            if (clientEntry instanceof Map<?, ?> clientRolesMap) {
                Object roles = clientRolesMap.get("roles");
                if (roles instanceof Collection<?> roleCollection) {
                    return roleCollection.stream().map(Object::toString).toList();
                }
            }
        }
        return Collections.emptyList();
    }
}
