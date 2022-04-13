package dev.oaiqiy.uphold.domain;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class GymAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private GymArea gymArea;


    private Date startTime;
    private Date endTime;

    private Integer count;
}
