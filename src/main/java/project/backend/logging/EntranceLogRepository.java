package project.backend.logging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EntranceLogRepository extends JpaRepository<EntranceLog, Long> {

    @Query(value = "SELECT el FROM EntranceLog el WHERE el.employeeRfid = :rfid AND el.gateAccessStatus != 'REJECTED' ORDER BY el.date DESC")
    List<EntranceLog> findEntranceLogByRfid(@Param("rfid") String rfid);

    @Query(value = "SELECT DISTINCT el.employeeRfid from EntranceLog el WHERE el.gateAccessStatus = 'IN' AND el.date BETWEEN :startDate AND :endDate ORDER BY el.date LIMIT 1")
    List<String> findActiveEmployeesRfid(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
