/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import org.bouncycastle.crypto.CipherParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsymmetricKeyParameter
/*    */   implements CipherParameters
/*    */ {
/*    */   boolean privateKey;
/*    */   
/*    */   public AsymmetricKeyParameter(boolean paramBoolean) {
/* 13 */     this.privateKey = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPrivate() {
/* 18 */     return this.privateKey;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/AsymmetricKeyParameter.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */