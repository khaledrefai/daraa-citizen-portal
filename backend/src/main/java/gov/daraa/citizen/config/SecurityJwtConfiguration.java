package gov.daraa.citizen.config;

import static gov.daraa.citizen.security.SecurityUtils.JWT_ALGORITHM;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import gov.daraa.citizen.management.SecurityMetersService;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.StringUtils;

@Configuration
public class SecurityJwtConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityJwtConfiguration.class);

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:}")
    private String jwkSetUri;

    @Bean
    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        final NimbusJwtDecoder jwkDecoder = StringUtils.hasText(jwkSetUri) ? NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build() : null;
        final NimbusJwtDecoder legacyDecoder = StringUtils.hasText(jwtKey)
            ? NimbusJwtDecoder.withSecretKey(getSecretKey()).macAlgorithm(JWT_ALGORITHM).build()
            : null;

        return token -> {
            try {
                if (jwkDecoder != null) {
                    return jwkDecoder.decode(token);
                }
            } catch (Exception e) {
                trackJwtError(metersService, e);
                if (legacyDecoder == null) {
                    throw e;
                }
            }
            try {
                if (legacyDecoder != null) {
                    return legacyDecoder.decode(token);
                }
            } catch (Exception e) {
                trackJwtError(metersService, e);
                throw e;
            }
            throw new IllegalStateException("No JWT decoder configured");
        };
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    private void trackJwtError(SecurityMetersService metersService, Exception e) {
        if (e.getMessage().contains("Invalid signature")) {
            metersService.trackTokenInvalidSignature();
        } else if (e.getMessage().contains("Jwt expired at")) {
            metersService.trackTokenExpired();
        } else if (
            e.getMessage().contains("Invalid JWT serialization") ||
            e.getMessage().contains("Malformed token") ||
            e.getMessage().contains("Invalid unsecured/JWS/JWE")
        ) {
            metersService.trackTokenMalformed();
        } else {
            LOG.error("Unknown JWT error {}", e.getMessage());
        }
    }
}
