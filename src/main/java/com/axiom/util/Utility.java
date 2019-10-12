package com.axiom.util;

import java.io.IOException;
import java.util.List;

import com.axiom.model.Mobile;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {
	public static List<Mobile> fetchMobilesFromResponse(String response, ObjectMapper mapper) throws IOException {
		return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, Mobile.class));
	}

	/**
	 * @return
	 */
	public static String createURI(int port) {
		return ApiConstants.HTTP_LOCALHOST + port + ApiConstants.REQUEST_MAPPING_MOBILE + ApiConstants.SEARCH;
	}

	private Utility() {

	}
}
