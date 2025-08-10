/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class OutputStreamFunneler
/*     */ {
/*     */   public static final long DEFAULT_TIMEOUT_MILLIS = 1000L;
/*     */   private OutputStream out;
/*     */   
/*     */   private final class Funnel
/*     */     extends OutputStream
/*     */   {
/*     */     private boolean closed = false;
/*     */     
/*     */     public void flush() throws IOException {
/*  49 */       synchronized (OutputStreamFunneler.this) {
/*  50 */         OutputStreamFunneler.this.dieIfClosed();
/*  51 */         OutputStreamFunneler.this.out.flush();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/*  57 */       synchronized (OutputStreamFunneler.this) {
/*  58 */         OutputStreamFunneler.this.dieIfClosed();
/*  59 */         OutputStreamFunneler.this.out.write(b);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/*  65 */       synchronized (OutputStreamFunneler.this) {
/*  66 */         OutputStreamFunneler.this.dieIfClosed();
/*  67 */         OutputStreamFunneler.this.out.write(b);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/*  73 */       synchronized (OutputStreamFunneler.this) {
/*  74 */         OutputStreamFunneler.this.dieIfClosed();
/*  75 */         OutputStreamFunneler.this.out.write(b, off, len);
/*     */       }  } private Funnel() {
/*     */       synchronized (OutputStreamFunneler.this) {
/*     */         ++OutputStreamFunneler.this.count;
/*     */       } 
/*     */     } public void close() throws IOException {
/*  81 */       OutputStreamFunneler.this.release(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  86 */   private int count = 0;
/*     */ 
/*     */   
/*     */   private boolean closed;
/*     */ 
/*     */   
/*     */   private long timeoutMillis;
/*     */ 
/*     */   
/*     */   public OutputStreamFunneler(OutputStream out) {
/*  96 */     this(out, 1000L);
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
/*     */   public OutputStreamFunneler(OutputStream out, long timeoutMillis) {
/* 108 */     if (out == null) {
/* 109 */       throw new IllegalArgumentException("OutputStreamFunneler.<init>:  out == null");
/*     */     }
/*     */     
/* 112 */     this.out = out;
/* 113 */     this.closed = false;
/* 114 */     setTimeout(timeoutMillis);
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
/*     */   public synchronized void setTimeout(long timeoutMillis) {
/* 126 */     this.timeoutMillis = timeoutMillis;
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
/*     */   public synchronized OutputStream getFunnelInstance() throws IOException {
/* 138 */     dieIfClosed();
/*     */     try {
/* 140 */       return new Funnel();
/*     */     } finally {
/* 142 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void release(Funnel funnel) throws IOException {
/* 148 */     if (!funnel.closed) {
/*     */       try {
/* 150 */         if (this.timeoutMillis > 0L) {
/* 151 */           long start = System.currentTimeMillis();
/* 152 */           long end = start + this.timeoutMillis;
/* 153 */           long now = System.currentTimeMillis();
/*     */           try {
/* 155 */             while (now < end) {
/* 156 */               wait(end - now);
/* 157 */               now = System.currentTimeMillis();
/*     */             } 
/* 159 */           } catch (InterruptedException interruptedException) {}
/*     */         } 
/*     */ 
/*     */         
/* 163 */         if (--this.count == 0) {
/* 164 */           close();
/*     */         }
/*     */       } finally {
/* 167 */         funnel.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized void close() throws IOException {
/*     */     try {
/* 174 */       dieIfClosed();
/* 175 */       this.out.close();
/*     */     } finally {
/* 177 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized void dieIfClosed() throws IOException {
/* 182 */     if (this.closed)
/* 183 */       throw new IOException("The funneled OutputStream has been closed."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/OutputStreamFunneler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */