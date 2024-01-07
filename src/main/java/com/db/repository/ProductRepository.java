package com.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.entity.ProductsEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {


}
	