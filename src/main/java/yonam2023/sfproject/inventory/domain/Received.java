package yonam2023.sfproject.inventory.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Received {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private int amount;

    private LocalDateTime localDateTime;
}
