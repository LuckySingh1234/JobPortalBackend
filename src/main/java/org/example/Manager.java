package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Manager {
    private String email;
    private String password;
    private String actions;

    public static Manager login(User user) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            FileInputStream fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet("Managers");
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String email = cell.getStringCellValue();
                    cell = row.getCell(1);
                    String password;
                    if (cell.getCellType() == CellType.STRING) {
                        password = cell.getStringCellValue();
                    } else {
                        password = String.valueOf((int)(cell.getNumericCellValue()));
                    }
                    cell = row.getCell(2);
                    String actions = cell.getStringCellValue();

                    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                        Manager manager = new Manager(email, password, actions);
                        return manager;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while manager login");
            e.printStackTrace();
        }
        return null;
    }

    public static String fetchManagerActions(String reqEmail) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            FileInputStream fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet("Managers");
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String email = cell.getStringCellValue();
                    cell = row.getCell(2);
                    String actions = cell.getStringCellValue();

                    if (reqEmail.equals(email)) {
                        return actions;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while fetching manager actions");
            e.printStackTrace();
        }
        return null;
    }

    public static List<Manager> fetchManagers() {
        List<Manager> managers = new ArrayList<>();
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Managers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        if (cell == null) {
                            excelErrors.append("Manager Email is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String email;
                        if (cell.getCellType() == CellType.STRING) {
                            email = cell.getStringCellValue();
                        } else {
                            email = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                            excelErrors.append("Manager E-mail does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(1);
                        if (cell == null) {
                            excelErrors.append("Manager Password is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String password;
                        if (cell.getCellType() == CellType.STRING) {
                            password = cell.getStringCellValue();
                        } else {
                            password = String.valueOf(cell.getNumericCellValue());
                        }
                        if (password.isEmpty()) {
                            excelErrors.append("Manager Password cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(2);
                        if (cell == null) {
                            excelErrors.append("Manager Action is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String address;
                        if (cell.getCellType() == CellType.STRING) {
                            address = cell.getStringCellValue();
                        } else {
                            address = String.valueOf(cell.getNumericCellValue());
                        }
                        if (address.isEmpty()) {
                            excelErrors.append("Manager Action cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }
                        Manager c = new Manager(email, password, address);
                        managers.add(c);
                    }
                } else {
                    System.out.println("Managers sheet is not available in the excel file");
                }
                System.err.println(excelErrors);
            } else {
                System.out.println("File does not exist at the specified path");
            }
        } catch (IOException e) {
            System.out.println("Error opening Excel file: " + e.getMessage());
        }
        return managers;
    }

    public static Manager fetchManagerByEmail(String email) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Managers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell;
                        String storedManagerEmail;
                        cell = row.getCell(0);
                        if (cell == null) {
                            excelErrors.append("Manager Email is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedManagerEmail = cell.getStringCellValue();
                        } else {
                            storedManagerEmail = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedManagerEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                            excelErrors.append("Manager E-mail does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(1);
                        if (cell == null) {
                            excelErrors.append("Manager Password is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String password;
                        if (cell.getCellType() == CellType.STRING) {
                            password = cell.getStringCellValue();
                        } else {
                            password = String.valueOf(cell.getNumericCellValue());
                        }
                        if (password.isEmpty()) {
                            excelErrors.append("Manager Password cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(2);
                        if (cell == null) {
                            excelErrors.append("Manager Actions is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String actions;
                        if (cell.getCellType() == CellType.STRING) {
                            actions = cell.getStringCellValue();
                        } else {
                            actions = String.valueOf(cell.getNumericCellValue());
                        }
                        if (actions.isEmpty()) {
                            excelErrors.append("Manager Password cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }

                        if (email.equals(storedManagerEmail)) {
                            Manager c = new Manager(email, password, actions);
                            return c;
                        }
                    }
                } else {
                    System.out.println("Managers sheet is not available in the excel file");
                }
                System.err.println(excelErrors);
            } else {
                System.out.println("File does not exist at the specified path");
            }
        } catch (IOException e) {
            System.out.println("Error opening Excel file: " + e.getMessage());
        }
        return null;
    }

    public static String addManager(String email, String password) {
        List<String> emails = new ArrayList<>();
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Managers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        Cell cell;

                        cell = row.getCell(0);
                        String storedEmail;
                        if (cell == null) {
                            excelErrors.append("Manager Email is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedEmail = cell.getStringCellValue();
                        } else {
                            storedEmail = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                            excelErrors.append("Email does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        emails.add(storedEmail);
                    }
                    if (emails.contains(email)) {
                        return "Manager with same email already exists";
                    }
                    if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                        return "Manager E-mail does not follow the pattern";
                    }
                    if (password.isEmpty()) {
                        return "Manager Password cannot be empty";
                    }

                    Manager m = new Manager(email, password, "view,add,remove");
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(m.getEmail());
                    cell = row.createCell(1);
                    cell.setCellValue(m.getPassword());
                    cell = row.createCell(2);
                    cell.setCellValue(m.getActions());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(filePath).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Managers sheet is not available in the excel file";
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

    public static String removeManager(String email) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Managers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    int rowNumToBeRemoved = -1;
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedEmail;
                        if (cell == null) {
                            excelErrors.append("Manager Email is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedEmail = cell.getStringCellValue();
                        } else {
                            storedEmail = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedEmail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                            excelErrors.append("Manager Email does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (storedEmail.equals(email)) {
                            rowNumToBeRemoved = i;
                            break;
                        }
                    }
                    if (rowNumToBeRemoved == -1) {
                        return "Manager Email does not exist";
                    }
                    if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                        return "Manager Email does follow the pattern";
                    }
                    int lastRowNum = sheet.getLastRowNum();
                    // Check if the row to be deleted exists in the sheet
                    if (rowNumToBeRemoved >= 0 && rowNumToBeRemoved < lastRowNum) {
                        // Shift rows up
                        sheet.shiftRows(rowNumToBeRemoved + 1, lastRowNum, -1);
                    }

                    // If the row to be deleted is the last row, simply remove it
                    if (rowNumToBeRemoved == lastRowNum) {
                        Row removingRow = sheet.getRow(rowNumToBeRemoved);
                        if (removingRow != null) {
                            sheet.removeRow(removingRow);
                        }
                    }
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(filePath).toAbsolutePath());
                    } catch (IOException e) {
                        return "Error creating Excel file: " + e.getMessage();
                    }
                } else {
                    return "Manager sheet is not available in the excel file";
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
