package dev.markozivkovic.spring_crud_generator_demo.persistance.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers.Details;

@Entity
@Table(name = "user_table")
public class UserEntity {

    @Id
    @SequenceGenerator(
        name = "User_gen",
        sequenceName = "user_table_id_seq",
        allocationSize = 50,
        initialValue = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String username;

    private String email;

    private String password;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Details details;

    @ElementCollection
    @CollectionTable(
        name = "user_table_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @OrderColumn(name = "order_index")
    private List<String> roles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "user_table_permissions",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @OrderColumn(name = "order_index")
    private List<String> permissions = new ArrayList<>();

    @Version
    private Integer version;

    public UserEntity() {

    }

    public UserEntity(final String username, final String email, final String password, final Details details, final List<String> roles, final List<String> permissions) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.details = details;
        this.roles = roles;
        this.permissions = permissions;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserEntity setUserId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public UserEntity setUsername(final String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public UserEntity setEmail(final String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public UserEntity setPassword(final String password) {
        this.password = password;
        return this;
    }

    public Details getDetails() {
        return this.details;
    }

    public UserEntity setDetails(final Details details) {
        this.details = details;
        return this;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public UserEntity setRoles(final List<String> roles) {
        this.roles = roles;
        return this;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public UserEntity setPermissions(final List<String> permissions) {
        this.permissions = permissions;
        return this;
    }


    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserEntity)) {
            return false;
        }
        final UserEntity other = (UserEntity) o;
        return Objects.nonNull(userId) && userId.equals(other.getUserId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
            " userId='" + getUserId() + "'" +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", details='" + getDetails() + "'" +
            ", roles='" + getRoles() + "'" +
            ", permissions='" + getPermissions() + "'" +
        "}";
    }

}