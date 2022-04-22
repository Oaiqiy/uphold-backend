package dev.oaiqiy.uphold.api.user;

import com.sun.istack.NotNull;
import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.Authority;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.sms.ShortMessageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserRegisterController {
    private final UserRepo userRepo;
    private final ShortMessageService shortMessageService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String,String> redisTemplate;


    @GetMapping("/check")
    public ResultInfo<String> checkPhone(String phone){
        return userRepo.existsUserByPhone(phone) ? new ResultInfo<>(1,"existing phone") : new ResultInfo<>(0,"usable phone");
    }

    @GetMapping("/code")
    public ResultInfo<String> getVerificationCode(String phone){
        if(userRepo.existsUserByPhone(phone))
            return new ResultInfo<>(1,"Repeated phone.");
        shortMessageService.sendRegisterMessage(phone);
        return new ResultInfo<>(0,"success");
    }

    @PostMapping("/register")
    public ResultInfo<String> register(@RequestBody RegisterForm form){

        String k = form.phone + ":code";
        String correct = redisTemplate.opsForValue().get(k);

        if(form.code.equals(correct)){
            redisTemplate.delete(k);
            User user = new User(form.phone, passwordEncoder.encode(form.password),null);
            user.addAuthority(new Authority(1));
            userRepo.save(user);
            log.info("user: " + form.phone + " register successfully");
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
class RegisterForm{
    @NotNull
    public String phone;
    @NotNull
    public String password;
    @NotNull
    public String code;
}
