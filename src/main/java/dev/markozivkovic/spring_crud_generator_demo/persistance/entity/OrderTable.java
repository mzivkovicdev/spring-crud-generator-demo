package dev.markozivkovic.spring_crud_generator_demo.persistance.entity;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @Column(
        nullable = false
    )
    private Integer quantity;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "order_user_table",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users;

    @Version
    private Integer version;

    public OrderTable() {

    }

    public OrderTable(final ProductModel product, final Integer quantity, final List<UserEntity> users) {
        this.product = product;
        this.quantity = quantity;
        this.users = users;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public OrderTable setOrderId(final Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public ProductModel getProduct() {
        return this.product;
    }

    public OrderTable setProduct(final ProductModel product) {
        this.product = product;
        return this;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderTable setQuantity(final Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public List<UserEntity> getUsers() {
        return this.users;
    }

    public OrderTable setUsers(final List<UserEntity> users) {
        this.users = users;
        return this;
    }


    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTable)) {
            return false;
        }
        final OrderTable other = (OrderTable) o;
        return Objects.nonNull(orderId) && orderId.equals(other.getOrderId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "OrderTable{" +
            " orderId='" + getOrderId() + "'" +
            ", product='" + getProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", users='" + getUsers() + "'" +
        "}";
    }

}