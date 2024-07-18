package geulsam.archive.global.security;

import geulsam.archive.global.exception.JwtAuthenticationEntryPoint;
import geulsam.archive.global.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 비밀번호 암호화에 사용할 수 있는 encoder
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * FilterChain 을 설정
     * httpBasic, csrf, formLogin, session 기능들을 전부 비활성화 후, custom Filter 를 설정
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .sessionManagement(SessionManagementConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                // preFlight error 해결
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                // 로그인, 회원가입은 일단 전체 허용 -> 회원가입은 deploy hasRole("master")로 이동
                                .requestMatchers(HttpMethod.POST,"/user/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                                // 유저 refresh 토큰 받아서 새로운 토큰 생성(초기 로그인)
                                .requestMatchers(HttpMethod.GET, "/user/refresh").permitAll()
                                .requestMatchers(HttpMethod.GET, "/user/check").authenticated()
                                .requestMatchers(HttpMethod.GET, "/poster/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/poster").hasRole("NORMAL")
                                .requestMatchers(HttpMethod.DELETE, "/poster").hasRole("NORMAL")
                                .requestMatchers(HttpMethod.POST, "/book").hasRole("NORMAL")
                                .requestMatchers(HttpMethod.DELETE, "/book").hasRole("NORMAL")
                                .requestMatchers(HttpMethod.GET, "/book/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/content/**").permitAll()
                                // 일정 관련
                                .requestMatchers(HttpMethod.POST, "/calendar").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/calendar").permitAll()
                                .requestMatchers(HttpMethod.GET, "/calendar").permitAll()
                                .requestMatchers(HttpMethod.POST, "/calendar/criticism").permitAll()
                                // 합평회 관련
                                .requestMatchers(HttpMethod.POST, "/criticismAuthor").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/criticismAuthor").authenticated()
                                // swagger 테스트 -> 추후 삭제
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/v2/api-docs",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**").permitAll()
                                .requestMatchers("/user/testing").hasRole("NORMAL")
                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );

        return httpSecurity.build();
    }
}
