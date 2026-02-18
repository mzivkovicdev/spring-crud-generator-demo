package dev.markozivkovic.spring_crud_generator_demo.generated.product.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductDetailsPayload;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.UserPayload;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Represents a product
 */

@Schema(name = "ProductPayload", description = "Represents a product")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.14.0")
public class ProductPayload {

  private @Nullable Long id;

  private String name;

  private Integer price;

  @Valid
  private List<@Valid UserPayload> users = new ArrayList<>();

  private UUID uuid;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate releaseDate;

  @Valid
  private List<@Valid ProductDetailsPayload> details = new ArrayList<>();

  /**
   * The status of the product
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  public ProductPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProductPayload(String name, Integer price, List<@Valid UserPayload> users, UUID uuid) {
    this.name = name;
    this.price = price;
    this.users = users;
    this.uuid = uuid;
  }

  public ProductPayload id(@Nullable Long id) {
    this.id = id;
    return this;
  }

  /**
   * The unique identifier for the product
   * @return id
   */
  
  @Schema(name = "id", description = "The unique identifier for the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Long getId() {
    return id;
  }

  public void setId(@Nullable Long id) {
    this.id = id;
  }

  public ProductPayload name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the product
   * @return name
   */
  @NotNull @Size(min = 10, max = 100) 
  @Schema(name = "name", description = "The name of the product", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductPayload price(Integer price) {
    this.price = price;
    return this;
  }

  /**
   * The price of the product
   * minimum: 1
   * maximum: 100
   * @return price
   */
  @NotNull @Min(1) @Max(100) 
  @Schema(name = "price", description = "The price of the product", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public ProductPayload users(List<@Valid UserPayload> users) {
    this.users = users;
    return this;
  }

  public ProductPayload addUsersItem(UserPayload usersItem) {
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
  public List<@Valid UserPayload> getUsers() {
    return users;
  }

  public void setUsers(List<@Valid UserPayload> users) {
    this.users = users;
  }

  public ProductPayload uuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  /**
   * The unique identifier for the product
   * @return uuid
   */
  @NotNull @Valid 
  @Schema(name = "uuid", description = "The unique identifier for the product", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uuid")
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public ProductPayload releaseDate(@Nullable LocalDate releaseDate) {
    this.releaseDate = releaseDate;
    return this;
  }

  /**
   * The release date of the product
   * @return releaseDate
   */
  @Valid 
  @Schema(name = "releaseDate", description = "The release date of the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("releaseDate")
  public @Nullable LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(@Nullable LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

  public ProductPayload details(List<@Valid ProductDetailsPayload> details) {
    this.details = details;
    return this;
  }

  public ProductPayload addDetailsItem(ProductDetailsPayload detailsItem) {
    if (this.details == null) {
      this.details = new ArrayList<>();
    }
    this.details.add(detailsItem);
    return this;
  }

  /**
   * Get details
   * @return details
   */
  @Valid 
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public List<@Valid ProductDetailsPayload> getDetails() {
    return details;
  }

  public void setDetails(List<@Valid ProductDetailsPayload> details) {
    this.details = details;
  }

  public ProductPayload status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * The status of the product
   * @return status
   */
  
  @Schema(name = "status", description = "The status of the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductPayload productPayload = (ProductPayload) o;
    return Objects.equals(this.id, productPayload.id) &&
        Objects.equals(this.name, productPayload.name) &&
        Objects.equals(this.price, productPayload.price) &&
        Objects.equals(this.users, productPayload.users) &&
        Objects.equals(this.uuid, productPayload.uuid) &&
        Objects.equals(this.releaseDate, productPayload.releaseDate) &&
        Objects.equals(this.details, productPayload.details) &&
        Objects.equals(this.status, productPayload.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, price, users, uuid, releaseDate, details, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductPayload {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    releaseDate: ").append(toIndentedString(releaseDate)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

