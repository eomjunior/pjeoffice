/*    */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*    */ 
/*    */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class X9ECParametersHolder
/*    */ {
/*    */   private X9ECParameters params;
/*    */   
/*    */   public synchronized X9ECParameters getParameters() {
/* 12 */     if (this.params == null)
/*    */     {
/* 14 */       this.params = createParameters();
/*    */     }
/*    */     
/* 17 */     return this.params;
/*    */   }
/*    */   
/*    */   protected abstract X9ECParameters createParameters();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9ECParametersHolder.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */