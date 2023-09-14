package br.estudorest;



//Assim importa todos os metodos que o objeto restAssured
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {
  
	@Test
	public void testOlaMundo() {
		Response response = RestAssured .request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode()== 200);
		Assert.assertTrue("O status code deveria ser 200",response.statusCode()== 200);// aqui ele está pegando e comparando
		Assert.assertEquals(200, response.statusCode());
		ValidatableResponse validacao = response.then(); // retorna validacoes do response
		validacao.statusCode(201); // aqui ele está apenas comparando com o statuscode
	}
	@Test
	public void devoConhecerOutrasFormas () {
		Response response = RestAssured .request(Method.GET, "http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		//RestAssured.get("http://restapi.wcaquino.me/ola");
		get("http://restapi.wcaquino.me/ola").then().statusCode(200); // assim ele já pega direto o metodo estático
		//then = então status code tem que ser igual a 200
		// Podendo já realizar as assertivas pois o get retorna um response 
		// Mas da para importar o Restassured como um todo e usar apenas o get no modo discurssivo
		
		// Porem o metodo mais utilizado é o metodo discurssivo 
		// forma discursiva pois ele usa o BDD
		// given pré condiçao when açao e then validaçao 
		// sempre lembrando que o ponto vem depois dos parenteses e o ponto e virgula no final do BDD
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200);
		
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void devoConhecerMatchersHamcrest() {
		
		assertThat("Maria", Matchers.is("Maria")); // Aqui se maria é igual a maria
		assertThat(128, Matchers.is(128)); // aqui verifica se 128 é igual a 128
		assertThat(128, Matchers.isA(Integer.class)); // aqui verifica se 128 é um inteiro
		assertThat(128d, Matchers.isA(Double.class)); // aqui verifica se 128 é um double
		assertThat(128, Matchers.greaterThan(120)); // aqui verifica se 128 é maior que 120
		assertThat(128, Matchers.lessThan(130)); // aqui verifica que 128 é menor que 130
		
		
		List<Integer> impares = Arrays.asList(1 , 3 , 5 , 7, 9 );
		assertThat(impares, Matchers.hasSize(5));
		assertThat(impares, Matchers.contains(1 , 3 , 5 , 7, 9 ));
		assertThat(impares, Matchers.containsInAnyOrder(3 , 1 , 5 , 7, 9));
		assertThat(impares, Matchers.hasItem(1));
		assertThat(impares, Matchers.hasItems(5));
		
		
		assertThat("Maria", is(not("joão")));
		assertThat("Maria", not("joão"));
		//assertThat("Joaquina", anyOf(is("Maria")), is("Joaquina"));
		assertThat("Joaquina", allOf(startsWith("joa"), endsWith("ina"), containsString("qui")));
		
		
	
	
	}
	
	@Test
	public void devoValidarBody() {
		   given()
			.when()
				.get("http://restapi.wcaquino.me/ola")
			.then()
				.statusCode(200)
				.body(is("Ola Mundo!"))
				.body(containsString("Mundo"))
				.body(is(not(nullValue())));
	}
	
	
}
	
