package project.backend.reporting;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import project.backend.employee.Employee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {

    public static ByteArrayInputStream employeesToExcel(List<Employee> employees) {
        String fileName = "Employees";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(fileName);
            String[] Headers = { "Id", "First name", "Last name", "Rfid", "Status"};

            // Header
            createHeader(sheet, Headers);

            int rowIdx = 1;
            for (Employee employee : employees) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(employee.getId());
                row.createCell(1).setCellValue(employee.getFirstName());
                row.createCell(2).setCellValue(employee.getLastName());
                row.createCell(3).setCellValue(employee.getRfid());
                row.createCell(4).setCellValue(employee.getStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream EmployeeLogsToExcel(List<ReportingDTO> logs) {
        String fileName = "EmployeeLogs";

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(fileName);
            String[] Headers = { "Id", "First name", "Last name", "Rfid", "Status", "Date", "GATE_ACCESS_STATUS"};

            // Header
            createHeader(sheet, Headers);

            int rowIdx = 1;
            for (ReportingDTO entranceLog : logs) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(entranceLog.id());
                row.createCell(1).setCellValue(entranceLog.firstName());
                row.createCell(2).setCellValue(entranceLog.lastName());
                row.createCell(3).setCellValue(entranceLog.rfid());
                row.createCell(4).setCellValue(entranceLog.status().name());
                row.createCell(5).setCellValue(entranceLog.date().toString().replace("T", " "));
                row.createCell(6).setCellValue(entranceLog.gateAccessStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    private static void createHeader(Sheet sheet, String[] Headers) {
        Row headerRow = sheet.createRow(0);

        for (int col = 0; col < Headers.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(Headers[col]);
        }
    }
}
