package com.axiom.service;

import javax.servlet.http.HttpServletRequest;

import com.axiom.exception.MobileNotFoundException;
import com.axiom.exception.ServiceException;

public interface SearchService {
    public Object fetchMobileDetails(HttpServletRequest request, String searchCriteria) throws MobileNotFoundException;

    public Object fetchAllMobiles(HttpServletRequest request) throws MobileNotFoundException;
}
