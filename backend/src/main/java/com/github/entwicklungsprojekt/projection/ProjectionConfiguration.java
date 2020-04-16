package com.github.entwicklungsprojekt.projection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

@Configuration
public class ProjectionConfiguration {

    @Bean
    public SpelAwareProxyProjectionFactory proxyProjectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }
}
