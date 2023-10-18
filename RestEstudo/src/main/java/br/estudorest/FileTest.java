package br.estudorest;

import org.apache.groovy.json.internal.IO;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FileTest {

    @Test
    public void deveObrigsrEnvioArquivo(){
        given()
                .log().all()
                .when()
                .post("http://restapi.wcaquino.me/upload") // Aqui ta fazendo a requisição sem o arquivo
                .then()
                .log().all()
                .statusCode(404)
                //Validando o retorno do erro
                .body("error", is("Arquivo não enviado"))
        ;





    }

    @Test
    public void fazerUploadArquivo(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/testefile.pdf"))
                //essa class faz o enviio do arquivo e seta um nome
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("testefile.pdf"))// Validando body response o campo name se é o nome do pdf
        ;





    }

    @Test
    public void fazerUploadArquivoMaiorQudOEsperado(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/aqui eu enviaria um arquivo maior que um MB "))
                //Acima no caso está simulando se estivesse adicionando um pdf maior do que o suportado pela requisição
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .time(lessThan(5000l))
                //A class time valida o tempo junto com o matcher lessThan
                .statusCode(413)
                //Aqui valida o retorno de erro
        ;





    }

    @Test
    public void deveBaixarArquivo() throws IOException {
        byte[] image = given() // Aqui essa requisição vai retornar uma sequencia de bytes
                //Aonde tais bytes capturados no logs.all vai para a variavel byte que é uma array de bytes
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/download")// Aqui é o end point da imagem
                .then()
                .statusCode(200)
                .extract().asByteArray(); // Aqui ele esxtra o array de byte

        File imagem = new File("src/main/resources/file.jpg"); // Aqui pela class file ele cria um espaço
        //para uma variavel imagem
        OutputStream out = new FileOutputStream(imagem); // O usa essa imagem como valor no construtor
            out.write(image); // Aqui o out ja de ponteiro escreve o array chamado na imagem que colocada no construtor
        //como valor inicial
            out.close();


        Assert.assertThat(imagem.length(), lessThan(100000L));



    }






}
