package dev.oaiqiy.uphold;

import dev.oaiqiy.uphold.data.*;
import dev.oaiqiy.uphold.domain.*;
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
    private final GymAreaRepo gymAreaRepo;
    private final PasswordEncoder passwordEncoder;
    private final GymRepo gymRepo;
    private final MembershipCardRepo membershipCardRepo;
    private final MembershipRegisterRepo membershipRegisterRepo;

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

                mhl.addAuthority(role_user);
                userRepo.save(mhl);
                mzy.addAuthority(role_user);
                userRepo.save(mzy);

                Gym gym = new Gym();
                gym.setName("北邮健身房");
                gym.setIntroduction("不来白不来");
                gym.setUser(mzy);
                gymRepo.save(gym);

                mhl.getCollection().add(gym);
                userRepo.save(mhl);


                GymArea gymArea = new GymArea();
                gymArea.setGym(gym);
                gymArea.setName("健身区");
                gymArea.setIntroduction("好地方");

                gymAreaRepo.save(gymArea);

                MembershipCard membershipCard = new MembershipCard();
                membershipCard.setGym(gym);
                membershipCard.setName("一年会员");
                membershipCard.setPrice(998);
                membershipCard.setDuration(365);

                membershipCardRepo.save(membershipCard);

                MembershipRegister membershipRegister = new MembershipRegister();
                membershipRegister.setUser(mhl);
                membershipRegister.setMembershipCard(membershipCard);

                membershipRegisterRepo.save(membershipRegister);

            }
        };

    }
}
