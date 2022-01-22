package br.com.gabrieltonhatti.api.controller;

import br.com.gabrieltonhatti.api.dto.ProductDTO;
import br.com.gabrieltonhatti.api.exception.IdNotFoundException;
import br.com.gabrieltonhatti.api.model.ApiException;
import br.com.gabrieltonhatti.api.model.Product;
import br.com.gabrieltonhatti.api.service.ProductService;
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

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/products", produces = "application/json;charset=utf-8")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Retorna uma lista de produtos")
    @ApiResponses(@ApiResponse(code = 200, message = "Retorna a lista de produtos"))
    @GetMapping
    public ResponseEntity<Stream<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @ApiOperation(value = "Retorna uma lista de produtos paginada (Use page e size ao invés de pageNumber e pageSize na hora de fazer a requisição)")
    @ApiResponses(@ApiResponse(code = 200, message = "Retorna a lista de produtos paginada"))
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> getProductsForPage(Pageable pageable) {
                return ResponseEntity.ok(productService.findAllPage(pageable));
    }

    @ApiOperation(value = "Retorna o produto pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Retorna o produto pelo ID"),
            @ApiResponse(code = 404, message = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) throws IdNotFoundException {
        return ResponseEntity
                .status(200)
                .body(productService.findById(id));
    }

    @ApiOperation(value = "Retorna o produto inserido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Retorna o produto inserido"),
            @ApiResponse(code = 404, message = "Lançou uma exceção de nome vazio ou preço inválido")
    })
    @PostMapping(consumes = "application/json;charset=utf-8")
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody @Validated @NotNull Product product)
            throws NotFoundException {
        return ResponseEntity.ok(productService.save(product));
    }

    @ApiOperation(value = "Retorna o produto atualizado pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Atualiza o produto pelo ID"),
            @ApiResponse(code = 404, message = "Produto não encontrado!")
    })
    @PutMapping(path = "/{id}", consumes = "application/json;charset=utf-8")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody @Validated @NotNull Product product,
                                                    @PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(productService.update(product, id));
    }

    @ApiOperation(value = "Exclui o produto pelo ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Exclui o produto pelo ID"),
            @ApiResponse(code = 404, message = "Produto não encontrado!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiException> deleteProductById(@PathVariable Integer id) throws IdNotFoundException {
        return ResponseEntity.ok(productService.deleteByid(id));
    }

}
