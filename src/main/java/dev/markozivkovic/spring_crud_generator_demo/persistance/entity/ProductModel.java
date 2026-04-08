package dev.markozivkovic.spring_crud_generator_demo.persistance.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.ProductDetails;

@Entity
@Table(name = "product_table")
@NamedEntityGraph(
    name = "Product.withUsers",
    attributeNodes = {
        @NamedAttributeNode("users")
    }
)
public class ProductModel {

    @Id
    @TableGenerator(
        name = "Product_gen",
        table = "product_table_id_gen",
        pkColumnName = "gen_name",
        valueColumnName = "gen_value",
        pkColumnValue = "product_table",
        allocationSize = 50,
        initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Product_gen")
    private Long id;

    @Column(
        unique = true, nullable = false, updatable = true, length = 100
    )
    private String name;

    @Column(
        nullable = false
    )
    private Integer price;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private List<UserEntity> users;

    @Column(
        nullable = false
    )
    private UUID uuid;

    private LocalDate releaseDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ProductDetails> details = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Version
    private Integer version;

    public ProductModel() {

    }

    public ProductModel(final String name, final Integer price, final List<UserEntity> users, final UUID uuid, final LocalDate releaseDate, final List<ProductDetails> details, final StatusEnum status) {
        this.name = name;
        this.price = price;
        this.users = users;
        this.uuid = uuid;
        this.releaseDate = releaseDate;
        this.details = details;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public ProductModel setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ProductModel setName(final String name) {
        this.name = name;
        return this;
    }

    public Integer getPrice() {
        return this.price;
    }

    public ProductModel setPrice(final Integer price) {
        this.price = price;
        return this;
    }

    public List<UserEntity> getUsers() {
        return this.users;
    }

    public ProductModel setUsers(final List<UserEntity> users) {
        this.users = users;
        return this;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ProductModel setUuid(final UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public LocalDate getReleaseDate() {
        return this.releaseDate;
    }

    public ProductModel setReleaseDate(final LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public List<ProductDetails> getDetails() {
        return this.details;
    }

    public ProductModel setDetails(final List<ProductDetails> details) {
        this.details = details;
        return this;
    }

    public StatusEnum getStatus() {
        return this.status;
    }

    public ProductModel setStatus(final StatusEnum status) {
        this.status = status;
        return this;
    }


    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductModel)) {
            return false;
        }
        final ProductModel other = (ProductModel) o;
        return Objects.nonNull(id) && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ProductModel{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", users='" + getUsers() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", details='" + getDetails() + "'" +
            ", status='" + getStatus() + "'" +
        "}";
    }

}