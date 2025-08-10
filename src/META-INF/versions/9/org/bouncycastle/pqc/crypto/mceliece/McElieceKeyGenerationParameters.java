/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import org.bouncycastle.crypto.KeyGenerationParameters;
/*    */ import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class McElieceKeyGenerationParameters
/*    */   extends KeyGenerationParameters
/*    */ {
/*    */   private McElieceParameters params;
/*    */   
/*    */   public McElieceKeyGenerationParameters(SecureRandom paramSecureRandom, McElieceParameters paramMcElieceParameters) {
/* 17 */     super(paramSecureRandom, 256);
/* 18 */     this.params = paramMcElieceParameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public McElieceParameters getParameters() {
/* 23 */     return this.params;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceKeyGenerationParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */