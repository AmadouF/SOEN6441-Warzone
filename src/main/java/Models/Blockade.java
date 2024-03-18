package Models;

/**
 * This class handles the implementation of Blockade card.
 */
public class Blockade implements Card{
    /**
     * Player object
     */
    Player d_player;
    /**
     * Target country
     */
    String d_trgCountry;
    /**
     * Log
     */
    String d_log;

    /**
     * Constructor to initialize the blockade object
     * @param p_player Player
     * @param p_trgCountry Target country
     */
    public Blockade(Player p_player, String p_trgCountry){
        this.d_player=p_player;
        this.d_trgCountry=p_trgCountry;
    }

    /**
     * Handles the execution of blockade order
     * @param p_gameState Current gamestate
     */
    @Override
    public void execute(GameState p_gameState){
        if(isValid(p_gameState)){
            Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);
            Integer l_trgArmies=l_trgCountry.getD_army();
            if(l_trgArmies==0) l_trgArmies=1;
            l_trgCountry.setD_army(l_trgArmies*3);
            d_player.getOwnedCountries().remove(l_trgCountry);
            Player l_player = p_gameState.getD_players().stream().filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);
            if(l_player!=null){
                l_player.getOwnedCountries().add(l_trgCountry);
            }
            d_player.removeCard("blockade");
            this.setD_orderExecutionLog("\nPlayer : " + this.d_player.getPlayerName()
                    + " is executing defensive blockade on Country :  " + l_trgCountry.getD_name()
                    + " with armies :  " + l_trgCountry.getD_army(), "default");
            p_gameState.addLogMessage(orderExecutionLog(),"effect");
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
     * @param p_typeOfLog Log type
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
    @Override
    public boolean isValid(GameState p_gameState){
        Country l_country = d_player.getOwnedCountries().stream().filter(l_cnt -> l_cnt.getD_name().equalsIgnoreCase(this.d_trgCountry)).findFirst().orElse(null);
        if(l_country==null){
            this.setD_orderExecutionLog("Blockade is not executed since Target country : "
                    + this.d_trgCountry + " given in blockade command does not owned to the player : "
                    + d_player.getPlayerName()
                    + " The card will have no affect and you don't get the card back.", "error");
            p_gameState.addLogMessage(orderExecutionLog(),"effect");
            return  false;
        }

        return true;
    }
    /**
     * Checks whether the order is valid or not.
     * @param p_gameState Current game state
     * @return True/False valid or invalid order
     */
    public Boolean checkValidOrder(GameState p_gameState){
        Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);
        if(l_trgCountry==null){
            this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
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
        return "blockade";
    }
    /**
     * Prints the order.
     */
    public void printOrder(){
        String order="Order: Blockade"+System.lineSeparator()+"Player: "+this.d_player.getPlayerName()+System.lineSeparator()+"Defensive blockade on "+this.d_trgCountry;
        System.out.println(order);
        this.d_log=order;

    }
}
