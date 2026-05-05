package com.example.talentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação.
 *
 * <p>Papéis disponíveis e suas permissões:</p>
 * <ul>
 *   <li><b>ROLE_ADMIN</b>  — acesso total a todas as funcionalidades</li>
 *   <li><b>ROLE_GESTOR</b> — gerenciamento de servidores e oportunidades</li>
 *   <li><b>ROLE_USUARIO</b>— cadastro de informações próprias e inscrições</li>
 * </ul>
 *
 * <p>Usuários de teste (in-memory):</p>
 * <pre>
 *   admin   / admin123   → ROLE_ADMIN
 *   gestor  / gestor123  → ROLE_GESTOR
 *   usuario / usuario123 → ROLE_USUARIO
 * </pre>
 *
 * <p>Autenticação via HTTP Basic. Controle fino de acesso via
 * {@code @PreAuthorize} nos controllers.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Encoder de senha usando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Usuários em memória para demonstração dos três papéis.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        var gestor = User.builder()
                .username("gestor")
                .password(encoder.encode("gestor123"))
                .roles("GESTOR")
                .build();

        var usuario = User.builder()
                .username("usuario")
                .password(encoder.encode("usuario123"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, gestor, usuario);
    }

    /**
     * Cadeia de filtros de segurança HTTP.
     *
     * <ul>
     *   <li>CSRF desabilitado (API REST stateless)</li>
     *   <li>Sessão STATELESS (sem cookies de sessão)</li>
     *   <li>GET /talentos/info é público (permitAll)</li>
     *   <li>Qualquer outra requisição exige autenticação</li>
     *   <li>Controle granular feito via @PreAuthorize nos controllers</li>
     * </ul>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoint público do sistema
                .requestMatchers(HttpMethod.GET, "/talentos/info").permitAll()
                // Todos os demais endpoints exigem autenticação;
                // a autorização por papel é controlada via @PreAuthorize
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}
