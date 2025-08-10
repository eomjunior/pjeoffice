/*     */ package org.apache.tools.mail;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
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
/*     */ public class SmtpResponseReader
/*     */ {
/*  36 */   protected BufferedReader reader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SmtpResponseReader(InputStream in) {
/*  44 */     this.reader = new BufferedReader(new InputStreamReader(in));
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
/*     */   public String getResponse() throws IOException {
/*  56 */     StringBuilder result = new StringBuilder();
/*  57 */     String line = this.reader.readLine();
/*     */     
/*  59 */     if (line != null && line.length() >= 3) {
/*  60 */       result.append(line, 0, 3);
/*  61 */       result.append(" ");
/*     */     } 
/*     */ 
/*     */     
/*  65 */     while (line != null) {
/*  66 */       appendTo(result, line);
/*  67 */       if (!hasMoreLines(line)) {
/*     */         break;
/*     */       }
/*  70 */       line = this.reader.readLine();
/*     */     } 
/*  72 */     return result.toString().trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  80 */     this.reader.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasMoreLines(String line) {
/*  90 */     return (line.length() > 3 && line.charAt(3) == '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendTo(StringBuilder target, String line) {
/*  99 */     if (line.length() > 4)
/* 100 */       target.append(line.substring(4)).append(' '); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/mail/SmtpResponseReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */