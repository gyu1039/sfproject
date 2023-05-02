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
    @Column(name = "McId")
    private int mid;

    @Column(name = "SVALUE")
    private int svalue;

    @Column(name = "OTIME")
    @CreatedDate
    private Date otime;

    @Builder
    public Production(int mid, int svalue){
        this.mid = mid;
        this.svalue = svalue;
    }
}
