package controllers;

import repositories.CrudRepository;
import services.service.Service;

public interface ServiceController {
    default void initialize(Service service){}
}
