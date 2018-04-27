package repositories;
import models.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by sinaastani on 4/26/18.
 */
@Repository
public interface GameRepo extends CrudRepository<Game, Long>{
    List<Game> findAll();
    List<Game> findById(String id);
}
