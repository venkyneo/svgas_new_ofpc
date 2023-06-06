package com.svgas.main.repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.svgas.main.models.reportdata_gs;

public class ExcelHelper 
{
	public ByteArrayInputStream ExcelReport_Stream(reportdata_gs rdgs)
	{
		try
		{
			Workbook workbook = new XSSFWorkbook();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Sheet sheet = workbook.createSheet("Report_XLS");
			
			Font font = workbook.createFont();  
	        font.setFontHeightInPoints((short)12);
	        font.setBold(true);
	        CellStyle style = workbook.createCellStyle();
			style.setFont(font);
			
			SetDetails(workbook,sheet,rdgs,style);
			
			//Heading
			Row headerRow = sheet.createRow(5);
			int col = 0;
			for(String headStr : rdgs.getColumn_names())
			{
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headStr);
				cell.setCellStyle(style);
				col++;
			}
			
			int rowIdx = 6;
			col = 0;
			for(String data_str : rdgs.getObj_list())
			{
				Row row = sheet.createRow(rowIdx++);
				col=0;
				String[] strarr = data_str.split(",");
				for(String str : strarr)
				{
					row.createCell(col).setCellValue(str);
					col++;
				}
			}
			workbook.write(out);
			workbook.close();
			return new ByteArrayInputStream(out.toByteArray());
		}
		catch(Exception et)
		{
			throw new RuntimeException("fail to import data to Excel file: " + et.getMessage());
		}
	}
	
	public void SetDetails(Workbook workbook, Sheet sheet, reportdata_gs rdgs,CellStyle style)
	{
//		Font font = workbook.createFont();  
//        font.setFontHeightInPoints((short)12);
//        font.setBold(true);
//        CellStyle style = workbook.createCellStyle();
//		style.setFont(font);
		
		Row DetailsRow = sheet.createRow(0);
		Cell cell = DetailsRow.createCell(0);
		cell.setCellValue("Company Name");
		cell.setCellStyle(style);
		cell = DetailsRow.createCell(1);
		cell.setCellValue(rdgs.getSitename());
		cell.setCellStyle(style);
		
		DetailsRow = sheet.createRow(1);
		cell = DetailsRow.createCell(0);
		cell.setCellValue("Report Type");
		cell.setCellStyle(style);
		cell = DetailsRow.createCell(1);
		cell.setCellValue(rdgs.getReport_type());
		cell.setCellStyle(style);
		
		DetailsRow = sheet.createRow(2);
		cell = DetailsRow.createCell(0);
		cell.setCellValue("Report Details");
		cell.setCellStyle(style);
		cell = DetailsRow.createCell(1);
		cell.setCellValue(rdgs.getReport_details());
		cell.setCellStyle(style);
	}
}
