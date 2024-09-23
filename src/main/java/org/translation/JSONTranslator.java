package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // Instance variables to hold the countries and their respective languages and translations
    private final List<String> countries;
    private final List<List<String>> countryLanguages;
    private final List<List<String>> translations;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        countries = new ArrayList<>();
        countryLanguages = new ArrayList<>();
        translations = new ArrayList<>();

        // read the file to get the data to populate things...
        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            // Loop through JSON array to populate instance variables
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String country = countryObject.getString("country");

                // Add the country to the list
                countries.add(country);

                // Get the languages array
                JSONArray languagesArray = countryObject.getJSONArray("languages");
                List<String> languages = new ArrayList<>();
                List<String> countryTranslation = new ArrayList<>();

                // Loop through languages to build language lists and translations
                for (int j = 0; j < languagesArray.length(); j++) {
                    JSONObject languageObject = languagesArray.getJSONObject(j);
                    String language = languageObject.getString("language");
                    String translation = languageObject.getString("translation");

                    languages.add(language);
                    countryTranslation.add(translation);
                }

                // Add the languages and translations to their respective lists
                countryLanguages.add(languages);
                translations.add(countryTranslation);
            }

        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // Find the index of the country in the list
        int index = countries.indexOf(country);
        if (index != -1) {
            // Return a new list of languages to avoid aliasing
            return new ArrayList<>(countryLanguages.get(index));
        }
        return new ArrayList<>(); // return empty if country not found
    }

    @Override
    public List<String> getCountries() {
        // Return a new list of all countries to avoid aliasing
        return new ArrayList<>(countries);
    }

    @Override
    public String translate(String country, String language) {
        // Find the index of the country in the list
        int countryIndex = countries.indexOf(country);
        if (countryIndex != -1) {
            // Find the index of the language in the country's language list
            List<String> languages = countryLanguages.get(countryIndex);
            int languageIndex = languages.indexOf(language);
            if (languageIndex != -1) {
                // Return the corresponding translation
                return translations.get(countryIndex).get(languageIndex);
            }
        }
        return null; // return null if either the country or language is not found
    }
}