/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public abstract class BaseFilterReader
/*     */   extends FilterReader
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private boolean initialized = false;
/*  40 */   private Project project = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFilterReader() {
/*  51 */     super(new StringReader(""));
/*  52 */     FileUtils.close(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFilterReader(Reader in) {
/*  63 */     super(in);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int read(char[] cbuf, int off, int len) throws IOException {
/*  83 */     for (int i = 0; i < len; i++) {
/*  84 */       int ch = read();
/*  85 */       if (ch == -1) {
/*  86 */         if (i == 0) {
/*  87 */           return -1;
/*     */         }
/*  89 */         return i;
/*     */       } 
/*     */       
/*  92 */       cbuf[off + i] = (char)ch;
/*     */     } 
/*  94 */     return len;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final long skip(long n) throws IOException, IllegalArgumentException {
/* 110 */     if (n < 0L) {
/* 111 */       throw new IllegalArgumentException("skip value is negative");
/*     */     }
/*     */     
/* 114 */     for (long i = 0L; i < n; i++) {
/* 115 */       if (read() == -1) {
/* 116 */         return i;
/*     */       }
/*     */     } 
/* 119 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setInitialized(boolean initialized) {
/* 128 */     this.initialized = initialized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean getInitialized() {
/* 137 */     return this.initialized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setProject(Project project) {
/* 147 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Project getProject() {
/* 156 */     return this.project;
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
/*     */   
/*     */   protected final String readLine() throws IOException {
/* 170 */     int ch = this.in.read();
/*     */     
/* 172 */     if (ch == -1) {
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     StringBuilder line = new StringBuilder();
/*     */     
/* 178 */     while (ch != -1) {
/* 179 */       line.append((char)ch);
/* 180 */       if (ch == 10) {
/*     */         break;
/*     */       }
/* 183 */       ch = this.in.read();
/*     */     } 
/* 185 */     return line.toString();
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
/*     */   protected final String readFully() throws IOException {
/* 197 */     return FileUtils.readFully(this.in, 8192);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/BaseFilterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */