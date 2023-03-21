package yonam2023.sfproject.logistics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SendRecord {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String itemName;

    private int amount;

    private LocalDateTime sendDateTime;

    public SendRecord(String itemName, int amount, LocalDateTime sendDateTime) {
        this.itemName = itemName;
        this.amount = amount;
        this.sendDateTime = sendDateTime;
    }

    @Override
    public String toString() {
        return "SendRecord{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", amount=" + amount +
                ", sendDateTime=" + sendDateTime +
                '}';
    }
}
