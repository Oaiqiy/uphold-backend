package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.Gym;
import dev.oaiqiy.uphold.domain.GymArea;
import org.springframework.data.repository.CrudRepository;

public interface GymAreaRepo extends CrudRepository<GymArea,Long> {
    GymArea findGymAreaById(Long id);

    void deleteByGymAndId(Gym gym, Long id);
}
