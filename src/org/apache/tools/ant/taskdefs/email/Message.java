/*     */ package org.apache.tools.ant.taskdefs.email;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.tools.ant.ProjectComponent;
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
/*     */ public class Message
/*     */   extends ProjectComponent
/*     */ {
/*  40 */   private File messageSource = null;
/*  41 */   private StringBuffer buffer = new StringBuffer();
/*  42 */   private String mimeType = "text/plain";
/*     */   private boolean specified = false;
/*  44 */   private String charset = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private String inputEncoding;
/*     */ 
/*     */ 
/*     */   
/*     */   public Message() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Message(String text) {
/*  57 */     addText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message(File file) {
/*  66 */     this.messageSource = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/*  75 */     this.buffer.append(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File src) {
/*  84 */     this.messageSource = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMimeType(String mimeType) {
/*  93 */     this.mimeType = mimeType;
/*  94 */     this.specified = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMimeType() {
/* 103 */     return this.mimeType;
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
/*     */   public void print(PrintStream ps) throws IOException {
/* 116 */     BufferedWriter out = null;
/*     */ 
/*     */     
/* 119 */     out = (this.charset == null) ? new BufferedWriter(new OutputStreamWriter(ps)) : new BufferedWriter(new OutputStreamWriter(ps, this.charset));
/* 120 */     if (this.messageSource != null)
/*     */     
/* 122 */     { BufferedReader in = new BufferedReader(getReader(this.messageSource)); 
/*     */       try { String line;
/* 124 */         while ((line = in.readLine()) != null) {
/* 125 */           out.write(getProject().replaceProperties(line));
/* 126 */           out.newLine();
/*     */         } 
/* 128 */         in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*     */        }
/* 130 */     else { out.write(getProject().replaceProperties(this.buffer.substring(0)));
/* 131 */       out.newLine(); }
/*     */     
/* 133 */     out.flush();
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
/*     */   public boolean isMimeTypeSpecified() {
/* 145 */     return this.specified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(String charset) {
/* 155 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharset() {
/* 165 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputEncoding(String encoding) {
/* 175 */     this.inputEncoding = encoding;
/*     */   }
/*     */   
/*     */   private Reader getReader(File f) throws IOException {
/* 179 */     if (this.inputEncoding != null) {
/* 180 */       InputStream fis = Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0]);
/*     */       try {
/* 182 */         return new InputStreamReader(fis, this.inputEncoding);
/* 183 */       } catch (IOException ex) {
/* 184 */         fis.close();
/* 185 */         throw ex;
/*     */       } 
/*     */     } 
/* 188 */     return new FileReader(f);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/email/Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */