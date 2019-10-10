package com.axiom.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.axiom.exception.MobileNotFoundException;
import com.axiom.model.Mobile;
import com.axiom.service.SearchService;
import com.axiom.util.ErrorConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Mobile> fetchMobileDetails(HttpServletRequest request, String searchCriteria) throws MobileNotFoundException {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://api.myjson.com/bins/1f2r2v?pretty=true",
                    String.class);

            LOGGER.info(response.toString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Mobile.class));
        } catch (Exception e) {
            LOGGER.error("Exception in fetchMobileDetails", e);
            throw new MobileNotFoundException(ErrorConstants.SERVICE_EXCEPTION);
        }
    }

    @Override
    public Object fetchAllMobiles(HttpServletRequest request) throws MobileNotFoundException {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://api.myjson.com/bins/1f2r2v?pretty=true",
                    String.class);

            LOGGER.info(response.getBody());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Mobile.class));
        } catch (Exception e) {
            LOGGER.error("Exception in fetchMobileDetails", e);
            throw new MobileNotFoundException(ErrorConstants.SERVICE_EXCEPTION);
        }
    }
}
