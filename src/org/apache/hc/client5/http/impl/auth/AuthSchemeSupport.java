/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.UnsupportedCharsetException;
/*    */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*    */ import org.apache.hc.core5.annotation.Internal;
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
/*    */ @Internal
/*    */ public class AuthSchemeSupport
/*    */ {
/*    */   public static Charset parseCharset(String charsetName, Charset defaultCharset) throws AuthenticationException {
/*    */     try {
/* 44 */       return (charsetName != null) ? Charset.forName(charsetName) : defaultCharset;
/* 45 */     } catch (UnsupportedCharsetException ex) {
/* 46 */       throw new AuthenticationException("Unsupported charset: " + charsetName);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/AuthSchemeSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */