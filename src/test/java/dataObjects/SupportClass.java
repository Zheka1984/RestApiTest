package dataObjects;

import static io.restassured.RestAssured.given;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SupportClass {

	ObjectMapper objectMapper;
	
	public SupportClass(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public int getWeightFromJson(ObjectMapper objectMapper, String jsonforPokemon) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readTree(jsonforPokemon).get("weight").intValue();
	}
	
	public List<JsonNode> getListOfAbilities(ObjectMapper objectMapper, String jsonforPokemon) throws JsonMappingException, JsonProcessingException{
		return objectMapper.readTree(jsonforPokemon).get("abilities").findValues("ability");
	}
	
	public MainData[] convertToBaseObjectArray(List<JsonNode> jsonNodeList, ObjectMapper objectMapper) throws JsonMappingException, JsonProcessingException {
		return objectMapper.readValue(jsonNodeList.toString(), MainData[].class);
	}
	
	public int countPokemonWithAbility(ObjectMapper objectMapper, String jsonPokemon, String ability) throws JsonMappingException, JsonProcessingException {
		int countPokemon = 0;
		List<JsonNode> jsonNodeList = getListOfAbilities(objectMapper, jsonPokemon);
		MainData[] baseObjectArray2 = convertToBaseObjectArray(jsonNodeList, objectMapper);
		for(MainData b : baseObjectArray2) {
			if(b.getName().equals(ability)) countPokemon++;
		}
		return countPokemon;
	}
	
	public Response responseForPokemon(String pokemonName) {
		Endpoints endpoints = new Endpoints();
		pokemonName = pokemonName.toLowerCase();
		String prefixForPokemon = null;
		if(pokemonName.equals("rattata")) prefixForPokemon = endpoints.getPrefixForRattata(); 
		if(pokemonName.equals("pidgeotto")) prefixForPokemon = endpoints.getPrefixForPidgeotto();
		return given()
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .when()
                .get(prefixForPokemon)
                .then()
                .assertThat().statusCode(200)
                .extract().response();
	}
}
