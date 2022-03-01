package br.com.gabrieltonhatti.api;

import br.com.gabrieltonhatti.api.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {

    @LocalServerPort
    private int port;

    private String jsonProductCorrect;
    private String jsonProductWithoutName;
    private String jsonProductWithoutPrice;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/products";

        jsonProductCorrect = ResourceUtils.getContentFromResource("/json/produto-correto.json");
        jsonProductWithoutName = ResourceUtils.getContentFromResource("/json/produto-sem-nome.json");
        jsonProductWithoutPrice = ResourceUtils.getContentFromResource("/json/produto-sem-preco.json");
    }

    @Test
    public void assertThatStautsIsOKWhenFindAllProducts() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void assertThatStautsIsOKWhenFindProductsByIdExisting() {
        given()
                .accept(ContentType.JSON)
                .pathParams("id", 1)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void assertThatStautsIsNotFoundWhenFindProductsByIdNotExisting() {
        given()
                .accept(ContentType.JSON)
                .pathParams("id", 100)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Produto não encontrado!"));
    }

    @Test
    public void assertThatStautsIsCreatedWhenFindAllProducts() {
        given()
                .body(jsonProductCorrect)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void assertThatStatusIsNotFoundWhenNameIsBlank() {
        given()
                .body(jsonProductWithoutName)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O nome não pode estar vazio!"));
    }

    @Test
    public void assertThatStatusIsNotFoundWhenPriceIsNegative() {
        given()
                .body(jsonProductWithoutPrice)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O preço não pode ser menor do que 0!"));
    }

    @Test
    public void assertThatStatusIsOKWhenDeleteProductByIdExisting() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("id", 2)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo("Produto deletado com sucesso!"));
    }

    @Test
    public void assertThatStautsIsNotFoundWhenDeleteProductsByIdNotExisting() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("id", 100)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("O ID do produto não foi encontrado!"));
    }

}
