package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.GymArea;
import org.springframework.data.repository.CrudRepository;

public interface GymAreaRepo extends CrudRepository<GymArea,Long> {
    GymArea findGymAreaById(Long id);
}
