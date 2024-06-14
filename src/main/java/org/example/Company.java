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

    public static String addCompany(String companyName, String companyDescription, String location, String imageUrl) {
        List<String> storedCompanyIds = new ArrayList<>();
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(COMPANY_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell = row.getCell(0);
                        String storedCompanyId = cell.getStringCellValue();
                        storedCompanyIds.add(storedCompanyId);
                    }

                    List<Integer> allCompanyIds = new ArrayList<>(storedCompanyIds.stream().map(id -> id.split("#")[1])
                            .map(Integer::parseInt)
                            .toList());
                    allCompanyIds.sort(null);
                    int lastId = allCompanyIds.isEmpty() ? 0 : allCompanyIds.get(allCompanyIds.size() - 1);
                    String id = String.format("%0" + 5 + "d", lastId + 1);
                    String companyId = "C#" + id;

                    Company company = new Company(companyId, companyName, companyDescription, location, imageUrl);
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(company.getCompanyId());
                    cell = row.createCell(1);
                    cell.setCellValue(company.getCompanyName());
                    cell = row.createCell(2);
                    cell.setCellValue(company.getCompanyDescription());
                    cell = row.createCell(3);
                    cell.setCellValue(company.getLocation());
                    cell = row.createCell(4);
                    cell.setCellValue(company.getImageUrl());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(DATABASE).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Company sheet is not available in the excel file";
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
