package dev.oaiqiy.uphold;

import dev.oaiqiy.uphold.data.AuthorityRepo;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.Authority;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.security.TokenCreator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class Init {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;
    private final TokenCreator tokenCreator;


    @Bean
    public CommandLineRunner userInit(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Authority role_user = new Authority();
                role_user.setAuthority("ROLE_USER");
                authorityRepo.save(role_user);


                User mhl = new User("123",passwordEncoder.encode("123"),"mhl");
                User mzy = new User("1235",passwordEncoder.encode("123"),"mzy");

                mhl.addAuthority(entityManager.find(Authority.class,1));
                userRepo.save(mhl);
                mzy.addAuthority(entityManager.find(Authority.class,1));
                userRepo.save(mzy);

            }
        };

    }
}
