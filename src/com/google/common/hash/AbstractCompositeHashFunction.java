/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
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
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class AbstractCompositeHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   final HashFunction[] functions;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractCompositeHashFunction(HashFunction... functions) {
/*  40 */     for (HashFunction function : functions) {
/*  41 */       Preconditions.checkNotNull(function);
/*     */     }
/*  43 */     this.functions = functions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract HashCode makeHash(Hasher[] paramArrayOfHasher);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  56 */     Hasher[] hashers = new Hasher[this.functions.length];
/*  57 */     for (int i = 0; i < hashers.length; i++) {
/*  58 */       hashers[i] = this.functions[i].newHasher();
/*     */     }
/*  60 */     return fromHashers(hashers);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  65 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  66 */     Hasher[] hashers = new Hasher[this.functions.length];
/*  67 */     for (int i = 0; i < hashers.length; i++) {
/*  68 */       hashers[i] = this.functions[i].newHasher(expectedInputSize);
/*     */     }
/*  70 */     return fromHashers(hashers);
/*     */   }
/*     */   
/*     */   private Hasher fromHashers(final Hasher[] hashers) {
/*  74 */     return new Hasher()
/*     */       {
/*     */         public Hasher putByte(byte b) {
/*  77 */           for (Hasher hasher : hashers) {
/*  78 */             hasher.putByte(b);
/*     */           }
/*  80 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBytes(byte[] bytes) {
/*  85 */           for (Hasher hasher : hashers) {
/*  86 */             hasher.putBytes(bytes);
/*     */           }
/*  88 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBytes(byte[] bytes, int off, int len) {
/*  93 */           for (Hasher hasher : hashers) {
/*  94 */             hasher.putBytes(bytes, off, len);
/*     */           }
/*  96 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBytes(ByteBuffer bytes) {
/* 101 */           int pos = bytes.position();
/* 102 */           for (Hasher hasher : hashers) {
/* 103 */             Java8Compatibility.position(bytes, pos);
/* 104 */             hasher.putBytes(bytes);
/*     */           } 
/* 106 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putShort(short s) {
/* 111 */           for (Hasher hasher : hashers) {
/* 112 */             hasher.putShort(s);
/*     */           }
/* 114 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putInt(int i) {
/* 119 */           for (Hasher hasher : hashers) {
/* 120 */             hasher.putInt(i);
/*     */           }
/* 122 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putLong(long l) {
/* 127 */           for (Hasher hasher : hashers) {
/* 128 */             hasher.putLong(l);
/*     */           }
/* 130 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putFloat(float f) {
/* 135 */           for (Hasher hasher : hashers) {
/* 136 */             hasher.putFloat(f);
/*     */           }
/* 138 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putDouble(double d) {
/* 143 */           for (Hasher hasher : hashers) {
/* 144 */             hasher.putDouble(d);
/*     */           }
/* 146 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBoolean(boolean b) {
/* 151 */           for (Hasher hasher : hashers) {
/* 152 */             hasher.putBoolean(b);
/*     */           }
/* 154 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putChar(char c) {
/* 159 */           for (Hasher hasher : hashers) {
/* 160 */             hasher.putChar(c);
/*     */           }
/* 162 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putUnencodedChars(CharSequence chars) {
/* 167 */           for (Hasher hasher : hashers) {
/* 168 */             hasher.putUnencodedChars(chars);
/*     */           }
/* 170 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putString(CharSequence chars, Charset charset) {
/* 175 */           for (Hasher hasher : hashers) {
/* 176 */             hasher.putString(chars, charset);
/*     */           }
/* 178 */           return this;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public <T> Hasher putObject(@ParametricNullness T instance, Funnel<? super T> funnel) {
/* 184 */           for (Hasher hasher : hashers) {
/* 185 */             hasher.putObject(instance, funnel);
/*     */           }
/* 187 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public HashCode hash() {
/* 192 */           return AbstractCompositeHashFunction.this.makeHash(hashers);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/AbstractCompositeHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */