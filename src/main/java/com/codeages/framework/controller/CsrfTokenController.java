package com.codeages.framework.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController {

	@RequestMapping(path = "/csrf-token", method = RequestMethod.GET)
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return  (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	}
}
