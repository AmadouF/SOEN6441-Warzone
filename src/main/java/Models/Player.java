package Models;

import java.util.*;

import Utils.Command;

import java.io.*;


public class Player {
    
    private String d_playerName;
    private List<Country> d_ownedCountries;
    private List<Continent> d_ownedContinents;
    private int d_reinforcement;
    private List<Order> d_issuedOrders;

    public Player(String p_name){
        this.d_playerName=p_name;
        this.d_reinforcement=0;
        this.d_ownedCountries=new ArrayList<>();
        this.d_ownedContinents=new ArrayList<>();
        this.d_issuedOrders=new ArrayList<>();
    }

    public void setPlayerName(String p_name){
        this.d_playerName=p_name;
    }

    public String getPlayerName(){
        return this.d_playerName;
    }

    public void setOwnedCountries(List<Country> p_ownedCountries){
        this.d_ownedCountries=p_ownedCountries;
    }

    public List<Country> getOwnedCountries(){
        return this.d_ownedCountries;
    }

    public void setOwnedContinents(List<Continent> p_ownedContinents){
        this.d_ownedContinents=p_ownedContinents;
    }

    public List<Continent> getOwnedContinents(){
        return this.d_ownedContinents;
    }

    public void setIssuedOrders(List<Order> p_issuedOrders){
        this.d_issuedOrders=p_issuedOrders;
    }

    public List<Order> getIssuedOrders(){
        return this.d_issuedOrders;
    }

    public void setReinforcement(int p_reinforcement){
        this.d_reinforcement=p_reinforcement;
    }

    public int getReinforcements(){
        return this.d_reinforcement;
    }

    public void issue_order(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter Deployment order: ");
        String l_enteredCommand=sc.nextLine();
        Command l_command=new Command(l_enteredCommand);
        
        // if(l_command.getRootCommand().toLowerCase().equals("deploy") && l_enteredCommand.split(" ").length==3){
        //     PlayerService l_playerService=new PlayerService();
        //     l_playerService.processDeployOrder(l_enteredCommand,this);
        // }
    }

    public Order next_order(){
        Order l_order = this.d_issuedOrders.get(0);
		this.d_issuedOrders.remove(l_order);
		return l_order;
    }

}
