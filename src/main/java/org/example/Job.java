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

import static org.example.Constant.*;
import static org.example.Constant.EMAIL_REGEX;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {
    private String jobId;
    private String companyId;
    private String roleTitle;
    private String roleDescription;
    private String jobPackage;
    private String requiredSkills;
    private String requiredExperience;
    private String status;

    private static String JOB_SHEET_NAME = "Jobs";
    private static String ACTIVE = "ACTIVE";
    private static String DELETED = "DELETED";

    public static List<Job> fetchAllJobByCompanyId(String companyId) {
        List<Job> allJobs = new ArrayList<>();
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(JOB_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(1);
                    String storedCompanyId = cell.getStringCellValue();
                    if (storedCompanyId.equals(companyId)) {
                        cell = row.getCell(0);
                        String jobId = cell.getStringCellValue();
                        cell = row.getCell(2);
                        String roleTitle = cell.getStringCellValue();
                        cell = row.getCell(3);
                        String roleDescription = cell.getStringCellValue();
                        cell = row.getCell(4);
                        String jobPackage = cell.getStringCellValue();
                        cell = row.getCell(5);
                        String requiredSkills = cell.getStringCellValue();
                        cell = row.getCell(6);
                        String requiredExperience = cell.getStringCellValue();
                        cell = row.getCell(7);
                        String status = cell.getStringCellValue();
                        if (ACTIVE.equals(status)) {
                            allJobs.add(new Job(jobId, companyId, roleTitle, roleDescription, jobPackage, requiredSkills, requiredExperience, status));
                        }
                    }
                }
            }
            return allJobs;
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all jobs by company id");
            e.printStackTrace();
        }
        return null;
    }

    public static String addJob(String companyId, String jobTitle, String jobDescription, String jobPackage, String experience, String skills) {
        List<String> storedJobIds = new ArrayList<>();
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(JOB_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String storedJobId = cell.getStringCellValue();
                        storedJobIds.add(storedJobId);
                    }

                    List<Integer> allJobIds = new ArrayList<>(storedJobIds.stream().map(id -> id.split("#")[1])
                            .map(Integer::parseInt)
                            .toList());
                    allJobIds.sort(null);
                    int lastId = allJobIds.isEmpty() ? 0 : allJobIds.get(allJobIds.size() - 1);
                    String id = String.format("%0" + 5 + "d", lastId + 1);
                    String jobId = "J#" + id;

                    Job job = new Job(jobId, companyId, jobTitle, jobDescription, jobPackage, experience, skills, ACTIVE);
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(job.getJobId());
                    cell = row.createCell(1);
                    cell.setCellValue(job.getCompanyId());
                    cell = row.createCell(2);
                    cell.setCellValue(job.getRoleTitle());
                    cell = row.createCell(3);
                    cell.setCellValue(job.getRoleDescription());
                    cell = row.createCell(4);
                    cell.setCellValue(job.getJobPackage());
                    cell = row.createCell(5);
                    cell.setCellValue(job.getRequiredExperience());
                    cell = row.createCell(6);
                    cell.setCellValue(job.getRequiredSkills());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(DATABASE).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Jobs sheet is not available in the excel file";
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

    public static String removeJob(String jobId) {
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(JOB_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                Row jobRowToBeDeleted = null;
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String storedJobId = cell.getStringCellValue();
                        if (storedJobId.equals(jobId)) {
                            jobRowToBeDeleted = row;
                        }
                    }
                    if (jobRowToBeDeleted == null) {
                        return "Job Id does not exist";
                    }
                    Cell cell = jobRowToBeDeleted.getCell(7);
                    cell.setCellValue(DELETED);
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(DATABASE).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Jobs sheet is not available in the excel file";
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
