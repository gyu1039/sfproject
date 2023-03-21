package yonam2023.sfproject.logistics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Received {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private int amount;

    private LocalDateTime localDateTime;

    public Received(String name, int amount, LocalDateTime localDateTime) {
        this.name = name;
        this.amount = amount;
        this.localDateTime = localDateTime;
    }
}
