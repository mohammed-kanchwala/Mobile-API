package com.axiom.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axiom.exception.MobileNotFoundException;
import com.axiom.model.Mobile;
import com.axiom.service.MobileService;
import com.axiom.util.ApiConstants;
import com.axiom.util.swagger.SwaggerResponse;
import com.axiom.util.swagger.SwaggerTags;

;

@RestController
@RequestMapping(ApiConstants.REQUEST_MAPPING_MOBILE)
@Api(tags = {ApiConstants.MOBILE_SERVICES})
public class MobileController {

    @Autowired
    private MobileService searchService;

    @GetMapping(ApiConstants.SEARCH)
    @ApiOperation(value = SwaggerTags.VALUE_MOBILE_SEARCH, notes = SwaggerTags.NOTES_MOBILE_SEARCH)
    @ApiResponses(value = {@ApiResponse(code = 200, message = SwaggerResponse.MOBILE_SEARCH_DETAILS),
            @ApiResponse(code = 422, message = SwaggerResponse.SERVICE_EXCEPTION)})
    public List<Mobile> searchMobile(@RequestParam Map<String, String> search)
            throws MobileNotFoundException {
        return searchService.searchMobile(search);
    }
}
