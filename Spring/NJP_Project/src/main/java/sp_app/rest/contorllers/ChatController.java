package sp_app.rest.contorllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sp_app.entities.ApplicationUser;
import sp_app.entities.Chat;
import sp_app.entities.Komentar;
import sp_app.entities.Message;
import sp_app.entities.holding.MessageHolder;
import sp_app.repository.ApplicationUserRepository;
import sp_app.repository.ChatRepository;
import sp_app.repository.MessageRepository;
import sp_app.utils.UtilsMethods;

@RestController
@RequestMapping("/chat")
public class ChatController {

	private ApplicationUserRepository userRepo;
	private ChatRepository chatRepo;
	private MessageRepository messageRepo;

	@Autowired
	public ChatController(ApplicationUserRepository applicationUserRepository, ChatRepository chatRepo,
			MessageRepository messageRepo) {
		this.userRepo = applicationUserRepository;
		this.chatRepo = chatRepo;
		this.messageRepo = messageRepo;
	}

	@PostMapping("/send")
	public boolean sendMessage(HttpServletRequest req, @RequestBody MessageHolder msgHolder) {
		try {
			ApplicationUser sender = UtilsMethods.getUserFromHeader(req, userRepo);
			ApplicationUser receiver = userRepo.findByUsername(msgHolder.getReceiver());

			Chat chat = chatRepo.findByReceiverAndSender(receiver, sender);
			if (chat == null) {
				chat = chatRepo.findByReceiverAndSender(sender, receiver);
				if (chat == null) {
					chat = new Chat();
					chat.setReceiver(receiver);
					chat.setSender(sender);
				}
			}

			Message msg = new Message();
			msg.setContent(sender.getUsername() + ": " + msgHolder.getContent());
			chat.getMessages().add(msg);

			messageRepo.save(msg);
			chatRepo.save(chat);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	@GetMapping("/get/{username}")
	public List<String> getChat(HttpServletRequest req, @PathVariable String username) {
		try {
			ApplicationUser sender = UtilsMethods.getUserFromHeader(req, userRepo);
			ApplicationUser receiver = userRepo.findByUsername(username);

			Chat chat = chatRepo.findByReceiverAndSender(receiver, sender);
			if (chat == null) {
				chat = chatRepo.findByReceiverAndSender(sender, receiver);
				if (chat == null) {
					chat = new Chat();
					chat.setReceiver(receiver);
					chat.setSender(sender);
				}
			}

			List<String> messages = new ArrayList<>();
			
			ArrayList<Message> list = new ArrayList<>(chat.getMessages());
			Collections.sort(list);
			
			for (Message msg : list) {
				messages.add(msg.getContent());
			}

			return messages;

		} catch (Exception e) {
			return null;
		}
	}

}