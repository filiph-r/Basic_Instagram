package sp_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sp_app.entities.holding.Likke;

@Repository
public interface LikeRepository extends JpaRepository<Likke, Long> {
}
