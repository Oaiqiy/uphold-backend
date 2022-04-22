package dev.oaiqiy.uphold.api.appointment;

import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymAppointmentRepo;
import dev.oaiqiy.uphold.data.GymAreaRepo;
import dev.oaiqiy.uphold.domain.GymAppointment;
import dev.oaiqiy.uphold.domain.GymAppointmentRecord;
import dev.oaiqiy.uphold.domain.GymArea;
import dev.oaiqiy.uphold.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.Date;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/appointment")
public class GymAppointmentController {

    private final GymAreaRepo gymAreaRepo;
    private final GymAppointmentRepo gymAppointmentRepo;

    @PostMapping("/publish")
    public ResultInfo<?> publishAppointment(@RequestBody AppointmentForm appointmentForm){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();


        GymArea gymArea = gymAreaRepo.findGymAreaById(appointmentForm.getAreaId());

        if(gymArea == null || !Objects.equals(gymArea.getGym().getUser().getId(), user.getId()))
            return ResultInfo.failure();


        GymAppointment appointment = new GymAppointment();
        appointment.setAppointed(0);
        appointment.setCount(appointmentForm.getCount());
        appointment.setStartTime(appointment.getStartTime());
        appointment.setEndTime(appointment.getEndTime());
        appointment.setGymArea(gymArea);

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
