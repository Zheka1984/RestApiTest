package dataObjects;

public class Endpoints {

	public String baseUrl = "https://pokeapi.co/api/v2/";
	public String prefixForRattata = "pokemon/rattata";
	public String prefixForPidgeotto = "pokemon/pidgeotto";
	public String allPokemons = "/pokemon";
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public String getPrefixForRattata() {
		return prefixForRattata;
	}
	
	public String getPrefixForPidgeotto() {
		return prefixForPidgeotto;
	}
	
	public String getAllPokemons() {
		return allPokemons;
	}
}
