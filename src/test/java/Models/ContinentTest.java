package Models;

import Exceptions.InvalidMap;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;


public class ContinentTest {

    /**
     * testAddCountry method will check list of country added to Continent
     */
    @Test
    public void testAddCountry()
    {
        testAddCountryAndReturnContinent();
    }

    /**
     *
     * @return Continent List
     *
     * testAddCountryAndReturnContinent method is used to add country in list
     */
    private Continent testAddCountryAndReturnContinent()
    {
        Continent l_continent = new Continent(1, "Asia", 5 );
        Country l_country1= new Country(2, 10,"India");
        Country l_country2= new Country(1, 6,"China");

        l_continent.addCountry(l_country1);
        l_continent.addCountry(l_country2);

        List<Country> l_expectedCountries = new ArrayList<>();
        l_expectedCountries.add(l_country1);
        l_expectedCountries.add(l_country2);

        List<Country> l_actualCountries = l_continent.getD_countries();

        Assert.assertArrayEquals("Expected and actual list of countries in continent are not equal. Actual = " + l_actualCountries + " Expected = " + l_expectedCountries, l_expectedCountries.toArray(), l_actualCountries.toArray());

        return l_continent;
    }

    /**
     * testRemoveCountry method will check list of country removed from Continent
     */
    @Test
    public void testRemoveCountry()
    {
        Continent l_continent = testAddCountryAndReturnContinent();
        Country l_countryToBeRemoved= new Country(2, 10,"India");

        l_continent.removeCountry(l_countryToBeRemoved);

        List<Country> l_expectedCountries = new ArrayList<>();
        l_expectedCountries.add(new Country(1, 6,"China"));

        List<Country> l_actualCountries = l_continent.getD_countries();

        Assert.assertArrayEquals("Expected and actual list of countries in continent are not equal. Actual = " + l_actualCountries + " Expected = " + l_expectedCountries, l_expectedCountries.toArray(), l_actualCountries.toArray());

    }

    @Test(expected = InvalidMap.class)
    public void testContinentSubgraphConnectedFailure() throws InvalidMap{
        Map l_map =  new Map();
        l_map.addContinent("America", 2);
        l_map.addCountry("Canada", "America");
        l_map.addCountry("USA", "America");
        l_map.addCountry("Mexico", "America");
        l_map.addCountry("Brazil", "America");
        l_map.addNeighbour("America", "Canada");
        l_map.addNeighbour("Brazil", "Mexico");

        l_map.isContinentConnected(l_map.getContinent("America"));
    }

}