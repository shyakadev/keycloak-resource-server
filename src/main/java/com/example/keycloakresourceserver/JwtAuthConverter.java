package com.example.keycloakresourceserver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    public JwtAuthConverter() {
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(@SuppressWarnings("null") Jwt jwt) {
        final Set<GrantedAuthority> authorities = Stream
                .concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractUserRoles(jwt).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Set<? extends GrantedAuthority> extractUserRoles(Jwt jwt) {
        final Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        @SuppressWarnings("unchecked")
        final List<String> realmRoles =  (List<String>) realmAccess.get("roles");

        if(CollectionUtils.isNotEmpty(realmRoles)) {
            return realmRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())).collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }
}
