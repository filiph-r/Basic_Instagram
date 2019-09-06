package sp_app.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@OneToOne
	private ApplicationUser sender;
	@OneToOne
	private ApplicationUser receiver;

	@OneToMany
	private Collection<Message> messages = new ArrayList<>();

	public long getId() {
		return id;
	}

	public ApplicationUser getSender() {
		return sender;
	}

	public void setSender(ApplicationUser sender) {
		this.sender = sender;
	}

	public ApplicationUser getReceiver() {
		return receiver;
	}

	public void setReceiver(ApplicationUser receiver) {
		this.receiver = receiver;
	}

	public Collection<Message> getMessages() {
		return messages;
	}

	public void setMessages(Collection<Message> messages) {
		this.messages = messages;
	}

}
