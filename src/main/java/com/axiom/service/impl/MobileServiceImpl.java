package com.axiom.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

	@Override
	public List<Mobile> searchMobile(HttpServletRequest request, Map<String, String> params)
			throws MobileNotFoundException {
		List<Mobile> mobileList = null;
		try {
			JSONArray jsonArray = new JSONArray(mobileDbResponse);
			JSONArray resultArray = new JSONArray();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
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

	private void fetchMobilesBasedOnInputConditions(JSONObject json, JSONArray resultArray, String key, String value) {

		boolean isKeyExistInRelease = isKeyExistsInJson(json.getJSONObject(ApiConstants.RELEASE_KEY), key, value);

		boolean isKeyExistInHardware = isKeyExistsInJson(json.getJSONObject(ApiConstants.HARDWARE_KEY), key, value);

		boolean isKeyExistInMobileParent = isKeyExistsInJson(json, key, value);

		if (isKeyExistInMobileParent || isKeyExistInRelease || isKeyExistInHardware) {
			resultArray.put(json);
		}
	}

	private boolean isKeyExistsInJson(JSONObject json, String key, String value) {
		boolean isExists = false;
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

}
