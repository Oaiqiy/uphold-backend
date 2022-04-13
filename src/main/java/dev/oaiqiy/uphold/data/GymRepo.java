package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GymRepo extends CrudRepository<Gym,Long> {
    Page<Gym> findAll(Pageable pageable);
}
