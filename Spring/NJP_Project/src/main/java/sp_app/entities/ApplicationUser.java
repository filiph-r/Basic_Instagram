package sp_app.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class ApplicationUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private boolean verified;
	private String activationToken;

	private String username;
	private String password;
	private String email;
	private long postCounter = 0;

	private long folowers = 0;
	@ManyToMany
	private Collection<ApplicationUser> folowing = new ArrayList<>();
	@OneToMany
	private Collection<Post> posts = new ArrayList<>();

	// --------------------------------------------------------------------------------------------------------
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getPostCounter() {
		return postCounter;
	}

	public void setPostCounter(long postCounter) {
		this.postCounter = postCounter;
	}

	public Collection<ApplicationUser> getFolowing() {
		return folowing;
	}

	public void setFolowing(Collection<ApplicationUser> folowing) {
		this.folowing = folowing;
	}

	public Collection<Post> getPosts() {
		return posts;
	}

	public void setPosts(Collection<Post> posts) {
		this.posts = posts;
	}

	public long getFolowers() {
		return folowers;
	}

	public void setFolowers(long folowers) {
		this.folowers = folowers;
	}
	

}