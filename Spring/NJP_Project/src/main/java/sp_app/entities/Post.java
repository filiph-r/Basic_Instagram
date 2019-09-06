package sp_app.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import sp_app.entities.holding.Likke;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String opis;
	private String path;

	@OneToMany
	private Collection<Komentar> komentari = new ArrayList<>();

	@OneToMany
	private Collection<Likke> likkes = new ArrayList<>();

	// --------------------------------------------------------------------------------------

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Collection<Komentar> getKomentari() {
		return komentari;
	}

	public void setKomentari(Collection<Komentar> komentari) {
		this.komentari = komentari;
	}

	public Collection<Likke> getLikkes() {
		return likkes;
	}

	public void setLikkes(Collection<Likke> likes) {
		this.likkes = likes;
	}

}
