/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HeaderElement;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class BasicHeaderValueFormatter
/*     */   implements HeaderValueFormatter
/*     */ {
/*  45 */   public static final BasicHeaderValueFormatter INSTANCE = new BasicHeaderValueFormatter();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SEPARATORS = " ;,:@()<>\\\"/[]?={}\t";
/*     */ 
/*     */   
/*     */   private static final String UNSAFE_CHARS = "\"\\";
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatElements(CharArrayBuffer buffer, HeaderElement[] elems, boolean quote) {
/*  57 */     Args.notNull(buffer, "Char array buffer");
/*  58 */     Args.notNull(elems, "Header element array");
/*     */     
/*  60 */     for (int i = 0; i < elems.length; i++) {
/*  61 */       if (i > 0) {
/*  62 */         buffer.append(", ");
/*     */       }
/*  64 */       formatHeaderElement(buffer, elems[i], quote);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatHeaderElement(CharArrayBuffer buffer, HeaderElement elem, boolean quote) {
/*  71 */     Args.notNull(buffer, "Char array buffer");
/*  72 */     Args.notNull(elem, "Header element");
/*     */     
/*  74 */     buffer.append(elem.getName());
/*  75 */     String value = elem.getValue();
/*  76 */     if (value != null) {
/*  77 */       buffer.append('=');
/*  78 */       formatValue(buffer, value, quote);
/*     */     } 
/*     */     
/*  81 */     int c = elem.getParameterCount();
/*  82 */     if (c > 0) {
/*  83 */       for (int i = 0; i < c; i++) {
/*  84 */         buffer.append("; ");
/*  85 */         formatNameValuePair(buffer, elem.getParameter(i), quote);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatParameters(CharArrayBuffer buffer, NameValuePair[] nvps, boolean quote) {
/*  93 */     Args.notNull(buffer, "Char array buffer");
/*  94 */     Args.notNull(nvps, "Header parameter array");
/*     */     
/*  96 */     for (int i = 0; i < nvps.length; i++) {
/*  97 */       if (i > 0) {
/*  98 */         buffer.append("; ");
/*     */       }
/* 100 */       formatNameValuePair(buffer, nvps[i], quote);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void formatNameValuePair(CharArrayBuffer buffer, NameValuePair nvp, boolean quote) {
/* 107 */     Args.notNull(buffer, "Char array buffer");
/* 108 */     Args.notNull(nvp, "Name / value pair");
/*     */     
/* 110 */     buffer.append(nvp.getName());
/* 111 */     String value = nvp.getValue();
/* 112 */     if (value != null) {
/* 113 */       buffer.append('=');
/* 114 */       formatValue(buffer, value, quote);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void formatValue(CharArrayBuffer buffer, String value, boolean quote) {
/* 120 */     boolean quoteFlag = quote;
/* 121 */     if (!quoteFlag) {
/* 122 */       for (int j = 0; j < value.length() && !quoteFlag; j++) {
/* 123 */         quoteFlag = isSeparator(value.charAt(j));
/*     */       }
/*     */     }
/*     */     
/* 127 */     if (quoteFlag) {
/* 128 */       buffer.append('"');
/*     */     }
/* 130 */     for (int i = 0; i < value.length(); i++) {
/* 131 */       char ch = value.charAt(i);
/* 132 */       if (isUnsafe(ch)) {
/* 133 */         buffer.append('\\');
/*     */       }
/* 135 */       buffer.append(ch);
/*     */     } 
/* 137 */     if (quoteFlag) {
/* 138 */       buffer.append('"');
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isSeparator(char ch) {
/* 143 */     return (" ;,:@()<>\\\"/[]?={}\t".indexOf(ch) >= 0);
/*     */   }
/*     */   
/*     */   boolean isUnsafe(char ch) {
/* 147 */     return ("\"\\".indexOf(ch) >= 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHeaderValueFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */