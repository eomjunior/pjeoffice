/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import java.util.TreeMap;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDSTreeHash;
/*     */ import org.bouncycastle.pqc.crypto.xmss.HashTreeAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.LTreeAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlus;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.WOTSPlusPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSNode;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSNodeUtil;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BDS
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private transient WOTSPlus wotsPlus;
/*     */   private final int treeHeight;
/*     */   private final List<BDSTreeHash> treeHashInstances;
/*     */   private int k;
/*     */   private XMSSNode root;
/*     */   private List<XMSSNode> authenticationPath;
/*     */   private Map<Integer, LinkedList<XMSSNode>> retain;
/*     */   private Stack<XMSSNode> stack;
/*     */   private Map<Integer, XMSSNode> keep;
/*     */   private int index;
/*     */   private boolean used;
/*     */   private transient int maxIndex;
/*     */   
/*     */   BDS(XMSSParameters paramXMSSParameters, int paramInt1, int paramInt2) {
/*  49 */     this(paramXMSSParameters.getWOTSPlus(), paramXMSSParameters.getHeight(), paramXMSSParameters.getK(), paramInt2);
/*  50 */     this.maxIndex = paramInt1;
/*  51 */     this.index = paramInt2;
/*  52 */     this.used = true;
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
/*     */   BDS(XMSSParameters paramXMSSParameters, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/*  65 */     this(paramXMSSParameters.getWOTSPlus(), paramXMSSParameters.getHeight(), paramXMSSParameters.getK(), (1 << paramXMSSParameters.getHeight()) - 1);
/*  66 */     initialize(paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress);
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
/*     */   BDS(XMSSParameters paramXMSSParameters, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress, int paramInt) {
/*  80 */     this(paramXMSSParameters.getWOTSPlus(), paramXMSSParameters.getHeight(), paramXMSSParameters.getK(), (1 << paramXMSSParameters.getHeight()) - 1);
/*     */     
/*  82 */     initialize(paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress);
/*     */     
/*  84 */     while (this.index < paramInt) {
/*     */       
/*  86 */       nextAuthenticationPath(paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress);
/*  87 */       this.used = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BDS(WOTSPlus paramWOTSPlus, int paramInt1, int paramInt2, int paramInt3) {
/*  93 */     this.wotsPlus = paramWOTSPlus;
/*  94 */     this.treeHeight = paramInt1;
/*  95 */     this.maxIndex = paramInt3;
/*  96 */     this.k = paramInt2;
/*  97 */     if (paramInt2 > paramInt1 || paramInt2 < 2 || (paramInt1 - paramInt2) % 2 != 0)
/*     */     {
/*  99 */       throw new IllegalArgumentException("illegal value for BDS parameter k");
/*     */     }
/* 101 */     this.authenticationPath = new ArrayList<>();
/* 102 */     this.retain = new TreeMap<>();
/* 103 */     this.stack = new Stack<>();
/*     */     
/* 105 */     this.treeHashInstances = new ArrayList<>();
/* 106 */     for (byte b = 0; b < paramInt1 - paramInt2; b++)
/*     */     {
/* 108 */       this.treeHashInstances.add(new BDSTreeHash(b));
/*     */     }
/*     */     
/* 111 */     this.keep = new TreeMap<>();
/* 112 */     this.index = 0;
/* 113 */     this.used = false;
/*     */   }
/*     */ 
/*     */   
/*     */   BDS(org.bouncycastle.pqc.crypto.xmss.BDS paramBDS) {
/* 118 */     this.wotsPlus = new WOTSPlus(paramBDS.wotsPlus.getParams());
/* 119 */     this.treeHeight = paramBDS.treeHeight;
/* 120 */     this.k = paramBDS.k;
/* 121 */     this.root = paramBDS.root;
/* 122 */     this.authenticationPath = new ArrayList<>();
/* 123 */     this.authenticationPath.addAll(paramBDS.authenticationPath);
/* 124 */     this.retain = new TreeMap<>();
/* 125 */     for (Integer integer : paramBDS.retain.keySet())
/*     */     {
/*     */       
/* 128 */       this.retain.put(integer, (LinkedList<XMSSNode>)((LinkedList)paramBDS.retain.get(integer)).clone());
/*     */     }
/* 130 */     this.stack = new Stack<>();
/* 131 */     this.stack.addAll(paramBDS.stack);
/* 132 */     this.treeHashInstances = new ArrayList<>();
/* 133 */     for (Iterator<BDSTreeHash> iterator = paramBDS.treeHashInstances.iterator(); iterator.hasNext();)
/*     */     {
/* 135 */       this.treeHashInstances.add(((BDSTreeHash)iterator.next()).clone());
/*     */     }
/* 137 */     this.keep = new TreeMap<>(paramBDS.keep);
/* 138 */     this.index = paramBDS.index;
/* 139 */     this.maxIndex = paramBDS.maxIndex;
/* 140 */     this.used = paramBDS.used;
/*     */   }
/*     */ 
/*     */   
/*     */   private BDS(org.bouncycastle.pqc.crypto.xmss.BDS paramBDS, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/* 145 */     this.wotsPlus = new WOTSPlus(paramBDS.wotsPlus.getParams());
/* 146 */     this.treeHeight = paramBDS.treeHeight;
/* 147 */     this.k = paramBDS.k;
/* 148 */     this.root = paramBDS.root;
/* 149 */     this.authenticationPath = new ArrayList<>();
/* 150 */     this.authenticationPath.addAll(paramBDS.authenticationPath);
/* 151 */     this.retain = new TreeMap<>();
/* 152 */     for (Integer integer : paramBDS.retain.keySet())
/*     */     {
/*     */       
/* 155 */       this.retain.put(integer, (LinkedList<XMSSNode>)((LinkedList)paramBDS.retain.get(integer)).clone());
/*     */     }
/* 157 */     this.stack = new Stack<>();
/* 158 */     this.stack.addAll(paramBDS.stack);
/* 159 */     this.treeHashInstances = new ArrayList<>();
/* 160 */     for (Iterator<BDSTreeHash> iterator = paramBDS.treeHashInstances.iterator(); iterator.hasNext();)
/*     */     {
/* 162 */       this.treeHashInstances.add(((BDSTreeHash)iterator.next()).clone());
/*     */     }
/* 164 */     this.keep = new TreeMap<>(paramBDS.keep);
/* 165 */     this.index = paramBDS.index;
/* 166 */     this.maxIndex = paramBDS.maxIndex;
/* 167 */     this.used = false;
/*     */     
/* 169 */     nextAuthenticationPath(paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   private BDS(org.bouncycastle.pqc.crypto.xmss.BDS paramBDS, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 174 */     this.wotsPlus = new WOTSPlus(new WOTSPlusParameters(paramASN1ObjectIdentifier));
/* 175 */     this.treeHeight = paramBDS.treeHeight;
/* 176 */     this.k = paramBDS.k;
/* 177 */     this.root = paramBDS.root;
/* 178 */     this.authenticationPath = new ArrayList<>();
/* 179 */     this.authenticationPath.addAll(paramBDS.authenticationPath);
/* 180 */     this.retain = new TreeMap<>();
/* 181 */     for (Integer integer : paramBDS.retain.keySet())
/*     */     {
/*     */       
/* 184 */       this.retain.put(integer, (LinkedList<XMSSNode>)((LinkedList)paramBDS.retain.get(integer)).clone());
/*     */     }
/* 186 */     this.stack = new Stack<>();
/* 187 */     this.stack.addAll(paramBDS.stack);
/* 188 */     this.treeHashInstances = new ArrayList<>();
/* 189 */     for (Iterator<BDSTreeHash> iterator = paramBDS.treeHashInstances.iterator(); iterator.hasNext();)
/*     */     {
/* 191 */       this.treeHashInstances.add(((BDSTreeHash)iterator.next()).clone());
/*     */     }
/* 193 */     this.keep = new TreeMap<>(paramBDS.keep);
/* 194 */     this.index = paramBDS.index;
/* 195 */     this.maxIndex = paramBDS.maxIndex;
/* 196 */     this.used = paramBDS.used;
/* 197 */     validate();
/*     */   }
/*     */ 
/*     */   
/*     */   private BDS(org.bouncycastle.pqc.crypto.xmss.BDS paramBDS, int paramInt, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 202 */     this.wotsPlus = new WOTSPlus(new WOTSPlusParameters(paramASN1ObjectIdentifier));
/* 203 */     this.treeHeight = paramBDS.treeHeight;
/* 204 */     this.k = paramBDS.k;
/* 205 */     this.root = paramBDS.root;
/* 206 */     this.authenticationPath = new ArrayList<>();
/* 207 */     this.authenticationPath.addAll(paramBDS.authenticationPath);
/* 208 */     this.retain = new TreeMap<>();
/* 209 */     for (Integer integer : paramBDS.retain.keySet())
/*     */     {
/*     */       
/* 212 */       this.retain.put(integer, (LinkedList<XMSSNode>)((LinkedList)paramBDS.retain.get(integer)).clone());
/*     */     }
/* 214 */     this.stack = new Stack<>();
/* 215 */     this.stack.addAll(paramBDS.stack);
/* 216 */     this.treeHashInstances = new ArrayList<>();
/* 217 */     for (Iterator<BDSTreeHash> iterator = paramBDS.treeHashInstances.iterator(); iterator.hasNext();)
/*     */     {
/* 219 */       this.treeHashInstances.add(((BDSTreeHash)iterator.next()).clone());
/*     */     }
/* 221 */     this.keep = new TreeMap<>(paramBDS.keep);
/* 222 */     this.index = paramBDS.index;
/* 223 */     this.maxIndex = paramInt;
/* 224 */     this.used = paramBDS.used;
/* 225 */     validate();
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.xmss.BDS getNextState(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/* 230 */     return new org.bouncycastle.pqc.crypto.xmss.BDS(this, paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initialize(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/* 235 */     if (paramOTSHashAddress == null)
/*     */     {
/* 237 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 242 */     LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).build();
/*     */ 
/*     */     
/* 245 */     HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).build();
/*     */ 
/*     */     
/* 248 */     for (byte b = 0; b < 1 << this.treeHeight; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(b).withChainAddress(paramOTSHashAddress.getChainAddress()).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(paramOTSHashAddress.getKeyAndMask())).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 260 */       this.wotsPlus.importKeys(this.wotsPlus.getWOTSPlusSecretKey(paramArrayOfbyte2, paramOTSHashAddress), paramArrayOfbyte1);
/* 261 */       WOTSPlusPublicKeyParameters wOTSPlusPublicKeyParameters = this.wotsPlus.getPublicKey(paramOTSHashAddress);
/*     */ 
/*     */ 
/*     */       
/* 265 */       lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(b).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
/* 266 */       XMSSNode xMSSNode = XMSSNodeUtil.lTree(this.wotsPlus, wOTSPlusPublicKeyParameters, lTreeAddress);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 271 */       hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeIndex(b).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/* 272 */       while (!this.stack.isEmpty() && ((XMSSNode)this.stack.peek()).getHeight() == xMSSNode.getHeight()) {
/*     */ 
/*     */         
/* 275 */         int i = b / (1 << xMSSNode.getHeight());
/* 276 */         if (i == 1)
/*     */         {
/* 278 */           this.authenticationPath.add(xMSSNode);
/*     */         }
/*     */         
/* 281 */         if (i == 3 && xMSSNode.getHeight() < this.treeHeight - this.k)
/*     */         {
/* 283 */           ((BDSTreeHash)this.treeHashInstances.get(xMSSNode.getHeight())).setNode(xMSSNode);
/*     */         }
/* 285 */         if (i >= 3 && (i & 0x1) == 1 && xMSSNode.getHeight() >= this.treeHeight - this.k && xMSSNode
/* 286 */           .getHeight() <= this.treeHeight - 2)
/*     */         {
/* 288 */           if (this.retain.get(Integer.valueOf(xMSSNode.getHeight())) == null) {
/*     */             
/* 290 */             LinkedList<XMSSNode> linkedList = new LinkedList();
/* 291 */             linkedList.add(xMSSNode);
/* 292 */             this.retain.put(Integer.valueOf(xMSSNode.getHeight()), linkedList);
/*     */           }
/*     */           else {
/*     */             
/* 296 */             ((LinkedList<XMSSNode>)this.retain.get(Integer.valueOf(xMSSNode.getHeight()))).add(xMSSNode);
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 304 */         hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex((hashTreeAddress.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/* 305 */         xMSSNode = XMSSNodeUtil.randomizeHash(this.wotsPlus, this.stack.pop(), xMSSNode, (XMSSAddress)hashTreeAddress);
/* 306 */         xMSSNode = new XMSSNode(xMSSNode.getHeight() + 1, xMSSNode.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 312 */         hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight() + 1).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/*     */       } 
/*     */       
/* 315 */       this.stack.push(xMSSNode);
/*     */     } 
/* 317 */     this.root = this.stack.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   private void nextAuthenticationPath(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/* 322 */     if (paramOTSHashAddress == null)
/*     */     {
/* 324 */       throw new NullPointerException("otsHashAddress == null");
/*     */     }
/* 326 */     if (this.used)
/*     */     {
/* 328 */       throw new IllegalStateException("index already used");
/*     */     }
/* 330 */     if (this.index > this.maxIndex - 1)
/*     */     {
/* 332 */       throw new IllegalStateException("index out of bounds");
/*     */     }
/*     */ 
/*     */     
/* 336 */     int i = XMSSUtil.calculateTau(this.index, this.treeHeight);
/*     */     
/* 338 */     if ((this.index >> i + 1 & 0x1) == 0 && i < this.treeHeight - 1)
/*     */     {
/* 340 */       this.keep.put(Integer.valueOf(i), this.authenticationPath.get(i));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).build();
/*     */ 
/*     */     
/* 349 */     HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).build();
/*     */ 
/*     */     
/* 352 */     if (i == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 358 */       paramOTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(paramOTSHashAddress.getLayerAddress())).withTreeAddress(paramOTSHashAddress.getTreeAddress())).withOTSAddress(this.index).withChainAddress(paramOTSHashAddress.getChainAddress()).withHashAddress(paramOTSHashAddress.getHashAddress()).withKeyAndMask(paramOTSHashAddress.getKeyAndMask())).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 363 */       this.wotsPlus.importKeys(this.wotsPlus.getWOTSPlusSecretKey(paramArrayOfbyte2, paramOTSHashAddress), paramArrayOfbyte1);
/* 364 */       WOTSPlusPublicKeyParameters wOTSPlusPublicKeyParameters = this.wotsPlus.getPublicKey(paramOTSHashAddress);
/*     */ 
/*     */ 
/*     */       
/* 368 */       lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)(new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(this.index).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex()).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
/* 369 */       XMSSNode xMSSNode = XMSSNodeUtil.lTree(this.wotsPlus, wOTSPlusPublicKeyParameters, lTreeAddress);
/* 370 */       this.authenticationPath.set(0, xMSSNode);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 378 */       hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)(new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(i - 1).withTreeIndex(this.index >> i).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 383 */       this.wotsPlus.importKeys(this.wotsPlus.getWOTSPlusSecretKey(paramArrayOfbyte2, paramOTSHashAddress), paramArrayOfbyte1);
/* 384 */       XMSSNode xMSSNode = XMSSNodeUtil.randomizeHash(this.wotsPlus, this.authenticationPath.get(i - 1), this.keep.get(Integer.valueOf(i - 1)), (XMSSAddress)hashTreeAddress);
/* 385 */       xMSSNode = new XMSSNode(xMSSNode.getHeight() + 1, xMSSNode.getValue());
/* 386 */       this.authenticationPath.set(i, xMSSNode);
/* 387 */       this.keep.remove(Integer.valueOf(i - 1));
/*     */       
/*     */       int j;
/* 390 */       for (j = 0; j < i; j++) {
/*     */         
/* 392 */         if (j < this.treeHeight - this.k) {
/*     */           
/* 394 */           this.authenticationPath.set(j, ((BDSTreeHash)this.treeHashInstances.get(j)).getTailNode());
/*     */         }
/*     */         else {
/*     */           
/* 398 */           this.authenticationPath.set(j, ((LinkedList<XMSSNode>)this.retain.get(Integer.valueOf(j))).removeFirst());
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 403 */       j = Math.min(i, this.treeHeight - this.k);
/* 404 */       for (byte b1 = 0; b1 < j; b1++) {
/*     */         
/* 406 */         int k = this.index + 1 + 3 * (1 << b1);
/* 407 */         if (k < 1 << this.treeHeight)
/*     */         {
/* 409 */           ((BDSTreeHash)this.treeHashInstances.get(b1)).initialize(k);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 415 */     for (byte b = 0; b < this.treeHeight - this.k >> 1; b++) {
/*     */       
/* 417 */       BDSTreeHash bDSTreeHash = getBDSTreeHashInstanceForUpdate();
/* 418 */       if (bDSTreeHash != null)
/*     */       {
/* 420 */         bDSTreeHash.update(this.stack, this.wotsPlus, paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress);
/*     */       }
/*     */     } 
/*     */     
/* 424 */     this.index++;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isUsed() {
/* 429 */     return this.used;
/*     */   }
/*     */ 
/*     */   
/*     */   void markUsed() {
/* 434 */     this.used = true;
/*     */   }
/*     */ 
/*     */   
/*     */   private BDSTreeHash getBDSTreeHashInstanceForUpdate() {
/* 439 */     BDSTreeHash bDSTreeHash = null;
/* 440 */     for (BDSTreeHash bDSTreeHash1 : this.treeHashInstances) {
/*     */       
/* 442 */       if (bDSTreeHash1.isFinished() || !bDSTreeHash1.isInitialized()) {
/*     */         continue;
/*     */       }
/*     */       
/* 446 */       if (bDSTreeHash == null) {
/*     */         
/* 448 */         bDSTreeHash = bDSTreeHash1;
/*     */         continue;
/*     */       } 
/* 451 */       if (bDSTreeHash1.getHeight() < bDSTreeHash.getHeight()) {
/*     */         
/* 453 */         bDSTreeHash = bDSTreeHash1;
/*     */         continue;
/*     */       } 
/* 456 */       if (bDSTreeHash1.getHeight() == bDSTreeHash.getHeight())
/*     */       {
/* 458 */         if (bDSTreeHash1.getIndexLeaf() < bDSTreeHash.getIndexLeaf())
/*     */         {
/* 460 */           bDSTreeHash = bDSTreeHash1;
/*     */         }
/*     */       }
/*     */     } 
/* 464 */     return bDSTreeHash;
/*     */   }
/*     */ 
/*     */   
/*     */   private void validate() {
/* 469 */     if (this.authenticationPath == null)
/*     */     {
/* 471 */       throw new IllegalStateException("authenticationPath == null");
/*     */     }
/* 473 */     if (this.retain == null)
/*     */     {
/* 475 */       throw new IllegalStateException("retain == null");
/*     */     }
/* 477 */     if (this.stack == null)
/*     */     {
/* 479 */       throw new IllegalStateException("stack == null");
/*     */     }
/* 481 */     if (this.treeHashInstances == null)
/*     */     {
/* 483 */       throw new IllegalStateException("treeHashInstances == null");
/*     */     }
/* 485 */     if (this.keep == null)
/*     */     {
/* 487 */       throw new IllegalStateException("keep == null");
/*     */     }
/* 489 */     if (!XMSSUtil.isIndexValid(this.treeHeight, this.index))
/*     */     {
/* 491 */       throw new IllegalStateException("index in BDS state out of bounds");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getTreeHeight() {
/* 497 */     return this.treeHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   protected XMSSNode getRoot() {
/* 502 */     return this.root;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<XMSSNode> getAuthenticationPath() {
/* 507 */     ArrayList<XMSSNode> arrayList = new ArrayList();
/*     */     
/* 509 */     for (XMSSNode xMSSNode : this.authenticationPath)
/*     */     {
/* 511 */       arrayList.add(xMSSNode);
/*     */     }
/* 513 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getIndex() {
/* 518 */     return this.index;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxIndex() {
/* 523 */     return this.maxIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.xmss.BDS withWOTSDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 528 */     return new org.bouncycastle.pqc.crypto.xmss.BDS(this, paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.xmss.BDS withMaxIndex(int paramInt, ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 533 */     return new org.bouncycastle.pqc.crypto.xmss.BDS(this, paramInt, paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 540 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 542 */     if (paramObjectInputStream.available() != 0) {
/*     */       
/* 544 */       this.maxIndex = paramObjectInputStream.readInt();
/*     */     }
/*     */     else {
/*     */       
/* 548 */       this.maxIndex = (1 << this.treeHeight) - 1;
/*     */     } 
/* 550 */     if (this.maxIndex > (1 << this.treeHeight) - 1 || this.index > this.maxIndex + 1 || paramObjectInputStream.available() != 0)
/*     */     {
/* 552 */       throw new IOException("inconsistent BDS data detected");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 560 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 562 */     paramObjectOutputStream.writeInt(this.maxIndex);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/BDS.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */