/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import org.bouncycastle.pqc.crypto.xmss.HashTreeAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.LTreeAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSNode;
/*     */ 
/*     */ 
/*     */ class XMSSNodeUtil
/*     */ {
/*     */   static XMSSNode lTree(WOTSPlus paramWOTSPlus, WOTSPlusPublicKeyParameters paramWOTSPlusPublicKeyParameters, LTreeAddress paramLTreeAddress) {
/*  14 */     if (paramWOTSPlusPublicKeyParameters == null)
/*     */     {
/*  16 */       throw new NullPointerException("publicKey == null");
/*     */     }
/*  18 */     if (paramLTreeAddress == null)
/*     */     {
/*  20 */       throw new NullPointerException("address == null");
/*     */     }
/*  22 */     int i = paramWOTSPlus.getParams().getLen();
/*     */     
/*  24 */     byte[][] arrayOfByte = paramWOTSPlusPublicKeyParameters.toByteArray();
/*  25 */     XMSSNode[] arrayOfXMSSNode = new XMSSNode[arrayOfByte.length]; byte b;
/*  26 */     for (b = 0; b < arrayOfByte.length; b++)
/*     */     {
/*  28 */       arrayOfXMSSNode[b] = new XMSSNode(0, arrayOfByte[b]);
/*     */     }
/*     */ 
/*     */     
/*  32 */     paramLTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(paramLTreeAddress.getLayerAddress())).withTreeAddress(paramLTreeAddress.getTreeAddress())).withLTreeAddress(paramLTreeAddress.getLTreeAddress()).withTreeHeight(0).withTreeIndex(paramLTreeAddress.getTreeIndex()).withKeyAndMask(paramLTreeAddress.getKeyAndMask())).build();
/*  33 */     while (i > 1) {
/*     */       
/*  35 */       for (b = 0; b < (int)Math.floor((i / 2)); b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  40 */         paramLTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(paramLTreeAddress.getLayerAddress())).withTreeAddress(paramLTreeAddress.getTreeAddress())).withLTreeAddress(paramLTreeAddress.getLTreeAddress()).withTreeHeight(paramLTreeAddress.getTreeHeight()).withTreeIndex(b).withKeyAndMask(paramLTreeAddress.getKeyAndMask())).build();
/*  41 */         arrayOfXMSSNode[b] = randomizeHash(paramWOTSPlus, arrayOfXMSSNode[2 * b], arrayOfXMSSNode[2 * b + 1], (XMSSAddress)paramLTreeAddress);
/*     */       } 
/*  43 */       if (i % 2 == 1)
/*     */       {
/*  45 */         arrayOfXMSSNode[(int)Math.floor((i / 2))] = arrayOfXMSSNode[i - 1];
/*     */       }
/*  47 */       i = (int)Math.ceil(i / 2.0D);
/*     */ 
/*     */ 
/*     */       
/*  51 */       paramLTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(paramLTreeAddress.getLayerAddress())).withTreeAddress(paramLTreeAddress.getTreeAddress())).withLTreeAddress(paramLTreeAddress.getLTreeAddress()).withTreeHeight(paramLTreeAddress.getTreeHeight() + 1).withTreeIndex(paramLTreeAddress.getTreeIndex()).withKeyAndMask(paramLTreeAddress.getKeyAndMask())).build();
/*     */     } 
/*  53 */     return arrayOfXMSSNode[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static XMSSNode randomizeHash(WOTSPlus paramWOTSPlus, XMSSNode paramXMSSNode1, XMSSNode paramXMSSNode2, XMSSAddress paramXMSSAddress) {
/*     */     LTreeAddress lTreeAddress3;
/*     */     HashTreeAddress hashTreeAddress3;
/*     */     LTreeAddress lTreeAddress2;
/*     */     HashTreeAddress hashTreeAddress2;
/*     */     LTreeAddress lTreeAddress1;
/*     */     HashTreeAddress hashTreeAddress1;
/*  66 */     if (paramXMSSNode1 == null)
/*     */     {
/*  68 */       throw new NullPointerException("left == null");
/*     */     }
/*  70 */     if (paramXMSSNode2 == null)
/*     */     {
/*  72 */       throw new NullPointerException("right == null");
/*     */     }
/*  74 */     if (paramXMSSNode1.getHeight() != paramXMSSNode2.getHeight())
/*     */     {
/*  76 */       throw new IllegalStateException("height of both nodes must be equal");
/*     */     }
/*  78 */     if (paramXMSSAddress == null)
/*     */     {
/*  80 */       throw new NullPointerException("address == null");
/*     */     }
/*  82 */     byte[] arrayOfByte1 = paramWOTSPlus.getPublicSeed();
/*     */     
/*  84 */     if (paramXMSSAddress instanceof LTreeAddress) {
/*     */       
/*  86 */       LTreeAddress lTreeAddress = (LTreeAddress)paramXMSSAddress;
/*     */ 
/*     */ 
/*     */       
/*  90 */       lTreeAddress3 = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(0)).build();
/*     */     }
/*  92 */     else if (lTreeAddress3 instanceof HashTreeAddress) {
/*     */       
/*  94 */       HashTreeAddress hashTreeAddress = (HashTreeAddress)lTreeAddress3;
/*     */ 
/*     */       
/*  97 */       hashTreeAddress3 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(0)).build();
/*     */     } 
/*     */     
/* 100 */     byte[] arrayOfByte2 = paramWOTSPlus.getKhf().PRF(arrayOfByte1, hashTreeAddress3.toByteArray());
/*     */     
/* 102 */     if (hashTreeAddress3 instanceof LTreeAddress) {
/*     */       
/* 104 */       LTreeAddress lTreeAddress = (LTreeAddress)hashTreeAddress3;
/*     */ 
/*     */ 
/*     */       
/* 108 */       lTreeAddress2 = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(1)).build();
/*     */     }
/* 110 */     else if (lTreeAddress2 instanceof HashTreeAddress) {
/*     */       
/* 112 */       HashTreeAddress hashTreeAddress = (HashTreeAddress)lTreeAddress2;
/*     */ 
/*     */       
/* 115 */       hashTreeAddress2 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(1)).build();
/*     */     } 
/*     */     
/* 118 */     byte[] arrayOfByte3 = paramWOTSPlus.getKhf().PRF(arrayOfByte1, hashTreeAddress2.toByteArray());
/*     */     
/* 120 */     if (hashTreeAddress2 instanceof LTreeAddress) {
/*     */       
/* 122 */       LTreeAddress lTreeAddress = (LTreeAddress)hashTreeAddress2;
/*     */ 
/*     */ 
/*     */       
/* 126 */       lTreeAddress1 = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(2)).build();
/*     */     }
/* 128 */     else if (lTreeAddress1 instanceof HashTreeAddress) {
/*     */       
/* 130 */       HashTreeAddress hashTreeAddress = (HashTreeAddress)lTreeAddress1;
/*     */ 
/*     */       
/* 133 */       hashTreeAddress1 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(2)).build();
/*     */     } 
/*     */     
/* 136 */     byte[] arrayOfByte4 = paramWOTSPlus.getKhf().PRF(arrayOfByte1, hashTreeAddress1.toByteArray());
/* 137 */     int i = paramWOTSPlus.getParams().getTreeDigestSize();
/* 138 */     byte[] arrayOfByte5 = new byte[2 * i]; byte b;
/* 139 */     for (b = 0; b < i; b++)
/*     */     {
/* 141 */       arrayOfByte5[b] = (byte)(paramXMSSNode1.getValue()[b] ^ arrayOfByte3[b]);
/*     */     }
/* 143 */     for (b = 0; b < i; b++)
/*     */     {
/* 145 */       arrayOfByte5[b + i] = (byte)(paramXMSSNode2.getValue()[b] ^ arrayOfByte4[b]);
/*     */     }
/* 147 */     byte[] arrayOfByte6 = paramWOTSPlus.getKhf().H(arrayOfByte2, arrayOfByte5);
/* 148 */     return new XMSSNode(paramXMSSNode1.getHeight(), arrayOfByte6);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/XMSSNodeUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */