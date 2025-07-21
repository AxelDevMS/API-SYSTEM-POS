package com.asmdev.api.pos.utils.helper;

import com.asmdev.api.pos.dto.CategoryDto;
import com.asmdev.api.pos.dto.PermissionDto;
import com.asmdev.api.pos.exception.BadRequestException;
import com.asmdev.api.pos.utils.status.ModuleSystem;
import com.asmdev.api.pos.utils.status.NamePermissions;
import com.asmdev.api.pos.utils.status.Status;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelHelper {

    public static List<PermissionDto> readDataPermissionExcel(InputStream inputStream) throws BadRequestException {
        try(Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            List<PermissionDto> permissionList = new ArrayList<>();

            for (int i = 1; i<= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);

                if (row == null) continue;

                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setName(NamePermissions.valueOf(getCellValue(row.getCell(0))));
                permissionDto.setModule(ModuleSystem.valueOf(getCellValue(row.getCell(1))));
                permissionDto.setDescription(getCellValue(row.getCell(2)));

                permissionList.add(permissionDto);
            }
            return permissionList;
        }catch (Exception e){
            throw new BadRequestException("Error al leer el archivo Excel",e);
        }
    }

    public static List<CategoryDto> readDataCategoryExcel(InputStream inputStream) throws BadRequestException {
        try(Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            List<CategoryDto> categoryList = new ArrayList<>();

            for (int i = 1; i <=sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);

                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setName(getCellValue(row.getCell(0)));
                categoryDto.setDescription(getCellValue(row.getCell(1)));

                categoryList.add(categoryDto);
            }
            return categoryList;
        }catch (Exception e){
            throw new BadRequestException("Erro al leer el archivo Excel", e);
        }
    }

    private static  String getCellValue(Cell cell){

        if (cell == null) return "";

        return switch (cell.getCellType()){
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
