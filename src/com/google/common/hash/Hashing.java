/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.invoke.MethodType;
/*     */ import java.security.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
/*     */ import javax.annotation.CheckForNull;
/*     */ import javax.crypto.spec.SecretKeySpec;
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
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public final class Hashing
/*     */ {
/*     */   public static HashFunction goodFastHash(int minimumBits) {
/*  71 */     int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
/*     */     
/*  73 */     if (bits == 32) {
/*  74 */       return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
/*     */     }
/*  76 */     if (bits <= 128) {
/*  77 */       return Murmur3_128HashFunction.GOOD_FAST_HASH_128;
/*     */     }
/*     */ 
/*     */     
/*  81 */     int hashFunctionsNeeded = (bits + 127) / 128;
/*  82 */     HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
/*  83 */     hashFunctions[0] = Murmur3_128HashFunction.GOOD_FAST_HASH_128;
/*  84 */     int seed = GOOD_FAST_HASH_SEED;
/*  85 */     for (int i = 1; i < hashFunctionsNeeded; i++) {
/*  86 */       seed += 1500450271;
/*  87 */       hashFunctions[i] = murmur3_128(seed);
/*     */     } 
/*  89 */     return new ConcatenatedHashFunction(hashFunctions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
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
/*     */   @Deprecated
/*     */   public static HashFunction murmur3_32(int seed) {
/* 114 */     return new Murmur3_32HashFunction(seed, false);
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
/*     */   @Deprecated
/*     */   public static HashFunction murmur3_32() {
/* 132 */     return Murmur3_32HashFunction.MURMUR3_32;
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
/*     */   public static HashFunction murmur3_32_fixed(int seed) {
/* 148 */     return new Murmur3_32HashFunction(seed, true);
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
/*     */   public static HashFunction murmur3_32_fixed() {
/* 164 */     return Murmur3_32HashFunction.MURMUR3_32_FIXED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_128(int seed) {
/* 175 */     return new Murmur3_128HashFunction(seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction murmur3_128() {
/* 186 */     return Murmur3_128HashFunction.MURMUR3_128;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24() {
/* 196 */     return SipHashFunction.SIP_HASH_24;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sipHash24(long k0, long k1) {
/* 206 */     return new SipHashFunction(2, 4, k0, k1);
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
/*     */   @Deprecated
/*     */   public static HashFunction md5() {
/* 223 */     return Md5Holder.MD5;
/*     */   }
/*     */   
/*     */   private static class Md5Holder {
/* 227 */     static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
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
/*     */   @Deprecated
/*     */   public static HashFunction sha1() {
/* 244 */     return Sha1Holder.SHA_1;
/*     */   }
/*     */   
/*     */   private static class Sha1Holder {
/* 248 */     static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashFunction sha256() {
/* 253 */     return Sha256Holder.SHA_256;
/*     */   }
/*     */   
/*     */   private static class Sha256Holder {
/* 257 */     static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha384() {
/* 267 */     return Sha384Holder.SHA_384;
/*     */   }
/*     */   
/*     */   private static class Sha384Holder {
/* 271 */     static final HashFunction SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction sha512() {
/* 277 */     return Sha512Holder.SHA_512;
/*     */   }
/*     */   
/*     */   private static class Sha512Holder {
/* 281 */     static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
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
/*     */   public static HashFunction hmacMd5(Key key) {
/* 294 */     return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
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
/*     */   public static HashFunction hmacMd5(byte[] key) {
/* 306 */     return hmacMd5(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacMD5"));
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
/*     */   public static HashFunction hmacSha1(Key key) {
/* 318 */     return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
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
/*     */   public static HashFunction hmacSha1(byte[] key) {
/* 330 */     return hmacSha1(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA1"));
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
/*     */   public static HashFunction hmacSha256(Key key) {
/* 342 */     return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
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
/*     */   public static HashFunction hmacSha256(byte[] key) {
/* 354 */     return hmacSha256(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA256"));
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
/*     */   public static HashFunction hmacSha512(Key key) {
/* 366 */     return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
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
/*     */   public static HashFunction hmacSha512(byte[] key) {
/* 378 */     return hmacSha512(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA512"));
/*     */   }
/*     */   
/*     */   private static String hmacToString(String methodName, Key key) {
/* 382 */     return "Hashing." + methodName + "(Key[algorithm=" + key
/*     */ 
/*     */       
/* 385 */       .getAlgorithm() + ", format=" + key
/*     */       
/* 387 */       .getFormat() + "])";
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
/*     */   public static HashFunction crc32c() {
/* 402 */     return Crc32CSupplier.HASH_FUNCTION;
/*     */   }
/*     */   
/*     */   @Immutable
/*     */   private enum Crc32CSupplier implements ImmutableSupplier<HashFunction> {
/* 407 */     JAVA_UTIL_ZIP
/*     */     {
/*     */       public HashFunction get()
/*     */       {
/* 411 */         return Hashing.ChecksumType.CRC_32C.hashFunction;
/*     */       }
/*     */     },
/* 414 */     ABSTRACT_HASH_FUNCTION
/*     */     {
/*     */       public HashFunction get() {
/* 417 */         return Crc32cHashFunction.CRC_32_C;
/*     */       }
/*     */     };
/*     */     
/* 421 */     static final HashFunction HASH_FUNCTION = (HashFunction)pickFunction().get();
/*     */     
/*     */     private static Crc32CSupplier pickFunction() {
/* 424 */       Crc32CSupplier[] functions = values();
/*     */       
/* 426 */       if (functions.length == 1)
/*     */       {
/* 428 */         return functions[0];
/*     */       }
/*     */ 
/*     */       
/* 432 */       Crc32CSupplier javaUtilZip = functions[0];
/*     */       
/*     */       try {
/* 435 */         Class.forName("java.util.zip.CRC32C");
/* 436 */         return javaUtilZip;
/* 437 */       } catch (ClassNotFoundException runningUnderJava8) {
/* 438 */         return ABSTRACT_HASH_FUNCTION;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashFunction crc32() {
/* 456 */     return ChecksumType.CRC_32.hashFunction;
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
/*     */   public static HashFunction adler32() {
/* 472 */     return ChecksumType.ADLER_32.hashFunction;
/*     */   }
/*     */   
/*     */   @Immutable
/*     */   enum ChecksumType implements ImmutableSupplier<Checksum> {
/* 477 */     CRC_32("Hashing.crc32()")
/*     */     {
/*     */       public Checksum get() {
/* 480 */         return new CRC32();
/*     */       }
/*     */     },
/* 483 */     CRC_32C("Hashing.crc32c()")
/*     */     {
/*     */       public Checksum get()
/*     */       {
/* 487 */         return Hashing.Crc32cMethodHandles.newCrc32c();
/*     */       }
/*     */     },
/* 490 */     ADLER_32("Hashing.adler32()")
/*     */     {
/*     */       public Checksum get() {
/* 493 */         return new Adler32();
/*     */       }
/*     */     };
/*     */     
/*     */     public final HashFunction hashFunction;
/*     */     
/*     */     ChecksumType(String toString) {
/* 500 */       this.hashFunction = new ChecksumHashFunction(this, 32, toString);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Crc32cMethodHandles
/*     */   {
/* 507 */     private static final MethodHandle CONSTRUCTOR = crc32cConstructor();
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     static Checksum newCrc32c() {
/*     */       try {
/* 512 */         return CONSTRUCTOR.invokeExact();
/* 513 */       } catch (Throwable e) {
/* 514 */         Throwables.throwIfUnchecked(e);
/*     */         
/* 516 */         throw newLinkageError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static MethodHandle crc32cConstructor() {
/*     */       try {
/* 522 */         Class<?> clazz = Class.forName("java.util.zip.CRC32C");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 530 */         return MethodHandles.lookup()
/* 531 */           .findConstructor(clazz, MethodType.methodType(void.class))
/* 532 */           .asType(MethodType.methodType(Checksum.class));
/* 533 */       } catch (ClassNotFoundException e) {
/*     */         
/* 535 */         throw new AssertionError(e);
/* 536 */       } catch (IllegalAccessException e) {
/*     */         
/* 538 */         throw newLinkageError(e);
/* 539 */       } catch (NoSuchMethodException e) {
/*     */         
/* 541 */         throw newLinkageError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static LinkageError newLinkageError(Throwable cause) {
/* 546 */       return new LinkageError(cause.toString(), cause);
/*     */     }
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
/*     */   public static HashFunction farmHashFingerprint64() {
/* 569 */     return FarmHashFingerprint64.FARMHASH_FINGERPRINT_64;
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
/*     */   public static HashFunction fingerprint2011() {
/* 593 */     return Fingerprint2011.FINGERPRINT_2011;
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
/*     */   public static int consistentHash(HashCode hashCode, int buckets) {
/* 627 */     return consistentHash(hashCode.padToLong(), buckets);
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
/*     */   public static int consistentHash(long input, int buckets) {
/* 661 */     Preconditions.checkArgument((buckets > 0), "buckets must be positive: %s", buckets);
/* 662 */     LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
/* 663 */     int candidate = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 668 */       int next = (int)((candidate + 1) / generator.nextDouble());
/* 669 */       if (next >= 0 && next < buckets) {
/* 670 */         candidate = next; continue;
/*     */       }  break;
/* 672 */     }  return candidate;
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
/*     */   public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
/* 687 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 688 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 689 */     int bits = ((HashCode)iterator.next()).bits();
/* 690 */     byte[] resultBytes = new byte[bits / 8];
/* 691 */     for (HashCode hashCode : hashCodes) {
/* 692 */       byte[] nextBytes = hashCode.asBytes();
/* 693 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 695 */       for (int i = 0; i < nextBytes.length; i++) {
/* 696 */         resultBytes[i] = (byte)(resultBytes[i] * 37 ^ nextBytes[i]);
/*     */       }
/*     */     } 
/* 699 */     return HashCode.fromBytesNoCopy(resultBytes);
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
/*     */   public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
/* 712 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 713 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 714 */     byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
/* 715 */     for (HashCode hashCode : hashCodes) {
/* 716 */       byte[] nextBytes = hashCode.asBytes();
/* 717 */       Preconditions.checkArgument((nextBytes.length == resultBytes.length), "All hashcodes must have the same bit length.");
/*     */       
/* 719 */       for (int i = 0; i < nextBytes.length; i++) {
/* 720 */         resultBytes[i] = (byte)(resultBytes[i] + nextBytes[i]);
/*     */       }
/*     */     } 
/* 723 */     return HashCode.fromBytesNoCopy(resultBytes);
/*     */   }
/*     */ 
/*     */   
/*     */   static int checkPositiveAndMakeMultipleOf32(int bits) {
/* 728 */     Preconditions.checkArgument((bits > 0), "Number of bits must be positive");
/* 729 */     return bits + 31 & 0xFFFFFFE0;
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
/*     */   public static HashFunction concatenating(HashFunction first, HashFunction second, HashFunction... rest) {
/* 745 */     List<HashFunction> list = new ArrayList<>();
/* 746 */     list.add(first);
/* 747 */     list.add(second);
/* 748 */     Collections.addAll(list, rest);
/* 749 */     return new ConcatenatedHashFunction(list.<HashFunction>toArray(new HashFunction[0]));
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
/*     */   public static HashFunction concatenating(Iterable<HashFunction> hashFunctions) {
/* 763 */     Preconditions.checkNotNull(hashFunctions);
/*     */     
/* 765 */     List<HashFunction> list = new ArrayList<>();
/* 766 */     for (HashFunction hashFunction : hashFunctions) {
/* 767 */       list.add(hashFunction);
/*     */     }
/* 769 */     Preconditions.checkArgument(!list.isEmpty(), "number of hash functions (%s) must be > 0", list.size());
/* 770 */     return new ConcatenatedHashFunction(list.<HashFunction>toArray(new HashFunction[0]));
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedHashFunction
/*     */     extends AbstractCompositeHashFunction {
/*     */     private ConcatenatedHashFunction(HashFunction... functions) {
/* 776 */       super(functions);
/* 777 */       for (HashFunction function : functions) {
/* 778 */         Preconditions.checkArgument(
/* 779 */             (function.bits() % 8 == 0), "the number of bits (%s) in hashFunction (%s) must be divisible by 8", function
/*     */             
/* 781 */             .bits(), function);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     HashCode makeHash(Hasher[] hashers) {
/* 788 */       byte[] bytes = new byte[bits() / 8];
/* 789 */       int i = 0;
/* 790 */       for (Hasher hasher : hashers) {
/* 791 */         HashCode newHash = hasher.hash();
/* 792 */         i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
/*     */       } 
/* 794 */       return HashCode.fromBytesNoCopy(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 799 */       int bitSum = 0;
/* 800 */       for (HashFunction function : this.functions) {
/* 801 */         bitSum += function.bits();
/*     */       }
/* 803 */       return bitSum;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 808 */       if (object instanceof ConcatenatedHashFunction) {
/* 809 */         ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
/* 810 */         return Arrays.equals((Object[])this.functions, (Object[])other.functions);
/*     */       } 
/* 812 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 817 */       return Arrays.hashCode((Object[])this.functions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LinearCongruentialGenerator
/*     */   {
/*     */     private long state;
/*     */ 
/*     */     
/*     */     public LinearCongruentialGenerator(long seed) {
/* 829 */       this.state = seed;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 833 */       this.state = 2862933555777941757L * this.state + 1L;
/* 834 */       return ((int)(this.state >>> 33L) + 1) / 2.147483648E9D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */