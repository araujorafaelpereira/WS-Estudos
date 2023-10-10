package br.estudorest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;




public class TrabalhandoComXML {
	
	public static RequestSpecification reqSpec; // Especifica o build que ira ocorrer antes da request
	public static ResponseSpecification resSpec; // Especifica o build que ira ocorrer antes do respose
	
	
	
	
	
	
	@BeforeClass
	public static void setup() { // Esse atributo do Junit vai executar esses comandos sempre antes de rodar essa class
		RestAssured.baseURI = "https://restapi.wcaquino.me";// Seta essa URL como a base URL 
		// RestAssured.port = 443; // Aqui é a porta em que a API está rodando
		// RestaAssured.basePath = ""; // Aqui é se tiver uma v2
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();// Esse cara contem diversos metodos com ações que poder ser feitas antes da request
		reqBuilder.log(LogDetail.ALL); //  Aqui é ponteiro indo buscar no cara acima uma ação que é pra ser feita 
		reqSpec = reqBuilder.build(); // Aqui é a class que faz ele ser executado e buildado antes de cada request
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();// Esse cara contem diversos metodos com ações que poder ser feitas antes da response
		resBuilder.expectStatusCode(200);//  Aqui é ponteiro indo buscar no cara acima uma ação que é pra ser feita 
		resSpec = resBuilder.build();// Aqui é a class que faz ele ser executado e buildado antes de cada response
		
		RestAssured.requestSpecification = reqSpec; // Pega o que é para ser buildado no reqSpec e executa pós request
		RestAssured.responseSpecification = resSpec; // Pega o que é para ser buildado no resSpec e executa pós response
		
		// Desse modo agora todos os logs irão aparecer pós request
		// E todos os reponse serão logago pós testes
		
	}

	@Test
	public void devoTrabalharComXML() {
		given()
			
			.when()
				.get("/usersXML/3")
			 .then()
			 	.statusCode(200)
			 	.body("user.name", is("Ana Julia")) // Aqui de user ele entra em name e pega valor
			 	.body("user.@id", is("3"))// Aqui ele pega o valor do id que em XML retorna como string
			 	.body("user.filhos.name.size()", is(2))// Aqui dentro da lista filho ele pega a quantidade de names com .size()
			 	.body("user.filhos.name[0]", is("Zezinho"))// Aqui ele valida um por um pelo index
			 	.body("user.filhos.name[1]", is("Luizinho"))// Aqui ele valida um por um pelo index
			 	.body("user.filhos.name", hasItem("Luizinho"))//Aquie ele valida se existe
			 	.body("user.filhos.name", hasItems("Luizinho", "Zezinho"))//Aquie ele valida se existe
			 
			 
			 ;
		
		
	}
	

	@Test
	public void devoTrabalharComXMLComRootPath() {
		given()
			.when()
				.get("/usersXML/3")
			 .then()
			 	.statusCode(200)
			 	//Como o user vai user baster ele coloca como nó raiz das proximas chamad
			 	.rootPath("user")
			 	
			 	.body("name", is("Ana Julia")) // Aqui de user ele entra em name e pega valor
			 	.body("@id", is("3"))// Aqui ele pega o valor do id que em XML retorna como string
			 
			 	.rootPath("user.filhos")
			 	//como o  user.filhos é muito colocado fica como nó raiz
			 	.body("name.size()", is(2))// Aqui dentro da lista filho ele pega a quantidade de names com .size()
			 	
			 	// Quando um dia quiser tirar o nó raiz
			 	.detachRootPath("user.filhos")
			 	.body("user.filhos.name[0]", is("Zezinho"))// Aqui ele valida um por um pelo index
			 	.body("user.filhos.name[1]", is("Luizinho"))// Aqui ele valida um por um pelo index
			 	
			 	
			 	//quando quiser adicionar o nó raiz
			 	.appendRootPath("user.filhos")
			 	.body("name", hasItem("Luizinho"))//Aquie ele valida se existe
			 	.body("name", hasItems("Luizinho", "Zezinho"))//Aquie ele valida se existe
			 
			 
			 ;
		
		
	}
	
	@Test 
	public void devoFazerPesquisasAvancadasComXML() {
		given()
			.when()
			 .get("/usersXML/3")
			.then()
				.statusCode(200)
				.body("user.user.size()", is(3)) // Aqui ele pega e vê se a instancia user é de tamanho 3 
				.body("user.user.findAll{it.age.toInteger()<=25}.size()", is(2)) // aqui ele converter a age para inteiro e valida a quantida de age menor igual a 25 
				.body("users.user.@id", hasItems("1","2", "3")) // aqui ele valida que dentro dos valores ID contemo os valores , 1, 2 e 3 
				.body("users.user.find{it.age == 25 }.name", is("Maria Joaquina")) // Aqui ele pega dentro da primeira sessão de users o age com 25 anos 
				.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia")) // Aqui ele pega dentro todos os names quais tem o 'n'
				.body("user.user.salary.find{it != null}.toDouble()", is(1234.5678d)) //  Aqui ele pega o primeiro salario diferente de null
				.body("users.user.age.collect{it.toInteger()*2}", hasItems(40, 50, 60)) // Aqui ele pega todas a idade transformar para inteiro e multiplica por dois 
				.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA")) // Aqui ele pega os nomes que inicia com maria e torna ele em letra maiscula e faz a comparação
			
			
			
			
			
			;
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
