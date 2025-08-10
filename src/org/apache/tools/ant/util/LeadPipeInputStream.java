/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public class LeadPipeInputStream
/*     */   extends PipedInputStream
/*     */ {
/*     */   private static final int BYTE_MASK = 255;
/*     */   private ProjectComponent managingPc;
/*     */   
/*     */   public LeadPipeInputStream() {}
/*     */   
/*     */   public LeadPipeInputStream(int size) {
/*  52 */     setBufferSize(size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LeadPipeInputStream(PipedOutputStream src) throws IOException {
/*  62 */     super(src);
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
/*     */   public LeadPipeInputStream(PipedOutputStream src, int size) throws IOException {
/*  74 */     super(src);
/*  75 */     setBufferSize(size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int read() throws IOException {
/*  86 */     int result = -1;
/*     */     try {
/*  88 */       result = super.read();
/*  89 */     } catch (IOException eyeOhEx) {
/*  90 */       String msg = eyeOhEx.getMessage();
/*  91 */       if ("write end dead".equalsIgnoreCase(msg) || "pipe broken"
/*  92 */         .equalsIgnoreCase(msg)) {
/*  93 */         if (this.in > 0 && this.out < this.buffer.length && this.out > this.in)
/*     */         {
/*  95 */           result = this.buffer[this.out++] & 0xFF;
/*     */         }
/*     */       } else {
/*  98 */         log("error at LeadPipeInputStream.read():  " + msg, 2);
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setBufferSize(int size) {
/* 110 */     if (size > this.buffer.length) {
/* 111 */       byte[] newBuffer = new byte[size];
/* 112 */       if (this.in >= 0) {
/* 113 */         if (this.in > this.out) {
/* 114 */           System.arraycopy(this.buffer, this.out, newBuffer, this.out, this.in - this.out);
/*     */         } else {
/* 116 */           int outlen = this.buffer.length - this.out;
/* 117 */           System.arraycopy(this.buffer, this.out, newBuffer, 0, outlen);
/* 118 */           System.arraycopy(this.buffer, 0, newBuffer, outlen, this.in);
/* 119 */           this.in += outlen;
/* 120 */           this.out = 0;
/*     */         } 
/*     */       }
/* 123 */       this.buffer = newBuffer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagingTask(Task task) {
/* 133 */     setManagingComponent((ProjectComponent)task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagingComponent(ProjectComponent pc) {
/* 142 */     this.managingPc = pc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, int loglevel) {
/* 151 */     if (this.managingPc != null) {
/* 152 */       this.managingPc.log(message, loglevel);
/*     */     }
/* 154 */     else if (loglevel > 1) {
/* 155 */       System.out.println(message);
/*     */     } else {
/* 157 */       System.err.println(message);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LeadPipeInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */