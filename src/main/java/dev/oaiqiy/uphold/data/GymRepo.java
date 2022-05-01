package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.Gym;
import dev.oaiqiy.uphold.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GymRepo extends CrudRepository<Gym,Long> {

    Page<Gym> findAll(Pageable pageable);
    Page<Gym> findGymsByLocation_Adcode(Integer adcode,Pageable pageable);
    Gym findGymById(Long id);
    Gym findByUser(User user);
}
