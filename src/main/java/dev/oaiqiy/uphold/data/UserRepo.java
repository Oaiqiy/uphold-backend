package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User,Long> {
    User findUserById(Long id);
    User findUserByPhone(String phone);
    boolean existsUserByPhone(String phone);
}
