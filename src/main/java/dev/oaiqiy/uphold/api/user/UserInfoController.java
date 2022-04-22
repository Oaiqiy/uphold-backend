package dev.oaiqiy.uphold.api.user;

import com.sun.istack.NotNull;
import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.file.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserInfoController {
    private final UserRepo userRepo;
    private final FileService fileService;

    @PostMapping("/info")
    public ResultInfo<String> changeInfo(@RequestBody UserInfo userInfo){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        userInfo.changeUser(user);
        userRepo.save(user);
        log.info(user.getPhone() + " changed user info");
        return new ResultInfo<>(0,"success");
    }

    @GetMapping("/info")
    public ResultInfo<User> getInfo(Long id){
        User user;
        if(id == null)
            user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        else{
            user = userRepo.findUserById(id);
            if(user == null)
                return new ResultInfo<>(1,"no such user");
        }
        return new ResultInfo<>(0,"success",user);
    }

    @PostMapping("/headshot")
    public ResultInfo<String> uploadHeadshot(@RequestParam("file") @NotNull MultipartFile headshot){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

        log.info(headshot.getOriginalFilename());
        log.info(headshot.getContentType());


        if(!headshot.getContentType().contains("image"))
            return new ResultInfo<>(1,"not a image");

        log.info(user.getPhone() + " upload headshot: " + headshot.getOriginalFilename()+ " size : " + headshot.getSize());
        if(fileService.save("headshot",headshot,user.getId().toString()))
            return new ResultInfo<>(0,"success");

        return new ResultInfo<>(1,"uphold failure");

    }

    @GetMapping("headshot")
    public void getHeadShot(Long id,HttpServletResponse httpServletResponse){
        if(id == null)
            id = ((User) SecurityContextHolder.getContext().getAuthentication().getDetails()).getId();


    }

//    @PostMapping("/headshot")
//    public void uploadHeadshot(@RequestParam("file") @NotNull MultipartFile headshot, HttpServletResponse httpServletResponse) throws Exception{
//
//       // httpServletResponse.getWriter().write(new String(headshot.getInputStream().readAllBytes()));
//        httpServletResponse.setContentType(headshot.getContentType());
//        httpServletResponse.getOutputStream().write(headshot.getInputStream().readAllBytes());
//    }

}


@AllArgsConstructor
@NoArgsConstructor
class UserInfo{
    public String nickname;
    public Boolean sex;
    public Integer age;

    public void changeUser(User user){
        if(nickname != null)
            user.setNickname(nickname);
        if(sex != null)
            user.setSex(sex);
        if(age != null && age > 0)
            user.setAge(age);

    }
}