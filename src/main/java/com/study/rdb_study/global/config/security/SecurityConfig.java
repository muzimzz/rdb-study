package com.study.rdb_study.global.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");   // "application/json"
                            res.getWriter().write("{\"message\": \"로그인이 필요합니다.\"}");
                        })

                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"message\": \"잘못된 접근입니다.\"}");
                        })
                )

                .formLogin(form -> form
                        // .loginPage("/login") GetMapping: view 방식
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")

                        .successHandler((req, res, auth) -> {
                            res.setStatus(HttpServletResponse.SC_OK);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"message\": \"로그인 성공\"}");
                        })

                        .failureHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");
                            String message = e instanceof DisabledException ?
                                    "{\"message\": \"휴면 계정입니다.\"}" :
                                    "{\"message\": \"잘못된 이메일 또는 비밀번호\"}";
                            res.getWriter().write(message);
                        })

                )



                .logout(logout -> logout
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(HttpServletResponse.SC_OK); // 200
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"message\": \"로그아웃 성공\"}");
                        })
                )

                .authorizeHttpRequests(auth -> auth
                        // 정적 자원
                        .requestMatchers("/css/**", "/js/**", "/*.html", "/admin/*.html").permitAll()
                        // 공개 API
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/products").permitAll()
                        .requestMatchers("/members").permitAll()  // POST 회원가입
                        // 관리자 API
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 나머지 API (장바구니, 주문, 회원정보 수정 등)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
