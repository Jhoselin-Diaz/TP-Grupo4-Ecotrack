package com.example.tpgrupo4ecotrack.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/webjars/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        .requestMatchers("/consumos//mostrar/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/usuarios/inserta").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/usuarios/lista").hasAuthority("ADMIN")
                        .requestMatchers("/usuarios/actualizar/**").hasAuthority("USER")
                        .requestMatchers("/usuarios/eliminar/**").hasAuthority("ADMIN")
                        .requestMatchers("/categorias/inserta").hasAuthority("ADMIN")
                        .requestMatchers("/categorias/lista").hasAuthority("ADMIN")
                        .requestMatchers("/factores/inserta").hasAuthority("ADMIN")
                        .requestMatchers("/factores/actualizar/**").hasAuthority("ADMIN")
                        .requestMatchers("/factores/listar/**").hasAuthority("ADMIN")
                        .requestMatchers("/alimentos/inserta").hasAuthority("USER")
                        .requestMatchers("/alimentos/actualizar/**").hasAuthority("USER")
                        .requestMatchers("/alimentos/listaAdmin").hasAuthority("ADMIN")
                        .requestMatchers("/alimentos/MisAlimentos/**").hasAuthority("USER")
                        .requestMatchers("/alimentos/MisAlimentos").hasAuthority("USER")
                        .requestMatchers("/alimentos/eliminar/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/alimentos/MiActualizar/**").hasAuthority("USER")
                        .requestMatchers("/alimentos/total").hasAuthority("USER")
                        .requestMatchers("/ropas/registrar").hasAuthority("USER")
                        .requestMatchers("/ropas/actualizar").hasAuthority("USER")
                        .requestMatchers("/ropas/listaAdmin").hasAuthority("ADMIN")
                        .requestMatchers("/ropas/MisRopa").hasAuthority("USER")
                        .requestMatchers("/ropas/elimina/**").hasAuthority("USER")
                        .requestMatchers("/ropas/total").hasAuthority("USER")
                        .requestMatchers("/electrodomesticos/registrar").hasAuthority("USER")
                        .requestMatchers("/electrodomesticos/actualizar/").hasAuthority("USER")
                        .requestMatchers("/electrodomesticos/listaAdmin").hasAuthority("ADMIN")
                        .requestMatchers("/electrodomesticos/MisElectrodomestico").hasAuthority("USER")
                        .requestMatchers("/electrodomesticos/elimina/**").hasAuthority("USER")
                        .requestMatchers("/electrodomesticos/total").hasAuthority("USER")
                        .requestMatchers("/servicios/MisServicio").hasAuthority("USER")
                        .requestMatchers("/servicios/eliminar/**").hasAuthority("USER")
                        .requestMatchers("/servicios/total").hasAuthority("USER")
                        .requestMatchers("/servicios/lista").hasAuthority("USER")
                        .requestMatchers("/servicios/inserta").hasAuthority("USER")
                        .requestMatchers("/coches/inserta").hasAuthority("USER")
                        .requestMatchers("/coches/MisCoches").hasAuthority("USER")
                        .requestMatchers("/coches/elimina/**").hasAuthority("USER")
                        .requestMatchers("/coches/total").hasAuthority("ADMIN")
                        .requestMatchers("/coches/registrar").hasAuthority("USER")
                        .requestMatchers("/coches/actualizar").hasAuthority("USER")
                        .requestMatchers("/autobuses/registrar").hasAuthority("USER")
                        .requestMatchers("/autobuses/elimina/**").hasAuthority("USER")
                        .requestMatchers("/autobuses/total").hasAuthority("USER")
                        .requestMatchers("/autobuses/listaAdmin").hasAuthority("USER")
                        .requestMatchers("/autobuses/lista/MisAutobuses").hasAuthority("USER")
                        .requestMatchers("/autobuses/actualizar/**").hasAuthority("USER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(encoder);
        return authManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
