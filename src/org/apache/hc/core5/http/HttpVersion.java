/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class HttpVersion
/*     */   extends ProtocolVersion
/*     */ {
/*     */   private static final long serialVersionUID = -5856653513894415344L;
/*     */   public static final String HTTP = "HTTP";
/*  52 */   public static final HttpVersion HTTP_0_9 = new HttpVersion(0, 9);
/*     */ 
/*     */   
/*  55 */   public static final HttpVersion HTTP_1_0 = new HttpVersion(1, 0);
/*     */ 
/*     */   
/*  58 */   public static final HttpVersion HTTP_1_1 = new HttpVersion(1, 1);
/*     */ 
/*     */   
/*  61 */   public static final HttpVersion HTTP_2_0 = new HttpVersion(2, 0);
/*  62 */   public static final HttpVersion HTTP_2 = HTTP_2_0;
/*     */ 
/*     */   
/*  65 */   public static final HttpVersion DEFAULT = HTTP_1_1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final HttpVersion[] ALL = new HttpVersion[] { HTTP_0_9, HTTP_1_0, HTTP_1_1, HTTP_2_0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpVersion get(int major, int minor) {
/*  83 */     for (int i = 0; i < ALL.length; i++) {
/*  84 */       if (ALL[i].equals(major, minor)) {
/*  85 */         return ALL[i];
/*     */       }
/*     */     } 
/*     */     
/*  89 */     return new HttpVersion(major, minor);
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
/*     */   public HttpVersion(int major, int minor) {
/* 101 */     super("HTTP", major, minor);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/HttpVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */