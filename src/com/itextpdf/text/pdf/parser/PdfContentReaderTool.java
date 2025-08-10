/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.pdf.PdfStream;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfContentReaderTool
/*     */ {
/*     */   public static String getDictionaryDetail(PdfDictionary dic) {
/*  75 */     return getDictionaryDetail(dic, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDictionaryDetail(PdfDictionary dic, int depth) {
/*  85 */     StringBuffer builder = new StringBuffer();
/*  86 */     builder.append('(');
/*  87 */     List<PdfName> subDictionaries = new ArrayList<PdfName>();
/*  88 */     for (PdfName key : dic.getKeys()) {
/*  89 */       PdfObject val = dic.getDirectObject(key);
/*  90 */       if (val.isDictionary())
/*  91 */         subDictionaries.add(key); 
/*  92 */       builder.append(key);
/*  93 */       builder.append('=');
/*  94 */       builder.append(val);
/*  95 */       builder.append(", ");
/*     */     } 
/*  97 */     if (builder.length() >= 2)
/*  98 */       builder.setLength(builder.length() - 2); 
/*  99 */     builder.append(')');
/* 100 */     for (PdfName pdfSubDictionaryName : subDictionaries) {
/* 101 */       builder.append('\n');
/* 102 */       for (int i = 0; i < depth + 1; i++) {
/* 103 */         builder.append('\t');
/*     */       }
/* 105 */       builder.append("Subdictionary ");
/* 106 */       builder.append(pdfSubDictionaryName);
/* 107 */       builder.append(" = ");
/* 108 */       builder.append(getDictionaryDetail(dic.getAsDict(pdfSubDictionaryName), depth + 1));
/*     */     } 
/* 110 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getXObjectDetail(PdfDictionary resourceDic) throws IOException {
/* 121 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 123 */     PdfDictionary xobjects = resourceDic.getAsDict(PdfName.XOBJECT);
/* 124 */     if (xobjects == null)
/* 125 */       return "No XObjects"; 
/* 126 */     for (PdfName entryName : xobjects.getKeys()) {
/* 127 */       PdfStream xobjectStream = xobjects.getAsStream(entryName);
/*     */       
/* 129 */       sb.append("------ " + entryName + " - subtype = " + xobjectStream.get(PdfName.SUBTYPE) + " = " + xobjectStream.getAsNumber(PdfName.LENGTH) + " bytes ------\n");
/*     */       
/* 131 */       if (!xobjectStream.get(PdfName.SUBTYPE).equals(PdfName.IMAGE)) {
/*     */         
/* 133 */         byte[] contentBytes = ContentByteUtils.getContentBytesFromContentObject((PdfObject)xobjectStream);
/*     */         
/* 135 */         InputStream is = new ByteArrayInputStream(contentBytes);
/*     */         int ch;
/* 137 */         while ((ch = is.read()) != -1) {
/* 138 */           sb.append((char)ch);
/*     */         }
/*     */         
/* 141 */         sb.append("------ " + entryName + " - subtype = " + xobjectStream.get(PdfName.SUBTYPE) + "End of Content------\n");
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void listContentStreamForPage(PdfReader reader, int pageNum, PrintWriter out) throws IOException {
/* 157 */     out.println("==============Page " + pageNum + "====================");
/* 158 */     out.println("- - - - - Dictionary - - - - - -");
/* 159 */     PdfDictionary pageDictionary = reader.getPageN(pageNum);
/* 160 */     out.println(getDictionaryDetail(pageDictionary));
/*     */     
/* 162 */     out.println("- - - - - XObject Summary - - - - - -");
/* 163 */     out.println(getXObjectDetail(pageDictionary.getAsDict(PdfName.RESOURCES)));
/*     */     
/* 165 */     out.println("- - - - - Content Stream - - - - - -");
/* 166 */     RandomAccessFileOrArray f = reader.getSafeFile();
/*     */     
/* 168 */     byte[] contentBytes = reader.getPageContent(pageNum, f);
/* 169 */     f.close();
/*     */     
/* 171 */     out.flush();
/*     */     
/* 173 */     InputStream is = new ByteArrayInputStream(contentBytes);
/*     */     int ch;
/* 175 */     while ((ch = is.read()) != -1) {
/* 176 */       out.print((char)ch);
/*     */     }
/*     */     
/* 179 */     out.flush();
/*     */     
/* 181 */     out.println("- - - - - Text Extraction - - - - - -");
/* 182 */     String extractedText = PdfTextExtractor.getTextFromPage(reader, pageNum, new LocationTextExtractionStrategy());
/* 183 */     if (extractedText.length() != 0) {
/* 184 */       out.println(extractedText);
/*     */     } else {
/* 186 */       out.println("No text found on page " + pageNum);
/*     */     } 
/* 188 */     out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void listContentStream(File pdfFile, PrintWriter out) throws IOException {
/* 200 */     PdfReader reader = new PdfReader(pdfFile.getCanonicalPath());
/*     */     
/* 202 */     int maxPageNum = reader.getNumberOfPages();
/*     */     
/* 204 */     for (int pageNum = 1; pageNum <= maxPageNum; pageNum++) {
/* 205 */       listContentStreamForPage(reader, pageNum, out);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void listContentStream(File pdfFile, int pageNum, PrintWriter out) throws IOException {
/* 219 */     PdfReader reader = new PdfReader(pdfFile.getCanonicalPath());
/*     */     
/* 221 */     listContentStreamForPage(reader, pageNum, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 230 */       if (args.length < 1 || args.length > 3) {
/* 231 */         System.out.println("Usage:  PdfContentReaderTool <pdf file> [<output file>|stdout] [<page num>]");
/*     */         
/*     */         return;
/*     */       } 
/* 235 */       PrintWriter writer = new PrintWriter(System.out);
/* 236 */       if (args.length >= 2 && 
/* 237 */         args[1].compareToIgnoreCase("stdout") != 0) {
/* 238 */         System.out.println("Writing PDF content to " + args[1]);
/* 239 */         writer = new PrintWriter(new FileOutputStream(new File(args[1])));
/*     */       } 
/*     */ 
/*     */       
/* 243 */       int pageNum = -1;
/* 244 */       if (args.length >= 3) {
/* 245 */         pageNum = Integer.parseInt(args[2]);
/*     */       }
/*     */       
/* 248 */       if (pageNum == -1) {
/* 249 */         listContentStream(new File(args[0]), writer);
/*     */       } else {
/* 251 */         listContentStream(new File(args[0]), pageNum, writer);
/*     */       } 
/* 253 */       writer.flush();
/*     */       
/* 255 */       if (args.length >= 2) {
/* 256 */         writer.close();
/* 257 */         System.out.println("Finished writing content to " + args[1]);
/*     */       } 
/* 259 */     } catch (Exception e) {
/* 260 */       e.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/PdfContentReaderTool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */