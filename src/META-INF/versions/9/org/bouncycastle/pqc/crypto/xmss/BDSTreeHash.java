/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Stack;
/*     */ import org.bouncycastle.pqc.crypto.xmss.HashTreeAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.LTreeAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSNode;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSNodeUtil;
/*     */ 
/*     */ class BDSTreeHash
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private XMSSNode tailNode;
/*     */   private final int initialHeight;
/*     */   
/*     */   BDSTreeHash(int paramInt) {
/*  22 */     this.initialHeight = paramInt;
/*  23 */     this.initialized = false;
/*  24 */     this.finished = false;
/*     */   }
/*     */   private int height; private int nextIndex; private boolean initialized; private boolean finished;
/*     */   
/*     */   void initialize(int paramInt) {
/*  29 */     this.tailNode = null;
/*  30 */     this.height = this.initialHeight;
/*  31 */     this.nextIndex = paramInt;
/*  32 */     this.initialized = true;
/*  33 */     this.finished = false;
/*     */   }
/*     */ 
/*     */   
/*     */   void update(Stack<XMSSNode> paramStack, WOTSPlus paramWOTSPlus, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/*  38 */     if (paramOTSHashAddress == null)
/*     */     {
/*  40 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/*  42 */     if (this.finished || !this.initialized)
/*     */     {
/*  44 */       throw new IllegalStateException("finished or not initialized");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(this.nextIndex).withChainAddress(paramOTSHashAddress.getChainAddress()).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(paramOTSHashAddress.getKeyAndMask())).build();
/*     */ 
/*     */     
/*  54 */     LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withLTreeAddress(this.nextIndex).build();
/*     */ 
/*     */     
/*  57 */     HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withTreeIndex(this.nextIndex).build();
/*     */     
/*  59 */     paramWOTSPlus.importKeys(paramWOTSPlus.getWOTSPlusSecretKey(paramArrayOfbyte2, paramOTSHashAddress), paramArrayOfbyte1);
/*  60 */     WOTSPlusPublicKeyParameters wOTSPlusPublicKeyParameters = paramWOTSPlus.getPublicKey(paramOTSHashAddress);
/*  61 */     XMSSNode xMSSNode = XMSSNodeUtil.lTree(paramWOTSPlus, wOTSPlusPublicKeyParameters, lTreeAddress);
/*     */     
/*  63 */     while (!paramStack.isEmpty() && ((XMSSNode)paramStack.peek()).getHeight() == xMSSNode.getHeight() && ((XMSSNode)paramStack
/*  64 */       .peek()).getHeight() != this.initialHeight) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  71 */       hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex((hashTreeAddress.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/*  72 */       xMSSNode = XMSSNodeUtil.randomizeHash(paramWOTSPlus, paramStack.pop(), xMSSNode, (XMSSAddress)hashTreeAddress);
/*  73 */       xMSSNode = new XMSSNode(xMSSNode.getHeight() + 1, xMSSNode.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  79 */       hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight() + 1).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/*     */     } 
/*     */     
/*  82 */     if (this.tailNode == null) {
/*     */       
/*  84 */       this.tailNode = xMSSNode;
/*     */ 
/*     */     
/*     */     }
/*  88 */     else if (this.tailNode.getHeight() == xMSSNode.getHeight()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex((hashTreeAddress.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/*  96 */       xMSSNode = XMSSNodeUtil.randomizeHash(paramWOTSPlus, this.tailNode, xMSSNode, (XMSSAddress)hashTreeAddress);
/*  97 */       xMSSNode = new XMSSNode(this.tailNode.getHeight() + 1, xMSSNode.getValue());
/*  98 */       this.tailNode = xMSSNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight() + 1).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/*     */     }
/*     */     else {
/*     */       
/* 108 */       paramStack.push(xMSSNode);
/*     */     } 
/*     */ 
/*     */     
/* 112 */     if (this.tailNode.getHeight() == this.initialHeight) {
/*     */       
/* 114 */       this.finished = true;
/*     */     }
/*     */     else {
/*     */       
/* 118 */       this.height = xMSSNode.getHeight();
/* 119 */       this.nextIndex++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   int getHeight() {
/* 125 */     if (!this.initialized || this.finished)
/*     */     {
/* 127 */       return Integer.MAX_VALUE;
/*     */     }
/* 129 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   int getIndexLeaf() {
/* 134 */     return this.nextIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   void setNode(XMSSNode paramXMSSNode) {
/* 139 */     this.tailNode = paramXMSSNode;
/* 140 */     this.height = paramXMSSNode.getHeight();
/* 141 */     if (this.height == this.initialHeight)
/*     */     {
/* 143 */       this.finished = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isFinished() {
/* 149 */     return this.finished;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isInitialized() {
/* 154 */     return this.initialized;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSNode getTailNode() {
/* 159 */     return this.tailNode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected org.bouncycastle.pqc.crypto.xmss.BDSTreeHash clone() {
/* 164 */     org.bouncycastle.pqc.crypto.xmss.BDSTreeHash bDSTreeHash = new org.bouncycastle.pqc.crypto.xmss.BDSTreeHash(this.initialHeight);
/*     */     
/* 166 */     bDSTreeHash.tailNode = this.tailNode;
/* 167 */     bDSTreeHash.height = this.height;
/* 168 */     bDSTreeHash.nextIndex = this.nextIndex;
/* 169 */     bDSTreeHash.initialized = this.initialized;
/* 170 */     bDSTreeHash.finished = this.finished;
/*     */     
/* 172 */     return bDSTreeHash;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/BDSTreeHash.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */