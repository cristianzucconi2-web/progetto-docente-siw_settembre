package it.uniroma3.siw.authentication;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credentials.DEFAULT_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
                .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/api/**").permitAll()

                        // Pagine pubbliche
                        .requestMatchers("/", "/index", "/login", "/register", "/logout", "/403", "/error",
                                "/festivals", "/festival/**",
                                "/films", "/film/**",
                                "/registi", "/regista/**",
                                "/sale", "/sala/**",
                                "/proiezioni", "/proiezione/**",
                                "/cerca/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()

                        // Area Admin
                        .requestMatchers("/admin/**").hasAuthority(ADMIN_ROLE)

                        // Operazioni Recensioni Utente Loggato
                        .requestMatchers(HttpMethod.POST, "/film/*/recensione").hasAnyAuthority(ADMIN_ROLE, DEFAULT_ROLE)
                        .requestMatchers("/recensione/modifica/**", "/recensione/elimina/**").hasAnyAuthority(ADMIN_ROLE, DEFAULT_ROLE)

                        // Qualsiasi altra richiesta
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login").permitAll().defaultSuccessUrl("/success", true)
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                        .clearAuthentication(true).permitAll())
                .exceptionHandling(
                        exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/403");
                        }));
        return httpSecurity.build();
    }
}
