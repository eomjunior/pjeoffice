/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class GlobPatternMapper
/*     */   implements FileNameMapper
/*     */ {
/*  42 */   protected String fromPrefix = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected String fromPostfix = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int prefixLength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int postfixLength;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected String toPrefix = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected String toPostfix = null;
/*     */ 
/*     */   
/*     */   private boolean fromContainsStar = false;
/*     */ 
/*     */   
/*     */   private boolean toContainsStar = false;
/*     */ 
/*     */   
/*     */   private boolean handleDirSep = false;
/*     */ 
/*     */   
/*     */   private boolean caseSensitive = true;
/*     */ 
/*     */   
/*     */   public void setHandleDirSep(boolean handleDirSep) {
/*  83 */     this.handleDirSep = handleDirSep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHandleDirSep() {
/*  93 */     return this.handleDirSep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/* 104 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(String from) {
/* 113 */     if (from == null) {
/* 114 */       throw new BuildException("this mapper requires a 'from' attribute");
/*     */     }
/* 116 */     int index = from.lastIndexOf('*');
/* 117 */     if (index < 0) {
/* 118 */       this.fromPrefix = from;
/* 119 */       this.fromPostfix = "";
/*     */     } else {
/* 121 */       this.fromPrefix = from.substring(0, index);
/* 122 */       this.fromPostfix = from.substring(index + 1);
/* 123 */       this.fromContainsStar = true;
/*     */     } 
/* 125 */     this.prefixLength = this.fromPrefix.length();
/* 126 */     this.postfixLength = this.fromPostfix.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTo(String to) {
/* 135 */     if (to == null) {
/* 136 */       throw new BuildException("this mapper requires a 'to' attribute");
/*     */     }
/* 138 */     int index = to.lastIndexOf('*');
/* 139 */     if (index < 0) {
/* 140 */       this.toPrefix = to;
/* 141 */       this.toPostfix = "";
/*     */     } else {
/* 143 */       this.toPrefix = to.substring(0, index);
/* 144 */       this.toPostfix = to.substring(index + 1);
/* 145 */       this.toContainsStar = true;
/*     */     } 
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
/*     */   public String[] mapFileName(String sourceFileName) {
/* 158 */     if (sourceFileName == null) {
/* 159 */       return null;
/*     */     }
/* 161 */     String modName = modifyName(sourceFileName);
/* 162 */     if (this.fromPrefix == null || sourceFileName
/* 163 */       .length() < this.prefixLength + this.postfixLength || (!this.fromContainsStar && 
/*     */       
/* 165 */       !modName.equals(modifyName(this.fromPrefix))) || (this.fromContainsStar && (
/*     */ 
/*     */       
/* 168 */       !modName.startsWith(modifyName(this.fromPrefix)) || 
/* 169 */       !modName.endsWith(modifyName(this.fromPostfix)))))
/*     */     {
/*     */       
/* 172 */       return null;
/*     */     }
/* 174 */     return new String[] { this.toPrefix + (
/* 175 */         this.toContainsStar ? (
/* 176 */         extractVariablePart(
/* 177 */           sourceFileName) + this.toPostfix) : 
/* 178 */         "") };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String extractVariablePart(String name) {
/* 188 */     return name.substring(this.prefixLength, name
/* 189 */         .length() - this.postfixLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String modifyName(String name) {
/* 198 */     if (!this.caseSensitive) {
/* 199 */       name = name.toLowerCase();
/*     */     }
/* 201 */     if (this.handleDirSep && 
/* 202 */       name.contains("\\")) {
/* 203 */       name = name.replace('\\', '/');
/*     */     }
/*     */     
/* 206 */     return name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/GlobPatternMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */