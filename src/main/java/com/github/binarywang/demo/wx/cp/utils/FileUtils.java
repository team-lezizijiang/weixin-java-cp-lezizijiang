package com.github.binarywang.demo.wx.cp.utils;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itextpdf.text.*;
import java.io.*;

public class FileUtils {
    private static final String base_path = "";
    public static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    public static String createDocx(String content, String articleID){
        File file = new File(base_path+articleID+".docx");
        XWPFDocument xwpfDocument = new XWPFDocument();
        try{
            if (!file.exists()) {
                if (file.createNewFile()) {
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
                    stream.write(content.getBytes());
                    xwpfDocument.write(stream);
                    logger.info("create " + articleID + ".txt success");
                }
            }
            return base_path + articleID + ".txt";
        }catch(IOException e){
            logger.info("use "+ articleID + ".docx");
        }
        return null;
    }
    public static String createPdf(String content, String articleID){
        Document document = new Document(PageSize.A4);
        try{
            File file = new File(base_path+articleID+".pdf");
            if(!file.exists()){
                if(file.createNewFile()){
                    PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
                    // 3.打开文档
                    document.open();
                    document.addTitle("Title@PDF-Java");// 标题
                    document.addAuthor("Author@umiz");// 作者

                    // 4.向文档中添加内容
                    Paragraph p = new Paragraph(content);
                    document.add(p);
                    // 5.关闭文档
                    document.close();
                    logger.info("use" + articleID + ".pdf success");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return base_path + articleID + ".pdf";
    }
    public static String createTxt(String content, String articleID){
        File file = new File(base_path+articleID+".txt");
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    logger.info("create " + articleID + ".txt success");
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                    bufferedWriter.write(content);
                    bufferedWriter.flush();
                }
            }
            return base_path + articleID + ".txt";
        } catch (IOException e) {
            logger.info("use" + articleID + ".txt failed");
        }
        return null;
    }
}
