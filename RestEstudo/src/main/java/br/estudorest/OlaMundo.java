package br.estudorest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {

	public static void main(String[] args) {
		Response response = RestAssured .request(Method.GET, "http://restapi.wcaquino.me/ola");
		System.out.println(response.getBody().asString().equals("Ola Mundo!"));
		//Response response = RestAssured .request(Method.GET, "http://IP/ola");
		// Acima é uma demostração quando uma API estiver em mais de um servidor 
		// Com a URL de chama da no terminal o comando ping "nome da chamada"
		// Ou colocando a porta como abaixo:
		//Response response = RestAssured .request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		System.out.println(response.statusCode()== 200); // aqui ele está pegando e comparando
		ValidatableResponse validacao = response.then(); // retorna validacoes do response
		validacao.statusCode(200); // aqui ele está apenas comparando com o statuscode
	}

}
