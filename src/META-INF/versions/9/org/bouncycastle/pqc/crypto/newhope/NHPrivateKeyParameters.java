/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.newhope;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ public class NHPrivateKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   final short[] secData;
/*    */   
/*    */   public NHPrivateKeyParameters(short[] paramArrayOfshort) {
/* 13 */     super(true);
/*    */     
/* 15 */     this.secData = Arrays.clone(paramArrayOfshort);
/*    */   }
/*    */ 
/*    */   
/*    */   public short[] getSecData() {
/* 20 */     return Arrays.clone(this.secData);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/newhope/NHPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */