package dev.oaiqiy.uphold.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MembershipCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer duration;
    private Integer price;

    @ManyToOne
    private Gym gym;
}
