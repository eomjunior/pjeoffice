/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.qtesla;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.pqc.crypto.qtesla.QTESLASecurityCategory;
/*    */ import org.bouncycastle.util.Arrays;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class QTESLAPublicKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   private int securityCategory;
/*    */   private byte[] publicKey;
/*    */   
/*    */   public QTESLAPublicKeyParameters(int paramInt, byte[] paramArrayOfbyte) {
/* 30 */     super(false);
/*    */     
/* 32 */     if (paramArrayOfbyte.length != QTESLASecurityCategory.getPublicSize(paramInt))
/*    */     {
/* 34 */       throw new IllegalArgumentException("invalid key size for security category");
/*    */     }
/*    */     
/* 37 */     this.securityCategory = paramInt;
/* 38 */     this.publicKey = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSecurityCategory() {
/* 49 */     return this.securityCategory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getPublicData() {
/* 59 */     return Arrays.clone(this.publicKey);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/qtesla/QTESLAPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */