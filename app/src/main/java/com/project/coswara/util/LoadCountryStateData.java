package com.project.coswara.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.project.coswara.util.Constants.DEFAULT_COUNTRY_EN;

public class LoadCountryStateData {
    private static final HashMap<String, List<String>> countryStateMap = new HashMap<>();

    public static void loadDataMap(Context context) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            context.getAssets().open("country_state_map.json")));

            String mLine;
            StringBuilder str = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                str.append(mLine);
            }

            String jsonStr = str.toString();
            JSONObject jsonObject = new JSONObject(jsonStr);

            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String country = keys.next();
                JSONArray states = jsonObject.getJSONArray(country);
                List<String> statesList = gson.fromJson(states.toString(), type);

                countryStateMap.put(country, statesList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> getCities(String countryName, String defVal) {
        if (countryName == null) return null;

        if (countryName.equalsIgnoreCase(DEFAULT_COUNTRY_EN))
            return new ArrayList<>(Collections.singletonList(defVal));

        List<String> states = countryStateMap.get(countryName.toLowerCase()
                .replace(" ", "_")
                .replace("(", "")
                .replace(")", "")
        );

        if (states != null) {
            states.set(0, defVal);
        } else {
            states = new ArrayList<>(Collections.singletonList(defVal));
        }

        return states;
    }
}
