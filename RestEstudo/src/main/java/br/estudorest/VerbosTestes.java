package br.estudorest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTestes {
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json") // ele mostra o que ele ta enviando é um json e não uma string
			.body("{\"name\": \"jose\", \"age\":50}") // Aqui o eclipse já ajuda quando você cola o JSON
			.when() 
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(201)
				.body("id", is(notNullValue())) // Aqui ele usa os matcher colocar
				// Matchers importar matchers depois importar todos para ficar abreviado
				.body("name", is("jose")) // Primeiro campo o que ta buscando no body response depois o matchers
				.body("age", is(50))
			
			
			//{"name": "jose", "age":50}
			
			;
		
		
		
	}
	
	@Test
	public void naoDeveSalvarSemoNome() {
		
		given()
			.log().all()
			.contentType("application/json") // ele mostra o que ele ta enviando é um json e não uma string
			.body("{\"age\":50}") // Aqui o eclipse já ajuda quando você cola o JSON
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(400)
				.body("error", is("Name é um atributo obrigatório"))
			
			
			
			
			;
		
		
		
	}
	
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
				.log().all()
				.contentType(ContentType.XML)
				.body("<user><name>Jose</name><age>50</age></user>")
			.when()
				.post("https://restapi.wcaquino.me/usersXML")
			.then()
				.log().all()
				.statusCode(201)
				.body("user.id", is(notNullValue()))// Sempre lembra que o xml retorna em tags e tem que navegar por elas
				.body("user.name", is("Jose"))
				.body("user.age", is("50"))// e os numeros vem em Strings
			
			
			
			
			
			
			;
		
		
		
		
		
	}
	
	@Test
	public void deveAlterarUsuario() {
		
		given()
			.log().all()
			.contentType(ContentType.JSON)// esse contentype é uma class que podemos buscar os content type
			.body("{\"name\":\"alterando nome\", \"age\": 78}")//json para alteracao
			.when()
				.put("https://restapi.wcaquino.me/users/1")// aqui ocorreu a alteração com o put 
			.then() // Validou as alterações com os matchers
				.log().all()
				.statusCode(200)
				.body("id", is(1))
				.body("name", is("alterando nome"))// nome alterando sendo validado 
				.body("age", is(80))//idade alterada sendo validada 
				.body("salary", is(1234.5678f))
				
			
			
			//{"name":"alterando nome", "age": 78}
			
			
			
			
			
			;
		
		
		
		
		
	}
	
	
	@Test
	public void deveAlterarUsuarioUsandoPathParam() {
		
		given()
			.log().all()
			.contentType(ContentType.JSON)// esse contentype é uma class que podemos buscar os content type
			.body("{\"name\":\"alterando nome\", \"age\": 78}")//json para alteracao
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
			.when()
				.put("https://restapi.wcaquino.me/{entidade}/{userId}")// aqui ocorreu a alteração com o put 
			.then() // Validou as alterações com os matchers
				.log().all()
				.statusCode(200)
				.body("id", is(1))
				.body("name", is("alterando nome"))// nome alterando sendo validado 
				.body("age", is(80))//idade alterada sendo validada 
				.body("salary", is(1234.5678f))
				
			
			
			
			
			
			
			
			
			;
		
		
		
		
		
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
			.when()
			.delete("https://restapi.wcaquino.me/users/1") // aqui ele deleta 
			.then()
			.log().all()
			.statusCode(204)// verifica se ele retornou status  204
			
			
			
			;
		
		
		
		
		
	}
	
	@Test
	public void deveRemoverUsuarioInexistente() {
		given()
			.log().all()
			.when()
			.delete("https://restapi.wcaquino.me/users/1")
			.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexstente")) // Aqui ele  envia um usuario inexistente e verifica se body retorna erro
			
			
			
			;
		
		
		
		// Parei em senerlização de MAPs copiar e documentar 
		
	}
	
	@Test
	public void deveSalvarUsuarioUsandoMap() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		// Aqui ele vai converter via map uma string e um object para json
		// após iniciar o params com params.put ele envia o nome do objeto com o valor
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		// desse modo passando params no body a requisição entendi que é um json
		// porem no param tem que conter a chamada de body esperada pela API
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(params)
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
				.log().all()
				.statusCode(200)
				.body("id", is(notNullValue()))
				.body("name", is("Usuario via map"))
				.body("age", is(25))
				
				
				;
			
		
		
		
		
		
	}
	
	@Test
	public void deveSalvarUsuarioUsandoObjeto() {
		
		User user = new User("Usuario via objeto", 35); 
		// Acim ele instanciou a class objeto que la tem a variaveis privadas
		// Aonde ele liberous os getters e setter e fez um contrutors
		// Para assim sendo obrigado passar os valor na chamada 
		
		given()
		.log().all()
		.contentType(ContentType.JSON)
		.body(user) // Aqui ele passa o objeto
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
			.log().all()
			.statusCode(200)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))
			;
		
		
		
		
	}
	
	@Test
	public void deveDeserializarObjetoAoSalvarUsuario() {
		
		
		User user = new User("Usuario deserializado", 35); 
		// Acima ele instanciou a class objeto que la tem a variaveis privadas
		// Aonde ele liberous os getters e setter e fez um contrutores
		// Para assim sendo obrigado passar os valor na chamada 
		
	User usuarioInserido =	given() // Ele instancio a class de novo recebendo a chamada
			//desse modo quando ele chamar essa instancia ele vai chamar o toString
			// Com seus valores que são as variaveis do objeto que foram setadas pelo body extraido
		.log().all()
		.contentType(ContentType.JSON)
		.body(user) // Aqui ele passa o objeto
			.when()
				.post("https://restapi.wcaquino.me/users")
			.then()
			.log().all()
			.statusCode(200)
			.extract().body().as(User.class)
			;
		
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
		
		
	}
	
	

}
