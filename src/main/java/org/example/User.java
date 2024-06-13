package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

import static org.example.Constant.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String password;
    private String interests;

    private static String USERS_SHEET_NAME = "Users";

    public static User login(String email, String password) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet("Users");
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(4);
                    String storedEmail = cell.getStringCellValue();

                    cell = row.getCell(5);
                    String storedPassword;
                    if (cell.getCellType() == CellType.STRING) {
                        storedPassword = cell.getStringCellValue();
                    } else {
                        storedPassword = String.valueOf((int)(cell.getNumericCellValue()));
                    }

                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
                        cell = row.getCell(0);
                        String userId = cell.getStringCellValue();

                        cell = row.getCell(3);
                        String mobile;
                        if (cell.getCellType() == CellType.STRING) {
                            mobile = cell.getStringCellValue();
                        } else {
                            mobile = String.valueOf((long)(cell.getNumericCellValue()));
                        }

                        return User.builder()
                                .userId(userId)
                                .mobile(mobile)
                                .email(email)
                                .build();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while manager login");
            e.printStackTrace();
        }
        return null;
    }

    public static String addUser(String firstName, String lastName, String mobile, String email, String password, String interests) {
        List<String> storedEmails = new ArrayList<>();
        List<String> storedUserIds = new ArrayList<>();
        try {
            Workbook workbook;
            if (Files.exists(Paths.get(DATABASE))) {
                FileInputStream fis = new FileInputStream(DATABASE);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet(USERS_SHEET_NAME);
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedUserId;
                        if (cell == null) {
                            excelErrors.append("User Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedUserId = cell.getStringCellValue();
                        } else {
                            storedUserId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedUserId.matches(USER_ID_REGEX)) {
                            excelErrors.append("User Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        storedUserIds.add(storedUserId);

                        cell = row.getCell(4);
                        String storedEmail;
                        if (cell == null) {
                            excelErrors.append("User Email is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedEmail = cell.getStringCellValue();
                        } else {
                            storedEmail = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedEmail.matches(EMAIL_REGEX)) {
                            excelErrors.append("Email does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        storedEmails.add(storedEmail);
                    }
                    if (storedEmails.contains(email)) {
                        return "User with same email already exists";
                    }

                    List<Integer> allUserId = new ArrayList<>(storedUserIds.stream().map(id -> id.split("#")[1])
                            .map(Integer::parseInt)
                            .toList());
                    allUserId.sort(null);
                    int lastId = allUserId.isEmpty() ? 0 : allUserId.get(allUserId.size() - 1);
                    String id = String.format("%0" + 5 + "d", lastId + 1);
                    String newCustomerId = "U#" + id;

                    if (!firstName.matches(NAME_REGEX)) {
                        return "User First Name does not follow the pattern";
                    }
                    if (!lastName.matches(NAME_REGEX)) {
                        return "User Last Name does not follow the pattern";
                    }
                    if (!mobile.matches(MOBILE_REGEX)) {
                        return "User Mobile does not follow the pattern";
                    }
                    if (!email.matches(EMAIL_REGEX)) {
                        return "User E-mail does not follow the pattern";
                    }
                    if (password.isEmpty()) {
                        return "User Password cannot be empty";
                    }

                    User u = new User(newCustomerId, firstName, lastName, mobile, email, password, interests);
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(u.getUserId());
                    cell = row.createCell(1);
                    cell.setCellValue(u.getFirstName());
                    cell = row.createCell(2);
                    cell.setCellValue(u.getLastName());
                    cell = row.createCell(3);
                    cell.setCellValue(u.getMobile());
                    cell = row.createCell(4);
                    cell.setCellValue(u.getEmail());
                    cell = row.createCell(5);
                    cell.setCellValue(u.getPassword());
                    cell = row.createCell(6);
                    cell.setCellValue(u.getInterests());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(DATABASE)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(DATABASE).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Users sheet is not available in the excel file";
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

    public static User fetchCustomerById(String customerId) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Customers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedCustomerId;
                        if (cell == null) {
                            excelErrors.append("Customer Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedCustomerId = cell.getStringCellValue();
                        } else {
                            storedCustomerId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedCustomerId.matches("^C#[0-9]{5}$")) {
                            excelErrors.append("Customer Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(1);
                        if (cell == null) {
                            excelErrors.append("Customer Full Name is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String fullName;
                        if (cell.getCellType() == CellType.STRING) {
                            fullName = cell.getStringCellValue();
                        } else {
                            fullName = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!fullName.matches("^[A-Za-z\s]{1,20}$")) {
                            excelErrors.append("Customer Full Name does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(2);
                        if (cell == null) {
                            excelErrors.append("Customer Mobile Number is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String mobile;
                        if (cell.getCellType() == CellType.STRING) {
                            mobile = cell.getStringCellValue();
                        } else {
                            mobile = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!mobile.matches("^[0-9]{10}$")) {
                            excelErrors.append("Customer Mobile does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(3);
                        if (cell == null) {
                            excelErrors.append("Customer Email is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String email;
                        if (cell.getCellType() == CellType.STRING) {
                            email = cell.getStringCellValue();
                        } else {
                            email = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                            excelErrors.append("Customer E-mail does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(4);
                        if (cell == null) {
                            excelErrors.append("Customer Password is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String password;
                        if (cell.getCellType() == CellType.STRING) {
                            password = cell.getStringCellValue();
                        } else {
                            password = String.valueOf(cell.getNumericCellValue());
                        }
                        if (password.isEmpty()) {
                            excelErrors.append("Customer Password cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(5);
                        if (cell == null) {
                            excelErrors.append("Customer Password is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String address;
                        if (cell.getCellType() == CellType.STRING) {
                            address = cell.getStringCellValue();
                        } else {
                            address = String.valueOf(cell.getNumericCellValue());
                        }
                        if (address.isEmpty()) {
                            excelErrors.append("Customer Address cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(6);
                        if (cell == null) {
                            excelErrors.append("Customer Status is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String status;
                        if (cell.getCellType() == CellType.STRING) {
                            status = cell.getStringCellValue();
                        } else {
                            status = String.valueOf(cell.getNumericCellValue());
                        }
                        if (status.isEmpty()) {
                            excelErrors.append("Customer Status cannot be empty: ").append(i + 1).append("\n");
                            continue;
                        }

                        if (customerId.equals(storedCustomerId) && status.equals("ACTIVE")) {
                            User c = new User(customerId, fullName, mobile, email, password, address, "ACTIVE");
                            return c;
                        }
                    }
                } else {
                    System.out.println("Customers sheet is not available in the excel file");
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
}
