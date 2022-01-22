package br.com.gabrieltonhatti.api.service;

import br.com.gabrieltonhatti.api.dto.ProductDTO;
import br.com.gabrieltonhatti.api.exception.IdNotFoundException;
import br.com.gabrieltonhatti.api.model.ApiException;
import br.com.gabrieltonhatti.api.model.Product;
import br.com.gabrieltonhatti.api.repository.ProductRepository;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Stream<ProductDTO> findAll() {
        Iterable<Product> products = productRepository.findAllOrder();
        Stream<Product> stream = StreamSupport.stream(products.spliterator(), false);

        return stream.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPage(Pageable pageable) {
        Page<Product> products = productRepository.findAllOrder(pageable);
        return products
                .map(ProductDTO::new);

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Integer id) throws IdNotFoundException {
        Product result = productRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("Produto não encontrado!"));

        return new ProductDTO(result);
    }

    @Transactional
    public ProductDTO save(Product product) throws NotFoundException {
        ProductDTO result = new ProductDTO(product);

        if (result.getName().isEmpty()) throw new NotFoundException("O nome não pode estar vazio!");
        else if (result.getPrice() < 0) throw new NotFoundException("O preço não pode ser menor do que 0!");

        productRepository.save(product);
        result.setId(product.getId());
        return result;
    }

    @Transactional
    public ProductDTO update(Product product, Integer id) throws NotFoundException {

        if (product.getName().isEmpty()) throw new NotFoundException("O nome não pode estar vazio!");
        else if (product.getPrice() < 0) throw new NotFoundException("O preço não pode ser menor do que 0!");

        product.setId(id);
        productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ApiException deleteByid(Integer id) throws IdNotFoundException {
        productRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("O ID do produto não foi encontrado!"));

        productRepository.deleteById(id);

        return new ApiException("Produto deletado com sucesso!", 200);
    }
}
