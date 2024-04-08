package br.com.ahpa.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ahpa.springboot.models.ProductModel;

@Repository
public interface IProductRepository extends JpaRepository<ProductModel, Long>{
}
