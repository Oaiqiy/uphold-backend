package dev.oaiqiy.uphold.api.gym;

import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymRepo;
import dev.oaiqiy.uphold.domain.Gym;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/gym")
public class GymInfoController {
    private final GymRepo gymRepo;

    @GetMapping("/list")
    public ResultInfo<List<Gym>> getGymInfos(Integer page, Integer size){
        Page<Gym> gyms = gymRepo.findAll(PageRequest.of(page, size));
        if(gyms.isEmpty())
            return new ResultInfo<>(1,"Request out of range");
        else
            return new ResultInfo<>(0,"Success",gyms.getContent());
    }




}
