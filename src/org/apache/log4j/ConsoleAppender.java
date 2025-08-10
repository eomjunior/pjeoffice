/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ public class ConsoleAppender
/*     */   extends WriterAppender
/*     */ {
/*     */   public static final String SYSTEM_OUT = "System.out";
/*     */   public static final String SYSTEM_ERR = "System.err";
/*  38 */   protected String target = "System.out";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean follow = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConsoleAppender() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConsoleAppender(Layout layout) {
/*  58 */     this(layout, "System.out");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConsoleAppender(Layout layout, String target) {
/*  68 */     setLayout(layout);
/*  69 */     setTarget(target);
/*  70 */     activateOptions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String value) {
/*  78 */     String v = value.trim();
/*     */     
/*  80 */     if ("System.out".equalsIgnoreCase(v)) {
/*  81 */       this.target = "System.out";
/*  82 */     } else if ("System.err".equalsIgnoreCase(v)) {
/*  83 */       this.target = "System.err";
/*     */     } else {
/*  85 */       targetWarn(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTarget() {
/*  96 */     return this.target;
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
/*     */   public final void setFollow(boolean newValue) {
/* 108 */     this.follow = newValue;
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
/*     */   public final boolean getFollow() {
/* 120 */     return this.follow;
/*     */   }
/*     */   
/*     */   void targetWarn(String val) {
/* 124 */     LogLog.warn("[" + val + "] should be System.out or System.err.");
/* 125 */     LogLog.warn("Using previously set target, System.out by default.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 132 */     if (this.follow) {
/* 133 */       if (this.target.equals("System.err")) {
/* 134 */         setWriter(createWriter(new SystemErrStream()));
/*     */       } else {
/* 136 */         setWriter(createWriter(new SystemOutStream()));
/*     */       }
/*     */     
/* 139 */     } else if (this.target.equals("System.err")) {
/* 140 */       setWriter(createWriter(System.err));
/*     */     } else {
/* 142 */       setWriter(createWriter(System.out));
/*     */     } 
/*     */ 
/*     */     
/* 146 */     super.activateOptions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void closeWriter() {
/* 153 */     if (this.follow) {
/* 154 */       super.closeWriter();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemErrStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 170 */       System.err.flush();
/*     */     }
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 174 */       System.err.write(b);
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 178 */       System.err.write(b, off, len);
/*     */     }
/*     */     
/*     */     public void write(int b) throws IOException {
/* 182 */       System.err.write(b);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SystemOutStream
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {
/* 198 */       System.out.flush();
/*     */     }
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 202 */       System.out.write(b);
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 206 */       System.out.write(b, off, len);
/*     */     }
/*     */     
/*     */     public void write(int b) throws IOException {
/* 210 */       System.out.write(b);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/ConsoleAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */