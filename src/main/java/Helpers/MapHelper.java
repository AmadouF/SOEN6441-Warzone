package Helpers;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import Exceptions.InvalidMap;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;

/**
 * This class processes the map functions like loading, reading, editing and saving the map.
 */
public class MapHelper {
    /**
     * This method loads and parses the map file data
     * @param p_gameState current game state
     * @param p_mapFileName map file name
     * @return Map object created after loading the map
     */
    public Map load(GameState p_gameState,String p_mapFileName){
        Map l_map=new Map();
        String l_mapFilePath = getMapFilePath(p_mapFileName);
		List<String> l_fileData = new ArrayList<>();
        
		BufferedReader l_reader;
		try {
			l_reader = new BufferedReader(new FileReader(l_mapFilePath));
			l_fileData = l_reader.lines().collect(Collectors.toList());
			l_reader.close();
		} catch (IOException l_exception) {
			System.out.println("File not Found!");
		}
		
        // parses the continents , countries and borders on the basis of the labels mentioned in the file.
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
            l_map.setMapFile(p_mapFileName);
            p_gameState.setD_map(l_map);

        }
        
        return l_map;
    }

    
    /**
     * Creates the Country objects for each country data mentioned in the map file.
     * @param p_list List of information of the country
     * @return List of the Country objects on the map file
     */
    public List<Country> createCountryObjects(List<String> p_list){
        List<Country> l_objectList=new ArrayList<Country>();
        
        for (String country : p_list) {
            String[] l_metaDataCountries = country.split(" ");
            l_objectList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), Integer.parseInt(l_metaDataCountries[2]), l_metaDataCountries[1]));
        }
            
        return l_objectList;
    }
    /**
     * Creates the Continent objects for each continent data mentioned in the map file.
     * @param p_list List of information of the continent
     * @return List of the Continent objects on the map file
     */
    public List<Continent> createContinentObjects(List<String> p_list){
        List<Continent> l_objectList=new ArrayList<Continent>();

        int l_continentId = 1;
        for (String cont : p_list) {
            String[] l_metaData = cont.split(" ");
            l_objectList.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentId++;
        }

        return l_objectList;
    }

    
    /**
     * This parses the borders data and sets the connectivity between the countries
     * @param p_countries List of countries
     * @param p_borders List of information of the borders
     * @return List of countries after setting up the connectivity
     */
    public List<Country> setConnectivity(List<Country> p_countries,List<String> p_borders){
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
        // parses the border data and sets the neighbors
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
        //assigns the neighboring countries list for the appropriate country object
		for (Country c : p_countries) {
			List<Integer> l_adjacentCountries = l_countryNeighbors.get(c.getD_id());
			c.setD_adjacentCountryIds(l_adjacentCountries);
		}



        return p_countries;
    }
    /**
     * This method handles the editmap operation on the map.
     * @param p_state Current state of the map
     * @param p_filePath Path of the map file.
     * @throws IOException exception
     */
    public void editMap(GameState p_state, String p_filePath) throws IOException{
        String l_mapFilePath = getMapFilePath(p_filePath);
        File l_fileToBeEdited = new File(l_mapFilePath);

        if (l_fileToBeEdited.createNewFile()) {
          System.out.println("File has been created.");
          Map l_map = new Map();
          l_map.setMapFile(p_filePath);
          p_state.setD_map(l_map);
        } else {
          System.out.println("File already exists.");
          this.load(p_state, p_filePath);
          if (null == p_state.getD_map()) {
            p_state.setD_map(new Map());
          }
          p_state.getD_map().setMapFile(p_filePath);
        }
    }

    /**
     * This method handles the edit continent operation on the map.
     * @param p_state Current state of the game.
     * @param p_argument Arguments that are passed in the command
     * @param p_operation Specifies the add or remove operation that has to be done
     * @throws IOException Input Output exception
     * @throws InvalidMap invalid map exception
     */
    public void editContinent(GameState p_state, String p_argument, String p_operation) throws IOException, InvalidMap {
		String l_mapFileName = p_state.getD_map().getMapFile();
		


        Map l_currentMap=null;
        if(p_state.getD_map().getContinentsList()==null && p_state.getD_map().getCountriesList()==null){
            l_currentMap=this.load(p_state,l_mapFileName);
        }else{
            l_currentMap=p_state.getD_map();
        }
        // Segregates the add and remove operation
		if(l_currentMap!=null) {

            if(p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                String l_newContinent=p_argument.split(" ")[0];
                int l_continentBonus=Integer.parseInt(p_argument.split(" ")[1]);
                l_currentMap.addContinent(l_newContinent, l_continentBonus);
            }
			
            else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1){
                l_currentMap.removeContinent(p_argument.split(" ")[0]);
            }else{
                System.out.println("Continent couldn't be added/removed. Changes are not made");
            }

			p_state.setD_map(l_currentMap);
			p_state.getD_map().setMapFile(l_mapFileName);
		}
	}
    /**
     * This method handles the edit country operation on the map.
     * @param p_state Current state of the game.
     * @param p_operation Specifies the add or remove operation that has to be done
     * @param p_argument Arguments that are passed in the command
     * @throws InvalidMap invalid map exception
     */
    public void editCountry(GameState p_state, String p_operation, String p_argument) throws InvalidMap{
		String l_mapFileName= p_state.getD_map().getMapFile();


		Map l_currentMap=null;
        if(p_state.getD_map().getContinentsList()==null && p_state.getD_map().getCountriesList()==null){
            l_currentMap=this.load(p_state,l_mapFileName);
        }else{
            l_currentMap=p_state.getD_map();
        }
        // Segregates the add and remove operation
		if(l_currentMap!=null) {

            if(p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                String l_newCountry=p_argument.split(" ")[0];
                
                String l_continentName=p_argument.trim().split(" ")[1];
                l_currentMap.addCountry(l_newCountry, l_continentName);
            }
			
            else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1){
                l_currentMap.removeCountry(p_argument.split(" ")[0]);
            }
            else{
                System.out.println("Couldn't Save your changes");
            }
			p_state.setD_map(l_currentMap);
			p_state.getD_map().setMapFile(l_mapFileName);
		}
	}
    /**
     * This method handles the edit neighbour operation on the map.
     * @param p_state Current state of the game.
     * @param p_operation Specifies the add or remove operation that has to be done
     * @param p_argument Arguments that are passed in the command
     * @throws InvalidMap invalid map exception
     */
    public void editNeighbour(GameState p_state, String p_operation, String p_argument) throws InvalidMap{
		String l_mapFileName= p_state.getD_map().getMapFile();
		
        Map l_currentMap=null;
        if(p_state.getD_map().getContinentsList()==null && p_state.getD_map().getCountriesList()==null){
            l_currentMap=this.load(p_state,l_mapFileName);
        }else{
            l_currentMap=p_state.getD_map();
        }

        // Segregates the add and remove operation
		if(l_currentMap!=null) {
			
            
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

			p_state.setD_map(l_currentMap);
			p_state.getD_map().setMapFile(l_mapFileName);
            
            
		}
	}

    /**
     * This method links the countries and the continents
     * @param p_countries List of countries
     * @param p_continents List of continents
     * @return List of Continents after connecting the countries
     */
    public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
		for (Country c : p_countries) {
			for (Continent cont : p_continents) {
				if (cont.getD_id().equals(c.getD_continentId())) {
					cont.addCountry(c);
				}
			}
		}
		return p_continents;
	}

    /**
     * This method handles the save map operation on the current loaded map
     * @param p_gameState current state of the map
     * @param p_fileName Map file name
     * @return True or False. Whether the map is saved or not.
     * @throws InvalidMap invalid map exception
     */
    public boolean saveMap(GameState p_gameState, String p_fileName) throws InvalidMap {
 		try {
 			System.out.println(p_gameState.getD_map().getMapFile());
 			
 			if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getMapFile())) {
 				p_gameState.setError("Kindly provide same file name to save which you have given for edit");
 				return false;
 			} else {
 				if (p_gameState.getD_map()!=null) {
 					Map l_currentMap = p_gameState.getD_map();

 					
 					System.out.println("Validating Map......");
 					boolean l_mapValidationStatus = l_currentMap.isValidMap();
 					if (l_mapValidationStatus) {
 						Files.deleteIfExists(Paths.get(getMapFilePath(p_fileName)));
 						FileWriter l_writer = new FileWriter(getMapFilePath(p_fileName));

 						if (null != p_gameState.getD_map().getContinentsList()
 								&& !p_gameState.getD_map().getContinentsList().isEmpty()) {
 							writeContinentMetadata(p_gameState, l_writer);
 						}
 						if (null != p_gameState.getD_map().getCountriesList()
 								&& !p_gameState.getD_map().getCountriesList().isEmpty()) {
 							writeCountryAndBoarderMetaData(p_gameState, l_writer);
 						}
 						l_writer.close();
 					}
 				} else {
 					p_gameState.setError("Validation Failed");
 					return false;
 				}
 			}
 			return true;
 		} catch (IOException l_e) {
 			l_e.printStackTrace();
 			p_gameState.setError("Error in saving map file");
 			return false;
 		}
 	}
    /**
     * This method writes the continent data in the map file.
     * @param p_gameState current state of the game.
     * @param p_writer File writer
     * @throws IOException Exception
    */
    private void writeContinentMetadata(GameState p_gameState, FileWriter p_writer) throws IOException {
 		p_writer.write(System.lineSeparator() + "[continents]" + System.lineSeparator());
 		for (Continent l_continent : p_gameState.getD_map().getContinentsList()) {
 			p_writer.write(
 					l_continent.getD_name().concat(" ").concat(l_continent.getD_value().toString())
 							+ System.lineSeparator());
 		}
 	}
    /**
     * This method writes the country and border data in the map file.
     * @param p_gameState current state of the map.
     * @param p_writer File Writer
     * @throws IOException Exception
     */ 
    private void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
 		String l_countryMetaData = new String();
 		String l_bordersMetaData = new String();
 		List<String> l_bordersList = new ArrayList<>();

 		//Writes the countries data and creates the adjacent countries list/ borders list
 		p_writer.write(System.lineSeparator() + "[countries]" + System.lineSeparator());
 		for (Country l_country : p_gameState.getD_map().getCountriesList()) {
 			l_countryMetaData = new String();
 			l_countryMetaData = l_country.getD_id().toString().concat(" ").concat(l_country.getD_name())
 					.concat(" ").concat(l_country.getD_continentId().toString());
 			p_writer.write(l_countryMetaData + System.lineSeparator());

 			if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
 				l_bordersMetaData = new String();
 				l_bordersMetaData = l_country.getD_id().toString();
 				for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
 					l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
 				}
 				l_bordersList.add(l_bordersMetaData);
 			}
 		}

        //Writes the borders data
 		if (null != l_bordersList && !l_bordersList.isEmpty()) {
 			p_writer.write(System.lineSeparator() + "[borders]" + System.lineSeparator());
 			for (String l_borderStr : l_bordersList) {
 				p_writer.write(l_borderStr + System.lineSeparator());
 			}
 		}
 	}
    /**
     * This method gives the absolute file path 
     * @param p_fileName Name of the file
     * @return Absolute file path
     */
    public String getMapFilePath(String p_fileName) {
		String l_absolutePath = new File("").getAbsolutePath();
		return l_absolutePath + File.separator + "src/main/resources" + File.separator + p_fileName;
	}

}
