/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class Location
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String fileName;
/*     */   private final int lineNumber;
/*     */   private final int columnNumber;
/*  44 */   public static final Location UNKNOWN_LOCATION = new Location();
/*     */   
/*  46 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Location() {
/*  52 */     this(null, 0, 0);
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
/*     */   public Location(String fileName) {
/*  64 */     this(fileName, 0, 0);
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
/*     */   public Location(Locator loc) {
/*  76 */     this(loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber());
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
/*     */   public Location(String fileName, int lineNumber, int columnNumber) {
/*  92 */     if (fileName != null && fileName.startsWith("file:")) {
/*  93 */       this.fileName = FILE_UTILS.fromURI(fileName);
/*     */     } else {
/*  95 */       this.fileName = fileName;
/*     */     } 
/*  97 */     this.lineNumber = lineNumber;
/*  98 */     this.columnNumber = columnNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 106 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 114 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnNumber() {
/* 122 */     return this.columnNumber;
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
/*     */   public String toString() {
/* 137 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 139 */     if (this.fileName != null) {
/* 140 */       buf.append(this.fileName);
/*     */       
/* 142 */       if (this.lineNumber != 0) {
/* 143 */         buf.append(":");
/* 144 */         buf.append(this.lineNumber);
/*     */       } 
/*     */       
/* 147 */       buf.append(": ");
/*     */     } 
/*     */     
/* 150 */     return buf.toString();
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
/*     */   public boolean equals(Object other) {
/* 162 */     return (this == other || (other != null && other.getClass() == getClass() && 
/* 163 */       toString().equals(other.toString())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 173 */     return Objects.hash(new Object[] { this.fileName, Integer.valueOf(this.lineNumber) });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Location.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */