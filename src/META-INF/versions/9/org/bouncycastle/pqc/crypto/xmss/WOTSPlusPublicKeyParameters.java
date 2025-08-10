/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusParameters;
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class WOTSPlusPublicKeyParameters
/*    */ {
/*    */   private final byte[][] publicKey;
/*    */   
/*    */   protected WOTSPlusPublicKeyParameters(WOTSPlusParameters paramWOTSPlusParameters, byte[][] paramArrayOfbyte) {
/* 14 */     if (paramWOTSPlusParameters == null)
/*    */     {
/* 16 */       throw new NullPointerException("params == null");
/*    */     }
/* 18 */     if (paramArrayOfbyte == null)
/*    */     {
/* 20 */       throw new NullPointerException("publicKey == null");
/*    */     }
/* 22 */     if (XMSSUtil.hasNullPointer(paramArrayOfbyte))
/*    */     {
/* 24 */       throw new NullPointerException("publicKey byte array == null");
/*    */     }
/* 26 */     if (paramArrayOfbyte.length != paramWOTSPlusParameters.getLen())
/*    */     {
/* 28 */       throw new IllegalArgumentException("wrong publicKey size");
/*    */     }
/* 30 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*    */       
/* 32 */       if ((paramArrayOfbyte[b]).length != paramWOTSPlusParameters.getTreeDigestSize())
/*    */       {
/* 34 */         throw new IllegalArgumentException("wrong publicKey format");
/*    */       }
/*    */     } 
/* 37 */     this.publicKey = XMSSUtil.cloneArray(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[][] toByteArray() {
/* 42 */     return XMSSUtil.cloneArray(this.publicKey);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/WOTSPlusPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */