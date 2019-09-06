package sp_app.rest.contorllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sp_app.entities.ApplicationUser;
import sp_app.entities.holding.NewEmail;
import sp_app.entities.holding.NewPassword;
import sp_app.entities.holding.NewProfilePic;
import sp_app.entities.holding.UserInfo;
import sp_app.repository.ApplicationUserRepository;
import sp_app.utils.EmailSender;
import sp_app.utils.UtilsMethods;

@RestController
@RequestMapping("/users")
public class UserController {

	private ApplicationUserRepository userRepo;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserController(ApplicationUserRepository applicationUserRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepo = applicationUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping("/registration")
	public boolean registration(@RequestBody ApplicationUser user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		if (userRepo.findByUsername(user.getUsername()) != null) {
			return false;
		}
		if (user.getEmail() == null || !user.getEmail().contains("@")) {
			return false;
		}

		user.setVerified(false);
		String token = user.getUsername() + UtilsMethods.generateToken(30);
		user.setActivationToken(token);
		userRepo.save(user);

		UtilsMethods.setDefaultProfilePic(user.getUsername());

		EmailSender.sendVerification(user.getEmail(), token);

		return true;
	}

	@GetMapping("/validation")
	public String validation(@RequestParam("token") String token) {
		ApplicationUser user = userRepo.findByActivationToken(token);
		if (user == null) {
			return "validation ERROR";
		}

		if (user.getActivationToken().equals("utilized")) {
			return "validation ERROR";
		}

		user.setVerified(true);
		user.setActivationToken("utilized");
		userRepo.save(user);

		return "validation successful";
	}

	@PostMapping("change/password")
	public boolean changePassword(HttpServletRequest req, @RequestBody NewPassword newPw) {

		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
		if (!user.isVerified()) { // nepotrebno jer se radi vec preko spring security-a
			return false;
		}

		user.setPassword(bCryptPasswordEncoder.encode(newPw.getPassword()));
		userRepo.save(user);

		return true;
	}

	@PostMapping("change/email")
	public boolean changeEmail(HttpServletRequest req, @RequestBody NewEmail newEmail) {

		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
		if (!user.isVerified()) {// nepotrebno jer se radi vec preko spring security-a
			return false;
		}

		user.setEmail(newEmail.getEmail());
		userRepo.save(user);

		return true;
	}

	@PostMapping("change/profile")
	public boolean changeProfile(HttpServletRequest req, @RequestBody NewProfilePic newProfilePic) {

		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
		if (!user.isVerified()) {// nepotrebno jer se radi vec preko spring security-a
			return false;
		}

		try {
			byte[] pic = Base64.getDecoder().decode(newProfilePic.getData());
			UtilsMethods.saveProfilePic(UtilsMethods.scaleImage(pic, 160, 160), user.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		userRepo.save(user);
		return true;
	}

	@GetMapping("/profile/{username}")
	public String getProfile(@PathVariable String username) {

		try {
			String prof = Base64.getEncoder()
					.encodeToString(UtilsMethods.readFile("D:/NJP/ProfilePics/" + username + ".png"));
			return prof;
		} catch (Exception e) {
			return "false";
		}

	}

	@GetMapping("/folow/{username}")
	public boolean folow(HttpServletRequest req, @PathVariable String username) {
		ApplicationUser folowUser = userRepo.findByUsername(username);
		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);

		if (folowUser == null) {
			return false;
		}
		if (folowUser.getUsername().equals(user.getUsername())) {
			return false;
		}
		if (user.getFolowing().contains(folowUser)) {
			return false;
		}

		folowUser.setFolowers(folowUser.getFolowers() + 1);
		user.getFolowing().add(folowUser);

		userRepo.save(user);
		userRepo.save(folowUser);
		return true;
	}

	@GetMapping("/unfolow/{username}")
	public boolean unfolow(HttpServletRequest req, @PathVariable String username) {
		ApplicationUser unfolowUser = userRepo.findByUsername(username);
		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);

		if (unfolowUser == null) {
			return false;
		}
		if (!user.getFolowing().contains(unfolowUser)) {
			return false;
		}

		user.getFolowing().remove(unfolowUser);
		unfolowUser.setFolowers(unfolowUser.getFolowers() - 1);

		userRepo.save(user);
		userRepo.save(unfolowUser);
		return true;
	}

	@GetMapping("/getallfolowing") // vraca listu korisnika koje dati korisnik prati
	public List<String> getAllFolowing(HttpServletRequest req) {
		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
		List<String> folowing = new ArrayList<>();

		for (ApplicationUser u : user.getFolowing()) {
			folowing.add(u.getUsername());
		}

		return folowing;
	}

	@GetMapping("/info/{username}")
	public UserInfo getUserInfo(HttpServletRequest req, @PathVariable String username) {
		try {
			UserInfo info = new UserInfo();
			ApplicationUser user1 = UtilsMethods.getUserFromHeader(req, userRepo);
			ApplicationUser user2 = userRepo.findByUsername(username);

			info.setUsername(user2.getUsername());
			info.setProfilePic(Base64.getEncoder()
					.encodeToString(UtilsMethods.readFile("D:/NJP/ProfilePics/" + username + ".png")));
			info.setFolowers(user2.getFolowers());
			info.setFolowing(user2.getFolowing().size());
			info.setFolowing(user2.getFolowing().contains(user1));
			info.setBrojSlika(user2.getPostCounter());

			return info;

		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/getAll")
	public List<String> getAllUsers(HttpServletRequest req) {
		try {
			ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
			List<String> list = new ArrayList<>();
			List<ApplicationUser> users = userRepo.findAll();

			for (ApplicationUser u : users) {
				if (!user.getUsername().equals(u.getUsername())) {
					list.add(u.getUsername());
				}
			}
			return list;

		} catch (Exception e) {
			return null;
		}

	}
}