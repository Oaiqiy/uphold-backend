package dev.oaiqiy.uphold.api.gym;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymAreaRepo;
import dev.oaiqiy.uphold.data.GymRepo;
import dev.oaiqiy.uphold.data.LocationRepo;
import dev.oaiqiy.uphold.domain.Gym;
import dev.oaiqiy.uphold.domain.GymArea;
import dev.oaiqiy.uphold.domain.Location;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.exception.GymNotFoundException;
import dev.oaiqiy.uphold.file.FileService;
import dev.oaiqiy.uphold.util.SecurityUtil;
import jdk.jfr.ContentType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/gym")
@Slf4j
public class GymInfoController {
    private final GymRepo gymRepo;
    private final LocationRepo locationRepo;
    private final FileService fileService;
    private final GymAreaRepo gymAreaRepo;

    @GetMapping("/list")
    public ResultInfo<List<Gym>> getGymInfos(Integer page, Integer size, Integer adcode){
        Page<Gym> gyms;
        if(adcode == null)
            gyms= gymRepo.findAll(PageRequest.of(page, size));
        else
            gyms = gymRepo.findGymsByLocation_Adcode(adcode,PageRequest.of(page, size));

        if(gyms.isEmpty())
            return new ResultInfo<>(1,"Request out of range");
        else
            return new ResultInfo<>(0,"Success",gyms.getContent());
    }

    @GetMapping("/info")
    public ResultInfo<?> getGymInfo(@NotNull Long id){
        Gym gym = gymRepo.findGymById(id);
        if(id == null)
            return ResultInfo.failure();
        else
            return new ResultInfo<>(0,"success",gym);
    }

    @PostMapping("/info")
    public ResultInfo<?> setGymInfo(@RequestBody GymInfo gymInfo){
        Gym gym = gymInfo.createGym();
        if(gym == null)
            return ResultInfo.failure();

        User user = SecurityUtil.user();
        gym.setUser(user);
        gym.setStatus(Gym.Status.UNVERIFIED);

        gymRepo.save(gym);

        return ResultInfo.success();
    }

    @PutMapping("/info")
    public ResultInfo<?> changeGymInfo(@RequestBody GymInfo gymInfo){
        User user = SecurityUtil.user();

        Gym gym = gymRepo.findByUser(user);
        if(gym == null)
            return ResultInfo.failure();

        gymInfo.changeGym(gym);

        gymRepo.save(gym);

        return ResultInfo.success();
    }

    @DeleteMapping("/info")
    public ResultInfo<?> deleteGymInfo(){
        User user = SecurityUtil.user();
        Gym gym = gymRepo.findByUser(user);
        if(gym == null)
            return ResultInfo.failure();
        gym.setStatus(Gym.Status.DELETED);
        gymRepo.save(gym);

        return ResultInfo.success();
    }

    @PostMapping("/headshot")
    public ResultInfo<?> uploadHeadshot(@RequestParam("file") @NotNull MultipartFile headshot){
        User user = SecurityUtil.user();
        Gym gym = gymRepo.findByUser(user);

        if(!headshot.getContentType().contains("image"))
            return new ResultInfo<>(1,"not a image");

        log.info(user.getPhone() + " upload gym headshot: " + headshot.getOriginalFilename()+ " size : " + headshot.getSize());

        fileService.delete("gym",gym.getId().toString(),"headshot");

        if(fileService.save(headshot, headshot.getOriginalFilename(),"gym",gym.getId().toString(),"headshot"))
            return new ResultInfo<>(0,"success");
        else
            return new ResultInfo<>(1,"uphold failure");

    }



    @GetMapping("/headshot/{id}")
    public ResponseEntity<Resource> getHeadShot(@PathVariable("id") Long id){

        Gym gym = gymRepo.findGymById(id);
        if(gym == null)
            return ResponseEntity.notFound().build();

        List<Resource> headshot = fileService.loadAll("gym",gym.getId().toString(),"headshot");
        Resource resource = headshot.get(0);

        try {
            Path path = resource.getFile().toPath();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path)).body(resource);
        } catch (IOException e) {
            log.warn("gym headshot read failure");
        }

        return ResponseEntity.notFound().build();

    }


    @PostMapping("/area")
    public ResultInfo<?> postGymAreaInfo(@RequestBody GymArea gymArea) throws GymNotFoundException {
        Gym gym = SecurityUtil.gym();
        gym.getGymAreas().add(gymArea);
        gymArea.setGym(gym);
        gymArea.setId(null);
        gymRepo.save(gym);
        return ResultInfo.failure();
    }

    @PutMapping("/area")
    public ResultInfo<?> changeGymAreaInfo(@RequestBody GymArea gymArea) throws GymNotFoundException {
        Gym gym = SecurityUtil.gym();
        GymArea area = gymAreaRepo.findGymAreaById(gymArea.getId());
        if(!gym.getId().equals(area.getGym().getId()))
            return ResultInfo.failure();
        area.change(gymArea);
        gymAreaRepo.save(area);

        return ResultInfo.success();
    }

    @DeleteMapping("/area")
    public ResultInfo<?> deleteGymAreaInfo(@NotNull Long id) throws GymNotFoundException {
        Gym gym = SecurityUtil.gym();

        gymAreaRepo.deleteByGymAndId(gym,id);

        return ResultInfo.success();
    }

    @PostMapping("/area/picture/{id}")
    public ResultInfo<?> postAreaPicture(@RequestParam("file") MultipartFile picture,@PathVariable("id") Long id) throws GymNotFoundException {
        Gym gym = SecurityUtil.gym();

        if(!picture.getContentType().contains("image"))
            return new ResultInfo<>(1,"not a image");

        fileService.save(picture,"gym",gym.getId().toString(),id.toString());

        return ResultInfo.success();
    }

    @GetMapping("/area/picture/{gym}/{id}/{filename}")
    public void getAreaPicture(@PathVariable("gym") Long gym,@PathVariable("id") Long id, @PathVariable("filename") String filename, HttpServletResponse httpServletResponse) throws IOException {
        if(filename == null){
            var pictures = fileService.loadAll("gym",gym.toString(),id.toString());
            if(pictures == null){
                httpServletResponse.setStatus(404);
                return;
            }
            var filenames = pictures.stream().map(r -> r.getFilename()).collect(Collectors.toList());ObjectMapper objectMapper = new ObjectMapper();
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(filenames));
            return;
        }

        var picture = fileService.load("gym",gym.toString(),id.toString(),filename);
        httpServletResponse.getOutputStream().write(picture.getInputStream().readAllBytes());
        httpServletResponse.setContentType(Files.probeContentType(picture.getFile().toPath()));

    }


    class GymInfo{
        private String name;
        private String introduction;
        private Integer adcode;
        private String detailLocation;
        private String mainPhone;
        private String sparePhone;

        public void changeGym(Gym gym){
            if(name != null)
                gym.setName(name);
            Location location = locationRepo.findByAdcode(adcode);
            if(location != null)
                gym.setLocation(location);

            gym.setIntroduction(introduction);

            gym.setMainPhone(mainPhone);
            gym.setSparePhone(sparePhone);
        }

        public Gym createGym(){
            if(name == null)
                return null;
            Location location = locationRepo.findByAdcode(adcode);
            if(location == null)
                return null;

            return new Gym(name,introduction,location,detailLocation,mainPhone,sparePhone);
        }
    }

}



