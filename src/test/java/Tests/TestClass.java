package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dataObjects.MainData;
import dataObjects.Endpoints;
import dataObjects.SupportClass;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TestClass {

	static String jsonRattata;
	static String jsonPidgeotto;
	static Endpoints endpoints;
	static SupportClass supportClass;
	static ObjectMapper objectMapper;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		objectMapper = new ObjectMapper();
		supportClass = new SupportClass(objectMapper);
		endpoints = new Endpoints();
		RestAssured.baseURI = endpoints.getBaseUrl();
		Response responseRattata = supportClass.responseForPokemon("Rattata");
		Response responsePidgeotto = supportClass.responseForPokemon("Pidgeotto");
		jsonRattata = responseRattata.print();
		jsonPidgeotto = responsePidgeotto.print();
	}

	@Test
	public void testTwoPokemonsByWeight() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		int rattataWeight = supportClass.getWeightFromJson(objectMapper, jsonRattata);
		int pidgeottoWeight = supportClass.getWeightFromJson(objectMapper, jsonPidgeotto);
		assertTrue(rattataWeight < pidgeottoWeight);
	}
	
	@Test
	public void testTwoPokemonsByAbility() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		int countRattata = 0, countPidgeotto = 0;
		countRattata = supportClass.countPokemonWithAbility(objectMapper, jsonRattata, "run-away");
		countPidgeotto = supportClass.countPokemonWithAbility(objectMapper, jsonPidgeotto, "run-away");
		assertTrue(countRattata > 0 && countPidgeotto == 0);
	}
	
	@Test
	public void checkLimitsTest() throws Exception {
		String n = "10";
		int listPokemonSize = 0;
		Response response = given()
                .contentType(ContentType.JSON)
                .param("limit", n)
                .param("offset", "0")
                .filter(new AllureRestAssured())
                .when()
                .get(endpoints.getAllPokemons())
                .then()
                .assertThat().statusCode(200)
                .extract().response();
		List<MainData> mainPokemonInfo = response.jsonPath().getList("results", MainData.class);
		listPokemonSize = mainPokemonInfo.size();
		mainPokemonInfo = mainPokemonInfo.stream()
				.filter(t -> t.getName().equals(null))
				.filter(t -> t.getName().equals(""))
				.collect(Collectors.toList());
		assertEquals(mainPokemonInfo.size(), 0);;//проверяем, что в итоговом массиве покемонов нет значений без имени или пустых записей
		assertEquals(listPokemonSize, Integer.parseInt(n)); //проверяем, что количество запрошенных покемонов равно количеству полученных
	}
}
