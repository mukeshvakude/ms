package com.brightpath.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.brightpath.model.Diet;

public interface DietRepository extends JpaRepository<Diet, Long> {
    // You can define custom query methods here if needed
}
