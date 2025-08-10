/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.sphincs;
/*    */ 
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
/*    */ public class SPHINCSKeyParameters
/*    */   extends AsymmetricKeyParameter
/*    */ {
/*    */   public static final String SHA512_256 = "SHA-512/256";
/*    */   public static final String SHA3_256 = "SHA3-256";
/*    */   private final String treeDigest;
/*    */   
/*    */   protected SPHINCSKeyParameters(boolean paramBoolean, String paramString) {
/* 22 */     super(paramBoolean);
/* 23 */     this.treeDigest = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTreeDigest() {
/* 28 */     return this.treeDigest;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/sphincs/SPHINCSKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */