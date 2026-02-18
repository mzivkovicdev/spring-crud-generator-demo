package dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers;

import java.util.Objects;

public class ProductDetails {

    private String description;

    private Double weight;

    private String option;


    public ProductDetails() {

    }

    public ProductDetails(final String description, final Double weight, final String option) {
        this.description = description;
        this.weight = weight;
        this.option = option;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductDetails setDescription(final String description) {
        this.description = description;
        return this;
    }

    public Double getWeight() {
        return this.weight;
    }

    public ProductDetails setWeight(final Double weight) {
        this.weight = weight;
        return this;
    }

    public String getOption() {
        return this.option;
    }

    public ProductDetails setOption(final String option) {
        this.option = option;
        return this;
    }


    @Override
    public int hashCode() {
        return Objects.hash(description, weight, option);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductDetails)) {
            return false;
        }
        final ProductDetails other = (ProductDetails) o;
        return Objects.equals(description, other.description)
                && Objects.equals(weight, other.weight)
                && Objects.equals(option, other.option);
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
            " description='" + getDescription() + "'" +
            ", weight='" + getWeight() + "'" +
            ", option='" + getOption() + "'" +
        "}";
    }

}