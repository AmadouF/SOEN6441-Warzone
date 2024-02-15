package Models;

import java.util.HashSet;
import java.util.Set;

public class Country extends Object{

    /**
     * Country ID
     */
    private Integer d_id;

    /**
     * ID of the Continent this country belongs to
     */
    private Integer d_continentId;

    /**
     * Name of the country
     */
    private String d_name;

    /**
     * The number of army personnel for the country
     */
    private Integer d_army;

    /**
     * Set of all the countries which can be reached directly by this country
     */
    private Set<Integer> d_adjacentCountryIds = new HashSet<>();;

    /**
     * Constructor of Country class
     *
     * @param p_id Country ID
     * @param p_continentId ID of the Continent this Country belongs to
     */
    public Country(Integer p_id, Integer p_continentId) {
        d_id = p_id;
        d_continentId = p_continentId;
    }

    /**
     * Constructor of Country class
     *
     * @param p_id Country ID
     * @param p_name Country Name
     * @param p_continentId ID of the Continent this Country belongs to
     */
    public Country(Integer p_id, Integer p_continentId, String p_name) {
        this(p_id, p_continentId);
        d_name = p_name;
    }

    /**
     * Getter method for the Country ID
     *
     * @return Country ID
     */
    public Integer getD_id() {
        return d_id;
    }

    /**
     * Getter method to get the Continent ID
     *
     * @return Continent ID
     */
    public Integer getD_continentId() {
        return d_continentId;
    }

    /**
     * Getter method for the Name of this Country
     *
     * @return Country Name
     */
    public String getD_name() {
        return d_name;
    }

    /**
     * Setter method to set the Name of this Country
     *
     * @param p_name Name to be set for this Country
     */
    public void setD_name(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Getter method for the number of army personnel
     *
     * @return Number of army personnel in this Country
     */
    public Integer getD_army() {
        return d_army;
    }

    /**
     * Setter method to set the number of army personnel
     *
     * @param p_army Number of army personnel
     */
    public void setD_army(Integer p_army) {
        this.d_army = p_army;
    }

    /**
     * Getter method to get the Adjacent Country IDs
     *
     * @return Set of adjacent country IDs
     */
    public Set<Integer> getD_adjacentCountryIds() {
        if(d_adjacentCountryIds==null){
            return new HashSet<Integer>();
        }

        return d_adjacentCountryIds;
    }

    /**
     * Setter method to set the Adjacent Country IDs.
     *
     * @param p_adjacentCountryIds Set of adjacent country IDs
     */
    public void setD_adjacentCountryIds(Set<Integer> p_adjacentCountryIds) {
        this.d_adjacentCountryIds = p_adjacentCountryIds;
    }

    /**
     * Adds a new country to the Adjacent Country List.
     *
     * @param p_countryId ID of the new country to be added as a neighbour
     */
    public void addAdjacentCountry(Integer p_countryId){
        d_adjacentCountryIds.add(p_countryId);
    }

    /**
     * Removes a country from adjacent country list
     *
     * @param p_countryId ID of country to be removed as neighbour
     */
    public void removeAdjacentCountry(Integer p_countryId){
        if(d_adjacentCountryIds.contains(p_countryId)){
            d_adjacentCountryIds.remove(p_countryId);
        }else{
            System.out.println("No neighbour country exists with id : " + p_countryId);
        }
    }
}
