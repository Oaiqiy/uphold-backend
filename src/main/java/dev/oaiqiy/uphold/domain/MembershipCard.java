package dev.oaiqiy.uphold.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
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
    @JsonIdentityReference(alwaysAsId = true)
    private Gym gym;
}
