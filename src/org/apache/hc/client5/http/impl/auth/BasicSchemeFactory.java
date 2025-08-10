/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.hc.client5.http.auth.AuthScheme;
/*    */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BasicSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/* 50 */   public static final BasicSchemeFactory INSTANCE = new BasicSchemeFactory();
/*    */ 
/*    */ 
/*    */   
/*    */   private final Charset charset;
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicSchemeFactory(Charset charset) {
/* 59 */     this.charset = charset;
/*    */   }
/*    */   
/*    */   public BasicSchemeFactory() {
/* 63 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthScheme create(HttpContext context) {
/* 68 */     return new BasicScheme(this.charset);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/BasicSchemeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */