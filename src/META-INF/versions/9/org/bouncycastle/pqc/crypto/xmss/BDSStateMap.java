/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.xmss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDS;
/*     */ import org.bouncycastle.pqc.crypto.xmss.OTSHashAddress;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Integers;
/*     */ 
/*     */ public class BDSStateMap implements Serializable {
/*     */   private static final long serialVersionUID = -3464451825208522308L;
/*  19 */   private final Map<Integer, BDS> bdsState = new TreeMap<>();
/*     */   
/*     */   private transient long maxIndex;
/*     */ 
/*     */   
/*     */   BDSStateMap(long paramLong) {
/*  25 */     this.maxIndex = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   BDSStateMap(org.bouncycastle.pqc.crypto.xmss.BDSStateMap paramBDSStateMap, long paramLong) {
/*  30 */     for (Integer integer : paramBDSStateMap.bdsState.keySet())
/*     */     {
/*     */ 
/*     */       
/*  34 */       this.bdsState.put(integer, new BDS(paramBDSStateMap.bdsState.get(integer)));
/*     */     }
/*  36 */     this.maxIndex = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   BDSStateMap(XMSSMTParameters paramXMSSMTParameters, long paramLong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  41 */     this.maxIndex = (1L << paramXMSSMTParameters.getHeight()) - 1L; long l;
/*  42 */     for (l = 0L; l < paramLong; l++)
/*     */     {
/*  44 */       updateState(paramXMSSMTParameters, l, paramArrayOfbyte1, paramArrayOfbyte2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMaxIndex() {
/*  50 */     return this.maxIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateState(XMSSMTParameters paramXMSSMTParameters, long paramLong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  55 */     XMSSParameters xMSSParameters = paramXMSSMTParameters.getXMSSParameters();
/*  56 */     int i = xMSSParameters.getHeight();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     long l = XMSSUtil.getTreeIndex(paramLong, i);
/*  62 */     int j = XMSSUtil.getLeafIndex(paramLong, i);
/*     */ 
/*     */     
/*  65 */     OTSHashAddress oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withTreeAddress(l)).withOTSAddress(j).build();
/*     */ 
/*     */     
/*  68 */     if (j < (1 << i) - 1) {
/*     */       
/*  70 */       if (get(0) == null || j == 0)
/*     */       {
/*  72 */         put(0, new BDS(xMSSParameters, paramArrayOfbyte1, paramArrayOfbyte2, oTSHashAddress));
/*     */       }
/*     */       
/*  75 */       update(0, paramArrayOfbyte1, paramArrayOfbyte2, oTSHashAddress);
/*     */     } 
/*     */ 
/*     */     
/*  79 */     for (byte b = 1; b < paramXMSSMTParameters.getLayers(); b++) {
/*     */ 
/*     */       
/*  82 */       j = XMSSUtil.getLeafIndex(l, i);
/*  83 */       l = XMSSUtil.getTreeIndex(l, i);
/*     */ 
/*     */       
/*  86 */       oTSHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)(new OTSHashAddress.Builder()).withLayerAddress(b)).withTreeAddress(l)).withOTSAddress(j).build();
/*     */ 
/*     */       
/*  89 */       if (this.bdsState.get(Integer.valueOf(b)) == null || XMSSUtil.isNewBDSInitNeeded(paramLong, i, b))
/*     */       {
/*  91 */         this.bdsState.put(Integer.valueOf(b), new BDS(xMSSParameters, paramArrayOfbyte1, paramArrayOfbyte2, oTSHashAddress));
/*     */       }
/*     */       
/*  94 */       if (j < (1 << i) - 1 && 
/*  95 */         XMSSUtil.isNewAuthenticationPathNeeded(paramLong, i, b))
/*     */       {
/*  97 */         update(b, paramArrayOfbyte1, paramArrayOfbyte2, oTSHashAddress);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 104 */     return this.bdsState.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   BDS get(int paramInt) {
/* 109 */     return this.bdsState.get(Integers.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   
/*     */   BDS update(int paramInt, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, OTSHashAddress paramOTSHashAddress) {
/* 114 */     return this.bdsState.put(Integers.valueOf(paramInt), ((BDS)this.bdsState.get(Integers.valueOf(paramInt))).getNextState(paramArrayOfbyte1, paramArrayOfbyte2, paramOTSHashAddress));
/*     */   }
/*     */ 
/*     */   
/*     */   void put(int paramInt, BDS paramBDS) {
/* 119 */     this.bdsState.put(Integers.valueOf(paramInt), paramBDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public org.bouncycastle.pqc.crypto.xmss.BDSStateMap withWOTSDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 124 */     org.bouncycastle.pqc.crypto.xmss.BDSStateMap bDSStateMap = new org.bouncycastle.pqc.crypto.xmss.BDSStateMap(this.maxIndex);
/*     */     
/* 126 */     for (Integer integer : this.bdsState.keySet())
/*     */     {
/*     */ 
/*     */       
/* 130 */       bDSStateMap.bdsState.put(integer, ((BDS)this.bdsState.get(integer)).withWOTSDigest(paramASN1ObjectIdentifier));
/*     */     }
/*     */     
/* 133 */     return bDSStateMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
/* 140 */     paramObjectInputStream.defaultReadObject();
/*     */     
/* 142 */     if (paramObjectInputStream.available() != 0) {
/*     */       
/* 144 */       this.maxIndex = paramObjectInputStream.readLong();
/*     */     }
/*     */     else {
/*     */       
/* 148 */       this.maxIndex = 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 156 */     paramObjectOutputStream.defaultWriteObject();
/*     */     
/* 158 */     paramObjectOutputStream.writeLong(this.maxIndex);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/xmss/BDSStateMap.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */