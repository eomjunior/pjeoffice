/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSAddress;
/*    */ import org.bouncycastle.util.Pack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LTreeAddress
/*    */   extends XMSSAddress
/*    */ {
/*    */   private static final int TYPE = 1;
/*    */   private final int lTreeAddress;
/*    */   private final int treeHeight;
/*    */   private final int treeIndex;
/*    */   
/*    */   private LTreeAddress(Builder paramBuilder) {
/* 18 */     super((XMSSAddress.Builder)paramBuilder);
/* 19 */     this.lTreeAddress = Builder.access$000(paramBuilder);
/* 20 */     this.treeHeight = Builder.access$100(paramBuilder);
/* 21 */     this.treeIndex = Builder.access$200(paramBuilder);
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
/*    */   protected byte[] toByteArray() {
/* 63 */     byte[] arrayOfByte = super.toByteArray();
/* 64 */     Pack.intToBigEndian(this.lTreeAddress, arrayOfByte, 16);
/* 65 */     Pack.intToBigEndian(this.treeHeight, arrayOfByte, 20);
/* 66 */     Pack.intToBigEndian(this.treeIndex, arrayOfByte, 24);
/* 67 */     return arrayOfByte;
/*    */   }
/*    */   
/*    */   protected int getLTreeAddress() {
/* 71 */     return this.lTreeAddress;
/*    */   }
/*    */   
/*    */   protected int getTreeHeight() {
/* 75 */     return this.treeHeight;
/*    */   }
/*    */   
/*    */   protected int getTreeIndex() {
/* 79 */     return this.treeIndex;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/LTreeAddress.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */