package com.financiauto.webservice.model.service;

import com.financiauto.webservice.model.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User save(User user);
    User update(User user);
    void delete(Long id);
}
