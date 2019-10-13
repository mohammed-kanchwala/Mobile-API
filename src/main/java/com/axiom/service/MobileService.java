package com.axiom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.axiom.exception.MobileNotFoundException;
import com.axiom.model.Mobile;

public interface MobileService {

    List<Mobile> searchMobile(Map<String, String> search)
            throws MobileNotFoundException;

}
