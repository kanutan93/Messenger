package ru.just.messenger.config.security;

public class SecurityConst {
  public static final String SECRET = "secret123";
  public static final long EXPIRATION_TIME = 10 * 86400000; // 10 days
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String AUTH_TOKEN = "AUTH_TOKEN";

  public static final String SIGN_UP_URL = "/auth/sign-up";
  public static final String SIGN_IN_URL = "/auth/sign-in";
  public static final String SIGN_OUT_URL = "/auth/sign-out";
  public static final String IS_LOGGED_IN_URL = "/auth/logged-in";
}
