package dev.oaiqiy.uphold.util;

import dev.oaiqiy.uphold.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User user(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
