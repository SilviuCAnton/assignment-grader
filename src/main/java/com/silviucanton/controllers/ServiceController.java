package com.silviucanton.controllers;

import com.silviucanton.services.service.Service;

public interface ServiceController {
    default void initialize(Service service){}
}
