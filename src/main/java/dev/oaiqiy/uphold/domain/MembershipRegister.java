package dev.oaiqiy.uphold.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class MembershipRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private MembershipCard membershipCard;

    private Date registerAt;

    @PrePersist
    public void createAt(){
        registerAt = new Date();
    }

    public boolean check(Long id){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registerAt);
        calendar.add(Calendar.DATE,membershipCard.getDuration());

        if(calendar.before(new Date()))
            return false;

        return membershipCard.getGym().getId().equals(id);

    }
}
