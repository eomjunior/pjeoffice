/*    */ package org.apache.hc.client5.http.classic.methods;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import org.apache.hc.core5.http.HeaderElement;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.MessageHeaders;
/*    */ import org.apache.hc.core5.http.message.MessageSupport;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ public class HttpOptions
/*    */   extends HttpUriRequestBase
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String METHOD_NAME = "OPTIONS";
/*    */   
/*    */   public HttpOptions(URI uri) {
/* 58 */     super("OPTIONS", uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpOptions(String uri) {
/* 68 */     this(URI.create(uri));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethod() {
/* 73 */     return "OPTIONS";
/*    */   }
/*    */   
/*    */   public Set<String> getAllowedMethods(HttpResponse response) {
/* 77 */     Args.notNull(response, "HTTP response");
/*    */     
/* 79 */     Iterator<HeaderElement> it = MessageSupport.iterate((MessageHeaders)response, "Allow");
/* 80 */     Set<String> methods = new HashSet<>();
/* 81 */     while (it.hasNext()) {
/* 82 */       HeaderElement element = it.next();
/* 83 */       methods.add(element.getName());
/*    */     } 
/* 85 */     return methods;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/HttpOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */