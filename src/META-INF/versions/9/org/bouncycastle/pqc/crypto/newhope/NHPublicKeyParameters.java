/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.newhope;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ public class NHPublicKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   final byte[] pubData;
/*    */   
/*    */   public NHPublicKeyParameters(byte[] paramArrayOfbyte) {
/* 13 */     super(false);
/* 14 */     this.pubData = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getPubData() {
/* 24 */     return Arrays.clone(this.pubData);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/newhope/NHPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */