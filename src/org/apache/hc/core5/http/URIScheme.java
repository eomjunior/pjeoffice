/*    */ package org.apache.hc.core5.http;
/*    */ 
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
/*    */ public enum URIScheme
/*    */ {
/* 39 */   HTTP("http"), HTTPS("https");
/*    */   
/*    */   public final String id;
/*    */   
/*    */   URIScheme(String id) {
/* 44 */     this.id = (String)Args.notBlank(id, "id");
/*    */   }
/*    */   
/*    */   public String getId() {
/* 48 */     return this.id;
/*    */   }
/*    */   
/*    */   public boolean same(String scheme) {
/* 52 */     return this.id.equalsIgnoreCase(scheme);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/URIScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */