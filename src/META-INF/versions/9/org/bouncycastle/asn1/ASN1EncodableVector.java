/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ 
/*     */ 
/*     */ public class ASN1EncodableVector
/*     */ {
/*   8 */   static final ASN1Encodable[] EMPTY_ELEMENTS = new ASN1Encodable[0];
/*     */   
/*     */   private static final int DEFAULT_CAPACITY = 10;
/*     */   
/*     */   private ASN1Encodable[] elements;
/*     */   
/*     */   private int elementCount;
/*     */   private boolean copyOnWrite;
/*     */   
/*     */   public ASN1EncodableVector() {
/*  18 */     this(10);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1EncodableVector(int paramInt) {
/*  23 */     if (paramInt < 0)
/*     */     {
/*  25 */       throw new IllegalArgumentException("'initialCapacity' must not be negative");
/*     */     }
/*     */     
/*  28 */     this.elements = (paramInt == 0) ? EMPTY_ELEMENTS : new ASN1Encodable[paramInt];
/*  29 */     this.elementCount = 0;
/*  30 */     this.copyOnWrite = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(ASN1Encodable paramASN1Encodable) {
/*  35 */     if (null == paramASN1Encodable)
/*     */     {
/*  37 */       throw new NullPointerException("'element' cannot be null");
/*     */     }
/*     */     
/*  40 */     int i = this.elements.length;
/*  41 */     int j = this.elementCount + 1;
/*  42 */     if ((((j > i) ? 1 : 0) | this.copyOnWrite) != 0)
/*     */     {
/*  44 */       reallocate(j);
/*     */     }
/*     */     
/*  47 */     this.elements[this.elementCount] = paramASN1Encodable;
/*  48 */     this.elementCount = j;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(org.bouncycastle.asn1.ASN1EncodableVector paramASN1EncodableVector) {
/*  53 */     if (null == paramASN1EncodableVector)
/*     */     {
/*  55 */       throw new NullPointerException("'other' cannot be null");
/*     */     }
/*     */     
/*  58 */     int i = paramASN1EncodableVector.size();
/*  59 */     if (i < 1) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  64 */     int j = this.elements.length;
/*  65 */     int k = this.elementCount + i;
/*  66 */     if ((((k > j) ? 1 : 0) | this.copyOnWrite) != 0)
/*     */     {
/*  68 */       reallocate(k);
/*     */     }
/*     */     
/*  71 */     byte b = 0;
/*     */     
/*     */     do {
/*  74 */       ASN1Encodable aSN1Encodable = paramASN1EncodableVector.get(b);
/*  75 */       if (null == aSN1Encodable)
/*     */       {
/*  77 */         throw new NullPointerException("'other' elements cannot be null");
/*     */       }
/*     */       
/*  80 */       this.elements[this.elementCount + b] = aSN1Encodable;
/*     */     }
/*  82 */     while (++b < i);
/*     */     
/*  84 */     this.elementCount = k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Encodable get(int paramInt) {
/*  95 */     if (paramInt >= this.elementCount)
/*     */     {
/*  97 */       throw new ArrayIndexOutOfBoundsException("" + paramInt + " >= " + paramInt);
/*     */     }
/*     */     
/* 100 */     return this.elements[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 110 */     return this.elementCount;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Encodable[] copyElements() {
/* 115 */     if (0 == this.elementCount)
/*     */     {
/* 117 */       return EMPTY_ELEMENTS;
/*     */     }
/*     */     
/* 120 */     ASN1Encodable[] arrayOfASN1Encodable = new ASN1Encodable[this.elementCount];
/* 121 */     System.arraycopy(this.elements, 0, arrayOfASN1Encodable, 0, this.elementCount);
/* 122 */     return arrayOfASN1Encodable;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Encodable[] takeElements() {
/* 127 */     if (0 == this.elementCount)
/*     */     {
/* 129 */       return EMPTY_ELEMENTS;
/*     */     }
/*     */     
/* 132 */     if (this.elements.length == this.elementCount) {
/*     */       
/* 134 */       this.copyOnWrite = true;
/* 135 */       return this.elements;
/*     */     } 
/*     */     
/* 138 */     ASN1Encodable[] arrayOfASN1Encodable = new ASN1Encodable[this.elementCount];
/* 139 */     System.arraycopy(this.elements, 0, arrayOfASN1Encodable, 0, this.elementCount);
/* 140 */     return arrayOfASN1Encodable;
/*     */   }
/*     */ 
/*     */   
/*     */   private void reallocate(int paramInt) {
/* 145 */     int i = this.elements.length;
/* 146 */     int j = Math.max(i, paramInt + (paramInt >> 1));
/*     */     
/* 148 */     ASN1Encodable[] arrayOfASN1Encodable = new ASN1Encodable[j];
/* 149 */     System.arraycopy(this.elements, 0, arrayOfASN1Encodable, 0, this.elementCount);
/*     */     
/* 151 */     this.elements = arrayOfASN1Encodable;
/* 152 */     this.copyOnWrite = false;
/*     */   }
/*     */ 
/*     */   
/*     */   static ASN1Encodable[] cloneElements(ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 157 */     return (paramArrayOfASN1Encodable.length < 1) ? EMPTY_ELEMENTS : (ASN1Encodable[])paramArrayOfASN1Encodable.clone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1EncodableVector.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */