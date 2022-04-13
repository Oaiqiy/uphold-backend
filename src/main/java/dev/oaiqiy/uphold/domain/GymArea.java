package dev.oaiqiy.uphold.domain;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;

@Entity
@Data
public class GymArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String introduction;

    @ManyToOne
    private Gym gym;

}
