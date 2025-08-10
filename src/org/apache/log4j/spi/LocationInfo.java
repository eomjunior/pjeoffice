/*     */ package org.apache.log4j.spi;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class LocationInfo
/*     */   implements Serializable
/*     */ {
/*     */   transient String lineNumber;
/*     */   transient String fileName;
/*     */   transient String className;
/*     */   transient String methodName;
/*     */   public String fullInfo;
/*     */   public static final String NA = "?";
/*     */   static final long serialVersionUID = -1325822038990805636L;
/*  67 */   public static final LocationInfo NA_LOCATION_INFO = new LocationInfo("?", "?", "?", "?");
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
/*     */   public LocationInfo(Throwable t, String fqnOfCallingClass) {
/*  95 */     if (t == null || fqnOfCallingClass == null)
/*     */       return; 
/*     */     try {
/*  98 */       StackTraceElement[] elements = t.getStackTrace();
/*  99 */       String prevClass = "?";
/* 100 */       for (int i = elements.length - 1; i >= 0; i--) {
/* 101 */         String thisClass = elements[i].getClassName();
/* 102 */         if (fqnOfCallingClass.equals(thisClass)) {
/* 103 */           int caller = i + 1;
/* 104 */           if (caller < elements.length) {
/* 105 */             this.className = prevClass;
/* 106 */             this.methodName = elements[caller].getMethodName();
/* 107 */             this.fileName = elements[caller].getFileName();
/* 108 */             if (this.fileName == null) {
/* 109 */               this.fileName = "?";
/*     */             }
/* 111 */             int line = elements[caller].getLineNumber();
/* 112 */             if (line < 0) {
/* 113 */               this.lineNumber = "?";
/*     */             } else {
/* 115 */               this.lineNumber = Integer.toString(line);
/*     */             } 
/* 117 */             StringBuilder buf = new StringBuilder();
/* 118 */             buf.append(this.className);
/* 119 */             buf.append(".");
/* 120 */             buf.append(this.methodName);
/* 121 */             buf.append("(");
/* 122 */             buf.append(this.fileName);
/* 123 */             buf.append(":");
/* 124 */             buf.append(this.lineNumber);
/* 125 */             buf.append(")");
/* 126 */             this.fullInfo = buf.toString();
/*     */           } 
/*     */           return;
/*     */         } 
/* 130 */         prevClass = thisClass;
/*     */       } 
/*     */       return;
/* 133 */     } catch (RuntimeException ex) {
/* 134 */       LogLog.debug("LocationInfo construction failed", ex);
/*     */       return;
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
/*     */   private static final void appendFragment(StringBuilder buf, String fragment) {
/* 148 */     if (fragment == null) {
/* 149 */       buf.append("?");
/*     */     } else {
/* 151 */       buf.append(fragment);
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
/*     */ 
/*     */   
/*     */   public LocationInfo(String file, String classname, String method, String line) {
/* 166 */     this.fileName = file;
/* 167 */     this.className = classname;
/* 168 */     this.methodName = method;
/* 169 */     this.lineNumber = line;
/* 170 */     StringBuilder buf = new StringBuilder();
/* 171 */     appendFragment(buf, classname);
/* 172 */     buf.append(".");
/* 173 */     appendFragment(buf, method);
/* 174 */     buf.append("(");
/* 175 */     appendFragment(buf, file);
/* 176 */     buf.append(":");
/* 177 */     appendFragment(buf, line);
/* 178 */     buf.append(")");
/* 179 */     this.fullInfo = buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 188 */     if (this.fullInfo == null)
/* 189 */       return "?"; 
/* 190 */     if (this.className != null) {
/* 191 */       return this.className;
/*     */     }
/*     */ 
/*     */     
/* 195 */     int iend = this.fullInfo.lastIndexOf('(');
/* 196 */     if (iend == -1) {
/* 197 */       this.className = "?";
/*     */     } else {
/* 199 */       iend = this.fullInfo.lastIndexOf('.', iend);
/* 200 */       int ibegin = 0;
/*     */       
/* 202 */       if (iend == -1) {
/* 203 */         this.className = "?";
/*     */       } else {
/* 205 */         this.className = this.fullInfo.substring(ibegin, iend);
/*     */       } 
/* 207 */     }  return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 218 */     if (this.fullInfo == null)
/* 219 */       return "?"; 
/* 220 */     if (this.fileName == null) {
/* 221 */       int iend = this.fullInfo.lastIndexOf(':');
/* 222 */       if (iend == -1) {
/* 223 */         this.fileName = "?";
/*     */       } else {
/* 225 */         int ibegin = this.fullInfo.lastIndexOf('(', iend - 1);
/* 226 */         this.fileName = this.fullInfo.substring(ibegin + 1, iend);
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLineNumber() {
/* 240 */     if (this.fullInfo == null)
/* 241 */       return "?"; 
/* 242 */     if (this.lineNumber == null) {
/* 243 */       int iend = this.fullInfo.lastIndexOf(')');
/* 244 */       int ibegin = this.fullInfo.lastIndexOf(':', iend - 1);
/* 245 */       if (ibegin == -1) {
/* 246 */         this.lineNumber = "?";
/*     */       } else {
/* 248 */         this.lineNumber = this.fullInfo.substring(ibegin + 1, iend);
/*     */       } 
/* 250 */     }  return this.lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 257 */     if (this.fullInfo == null)
/* 258 */       return "?"; 
/* 259 */     if (this.methodName == null) {
/* 260 */       int iend = this.fullInfo.lastIndexOf('(');
/* 261 */       int ibegin = this.fullInfo.lastIndexOf('.', iend);
/* 262 */       if (ibegin == -1) {
/* 263 */         this.methodName = "?";
/*     */       } else {
/* 265 */         this.methodName = this.fullInfo.substring(ibegin + 1, iend);
/*     */       } 
/* 267 */     }  return this.methodName;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/LocationInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */