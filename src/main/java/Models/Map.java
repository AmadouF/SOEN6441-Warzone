package Models;
import java.util.*;

import java.io.*;
/*
 * Map model:
 * 1. Data: 
 *      Continents List
 *      Countries List
 *      Adjacency list?
 * 2. Methods:
 *      Add continent
 *      Add country
 *      Remove continent
 *      Remove country
 *      Add neighbour
 *      Remove neighbour
 *      Get adjacency list for the country
 *      VALIDATION:
 *          1. There are no countries or continents or any country has no neighbouring country
 *          2. Check that every continent has at least one country
 *          3. Check that the map is connected or not.
 *          4. Check that every continent is strongly connected or not.     
 */
/*
 * Expected methods in Continent and Country models:
 * Continent:
 *      getContinentName(), addCountry(String name), removeCountry(Country c), getContinentID(), getCountries()
 * Country:
 *      getContinentID(), 
 */
public class Map {
    private List<Continent> d_continentsList;
    private List<Country> d_countriesList;
    public setContinents(List<Continent> p_continentsList){
        this.d_continentsList=p_continentsList;
    }
    public List<String> getContinentsList(){
        return this.d_continentsList;
    }
    public setCountries(List<Country> p_countriesList){
        this.d_countriesList=p_countriesList;
    }
    public List<String> getCountriesList(){
        return this.d_countriesList;
    }
    
    public void addCountry(String p_countryName, String p_continentName){
        Country l_newCountry=new Country(p_countryName,p_continentName); //pass the continent name or id?
        d_countriesList.add(l_newCountry);
        for(Continent i:d_continentsList){
            if(i.getContinentName().equals(p_continentName)){
                i.addCountry(l_newCountry);
            }
        }

    }
    
    public void removeCountry(String p_countryName){
        Country l_country=getCountryByName(p_countryName);
        //country->continentID
        //continent name list
        for(Continent i:d_continentsList){
            if(l_country.getContinentID()==i.getContinentID()){
                i.removeCountry(l_country);
            }
        }
        // remove the country from the neighbouring/adjacenecy list
        for(Country i:d_countriesList){
            if(i.getAdjacentCountryID().contains(l_country.getCountryID())){
                i.removeNeighbour(l_country.getCountryID);
            }
        }
        
        d_countriesList.remove(l_country);
    }

    public void addContinent(String p_continentName, int bonus){
        Continent l_newContinent=new Continent(p_continentName,bonus);
        d_continentsList.add(l_newContinent);
    }

    public void removeContinent(String p_continentName){
        Continent l_continent=getContinentByName(p_continentName);
        for(Country i:l_continent.getCountries()){
            // updating the adjacenecy list
            
            removeCountry(i.getCountryName);
            
        }

        d_continentsList.remove(l_continent);
    }


    public boolean isValidMap(){
        if(d_continents==null || d_continents.isEmpty() || d_countries==null || d_countries.isEmpty()){
            return false;
        }
        
        for(Country c: d_countries){
            if(c.getAdjacentCountryID().size()<1){
                return false;
            }
        }

        for (Continent c:d_continents){
			if (c.getCountries()==null || c.getCountries().size()<1){
				return false;
			}
			if(!isContinentConnected(c)){
				return false;
			}
		}

        return true && areCountriesConnected();



        return true;
    }

    public boolean areCountriesConnected(){
        HashMap<Country, Boolean> l_visitedCountryMap = new HashMap<Country, Boolean>();

        for (Country c : d_countriesList) {
            l_visitedCountryMap.put(c.getD_countryId(), false);
        }
        dfsCountry(d_countriesList.get(0),l_visitedCountryMap);


        return !l_visitedCountryMap.containsValue(false);
    }

    public void dfsCountry(Country p_country, HashMap<Country, Boolean> l_visitedCountryMap) {
        l_visitedCountryMap.put(p_country, true);
        List<Country> l_adjCountries = new ArrayList<Country>();

        
		for (int i : p_country.getAdjacentCountryID()) {
            l_adjCountries.add(getCountry(i));
        }
        
		
        for (Country l_country : l_adjCountries) {
            if (!l_visitedCountryMap.get(l_country)) {
                dfsCountry(l_country);
            }
        }
    }

    // public Country getCountry(int p_countryId) {
    //     return d_countriesList.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryId)).findFirst().orElse(null);
    // }

    public boolean isContinentConnected(Continent p_continent){
        HashMap<Country, Boolean> l_visitedCountryMap = new HashMap<Country, Boolean>();

        for (Country c : p_continent.getCountries()) {
            l_visitedCountryMap.put(c, false);
        }
        dfs(p_continent.getCountries().get(0), l_visitedCountryMap, p_continent);

        
        // for (Entry<Country, Boolean> entry : l_visitedCountryMap.entrySet()) {
        //     if (!entry.getValue()) {
        //         Country l_country = entry.getKey();
        //         String l_messageException = l_country.getD_countryName() + " in Continent " + p_continent.getD_continentName() + " is not reachable";
        //         throw new InvalidMap(l_messageException);
        //     }
        // }
        return !l_visitedCountryMap.containsValue(false);
    }

    public void dfs(Country p_country, HashMap<Country, Boolean> l_visitedCountryMap, Continent p_continent){
        l_visitedCountryMap.put(p_country, true);
        for (Country l_country : p_continent.getCountries()) {
            if (p_country.getAdjacentCountryID.contains(l_country.getCountryID())) {
                if (!l_visitedCountryMap.get(l_country)) {
                    dfs(l_country, l_visitedCountryMap, p_continent);
                }
            }
        }
    }

}
