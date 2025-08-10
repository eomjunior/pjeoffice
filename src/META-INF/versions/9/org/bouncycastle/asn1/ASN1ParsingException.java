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
/*    */ public class ASN1ParsingException
/*    */   extends IllegalStateException
/*    */ {
/*    */   private Throwable cause;
/*    */   
/*    */   public ASN1ParsingException(String paramString) {
/* 18 */     super(paramString);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ASN1ParsingException(String paramString, Throwable paramThrowable) {
/* 29 */     super(paramString);
/* 30 */     this.cause = paramThrowable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 40 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1ParsingException.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */