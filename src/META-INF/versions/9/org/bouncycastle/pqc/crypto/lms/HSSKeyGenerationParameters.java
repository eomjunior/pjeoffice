/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import org.bouncycastle.crypto.KeyGenerationParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LmsUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HSSKeyGenerationParameters
/*    */   extends KeyGenerationParameters
/*    */ {
/*    */   private final LMSParameters[] lmsParameters;
/*    */   
/*    */   public HSSKeyGenerationParameters(LMSParameters[] paramArrayOfLMSParameters, SecureRandom paramSecureRandom) {
/* 22 */     super(paramSecureRandom, LmsUtils.calculateStrength(paramArrayOfLMSParameters[0]));
/* 23 */     if (paramArrayOfLMSParameters.length == 0 || paramArrayOfLMSParameters.length > 8)
/*    */     {
/* 25 */       throw new IllegalArgumentException("lmsParameters length should be between 1 and 8 inclusive");
/*    */     }
/* 27 */     this.lmsParameters = paramArrayOfLMSParameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDepth() {
/* 32 */     return this.lmsParameters.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public LMSParameters[] getLmsParameters() {
/* 37 */     return this.lmsParameters;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/HSSKeyGenerationParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */