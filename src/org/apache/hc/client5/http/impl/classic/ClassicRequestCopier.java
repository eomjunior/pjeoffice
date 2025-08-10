/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.apache.hc.client5.http.impl.MessageCopier;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
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
/*    */ @Deprecated
/*    */ public final class ClassicRequestCopier
/*    */   implements MessageCopier<ClassicHttpRequest>
/*    */ {
/* 45 */   public static final ClassicRequestCopier INSTANCE = new ClassicRequestCopier();
/*    */ 
/*    */   
/*    */   public ClassicHttpRequest copy(ClassicHttpRequest original) {
/* 49 */     if (original == null) {
/* 50 */       return null;
/*    */     }
/* 52 */     BasicClassicHttpRequest copy = new BasicClassicHttpRequest(original.getMethod(), null, original.getPath());
/* 53 */     copy.setScheme(original.getScheme());
/* 54 */     copy.setAuthority(original.getAuthority());
/* 55 */     copy.setVersion(original.getVersion());
/* 56 */     for (Iterator<Header> it = original.headerIterator(); it.hasNext();) {
/* 57 */       copy.addHeader(it.next());
/*    */     }
/* 59 */     copy.setEntity(original.getEntity());
/* 60 */     return (ClassicHttpRequest)copy;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ClassicRequestCopier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */