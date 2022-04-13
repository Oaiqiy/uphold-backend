package dev.oaiqiy.uphold.security;

import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;
import java.rmi.server.ExportException;


@Slf4j
public class CommonAuthFilter extends BasicAuthenticationFilter {
    private final TokenCreator tokenCreator;
    private final UserRepo userRepo;

    public CommonAuthFilter(AuthenticationManager authenticationManager,TokenCreator tokenCreator,UserRepo userRepo) {
        super(authenticationManager);
        this.tokenCreator = tokenCreator;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Auth");
        UsernamePasswordAuthenticationToken  auth;



        try {
            if(token == null)
                throw new Exception(" has no token");
            String[] phoneAndPassword = tokenCreator.decodeTokenWithPassword(token);
            if(phoneAndPassword == null)
                throw new Exception(" use wrong token");
            User user = userRepo.findUserByPhone(phoneAndPassword[0]);

//            if(!user.getPassword().equals(phoneAndPassword[1]))
//                throw new Exception(" use expired token");

            auth = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
            auth.setDetails(user);

        }catch (Exception e){
            log.warn(request.getRemoteHost() + " request " + request.getRequestURI() + e.getMessage());
            auth = new UsernamePasswordAuthenticationToken(null,null,null);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request,response);
    }
}
