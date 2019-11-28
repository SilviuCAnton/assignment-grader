package com.silviucanton.domain.validators;

import com.silviucanton.exceptions.ValidationException;

/**
 * Interface for validating entities
 *
 * @param <E> - type of the entity
 */
public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}
