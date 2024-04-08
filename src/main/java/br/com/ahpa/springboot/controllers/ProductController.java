package br.com.ahpa.springboot.controllers;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ahpa.springboot.dtos.ProductRecordDTO;
import br.com.ahpa.springboot.models.ProductModel;
import br.com.ahpa.springboot.repositories.IProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	IProductRepository iProductRepository;

	@PostMapping()
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO) {

		ProductModel productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDTO, productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(iProductRepository.save(productModel));
	}

	@GetMapping(path = "/get/{pageNumber}/{pageSize}")
	public ResponseEntity<Page<ProductModel>> getAllProducts(@PathVariable(name = "pageNumber") int pageNumber,
			@PathVariable(name = "pageSize") int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		return ResponseEntity.status(HttpStatus.OK).body(iProductRepository.findAll(page));
	}

	@GetMapping(path = "/get/{id}")
	public ResponseEntity<Object> getProductById(@PathVariable(name = "id") Long id) {
		Optional<ProductModel> product = iProductRepository.findById(id);
		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(product.get());
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable(name = "id") Long id,
			@RequestBody @Valid ProductRecordDTO productRecordDTO) {
		Optional<ProductModel> product = iProductRepository.findById(id);
		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}

		ProductModel productModel = product.get();
		BeanUtils.copyProperties(productRecordDTO, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(iProductRepository.save(productModel));
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<Object> deleteProductById(@PathVariable(name = "id") Long id) {
		Optional<ProductModel> product = iProductRepository.findById(id);

		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		
		iProductRepository.delete(product.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
	}

}
