package br.estudorest;

import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class JsonSchemaValidation {

    @Test
    public void validaJsonWithJsonShema(){
        given()
                .log().all()
                .when()
                .get("https://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))


        // A intenção desse teste é validar o json com uma parte inteira ou seja dentro do users.json
        // Existe um JsonSchema que seria um json com as propreiedas obrigatorioas que json deve ter
        // Quem gera um json desse é chatgpt
        //Tambem é necessario importar um dependencia no POM XML chamada de Json schema Validator



        ;






    }



}
