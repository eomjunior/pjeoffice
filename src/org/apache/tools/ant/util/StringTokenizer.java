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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringTokenizer
/*     */   extends ProjectComponent
/*     */   implements Tokenizer
/*     */ {
/*     */   private static final int NOT_A_CHAR = -2;
/*  36 */   private String intraString = "";
/*  37 */   private int pushed = -2;
/*  38 */   private char[] delims = null;
/*     */   
/*     */   private boolean delimsAreTokens = false;
/*     */   
/*     */   private boolean suppressDelims = false;
/*     */   
/*     */   private boolean includeDelims = false;
/*     */ 
/*     */   
/*     */   public void setDelims(String delims) {
/*  48 */     this.delims = StringUtils.resolveBackSlash(delims).toCharArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimsAreTokens(boolean delimsAreTokens) {
/*  58 */     this.delimsAreTokens = delimsAreTokens;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuppressDelims(boolean suppressDelims) {
/*  66 */     this.suppressDelims = suppressDelims;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeDelims(boolean includeDelims) {
/*  76 */     this.includeDelims = includeDelims;
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
/*  87 */     int ch = -1;
/*  88 */     if (this.pushed != -2) {
/*  89 */       ch = this.pushed;
/*  90 */       this.pushed = -2;
/*     */     } else {
/*  92 */       ch = in.read();
/*     */     } 
/*  94 */     if (ch == -1) {
/*  95 */       return null;
/*     */     }
/*  97 */     boolean inToken = true;
/*  98 */     this.intraString = "";
/*  99 */     StringBuilder word = new StringBuilder();
/* 100 */     StringBuilder padding = new StringBuilder();
/* 101 */     while (ch != -1) {
/* 102 */       char c = (char)ch;
/* 103 */       boolean isDelim = isDelim(c);
/* 104 */       if (inToken) {
/* 105 */         if (isDelim) {
/* 106 */           if (this.delimsAreTokens) {
/* 107 */             if (word.length() > 0) {
/* 108 */               this.pushed = ch; break;
/*     */             } 
/* 110 */             word.append(c);
/*     */             
/*     */             break;
/*     */           } 
/* 114 */           padding.append(c);
/* 115 */           inToken = false;
/*     */         } else {
/* 117 */           word.append(c);
/*     */         } 
/* 119 */       } else if (isDelim) {
/* 120 */         padding.append(c);
/*     */       } else {
/* 122 */         this.pushed = ch;
/*     */         break;
/*     */       } 
/* 125 */       ch = in.read();
/*     */     } 
/* 127 */     this.intraString = padding.toString();
/* 128 */     if (this.includeDelims) {
/* 129 */       word.append(this.intraString);
/*     */     }
/* 131 */     return word.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPostToken() {
/* 139 */     return (this.suppressDelims || this.includeDelims) ? "" : this.intraString;
/*     */   }
/*     */   
/*     */   private boolean isDelim(char ch) {
/* 143 */     if (this.delims == null) {
/* 144 */       return Character.isWhitespace(ch);
/*     */     }
/* 146 */     for (char delim : this.delims) {
/* 147 */       if (delim == ch) {
/* 148 */         return true;
/*     */       }
/*     */     } 
/* 151 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/StringTokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */