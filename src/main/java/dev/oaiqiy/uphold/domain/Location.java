package dev.oaiqiy.uphold.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Location {

    public Location(Integer adcode) {
        this.adcode = adcode;
    }

    @Id
    private Integer adcode;

    private String name;

}
