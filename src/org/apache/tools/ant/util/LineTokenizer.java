/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
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
/*     */ public class LineTokenizer
/*     */   extends ProjectComponent
/*     */   implements Tokenizer
/*     */ {
/*     */   private static final int NOT_A_CHAR = -2;
/*  33 */   private String lineEnd = "";
/*  34 */   private int pushed = -2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean includeDelims = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeDelims(boolean includeDelims) {
/*  46 */     this.includeDelims = includeDelims;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToken(Reader in) throws IOException {
/*     */     int ch;
/*  58 */     if (this.pushed == -2) {
/*  59 */       ch = in.read();
/*     */     } else {
/*  61 */       ch = this.pushed;
/*  62 */       this.pushed = -2;
/*     */     } 
/*  64 */     if (ch == -1) {
/*  65 */       return null;
/*     */     }
/*     */     
/*  68 */     this.lineEnd = "";
/*  69 */     StringBuilder line = new StringBuilder();
/*     */     
/*  71 */     int state = 0;
/*  72 */     while (ch != -1) {
/*  73 */       if (state == 0) {
/*  74 */         if (ch == 13)
/*  75 */         { state = 1; }
/*  76 */         else { if (ch == 10) {
/*  77 */             this.lineEnd = "\n";
/*     */             break;
/*     */           } 
/*  80 */           line.append((char)ch); }
/*     */       
/*     */       } else {
/*  83 */         state = 0;
/*  84 */         if (ch == 10) {
/*  85 */           this.lineEnd = "\r\n"; break;
/*     */         } 
/*  87 */         this.pushed = ch;
/*  88 */         this.lineEnd = "\r";
/*     */         
/*     */         break;
/*     */       } 
/*  92 */       ch = in.read();
/*     */     } 
/*  94 */     if (ch == -1 && state == 1) {
/*  95 */       this.lineEnd = "\r";
/*     */     }
/*     */     
/*  98 */     if (this.includeDelims) {
/*  99 */       line.append(this.lineEnd);
/*     */     }
/* 101 */     return line.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPostToken() {
/* 109 */     return this.includeDelims ? "" : this.lineEnd;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LineTokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */