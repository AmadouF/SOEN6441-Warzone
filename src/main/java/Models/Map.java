package Models;
import java.util.*;
import java.util.Map.Entry;

import Exceptions.InvalidMap;
import Utils.CommonUtil;

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
    HashMap<Country, Boolean> d_countryNeighbours = new HashMap<Country, Boolean>();
    private List<Continent> d_continentsList;
    private List<Country> d_countriesList;
  
    private String d_mapFile;
    //    private String d_mapName;
    
    public void setContinents(List<Continent> p_continentsList){
        this.d_continentsList=p_continentsList;
    }
    public List<Continent> getContinentsList(){
        return this.d_continentsList;
    }
    public void setCountries(List<Country> p_countriesList){
        this.d_countriesList=p_countriesList;
    }
    public List<Country> getCountriesList(){
        return this.d_countriesList;
    }
    public List<Integer> getCountryIDs(){
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if(!d_countriesList.isEmpty()){
            for(Country c: d_countriesList){
                l_countryIDs.add(c.getD_continentId());
            }
        }
        return l_countryIDs;
    }
    public Continent getContinent(String p_continentName){
        return d_continentsList.stream().filter(l_continent -> l_continent.getContinentName().equals(p_continentName)).findFirst().orElse(null);
     }

    public void setMapFile(String p_mapFile){
        this.d_mapFile=p_mapFile;
    }
    public String getMapFile(){
        return this.d_mapFile;
    }
    public void addCountry(String p_countryName, String p_continentName){
        int l_countryID=0;
        l_countryID=d_countriesList.size()>0? Collections.max(getCountryIDs())+1:1;
        Country l_newCountry=new Country(l_countryID,getContinent(p_continentName).getD_continentID(),p_countryName); //pass the continent name or id?
        d_countriesList.add(l_newCountry);
        for(Continent i:d_continentsList){
            if(i.getContinentName().equals(p_continentName)){
                i.addCountry(l_newCountry);
            }
        }

    }
    public Country getCountryByName(String p_countryName){
        return d_countriesList.stream().filter(l_country -> l_country.getD_name().equals(p_countryName)).findFirst().orElse(null);
    }
    public void removeCountry(String p_countryName){
        Country l_country=getCountryByName(p_countryName);
        //country->continentID
        //continent name list
        for(Continent i:d_continentsList){
            if(l_country.getD_continentId()==i.getD_continentID()){
                i.removeCountry(l_country);
            }
        }
        // remove the country from the neighbouring/adjacenecy list
        for(Country i:d_countriesList){
            if(i.getD_adjacentCountryIds().contains(l_country.getD_id())){
                i.removeAdjacentCountry(l_country.getD_id());
            }
        }
        
        d_countriesList.remove(l_country);
    }


    public Continent getContinentByName(String p_continentName){
        return d_continentsList.stream().filter(l_continent -> l_continent.getContinentName().equals(p_continentName)).findFirst().orElse(null);
     }


     public List<Integer> getContinentIDs(){
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_continentsList.isEmpty()) {
            for(Continent c: d_continentsList){
                l_continentIDs.add(c.getD_continentID());
            }
        }
        return l_continentIDs;
    }


    public void addContinent(String p_continentName, int bonus){
        int l_continentId=0;
        l_continentId=d_continentsList.size()>0?Collections.max(getContinentIDs())+1:1;
        Continent l_newContinent=new Continent(l_continentId,p_continentName,bonus);
        d_continentsList.add(l_newContinent);
    }

    public void removeContinent(String p_continentName){
        Continent l_continent=getContinentByName(p_continentName);
        for(Country i:l_continent.getCountries()){
            // updating the adjacenecy list
            removeCountryNeighboursFromAll(i.getD_id());
            for(Continent c: d_continentsList){
                c.removeCountryNeighboursFromAll(i.getD_id());
            }
            removeCountry(i.getD_name());
            
        }

        d_continentsList.remove(l_continent);
    }

    public void removeCountryNeighboursFromAll(Integer p_countryID){
        for (Country c: d_countriesList) {
            if (!CommonUtil.isNull(c.getD_adjacentCountryIds())) {
                if (c.getD_adjacentCountryIds().contains(p_countryID)) {
                    c.removeAdjacentCountry(p_countryID);
                }
            }
        }
    }

    

    public boolean isValidMap() throws InvalidMap {
        if(d_continentsList==null || d_continentsList.isEmpty() || d_countriesList==null || d_countriesList.isEmpty()){
            
            throw new InvalidMap("Map must possess atleast one continent!");
            
        }
        
        for(Country c: d_countriesList){
            if(c.getD_adjacentCountryIds().size()<1){
                throw new InvalidMap(c.getD_name()+" does not possess any neighbour, hence isn't reachable!");
            }
        }

        for (Continent c:d_continentsList){
			if (c.getCountries()==null || c.getCountries().size()<1){
				throw new InvalidMap(c.getContinentName() + " has no countries, it must possess atleast 1 country");
			}
			if(!isContinentConnected(c)){
				return false;
			}
		}

        return true;



        
    }

    public boolean areCountriesConnected() throws InvalidMap {
        // HashMap<Country, Boolean> l_visitedCountryMap = new HashMap<Country, Boolean>();

        for (Country c : d_countriesList) {
            d_countryNeighbours.put(c, false);
        }
        dfsCountry(d_countriesList.get(0));

        // Iterates over entries to locate the unreachable country
        for (Entry<Country, Boolean> entry : d_countryNeighbours.entrySet()) {
            if (!entry.getValue()) {
                String l_exceptionMessage = getCountry(entry.getKey().getD_id()).getD_name() + " country is not reachable";
                throw new InvalidMap(l_exceptionMessage);
            }
        }
        return !d_countryNeighbours.containsValue(false);
    }

    public void dfsCountry(Country p_c) throws InvalidMap {
        d_countryNeighbours.put(p_c, true);
        for (Country l_nextCountry : getAdjacentCountry(p_c)) {
            if (!d_countryNeighbours.get(l_nextCountry)) {
                dfsCountry(l_nextCountry);
            }
        }
    }

    public Country getCountry(Integer p_countryId) {
        return d_countriesList.stream().filter(l_country -> l_country.getD_id().equals(p_countryId)).findFirst().orElse(null);
    }

    public void dfsCountry(Country p_country, HashMap<Country, Boolean> l_visitedCountryMap) {
        l_visitedCountryMap.put(p_country, true);
        List<Country> l_adjCountries = new ArrayList<Country>();

        
		for (int i : p_country.getD_adjacentCountryIds()) {
            l_adjCountries.add(getCountry(i));
        }
        
		
        for (Country l_country : l_adjCountries) {
            if (!l_visitedCountryMap.get(l_country)) {
                dfsCountry(l_country,l_visitedCountryMap);
            }
        }
    }

    // public Country getCountry(int p_countryId) {
    //     return d_countriesList.stream().filter(l_country -> l_country.getD_countryId().equals(p_countryId)).findFirst().orElse(null);
    // }

    public boolean isContinentConnected(Continent p_continent) throws InvalidMap{
        HashMap<Country, Boolean> l_visitedCountryMap = new HashMap<Country, Boolean>();

        for (Country c : p_continent.getCountries()) {
            l_visitedCountryMap.put(c, false);
        }
        dfs(p_continent.getCountries().get(0), l_visitedCountryMap, p_continent);

        
        for (Entry<Country, Boolean> entry : l_visitedCountryMap.entrySet()) {
            if (!entry.getValue()) {
                Country l_country = entry.getKey();
                String l_messageException = l_country.getD_name() + " in Continent " + p_continent.getContinentName() + " is not reachable";
                throw new InvalidMap(l_messageException);
            }
        }
        return !l_visitedCountryMap.containsValue(false);
    }

    public void dfs(Country p_country, HashMap<Country, Boolean> l_visitedCountryMap, Continent p_continent){
        l_visitedCountryMap.put(p_country, true);
        for (Country l_country : p_continent.getCountries()) {
            if (p_country.getD_adjacentCountryIds().contains(l_country.getD_id())) {
                if (!l_visitedCountryMap.get(l_country)) {
                    dfs(l_country, l_visitedCountryMap, p_continent);
                }
            }
        }
    }



    public List<Country> getAdjacentCountry(Country p_country) {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (p_country.getD_adjacentCountryIds().size() > 0) {
			for (int i : p_country.getD_adjacentCountryIds()) {
                l_adjCountries.add(getCountry(i));
            }
        } 
		return l_adjCountries;
	}



    public void addNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countriesList!=null){
            if(!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighbourName))){
                d_countriesList.get(d_countriesList.indexOf(getCountryByName(p_countryName))).addAdjacentCountry(getCountryByName(p_neighbourName).getD_id());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }

    public void removeNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countriesList!=null){
            if(!CommonUtil.isNull(getCountryByName(p_countryName)) && !CommonUtil.isNull(getCountryByName(p_neighbourName))) {
                d_countriesList.get(d_countriesList.indexOf(getCountryByName(p_countryName))).removeAdjacentCountry(getCountryByName(p_neighbourName).getD_id());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }



}
