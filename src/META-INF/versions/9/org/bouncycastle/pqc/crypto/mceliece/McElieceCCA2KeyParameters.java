/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class McElieceCCA2KeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   private String params;
/*    */   
/*    */   public McElieceCCA2KeyParameters(boolean paramBoolean, String paramString) {
/* 15 */     super(paramBoolean);
/* 16 */     this.params = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDigest() {
/* 22 */     return this.params;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2KeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */