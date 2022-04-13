package dev.oaiqiy.uphold.domain;

import lombok.Data;

import javax.persistence.*;

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
}
