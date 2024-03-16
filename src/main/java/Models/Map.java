package Models;
import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.collections4.CollectionUtils;
import Exceptions.InvalidMap;



/**
 * This class manages all the edit operations for the maps and validates the maps.
 */
public class Map {
    /**
     * Hash map to track the neighbouring countries from a particular country.
     */
    HashMap<Country, Boolean> d_countryNeighbours;
    /**
     * List to store the continents of the map.
     */
    private List<Continent> d_continentsList;
    /**
     * List to store the countries of the map.
     */
    private List<Country> d_countriesList;
    /**
     * Stores the map file name.
     */
    private String d_mapFile;
    /**
     * Constructor to set the data members
     */
    public Map() {
        this.d_countryNeighbours = new HashMap<Country, Boolean>();
        this.d_continentsList = new ArrayList<Continent>();
        this.d_countriesList = new ArrayList<Country>();
        this.d_mapFile = null;
    }

    /**
     * setter method to set the continents list of map.
     * @param p_continentsList list of continents
     */
    public void setContinents(List<Continent> p_continentsList){
        this.d_continentsList=p_continentsList;
    }
    /**
     * getter method to get the continents list of the map.
     * @return list of continents
     */
    public List<Continent> getContinentsList(){
        return this.d_continentsList;
    }
    /**
     * setter method to set the list of countries of map.
     * @param p_countriesList list of the countries
     */
    public void setCountries(List<Country> p_countriesList){
        this.d_countriesList=p_countriesList;
    }
    /**
     * getter method to get the list of countries of map.
     * @return list of countries
     */
    public List<Country> getCountriesList(){
        return this.d_countriesList;
    }
    /**
     * setter method to set the name of the map file.
     * @param p_mapFile map file name
     */
    public void setMapFile(String p_mapFile){
        this.d_mapFile=p_mapFile;
    }
    /**
     * getter method to get the name of the map file.
     * @return map file name
     */
    public String getMapFile(){
        return this.d_mapFile;
    }
    /**
     * method to extract the ID's of countries from the countries list.
     * @return list of ID's of countries
     */
    public List<Integer> getCountryIDs(){
        List<Integer> l_countryIDs = new ArrayList<Integer>();
        if(!d_countriesList.isEmpty()){
            for(Country c: d_countriesList){
                l_countryIDs.add(c.getD_id());
            }
        }
        return l_countryIDs;
    }
    /**
     * method to get the continent object from the name of the continent
     * @param p_continentName name of the continent
     * @return Continent object
     */
    public Continent getContinent(String p_continentName){
        return d_continentsList.stream().filter(l_continent -> l_continent.getD_name().equals(p_continentName)).findFirst().orElse(null);
     }
    /**
     * This method handles the add country operation on the map.
     * @param p_countryName name of the country to be added
     * @param p_continentName name of the continent that the new country belongs to
     * @throws InvalidMap exception
     */
    public void addCountry(String p_countryName, String p_continentName) throws InvalidMap{
        int l_countryID=0;
        if(getCountryByName(p_countryName)==null){
            l_countryID=d_countriesList.size()>0? Collections.max(getCountryIDs())+1:1;
            if(d_continentsList!=null && getContinentIDs().contains(getContinent(p_continentName).getD_id())){
                Country l_newCountry=new Country(l_countryID,getContinent(p_continentName).getD_id(),p_countryName); //pass the continent name or id?
                d_countriesList.add(l_newCountry);
                for(Continent i:d_continentsList){
                    if(i.getD_name().equals(p_continentName)){
                        i.addCountry(l_newCountry);
                    }
                }
            } else{
                throw new InvalidMap("Cannot add Country to a Continent that doesn't exist!");
            }
        }else{
            throw new InvalidMap("Country with name "+ p_countryName+" already Exists!");
        }

    }
    /**
     * method to get the country object from the country name.
     * @param p_countryName name of the country
     * @return Country object 
     */
    public Country getCountryByName(String p_countryName){
        return d_countriesList==null? null : d_countriesList.stream().filter(l_country -> l_country.getD_name().equals(p_countryName)).findFirst().orElse(null);
    }
    /**
     * This method handles the remove country operation on the map.
     * @param p_countryName name of the country that is to be removed
     * @throws InvalidMap exception
     */
    public void removeCountry(String p_countryName)  throws InvalidMap{
        if(d_countriesList!=null && getCountryByName(p_countryName)!=null) {
            Country l_country=getCountryByName(p_countryName);
            
            for(Continent i:d_continentsList){
                if(l_country.getD_continentId().equals(i.getD_id())){
                    i.removeCountry(l_country);
                }
                i.removeCountryNeighboursFromAll(getCountryByName(p_countryName).getD_id());
            }
            // remove the country from the neighbouring/adjacenecy list
            for(Country i:d_countriesList){
                if (!CollectionUtils.isNotEmpty(i.getD_adjacentCountryIds())) {
                    if(i.getD_adjacentCountryIds().contains(l_country.getD_id())){
                        i.removeAdjacentCountry(l_country.getD_id());
                    }
                }
            }
            
            d_countriesList.remove(l_country);
        }else{
            throw new InvalidMap("Country:  "+ p_countryName+" does not exist!");
         }
    }

    /**
     * method to get Continent object from the name of the continent.
     * @param p_continentName name of the continent
     * @return Continent object
     */
    public Continent getContinentByName(String p_continentName){
        return d_continentsList.stream().filter(l_continent -> l_continent.getD_name().equals(p_continentName)).findFirst().orElse(null);
     }

     /**
     * method to get list of Continent IDs.
     * @return List of continent IDs
     */
     public List<Integer> getContinentIDs(){
        List<Integer> l_continentIDs = new ArrayList<Integer>();
        if (!d_continentsList.isEmpty()) {
            for(Continent c: d_continentsList){
                l_continentIDs.add(c.getD_id());
            }
        }
        return l_continentIDs;
    }

    /**
     * This method handles the add continent operation on the map.
     * @param p_continentName name of the continent that is to be added
     * @param p_bonus Control value for the new continent [Armies the player gets on acquiring the whole continent]
     * @throws InvalidMap exception
     */
    public void addContinent(String p_continentName, int p_bonus) throws InvalidMap{
        int l_continentId=0;
        if (d_continentsList!=null) {
            l_continentId=d_continentsList.size()>0?Collections.max(getContinentIDs())+1:1;
            Continent l_newContinent=new Continent(l_continentId,p_continentName,p_bonus);
            if(getContinent(p_continentName)==null){
                d_continentsList.add(l_newContinent);
            }else{
                throw new InvalidMap("Continent cannot be added! It already exists!");
            }
        }else{
            d_continentsList= new ArrayList<Continent>();
            d_continentsList.add(new Continent(1, p_continentName, p_bonus));
        }
    }
    /**
     * This method handles the remove continent operation on the map.
     * @param p_continentName name of the continent that is to be removed
     * @throws InvalidMap exception
     */
    public void removeContinent(String p_continentName)  throws InvalidMap{
        if (d_continentsList!=null) {
            if(getContinent(p_continentName)!=null){
                Continent l_continent=getContinentByName(p_continentName);
                // Deletes the continent and updates neighbour as well as country objects
                if (getContinent(p_continentName).getD_countries()!=null) {
                    
                    
                    for(Country i:l_continent.getD_countries()){
                        // updating the adjacenecy list
                        removeCountryNeighboursFromAll(i.getD_id());
                        for(Continent c: d_continentsList){
                            c.removeCountryNeighboursFromAll(i.getD_id());
                        }
                        
                        d_countriesList.remove(i);
                        
                    }
                }

            d_continentsList.remove(l_continent);
            }else{
                throw new InvalidMap("No such Continent exists!");
            }
        } else{
            throw new InvalidMap("No continents in the Map to remove!");
        }
    }
    /**
     * This method is responsible for removing the specific country as neighbour for other countries.
     * @param p_countryID ID for the country that should be removed as neighbour
     */
    public void removeCountryNeighboursFromAll(Integer p_countryID){
        for (Country c: d_countriesList) {
            if (!CollectionUtils.isNotEmpty(c.getD_adjacentCountryIds())) {
                if (c.getD_adjacentCountryIds().contains(p_countryID)) {
                    c.removeAdjacentCountry(p_countryID);
                }
            }
        }
    }

    
    /**
     * This method handles the validation for the map.
     * @return True or False. The map is valid or not.
     * @throws InvalidMap exception
     */
    public boolean isValidMap() throws InvalidMap {
        //checks if atleast 1 continent exists
        if(CollectionUtils.isEmpty(d_continentsList)){
            throw new InvalidMap(" !!!  Map must have minimum one continent !!!");
        }

        //check if atleast 1 country exists
        if(CollectionUtils.isEmpty(d_countriesList)){
            throw new InvalidMap(" !!!  Map must have minimum one country !!!");
        }
        //checks whether the country has neighbors or not.
        for(Country c: d_countriesList){
            if(c.getD_adjacentCountryIds().size()<1){
                throw new InvalidMap(c.getD_name()+" does not possess any neighbour, hence isn't reachable!");
            }
        }
        //checks whether the continent possesses any country or not.
        for (Continent c:d_continentsList){
			if (c.getD_countries()==null || c.getD_countries().size()<1){
				throw new InvalidMap(c.getD_name() + " has no countries, it must possess atleast 1 country");
			}
			if(!isContinentConnected(c)){
				return false;
			}
		}

        return true;



        
    }
    /**
     * This method checks whether all the countries are connected or not. [Directed connected graph]
     * @return True or False. Are the countries connected or not.
     * @throws InvalidMap exception
     */
    public boolean areCountriesConnected() throws InvalidMap {
        // HashMap<Country, Boolean> l_visitedCountryMap = new HashMap<Country, Boolean>();

        for (Country l_country : d_countriesList) {
            d_countryNeighbours.put(l_country, false);
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
    /**
     * Applies the depth first search from one specified country to the neighbouring countries tree.
     * @param p_c Country from which the DFS has to be applied
     * @throws InvalidMap exception
     */
    public void dfsCountry(Country p_c) throws InvalidMap {
        d_countryNeighbours.put(p_c, true);
        for (Country l_nextCountry : getAdjacentCountry(p_c)) {
            if (!d_countryNeighbours.get(l_nextCountry)) {
                dfsCountry(l_nextCountry);
            }
        }
    }
    /**
     * method to get the country object from the country ID.
     * @param p_countryId Country ID.
     * @return Country object
     */
    public Country getCountry(Integer p_countryId) {
        return d_countriesList.stream().filter(l_country -> l_country.getD_id().equals(p_countryId)).findFirst().orElse(null);
    }

    
    /**
     * This method checks whether the continent is connected or not. [Directed connected subgraph ]
     * @param p_continent Continent object
     * @return True or false. Whether the continent is connected subgraph or not.
     * @throws InvalidMap exception
     */
    public boolean isContinentConnected(Continent p_continent) throws InvalidMap{
        HashMap<Country, Boolean> l_visitedCountryMap = new HashMap<Country, Boolean>();

        for (Country l_country : p_continent.getD_countries()) {
            l_visitedCountryMap.put(l_country, false);
        }
        dfs(p_continent.getD_countries().get(0), l_visitedCountryMap, p_continent);

        
        for (Entry<Country, Boolean> entry : l_visitedCountryMap.entrySet()) {
            if (!entry.getValue()) {
                Country l_country = entry.getKey();
                String l_messageException = l_country.getD_name() + " in Continent " + p_continent.getD_name() + " is not reachable";
                throw new InvalidMap(l_messageException);
            }
        }
        return !l_visitedCountryMap.containsValue(false);
    }
    /**
     * Applies the DFS for the countries in the continent.
     * @param p_country Current country from where DFS is to applied
     * @param l_visitedCountryMap Hash map to track whether the country is visited or not.
     * @param p_continent Continent name
     */
    public void dfs(Country p_country, HashMap<Country, Boolean> l_visitedCountryMap, Continent p_continent){
        l_visitedCountryMap.put(p_country, true);
        for (Country l_country : p_continent.getD_countries()) {
            if (p_country.getD_adjacentCountryIds().contains(l_country.getD_id())) {
                if (!l_visitedCountryMap.get(l_country)) {
                    dfs(l_country, l_visitedCountryMap, p_continent);
                }
            }
        }
    }


    /**
     * This method gets the list of countries which are adjacent to the specified country.
     * @param p_country Country Object
     * @return List of adjacent countries
     * @throws InvalidMap exception
     */
    public List<Country> getAdjacentCountry(Country p_country) throws InvalidMap {
        List<Country> l_adjCountries = new ArrayList<Country>();

        if (p_country.getD_adjacentCountryIds().size() > 0) {
			for (int i : p_country.getD_adjacentCountryIds()) {
                l_adjCountries.add(getCountry(i));
            }
        } else {
            throw new InvalidMap(p_country.getD_name() + " doesn't have any adjacent countries");
		}
		return l_adjCountries;
	}


    /**
     * This method handles the add neighbor operation on the map.
     * @param p_countryName Name of the country for which the neighbor is to added
     * @param p_neighbourName Name of the neighbour country
     * @throws InvalidMap exception
     */
    public void addNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countriesList!=null){
            if(getCountryByName(p_countryName)!=null && getCountryByName(p_neighbourName)!=null){
                d_countriesList.get(d_countriesList.indexOf(getCountryByName(p_countryName))).addAdjacentCountry(getCountryByName(p_neighbourName).getD_id());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }
    /**
     * This method handles the remove neighbour operation on the map.
     * @param p_countryName Name of the country for which the neighbour is to removed. 
     * @param p_neighbourName Name of neighbour country.
     * @throws InvalidMap exception
     */
    public void removeNeighbour(String p_countryName, String p_neighbourName) throws InvalidMap{
        if(d_countriesList!=null){
            if(getCountryByName(p_countryName)!=null && getCountryByName(p_neighbourName)!=null) {
                d_countriesList.get(d_countriesList.indexOf(getCountryByName(p_countryName))).removeAdjacentCountry(getCountryByName(p_neighbourName).getD_id());
            } else{
                throw new InvalidMap("Invalid Neighbour Pair! Either of the Countries Doesn't exist!");
            }
        }
    }



}
