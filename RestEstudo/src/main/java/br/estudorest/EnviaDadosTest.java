package br.estudorest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class EnviaDadosTest {

    @Test
    public void deveEnviarValorViaQuery(){
        given() //Aqui ele faz a requisição normalmente
                .log().all()
                .when()
                    .get("https://restapi.wcaquino.me/v2/users?format=json")
                // Acima ele adiciona o parametro de query direto na requisição ?format=json
                .then()
                    .log().all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
        ;



    }

    @Test
    public void deveEnviarValorViaQueryParametrizada(){
        given()
                .log().all()
                .queryParam("format","json") // Aqui ele usa
                // A propriedade query param que se compara a passar no end point
                // Porem aqui é separadamente
                // Passando o parameter name e o value
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .contentType(containsString("utf-8"))
        ;



    }

    @Test
    public void deveEnviarValorViaHeader(){
        given() //Aqui ele faz a requisição normalmente
                .log().all()
                .accept(ContentType.JSON) // Aqui ele diz que só a aceita json
                // é diferente da parametrização que fala qual contentType a requisição vai aceitar
                .when()
                .get("https://restapi.wcaquino.me/v2/users?format=json")
                // Acima ele adiciona o parametro de query direto na requisição ?format=json
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;



    }





}
