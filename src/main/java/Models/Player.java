package Models;

import java.util.*;
import Helpers.PlayerHelper;
import Utils.Command;
import org.apache.commons.collections4.CollectionUtils;
import java.io.*;

/**
 * This class holds the player's data and issuing the order functionality.
 */
public class Player {
    /**
     * Name of the player
     */
    private String d_playerName;
    /**
     * List of the countries owned by the player
     */
    private List<Country> d_ownedCountries;
    /**
     * List of the continents owned by the player
     */
    private List<Continent> d_ownedContinents;
    /**
     * Number of reinforcement armies of the player
     */

    private Integer d_reinforcement;

    /**
     * List of orders issued by the player
     */
    private List<Order> d_issuedOrders;
    /**
     * Player constructor to initialize the player object
     * @param p_name Name of the player
     */
    public Player(String p_name){
        this.d_playerName=p_name;
        this.d_reinforcement=0;
        this.d_ownedCountries=new ArrayList<>();
        this.d_ownedContinents=new ArrayList<>();
        this.d_issuedOrders=new ArrayList<>();
    }
    /**
     * Setter method to set the name of the player
     * @param p_name name of the player
     */
    public void setPlayerName(String p_name){
        this.d_playerName=p_name;
    }
    /**
     * Getter method to get the name of the player
     * @return Name of the player
     */
    public String getPlayerName(){
        return this.d_playerName;
    }
    /**
     * Setter method to set the list of countries owned by the player
     * @param p_ownedCountries List of owned countries
     */
    public void setOwnedCountries(List<Country> p_ownedCountries){
        this.d_ownedCountries=p_ownedCountries;
    }
    /**
     * Getter method to get the list of countries owned by the player
     * @return list of owned countries
     */
    public List<Country> getOwnedCountries(){
        return this.d_ownedCountries;
    }
    /**
     * Setter method to set the list of continents owned by the player
     * @param p_ownedContinents List of owned continents
     */
    public void setOwnedContinents(List<Continent> p_ownedContinents){
        this.d_ownedContinents=p_ownedContinents;
    }
    /**
     * Getter method to get the list of countries owned by the player
     * @return list of owned continents
     */
    public List<Continent> getOwnedContinents(){
        return this.d_ownedContinents;
    }
    /**
     * Setter method to set the list of orders issued by the player
     * @param p_issuedOrders List of issued orders
     */
    public void setIssuedOrders(List<Order> p_issuedOrders){
        this.d_issuedOrders=p_issuedOrders;
    }
    /**
     * Getter method to get the list of orders issued by the player
     * @return List of issued orders
     */
    public List<Order> getIssuedOrders(){
        return this.d_issuedOrders;
    }

    /**
     * Setter method to set the reinforcement armies
     * @param p_reinforcement Number of reinforcement armies
     */
    public void setReinforcement(Integer p_reinforcement){
        this.d_reinforcement=p_reinforcement;
    }
    /**
     * Getter method to get the reinforcement armies
     * @return Number of reinforcement armies
     */
    public Integer getReinforcements(){
        return this.d_reinforcement;
    }
    /**
     * This method takes the input for the deployment order
     * @throws IOException Exception
     */
    public void issue_order() throws IOException{
        BufferedReader sc= new BufferedReader(new InputStreamReader(System.in));
        PlayerHelper l_playerHelper = new PlayerHelper();
		System.out.println("\nEnter command to deploy reinforcement armies on the map for player : " + this.getPlayerName());
        String l_enteredCommand=sc.readLine();
        Command l_command=new Command(l_enteredCommand);

        if (l_command.getFirstCommand().equalsIgnoreCase("deploy") && l_enteredCommand.split(" ").length == 3) {
        	l_playerHelper.createDeployOrder(l_enteredCommand, this);
		} else {
			System.out.println("Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)");;
		}
    }
    /**
     * This method gives the next order that is to be executed
     * @return Next order from the issue order list
     */
    public Order next_order(){
        
        if(CollectionUtils.isEmpty(this.d_issuedOrders)){
            return null;
        }
        Order l_order = this.d_issuedOrders.get(0);
		this.d_issuedOrders.remove(l_order);
		return l_order;
    }

}
