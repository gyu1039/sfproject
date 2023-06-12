package yonam2023.sfproject.production.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MachineStockAddData {
    int machineId;

    int amount;

    int maxStock;

    String resourceType;

    int resourceRemain;

    public MachineStockAddData(int machineId, int amount, int maxStock, String resourceType, int resourceRemain){
        this.machineId = machineId;
        this.amount = amount;
        this.maxStock = maxStock;
        this.resourceType = resourceType;
        this.resourceRemain = resourceRemain;
    }
}
