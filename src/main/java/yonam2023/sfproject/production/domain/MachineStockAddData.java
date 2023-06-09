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

    public MachineStockAddData(int machineId, int amount, int maxStock){
        this.machineId = machineId;
        this.amount = amount;
        this.maxStock = maxStock;
    }
}
