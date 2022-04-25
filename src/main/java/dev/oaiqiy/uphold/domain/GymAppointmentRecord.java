package dev.oaiqiy.uphold.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class GymAppointmentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private GymAppointment gymAppointment;

    private Integer status;
    private Date inTime;
    private Date outTime;

    public GymAppointmentRecord(User user, GymAppointment gymAppointment, Integer status) {
        this.user = user;
        this.gymAppointment = gymAppointment;
        this.status = status;
    }
}
