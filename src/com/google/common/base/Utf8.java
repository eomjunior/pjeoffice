/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Utf8
/*     */ {
/*     */   public static int encodedLength(CharSequence sequence) {
/*  51 */     int utf16Length = sequence.length();
/*  52 */     int utf8Length = utf16Length;
/*  53 */     int i = 0;
/*     */ 
/*     */     
/*  56 */     while (i < utf16Length && sequence.charAt(i) < '') {
/*  57 */       i++;
/*     */     }
/*     */ 
/*     */     
/*  61 */     for (; i < utf16Length; i++) {
/*  62 */       char c = sequence.charAt(i);
/*  63 */       if (c < 'ࠀ') {
/*  64 */         utf8Length += 127 - c >>> 31;
/*     */       } else {
/*  66 */         utf8Length += encodedLengthGeneral(sequence, i);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  71 */     if (utf8Length < utf16Length)
/*     */     {
/*  73 */       throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (utf8Length + 4294967296L));
/*     */     }
/*     */     
/*  76 */     return utf8Length;
/*     */   }
/*     */   
/*     */   private static int encodedLengthGeneral(CharSequence sequence, int start) {
/*  80 */     int utf16Length = sequence.length();
/*  81 */     int utf8Length = 0;
/*  82 */     for (int i = start; i < utf16Length; i++) {
/*  83 */       char c = sequence.charAt(i);
/*  84 */       if (c < 'ࠀ') {
/*  85 */         utf8Length += 127 - c >>> 31;
/*     */       } else {
/*  87 */         utf8Length += 2;
/*     */         
/*  89 */         if ('?' <= c && c <= '?') {
/*     */           
/*  91 */           if (Character.codePointAt(sequence, i) == c) {
/*  92 */             throw new IllegalArgumentException(unpairedSurrogateMsg(i));
/*     */           }
/*  94 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     return utf8Length;
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
/*     */   public static boolean isWellFormed(byte[] bytes) {
/* 112 */     return isWellFormed(bytes, 0, bytes.length);
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
/*     */   public static boolean isWellFormed(byte[] bytes, int off, int len) {
/* 125 */     int end = off + len;
/* 126 */     Preconditions.checkPositionIndexes(off, end, bytes.length);
/*     */     
/* 128 */     for (int i = off; i < end; i++) {
/* 129 */       if (bytes[i] < 0) {
/* 130 */         return isWellFormedSlowPath(bytes, i, end);
/*     */       }
/*     */     } 
/* 133 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
/* 137 */     int index = off;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 143 */       if (index >= end)
/* 144 */         return true; 
/*     */       int byte1;
/* 146 */       if ((byte1 = bytes[index++]) < 0) {
/*     */         
/* 148 */         if (byte1 < -32) {
/*     */           
/* 150 */           if (index == end) {
/* 151 */             return false;
/*     */           }
/*     */ 
/*     */           
/* 155 */           if (byte1 < -62 || bytes[index++] > -65)
/* 156 */             return false;  continue;
/*     */         } 
/* 158 */         if (byte1 < -16) {
/*     */           
/* 160 */           if (index + 1 >= end) {
/* 161 */             return false;
/*     */           }
/* 163 */           int i = bytes[index++];
/* 164 */           if (i > -65 || (byte1 == -32 && i < -96) || (byte1 == -19 && -96 <= i) || bytes[index++] > -65)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 171 */             return false;
/*     */           }
/*     */           continue;
/*     */         } 
/* 175 */         if (index + 2 >= end) {
/* 176 */           return false;
/*     */         }
/* 178 */         int byte2 = bytes[index++];
/* 179 */         if (byte2 > -65 || (byte1 << 28) + byte2 - -112 >> 30 != 0 || bytes[index++] > -65 || bytes[index++] > -65) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String unpairedSurrogateMsg(int i) {
/* 196 */     return "Unpaired surrogate at index " + i;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Utf8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */