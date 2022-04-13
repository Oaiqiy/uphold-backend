package dev.oaiqiy.uphold.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class GymAppointmentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private GymAppointment gymAppointment;

    private Integer Status;
    private Date inTime;
    private Date outTime;
}
