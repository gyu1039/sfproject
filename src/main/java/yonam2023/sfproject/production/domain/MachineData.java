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

    @Column(name="MACHINEID")
    int machineId;
    @Column(name = "NAME")
    String name;

    @Column(name = "MAX")
    int max;

    @Column(name = "MIN")
    int min;

    @Column(name = "RDATA")
    int recentData=-1;

    @Column(name = "RESOUCETYPE")
    String resourceType;

    @Column(name = "STATE") //ErrorCode로 변경해야 할 수 있음.
    boolean state;

    @Column(name = "FATAL")
    boolean fatal;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "STOCK")
    int stock;

    @Column(name = "MAXSTOCK")
    int maxStock;

    @Builder
    public MachineData(int machineId, String name, int max, int min, int recentData,boolean state, boolean fatal, String description, int stock, int maxStock, String resourceType){
        this.machineId = machineId;
        this.name = name;
        this.max = max;
        this.min = min;
        this.recentData = recentData;
        this.state = state;
        this.fatal = fatal;
        this.description = description;
        this.stock = stock;
        this.maxStock = maxStock;
        this.resourceType = resourceType;
    }
}
