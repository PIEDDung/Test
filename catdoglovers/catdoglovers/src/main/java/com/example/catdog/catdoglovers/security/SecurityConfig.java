package com.example.catdog.catdoglovers.security;

import com.example.catdog.catdoglovers.exhandler.StatusFalseException;
import com.example.catdog.catdoglovers.model.Account;
import com.example.catdog.catdoglovers.model.AuthenticationProvider;
import com.example.catdog.catdoglovers.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private CustomerUserDetailsService customerUserDetailsService;

    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private Oauth2UserService oauth2UserService;

    @Autowired
    public SecurityConfig(CustomerUserDetailsService customerUserDetailsService,
                          CustomOAuth2UserService oAuth2UserService) {
        this.customerUserDetailsService = customerUserDetailsService;
        this.oAuth2UserService = oAuth2UserService;
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/register", "/", "/register/**","/posts","/user-photos/**",
                                "/css/**", "/js/**", "/vendor/**", "/scss/**", "/error",
                                "/verify", "/verify/**").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/posts")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                if (exception.getCause() instanceof StatusFalseException) {
                                    response.sendRedirect("/error"); // replace '/errorPage' with the path to your error page
                                } else {
                                    response.sendRedirect("/login?error=true");
                                }
                            }
                            }
                        )
                        .permitAll())
                .oauth2Login(auth -> auth
                        .loginPage("/login")
                        .defaultSuccessUrl("/posts")
                        .userInfoEndpoint(user -> user.userService(oAuth2UserService))
                        .successHandler(new AuthenticationSuccessHandler() {

                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                CustomOauth2User o2User =(CustomOauth2User) authentication.getPrincipal();
                                String userEmail = o2User.getEmail();
                                String name = o2User.getName();

                                oauth2UserService.processAfterGoogleLogin(userEmail, name);
                                response.sendRedirect("/posts");
                            }
                        })
                        .permitAll())
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll())
//                .exceptionHandling(exception -> exception.accessDeniedPage("/error"));
        ;
        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customerUserDetailsService).passwordEncoder(passwordEncoder());
    }




}