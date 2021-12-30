package br.com.gabrieltonhatti.api.repository;

import br.com.gabrieltonhatti.api.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

}
