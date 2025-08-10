/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionConverter
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 8657630363395849399L;
/*     */   private Exception ex;
/*     */   private String prefix;
/*     */   
/*     */   public ExceptionConverter(Exception ex) {
/*  72 */     super(ex);
/*  73 */     this.ex = ex;
/*  74 */     this.prefix = (ex instanceof RuntimeException) ? "" : "ExceptionConverter: ";
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
/*     */   public static final RuntimeException convertException(Exception ex) {
/*  86 */     if (ex instanceof RuntimeException) {
/*  87 */       return (RuntimeException)ex;
/*     */     }
/*  89 */     return new ExceptionConverter(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getException() {
/*  97 */     return this.ex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 105 */     return this.ex.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocalizedMessage() {
/* 113 */     return this.ex.getLocalizedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     return this.prefix + this.ex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace() {
/* 126 */     printStackTrace(System.err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream s) {
/* 135 */     synchronized (s) {
/* 136 */       s.print(this.prefix);
/* 137 */       this.ex.printStackTrace(s);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter s) {
/* 146 */     synchronized (s) {
/* 147 */       s.print(this.prefix);
/* 148 */       this.ex.printStackTrace(s);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable fillInStackTrace() {
/* 159 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ExceptionConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */