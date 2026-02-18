package dev.markozivkovic.spring_crud_generator_demo.generated.product.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.markozivkovic.spring_crud_generator_demo.generated.product.model.ProductDetailsPayload;
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

@Schema(name = "ProductUpdatePayload", description = "Represents a product")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.14.0")
public class ProductUpdatePayload {

  private String name;

  private Integer price;

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

  public ProductUpdatePayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProductUpdatePayload(String name, Integer price, UUID uuid) {
    this.name = name;
    this.price = price;
    this.uuid = uuid;
  }

  public ProductUpdatePayload name(String name) {
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

  public ProductUpdatePayload price(Integer price) {
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

  public ProductUpdatePayload uuid(UUID uuid) {
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

  public ProductUpdatePayload releaseDate(@Nullable LocalDate releaseDate) {
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

  public ProductUpdatePayload details(List<@Valid ProductDetailsPayload> details) {
    this.details = details;
    return this;
  }

  public ProductUpdatePayload addDetailsItem(ProductDetailsPayload detailsItem) {
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

  public ProductUpdatePayload status(@Nullable StatusEnum status) {
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
    ProductUpdatePayload productUpdatePayload = (ProductUpdatePayload) o;
    return Objects.equals(this.name, productUpdatePayload.name) &&
        Objects.equals(this.price, productUpdatePayload.price) &&
        Objects.equals(this.uuid, productUpdatePayload.uuid) &&
        Objects.equals(this.releaseDate, productUpdatePayload.releaseDate) &&
        Objects.equals(this.details, productUpdatePayload.details) &&
        Objects.equals(this.status, productUpdatePayload.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, price, uuid, releaseDate, details, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductUpdatePayload {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

