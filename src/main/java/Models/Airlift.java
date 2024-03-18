package Models;

/**
 * This class handles the implementation of Airlift card.
 */
public class Airlift implements Card{
    /**
     * Source country
     */
    String d_srcCountry;
    /**
     * Target Country
     */
    String d_trgCountry;
    /**
     * Player object
     */
    Player d_player;
    /**
     * Number of armies to be airlifted
     */
    Integer d_noOfArmies;
    /**
     * Log
     */
    String d_log;

    /**
     * Constructor to initialize Airlift object
     * @param p_srcCountry Source Country
     * @param p_trgCountry target Country
     * @param p_player Player
     * @param p_noOfArmies Number of armies
     */
    public Airlift(String p_srcCountry,String p_trgCountry, Player p_player, Integer p_noOfArmies){
        this.d_srcCountry=p_srcCountry;
        this.d_trgCountry=p_trgCountry;
        this.d_player=p_player;
        this.d_noOfArmies=p_noOfArmies;
    }

    /**
     * Handles the execution of command.
     * @param p_gameState Current game state
     */
    @Override
    public void execute(GameState p_gameState){
        if(isValid(p_gameState)){
            Country l_srcCountry=p_gameState.getD_map().getCountryByName(this.d_srcCountry);
            Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);
            Integer l_srcArmies=l_srcCountry.getD_army()-this.d_noOfArmies;
            Integer l_trgArmies=l_trgCountry.getD_army()+this.d_noOfArmies;
            l_srcCountry.setD_army(l_srcArmies);
            l_trgCountry.setD_army(l_trgArmies);
            d_player.removeCard("airlift");

            this.setD_orderExecutionLog("Airlift Operation from "+ d_srcCountry+ " to "+d_trgCountry+" successful!", "default");
            p_gameState.addLogMessage(d_log,"effect");
        }else{
            this.setD_orderExecutionLog("Incomplete Execution of given Airlift Command!", "error");
            p_gameState.addLogMessage(d_log,"effect");
        }


    }


    /**
     * Getter method to get the log.
     * @return Log
     */
    @Override
    public String orderExecutionLog() {
        return this.d_log;
    }

    /**
     * Setter method to set the log.
     * @param p_orderExecutionLog Log
     * @param p_typeOfLog Log type.
     */
    @Override
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog) {
        this.d_log=p_orderExecutionLog;
        if (p_typeOfLog.equals("error")) System.err.println(p_orderExecutionLog);
        else System.out.println(p_orderExecutionLog);

    }


    /**
     * Checks whether the game state is valid or not.
     * @param p_gameState Current game state
     * @return True/false valid or invalid game state
     */
    public boolean isValid(GameState p_gameState){
        Country l_srcCountry=null;
        l_srcCountry = d_player.getOwnedCountries().stream().filter(l_country -> l_country.getD_name().equalsIgnoreCase(this.d_srcCountry)).findFirst().orElse(null);

        if(l_srcCountry==null){
            this.setD_orderExecutionLog(
                    "Airlift order is not executed since Source country : " + this.d_srcCountry + " given in card order does not belongs to the player : " + d_player.getPlayerName(),
                    "error");
            p_gameState.addLogMessage(orderExecutionLog(),"effect");
            return false;
        }

        Country l_trgCountry=null;
        l_trgCountry = d_player.getOwnedCountries().stream().filter(l_country -> l_country.getD_name().equalsIgnoreCase(this.d_trgCountry)).findFirst().orElse(null);

        if(l_trgCountry==null){
            this.setD_orderExecutionLog(
                    "Airlift order is not executed since Target country : " + this.d_trgCountry + " given in card order does not belongs to the player : " + d_player.getPlayerName(),
                    "error");
            p_gameState.addLogMessage(orderExecutionLog(),"effect");
            return false;
        }

        if(this.d_noOfArmies>l_srcCountry.getD_army()){
            this.setD_orderExecutionLog("Airlift order is not executed as armies given in card order exceeds armies of source country : " + this.d_srcCountry, "error");
            p_gameState.addLogMessage(orderExecutionLog(),"effect");
            return false;
        }

        return true;
    }

    /**
     * Prints the order.
     */
    public void printOrder(){
        String order="Order: Airlift"+System.lineSeparator()+"Player: "+this.d_player.getPlayerName()+System.lineSeparator()+"Move "+this.d_noOfArmies+" armies from "+this.d_srcCountry+" to "+this.d_trgCountry;
        System.out.println(order);
        this.d_log=order;


    }

    /**
     * Checks whether the order is valid or not.
     * @param p_gameState Current game state
     * @return True/False valid or invalid order
     */
    @Override
    public Boolean checkValidOrder(GameState p_gameState){
        Country l_srcCountry=p_gameState.getD_map().getCountryByName(this.d_srcCountry);
        Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);

        if(l_srcCountry==null || l_trgCountry==null){
            this.setD_orderExecutionLog("Invalid Source or Target Country! Doesn't exist on the map!", "error");
            p_gameState.addLogMessage(orderExecutionLog(),"effect");
            return false;
        }
        return true;
    }

    /**
     * Returns the order name.
     * @return Order name
     */
    @Override
    public String getOrderName(){
        return "airlift";
    }
}
