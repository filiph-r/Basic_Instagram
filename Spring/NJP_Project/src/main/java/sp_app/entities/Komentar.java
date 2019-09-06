package sp_app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Komentar implements Comparable<Komentar> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String value;

	// --------------------------------------------------------------------------------------

	public String getValue() {
		return value;
	}

	public long getId() {
		return id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(Komentar k) {

		return (int) (this.id - k.getId());

	}

}
