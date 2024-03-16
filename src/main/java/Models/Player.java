package Models;

import java.util.*;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
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
    boolean d_moreOrders;
    boolean d_oneCard=false;
    String d_log;
    List<String> d_ownedCards=new ArrayList<>();
    List<Player> d_negotiatedPlayers=new ArrayList<Player>();

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
        this.d_moreOrders=true;
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

    public void addPlayerNegotiation(Player p_player){
        this.d_negotiatedPlayers.add(p_player);
    }
    public boolean getMoreOrders(){
        return this.d_moreOrders;
    }
    public void setMoreOrders(boolean p_moreOrders){
        this.d_moreOrders=p_moreOrders;
    }
    public List<String> getOwnedCards(){
        return this.d_ownedCards;
    }
    public void setOneCard(boolean p_card){
        this.d_oneCard=p_card;
    }
//    public void issue_order() throws IOException{
//        BufferedReader sc= new BufferedReader(new InputStreamReader(System.in));
//        PlayerHelper l_playerHelper = new PlayerHelper();
//		System.out.println("\nEnter command to deploy reinforcement armies on the map for player : " + this.getPlayerName());
//        String l_enteredCommand=sc.readLine();
//        Command l_command=new Command(l_enteredCommand);
//
//        if (l_command.getFirstCommand().equalsIgnoreCase("deploy") && l_enteredCommand.split(" ").length == 3) {
//        	l_playerHelper.createDeployOrder(l_enteredCommand, this);
//		} else {
//			System.out.println("Invalid command. Kindly provide command in Format of : deploy countryID <CountryName> <num> (until all reinforcements have been placed)");;
//		}
//    }
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


    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<String>();
        if(d_ownedCountries!=null){
            for (Country c : d_ownedCountries) {
                l_countryNames.add(c.getD_name());
            }
            return l_countryNames;
        }
        return null;
    }

    public List<String> getContinentNames() {
        List<String> l_continentNames = new ArrayList<String>();
        if (d_ownedContinents != null) {
            for (Continent c : d_ownedContinents) {
                l_continentNames.add(c.getD_name());
            }
            return l_continentNames;
        }
        return null;
    }

    public void setD_playerLog(String p_playerLog, String p_typeLog) {
        this.d_log = p_playerLog;
        if(p_typeLog.equals("error")) System.err.println(p_playerLog);
        else if(p_typeLog.equals("log")) System.out.println(p_playerLog);
    }
    public String getLog(){
        return this.d_log;
    }
    public void checkForMoreOrders() throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("\nDo you still want to give order for player : " + this.getPlayerName()
                + " in next turn ? \nPress Y for Yes or N for No");
        String l_moreOrders = sc.nextLine();
        if (l_moreOrders.equalsIgnoreCase("Y") || l_moreOrders.equalsIgnoreCase("N")) {
            this.setMoreOrders(l_moreOrders.equalsIgnoreCase("Y") ? true : false);
        } else {
            System.err.println("Invalid Input Passed.");
            this.checkForMoreOrders();
        }
    }
    /**
     * creates the deploy order on the commands entered by the player.
     *
     * @param p_command command entered by the user
     */

    public void createDeployOrder(String p_command){
        String l_trgCountry;
        String l_armies;
        try{
            l_trgCountry=p_command.split(" ")[1];
            l_armies=p_command.split(" ")[2];
            if (isValidArmies(this, l_armies)) {
                this.setD_playerLog(
                        "Player does not have enough reinforcements to deploy!! Aborted.", "error");
            } else {
                this.d_issuedOrders.add(new Deploy(this, l_trgCountry, Integer.parseInt(l_armies)));
                Integer l_reinforcement = this.getReinforcements() - Integer.parseInt(l_armies);
                this.setReinforcement(l_reinforcement);
                this.setD_playerLog("Deploy order has been added to queue for execution. For player: " + this.getPlayerName(), "log");

            }
        }catch(Exception e){
            this.setD_playerLog("Invalid deploy order entered.", "error");
        }
    }

    /**
     * Used to test number of armies entered in deploy command to check that player
     * cannot deploy more armies than they have.
     *
     * @param p_player player to create deploy order
     * @param p_noOfArmies number of armies to deploy
     *
     * @return boolean to validate armies to deploy
     */
    public boolean isValidArmies(Player p_player, String p_noOfArmies) {
        return p_player.getReinforcements() >= Integer.parseInt(p_noOfArmies);
    }
    //need to implement.
    public void issue_order(IssueOrderPhase p_issueOrderPhase) throws InvalidCommand, IOException, InvalidMap {
        p_issueOrderPhase.askForOrder(this);
    }

    public void createAdvanceOrder(String p_commandEntered, GameState p_gameState) {
        try {
            if (p_commandEntered.split(" ").length == 4) {
                String l_srcCountry = p_commandEntered.split(" ")[1];
                String l_trgCountry = p_commandEntered.split(" ")[2];
                String l_armies = p_commandEntered.split(" ")[3];
                if (this.checkCountryExists(l_srcCountry, p_gameState) && this.checkCountryExists(l_trgCountry, p_gameState)
                        && !checkZeroArmies(l_armies) && checkAdjacency(p_gameState, l_srcCountry, l_trgCountry)) {
                    this.d_issuedOrders.add(new Advance(this, l_srcCountry, l_trgCountry, Integer.parseInt(l_armies)));
                    this.setD_playerLog("Advance order has been added to queue for "+this.d_playerName, "log");
                }
            } else {
                this.setD_playerLog("Invalid Arguments Passed For Advance Order", "error");
            }

        } catch (Exception l_e) {
            this.setD_playerLog("Invalid Advance Order Given", "error");
        }
    }

    private Boolean checkCountryExists(String p_country, GameState p_gameState) {
        if (p_gameState.getD_map().getCountryByName(p_country) == null) {
            this.setD_playerLog("Country doesn't exists in map.", "error");
            return false;
        }
        return true;
    }

    private Boolean checkZeroArmies(String p_armies) {
        if (Integer.parseInt(p_armies) == 0) {
            this.setD_playerLog("No armies to advance.", "error");
            return true;
        }
        return false;
    }

    @SuppressWarnings("unlikely-arg-type")
    public boolean checkAdjacency(GameState p_gameState, String p_srcCountry, String p_trgCountry) {
        Country l_srcCountry = p_gameState.getD_map().getCountryByName(p_srcCountry);
        Country l_trgCountry = p_gameState.getD_map().getCountryByName(p_trgCountry);
        Integer l_targetCountryId = l_srcCountry.getD_adjacentCountryIds().stream().filter(l_adjCountry -> l_adjCountry == l_trgCountry.getD_id()).findFirst().orElse(null);
        if (l_targetCountryId == null) {
            this.setD_playerLog("Advance order cant be issued since target country : " + p_trgCountry + " is not adjacent to source country : " + p_srcCountry, "error");
            return false;
        }
        return true;
    }

    public void assignCard() {
        if (!d_oneCard) {
            List<String> cards = Arrays.asList("bomb", "blockade", "airlift", "negotiate");
            int sz = cards.size();

            Random l_random = new Random();
            this.d_ownedCards.add(cards.get(l_random.nextInt(sz)));
            this.setD_playerLog("Player: "+ this.d_playerName+ " received " + this.d_ownedCards.get(this.d_ownedCards.size()-1)+" card", "log");
            this.setOneCard(true);
        }else{
            this.setD_playerLog("Player: "+this.d_playerName+ " has already earned the card.", "error");
        }
    }

    public void removeCard(String p_card){
        this.d_ownedCards.remove(p_card);
    }

    public boolean negotiationValidation(String p_trgCountry){

        for(Player p: d_negotiatedPlayers){
            if (p.getCountryNames().contains(p_trgCountry))
                return false;
        }
        return true;
    }

    public void clearNegotiatedPlayers(){
        d_negotiatedPlayers.clear();
    }

    public boolean validArguments(String p_command){
        if(p_command.split(" ")[0].equalsIgnoreCase("airlift")) return p_command.split(" ").length == 4;
        else if (p_command.split(" ")[0].equalsIgnoreCase("blockade") || p_command.split(" ")[0].equalsIgnoreCase("bomb") || p_command.split(" ")[0].equalsIgnoreCase("negotiate")) {
            return p_command.split(" ").length == 2;
        } else return false;

    }

    public void handleCardCommands(String p_command, GameState p_gameState) {
        if (validArguments(p_command)) {
            switch (p_command.split(" ")[0]) {
                case "airlift":
                    Card l_order = new Airlift(p_command.split(" ")[1], p_command.split(" ")[2],this,
                            Integer.parseInt(p_command.split(" ")[3]));
                    if (l_order.checkValidOrder(p_gameState)) {
                        this.d_issuedOrders.add(l_order);
                        this.setD_playerLog("Card Command Added to Queue.", "log");
                        p_gameState.addLogMessage(getLog(), "effect");
                    }
                    break;
                case "blockade":
                    Card l_blockadeOrder = new Blockade(this, p_command.split(" ")[1]);
                    if (l_blockadeOrder.checkValidOrder(p_gameState)) {
                        this.d_issuedOrders.add(l_blockadeOrder);
                        this.setD_playerLog("Card Command Added to Queue.", "log");
                        p_gameState.addLogMessage(getLog(), "effect");
                    }
                    break;
                case "bomb":
                    Card l_bombOrder = new Bomb(this, p_command.split(" ")[1]);
                    if (l_bombOrder.checkValidOrder(p_gameState)) {
                        this.d_issuedOrders.add(l_bombOrder);
                        this.setD_playerLog("Card Command Added to Queue.", "log");
                        p_gameState.addLogMessage(getLog(), "effect");
                    }
                    break;
                case "negotiate":
                    Card l_negotiateOrder = new Diplomacy(this,p_command.split(" ")[1]);
                    if (l_negotiateOrder.checkValidOrder(p_gameState)) {
                        this.d_issuedOrders.add(l_negotiateOrder);
                        this.setD_playerLog("Card Command Added to Queue.", "log");
                        p_gameState.addLogMessage(getLog(), "effect");
                    }
                    break;
                default:
                    this.setD_playerLog("Invalid Command!", "error");
                    p_gameState.addLogMessage(getLog(), "effect");
                    break;
            }
        } else{
            this.setD_playerLog("Invalid Card Command Passed! Check Arguments!", "error");
        }
    }


}
