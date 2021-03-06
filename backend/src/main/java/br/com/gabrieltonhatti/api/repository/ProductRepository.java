package br.com.gabrieltonhatti.api.repository;

import br.com.gabrieltonhatti.api.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p ORDER BY p.id ASC")
    public Iterable<Product> findAllOrder();

    @Query("SELECT p FROM Product p ORDER BY p.id ASC")
    public Page<Product> findAllOrder(Pageable pageable);

}
