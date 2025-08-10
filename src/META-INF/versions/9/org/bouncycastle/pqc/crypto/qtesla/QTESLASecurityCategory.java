/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.qtesla;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QTESLASecurityCategory
/*    */ {
/*    */   public static final int PROVABLY_SECURE_I = 5;
/*    */   public static final int PROVABLY_SECURE_III = 6;
/*    */   
/*    */   static void validate(int paramInt) {
/* 17 */     switch (paramInt) {
/*    */       case 5:
/*    */       case 6:
/*    */         return;
/*    */     } 
/*    */     
/* 23 */     throw new IllegalArgumentException("unknown security category: " + paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static int getPrivateSize(int paramInt) {
/* 29 */     switch (paramInt) {
/*    */       
/*    */       case 5:
/* 32 */         return 5224;
/*    */       case 6:
/* 34 */         return 12392;
/*    */     } 
/*    */     
/* 37 */     throw new IllegalArgumentException("unknown security category: " + paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static int getPublicSize(int paramInt) {
/* 43 */     switch (paramInt) {
/*    */       
/*    */       case 5:
/* 46 */         return 14880;
/*    */       case 6:
/* 48 */         return 38432;
/*    */     } 
/*    */     
/* 51 */     throw new IllegalArgumentException("unknown security category: " + paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static int getSignatureSize(int paramInt) {
/* 57 */     switch (paramInt) {
/*    */ 
/*    */       
/*    */       case 5:
/* 61 */         return 2592;
/*    */       case 6:
/* 63 */         return 5664;
/*    */     } 
/* 65 */     throw new IllegalArgumentException("unknown security category: " + paramInt);
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
/*    */   public static String getName(int paramInt) {
/* 77 */     switch (paramInt) {
/*    */       
/*    */       case 5:
/* 80 */         return "qTESLA-p-I";
/*    */       case 6:
/* 82 */         return "qTESLA-p-III";
/*    */     } 
/* 84 */     throw new IllegalArgumentException("unknown security category: " + paramInt);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/qtesla/QTESLASecurityCategory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */