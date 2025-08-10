/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*     */ 
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McElieceCCA2Parameters
/*     */   extends McElieceParameters
/*     */ {
/*     */   private final String digest;
/*     */   
/*     */   public McElieceCCA2Parameters() {
/*  13 */     this(11, 50, "SHA-256");
/*     */   }
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(String paramString) {
/*  18 */     this(11, 50, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(int paramInt) {
/*  29 */     this(paramInt, "SHA-256");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(int paramInt, String paramString) {
/*  41 */     super(paramInt);
/*  42 */     this.digest = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(int paramInt1, int paramInt2) {
/*  55 */     this(paramInt1, paramInt2, "SHA-256");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(int paramInt1, int paramInt2, String paramString) {
/*  68 */     super(paramInt1, paramInt2);
/*  69 */     this.digest = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(int paramInt1, int paramInt2, int paramInt3) {
/*  84 */     this(paramInt1, paramInt2, paramInt3, "SHA-256");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceCCA2Parameters(int paramInt1, int paramInt2, int paramInt3, String paramString) {
/* 100 */     super(paramInt1, paramInt2, paramInt3);
/* 101 */     this.digest = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDigest() {
/* 111 */     return this.digest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2Parameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */