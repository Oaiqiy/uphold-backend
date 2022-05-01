package dev.oaiqiy.uphold.util;

import dev.oaiqiy.uphold.data.GymRepo;
import dev.oaiqiy.uphold.domain.Gym;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.exception.GymNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
public class SecurityUtil {
    private final GymRepo gymRepoTemp;
    private static GymRepo gymRepo;

    @PostConstruct
    public void init(){
        gymRepo = gymRepoTemp;
    }

    public static User user(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public static Gym gym() throws GymNotFoundException {
        Gym g = gymRepo.findByUser(user());
        if(g == null)
            throw new GymNotFoundException();
        return g;
    }
}
