package dev.oaiqiy.uphold.api.user;

import dev.oaiqiy.uphold.api.ResultInfo;
import dev.oaiqiy.uphold.data.GymAppointmentRecordRepo;
import dev.oaiqiy.uphold.data.GymAppointmentRepo;
import dev.oaiqiy.uphold.data.MembershipRegisterRepo;
import dev.oaiqiy.uphold.domain.*;
import dev.oaiqiy.uphold.security.TokenCreator;
import dev.oaiqiy.uphold.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/appointment")
@AllArgsConstructor
@Slf4j
public class UserAppointmentController {
    private final GymAppointmentRepo gymAppointmentRepo;
    private final GymAppointmentRecordRepo gymAppointmentRecordRepo;
    private final MembershipRegisterRepo membershipRegisterRepo;
    private final TokenCreator tokenCreator;


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
    private ResultInfo<List<GymAppointmentRecord>> getRecordList(Integer page,Integer size, Long id){
        User user = SecurityUtil.user();

        if(id != null){
            Optional<GymAppointmentRecord> record = gymAppointmentRecordRepo.findById(id);
            if(record.isEmpty())
                return new ResultInfo<>(2,"no such record");
            List<GymAppointmentRecord> result = new ArrayList<>(1);
            result.add(record.get());
            return new ResultInfo<>(0,"success",result);

        }

        Page<GymAppointmentRecord> records = gymAppointmentRecordRepo.findAllByUser(user, PageRequest.of(page,size));

        if(records.isEmpty())
            return new ResultInfo<>(1,"out of range");
        else
            return new ResultInfo<>(0,"success",records.getContent());

    }

    @GetMapping("/check")
    public ResultInfo<String> signIn(Long id){
        User user = SecurityUtil.user();
        Optional<GymAppointmentRecord> gymAppointmentRecord = gymAppointmentRecordRepo.findById(id);
        if(gymAppointmentRecord.isEmpty())
            return new ResultInfo<>(0,"failure");

        GymAppointmentRecord record = gymAppointmentRecord.get();

        if(!record.getUser().getId().equals(user.getId()))
            return new ResultInfo<>(0,"failure");

        String token = tokenCreator.createToken(user.getPhone(),id.toString());

        return new ResultInfo<>(0,"success",token);

    }




    @GetMapping("/info")
    public ResultInfo<List<GymAppointment>> getAppointmentInfo(Long id, Long appointment){
        List<GymAppointment> result = new ArrayList<>();
        User user = SecurityUtil.user();
        if(id == null && appointment == null){
            Set<Gym> gyms = membershipRegisterRepo.findMembershipRegistersByUser(user)
                    .stream().filter(MembershipRegister::check)
                    .map(m->m.getMembershipCard().getGym())
                    .collect(Collectors.toSet());
            for(Gym gym : gyms)
                result.addAll(gymAppointmentRepo.findGymAppointmentsByGymArea_Gym(gym));
        }
        else if(id != null)
            result.addAll(gymAppointmentRepo.findGymAppointmentsByGymArea_Gym_Id(id));
        else {
            Optional<GymAppointment> gymAppointment = gymAppointmentRepo.findById(appointment);
            gymAppointment.ifPresent(result::add);
        }


        if(result.isEmpty())
            return new ResultInfo<>(1,"no such appointment");
        else
            return new ResultInfo<>(0,"success",result);

    }


}
