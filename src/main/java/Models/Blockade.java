package Models;

public class Blockade implements Card{
    Player d_player;
    String d_trgCountry;
    String d_log;

    public Blockade(Player p_player, String p_trgCountry){
        this.d_player=p_player;
        this.d_trgCountry=p_trgCountry;
    }
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
            this.setD_orderExecutionLog("\nPlayer : " + this.d_player.getPlayerName()
                    + " is executing defensive blockade on Country :  " + l_trgCountry.getD_name()
                    + " with armies :  " + l_trgCountry.getD_army(), "default");
        }


    }



    @Override
    public String orderExecutionLog() {
        return this.d_log;
    }

    @Override
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog) {
        this.d_log=p_orderExecutionLog;
        if (p_typeOfLog.equals("error")) System.err.println(p_orderExecutionLog);
        else System.out.println(p_orderExecutionLog);
    }



    @Override
    public boolean isValid(GameState p_gameState){
        Country l_country = d_player.getOwnedCountries().stream().filter(l_cnt -> l_cnt.getD_name().equalsIgnoreCase(this.d_trgCountry)).findFirst().orElse(null);
        if(l_country==null){
            this.setD_orderExecutionLog("Blockade is not executed since Target country : "
                    + this.d_trgCountry + " given in blockade command does not owned to the player : "
                    + d_player.getPlayerName()
                    + " The card will have no affect and you don't get the card back.", "error");
            return  false;
        }
        //todo update log
        return true;
    }
    public Boolean checkValidOrder(GameState p_gameState){
        Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);
        if(l_trgCountry==null){
            this.setD_orderExecutionLog("Invalid Target Country! Doesn't exist on the map!", "error");
            return false;
        }
        //todo update log
        return true;
    }
    @Override
    public String getOrderName(){
        return "blockade";
    }

    public void printOrder(){
        String order="Order: Blockade"+System.lineSeparator()+"Player: "+this.d_player.getPlayerName()+System.lineSeparator()+"Defensive blockade on "+this.d_trgCountry;
        System.out.println(order);
        this.d_log=order;
        //todo log the order
    }
}
