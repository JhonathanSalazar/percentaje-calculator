package test.tenpo.percentajecalculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.tenpo.percentajecalculator.model.CallHistory;

@Repository
public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
}