/*    */ package org.apache.hc.client5.http.fluent;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.http.NameValuePair;
/*    */ import org.apache.hc.core5.http.message.BasicNameValuePair;
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
/*    */ public class Form
/*    */ {
/*    */   private final List<NameValuePair> params;
/*    */   
/*    */   public static Form form() {
/* 45 */     return new Form();
/*    */   }
/*    */ 
/*    */   
/*    */   Form() {
/* 50 */     this.params = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public Form add(String name, String value) {
/* 54 */     this.params.add(new BasicNameValuePair(name, value));
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public List<NameValuePair> build() {
/* 59 */     return new ArrayList<>(this.params);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/Form.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */