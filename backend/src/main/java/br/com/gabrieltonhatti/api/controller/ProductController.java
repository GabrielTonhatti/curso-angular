package br.com.gabrieltonhatti.api.controller;

import br.com.gabrieltonhatti.api.exception.IdNotFoundException;
import br.com.gabrieltonhatti.api.model.ApiException;
import br.com.gabrieltonhatti.api.model.Product;
import br.com.gabrieltonhatti.api.repository.ProductRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/products", produces = "application/json;charset=utf-8")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ApiOperation(value = "Retorna uma lista de produtos")
    @ApiResponses(@ApiResponse(code = 200, message = "Retorna a lista de produtos"))
    @GetMapping
    public ResponseEntity<Iterable<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAllOrder());
    }

    @ApiOperation(value = "Retorna uma lista de produtos paginada (Use page e size ao invés de pageNumber e pageSize na hora de fazer a requisição)")
    @ApiResponses(@ApiResponse(code = 200, message = "Retorna a lista de produtos paginada"))
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> getProductsForPage(Pageable pageable) {
        Page<Product> products = productRepository.findAllOrder(pageable);
        System.out.println(pageable.getPageSize());

        return ResponseEntity.ok(products);
    }

    @ApiOperation(value = "Retorna o produto pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Retorna o produto pelo ID"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) throws IdNotFoundException {

        return ResponseEntity
                .status(200)
                .body(productRepository
                        .findById(id)
                        .orElseThrow(() -> new IdNotFoundException("Produto não encontrado!")));
    }

    @ApiOperation(value = "Retorna o produto inserido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Retorna o produto inserido"),
            @ApiResponse(code = 404, message = "Lançou uma exceção de nome vazio ou preço inválido")
    })
    @PostMapping(consumes = "application/json;charset=utf-8")
    public ResponseEntity<Product> saveProduct(@RequestBody @Validated @NotNull Product product)
            throws NotFoundException {

        if (product.getName().isEmpty()) throw new NotFoundException("O nome não pode estar vazio!");
        else if (product.getPrice() < 0) throw new NotFoundException("O preço não pode ser menor do que 0!");

        return ResponseEntity.ok(productRepository.save(product));
    }

    @ApiOperation(value = "Retorna o produto atualizado pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Atualiza o produto pelo ID"),
            @ApiResponse(code = 404, message = "Produto não encontrado!")
    })
    @PutMapping(path = "/{id}", consumes = "application/json;charset=utf-8")
    public ResponseEntity<Product> updateProduct(@RequestBody @Validated @NotNull Product product,
                                                 @PathVariable Integer id) throws NotFoundException {

        if (product.getName().isEmpty()) throw new NotFoundException("O nome não pode estar vazio!");
        else if (product.getPrice() < 0) throw new NotFoundException("O preço não pode ser menor do que 0!");

        product.setId(id);

        return ResponseEntity.ok(productRepository.save(product));
    }

    @ApiOperation(value = "Exclui o produto pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Exclui o produto pelo ID"),
            @ApiResponse(code = 404, message = "Produto não encontrado!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiException> deleteProductById(@PathVariable Integer id) throws IdNotFoundException {

        productRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFoundException("O ID do produto não foi encontrado!"));

        productRepository.deleteById(id);

        return ResponseEntity.ok(new ApiException("Produto deletado com sucesso!", 200));

    }

}
