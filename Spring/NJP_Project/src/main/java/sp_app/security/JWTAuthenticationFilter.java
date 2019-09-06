package sp_app.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import sp_app.entities.ApplicationUser;
import sp_app.repository.ApplicationUserRepository;

import static sp_app.security.SecurityConstants.*;

/**
 * Sluzi da da JSON Web Token user-u koji pokusava da pristupi (user salje
 * username i password).
 *
 * Ova klasa nasledjuje UsernamePasswordAuthenticationFilter koja nasledjuje
 * AbstractAuthenticationProcessingFilter Ocekuje da u login formi imamo dva
 * parametra: username i password Ukoliko hocemo da promenimo ove default
 * vrednosti moramo da uradimo override settera za parametre: usernameParameter
 * i passwordParameter Po defaultu ocekuje da je URL /login
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private ApplicationUserRepository userRepo;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
			ApplicationUserRepository applicationUserRepository) {
		this.userRepo = applicationUserRepository;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			ApplicationUser creds = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);

			ApplicationUser user = userRepo.findByUsername(creds.getUsername());
			if (!user.isVerified()) {
				throw new Exception();
			}

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) {

		String token = JWT.create().withSubject(((User) auth.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).sign(HMAC512(SECRET.getBytes()));
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
}