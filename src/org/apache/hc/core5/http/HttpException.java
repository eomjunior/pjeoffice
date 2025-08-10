/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpException
/*     */   extends Exception
/*     */ {
/*     */   private static final int FIRST_VALID_CHAR = 32;
/*     */   private static final long serialVersionUID = -5437299376222011036L;
/*     */   
/*     */   static String clean(String message) {
/*  47 */     char[] chars = message.toCharArray();
/*     */     
/*     */     int i;
/*  50 */     for (i = 0; i < chars.length && 
/*  51 */       chars[i] >= ' '; i++);
/*     */ 
/*     */ 
/*     */     
/*  55 */     if (i == chars.length) {
/*  56 */       return message;
/*     */     }
/*  58 */     StringBuilder builder = new StringBuilder(chars.length * 2);
/*  59 */     for (i = 0; i < chars.length; i++) {
/*  60 */       char ch = chars[i];
/*  61 */       if (ch < ' ') {
/*  62 */         builder.append("[0x");
/*  63 */         String hexString = Integer.toHexString(i);
/*  64 */         if (hexString.length() == 1) {
/*  65 */           builder.append("0");
/*     */         }
/*  67 */         builder.append(hexString);
/*  68 */         builder.append("]");
/*     */       } else {
/*  70 */         builder.append(ch);
/*     */       } 
/*     */     } 
/*  73 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpException() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpException(String message) {
/*  89 */     super(clean(message));
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
/*     */   public HttpException(String format, Object... args) {
/* 101 */     super(clean(String.format(format, args)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpException(String message, Throwable cause) {
/* 112 */     super(clean(message));
/* 113 */     initCause(cause);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */