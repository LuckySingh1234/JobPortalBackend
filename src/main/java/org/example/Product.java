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
public class Product {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
    private String category;
    private String status;
    private String imageUrl;
    private static List<String> categories = List.of("shirt", "tshirt", "pant", "saree", "chudi");

    public static List<Product> fetchProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Products");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String productId;
                        if (cell == null) {
                            excelErrors.append("Product Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            productId = cell.getStringCellValue();
                        } else {
                            productId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!productId.matches("^P#[0-9]{5}$")) {
                            excelErrors.append("Product Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(1);
                        if (cell == null) {
                            excelErrors.append("Product Name is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String name;
                        if (cell.getCellType() == CellType.STRING) {
                            name = cell.getStringCellValue();
                        } else {
                            name = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!name.matches("^[A-Za-z0-9\s-]{1,20}$")) {
                            excelErrors.append("Product Name does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(2);
                        if (cell == null) {
                            excelErrors.append("Product Price is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Double price;
                        if (cell.getCellType() == CellType.STRING) {
                            excelErrors.append("Product Price has string value at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            price = cell.getNumericCellValue();
                            if (price <= 0) {
                                excelErrors.append("Product Price is less than or equal to zero at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                        } catch (Exception e) {
                            excelErrors.append("Incorrect Price at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(3);
                        if (cell == null) {
                            excelErrors.append("Product Stock Quantity is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Integer qty;
                        if (cell.getCellType() == CellType.STRING) {
                            excelErrors.append("Product Stock Quantity has string value at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            Double doubleQty = cell.getNumericCellValue();
                            qty = doubleQty.intValue();
                            if (doubleQty.compareTo(qty.doubleValue()) != 0) {
                                excelErrors.append("Stock Quantity is fractional at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                            if (qty < 0) {
                                excelErrors.append("Stock Quantity is negative at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                        } catch (Exception e) {
                            excelErrors.append("Incorrect Stock Quantity at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(4);
                        String storedCategory;
                        if (cell == null) {
                            excelErrors.append("Category is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            storedCategory = cell.getStringCellValue();
                        } catch (Exception e) {
                            excelErrors.append("Incorrect category at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (!storedCategory.equals(category) && !category.equals("ALL")) {
                            continue;
                        }

                        cell = row.getCell(5);
                        String status;
                        if (cell == null) {
                            excelErrors.append("Status is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            status = cell.getStringCellValue();
                        } catch (Exception e) {
                            excelErrors.append("Incorrect status at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (!status.equals("ACTIVE")) {
                            continue;
                        }

                        cell = row.getCell(6);
                        String imageUrl;
                        if (cell == null) {
                            excelErrors.append("Image Url is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            imageUrl = cell.getStringCellValue();
                        } catch (Exception e) {
                            excelErrors.append("Incorrect image url at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Product p = new Product(productId, name, price, qty, storedCategory, status, imageUrl);
                        products.add(p);
                    }
                } else {
                    System.out.println("Products sheet is not available in the excel file");
                }
                System.err.println(excelErrors);
            } else {
                System.out.println("File does not exist at the specified path");
            }
        } catch (IOException e) {
            System.out.println("Error opening Excel file: " + e.getMessage());
        }
        return products;
    }

    public static Product fetchProductById(String productId) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Products");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedProductId;
                        if (cell == null) {
                            excelErrors.append("Product Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedProductId = cell.getStringCellValue();
                        } else {
                            storedProductId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedProductId.matches("^P#[0-9]{5}$")) {
                            excelErrors.append("Product Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(1);
                        if (cell == null) {
                            excelErrors.append("Product Name is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        String name;
                        if (cell.getCellType() == CellType.STRING) {
                            name = cell.getStringCellValue();
                        } else {
                            name = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!name.matches("^[A-Za-z0-9\s-]{1,20}$")) {
                            excelErrors.append("Product Name does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(2);
                        if (cell == null) {
                            excelErrors.append("Product Price is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Double price;
                        if (cell.getCellType() == CellType.STRING) {
                            excelErrors.append("Product Price has string value at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            price = cell.getNumericCellValue();
                            if (price <= 0) {
                                excelErrors.append("Product Price is less than or equal to zero at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                        } catch (Exception e) {
                            excelErrors.append("Incorrect Price at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(3);
                        if (cell == null) {
                            excelErrors.append("Product Stock Quantity is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        Integer qty;
                        if (cell.getCellType() == CellType.STRING) {
                            excelErrors.append("Product Stock Quantity has string value at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            Double doubleQty = cell.getNumericCellValue();
                            qty = doubleQty.intValue();
                            if (doubleQty.compareTo(qty.doubleValue()) != 0) {
                                excelErrors.append("Stock Quantity is fractional at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                            if (qty < 0) {
                                excelErrors.append("Stock Quantity is negative at Row: ").append(i + 1).append("\n");
                                continue;
                            }
                        } catch (Exception e) {
                            excelErrors.append("Incorrect Stock Quantity at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(4);
                        String storedCategory;
                        if (cell == null) {
                            excelErrors.append("Category is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            storedCategory = cell.getStringCellValue();
                        } catch (Exception e) {
                            excelErrors.append("Incorrect category at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        cell = row.getCell(5);
                        String status;
                        if (cell == null) {
                            excelErrors.append("Status is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            status = cell.getStringCellValue();
                        } catch (Exception e) {
                            excelErrors.append("Incorrect status at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (!status.equals("ACTIVE")) {
                            continue;
                        }

                        cell = row.getCell(6);
                        String imageUrl;
                        if (cell == null) {
                            excelErrors.append("Image Url is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        try {
                            imageUrl = cell.getStringCellValue();
                        } catch (Exception e) {
                            excelErrors.append("Incorrect image url at Row: ").append(i + 1).append("\n");
                            continue;
                        }

                        if (productId.equals(storedProductId)) {
                            Product p = new Product(productId, name, price, qty, storedCategory, status, imageUrl);
                            return p;
                        }
                    }
                } else {
                    System.out.println("Products sheet is not available in the excel file");
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

    public static String addProduct(String name, String price, String stockQuantity, String category, String imageUrl) {
        List<String> productIds = new ArrayList<>();
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Products");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedProductId;
                        if (cell == null) {
                            excelErrors.append("Product Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedProductId = cell.getStringCellValue();
                        } else {
                            storedProductId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedProductId.matches("^P#[0-9A-Z]{5}$")) {
                            excelErrors.append("Product Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        productIds.add(storedProductId);
                    }
                    List<Integer> allProductId = new ArrayList<>(productIds.stream().map(id -> id.split("#")[1])
                            .map(Integer::parseInt)
                            .toList());
                    allProductId.sort(null);
                    int lastId = allProductId.isEmpty() ? 0 : allProductId.get(allProductId.size() - 1);
                    String id = String.format("%0" + 5 + "d", lastId + 1);
                    String newProductId = "P#" + id;
                    if (!name.matches("^[A-Za-z0-9\s-]{1,20}$")) {
                        return "Product Name does follow the pattern";
                    }
                    try {
                        Double priceDouble = Double.parseDouble(price);
                        if (priceDouble < 1) {
                            return "Product Price cannot be negative or zero";
                        }
                    } catch (Exception e) {
                        return "Product Price should be a decimal value";
                    }
                    try {
                        Integer stockQuantityInt = Integer.parseInt(stockQuantity);
                        if (stockQuantityInt <= 0) {
                            return "Product Stock Quantity cannot be negative or zero";
                        }
                    } catch (Exception e) {
                        return "Product Stock Quantity should be an integer value";
                    }
                    if (!categories.contains(category)) {
                        return "Product Category is invalid";
                    }

                    Product p = new Product(newProductId, name, Double.parseDouble(price),
                            Integer.parseInt(stockQuantity), category, "ACTIVE", imageUrl);
                    int rowNum = sheet.getLastRowNum() + 1;
                    Row row = sheet.createRow(rowNum);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(p.getProductId());
                    cell = row.createCell(1);
                    cell.setCellValue(p.getName());
                    cell = row.createCell(2);
                    cell.setCellValue(p.getPrice());
                    cell = row.createCell(3);
                    cell.setCellValue(p.getStockQuantity());
                    cell = row.createCell(4);
                    cell.setCellValue(p.getCategory());
                    cell = row.createCell(5);
                    cell.setCellValue(p.getStatus());
                    cell = row.createCell(6);
                    cell.setCellValue(p.getImageUrl());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(filePath).toAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating Excel file: " + e.getMessage());
                    }
                } else {
                    return "Products sheet is not available in the excel file";
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

    public static String editProduct(String productId, String name, String price, String stockQuantity, String category, String imageUrl) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Products");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    int rowNumToBeEdited = -1;
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedProductId;
                        if (cell == null) {
                            excelErrors.append("Product Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedProductId = cell.getStringCellValue();
                        } else {
                            storedProductId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedProductId.matches("^P#[0-9]{5}$")) {
                            excelErrors.append("Product Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (storedProductId.equals(productId)) {
                            rowNumToBeEdited = i;
                            break;
                        }
                    }
                    if (rowNumToBeEdited == -1) {
                        return "Product Id does not exist";
                    }
                    if (!productId.matches("^P#[0-9]{5}$")) {
                        return "Product Id does follow the pattern";
                    }
                    if (!name.matches("^[A-Za-z0-9\s-]{1,20}$")) {
                        return "Product Name does follow the pattern";
                    }
                    try {
                        Double priceDouble = Double.parseDouble(price);
                        if (priceDouble < 1) {
                            return "Product Price cannot be negative or zero";
                        }
                    } catch (Exception e) {
                        return "Product Price should be a decimal value";
                    }
                    try {
                        Integer stockQuantityInt = Integer.parseInt(stockQuantity);
                        if (stockQuantityInt <= 0) {
                            return "Product Stock Quantity cannot be negative or zero";
                        }
                    } catch (Exception e) {
                        return "Product Stock Quantity should be an integer value";
                    }
                    if (!categories.contains(category)) {
                        return "Product Category is invalid";
                    }
                    Row row = sheet.createRow(rowNumToBeEdited);
                    Product p = new Product(productId, name, Double.parseDouble(price),
                            Integer.parseInt(stockQuantity), category, "ACTIVE", imageUrl);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(p.getProductId());
                    cell = row.createCell(1);
                    cell.setCellValue(p.getName());
                    cell = row.createCell(2);
                    cell.setCellValue(p.getPrice());
                    cell = row.createCell(3);
                    cell.setCellValue(p.getStockQuantity());
                    cell = row.createCell(4);
                    cell.setCellValue(p.getCategory());
                    cell = row.createCell(5);
                    cell.setCellValue(p.getStatus());
                    cell = row.createCell(6);
                    cell.setCellValue(p.getImageUrl());
                    // Write to Excel file
                    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        workbook.write(outputStream);
                        System.out.println("Excel file created successfully at the below location");
                        System.out.println(Paths.get(filePath).toAbsolutePath());
                    } catch (IOException e) {
                        return "Error creating Excel file: " + e.getMessage();
                    }
                } else {
                    return "Products sheet is not available in the excel file";
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

    public static String removeProduct(String productId) {
        try {
            String filePath = "F:/OnlineStoreAppBackendAPI/data/OnlineStoreAppDatabase.xlsx";
            Workbook workbook;
            if (Files.exists(Paths.get(filePath))) {
                FileInputStream fis = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheet("Products");
                StringBuilder excelErrors = new StringBuilder();
                if (sheet != null) {
                    int rowNumToBeRemoved = -1;
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        Cell cell = row.getCell(0);
                        String storedProductId;
                        if (cell == null) {
                            excelErrors.append("Product Id is null at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING) {
                            storedProductId = cell.getStringCellValue();
                        } else {
                            storedProductId = String.valueOf(cell.getNumericCellValue());
                        }
                        if (!storedProductId.matches("^P#[0-9]{5}$")) {
                            excelErrors.append("Product Id does not match the pattern at Row: ").append(i + 1).append("\n");
                            continue;
                        }
                        if (storedProductId.equals(productId)) {
                            rowNumToBeRemoved = i;
                            break;
                        }
                    }
                    if (rowNumToBeRemoved == -1) {
                        return "Product Id does not exist";
                    }
                    if (!productId.matches("^P#[0-9]{5}$")) {
                        return "Product Id does follow the pattern";
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
                    return "Products sheet is not available in the excel file";
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
