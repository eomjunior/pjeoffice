/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.hc.client5.http.auth.AuthScope;
/*    */ import org.apache.hc.client5.http.auth.Credentials;
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
/*    */ final class CredentialsMatcher
/*    */ {
/*    */   static Credentials matchCredentials(Map<AuthScope, Credentials> map, AuthScope authScope) {
/* 46 */     Credentials creds = map.get(authScope);
/* 47 */     if (creds == null) {
/*    */ 
/*    */       
/* 50 */       int bestMatchFactor = -1;
/* 51 */       AuthScope bestMatch = null;
/* 52 */       for (AuthScope current : map.keySet()) {
/* 53 */         int factor = authScope.match(current);
/* 54 */         if (factor > bestMatchFactor) {
/* 55 */           bestMatchFactor = factor;
/* 56 */           bestMatch = current;
/*    */         } 
/*    */       } 
/* 59 */       if (bestMatch != null) {
/* 60 */         creds = map.get(bestMatch);
/*    */       }
/*    */     } 
/* 63 */     return creds;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/CredentialsMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */