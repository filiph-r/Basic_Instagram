package sp_app.entities.holding;

public class UserInfo {

	private String username;
	private String profilePic;
	private long folowers;
	private long folowing;
	private boolean isFolowing;
	private long brojSlika;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public long getFolowers() {
		return folowers;
	}

	public void setFolowers(long folowers) {
		this.folowers = folowers;
	}

	public long getFolowing() {
		return folowing;
	}

	public void setFolowing(long folowing) {
		this.folowing = folowing;
	}

	public boolean isFolowing() {
		return isFolowing;
	}

	public void setFolowing(boolean isFolowing) {
		this.isFolowing = isFolowing;
	}

	public long getBrojSlika() {
		return brojSlika;
	}

	public void setBrojSlika(long brojSlika) {
		this.brojSlika = brojSlika;
	}

}
