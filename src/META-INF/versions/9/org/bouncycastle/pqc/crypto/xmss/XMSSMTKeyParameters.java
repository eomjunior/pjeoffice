/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ 
/*    */ 
/*    */ public class XMSSMTKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   private final String treeDigest;
/*    */   
/*    */   public XMSSMTKeyParameters(boolean paramBoolean, String paramString) {
/* 12 */     super(paramBoolean);
/* 13 */     this.treeDigest = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTreeDigest() {
/* 18 */     return this.treeDigest;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSMTKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */