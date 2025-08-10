/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XMSSKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   public static final String SHA_256 = "SHA-256";
/*    */   public static final String SHA_512 = "SHA-512";
/*    */   public static final String SHAKE128 = "SHAKE128";
/*    */   public static final String SHAKE256 = "SHAKE256";
/*    */   private final String treeDigest;
/*    */   
/*    */   public XMSSKeyParameters(boolean paramBoolean, String paramString) {
/* 17 */     super(paramBoolean);
/* 18 */     this.treeDigest = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTreeDigest() {
/* 23 */     return this.treeDigest;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */