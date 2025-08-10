/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DSAValidationParameters
/*    */ {
/*    */   private int usageIndex;
/*    */   private byte[] seed;
/*    */   private int counter;
/*    */   
/*    */   public DSAValidationParameters(byte[] paramArrayOfbyte, int paramInt) {
/* 15 */     this(paramArrayOfbyte, paramInt, -1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DSAValidationParameters(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/* 23 */     this.seed = Arrays.clone(paramArrayOfbyte);
/* 24 */     this.counter = paramInt1;
/* 25 */     this.usageIndex = paramInt2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCounter() {
/* 30 */     return this.counter;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getSeed() {
/* 35 */     return Arrays.clone(this.seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUsageIndex() {
/* 40 */     return this.usageIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 45 */     return this.counter ^ Arrays.hashCode(this.seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 51 */     if (!(paramObject instanceof org.bouncycastle.crypto.params.DSAValidationParameters))
/*    */     {
/* 53 */       return false;
/*    */     }
/*    */     
/* 56 */     org.bouncycastle.crypto.params.DSAValidationParameters dSAValidationParameters = (org.bouncycastle.crypto.params.DSAValidationParameters)paramObject;
/*    */     
/* 58 */     if (dSAValidationParameters.counter != this.counter)
/*    */     {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     return Arrays.areEqual(this.seed, dSAValidationParameters.seed);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/DSAValidationParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */