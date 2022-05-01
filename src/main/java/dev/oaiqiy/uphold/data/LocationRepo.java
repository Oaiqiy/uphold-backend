package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepo extends CrudRepository<Location,Integer> {
    Location findByAdcode(Integer adcode);
}
