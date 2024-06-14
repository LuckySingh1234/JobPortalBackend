package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.example.Constant.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobApplication {
    private String jobId;
    private String userId;
    private String status;

    private static String JOB_SHEET_NAME = "JobApplications";
    private static String APPLIED = "APPLIED";

    public static String applyJob(String jobId, String userId) {
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(JOB_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    JobApplication jobApplication = new JobApplication(jobId, userId, APPLIED);
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(jobApplication.getJobId());
                    cell = row.createCell(1);
                    cell.setCellValue(jobApplication.getUserId());
                    cell = row.createCell(2);
                    cell.setCellValue(APPLIED);
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(DATABASE).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Job applications sheet is not available in the excel file";
                }
                System.err.println(excelErrors);
            } else {
                return "File does not exist at the specified path";
            }
        } catch (IOException e) {
            return "Error opening Excel file: " + e.getMessage();
        }
        return "true";
    }
}
