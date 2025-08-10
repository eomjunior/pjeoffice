/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.sphincs;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSKeyParameters;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ public class SPHINCSPrivateKeyParameters
/*    */   extends SPHINCSKeyParameters
/*    */ {
/*    */   private final byte[] keyData;
/*    */   
/*    */   public SPHINCSPrivateKeyParameters(byte[] paramArrayOfbyte) {
/* 12 */     super(true, null);
/* 13 */     this.keyData = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public SPHINCSPrivateKeyParameters(byte[] paramArrayOfbyte, String paramString) {
/* 18 */     super(true, paramString);
/* 19 */     this.keyData = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getKeyData() {
/* 24 */     return Arrays.clone(this.keyData);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/sphincs/SPHINCSPrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */