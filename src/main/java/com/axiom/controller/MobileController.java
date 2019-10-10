package com.axiom.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axiom.exception.MobileNotFoundException;
import com.axiom.exception.ServiceException;
import com.axiom.service.SearchService;
import com.axiom.util.ApiConstants;
import com.axiom.util.swagger.SwaggerResponse;
import com.axiom.util.swagger.SwaggerTags;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ApiConstants.REQUEST_MAPPING_MOBILE)
@Api(tags = {ApiConstants.MOBILE_SERVICES})
public class MobileController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/")
    @ApiOperation(value = SwaggerTags.VALUE_MOBILE_SEARCH, notes = SwaggerTags.NOTES_MOBILE_SEARCH)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerResponse.MOBILE_SEARCH_DETAILS),
            @ApiResponse(code = 422, message = SwaggerResponse.SERVICE_EXCEPTION)
    })
    public ResponseEntity<Object> mobilesList (HttpServletRequest request) throws MobileNotFoundException {
        return ok(searchService.fetchAllMobiles(request));
    }

    @GetMapping(ApiConstants.SEARCH)
    @ApiOperation(value = SwaggerTags.VALUE_MOBILE_SEARCH, notes = SwaggerTags.NOTES_MOBILE_SEARCH)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerResponse.MOBILE_SEARCH_DETAILS),
            @ApiResponse(code = 422, message = SwaggerResponse.SERVICE_EXCEPTION)
    })
    public ResponseEntity<Object> searchMobile(HttpServletRequest request,
                                               @RequestParam(value = "search") String searchCriteria) throws MobileNotFoundException {
        return ok(searchService.fetchMobileDetails(request, searchCriteria));
    }
}
