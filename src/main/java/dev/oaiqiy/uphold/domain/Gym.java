package dev.oaiqiy.uphold.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String introduction;

    @ManyToOne
    private Location location;
    private String detailLocation;

    private String mainPhone;
    private String sparePhone;

    @ManyToOne
    @JsonIgnore
    private User user;


    private Integer status;

    private String businessLicence;

    @OneToMany(mappedBy = "gym")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GymArea> gymAreas;

    @OneToMany(mappedBy = "gym")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<MembershipCard> membershipCards;


}
