package sp_app.rest.contorllers;

import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sp_app.entities.ApplicationUser;
import sp_app.entities.Komentar;
import sp_app.entities.Post;
import sp_app.entities.holding.CommentHolder;
import sp_app.entities.holding.Likke;
import sp_app.entities.holding.PostData;
import sp_app.entities.holding.PostInfo;
import sp_app.repository.ApplicationUserRepository;
import sp_app.repository.KomentarRepository;
import sp_app.repository.LikeRepository;
import sp_app.repository.PostRepository;
import sp_app.utils.UtilsMethods;

@RestController
@RequestMapping("/post")
public class PostController {

	private ApplicationUserRepository userRepo;
	private PostRepository postRepo;
	private KomentarRepository komentarRepo;
	private LikeRepository likeRepo;

	@Autowired
	public PostController(ApplicationUserRepository applicationUserRepository, PostRepository postRepo,
			KomentarRepository komentarRepo, LikeRepository likeRepo) {
		this.userRepo = applicationUserRepository;
		this.postRepo = postRepo;
		this.komentarRepo = komentarRepo;
		this.likeRepo = likeRepo;
	}

	@PostMapping("/upload")
	public boolean upload(HttpServletRequest req, @RequestBody PostData postData) {
		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);

		byte[] photo = Base64.getDecoder().decode(postData.getData());
		String path = user.getUsername() + user.getPostCounter();
		UtilsMethods.savePostPic(photo, path);

		Post post = new Post();
		post.setOpis(postData.getOpis());
		post.setPath(path);

		user.getPosts().add(post);
		user.setPostCounter(user.getPostCounter() + 1);

		postRepo.save(post);
		userRepo.save(user);
		return true;
	}

	@GetMapping("/download")
	public Object download(HttpServletRequest req, @RequestParam("username") String username,
			@RequestParam("postNum") int postNum) {

		ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);

		if (!UtilsMethods.isFolowing(req, userRepo, username)) {
			return false;
		}

		Post post = postRepo.findByPath(username + postNum);
		if (post == null) {
			return false;
		}

		byte[] file = UtilsMethods.readFile("D:/NJP/Posts/" + username + postNum + ".png");
		PostInfo postInfo = UtilsMethods.createPostInfo(user, username, postNum, postRepo);

		return postInfo;
	}

	@GetMapping("/like")
	public boolean like(HttpServletRequest req, @RequestParam("username") String username,
			@RequestParam("postNum") int postNum) {
		try {
			Post post = postRepo.findByPath(username + postNum);
			ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
			ApplicationUser fuser = userRepo.findByUsername(username);

			Likke like = new Likke();
			like.setUsername(user.getUsername());

			if (user.getFolowing().contains(fuser)) {
				if (!post.getLikkes().contains(like)) {
					post.getLikkes().add(like);
					likeRepo.save(like);
					postRepo.save(post);
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@GetMapping("/dislike")
	public boolean dislike(HttpServletRequest req, @RequestParam("username") String username,
			@RequestParam("postNum") int postNum) {

		try {
			Post post = postRepo.findByPath(username + postNum);
			ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);

			Likke like = new Likke();
			like.setUsername(user.getUsername());

			if (post.getLikkes().contains(like)) {

				post.getLikkes().remove(like);
				likeRepo.save(like);
				postRepo.save(post);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	@PostMapping("/comment")
	public boolean comment(HttpServletRequest req, @RequestBody CommentHolder commentHolder) {
		try {
			Post post = postRepo.findByPath(commentHolder.getUsername() + commentHolder.getPostNum());
			ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);

			if (UtilsMethods.isFolowing(req, userRepo, commentHolder.getUsername())) {
				Komentar komentar = new Komentar();
				komentar.setValue(user.getUsername() + ": " + commentHolder.getComment());

				post.getKomentari().add(komentar);

				komentarRepo.save(komentar);
				postRepo.save(post);
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@GetMapping("/getPostList/{startAt}")
	public ArrayList<PostInfo> getPostList(HttpServletRequest req, @PathVariable int startAt) {

		try {
			ArrayList<PostInfo> postList = new ArrayList<>();

			ApplicationUser user = UtilsMethods.getUserFromHeader(req, userRepo);
			ArrayList<ApplicationUser> folowingList = new ArrayList<>(user.getFolowing());
			int counter = 0;

			for (int i = 0; i < folowingList.size(); i++) {
				ApplicationUser fuser = folowingList.get(i);
				for (int j = 0; j < fuser.getPostCounter(); j++) {
					if (counter >= startAt) {
						postList.add(UtilsMethods.createPostInfo(user, fuser.getUsername(), j, postRepo));

						if (postList.size() >= 5) {
							return postList;
						}
					} else {
						counter++;
					}
				}
			}

			return postList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}