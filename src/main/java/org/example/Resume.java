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
import java.util.ArrayList;
import java.util.List;

import static org.example.Constant.DATABASE;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Resume {
    private String userId;
    private String name;
    private String dob;
    private String email;
    private String institutionName;
    private String degree;
    private String yearOfGraduation;
    private String companyName;
    private String role;
    private String duration;
    private String responsibilities;

    private static final String RESUME_SHEET_NAME = "Resume";

    public static String saveResume(String userId, String name, String dob,
                                    String email, String institutionName, String degree, String yearOfGraduation,
                                    String companyName, String role, String duration, String responsibilities) {
        List<String> storedUserIds = new ArrayList<>();
        String saveResumeResponse;
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(RESUME_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String storedUserId = cell.getStringCellValue();
                        storedUserIds.add(storedUserId);
                    }
                    Resume resume = new Resume(userId, name, dob, email, institutionName, degree,
                            yearOfGraduation, companyName, role, duration, responsibilities);
//                    if (storedUserIds.contains(userId)) {
//                        saveResumeResponse = editResume(resume);
//                    } else {
//                        saveResumeResponse = addResume(resume);
//                    }
                    saveResumeResponse = addResume(resume);
                } else {
                    return "Resume sheet is not available in the excel file";
                }
                System.err.println(excelErrors);
            } else {
                return "File does not exist at the specified path";
            }
        } catch (IOException e) {
            return "Error opening Excel file: " + e.getMessage();
        }
        return saveResumeResponse;
    }

    private static String addResume(Resume resume) {
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(RESUME_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(resume.getUserId());
                    cell = row.createCell(1);
                    cell.setCellValue(resume.getName());
                    cell = row.createCell(2);
                    cell.setCellValue(resume.getDob());
                    cell = row.createCell(3);
                    cell.setCellValue(resume.getEmail());
                    cell = row.createCell(4);
                    cell.setCellValue(resume.getInstitutionName());
                    cell = row.createCell(5);
                    cell.setCellValue(resume.getDegree());
                    cell = row.createCell(6);
                    cell.setCellValue(resume.getYearOfGraduation());
                    cell = row.createCell(7);
                    cell.setCellValue(resume.getCompanyName());
                    cell = row.createCell(8);
                    cell.setCellValue(resume.getRole());
                    cell = row.createCell(9);
                    cell.setCellValue(resume.getDuration());
                    cell = row.createCell(10);
                    cell.setCellValue(resume.getResponsibilities());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(DATABASE).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Resume sheet is not available in the excel file";
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

//    private static String saveResume(Resume resume) {
//
//    }
}
