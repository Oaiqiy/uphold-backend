package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.GymAppointment;
import org.springframework.data.repository.CrudRepository;

public interface GymAppointmentRepo extends CrudRepository<GymAppointment,Long> {
}
