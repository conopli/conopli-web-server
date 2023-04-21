package conopli.webserver.config;

import conopli.webserver.auth.filter.JwtVerificationFilter;
import conopli.webserver.auth.handler.UserAccessDeniedHandler;
import conopli.webserver.auth.handler.UserAuthenticationEntryPoint;
import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.auth.token.JwtTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig(JwtTokenizer jwtTokenizer, JwtAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    private final JwtTokenizer jwtTokenizer;

    private final JwtAuthorityUtils authorityUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/api/auth/verify-user")).hasRole("USER")
                .requestMatchers(
                        new AntPathRequestMatcher("/**")).permitAll()
                .and()
                .csrf().disable()
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                .accessDeniedHandler(new UserAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfig())
                .and()
                .httpBasic().disable()
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);
            builder.addFilterBefore(jwtVerificationFilter, SecurityContextHolderFilter.class);
        }
    }
}
