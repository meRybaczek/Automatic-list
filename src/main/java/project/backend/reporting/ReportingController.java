package project.backend.reporting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/reporting/access/all")
    public ResponseEntity<InputStreamResource> getLogs(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        InputStreamResource file = new InputStreamResource(reportingService.getLogs(startDate, endDate));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Access Logs.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
    @GetMapping("/reporting/access/{id}")
    public ResponseEntity<InputStreamResource> getLogsByEmployeeId(@PathVariable long id, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        InputStreamResource file = new InputStreamResource(reportingService.getLogsOfGivenEmployee(id, startDate, endDate));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employees.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping("/reporting/users/all")
    public ResponseEntity<InputStreamResource> getAllUsers() {
        InputStreamResource file = new InputStreamResource(reportingService.getAllUsers());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employees.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping("/reporting/users/active")
    public ResponseEntity<InputStreamResource> getActiveUsers() {
        InputStreamResource file = new InputStreamResource(reportingService.getActiveUsers());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Active Employees.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping("/reporting/users/status")
    public ResponseEntity<InputStreamResource> getUsersByStatus(@RequestParam List<String> employeeStatus) {
        InputStreamResource file = new InputStreamResource(reportingService.getEmployeesByRoles(employeeStatus));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Active Employees.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
