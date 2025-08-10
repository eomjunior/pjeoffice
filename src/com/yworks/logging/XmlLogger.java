/*     */ package com.yworks.logging;
/*     */ 
/*     */ import com.yworks.util.Version;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringReader;
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
/*     */ public class XmlLogger
/*     */   extends Logger
/*     */ {
/*     */   private PrintWriter pw;
/*     */   
/*     */   public XmlLogger(PrintWriter pw) {
/*  24 */     this.pw = pw;
/*  25 */     pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/*  26 */     pw.println("<yshrink version=\"" + Version.getVersion() + "\">");
/*  27 */     register();
/*     */   }
/*     */   
/*     */   public void doLog(String s) {
/*  31 */     this.pw.println("<!-- " + s + " -->");
/*     */   }
/*     */   
/*     */   public void doErr(String s) {
/*  35 */     this.pw.println("<!-- ERROR: ");
/*  36 */     this.pw.println(s);
/*  37 */     this.pw.println("-->");
/*     */   }
/*     */   
/*     */   public void doErr(String s, Throwable ex) {
/*  41 */     this.pw.println("<!-- ERROR: ");
/*  42 */     this.pw.println(s);
/*  43 */     ex.printStackTrace(this.pw);
/*  44 */     this.pw.println("-->");
/*     */   }
/*     */   
/*     */   public void doWarn(String s) {
/*  48 */     this.pw.println("<!-- WARNING:" + s + " -->");
/*     */   }
/*     */   
/*     */   public void doWarnToLog(String s) {
/*  52 */     this.pw.println("<!-- WARNING:" + s + " -->");
/*     */   }
/*     */   
/*     */   public void doShrinkLog(String s) {
/*  56 */     this.pw.println(s);
/*     */   }
/*     */   
/*     */   public void close() {
/*  60 */     this.pw.println("</yshrink>");
/*  61 */     this.pw.println();
/*  62 */     this.pw.close();
/*  63 */     unregister();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String replaceSpecialChars(String s) {
/*  74 */     StringReader reader = new StringReader(s);
/*     */     
/*  76 */     StringBuilder r = new StringBuilder();
/*     */     
/*     */     try {
/*     */       int i;
/*  80 */       while ((i = reader.read()) != -1)
/*     */       {
/*  82 */         char c = (char)i;
/*     */         
/*  84 */         switch (c) {
/*     */           
/*     */           case '>':
/*  87 */             r.append("&gt;");
/*     */             continue;
/*     */           
/*     */           case '<':
/*  91 */             r.append("&lt;");
/*     */             continue;
/*     */         } 
/*     */         
/*  95 */         r.append(c);
/*     */       }
/*     */     
/*     */     }
/*  99 */     catch (IOException e) {
/* 100 */       Logger.err(e.getMessage());
/*     */     } 
/* 102 */     return r.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/logging/XmlLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */