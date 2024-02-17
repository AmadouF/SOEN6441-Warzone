package Helpers;

import java.util.*;
import java.util.stream.Collectors;

import Exceptions.InvalidMap;

import java.io.*;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Models.Player;
import Utils.CommonUtil;


public class MapHelper {
    public Map load(GameState p_gameState,String p_mapFileName){
        Map l_map=new Map();
        String l_mapFilePath = CommonUtil.getMapFilePath(p_mapFileName);
		List<String> l_fileData = new ArrayList<>();
        
		BufferedReader l_reader;
		try {
			l_reader = new BufferedReader(new FileReader(l_mapFilePath));
			l_fileData = l_reader.lines().collect(Collectors.toList());
			l_reader.close();
		} catch (IOException l_exception) {
			System.out.println("File not Found!");
		}
		

        if(l_fileData!=null && !l_fileData.isEmpty()){
            int l_continentsDataStart=l_fileData.indexOf("[continents]")+1;
            int l_continentsDataEnd=l_fileData.indexOf("[countries]")-1;
            List<String> l_continents=l_fileData.subList(l_continentsDataStart, l_continentsDataEnd);
            List<Continent> l_continentsObj=createContinentObjects(l_continents);
            l_map.setContinents(l_continentsObj);

            int l_countriesDataStart=l_fileData.indexOf("[countries]")+1;
            int l_countriesDataEnd=l_fileData.indexOf("[borders]")-1;
            List<String> l_countries=l_fileData.subList(l_countriesDataStart, l_countriesDataEnd);
            List<Country> l_countriesObj=createCountryObjects(l_countries);
            

            int l_bordersDataStart=l_fileData.indexOf("[borders]")+1;
            int l_bordersDataEnd=l_fileData.size();
            List<String> l_borders=l_fileData.subList(l_bordersDataStart, l_bordersDataEnd);
            l_countriesObj=setConnectivity(l_countriesObj,l_borders);

            l_map.setCountries(l_countriesObj);
            p_gameState.setMap(l_map);

        }
        
        return l_map;
    }

    // public List<Object> convertStringToObjects(List<String> p_list,String objectType){
    //     List<Object> l_objectList=new ArrayList<Object>();
    //     if(objectType.equals("continents")){
    //         // List<Continent> l_continents=new ArrayList<Continent>();
    //         int l_continentId = 1;
    //         for (String cont : p_list) {
    //             String[] l_metaData = cont.split(" ");
    //             l_objectList.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
    //             l_continentId++;
    //         }
            
    //     }
    //     if(objectType.equals("countries")){
            
    //         for (String country : p_list) {
    //             String[] l_metaDataCountries = country.split(" ");
    //             l_objectList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), Integer.parseInt(l_metaDataCountries[2]), l_metaDataCountries[1]));
    //         }

            
    //     }
    //     return l_objectList;
    // }

    public List<Country> createCountryObjects(List<String> p_list){
        List<Country> l_objectList=new ArrayList<Country>();
        // if(objectType.equals("continents")){
            // List<Continent> l_continents=new ArrayList<Continent>();
            // LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
            for (String country : p_list) {
                String[] l_metaDataCountries = country.split(" ");
                l_objectList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), Integer.parseInt(l_metaDataCountries[2]), l_metaDataCountries[1]));
            }
            
        // }
        return l_objectList;
    }

    public List<Continent> createContinentObjects(List<String> p_list){
        List<Continent> l_objectList=new ArrayList<Continent>();
        // if(objectType.equals("countries")){
            
            
            int l_continentId = 1;
            for (String cont : p_list) {
                String[] l_metaData = cont.split(" ");
                l_objectList.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
                l_continentId++;
            }

            
        // }
        return l_objectList;
    }

    public List<Country> setConnectivity(List<Country> p_countries,List<String> p_borders){
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

		for (String l_border : p_borders) {
			if (l_border!=null && !l_border.isEmpty()) {
				ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
				String[] l_splitString = l_border.split(" ");
				for (int i = 1; i < l_splitString.length ; i++) {
					l_neighbours.add(Integer.parseInt(l_splitString[i]));

				}
				l_countryNeighbors.put(Integer.parseInt(l_splitString[0]), l_neighbours);
			}
		}
		for (Country c : p_countries) {
			List<Integer> l_adjacentCountries = l_countryNeighbors.get(c.getD_id());
			c.setD_adjacentCountryIds(l_adjacentCountries);
		}



        return p_countries;
    }

    public void edit(GameState p_state, String p_filePath) throws IOException{
        String l_mapFilePath = CommonUtil.getMapFilePath(p_filePath);
        File l_fileToBeEdited = new File(l_mapFilePath);

		if (l_fileToBeEdited.createNewFile()) {
			System.out.println("File has been created.");
			Map l_map = new Map();
			l_map.setMapFile(p_filePath);
			p_state.setMap(l_map);
		} else {
			System.out.println("File already exists.");
			this.load(p_state, p_filePath);
			if (null == p_state.getMap()) {
				p_state.setMap(new Map());
			}
			p_state.getMap().setMapFile(p_filePath);
		}
    }


    public void editContinent(GameState p_state, String p_argument, String p_operation) throws IOException, InvalidMap {
		String l_mapFileName = p_state.getMap().getMapFile();
		


        Map l_currentMap=null;
        if(p_state.getMap().getContinentsList()==null && p_state.getMap().getCountriesList()==null){
            l_currentMap=this.load(p_state,l_mapFileName);
        }else{
            l_currentMap=p_state.getMap();
        }
                
		if(!CommonUtil.isNull(l_currentMap)) {

            if(p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                String l_newContinent=p_argument.split(" ")[0];
                int l_continentBonus=Integer.parseInt(p_argument.split(" ")[1]);
                l_currentMap.addContinent(l_newContinent, l_continentBonus);
            }
			
            if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1){
                l_currentMap.removeContinent(p_argument.split(" ")[0]);
            }

			p_state.setMap(l_currentMap);
			p_state.getMap().setMapFile(l_mapFileName);
		}
	}

    public void editCountry(GameState p_state, String p_operation, String p_argument) throws InvalidMap{
		String l_mapFileName= p_state.getMap().getMapFile();


		Map l_currentMap=null;
        if(p_state.getMap().getContinentsList()==null && p_state.getMap().getCountriesList()==null){
            l_currentMap=this.load(p_state,l_mapFileName);
        }else{
            l_currentMap=p_state.getMap();
        }

		if(!CommonUtil.isNull(l_currentMap)) {

            if(p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                String l_newCountry=p_argument.split(" ")[0];
                
                String l_continentName=p_argument.trim().split(" ")[1];
                l_currentMap.addCountry(l_newCountry, l_continentName);
            }
			
            if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1){
                l_currentMap.removeCountry(p_argument.split(" ")[0]);
            }

			p_state.setMap(l_currentMap);
			p_state.getMap().setMapFile(l_mapFileName);
		}
	}

    public void editNeighbour(GameState p_state, String p_operation, String p_argument) throws InvalidMap{
		String l_mapFileName= p_state.getMap().getMapFile();
		
        Map l_currentMap=null;
        if(p_state.getMap().getContinentsList()==null && p_state.getMap().getCountriesList()==null){
            l_currentMap=this.load(p_state,l_mapFileName);
        }else{
            l_currentMap=p_state.getMap();
        }


		if(!CommonUtil.isNull(l_currentMap)) {
			
            
            if(p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                String l_country=p_argument.split(" ")[0];
                
                String l_neighbour=p_argument.trim().split(" ")[1];
                
                l_currentMap.addNeighbour(l_country, l_neighbour);
            }
			
            if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1){
                String l_country=p_argument.split(" ")[0];
                
                String l_neighbour=p_argument.trim().split(" ")[1];
                
                l_currentMap.removeNeighbour(l_country, l_neighbour);
            }

			p_state.setMap(l_currentMap);
			p_state.getMap().setMapFile(l_mapFileName);
            
            
		}
	}


    public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
		for (Country c : p_countries) {
			for (Continent cont : p_continents) {
				if (cont.getD_continentID().equals(c.getD_continentId())) {
					cont.addCountry(c);
				}
			}
		}
		return p_continents;
	}


}
