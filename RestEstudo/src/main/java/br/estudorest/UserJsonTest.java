package br.estudorest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	@Test
	
	public void deveVerificarPrimeiroNivel () {
		given()
		.when()
			.get("https://restapi.wcaquino.me/user/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18));
	}
	
	public void deveVerificarPrimeiroNivelOutrasFormas () {
		// aqui ele coloca o retorno do body response dentro da instancia response
		Response response = RestAssured.request(Method.GET,"https://restapi.wcaquino.me/user/1");
		
		//path
		
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s","id"));
		
		//jsonpath
		
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
		
	}
	@Test
	
	public void deveVerificarSegundoNivel() {
		given()
		 .when()
		 	.get("https://restapi.wcaquino.me/user/2")
		 .then()
		 	.statusCode(200)
			.body("name", containsString("joaquina"))
			.body("endereco.rua",is("Rua dos bobos") );
		
		
		
		
		
	}
	// Lembrar de importar todos os matcher do hamcrest 
	
	@Test
	public void deveVerificarJsonComLista() {
		given()
			.when()
				.get("https://restapi.wcaquino.me/user/2")
			.then()
				.statusCode(200)
				.body("name", containsString("Ana")) // aqui ele valida se dentro o objeto principal tem ana 
				.body("filhos", hasSize(2)) // aqui se a lista dentro do objeto tem dois objetos
				.body("filhos[0].name", is("Zezinho"))// aqui se dentro da lista o objeto que tem indice zero seu name é zezinho
				.body("filhos[1].name", is("Luizinho"))// aqui se dentro da lista o objeto que tem indice zero seu name é Luizinho
				.body("filho.name", hasItem("Zezinho"))// Aqui ele olha para lista inteira e ve se algum tem zezinho
				.body("filho.name", hasItems("Zezinho", "Luizinho"))// Aqui ele olha para lista inteira e ve se algum tem zezinho ou luizinho
				// Lembra do S do hasItems
			
			;
	}
	@Test
	public void validandoErro () {
		given()
		 .when()
		 	.get("https://restapi.wcaquino.me/user/4")
		  .then()
		  	.statusCode(404)
		  	.body("error", is("Usuário inexistente"))
		  
		  
		  
		  
		  ;
		
	}
	
	@Test
	public void deveVerificarListaNaRaiz() {
		
		
		given()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			
			.body("$", hasSize(3) ) // Aqui por ser uma lista na raiz o cifrão aponta pra tudo que dentro dela 
			.body("name", hasItems("João da Silva","Maria Joaquina","Ana Júlia"))// Aqui ele ta falando de todos os names 
			// Na hora hasItems copiar string dentro do json identica para asserção não falhar
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))) // aqui dentro da lista tem outras lista dentro de filhos
			//foi usado o . para acessar tal lista 
			.body("salary", contains(1234.5678f, 2500, null)) // Aqui ele vai atras da propriedade salario lembrar que tem que ficar na ordem
			
		
		
		
		;
		
	}
	@Test
	
	public void devoFazerVerificacoesAvancadas() {
		given()
			.when()
				.get("https://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)
				.body("$", hasSize(3)) // quantos objetos tem nalistas
				.body("age.findAll{it <= 25}.size()", is(2))// aqui findAll percorre por toda a lista e joga o age dentro do it e valida a quantidade de it 
				.body("age.findAll{it <= 25 && it > 20}.size()", is(1))// aqui ele faz a validação acim só que com uma condição imposta 
				.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))// Aqui ele pega todos os age na condição imposta e traz o nome e valida se o msm que o esperado
				.body("findAll{it.age <= 25 }[0].name", is("Maria Joaquina"))// Aqui ele pega o nome do age que está na condição imposta e no index de valor 0
				.body("findAll{it.age <= 25 }[-1].name", is("Ana Júlia"))// aqui ele pega o mesmo só que está fora da condição
				.body("find{it.age <= 25 }.name", is("Maria Joaquina"))// Aqui é find ele pega a primeira
				.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina","Ana Júlia" ))// Aqui ele pega todos os names que contem n 
				.body("findAll{it.name.length()> 10}.name", hasItems("João da Silva","Maria Joaquina" ))//Aqui ele pega os names que tenho tamanho 10 
				.body("name.collect{it.toUpperCase()}", hasItems("MARIA JOAQUINA"))// Aqui ele pega o name do primeiro e transforma em letra masicula
				.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",hasItems("MARIA JOAQUINA") )// aqui pega todos os nomes que começa com maria e coloca em letra maiscula
				//.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()",allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)) )// aqui ele pega todos os nomes que começa com maria e tambem valida os que ele não pegou
				.body("age.collect{it * 2}", hasItems(60,50,40))// Aqui multiplicou todos os ages por dois
				.body("id.max()", is(3))// pegou o id de maior valor
				.body("salary.min()", is(1234.5678f))//pegou o salario de menor valor
				.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))// somou todos os salario que são diferente de null
				.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))// aqui colocou um entre tal valor e tal valor para fazer a soma 
				
				
				
				;
		
		
		
		
	}
	
	
	@Test
	public void devoUnirJsonPathComJAVA() {
		ArrayList<String> names =  // Aqui ele fez a chamada e pelo json path ele extraiu o retorno da condição
				given() // O retorno da condição veio em um array de string
					.when()
						.get("https://restapi.wcaquino.me/users")
					.then()
						.statusCode(200)
						.extract().path("name.findAll{it.startsWith('Maria')}")
						
					; // Agora se baseando que o name é um array de string ele vai fazer as validações com Junit	
		
		
					Assert.assertEquals(1, names.size());// Aqui se baseando no retorno que virou uma lista ele validou o tamanho de tal 
					Assert.assertTrue(names.get(0).equalsIgnoreCase("MaRia JoQuina"));// Aqui ele validou index 0 com esse equals que ignore o case ou seja a formatação das letras
					Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());// Aqui ele pegou o index zero deixou em upper case e colocou  o commparativo tambem em uppercase 
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
}
