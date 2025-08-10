/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import org.bouncycastle.math.ec.ECLookupTable;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.math.ec.PreCompInfo;
/*    */ 
/*    */ public class FixedPointPreCompInfo implements PreCompInfo {
/*  8 */   protected ECPoint offset = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   protected ECLookupTable lookupTable = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   protected int width = -1;
/*    */ 
/*    */   
/*    */   public ECLookupTable getLookupTable() {
/* 24 */     return this.lookupTable;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLookupTable(ECLookupTable paramECLookupTable) {
/* 29 */     this.lookupTable = paramECLookupTable;
/*    */   }
/*    */ 
/*    */   
/*    */   public ECPoint getOffset() {
/* 34 */     return this.offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setOffset(ECPoint paramECPoint) {
/* 39 */     this.offset = paramECPoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 44 */     return this.width;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setWidth(int paramInt) {
/* 49 */     this.width = paramInt;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/FixedPointPreCompInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */