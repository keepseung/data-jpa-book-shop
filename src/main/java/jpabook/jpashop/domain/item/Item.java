package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;

    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    //==비즈니스 로직==//

    /**
     * 재고 증가
     * @param quantity
     * 재고가 증가하거나 상품 주 문을 취소해서 재고를 다시 늘려야 할 때 사용한다.
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고를 줄인다.
     * @param quantity
     * 만약 재고가 부족하면 예외가 발생한다. 주로 상품을 주문할 때 사용한다.
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
