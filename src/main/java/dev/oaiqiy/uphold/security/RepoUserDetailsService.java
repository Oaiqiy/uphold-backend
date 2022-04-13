package dev.oaiqiy.uphold.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RepoUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepo.findUserByPhone(username);
        if(user == null)
            throw new UsernameNotFoundException(username + "not found");
        return user;

    }

}
