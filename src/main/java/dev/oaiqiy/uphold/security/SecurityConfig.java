package dev.oaiqiy.uphold.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oaiqiy.uphold.data.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final RepoUserDetailsService repoUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final TokenCreator tokenCreator;
    private final UserRepo userRepo;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(repoUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/code","/user/register").access("permitAll()")
                .antMatchers("/**","/").access("hasRole('ROLE_USER')")
                .and().addFilterAt(createLoginAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(createCommonAuthFilter())
                .csrf().disable();
    }


    private LoginAuthFilter createLoginAuthFilter() {
        try {
            return new LoginAuthFilter(objectMapper,tokenCreator,authenticationManagerBean());
        } catch (Exception e) {
            return null;
        }
    }

    private CommonAuthFilter createCommonAuthFilter(){
        try {
            return new CommonAuthFilter(authenticationManagerBean(),tokenCreator,userRepo);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
