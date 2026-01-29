package dev.markozivkovic.spring_crud_generator_demo.generated.product.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
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

  private @Nullable String name;

  private @Nullable String price;

  private @Nullable UUID uuid;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate birthDate;

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

  public ProductUpdatePayload name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the product
   * @return name
   */
  @Size(max = 10000) 
  @Schema(name = "name", description = "The name of the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ProductUpdatePayload price(@Nullable String price) {
    this.price = price;
    return this;
  }

  /**
   * The price of the product
   * @return price
   */
  
  @Schema(name = "price", description = "The price of the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable String getPrice() {
    return price;
  }

  public void setPrice(@Nullable String price) {
    this.price = price;
  }

  public ProductUpdatePayload uuid(@Nullable UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  /**
   * The unique identifier for the product
   * @return uuid
   */
  @Valid 
  @Schema(name = "uuid", description = "The unique identifier for the product", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uuid")
  public @Nullable UUID getUuid() {
    return uuid;
  }

  public void setUuid(@Nullable UUID uuid) {
    this.uuid = uuid;
  }

  public ProductUpdatePayload birthDate(@Nullable LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  /**
   * The date and time the product was created
   * @return birthDate
   */
  @Valid 
  @Schema(name = "birthDate", description = "The date and time the product was created", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("birthDate")
  public @Nullable LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(@Nullable LocalDate birthDate) {
    this.birthDate = birthDate;
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
        Objects.equals(this.birthDate, productUpdatePayload.birthDate) &&
        Objects.equals(this.status, productUpdatePayload.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, price, uuid, birthDate, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductUpdatePayload {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
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

