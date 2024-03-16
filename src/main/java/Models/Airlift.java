package Models;

public class Airlift {
    String d_srcCountry;
    String d_trgCountry;
    Player d_player;
    Integer d_noOfArmies;
    //todo log
    public Airlift(String p_srcCountry,String p_trgCountry, Player p_player, Integer p_noOfArmies){
        this.d_srcCountry=p_srcCountry;
        this.d_trgCountry=p_trgCountry;
        this.d_player=p_player;
        this.d_noOfArmies=p_noOfArmies;
    }
    public void execute(GameState p_gameState){
        if(validGameState(p_gameState)){
            Country l_srcCountry=p_gameState.getD_map().getCountryByName(this.d_srcCountry);
            Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);
            Integer l_srcArmies=l_srcCountry.getD_army()-this.d_noOfArmies;
            Integer l_trgArmies=l_trgCountry.getD_army()+this.d_noOfArmies;
            l_srcCountry.setD_army(l_srcArmies);
            l_trgCountry.setD_army(l_trgArmies);
            // remove card from player
        }
        //todo valis and invalid gamestate

    }

    public boolean validGameState(GameState p_gameState){
        Country l_srcCountry=null;
        l_srcCountry = d_player.getOwnedCountries().stream().filter(l_country -> l_country.getD_name().equalsIgnoreCase(this.d_srcCountry)).findFirst().orElse(null);

        if(l_srcCountry==null)
            return false; //todo log invalid source
        Country l_trgCountry=null;
        l_trgCountry = d_player.getOwnedCountries().stream().filter(l_country -> l_country.getD_name().equalsIgnoreCase(this.d_trgCountry)).findFirst().orElse(null);

        if(l_trgCountry==null)
            return false; //todo log invalid target
        if(this.d_noOfArmies>l_srcCountry.getD_army())
            return false; // todo invalid armies
        return true;
    }

    public void printOrder(){
        String order="Order: Airlift"+System.lineSeparator()+"Player: "+this.d_player.getPlayerName()+System.lineSeparator()+"Move "+this.d_noOfArmies+" armies from "+this.d_srcCountry+" to "+this.d_trgCountry;
        System.out.println(order);
        //todo log the order
    }
    //todo setter getter for execution log

    public boolean validOrder(GameState p_gameState){
        Country l_srcCountry=p_gameState.getD_map().getCountryByName(this.d_srcCountry);
        Country l_trgCountry=p_gameState.getD_map().getCountryByName(this.d_trgCountry);

        if(l_srcCountry==null || l_trgCountry==null) return false; //todo invalid order
        return true;
    }

    public String getName(){
        return "airlift";
    }
}
