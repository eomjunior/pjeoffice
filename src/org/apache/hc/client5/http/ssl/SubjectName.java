/*    */ package org.apache.hc.client5.http.ssl;
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
/*    */ final class SubjectName
/*    */ {
/*    */   static final int DNS = 2;
/*    */   static final int IP = 7;
/*    */   private final String value;
/*    */   private final int type;
/*    */   
/*    */   static SubjectName IP(String value) {
/* 40 */     return new SubjectName(value, 7);
/*    */   }
/*    */   
/*    */   static SubjectName DNS(String value) {
/* 44 */     return new SubjectName(value, 2);
/*    */   }
/*    */   
/*    */   SubjectName(String value, int type) {
/* 48 */     this.value = (String)Args.notNull(value, "Value");
/* 49 */     this.type = Args.positive(type, "Type");
/*    */   }
/*    */   
/*    */   public int getType() {
/* 53 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 57 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/SubjectName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */