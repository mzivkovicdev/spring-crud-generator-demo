package dev.markozivkovic.spring_crud_generator_demo.persistance.entity.helpers;

import java.util.Objects;

public class Details {

    private String firstName;

    private String lastName;


    public Details() {

    }

    public Details(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Details setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Details setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }


    @Override
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Details)) {
            return false;
        }
        final Details other = (Details) o;
        return Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return "Details{" +
            " firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
        "}";
    }

}