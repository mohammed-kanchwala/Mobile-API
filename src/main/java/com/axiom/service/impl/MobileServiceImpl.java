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
import com.axiom.util.ErrorConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MobileServiceImpl implements MobileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Value("${mobile.db.api}")
	private String mobileDbApi;

	private static String mobileDbResponse;

	@PostConstruct
	public void postConstruct() {
		ResponseEntity<String> response = restTemplate.getForEntity(mobileDbApi, String.class);
		mobileDbResponse = response.getBody();
	}

	@Override
	public List<Mobile> searchMobile(HttpServletRequest request, Map<String, String> params)
			throws MobileNotFoundException {

		JSONArray jsonArray = new JSONArray(mobileDbResponse);
		JSONArray resultArray = new JSONArray();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().toLowerCase();
			for (int i = 0; i <= jsonArray.length() - 1; i++) {
				JSONObject json = jsonArray.getJSONObject(i);

				JSONObject jsonRelease = json.getJSONObject("release");
				JSONObject jsonHardware = json.getJSONObject("hardware");
				boolean isRelease = false;
				boolean isHardware = false;
				if (jsonRelease.has(key)) {
					Object currentValue = jsonRelease.get(key);
					if (currentValue instanceof Double) {
						if (currentValue == value) {
							isRelease = true;
						}
					} else if (Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE)
							.matcher((CharSequence) currentValue).find()) {
						isRelease = true;
					}
				}
				if (jsonHardware.has(key) && jsonHardware.getString(key).contains(value)) {
					isHardware = true;
				}

				Object currentValue = json.get(key);
				if (currentValue instanceof Long) {
					if (currentValue == value) {
						resultArray.put(json);
					}
				} else if (json.has(key) && Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE)
						.matcher((CharSequence) currentValue).find()) {
					resultArray.put(json);
				} else if (isRelease || isHardware) {
					resultArray.put(json);
				}
			}
			jsonArray = resultArray;
			resultArray = new JSONArray();
		}

		try {
			List<Mobile> mobilesList = mapper.readValue(jsonArray.toString(),
					mapper.getTypeFactory().constructCollectionType(List.class, Mobile.class));
			return mobilesList;
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.SERVICE_EXCEPTION);
			throw new MobileNotFoundException(ErrorConstants.MOBILE_NOT_FOUND_MESSAGE);
		}

	}

}
