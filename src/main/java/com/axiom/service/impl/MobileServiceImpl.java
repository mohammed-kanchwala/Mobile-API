package com.axiom.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.axiom.exception.MobileNotFoundException;
import com.axiom.model.Mobile;
import com.axiom.service.MobileService;
import com.axiom.util.ApiConstants;
import com.axiom.util.ErrorConstants;
import com.axiom.util.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;

@Service
public class MobileServiceImpl implements MobileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${mobile.db.api}")
    private String mobileDbApi;

    private String mobileDbResponse;

    @PostConstruct
    public void postConstruct() {
        ResponseEntity<String> response = restTemplate.getForEntity(mobileDbApi, String.class);
        mobileDbResponse = response.getBody();
    }

    /**
     * Searches for Mobile from the Json based the params
     *
     * @param params: List of query parameters
     * @return List of Mobiles
     * @throws MobileNotFoundException
     */
    @Override
    public List<Mobile> searchMobile(Map<String, String> params)
            throws MobileNotFoundException {
        List<Mobile> mobileList;
        try {
            JSONArray jsonArray = new JSONArray(mobileDbResponse);
            JSONArray resultArray = new JSONArray();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey().toLowerCase();
                key = getValueOfKeyIfPrice(key);
                String value = entry.getValue();

                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    JSONObject json = jsonArray.getJSONObject(i);

                    fetchMobilesBasedOnInputConditions(json, resultArray, key, value);
                }
                jsonArray = resultArray;
                resultArray = new JSONArray();
            }

            mobileList = Utility.fetchMobilesFromResponse(jsonArray.toString(), mapper);
            if (mobileList.isEmpty()) {
                throw new MobileNotFoundException(ErrorConstants.MOBILE_NOT_FOUND_MESSAGE);
            }
        } catch (Exception e) {
            LOGGER.error(ErrorConstants.SERVICE_EXCEPTION);
            throw new MobileNotFoundException(ErrorConstants.MOBILE_NOT_FOUND_MESSAGE);
        }
        return mobileList;
    }

    /**
     * Updating the value of key if the key in request is only having "price" instead of "priceEur"
     *
     * @param key: input key
     * @return key: Updated key with lowercase
     */
    private String getValueOfKeyIfPrice(String key) {
        key = key.equalsIgnoreCase(ApiConstants.PRICE) ? ApiConstants.PRICE_EUR_KEY : key;
        return key.toLowerCase();
    }

    /**
     * Fetches the List of Mobiles Json based on the input conditions
     *
     * @param json:        Input JsonArray which holds the result after the iteration
     * @param resultArray: JsonArray which holds the result of the iteration
     * @param key:         parameter from request
     * @param value:       value of the parameter from the request
     */
    private void fetchMobilesBasedOnInputConditions(JSONObject json, JSONArray resultArray, String key, String value) {

        boolean isKeyExistInRelease = isKeyExistsInJson(json.getJSONObject(ApiConstants.RELEASE), key, value);

        boolean isKeyExistInHardware = isKeyExistsInJson(json.getJSONObject(ApiConstants.HARDWARE), key, value);

        boolean isKeyExistInMobileParent = isKeyExistsInJson(json, key, value);

        if (isKeyExistInMobileParent || isKeyExistInRelease || isKeyExistInHardware) {
            resultArray.put(json);
        }
    }

    /**
     * Checks if the given key is present in the given Json
     *
     * @param json:  Json to check for the key
     * @param key:   input key to check if it exists in the Json
     * @param value: input value which gets compared to the value of the Json based on the input key
     * @return isExists:    true if the key exists in Json along with the input value
     */
    private boolean isKeyExistsInJson(JSONObject json, String key, String value) {
        boolean isExists = false;
        json = fetchJsonWithLowerCaseKey(json);
        if (json.has(key)) {
            Object currentValue = json.get(key);
            if (currentValue instanceof Integer) {
                if (Objects.equal(currentValue, Integer.parseInt(value))) {
                    isExists = true;
                }
            } else if (Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE)
                    .matcher((CharSequence) currentValue).find()) {
                isExists = true;
            }
        }
        return isExists;
    }


    /**
     * Converts the input Json to have all the keys in lower-case
     * @param jsonObject : Json Object to convert the keys to lower-case
     * @return JsonObject: Converted JsonObject with all keys to lower-case
     * @throws JSONException
     */
    public JSONObject fetchJsonWithLowerCaseKey(JSONObject jsonObject) throws JSONException {
        JSONObject resultJsonObject = new JSONObject();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value;
            try {
                JSONObject nestedJsonObject = jsonObject.getJSONObject(key);
                value = fetchJsonWithLowerCaseKey(nestedJsonObject);
            } catch (JSONException jsonException) {
                value = jsonObject.get(key);
            }
            resultJsonObject.put(key.toLowerCase(), value);
        }
        return resultJsonObject;
    }
}
