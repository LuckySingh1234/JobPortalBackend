package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.example.Constant.DATABASE;

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

    private static String JOB_SHEET_NAME = "Jobs";

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
                        allJobs.add(new Job(jobId, companyId, roleTitle, roleDescription, jobPackage, requiredSkills, requiredExperience));
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
}
