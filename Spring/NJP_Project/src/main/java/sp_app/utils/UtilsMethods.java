package sp_app.utils;

import static sp_app.security.SecurityConstants.HEADER_STRING;
import static sp_app.security.SecurityConstants.SECRET;
import static sp_app.security.SecurityConstants.TOKEN_PREFIX;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.io.Files;

import sp_app.entities.ApplicationUser;
import sp_app.entities.Komentar;
import sp_app.entities.Post;
import sp_app.entities.holding.Likke;
import sp_app.entities.holding.PostInfo;
import sp_app.repository.ApplicationUserRepository;
import sp_app.repository.PostRepository;

public final class UtilsMethods {

	static public String generateToken(int length) {
		String CharSet = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ1234567890";
		String Token = "";
		for (int a = 1; a <= length; a++) {
			Token += CharSet.charAt(new Random().nextInt(CharSet.length()));
		}
		return Token;
	}

	static public synchronized boolean saveProfilePic(byte[] photo, String name) {
		try {
			Files.write(photo, new File("D:/NJP/ProfilePics/" + name + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static public synchronized boolean savePostPic(byte[] photo, String name) {
		try {
			Files.write(photo, new File("D:/NJP/Posts/" + name + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static public synchronized byte[] readFile(String filePath) {
		try {
			Path path = Paths.get(filePath);
			return java.nio.file.Files.readAllBytes(path);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static public boolean regexMatcher(String patern, String value) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(patern);
		java.util.regex.Matcher matcher = pattern.matcher(value);

		return matcher.matches();
	}

	static public synchronized void setDefaultProfilePic(String username) {
		byte[] defaultPic = readFile("D:/NJP/default.png");
		saveProfilePic(defaultPic, username);
	}

	static public byte[] scaleImage(byte[] data, int width, int height) {
		try {
			BufferedImage bsrc = ImageIO.read(new ByteArrayInputStream(data));

			BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bdest.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance((double) width / bsrc.getWidth(),
					(double) height / bsrc.getHeight());
			g.drawRenderedImage(bsrc, at);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bdest, "png", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();

			return imageInByte;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	static public ApplicationUser getUserFromHeader(HttpServletRequest request, ApplicationUserRepository userRepo) {

		String token = request.getHeader(HEADER_STRING);
		String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX, ""))
				.getSubject();

		return userRepo.findByUsername(user);
	}

	static public boolean isFolowing(HttpServletRequest request, ApplicationUserRepository userRepo, String fuser) {
		try {
			ApplicationUser user = getUserFromHeader(request, userRepo);
			ApplicationUser folowUser = userRepo.findByUsername(fuser);

			if (user.getFolowing().contains(folowUser))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}

	}

	static public PostInfo createPostInfo(ApplicationUser user, String username, int postNum, PostRepository postRepo) {
		Post post = postRepo.findByPath(username + postNum);
		if (post == null) {
			return null;
		}

		byte[] file = UtilsMethods.readFile("D:/NJP/Posts/" + username + postNum + ".png");
		PostInfo postInfo = new PostInfo();
		postInfo.setFile(Base64.getEncoder().encodeToString(file));
		postInfo.setOpis(post.getOpis());
		postInfo.setOwner(username);
		
		ArrayList<Komentar> list = new ArrayList<>(post.getKomentari());
		Collections.sort(list);
		
		postInfo.setKomentari(list);
		postInfo.setNummberOfLikes(post.getLikkes().size());
		Likke like = new Likke();
		like.setUsername(user.getUsername());
		postInfo.setLiked(post.getLikkes().contains(like));
		postInfo.setPostnum(postNum);

		return postInfo;
	}

}
