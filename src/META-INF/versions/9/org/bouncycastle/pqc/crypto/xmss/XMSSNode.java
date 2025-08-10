/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class XMSSNode
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final int height;
/*    */   private final byte[] value;
/*    */   
/*    */   protected XMSSNode(int paramInt, byte[] paramArrayOfbyte) {
/* 19 */     this.height = paramInt;
/* 20 */     this.value = paramArrayOfbyte;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 25 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getValue() {
/* 30 */     return XMSSUtil.cloneArray(this.value);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSNode.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */