package warzone.Models;
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

}
