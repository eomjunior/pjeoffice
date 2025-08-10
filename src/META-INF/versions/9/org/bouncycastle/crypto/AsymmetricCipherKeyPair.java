/*    */ package META-INF.versions.9.org.bouncycastle.crypto;
/*    */ 
/*    */ import org.bouncycastle.crypto.CipherParameters;
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
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
/*    */ public class AsymmetricCipherKeyPair
/*    */ {
/*    */   private AsymmetricKeyParameter publicParam;
/*    */   private AsymmetricKeyParameter privateParam;
/*    */   
/*    */   public AsymmetricCipherKeyPair(AsymmetricKeyParameter paramAsymmetricKeyParameter1, AsymmetricKeyParameter paramAsymmetricKeyParameter2) {
/* 23 */     this.publicParam = paramAsymmetricKeyParameter1;
/* 24 */     this.privateParam = paramAsymmetricKeyParameter2;
/*    */   }
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
/*    */   public AsymmetricCipherKeyPair(CipherParameters paramCipherParameters1, CipherParameters paramCipherParameters2) {
/* 38 */     this.publicParam = (AsymmetricKeyParameter)paramCipherParameters1;
/* 39 */     this.privateParam = (AsymmetricKeyParameter)paramCipherParameters2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsymmetricKeyParameter getPublic() {
/* 49 */     return this.publicParam;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsymmetricKeyParameter getPrivate() {
/* 59 */     return this.privateParam;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/AsymmetricCipherKeyPair.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */