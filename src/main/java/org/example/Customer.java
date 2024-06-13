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
public class Customer {
    private String customerId;
    private String fullName;
    private String mobile;
    private String email;
    private String password;
    private String address;
    private String status;

    public static Customer login(User user) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            FileInputStream fis = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet("Customers");
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(3);
                    String email = cell.getStringCellValue();

                    cell = row.getCell(4);
                    String password;
                    if (cell.getCellType() == CellType.STRING) {
                        password = cell.getStringCellValue();
                    } else {
                        password = String.valueOf((int)(cell.getNumericCellValue()));
                    }

                    cell = row.getCell(6);
                    String status = cell.getStringCellValue();

                    if (user.getEmail().equals(email) && user.getPassword().equals(password) && status.equals("ACTIVE")) {
                        cell = row.getCell(0);
                        String customerId = cell.getStringCellValue();

                        cell = row.getCell(1);
                        String fullName = cell.getStringCellValue();

                        cell = row.getCell(2);
                        String mobile = cell.getStringCellValue();

                        cell = row.getCell(5);
                        String address = cell.getStringCellValue();

                        Customer customer = new Customer(customerId, fullName, mobile, email, password, address, status);
                        return customer;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while manager login");
            e.printStackTrace();
        }
        return null;
    }

    public static List<Customer> fetchCustomers() {
        List<Customer> customers = new ArrayList<>();
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
                        String customerId;
                        if (cell == null) {
                            excelErrors.append("Customer Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            customerId = cell.getStringCellValue();
                        } else {
                            customerId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!customerId.matches("^C#[0-9]{5}$")) {
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
                            excelErrors.append("Customer Address is null at Row: ").append(i + 1).append("\n");
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
                        if (status.equals("ACTIVE")) {
                            Customer c = new Customer(customerId, fullName, mobile, email, password, address, "ACTIVE");
                            customers.add(c);
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
        return customers;
    }

    public static Customer fetchCustomerById(String customerId) {
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
                            Customer c = new Customer(customerId, fullName, mobile, email, password, address, "ACTIVE");
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

    public static String addCustomer(String fullName, String mobile, String email, String password, String address) {
        List<String> emails = new ArrayList<>();
        List<String> customerIds = new ArrayList<>();
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
                        customerIds.add(storedCustomerId);

                        cell = row.getCell(3);
                        String storedEmail;
                        if (cell == null) {
                            excelErrors.append("Customer Email is null at Row: ").append(i + 1).append("\n");
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
                        return "Customer with same email already exists";
                    }

                    List<Integer> allCustomerId = new ArrayList<>(customerIds.stream().map(id -> id.split("#")[1])
                            .map(Integer::parseInt)
                            .toList());
                    allCustomerId.sort(null);
                    int lastId = allCustomerId.isEmpty() ? 0 : allCustomerId.get(allCustomerId.size() - 1);
                    String id = String.format("%0" + 5 + "d", lastId + 1);
                    String newCustomerId = "C#" + id;

                    if (!fullName.matches("^[A-Za-z\s]{1,20}$")) {
                        return "Customer Name does not follow the pattern";
                    }
                    if (!mobile.matches("^[0-9]{10}$")) {
                        return "Customer Mobile does not follow the pattern";
                    }
                    if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                        return "Customer E-mail does not follow the pattern";
                    }
                    if (password.isEmpty()) {
                        return "Customer Password cannot be empty";
                    }
                    if (address.isEmpty()) {
                        return "Customer Address cannot be empty";
                    }

                    Customer c = new Customer(newCustomerId, fullName, mobile, email, password, address, "ACTIVE");
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(c.getCustomerId());
                    cell = row.createCell(1);
                    cell.setCellValue(c.getFullName());
                    cell = row.createCell(2);
                    cell.setCellValue(c.getMobile());
                    cell = row.createCell(3);
                    cell.setCellValue(c.getEmail());
                    cell = row.createCell(4);
                    cell.setCellValue(c.getPassword());
                    cell = row.createCell(5);
                    cell.setCellValue(c.getAddress());
                    cell = row.createCell(6);
                    cell.setCellValue(c.getStatus());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(filePath).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Customers sheet is not available in the excel file";
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

    public static String editCustomer(String customerId, String fullName, String mobile, String email, String password, String address) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Customers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    int rowNumToBeEdited = -1;
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
                        if (storedCustomerId.equals(customerId)) {
                            rowNumToBeEdited = i;
                            break;
                        }
                    }
                    if (rowNumToBeEdited == -1) {
                        return "Customer Id does not exist";
                    }
                    if (!customerId.matches("^C#[0-9]{5}$")) {
                        return "Customer Id does follow the pattern";
                    }
                    if (!fullName.matches("^[A-Za-z\s]{1,20}$")) {
                        return "Customer Name does follow the pattern";
                    }
                    if (!mobile.matches("^[0-9]{10}$")) {
                        return "Customer Mobile does follow the pattern";
                    }
                    if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                        return "Customer email does follow the pattern";
                    }
                    if (password.isEmpty()) {
                        return "Customer Password cannot be empty";
                    }
                    if (address.isEmpty()) {
                        return "Customer Address cannot be empty";
                    }

                    Row row = sheet.createRow(rowNumToBeEdited);
                    Customer c = new Customer(customerId, fullName, mobile, email, password, address, "ACTIVE");
                    Cell cell = row.createCell(0);
                    cell.setCellValue(c.getCustomerId());
                    cell = row.createCell(1);
                    cell.setCellValue(c.getFullName());
                    cell = row.createCell(2);
                    cell.setCellValue(c.getMobile());
                    cell = row.createCell(3);
                    cell.setCellValue(c.getEmail());
                    cell = row.createCell(4);
                    cell.setCellValue(c.getPassword());
                    cell = row.createCell(5);
                    cell.setCellValue(c.getAddress());
                    cell = row.createCell(6);
                    cell.setCellValue(c.getStatus());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(filePath).toAbsolutePath());
                    } catch (IOException e) {
                        return "Error creating Excel file: " + e.getMessage();
                    }
                } else {
                    return "Customers sheet is not available in the excel file";
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

    public static String removeCustomer(String customerId) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Customers");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    int rowNumToBeRemoved = -1;
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
                        if (storedCustomerId.equals(customerId)) {
                            rowNumToBeRemoved = i;
                            break;
                        }
                    }
                    if (rowNumToBeRemoved == -1) {
                        return "Customer Id does not exist";
                    }
                    if (!customerId.matches("^C#[0-9]{5}$")) {
                        return "Customer Id does follow the pattern";
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
                    return "Customer sheet is not available in the excel file";
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
