package sp_app.entities.holding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sp_app.entities.Komentar;

public class PostInfo {

	private String owner;
	private int postnum;
	private String opis;
	private List<Komentar> komentari = new ArrayList<>();
	private boolean isLiked;
	private long nummberOfLikes;
	private String file;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getPostnum() {
		return postnum;
	}

	public void setPostnum(int postnum) {
		this.postnum = postnum;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public List<Komentar> getKomentari() {
		return komentari;
	}

	public void setKomentari(List<Komentar> komentari) {
		this.komentari = komentari;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}

	public long getNummberOfLikes() {
		return nummberOfLikes;
	}

	public void setNummberOfLikes(long nummberOfLikes) {
		this.nummberOfLikes = nummberOfLikes;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
