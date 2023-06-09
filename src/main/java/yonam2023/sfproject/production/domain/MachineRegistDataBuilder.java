package yonam2023.sfproject.production.domain;

public class MachineRegistDataBuilder {
    int machineId;
    String name;
    String description;


    public MachineRegistDataBuilder machineId(int machineId){
        this.machineId = machineId;
        return this;
    }

    public MachineRegistDataBuilder name(String name){
        this.name = name;
        return this;
    }

    public MachineRegistDataBuilder description(String description){
        this.description = description;
        return this;
    }

    public MachineRegistData build(){
        return new MachineRegistData(machineId, name, description);
    }

}
