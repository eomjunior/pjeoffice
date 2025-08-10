/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.xmss.XMSSAddress;
/*    */ import org.bouncycastle.util.Pack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class HashTreeAddress
/*    */   extends XMSSAddress
/*    */ {
/*    */   private static final int TYPE = 2;
/*    */   private static final int PADDING = 0;
/*    */   private final int padding;
/*    */   private final int treeHeight;
/*    */   private final int treeIndex;
/*    */   
/*    */   private HashTreeAddress(Builder paramBuilder) {
/* 21 */     super((XMSSAddress.Builder)paramBuilder);
/* 22 */     this.padding = 0;
/* 23 */     this.treeHeight = Builder.access$000(paramBuilder);
/* 24 */     this.treeIndex = Builder.access$100(paramBuilder);
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
/* 68 */     byte[] arrayOfByte = super.toByteArray();
/* 69 */     Pack.intToBigEndian(this.padding, arrayOfByte, 16);
/* 70 */     Pack.intToBigEndian(this.treeHeight, arrayOfByte, 20);
/* 71 */     Pack.intToBigEndian(this.treeIndex, arrayOfByte, 24);
/* 72 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getPadding() {
/* 77 */     return this.padding;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getTreeHeight() {
/* 82 */     return this.treeHeight;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getTreeIndex() {
/* 87 */     return this.treeIndex;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/HashTreeAddress.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */