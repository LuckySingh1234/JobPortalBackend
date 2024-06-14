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
public class Company {
    private String companyId;
    private String companyName;
    private String companyDescription;
    private String location;
    private String imageUrl;

    private static String COMPANY_SHEET_NAME = "Companies";

    public static List<Company> fetchAllCompanies() {
        List<Company> allCompanies = new ArrayList<>();
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(COMPANY_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String companyId = cell.getStringCellValue();
                    cell = row.getCell(1);
                    String companyName = cell.getStringCellValue();
                    cell = row.getCell(2);
                    String companyDescription = cell.getStringCellValue();
                    cell = row.getCell(3);
                    String location = cell.getStringCellValue();
                    cell = row.getCell(4);
                    String imageUrl = cell.getStringCellValue();

                    allCompanies.add(new Company(companyId, companyName, companyDescription, location, imageUrl));
                }
            }
            return allCompanies;
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching all companies");
            e.printStackTrace();
        }
        return null;
    }

    public static Company fetchCompanyById(String companyId) {
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
                    if (storedCompanyId.equals(companyId)) {
                        cell = row.getCell(1);
                        String companyName = cell.getStringCellValue();
                        cell = row.getCell(2);
                        String companyDescription = cell.getStringCellValue();
                        cell = row.getCell(3);
                        String location = cell.getStringCellValue();
                        cell = row.getCell(4);
                        String imageUrl = cell.getStringCellValue();
                        return new Company(companyId, companyName, companyDescription, location, imageUrl);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching company by id");
            e.printStackTrace();
        }
        return null;
    }
}
