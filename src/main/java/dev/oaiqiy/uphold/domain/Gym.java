package dev.oaiqiy.uphold.domain;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Location location;
    private String detailLocation;

    private String mainPhone;
    private String sparePhone;

    @ManyToOne
    private User user;

    private Integer status;

    private String businessLicence;

    @OneToMany
    private List<GymArea> gymAreas;



}
