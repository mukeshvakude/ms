package com.brightpath.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.brightpath.model.Roles;

import java.util.List;


// The RolesRepository interface extends the CrudRepository interface provided by Spring Data.
// This will provide CRUD operations for the Roles entity.
@Repository
public interface RolesRepository extends CrudRepository<Roles, Long> {

	 
    boolean existsByAdminAndActive(Long admin, boolean active);
}
