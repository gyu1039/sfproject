package yonam2023.sfproject.production.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MachineStockAddData {
    int mid;

    int amount;

    int maxStock;

    public MachineStockAddData(int mid, int amount, int maxStock){
        this.mid = mid;
        this.amount = amount;
        this.maxStock = maxStock;
    }
}
