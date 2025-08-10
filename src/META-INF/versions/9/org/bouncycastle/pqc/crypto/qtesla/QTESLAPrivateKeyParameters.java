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
/*    */ public final class QTESLAPrivateKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   private int securityCategory;
/*    */   private byte[] privateKey;
/*    */   
/*    */   public QTESLAPrivateKeyParameters(int paramInt, byte[] paramArrayOfbyte) {
/* 30 */     super(true);
/*    */     
/* 32 */     if (paramArrayOfbyte.length != QTESLASecurityCategory.getPrivateSize(paramInt))
/*    */     {
/* 34 */       throw new IllegalArgumentException("invalid key size for security category");
/*    */     }
/*    */     
/* 37 */     this.securityCategory = paramInt;
/* 38 */     this.privateKey = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSecurityCategory() {
/* 48 */     return this.securityCategory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getSecret() {
/* 58 */     return Arrays.clone(this.privateKey);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/qtesla/QTESLAPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */