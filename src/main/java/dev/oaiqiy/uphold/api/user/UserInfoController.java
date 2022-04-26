package dev.oaiqiy.uphold.api.user;

import com.sun.istack.NotNull;
import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.file.FileService;
import dev.oaiqiy.uphold.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
    public ResultInfo<?> uploadHeadshot(@RequestParam("file") @NotNull MultipartFile headshot){
        User user = SecurityUtil.user();

        if(!headshot.getContentType().contains("image"))
            return new ResultInfo<>(1,"not a image");

        log.info(user.getPhone() + " upload headshot: " + headshot.getOriginalFilename()+ " size : " + headshot.getSize());

        fileService.delete("headshot",user.getId().toString());

        if(fileService.save(headshot, headshot.getOriginalFilename(),"headshot",user.getId().toString()))
            return new ResultInfo<>(0,"success");
        else
            return new ResultInfo<>(1,"uphold failure");

    }

    @GetMapping("headshot")
    public ResponseEntity<Resource> getHeadShot(Long id){
        if(id == null)
            id = SecurityUtil.user().getId();

        List<Resource> headshot = fileService.loadAll("headshot",id.toString());
        Resource resource = headshot.get(0);

        try {
            Path path = resource.getFile().toPath();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path)).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();

    }


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