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
/*    */ final class OTSHashAddress
/*    */   extends XMSSAddress
/*    */ {
/*    */   private static final int TYPE = 0;
/*    */   private final int otsAddress;
/*    */   private final int chainAddress;
/*    */   private final int hashAddress;
/*    */   
/*    */   private OTSHashAddress(Builder paramBuilder) {
/* 20 */     super((XMSSAddress.Builder)paramBuilder);
/* 21 */     this.otsAddress = Builder.access$000(paramBuilder);
/* 22 */     this.chainAddress = Builder.access$100(paramBuilder);
/* 23 */     this.hashAddress = Builder.access$200(paramBuilder);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected byte[] toByteArray() {
/* 74 */     byte[] arrayOfByte = super.toByteArray();
/* 75 */     Pack.intToBigEndian(this.otsAddress, arrayOfByte, 16);
/* 76 */     Pack.intToBigEndian(this.chainAddress, arrayOfByte, 20);
/* 77 */     Pack.intToBigEndian(this.hashAddress, arrayOfByte, 24);
/* 78 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getOTSAddress() {
/* 83 */     return this.otsAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getChainAddress() {
/* 88 */     return this.chainAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getHashAddress() {
/* 93 */     return this.hashAddress;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/OTSHashAddress.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */