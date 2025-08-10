/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSStoreableObjectInterface;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMSSPublicKeyParameters
/*     */   extends XMSSKeyParameters
/*     */   implements XMSSStoreableObjectInterface, Encodable
/*     */ {
/*     */   private final XMSSParameters params;
/*     */   private final int oid;
/*     */   private final byte[] root;
/*     */   private final byte[] publicSeed;
/*     */   
/*     */   private XMSSPublicKeyParameters(Builder paramBuilder) {
/*  26 */     super(false, Builder.access$000(paramBuilder).getTreeDigest());
/*  27 */     this.params = Builder.access$000(paramBuilder);
/*  28 */     if (this.params == null)
/*     */     {
/*  30 */       throw new NullPointerException("params == null");
/*     */     }
/*  32 */     int i = this.params.getTreeDigestSize();
/*  33 */     byte[] arrayOfByte = Builder.access$100(paramBuilder);
/*  34 */     if (arrayOfByte != null) {
/*     */ 
/*     */       
/*  37 */       byte b = 4;
/*  38 */       int j = i;
/*  39 */       int k = i;
/*     */       
/*  41 */       int m = 0;
/*     */       
/*  43 */       if (arrayOfByte.length == j + k)
/*     */       {
/*  45 */         this.oid = 0;
/*  46 */         this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, j);
/*  47 */         m += j;
/*  48 */         this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, k);
/*     */       }
/*  50 */       else if (arrayOfByte.length == b + j + k)
/*     */       {
/*  52 */         this.oid = Pack.bigEndianToInt(arrayOfByte, 0);
/*  53 */         m += b;
/*  54 */         this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, j);
/*  55 */         m += j;
/*  56 */         this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, m, k);
/*     */       }
/*     */       else
/*     */       {
/*  60 */         throw new IllegalArgumentException("public key has wrong size");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  66 */       if (this.params.getOid() != null) {
/*     */         
/*  68 */         this.oid = this.params.getOid().getOid();
/*     */       }
/*     */       else {
/*     */         
/*  72 */         this.oid = 0;
/*     */       } 
/*  74 */       byte[] arrayOfByte1 = Builder.access$200(paramBuilder);
/*  75 */       if (arrayOfByte1 != null) {
/*     */         
/*  77 */         if (arrayOfByte1.length != i)
/*     */         {
/*  79 */           throw new IllegalArgumentException("length of root must be equal to length of digest");
/*     */         }
/*  81 */         this.root = arrayOfByte1;
/*     */       }
/*     */       else {
/*     */         
/*  85 */         this.root = new byte[i];
/*     */       } 
/*  87 */       byte[] arrayOfByte2 = Builder.access$300(paramBuilder);
/*  88 */       if (arrayOfByte2 != null) {
/*     */         
/*  90 */         if (arrayOfByte2.length != i)
/*     */         {
/*  92 */           throw new IllegalArgumentException("length of publicSeed must be equal to length of digest");
/*     */         }
/*  94 */         this.publicSeed = arrayOfByte2;
/*     */       }
/*     */       else {
/*     */         
/*  98 */         this.publicSeed = new byte[i];
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() throws IOException {
/* 106 */     return toByteArray();
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
/* 155 */     int i = this.params.getTreeDigestSize();
/* 156 */     byte b = 4;
/* 157 */     int j = i;
/* 158 */     int k = i;
/*     */ 
/*     */     
/* 161 */     int m = 0;
/*     */     
/* 163 */     if (this.oid != 0) {
/*     */       
/* 165 */       arrayOfByte = new byte[b + j + k];
/* 166 */       Pack.intToBigEndian(this.oid, arrayOfByte, m);
/* 167 */       m += b;
/*     */     }
/*     */     else {
/*     */       
/* 171 */       arrayOfByte = new byte[j + k];
/*     */     } 
/*     */     
/* 174 */     XMSSUtil.copyBytesAtOffset(arrayOfByte, this.root, m);
/* 175 */     m += j;
/*     */     
/* 177 */     XMSSUtil.copyBytesAtOffset(arrayOfByte, this.publicSeed, m);
/* 178 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getRoot() {
/* 183 */     return XMSSUtil.cloneArray(this.root);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getPublicSeed() {
/* 188 */     return XMSSUtil.cloneArray(this.publicSeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSParameters getParameters() {
/* 193 */     return this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSPublicKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */