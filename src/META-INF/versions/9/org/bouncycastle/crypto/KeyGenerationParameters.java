/*    */ package META-INF.versions.9.org.bouncycastle.crypto;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import org.bouncycastle.crypto.CryptoServicesRegistrar;
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
/*    */ public class KeyGenerationParameters
/*    */ {
/*    */   private SecureRandom random;
/*    */   private int strength;
/*    */   
/*    */   public KeyGenerationParameters(SecureRandom paramSecureRandom, int paramInt) {
/* 24 */     this.random = CryptoServicesRegistrar.getSecureRandom(paramSecureRandom);
/* 25 */     this.strength = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SecureRandom getRandom() {
/* 36 */     return this.random;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getStrength() {
/* 46 */     return this.strength;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/KeyGenerationParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */