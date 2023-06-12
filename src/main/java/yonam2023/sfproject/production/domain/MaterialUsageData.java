package yonam2023.sfproject.production.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MaterialUsageData {

    //사용량 통계용 데이터

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "NAME")
    String name;

    @Column(name = "AMOUNT")
    int amount;

    @Builder
    public MaterialUsageData(String name, int amount){
        this.name = name;
        this.amount = amount;
    }
}
