/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.rainbow;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RainbowKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   private int docLength;
/*    */   
/*    */   public RainbowKeyParameters(boolean paramBoolean, int paramInt) {
/* 14 */     super(paramBoolean);
/* 15 */     this.docLength = paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDocLength() {
/* 23 */     return this.docLength;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/rainbow/RainbowKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */