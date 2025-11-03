package com.inyro.api.global.config;

import com.inyro.api.global.security.filter.CustomLoginFilter;
import com.inyro.api.global.security.exception.handler.JwtAccessDeniedHandler;
import com.inyro.api.global.security.exception.handler.JwtAuthenticationEntryPoint;
import com.inyro.api.global.security.filter.CustomLogoutHandler;
import com.inyro.api.global.security.filter.CustomLogoutSuccessHandler;
import com.inyro.api.global.security.filter.JwtAuthorizationFilter;
import com.inyro.api.global.security.jwt.JwtUtil;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 빈 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final Validator validator;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomLogoutHandler jwtLogoutHandler;
    private final CustomLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final RedisTemplate<String, String> redisTemplate;


    //인증이 필요하지 않은 url
    private final String[] allowUrl = {
            "/api/v1/auth/signup", // 회원가입
            "/api/v1/auth/login", //로그인
            "/api/v1/auth/reissue", // 토큰 재발급
            "/api/v1/auth/password/reset/code",
            "/api/usage",
            "/swagger-ui/**",   // swagger 관련 URL
            "/v3/api-docs/**",
            "/api/v1/auth/smul",
            "/api/v1/auth/password/reset/smul"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        CustomLoginFilter loginFilter = new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, validator);
        loginFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http
                .cors(cors -> cors.configurationSource(CorsConfig.apiConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // logout
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)
                )
                // end of logout
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}