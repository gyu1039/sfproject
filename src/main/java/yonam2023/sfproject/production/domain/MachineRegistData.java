package yonam2023.sfproject.production.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MachineRegistData {
    int machineId;
    String name;
    String description;

    boolean machineExists = false;

    public boolean machineExists() {
        return machineExists;
    }

    public void setMachineExists(boolean machineExists) {
        this.machineExists = machineExists;
    }

    public MachineRegistData(int machineId,String name,String description){
        this.machineId = machineId;
        this.name = name;
        this.description = description;
    }

}
