package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.Gym;
import dev.oaiqiy.uphold.domain.GymAppointment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GymAppointmentRepo extends CrudRepository<GymAppointment,Long> {
    List<GymAppointment> findGymAppointmentsByGymArea_Gym_Id(Long id);
    List<GymAppointment> findGymAppointmentsByGymArea_Gym(Gym gym);

}
