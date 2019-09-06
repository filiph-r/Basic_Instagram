package sp_app.security;

public final class SecurityConstants {
	public static final String SECRET = "MojSecretKey";
	public static final long EXPIRATION_TIME = 864000000;
	public static final String TOKEN_PREFIX = "Basic ";
	public static final String HEADER_STRING = "Authorization";

	public static final String LOGIN_URL = "/login";
	public static final String REGISTER_URL = "/users/registration";
	public static final String VALIDATE_URL = "/users/validation";
}