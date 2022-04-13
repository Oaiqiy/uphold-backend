package dev.oaiqiy.uphold;

import dev.oaiqiy.uphold.data.UserRepo;
import dev.oaiqiy.uphold.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseTest {
    @Autowired
    private UserRepo userRepo;

    @Test
    public void userTest(){
        User user = new User("1234","1234","mhl");
        userRepo.save(user);
    }
}
