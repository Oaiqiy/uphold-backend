package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.GymAppointment;
import dev.oaiqiy.uphold.domain.GymAppointmentRecord;
import dev.oaiqiy.uphold.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GymAppointmentRecordRepo extends CrudRepository<GymAppointmentRecord,Long> {
    boolean existsByStatusAndGymAppointmentAndUser_Id(Integer status, GymAppointment gymAppointment, Long userId);
    Page<GymAppointmentRecord> findAllByUser(User user, Pageable pageable);
}
