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
                    if (storedUserIds.contains(userId)) {
                        saveResumeResponse = editResume(resume);
                    } else {
                        saveResumeResponse = addResume(resume);
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

    private static String editResume(Resume resume) {
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(RESUME_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                Row resumeRowToBeUpdated = null;
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String storedUserId = cell.getStringCellValue();
                        if (storedUserId.equals(resume.getUserId())) {
                            resumeRowToBeUpdated = row;
                        }
                    }
                    if (resumeRowToBeUpdated == null) {
                        return "User Id does not exist";
                    }
                    resumeRowToBeUpdated.getCell(1).setCellValue(resume.getName());
                    resumeRowToBeUpdated.getCell(2).setCellValue(resume.getDob());
                    resumeRowToBeUpdated.getCell(3).setCellValue(resume.getEmail());
                    resumeRowToBeUpdated.getCell(4).setCellValue(resume.getInstitutionName());
                    resumeRowToBeUpdated.getCell(5).setCellValue(resume.getDegree());
                    resumeRowToBeUpdated.getCell(6).setCellValue(resume.getYearOfGraduation());
                    resumeRowToBeUpdated.getCell(7).setCellValue(resume.getCompanyName());
                    resumeRowToBeUpdated.getCell(8).setCellValue(resume.getRole());
                    resumeRowToBeUpdated.getCell(9).setCellValue(resume.getDuration());
                    resumeRowToBeUpdated.getCell(10).setCellValue(resume.getResponsibilities());
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

    public static Resume fetchResumeByUserId(String userId) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(RESUME_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String storedUserId = cell.getStringCellValue();
                    if (storedUserId.equals(userId)) {
                        cell = row.getCell(1);
                        String name = cell.getStringCellValue();
                        cell = row.getCell(2);
                        String dob = cell.getStringCellValue();
                        cell = row.getCell(3);
                        String email = cell.getStringCellValue();
                        cell = row.getCell(4);
                        String institutionName = cell.getStringCellValue();
                        cell = row.getCell(5);
                        String degree = cell.getStringCellValue();
                        cell = row.getCell(6);
                        String yearOfGraduation = cell.getStringCellValue();
                        cell = row.getCell(7);
                        String companyName = cell.getStringCellValue();
                        cell = row.getCell(8);
                        String role = cell.getStringCellValue();
                        cell = row.getCell(9);
                        String duration = cell.getStringCellValue();
                        cell = row.getCell(10);
                        String responsibilities = cell.getStringCellValue();
                        return new Resume(userId, name, dob, email, institutionName, degree, yearOfGraduation,
                                companyName, role, duration, responsibilities);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching resume by user id");
            e.printStackTrace();
        }
        return null;
    }
}
