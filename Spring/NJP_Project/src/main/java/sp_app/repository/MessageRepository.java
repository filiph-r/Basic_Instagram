package sp_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sp_app.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
