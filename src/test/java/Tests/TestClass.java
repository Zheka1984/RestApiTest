package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import PokemonObjects.BaseObject;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TestClass {

	int count;
	int countNull;
	static String jsonRattata;
	static String jsonPidgeotto;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		RestAssured.baseURI = "https://pokeapi.co/api/v2/";
		Response responseRattata = given()
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .when()
                .get("pokemon/rattata")
                .then()
                .assertThat().statusCode(200)
                .extract().response();
		Response responsePidgeotto = given()
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .when()
                .get("pokemon/pidgeotto")
                .then()
                .assertThat().statusCode(200)
                .extract().response();
		jsonRattata = responseRattata.print();
		jsonPidgeotto = responsePidgeotto.print();
	}

	@Test
	public void testTwoPokemonsByWeight() throws JsonMappingException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		int rattata = om.readTree(jsonRattata).get("weight").intValue();
		int pidgeotto = om.readTree(jsonPidgeotto).get("weight").intValue();
		System.out.println(rattata +  " " + pidgeotto);
		assertTrue(rattata < pidgeotto);
	}
	
	@Test
	public void testTwoPokemonsByAbility() throws IOException {
		int countRattata = 0, countPidgeotto = 0;
		ObjectMapper om = new ObjectMapper();
		List<JsonNode> jn = om.readTree(jsonRattata).get("abilities").findValues("ability");
		BaseObject[] bo = om.readValue(jn.toString(), BaseObject[].class);
		for(BaseObject b : bo) {
			if(b.getName().equals("run-away")) countRattata++;
		}
		List<JsonNode> jn1 = om.readTree(jsonPidgeotto).get("abilities").findValues("ability");
		BaseObject[] bo1 = om.readValue(jn1.toString(), BaseObject[].class);
		for(BaseObject b : bo1) {
			if(b.getName().equals("run-away")) countPidgeotto++;
		}
		//System.out.println(countRattata +" "+countPidgeotto);
		assertTrue(countRattata > 0 && countPidgeotto == 0);
	}
	
	@Test
	public void checkLimitsTest() throws Exception {
		String n = "10";
		Response response = given()
                .contentType(ContentType.JSON)
                .param("limit", n)
                .param("offset", "0")
                .filter(new AllureRestAssured())
                .when()
                .get("/pokemon")
                .then()
                .assertThat().statusCode(200)
                .extract().response();
		List<BaseObject> list = response.jsonPath().getList("results", BaseObject.class);
		list.forEach(t -> {
		if(t.getName().equals(null)) countNull++; 	
		if(t.getName().equals("")) count++;
		});
		assertTrue(countNull == 0); //проверяем, есть ли поле name, если его нет, то метод getName() возвратит null
		assertTrue(count == 0); //проверяем, нет ли имен с пустыми значениями
		assertEquals(list.size(), Integer.parseInt(n)); //проверяем, что количество запрошенных покемонов равно количеству полученных
	}

}
