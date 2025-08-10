/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.sphincs;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSKeyParameters;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ public class SPHINCSPublicKeyParameters
/*    */   extends SPHINCSKeyParameters
/*    */ {
/*    */   private final byte[] keyData;
/*    */   
/*    */   public SPHINCSPublicKeyParameters(byte[] paramArrayOfbyte) {
/* 12 */     super(false, null);
/* 13 */     this.keyData = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SPHINCSPublicKeyParameters(byte[] paramArrayOfbyte, String paramString) {
/* 19 */     super(false, paramString);
/* 20 */     this.keyData = Arrays.clone(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getKeyData() {
/* 25 */     return Arrays.clone(this.keyData);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/sphincs/SPHINCSPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */