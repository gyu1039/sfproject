package yonam2023.sfproject.production.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="PRODUCTION")
@Getter
@Setter
@NoArgsConstructor
public class Production {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "MACHINEID")
    private int machineId;

    @Column(name = "SVALUE")
    private int svalue;

    @Column(name = "USED")
    private int used;

    @Column(name = "OTIME")
    @CreatedDate
    private Date otime;

    @Builder
    public Production(int machineId, int svalue, int used){
        this.machineId = machineId;
        this.svalue = svalue;
        this.used = used;
    }
}
