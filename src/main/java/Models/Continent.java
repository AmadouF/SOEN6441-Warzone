package Models;
import java.util.ArrayList;
import java.util.List;

import Utils.CommonUtil;

/**
 * Model class for Continent to store all its information and operations related to continent
 */
public class Continent {

    /**
     * Continent ID.
     */
    private Integer d_id;

    /**
     * Continent name.
     */
    private String d_name;

    /**
     * Continent value.
     */
    private Integer d_value;

    /**
     * List of countries.
     */
    private List<Country> d_countries;

    /**
     * Constructor for Continent class
     *
     * @param p_id Continent ID
     * @param p_name Continent name
     * @param p_value Continent value
     */
    public Continent(Integer p_id, String p_name, Integer p_value) {
        this(p_name);
        this.d_id=p_id;
        this.d_value=p_value;
    }

    /**
     * Constructor for Continent class
     *
     * @param p_name Continent name
     */
    public Continent(String p_name)
    {
        this.d_name = p_name;
    }

    /**
     * Get method to get Continent ID.
     *
     * @return Continent ID
     */
    public Integer getD_id() {
        return d_id;
    }

    /**
     * Set method to set Continent ID.
     *
     * @param p_id Continent ID
     */
    public void setD_id(Integer p_id) {
        this.d_id = p_id;
    }

    /**
     * Get method to get the Continent name.
     *
     * @return Continent name
     */
    public String getD_name() {
        return d_name;
    }

    /**
     * Set method to set the Continent name.
     *
     * @param p_name Continent name
     */
    public void setD_name(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Get method to get the Continent value.
     *
     * @return Continent value
     */
    public Integer getD_value() {
        return d_value;
    }

    /**
     * Set method to set the Continent value.
     *
     * @param p_value Continent value
     */
    public void setD_value(Integer p_value) {
        this.d_value = p_value;
    }

    /**
     * Get method to get the List of Countries.
     *
     * @return list of countries
     */
    public List<Country> getD_countries() {
        return d_countries;
    }

    /**
     * Set method to set the List of Countries.
     *
     * @param p_countries list of countries
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }

    /**
     * Add the specified Country to Continent.
     *
     * @param p_country Country which need to be added
     */
    public void addCountry(Country p_country){
        if (d_countries==null){
            d_countries=new ArrayList<Country>();
        }
        d_countries.add(p_country);
    }

    /**
     * Remove Country from Continent.
     *
     * @param p_country Country which need to be removed
     */
    public void removeCountry(Country p_country){
        if(d_countries==null || !d_countries.contains(p_country)){
            System.out.println("No such Country Exists " + p_country);
        }else {
            d_countries.remove(p_country);
        }
    }

	public void removeCountryNeighboursFromAll(Integer p_countryId){
		if (null!=d_countries && !d_countries.isEmpty()) {
			for (Country c: d_countries){
				if (!CommonUtil.isNull(c.getD_adjacentCountryIds())) {
					if (c.getD_adjacentCountryIds().contains(p_countryId)){
						c.removeAdjacentCountry(p_countryId);
					}
				}
			}
		}
	}

}