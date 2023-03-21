package yonam2023.sfproject.logistics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Inventory {
    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column
    private String name;

    private int amount;

    public Inventory(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
