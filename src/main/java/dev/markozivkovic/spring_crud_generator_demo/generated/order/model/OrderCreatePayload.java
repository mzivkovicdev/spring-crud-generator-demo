package dev.markozivkovic.spring_crud_generator_demo.generated.order.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.ProductInput1;
import dev.markozivkovic.spring_crud_generator_demo.generated.order.model.UserInput1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Represents an order
 */

@Schema(name = "OrderCreatePayload", description = "Represents an order")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.14.0")
public class OrderCreatePayload {

  private ProductInput product;

  private Integer quantity;

  @Valid
  private List<@Valid UserInput> users = new ArrayList<>();

  public OrderCreatePayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public OrderCreatePayload(ProductInput product, Integer quantity, List<@Valid UserInput> users) {
    this.product = product;
    this.quantity = quantity;
    this.users = users;
  }

  public OrderCreatePayload product(ProductInput product) {
    this.product = product;
    return this;
  }

  /**
   * Get product
   * @return product
   */
  @NotNull @Valid 
  @Schema(name = "product", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("product")
  public ProductInput getProduct() {
    return product;
  }

  public void setProduct(ProductInput product) {
    this.product = product;
  }

  public OrderCreatePayload quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * maximum: 100
   * @return quantity
   */
  @NotNull @Min(1) @Max(100) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public OrderCreatePayload users(List<@Valid UserInput> users) {
    this.users = users;
    return this;
  }

  public OrderCreatePayload addUsersItem(UserInput usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    this.users.add(usersItem);
    return this;
  }

  /**
   * Get users
   * @return users
   */
  @NotNull @Valid 
  @Schema(name = "users", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("users")
  public List<@Valid UserInput> getUsers() {
    return users;
  }

  public void setUsers(List<@Valid UserInput> users) {
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderCreatePayload orderCreatePayload = (OrderCreatePayload) o;
    return Objects.equals(this.product, orderCreatePayload.product) &&
        Objects.equals(this.quantity, orderCreatePayload.quantity) &&
        Objects.equals(this.users, orderCreatePayload.users);
  }

  @Override
  public int hashCode() {
    return Objects.hash(product, quantity, users);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderCreatePayload {\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

