package dev.oaiqiy.uphold.api.gym;

import com.sun.istack.NotNull;
import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.Authority;
import dev.oaiqiy.uphold.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gym")
@Slf4j
@AllArgsConstructor
public class GymRegisterController {
    private final RedisTemplate<String,String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;


    @PostMapping("/register")
    public ResultInfo<String> register(@RequestBody GymRegisterForm form){

        String k = form.phone + ":code";
        String correct = redisTemplate.opsForValue().get(k);

        if(form.code.equals(correct)){
            redisTemplate.delete(k);
            User user = new User(form.phone, passwordEncoder.encode(form.password),null);
            user.addAuthority(new Authority(2));
            userRepo.save(user);
            log.info("gym user  : " + form.phone + " register successfully");
            return new ResultInfo<>(0,"success");
        }else{
            log.warn(form.phone + " register use wrong code: " + form.code);
            return new ResultInfo<>(1,"Wrong code");
        }

    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class GymRegisterForm{
    @NotNull
    public String phone;
    @NotNull
    public String password;
    @NotNull
    public String code;
}
