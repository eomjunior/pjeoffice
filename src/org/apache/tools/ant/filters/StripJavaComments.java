/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
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
/*     */ public final class StripJavaComments
/*     */   extends BaseFilterReader
/*     */   implements ChainableReader
/*     */ {
/*  40 */   private int readAheadCh = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inString = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean quoted = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StripJavaComments() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StripJavaComments(Reader in) {
/*  69 */     super(in);
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
/*     */   public int read() throws IOException {
/*  83 */     int ch = -1;
/*  84 */     if (this.readAheadCh != -1) {
/*  85 */       ch = this.readAheadCh;
/*  86 */       this.readAheadCh = -1;
/*     */     } else {
/*  88 */       ch = this.in.read();
/*  89 */       if (ch == 34 && !this.quoted) {
/*  90 */         this.inString = !this.inString;
/*  91 */         this.quoted = false;
/*  92 */       } else if (ch == 92) {
/*  93 */         this.quoted = !this.quoted;
/*     */       } else {
/*  95 */         this.quoted = false;
/*  96 */         if (!this.inString && 
/*  97 */           ch == 47) {
/*  98 */           ch = this.in.read();
/*  99 */           if (ch == 47) {
/* 100 */             while (ch != 10 && ch != -1 && ch != 13) {
/* 101 */               ch = this.in.read();
/*     */             }
/* 103 */           } else if (ch == 42) {
/* 104 */             while (ch != -1) {
/* 105 */               ch = this.in.read();
/* 106 */               if (ch == 42) {
/* 107 */                 ch = this.in.read();
/* 108 */                 while (ch == 42) {
/* 109 */                   ch = this.in.read();
/*     */                 }
/*     */                 
/* 112 */                 if (ch == 47) {
/* 113 */                   ch = read();
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } else {
/* 119 */             this.readAheadCh = ch;
/* 120 */             ch = 47;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 127 */     return ch;
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
/*     */   public Reader chain(Reader rdr) {
/* 142 */     return new StripJavaComments(rdr);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/StripJavaComments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */