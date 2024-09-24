package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    // == Instance Variables ==
    // codeMap maps a country's alpha-3 code to its English name.
    Map<String, String> codeMap = new HashMap<String, String>();

    // == Constructors ==
    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        try {
            // Get the path of the file in the resources folder
            List<String> lines = Files.readAllLines(Paths.get(getClass().getClassLoader()
                    .getResource(filename).toURI()));

            // Loop through each line in the file
            for (String line : lines) {
                String[] parts = line.split("\t");

                // Make sure we have exactly two parts: country name and code
                if (parts.length >= 3) {
                    String country = parts[0].trim();
                    String code = parts[2].trim();

                    // Add the mapping to the HashMap
                    codeMap.put(code, country);
                }
            }
            codeMap.remove("Alpha-3 code");

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return this.codeMap.get(code);
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        for (String value : this.codeMap.values()) {
            if (value.equals(country)) {
                return country;
            }
        }
        // if the country is not found, return an exception
        throw new RuntimeException("Country not found");
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        int counter = 0;
        for (String country : this.codeMap.values()){
            counter += 1;
        }
        return counter;
    }
}
