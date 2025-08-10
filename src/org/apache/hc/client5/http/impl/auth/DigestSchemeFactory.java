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
/*    */ public class DigestSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/* 50 */   public static final DigestSchemeFactory INSTANCE = new DigestSchemeFactory();
/*    */ 
/*    */   
/*    */   private final Charset charset;
/*    */ 
/*    */ 
/*    */   
/*    */   public DigestSchemeFactory(Charset charset) {
/* 58 */     this.charset = charset;
/*    */   }
/*    */   
/*    */   public DigestSchemeFactory() {
/* 62 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthScheme create(HttpContext context) {
/* 67 */     return new DigestScheme(this.charset);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/DigestSchemeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */