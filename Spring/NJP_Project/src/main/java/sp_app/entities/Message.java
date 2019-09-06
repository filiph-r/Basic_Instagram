package sp_app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message implements Comparable<Message> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	@Override
	public int compareTo(Message m) {
		return (int) (this.id - m.getId());
	}

}
