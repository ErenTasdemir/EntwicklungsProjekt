package com.github.entwicklungsprojekt.projection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

/**
 * Configures the ProjectionFactory-Bean-Type
 */
@Configuration
public class ProjectionConfiguration {

    @Bean
    public SpelAwareProxyProjectionFactory proxyProjectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }
}
