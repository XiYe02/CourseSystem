package org.example.coursesystem.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xssf.usermodel.*;
import org.example.coursesystem.entity.CourseSelection;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 导出Excel格式的选课数据
     */
    public void exportToExcel(List<CourseSelection> courseSelections, HttpServletResponse response) throws IOException {
        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("学生选课数据");

        // 创建标题样式
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);

        // 创建标题行
        XSSFRow headerRow = sheet.createRow(0);
        String[] headers = {"学号", "学生姓名", "课程代码", "课程名称", "学分", "学时", "学期", "选课时间", "成绩", "绩点", "状态"};
        
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 填充数据
        int rowNum = 1;
        for (CourseSelection selection : courseSelections) {
            XSSFRow row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(selection.getStudent() != null ? selection.getStudent().getStudentNumber() : "");
            row.createCell(1).setCellValue(selection.getStudent() != null ? selection.getStudent().getName() : "");
            row.createCell(2).setCellValue(selection.getCourse() != null ? selection.getCourse().getCourseCode() : "");
            row.createCell(3).setCellValue(selection.getCourse() != null ? selection.getCourse().getCourseName() : "");
            row.createCell(4).setCellValue(selection.getCourse() != null ? selection.getCourse().getCredits().doubleValue() : 0.0);
            row.createCell(5).setCellValue(selection.getCourse() != null ? selection.getCourse().getHours() : 0);
            row.createCell(6).setCellValue(selection.getSemester() != null ? 
                selection.getSemester().getSemesterName() + " (" + selection.getSemester().getAcademicYear() + ")" : "");
            row.createCell(7).setCellValue(selection.getSelectionTime() != null ? 
                selection.getSelectionTime().format(DATE_FORMATTER) : "");
            row.createCell(8).setCellValue(selection.getScore() != null ? selection.getScore().doubleValue() : 0.0);
            row.createCell(9).setCellValue(selection.getGradePoint() != null ? selection.getGradePoint().doubleValue() : 0.0);
            row.createCell(10).setCellValue(getStatusText(selection.getStatus()));
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=course_selections.xlsx");

        // 写入响应
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 导出PDF格式的选课数据
     */
    public void exportToPdf(List<CourseSelection> courseSelections, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 添加标题
        Paragraph title = new Paragraph("学生选课数据报表")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold();
        document.add(title);

        // 创建表格
        Table table = new Table(UnitValue.createPercentArray(new float[]{8, 10, 10, 15, 6, 6, 12, 12, 8, 8, 8}));
        table.setWidth(UnitValue.createPercentValue(100));

        // 添加表头
        String[] headers = {"学号", "学生姓名", "课程代码", "课程名称", "学分", "学时", "学期", "选课时间", "成绩", "绩点", "状态"};
        for (String header : headers) {
            Cell headerCell = new Cell().add(new Paragraph(header).setBold());
            headerCell.setTextAlignment(TextAlignment.CENTER);
            table.addHeaderCell(headerCell);
        }

        // 添加数据行
        for (CourseSelection selection : courseSelections) {
            table.addCell(new Cell().add(new Paragraph(selection.getStudent() != null ? selection.getStudent().getStudentNumber() : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getStudent() != null ? selection.getStudent().getName() : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getCourse() != null ? selection.getCourse().getCourseCode() : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getCourse() != null ? selection.getCourse().getCourseName() : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getCourse() != null ? String.valueOf(selection.getCourse().getCredits()) : "0")));
            table.addCell(new Cell().add(new Paragraph(selection.getCourse() != null ? String.valueOf(selection.getCourse().getHours()) : "0")));
            table.addCell(new Cell().add(new Paragraph(selection.getSemester() != null ? 
                selection.getSemester().getSemesterName() + " (" + selection.getSemester().getAcademicYear() + ")" : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getSelectionTime() != null ? 
                selection.getSelectionTime().format(DATE_FORMATTER) : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getScore() != null ? selection.getScore().toString() : "")));
            table.addCell(new Cell().add(new Paragraph(selection.getGradePoint() != null ? selection.getGradePoint().toString() : "")));
            table.addCell(new Cell().add(new Paragraph(getStatusText(selection.getStatus()))));
        }

        document.add(table);
        document.close();

        // 设置响应头
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=course_selections.pdf");
        response.setContentLength(baos.size());

        // 写入响应
        response.getOutputStream().write(baos.toByteArray());
        baos.close();
    }

    /**
     * 导出Word格式的选课数据
     */
    public void exportToWord(List<CourseSelection> courseSelections, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // 创建Word文档
        XWPFDocument document = new XWPFDocument();
        
        // 添加标题
        XWPFParagraph titleParagraph = document.createParagraph();
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("学生选课数据报表");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        
        // 创建表格
        XWPFTable table = document.createTable();
        
        // 创建表头
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("学号");
        headerRow.addNewTableCell().setText("学生姓名");
        headerRow.addNewTableCell().setText("课程代码");
        headerRow.addNewTableCell().setText("课程名称");
        headerRow.addNewTableCell().setText("学分");
        headerRow.addNewTableCell().setText("学时");
        headerRow.addNewTableCell().setText("学期");
        headerRow.addNewTableCell().setText("选课时间");
        headerRow.addNewTableCell().setText("成绩");
        headerRow.addNewTableCell().setText("绩点");
        headerRow.addNewTableCell().setText("状态");
        
        // 添加数据行
        for (CourseSelection selection : courseSelections) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(selection.getStudent() != null ? selection.getStudent().getStudentNumber() : "");
            row.getCell(1).setText(selection.getStudent() != null ? selection.getStudent().getName() : "");
            row.getCell(2).setText(selection.getCourse() != null ? selection.getCourse().getCourseCode() : "");
            row.getCell(3).setText(selection.getCourse() != null ? selection.getCourse().getCourseName() : "");
            row.getCell(4).setText(selection.getCourse() != null ? String.valueOf(selection.getCourse().getCredits()) : "0");
            row.getCell(5).setText(selection.getCourse() != null ? String.valueOf(selection.getCourse().getHours()) : "0");
            row.getCell(6).setText(selection.getSemester() != null ? 
                selection.getSemester().getSemesterName() + " (" + selection.getSemester().getAcademicYear() + ")" : "");
            row.getCell(7).setText(selection.getSelectionTime() != null ? 
                selection.getSelectionTime().format(DATE_FORMATTER) : "");
            row.getCell(8).setText(selection.getScore() != null ? selection.getScore().toString() : "");
            row.getCell(9).setText(selection.getGradePoint() != null ? selection.getGradePoint().toString() : "");
            row.getCell(10).setText(getStatusText(selection.getStatus()));
        }
        
        document.write(baos);
        document.close();

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=course_selections.docx");
        response.setContentLength(baos.size());

        // 写入响应
        response.getOutputStream().write(baos.toByteArray());
        baos.close();
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "SELECTED":
                return "已选";
            case "COMPLETED":
                return "已完成";
            case "FAILED":
                return "未通过";
            default:
                return status;
        }
    }
}