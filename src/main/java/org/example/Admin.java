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

import static org.example.Constant.DATABASE;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
    private String email;
    private String password;

    private static String ADMIN_SHEET_NAME = "Admin";

    public static Admin login(String email, String password) {
        try {
            Workbook workbook;
            FileInputStream fis = new FileInputStream(DATABASE);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(ADMIN_SHEET_NAME);
            if (sheet != null) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    Cell cell = row.getCell(0);
                    String storedEmail = cell.getStringCellValue();
                    cell = row.getCell(1);
                    String storedPassword;
                    if (cell.getCellType() == CellType.STRING) {
                        storedPassword = cell.getStringCellValue();
                    } else {
                        storedPassword = String.valueOf((int)(cell.getNumericCellValue()));
                    }

                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
                        return new Admin(email, password);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while manager login");
            e.printStackTrace();
        }
        return null;
    }
}
