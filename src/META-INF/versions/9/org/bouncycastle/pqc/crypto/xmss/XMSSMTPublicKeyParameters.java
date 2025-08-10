/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ public final class XMSSMTPublicKeyParameters
/*     */   extends XMSSMTKeyParameters
/*     */   implements XMSSStoreableObjectInterface, Encodable
/*     */ {
/*     */   private final XMSSMTParameters params;
/*     */   private final int oid;
/*     */   private final byte[] root;
/*     */   private final byte[] publicSeed;
/*     */   
/*     */   private XMSSMTPublicKeyParameters(Builder paramBuilder) {
/*  22 */     super(false, Builder.access$000(paramBuilder).getTreeDigest());
/*  23 */     this.params = Builder.access$000(paramBuilder);
/*  24 */     if (this.params == null)
/*     */     {
/*  26 */       throw new NullPointerException("params == null");
/*     */     }
/*  28 */     int i = this.params.getTreeDigestSize();
/*  29 */     byte[] arrayOfByte = Builder.access$100(paramBuilder);
/*  30 */     if (arrayOfByte != null) {
/*     */ 
/*     */       
/*  33 */       byte b = 4;
/*  34 */       int j = i;
/*  35 */       int k = i;
/*  36 */       int m = 0;
/*     */       
/*  38 */       if (arrayOfByte.length == j + k)
/*     */       {
/*  40 */         this.oid = 0;
/*  41 */         this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, j);
/*  42 */         m += j;
/*  43 */         this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, k);
/*     */       }
/*  45 */       else if (arrayOfByte.length == b + j + k)
/*     */       {
/*  47 */         this.oid = Pack.bigEndianToInt(arrayOfByte, 0);
/*  48 */         m += b;
/*  49 */         this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, j);
/*  50 */         m += j;
/*  51 */         this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, k);
/*     */       }
/*     */       else
/*     */       {
/*  55 */         throw new IllegalArgumentException("public key has wrong size");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  61 */       if (this.params.getOid() != null) {
/*     */         
/*  63 */         this.oid = this.params.getOid().getOid();
/*     */       }
/*     */       else {
/*     */         
/*  67 */         this.oid = 0;
/*     */       } 
/*  69 */       byte[] arrayOfByte1 = Builder.access$200(paramBuilder);
/*  70 */       if (arrayOfByte1 != null) {
/*     */         
/*  72 */         if (arrayOfByte1.length != i)
/*     */         {
/*  74 */           throw new IllegalArgumentException("length of root must be equal to length of digest");
/*     */         }
/*  76 */         this.root = arrayOfByte1;
/*     */       }
/*     */       else {
/*     */         
/*  80 */         this.root = new byte[i];
/*     */       } 
/*  82 */       byte[] arrayOfByte2 = Builder.access$300(paramBuilder);
/*  83 */       if (arrayOfByte2 != null) {
/*     */         
/*  85 */         if (arrayOfByte2.length != i)
/*     */         {
/*  87 */           throw new IllegalArgumentException("length of publicSeed must be equal to length of digest");
/*     */         }
/*  89 */         this.publicSeed = arrayOfByte2;
/*     */       }
/*     */       else {
/*     */         
/*  93 */         this.publicSeed = new byte[i];
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 101 */     return toByteArray();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/*     */     byte[] arrayOfByte;
/* 150 */     int i = this.params.getTreeDigestSize();
/* 151 */     byte b = 4;
/* 152 */     int j = i;
/* 153 */     int k = i;
/*     */     
/* 155 */     int m = 0;
/*     */     
/* 157 */     if (this.oid != 0) {
/*     */       
/* 159 */       arrayOfByte = new byte[b + j + k];
/* 160 */       Pack.intToBigEndian(this.oid, arrayOfByte, m);
/* 161 */       m += b;
/*     */     }
/*     */     else {
/*     */       
/* 165 */       arrayOfByte = new byte[j + k];
/*     */     } 
/*     */     
/* 168 */     XMSSUtil.copyBytesAtOffset(arrayOfByte, this.root, m);
/* 169 */     m += j;
/*     */     
/* 171 */     XMSSUtil.copyBytesAtOffset(arrayOfByte, this.publicSeed, m);
/* 172 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getRoot() {
/* 177 */     return XMSSUtil.cloneArray(this.root);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getPublicSeed() {
/* 182 */     return XMSSUtil.cloneArray(this.publicSeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSMTParameters getParameters() {
/* 187 */     return this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSMTPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */