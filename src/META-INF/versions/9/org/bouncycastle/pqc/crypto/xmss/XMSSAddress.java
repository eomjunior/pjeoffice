/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.util.Pack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class XMSSAddress
/*    */ {
/*    */   private final int layerAddress;
/*    */   private final long treeAddress;
/*    */   private final int type;
/*    */   private final int keyAndMask;
/*    */   
/*    */   protected XMSSAddress(Builder paramBuilder) {
/* 18 */     this.layerAddress = Builder.access$000(paramBuilder);
/* 19 */     this.treeAddress = Builder.access$100(paramBuilder);
/* 20 */     this.type = Builder.access$200(paramBuilder);
/* 21 */     this.keyAndMask = Builder.access$300(paramBuilder);
/*    */   }
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
/*    */   
/*    */   protected byte[] toByteArray() {
/* 65 */     byte[] arrayOfByte = new byte[32];
/* 66 */     Pack.intToBigEndian(this.layerAddress, arrayOfByte, 0);
/* 67 */     Pack.longToBigEndian(this.treeAddress, arrayOfByte, 4);
/* 68 */     Pack.intToBigEndian(this.type, arrayOfByte, 12);
/* 69 */     Pack.intToBigEndian(this.keyAndMask, arrayOfByte, 28);
/* 70 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final int getLayerAddress() {
/* 75 */     return this.layerAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final long getTreeAddress() {
/* 80 */     return this.treeAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getType() {
/* 85 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getKeyAndMask() {
/* 90 */     return this.keyAndMask;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSAddress.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */