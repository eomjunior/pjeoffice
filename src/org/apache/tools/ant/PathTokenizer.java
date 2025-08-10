/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
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
/*     */ public class PathTokenizer
/*     */ {
/*     */   private StringTokenizer tokenizer;
/*  44 */   private String lookahead = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private boolean onNetWare = Os.isFamily("netware");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dosStyleFilesystem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathTokenizer(String path) {
/*  65 */     if (this.onNetWare) {
/*     */ 
/*     */       
/*  68 */       this.tokenizer = new StringTokenizer(path, ":;", true);
/*     */     }
/*     */     else {
/*     */       
/*  72 */       this.tokenizer = new StringTokenizer(path, ":;", false);
/*     */     } 
/*  74 */     this.dosStyleFilesystem = (File.pathSeparatorChar == ';');
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
/*     */   public boolean hasMoreTokens() {
/*  86 */     return (this.lookahead != null || this.tokenizer.hasMoreTokens());
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
/*     */   public String nextToken() throws NoSuchElementException {
/*  98 */     String token = null;
/*  99 */     if (this.lookahead != null) {
/* 100 */       token = this.lookahead;
/* 101 */       this.lookahead = null;
/*     */     } else {
/* 103 */       token = this.tokenizer.nextToken().trim();
/*     */     } 
/*     */     
/* 106 */     if (!this.onNetWare) {
/* 107 */       if (token.length() == 1 && Character.isLetter(token.charAt(0)) && this.dosStyleFilesystem && this.tokenizer
/*     */         
/* 109 */         .hasMoreTokens()) {
/*     */ 
/*     */         
/* 112 */         String nextToken = this.tokenizer.nextToken().trim();
/* 113 */         if (nextToken.startsWith("\\") || nextToken.startsWith("/")) {
/*     */ 
/*     */ 
/*     */           
/* 117 */           token = token + ":" + nextToken;
/*     */         } else {
/*     */           
/* 120 */           this.lookahead = nextToken;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 126 */       if (token.equals(File.pathSeparator) || ":".equals(token))
/*     */       {
/* 128 */         token = this.tokenizer.nextToken().trim();
/*     */       }
/*     */       
/* 131 */       if (this.tokenizer.hasMoreTokens()) {
/*     */         
/* 133 */         String nextToken = this.tokenizer.nextToken().trim();
/*     */ 
/*     */         
/* 136 */         if (!nextToken.equals(File.pathSeparator)) {
/* 137 */           if (":".equals(nextToken)) {
/* 138 */             if (!token.startsWith("/") && !token.startsWith("\\") && 
/* 139 */               !token.startsWith(".") && 
/* 140 */               !token.startsWith(".."))
/*     */             {
/* 142 */               String oneMore = this.tokenizer.nextToken().trim();
/* 143 */               if (!oneMore.equals(File.pathSeparator)) {
/* 144 */                 token = token + ":" + oneMore;
/*     */               } else {
/* 146 */                 token = token + ":";
/* 147 */                 this.lookahead = oneMore;
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 154 */             this.lookahead = nextToken;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 159 */     return token;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/PathTokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */