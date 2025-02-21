package com.enigmacamp.loan_app.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiBash {
    public static final String API_BASE = "/api/v1";

    public static final String AUTH_ENDPOINT = API_BASE + "/auth";
    public static final String SIGNUP = "/signup";
    public static final String SIGNIN = "/signin";
    public static final String LOGOUT = "/logout";
    public static final String ADMIN_SIGNUP = SIGNUP + "/admin";
    public static final String STAFF_SIGNUP = SIGNUP + "/staff";

    public static final String GET_BY_ID = "/{id}";
    public static final String DELETE = "/{id}";
    public static final String UPDATE = "/{id}";
}
