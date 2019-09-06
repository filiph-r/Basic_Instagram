package sp_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sp_app.entities.ApplicationUser;
import sp_app.entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Post findByPath(String path);
}
