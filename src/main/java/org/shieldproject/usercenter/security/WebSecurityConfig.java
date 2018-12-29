package org.shieldproject.usercenter.security;

import org.shieldproject.usercenter.security.handler.LoginSuccessHandler;
import org.shieldproject.usercenter.security.handler.LogoutSuccessHandler;
import org.shieldproject.usercenter.security.handler.UserSecurityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/captcha/**", "/user/login", "/css/**", "/images/**", "/js/**", "/laydate/**", "/layer/**", "/token/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(loginSuccessHandler())//code3
                .and()
                .logout().invalidateHttpSession(true).logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenValiditySeconds(1209600)
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Autowired
    UserSecurityHandler userSecurityHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityHandler).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
    }
}