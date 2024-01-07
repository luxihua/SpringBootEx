package org.zerock.club.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailHandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.util.JWTUtil;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user1")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        log.info("-------------------filterChain----------------------");
//
//        http.authorizeHttpRequests((auth) -> {
//            auth.requestMatchers("/sample/all").permitAll();
//            auth.requestMatchers("/sample/member").hasAnyRole("USER");
//            auth.requestMatchers("/sample/loginSuccess").authenticated();
//        });


        // formLogin
        http.formLogin(config -> {});
        // CSRF 토큰 비활성화
        http.csrf(config->config.disable());
        // FormLogout
        http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/sample/logout"));


        http.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                .successHandler((request, response, authentication) -> {
                    log.info("---------------OAuth2LoginSuccessHandler-----------------");
                    authentication.getAuthorities().forEach(authority -> {
                        log.info(authority);
                    });

                    log.info(authentication.getPrincipal());

                    log.info("----------------------------------------------");


                    response.sendRedirect("/sample/loginSuccess");
                })
                .successHandler(successHandler()) // 인증 인가 성공 후 handler 추가


        );
        http.rememberMe().tokenValiditySeconds(60*60*24*7); // 쿠키 유지 기간을 7일로 설정

        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        // AuthenticationManager 설정

        // ApiLoginFilter는 AbstractAuthenticaionProcessingFilter를 상속받고, 이를 위해선 반드시 AuthenticaionManager가 필요함
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // 반드시 필요
        http.authenticationManager(authenticationManager);

       //APILoginFilter
        // login 시 토큰 발행
        ApiLoginFilter apiLoginFilter =  new ApiLoginFilter("/api/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*", jwtUtil());

    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }





}
