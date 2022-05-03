package dev.oaiqiy.uphold.api.user;

import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymRepo;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.Gym;
import dev.oaiqiy.uphold.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/collect")
@AllArgsConstructor
@Slf4j
public class UserCollectionController {
    private final UserRepo userRepo;
    private final GymRepo gymRepo;

    @GetMapping
    public ResultInfo<?> addCollection(Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        Gym gym = gymRepo.findGymById(id);
        if(gym == null)
            return new ResultInfo<>(0,"no such gym");

        if(user.getCollection().stream().anyMatch(g -> g.getId().equals(id)))
            return ResultInfo.failure();

        user.getCollection().add(gym);

        userRepo.save(user);

        log.info("user: " + user.getPhone() + " collect " + id);
        return ResultInfo.success();

    }

    @PostMapping
    public ResultInfo<?> deleteCollection(Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        Gym gym = gymRepo.findGymById(id);
        if(gym == null)
            return new ResultInfo<>(0,"no such gym");


        user.getCollection().removeIf(g -> g.getId().equals(id));
        userRepo.save(user);

        log.info("user: " + user.getPhone() + "cancel collect " + id);
        return ResultInfo.success();
    }
}
