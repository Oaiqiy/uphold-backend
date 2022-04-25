package dev.oaiqiy.uphold.api.user;

import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymAppointmentRecordRepo;
import dev.oaiqiy.uphold.data.GymAppointmentRepo;
import dev.oaiqiy.uphold.domain.GymAppointment;
import dev.oaiqiy.uphold.domain.GymAppointmentRecord;
import dev.oaiqiy.uphold.domain.User;
import dev.oaiqiy.uphold.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/appointment")
@AllArgsConstructor
@Slf4j
public class UserAppointmentController {
    private final GymAppointmentRepo gymAppointmentRepo;
    private final GymAppointmentRecordRepo gymAppointmentRecordRepo;


    @PostMapping
    public ResultInfo<?> appoint(Long id){
        Optional<GymAppointment> gymAppointment = gymAppointmentRepo.findById(id);


        if(gymAppointment.isEmpty())
            return ResultInfo.failure();

        GymAppointment appointment = gymAppointment.get();

        if(!appointment.getStartTime().before(new Date()))
            return ResultInfo.failure();

        User user = SecurityUtil.user();

        // judge if user register this gym
        if(user.getMembershipRegisters().stream().noneMatch(m -> m.check(id)))
            return ResultInfo.failure();

        if(gymAppointmentRecordRepo.existsByStatusAndGymAppointmentAndUser_Id(0,appointment,user.getId()))
            return ResultInfo.failure();

        if(!appointment.appointedIncr())
            return ResultInfo.failure();

        gymAppointmentRepo.save(appointment);

        GymAppointmentRecord gymAppointmentRecord = new GymAppointmentRecord(user,appointment,0);
        gymAppointmentRecordRepo.save(gymAppointmentRecord);

        log.info(user.getPhone() + " appoint successfully : " + id);

        return ResultInfo.success();

    }

    @DeleteMapping
    public ResultInfo<?> cancelAppointment(Long id){
        Optional<GymAppointmentRecord> option = gymAppointmentRecordRepo.findById(id);
        if(option.isEmpty())
            return ResultInfo.failure();
        GymAppointmentRecord record = option.get();
        User user = SecurityUtil.user();
        if(!record.getUser().getId().equals(user.getId()))
            return ResultInfo.failure();

        record.setStatus(1);
        record.getGymAppointment().appointedDecr();
        gymAppointmentRecordRepo.save(record);

        log.info(user.getPhone() + " cancel appointment " + id);

        return ResultInfo.success();

    }

    @GetMapping
    private ResultInfo<List<GymAppointmentRecord>> getRecordList(Integer page,Integer size){
        User user = SecurityUtil.user();

        Page<GymAppointmentRecord> records = gymAppointmentRecordRepo.findAllByUser(user, PageRequest.of(page,size));

        if(records.isEmpty())
            return new ResultInfo<>(1,"out of range");
        else
            return new ResultInfo<>(0,"success",records.getContent());

    }


}
