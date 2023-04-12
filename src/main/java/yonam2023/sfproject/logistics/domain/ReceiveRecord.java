package yonam2023.sfproject.logistics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReceiveRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    private int amount;

    private String supplier;  // 공급처

    private LocalDateTime dateTime;

    public ReceiveRecord(String itemName, int amount, String supplier, LocalDateTime dateTime) {
        this.itemName = itemName;
        this.amount = amount;
        this.supplier = supplier;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ReceiveRecord{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", amount=" + amount +
                ", supplier='" + supplier + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
