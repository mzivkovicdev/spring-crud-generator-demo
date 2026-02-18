package dev.markozivkovic.spring_crud_generator_demo.generated.product.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Represents a product details
 */

@Schema(name = "ProductDetailsPayload", description = "Represents a product details")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.14.0")
public class ProductDetailsPayload {

  private @Nullable String description;

  private @Nullable Double weight;

  private @Nullable String option;

  public ProductDetailsPayload description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ProductDetailsPayload weight(@Nullable Double weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable Double getWeight() {
    return weight;
  }

  public void setWeight(@Nullable Double weight) {
    this.weight = weight;
  }

  public ProductDetailsPayload option(@Nullable String option) {
    this.option = option;
    return this;
  }

  /**
   * Get option
   * @return option
   */
  
  @Schema(name = "option", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("option")
  public @Nullable String getOption() {
    return option;
  }

  public void setOption(@Nullable String option) {
    this.option = option;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductDetailsPayload productDetailsPayload = (ProductDetailsPayload) o;
    return Objects.equals(this.description, productDetailsPayload.description) &&
        Objects.equals(this.weight, productDetailsPayload.weight) &&
        Objects.equals(this.option, productDetailsPayload.option);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, weight, option);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductDetailsPayload {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    option: ").append(toIndentedString(option)).append("\n");
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

