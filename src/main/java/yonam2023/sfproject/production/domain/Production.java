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
    private int id;

    @Column(name = "STYPE")
    private String stype;

    @Column(name = "SVALUE")
    private int svalue;

    @Column(name = "OTIME")
    @CreatedDate
    private Date otime;

    @Builder
    public Production(String stype, int svalue){
        this.stype = stype;
        this.svalue = svalue;
    }
}