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

    private String destination;

    private LocalDateTime dateTime;


    public SendRecord(String itemName, int amount, String destination, LocalDateTime dateTime) {
        this.itemName = itemName;
        this.amount = amount;
        this.destination = destination;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "SendRecord{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", amount=" + amount +
                ", destination='" + destination + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
