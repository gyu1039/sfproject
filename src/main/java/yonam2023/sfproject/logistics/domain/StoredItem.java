package yonam2023.sfproject.logistics.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class StoredItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int amount;

    public StoredItem(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public void addAmount(int amount){
        this.amount += amount;
    }
    public void subAmount (int amount){
        if (this.amount < amount){
        //todo:  throw new minusAmountException("출고량이 재고량을 초과합니다.");
        }
        this.amount -= amount;
    }

    @Override
    public String toString() {
        return "StoredItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoredItem that = (StoredItem) o;

        if (getAmount() != that.getAmount()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getAmount();
        return result;
    }
}
