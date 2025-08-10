/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.Immutable;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ @ElementTypesAreNonnullByDefault
/*    */ abstract class AbstractHashFunction
/*    */   implements HashFunction
/*    */ {
/*    */   public <T> HashCode hashObject(@ParametricNullness T instance, Funnel<? super T> funnel) {
/* 36 */     return newHasher().<T>putObject(instance, funnel).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashUnencodedChars(CharSequence input) {
/* 41 */     int len = input.length();
/* 42 */     return newHasher(len * 2).putUnencodedChars(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashString(CharSequence input, Charset charset) {
/* 47 */     return newHasher().putString(input, charset).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashInt(int input) {
/* 52 */     return newHasher(4).putInt(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashLong(long input) {
/* 57 */     return newHasher(8).putLong(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashBytes(byte[] input) {
/* 62 */     return hashBytes(input, 0, input.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashBytes(byte[] input, int off, int len) {
/* 67 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/* 68 */     return newHasher(len).putBytes(input, off, len).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashBytes(ByteBuffer input) {
/* 73 */     return newHasher(input.remaining()).putBytes(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public Hasher newHasher(int expectedInputSize) {
/* 78 */     Preconditions.checkArgument((expectedInputSize >= 0), "expectedInputSize must be >= 0 but was %s", expectedInputSize);
/*    */     
/* 80 */     return newHasher();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/AbstractHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */