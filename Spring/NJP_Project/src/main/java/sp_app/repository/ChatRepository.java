package sp_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sp_app.entities.ApplicationUser;
import sp_app.entities.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
	Chat findByReceiverAndSender(ApplicationUser receiver, ApplicationUser sender);

}
