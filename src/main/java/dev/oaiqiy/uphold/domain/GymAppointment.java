package dev.oaiqiy.uphold.domain;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class GymAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GymArea gymArea;


    private Date startTime;
    private Date endTime;


    private Integer count;
    private Integer appointed;

    public synchronized boolean appointedIncr(){
        if(count <= appointed)
            return false;

        appointed = appointed + 1;
        return true;
    }

    public synchronized void appointedDecr(){
        appointed = appointed - 1;
    }

    public synchronized boolean check(){
        return appointed <= count;
    }

    public GymAppointment(GymArea gymArea, Date startTime, Date endTime, Integer count, Integer appointed) {
        this.gymArea = gymArea;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
        this.appointed = appointed;
    }
}
