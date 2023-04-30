package yonam2023.sfproject.production.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="MACHINES")
@Getter
@Setter
@NoArgsConstructor
public class MachineData {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="MID")
    int mid;
    @Column(name = "NAME")
    String name;

    @Column(name = "MAX")
    int max;

    @Column(name = "MIN")
    int min;

    @Column(name = "STATE")
    boolean state;

    @Column(name = "FATAL")
    boolean fatal;

    @Column(name = "DESCRIPTION")
    String description;

    @Builder
    public MachineData(int mid,String name, boolean state, String description){
        this.mid = mid;
        this.name = name;
        this.state = state;
        this.description = description;
    }
}
