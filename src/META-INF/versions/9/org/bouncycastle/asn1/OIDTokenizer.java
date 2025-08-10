/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
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
/*    */ public class OIDTokenizer
/*    */ {
/*    */   private String oid;
/*    */   private int index;
/*    */   
/*    */   public OIDTokenizer(String paramString) {
/* 22 */     this.oid = paramString;
/* 23 */     this.index = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasMoreTokens() {
/* 33 */     return (this.index != -1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String nextToken() {
/* 43 */     if (this.index == -1)
/*    */     {
/* 45 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 49 */     int i = this.oid.indexOf('.', this.index);
/*    */     
/* 51 */     if (i == -1) {
/*    */       
/* 53 */       String str1 = this.oid.substring(this.index);
/* 54 */       this.index = -1;
/* 55 */       return str1;
/*    */     } 
/*    */     
/* 58 */     String str = this.oid.substring(this.index, i);
/*    */     
/* 60 */     this.index = i + 1;
/* 61 */     return str;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/OIDTokenizer.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */