package dev.oaiqiy.uphold.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GymArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String introduction;

    @JsonIgnore
    @ManyToOne(targetEntity = Gym.class)
    private Gym gym;

    public void change(GymArea area){
        if(area.getIntroduction() != null)
            introduction = area.introduction;
        if(area.getName() != null)
            name = area.name;
    }

}
