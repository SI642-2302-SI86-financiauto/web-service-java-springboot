package com.financiauto.webservice.model.repository;

import com.financiauto.webservice.model.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

     List<User> findAll();

}
