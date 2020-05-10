package nl.erikkan.turnipbot.repository;

import nl.erikkan.turnipbot.model.TurnipLog;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TurnipLogRepository extends CrudRepository<TurnipLog, Integer> {
    Optional<TurnipLog> findFirstByOrderByTurnipValueDesc();

    Optional<TurnipLog> findFirstByOrderByTurnipValueAsc();
}
