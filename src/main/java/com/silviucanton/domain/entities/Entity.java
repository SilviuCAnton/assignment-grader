package com.silviucanton.domain.entities;

import java.io.Serializable;

/**
 * The base entity class
 *
 * @param <ID> - the type of the entity's id
 */
public interface Entity<ID> extends Serializable {

    /**
     * returns the id of the entity
     *
     * @return id - ID
     */
    ID getId();

    /**
     * sets the id of the entity
     *
     * @param id - ID
     */
    void setId(ID id);

    String toFileString();
}