/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
/*    */ import org.bouncycastle.pqc.crypto.lms.LMSigParameters;
/*    */ 
/*    */ public class LMSParameters {
/*    */   private final LMSigParameters lmSigParam;
/*    */   
/*    */   public LMSParameters(LMSigParameters paramLMSigParameters, LMOtsParameters paramLMOtsParameters) {
/* 10 */     this.lmSigParam = paramLMSigParameters;
/* 11 */     this.lmOTSParam = paramLMOtsParameters;
/*    */   }
/*    */   private final LMOtsParameters lmOTSParam;
/*    */   
/*    */   public LMSigParameters getLMSigParam() {
/* 16 */     return this.lmSigParam;
/*    */   }
/*    */ 
/*    */   
/*    */   public LMOtsParameters getLMOTSParam() {
/* 21 */     return this.lmOTSParam;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */