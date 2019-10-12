package com.axiom.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.axiom.model.Mobile;
import com.axiom.util.ApiConstants;
import com.axiom.util.ErrorConstants;
import com.axiom.util.Utility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MobileControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MobileControllerTest.class);

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@LocalServerPort
	private int port;

	@BeforeAll
	static void beforeAll() {
		LOGGER.info("Start of Mobile Controller Test (Timestamp: {})", LocalDateTime.now());
	}

	@AfterAll
	static void afterAll() {
		LOGGER.info("End of Mobile Controller Test (Timestamp: {})", LocalDateTime.now());
	}

	@Test
	@DisplayName("Test Fetching All Mobiles")
	public void searchMobile_FullResponseTest() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port));
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		List<Mobile> mobilesList = Utility.fetchMobilesFromResponse(response.getBody(), mapper);
		assertNotNull(mobilesList);
		assertEquals(105, mobilesList.size());
	}

	@Test
	@DisplayName("Test for SIM Type=eSIM")
	public void searchMobile_SimType_ESIM_Test() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.SIM_KEY, ApiConstants.E_SIM);
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		List<Mobile> mobileList = checkStatusAndGetReponseObject(response);

		mobileList.forEach(mobile -> {
			assertThat(mobile.getSim().contains(ApiConstants.E_SIM));
		});
	}

	@Test
	@DisplayName("Test for priceEur=200")
	public void searchMobile_Price_200() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.PRICE_EUR_KEY, 200);
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		List<Mobile> mobilesList = checkStatusAndGetReponseObject(response);
		assertEquals(10, mobilesList.size());
	}

	@Test
	@DisplayName("Test for priceEur=200 & announceDate=1999")
	public void searchMobile_Price_200_And_AnnounceDate_1999() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.PRICE_EUR_KEY, 200).queryParam(ApiConstants.ANNOUNCE_DATE_KEY, "1999");
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		List<Mobile> mobilesList = checkStatusAndGetReponseObject(response);
		assertEquals(2, mobilesList.size());
	}

	@Test
	@DisplayName("Test for priceEur=1100 & announceDate=2018")
	public void searchMobile_Price_1100_And_AnnounceDate_2018() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.PRICE_EUR_KEY, 1100).queryParam(ApiConstants.ANNOUNCE_DATE_KEY, "2018");
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		List<Mobile> mobileList = checkStatusAndGetReponseObject(response);
		assertEquals(1, mobileList.size());
	}

	@Test
	@DisplayName("Test for brand=Apple & announceDate=2018")
	public void searchMobile_Brand_Apple_And_AnnounceDate_2018() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.BRAND_KEY, "Apple").queryParam(ApiConstants.ANNOUNCE_DATE_KEY, "2018");
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		List<Mobile> mobileList = checkStatusAndGetReponseObject(response);
		assertEquals(8, mobileList.size());
	}

	@Test
	@DisplayName("Test for brand=Apple & announceDate=2018 & sim=eSIM")
	public void searchMobile_Brand_Apple_And_AnnounceDate_2018_And_Sim_ESIM() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.BRAND_KEY, "Apple").queryParam(ApiConstants.ANNOUNCE_DATE_KEY, "2018")
				.queryParam(ApiConstants.SIM_KEY, ApiConstants.NANO_SIM);
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		List<Mobile> mobileList = checkStatusAndGetReponseObject(response);
		assertEquals(5, mobileList.size());
	}

	@Test
	@DisplayName("Test for MobileNotFoundException")
	public void searchMobile_MobileNotFoundException() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(Utility.createURI(port))
				.queryParam(ApiConstants.BRAND_KEY, "Nokia");
		ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}
	
	private List<Mobile> checkStatusAndGetReponseObject(ResponseEntity<String> response)
			throws JsonParseException, JsonMappingException, IOException {
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Mobile> mobileList = Utility.fetchMobilesFromResponse(response.getBody(), mapper);
		assertNotNull(mobileList);
		return mobileList;
	}

}
