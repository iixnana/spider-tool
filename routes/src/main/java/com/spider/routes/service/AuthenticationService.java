package com.spider.routes.service;

import com.spider.routes.dao.request.SignInRequest;
import com.spider.routes.dao.request.SignUpRequest;
import com.spider.routes.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
