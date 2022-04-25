package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.MembershipRegister;
import dev.oaiqiy.uphold.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembershipRegisterRepo extends CrudRepository<MembershipRegister,Long> {
    List<MembershipRegister> findMembershipRegistersByUser(User user);
}
