package sp_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sp_app.entities.Komentar;

@Repository
public interface KomentarRepository extends JpaRepository<Komentar, Long> {
}
