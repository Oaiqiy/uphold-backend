package dev.oaiqiy.uphold.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final TokenCreator tokenCreator;

    public LoginAuthFilter(ObjectMapper objectMapper, TokenCreator tokenCreator, AuthenticationManager authenticationManager) {
        this.objectMapper = objectMapper;
        this.tokenCreator = tokenCreator;
        setFilterProcessesUrl("/user/login");
        setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken auth;
        try(InputStream inputStream = request.getInputStream()){
            LoginForm content = objectMapper.readValue(inputStream,LoginForm.class);
            auth = new UsernamePasswordAuthenticationToken(content.phone,content.password);
        } catch (IOException e) {
            auth = new UsernamePasswordAuthenticationToken("","");
        }

        try{
            return getAuthenticationManager().authenticate(auth);
        }catch (DisabledException e){
            throw new DisabledException("");
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = (User) authResult.getPrincipal();


        String token = tokenCreator.createToken(user.getPhone(),user.getPassword());
        log.info("user: " + user.getUsername() + " login successfully");

        ResultInfo<String> result = new ResultInfo<>(0,"login success",token);


        response.getWriter().append(objectMapper.writeValueAsString(result));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().append(objectMapper.writeValueAsString(new ResultInfo<String>(1,"login failure")));
    }
}

@AllArgsConstructor
@NoArgsConstructor
class LoginForm{
    public String phone;
    public String password;
}
