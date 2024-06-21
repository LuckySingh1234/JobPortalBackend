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

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobApplication {
    private String jobId;
    private String userId;
    private String status;

    private static String JOB_APPLICATIONS_SHEET_NAME = "JobApplications";
    private static String JOB_SHEET_NAME = "Jobs";
    private static String COMPANY_SHEET_NAME = "Companies";
    private static String USERS_SHEET_NAME = "Users";
    private static String APPLIED = "APPLIED";

    public static String applyJob(String jobId, String userId) {
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(JOB_APPLICATIONS_SHEET_NAME);
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedJobId = cell.getStringCellValue();

                        cell = row.getCell(1);
                        String storedUserId = cell.getStringCellValue();

                        if (storedJobId.equals(jobId) && storedUserId.equals(userId)) {
                            return "You have already applied for this job";
                        }
                    }

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
            } else {
                return "File does not exist at the specified path";
            }
        } catch (IOException e) {
            return "Error opening Excel file: " + e.getMessage();
        }
        return "true";
    }

    public static List<JobApplicationDetails> fetchAllUsersJobApplications() {
        List<JobApplicationDetails> allUsersJobApplications = new ArrayList<>();
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(JOB_APPLICATIONS_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String jobId = cell.getStringCellValue();
                    cell = row.getCell(1);
                    String userId = cell.getStringCellValue();
                    cell = row.getCell(2);
                    String status = cell.getStringCellValue();
                    String companyName = getCompanyNameByJobId(jobId);
                    String role = getRoleNameByJobId(jobId);
                    String name = getUserFullNameByUserId(userId);
                    allUsersJobApplications.add(new JobApplicationDetails(jobId, userId, companyName, role, name, status));
                }
            }
            return allUsersJobApplications;
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return null;
    }

    private static String getCompanyNameByJobId(String jobId) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(JOB_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String storedJobId = cell.getStringCellValue();

                    if (jobId.equals(storedJobId)) {
                        cell = row.getCell(1);
                        String companyId = cell.getStringCellValue();
                        return getCompanyNameByCompanyId(companyId);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return "No Company Name Found";
    }

    private static String getCompanyNameByCompanyId(String companyId) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(COMPANY_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String storedCompanyId = cell.getStringCellValue();

                    if (companyId.equals(storedCompanyId)) {
                        cell = row.getCell(1);
                        return cell.getStringCellValue();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return "No Company Name Found";
    }

    private static String getRoleNameByJobId(String jobId) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(JOB_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String storedJobId = cell.getStringCellValue();

                    if (jobId.equals(storedJobId)) {
                        cell = row.getCell(2);
                        return cell.getStringCellValue();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return "No Company Name Found";
    }

    private static String getUserFullNameByUserId(String userId) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(USERS_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String storedUserId = cell.getStringCellValue();

                    if (userId.equals(storedUserId)) {
                        String firstName = row.getCell(1).getStringCellValue();
                        String lastName = row.getCell(2).getStringCellValue();
                        return firstName + " " + lastName;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return "No User Name Found";
    }

    public static List<JobApplicationDetails> fetchAllJobApplicationsByUserId(String userId) {
        List<JobApplicationDetails> allUsersJobApplications = new ArrayList<>();
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(JOB_APPLICATIONS_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    Cell cell = row.getCell(0);
                    String jobId = cell.getStringCellValue();
                    cell = row.getCell(1);
                    String storedUserId = cell.getStringCellValue();
                    cell = row.getCell(2);
                    if (!storedUserId.equals(userId)) {
                        continue;
                    }
                    String status = cell.getStringCellValue();
                    String companyName = getCompanyNameByJobId(jobId);
                    String role = getRoleNameByJobId(jobId);
                    String name = getUserFullNameByUserId(userId);
                    allUsersJobApplications.add(new JobApplicationDetails(jobId, userId, companyName, role, name, status));
                }
            }
            return allUsersJobApplications;
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return null;
    }

    public static String updateJobApplicationStatus(String jobId, String userId, String status) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(JOB_APPLICATIONS_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    Cell cell = row.getCell(0);
                    String storedJobId = cell.getStringCellValue();
                    cell = row.getCell(1);
                    String storedUserId = cell.getStringCellValue();
                    if (storedJobId.equals(jobId) && storedUserId.equals(userId)) {
                        cell = row.getCell(2);
                        cell.setCellValue(status);
                        try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                            workbook.write(outputStream);
                            return "true";
                        } catch (IOException e) {
                            System.out.println("Error writing to Excel file: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all users job applications");
            e.printStackTrace();
        }
        return "Failed to update job application status";
    }
}
