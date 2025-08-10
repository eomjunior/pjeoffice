/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusParameters;
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class WOTSPlusSignature
/*    */ {
/*    */   private byte[][] signature;
/*    */   
/*    */   protected WOTSPlusSignature(WOTSPlusParameters paramWOTSPlusParameters, byte[][] paramArrayOfbyte) {
/* 14 */     if (paramWOTSPlusParameters == null)
/*    */     {
/* 16 */       throw new NullPointerException("params == null");
/*    */     }
/* 18 */     if (paramArrayOfbyte == null)
/*    */     {
/* 20 */       throw new NullPointerException("signature == null");
/*    */     }
/* 22 */     if (XMSSUtil.hasNullPointer(paramArrayOfbyte))
/*    */     {
/* 24 */       throw new NullPointerException("signature byte array == null");
/*    */     }
/* 26 */     if (paramArrayOfbyte.length != paramWOTSPlusParameters.getLen())
/*    */     {
/* 28 */       throw new IllegalArgumentException("wrong signature size");
/*    */     }
/* 30 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*    */       
/* 32 */       if ((paramArrayOfbyte[b]).length != paramWOTSPlusParameters.getTreeDigestSize())
/*    */       {
/* 34 */         throw new IllegalArgumentException("wrong signature format");
/*    */       }
/*    */     } 
/* 37 */     this.signature = XMSSUtil.cloneArray(paramArrayOfbyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[][] toByteArray() {
/* 42 */     return XMSSUtil.cloneArray(this.signature);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/WOTSPlusSignature.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */