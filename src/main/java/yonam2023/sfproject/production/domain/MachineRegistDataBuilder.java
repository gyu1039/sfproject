package yonam2023.sfproject.production.domain;

public class MachineRegistDataBuilder {
    int mid;
    String name;
    String description;


    public MachineRegistDataBuilder mid(int mid){
        this.mid = mid;
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
        return new MachineRegistData(mid, name, description);
    }

}
