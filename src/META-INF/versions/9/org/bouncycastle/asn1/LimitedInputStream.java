/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import org.bouncycastle.asn1.IndefiniteLengthInputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class LimitedInputStream
/*    */   extends InputStream
/*    */ {
/*    */   protected final InputStream _in;
/*    */   private int _limit;
/*    */   
/*    */   LimitedInputStream(InputStream paramInputStream, int paramInt) {
/* 18 */     this._in = paramInputStream;
/* 19 */     this._limit = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   int getLimit() {
/* 24 */     return this._limit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setParentEofDetect(boolean paramBoolean) {
/* 29 */     if (this._in instanceof IndefiniteLengthInputStream)
/*    */     {
/* 31 */       ((IndefiniteLengthInputStream)this._in).setEofOn00(paramBoolean);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/LimitedInputStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */