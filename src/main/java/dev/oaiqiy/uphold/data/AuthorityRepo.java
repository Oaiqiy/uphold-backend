package dev.oaiqiy.uphold.data;

import dev.oaiqiy.uphold.domain.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepo extends CrudRepository<Authority,Integer> {
    Authority findAuthorityByAuthority(String authority);
}
