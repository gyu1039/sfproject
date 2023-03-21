package yonam2023.sfproject.inventory.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Inventory {
    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column
    private String name;

    private int amount;

}
