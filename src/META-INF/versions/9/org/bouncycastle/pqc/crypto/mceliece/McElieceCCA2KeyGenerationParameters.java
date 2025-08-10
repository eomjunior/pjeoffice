/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import org.bouncycastle.crypto.KeyGenerationParameters;
/*    */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class McElieceCCA2KeyGenerationParameters
/*    */   extends KeyGenerationParameters
/*    */ {
/*    */   private McElieceCCA2Parameters params;
/*    */   
/*    */   public McElieceCCA2KeyGenerationParameters(SecureRandom paramSecureRandom, McElieceCCA2Parameters paramMcElieceCCA2Parameters) {
/* 17 */     super(paramSecureRandom, 128);
/* 18 */     this.params = paramMcElieceCCA2Parameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public McElieceCCA2Parameters getParameters() {
/* 23 */     return this.params;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2KeyGenerationParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */