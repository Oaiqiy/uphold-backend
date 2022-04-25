package dev.oaiqiy.uphold.api.gym;

import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymAppointmentRepo;
import dev.oaiqiy.uphold.data.GymAreaRepo;
import dev.oaiqiy.uphold.domain.GymAppointment;
import dev.oaiqiy.uphold.domain.GymArea;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/gym/appointment")
public class GymAppointmentController {

    private final GymAreaRepo gymAreaRepo;
    private final GymAppointmentRepo gymAppointmentRepo;

    @PostMapping
    public ResultInfo<?> publishAppointment(@RequestBody AppointmentForm appointmentForm){
        User user = SecurityUtil.user();

        GymArea gymArea = gymAreaRepo.findGymAreaById(appointmentForm.getAreaId());

        if(gymArea == null || !Objects.equals(gymArea.getGym().getUser().getId(), user.getId()))
            return ResultInfo.failure();

        GymAppointment appointment = new GymAppointment(gymArea,appointmentForm.getStartTime(),appointmentForm.getEndTime(),appointmentForm.getCount(),0);

        gymAppointmentRepo.save(appointment);

        return ResultInfo.success();

    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AppointmentForm{
    private Long areaId;
    private Date startTime;
    private Date endTime;
    private Integer count;
}
