/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DHValidationParameters
/*    */ {
/*    */   private byte[] seed;
/*    */   private int counter;
/*    */   
/*    */   public DHValidationParameters(byte[] paramArrayOfbyte, int paramInt) {
/* 14 */     this.seed = Arrays.clone(paramArrayOfbyte);
/* 15 */     this.counter = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCounter() {
/* 20 */     return this.counter;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getSeed() {
/* 25 */     return Arrays.clone(this.seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 31 */     if (!(paramObject instanceof org.bouncycastle.crypto.params.DHValidationParameters))
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */     
/* 36 */     org.bouncycastle.crypto.params.DHValidationParameters dHValidationParameters = (org.bouncycastle.crypto.params.DHValidationParameters)paramObject;
/*    */     
/* 38 */     if (dHValidationParameters.counter != this.counter)
/*    */     {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     return Arrays.areEqual(this.seed, dHValidationParameters.seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 48 */     return this.counter ^ Arrays.hashCode(this.seed);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/DHValidationParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */