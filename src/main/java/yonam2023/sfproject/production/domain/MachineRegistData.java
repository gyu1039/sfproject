package yonam2023.sfproject.production.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MachineRegistData {
    int mid;
    String name;
    String description;

    boolean machineExists = false;

    public boolean machineExists() {
        return machineExists;
    }

    public void setMachineExists(boolean machineExists) {
        this.machineExists = machineExists;
    }

    @Builder
    public MachineRegistData(int mid,String name,String description){
        this.mid = mid;
        this.name = name;
        this.description = description;
    }

}
