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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    @Value("${spring.security.swagger.user}")
    private String swaggerUser;

    @Value("${spring.security.swagger.pass}")
    private String swaggerPass;

    //인증이 필요하지 않은 url
    private final String[] allowUrl = {
            "/api/v1/auth/signup", // 회원가입
            "/api/v1/auth/login", //로그인
            "/api/v1/auth/reissue", // 토큰 재발급
            "/api/v1/auth/password/reset/code",
            "/api/usage",
//            "/swagger-ui/**",   // swagger 관련 URL
//            "/v3/api-docs/**",
            "/api/v1/auth/smul",
            "/api/v1/auth/password/reset/smul"
    };

    @Bean
    @Order(1)
    public SecurityFilterChain swaggerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Swagger 관련 요청만 여기에 매칭
                .securityMatcher("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                // 폼 로그인 활성화
                .formLogin(form -> form
                        .defaultSuccessUrl("/swagger-ui/index.html", true)
                        .permitAll()
                )
                .logout(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityfilterChain(HttpSecurity http) throws Exception{
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

    @Bean
    public UserDetailsService swaggerUsers() {
        UserDetails swaggerUserDetails = User.builder()
                .username(swaggerUser)
                .password("{noop}" + swaggerPass) // 암호화 안함(noop)
                .roles("SWAGGER")
                .build();

        return new InMemoryUserDetailsManager(swaggerUserDetails);
    }
}