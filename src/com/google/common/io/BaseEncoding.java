/*      */ package com.google.common.io;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.Arrays;
/*      */ import java.util.Objects;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class BaseEncoding
/*      */ {
/*      */   public static final class DecodingException
/*      */     extends IOException
/*      */   {
/*      */     DecodingException(String message) {
/*  140 */       super(message);
/*      */     }
/*      */     
/*      */     DecodingException(Throwable cause) {
/*  144 */       super(cause);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public String encode(byte[] bytes) {
/*  150 */     return encode(bytes, 0, bytes.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String encode(byte[] bytes, int off, int len) {
/*  158 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  159 */     StringBuilder result = new StringBuilder(maxEncodedSize(len));
/*      */     try {
/*  161 */       encodeTo(result, bytes, off, len);
/*  162 */     } catch (IOException impossible) {
/*  163 */       throw new AssertionError(impossible);
/*      */     } 
/*  165 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public abstract OutputStream encodingStream(Writer paramWriter);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public final ByteSink encodingSink(final CharSink encodedSink) {
/*  183 */     Preconditions.checkNotNull(encodedSink);
/*  184 */     return new ByteSink()
/*      */       {
/*      */         public OutputStream openStream() throws IOException {
/*  187 */           return BaseEncoding.this.encodingStream(encodedSink.openStream());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] extract(byte[] result, int length) {
/*  195 */     if (length == result.length) {
/*  196 */       return result;
/*      */     }
/*  198 */     byte[] trunc = new byte[length];
/*  199 */     System.arraycopy(result, 0, trunc, 0, length);
/*  200 */     return trunc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean canDecode(CharSequence paramCharSequence);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] decode(CharSequence chars) {
/*      */     try {
/*  220 */       return decodeChecked(chars);
/*  221 */     } catch (DecodingException badInput) {
/*  222 */       throw new IllegalArgumentException(badInput);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final byte[] decodeChecked(CharSequence chars) throws DecodingException {
/*  235 */     chars = trimTrailingPadding(chars);
/*  236 */     byte[] tmp = new byte[maxDecodedSize(chars.length())];
/*  237 */     int len = decodeTo(tmp, chars);
/*  238 */     return extract(tmp, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public abstract InputStream decodingStream(Reader paramReader);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   public final ByteSource decodingSource(final CharSource encodedSource) {
/*  256 */     Preconditions.checkNotNull(encodedSource);
/*  257 */     return new ByteSource()
/*      */       {
/*      */         public InputStream openStream() throws IOException {
/*  260 */           return BaseEncoding.this.decodingStream(encodedSource.openStream());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   abstract int maxEncodedSize(int paramInt);
/*      */ 
/*      */   
/*      */   abstract void encodeTo(Appendable paramAppendable, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
/*      */   
/*      */   abstract int maxDecodedSize(int paramInt);
/*      */   
/*      */   abstract int decodeTo(byte[] paramArrayOfbyte, CharSequence paramCharSequence) throws DecodingException;
/*      */   
/*      */   CharSequence trimTrailingPadding(CharSequence chars) {
/*  276 */     return (CharSequence)Preconditions.checkNotNull(chars);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  336 */   private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", 
/*      */       
/*  338 */       Character.valueOf('='));
/*      */ 
/*      */   
/*      */   public abstract BaseEncoding omitPadding();
/*      */   
/*      */   public abstract BaseEncoding withPadChar(char paramChar);
/*      */   
/*      */   public abstract BaseEncoding withSeparator(String paramString, int paramInt);
/*      */   
/*      */   public abstract BaseEncoding upperCase();
/*      */   
/*      */   public abstract BaseEncoding lowerCase();
/*      */   
/*      */   public abstract BaseEncoding ignoreCase();
/*      */   
/*      */   public static BaseEncoding base64() {
/*  354 */     return BASE64;
/*      */   }
/*      */   
/*  357 */   private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", 
/*      */       
/*  359 */       Character.valueOf('='));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base64Url() {
/*  376 */     return BASE64_URL;
/*      */   }
/*      */   
/*  379 */   private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", 
/*  380 */       Character.valueOf('='));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base32() {
/*  395 */     return BASE32;
/*      */   }
/*      */   
/*  398 */   private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", 
/*  399 */       Character.valueOf('='));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base32Hex() {
/*  414 */     return BASE32_HEX;
/*      */   }
/*      */   
/*  417 */   private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseEncoding base16() {
/*  433 */     return BASE16;
/*      */   }
/*      */   
/*      */   static final class Alphabet
/*      */   {
/*      */     private final String name;
/*      */     private final char[] chars;
/*      */     final int mask;
/*      */     final int bitsPerChar;
/*      */     final int charsPerChunk;
/*      */     final int bytesPerChunk;
/*      */     private final byte[] decodabet;
/*      */     private final boolean[] validPadding;
/*      */     private final boolean ignoreCase;
/*      */     
/*      */     Alphabet(String name, char[] chars) {
/*  449 */       this(name, chars, decodabetFor(chars), false);
/*      */     }
/*      */     
/*      */     private Alphabet(String name, char[] chars, byte[] decodabet, boolean ignoreCase) {
/*  453 */       this.name = (String)Preconditions.checkNotNull(name);
/*  454 */       this.chars = (char[])Preconditions.checkNotNull(chars);
/*      */       try {
/*  456 */         this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
/*  457 */       } catch (ArithmeticException e) {
/*  458 */         throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  468 */       int zeroesInBitsPerChar = Integer.numberOfTrailingZeros(this.bitsPerChar);
/*  469 */       this.charsPerChunk = 1 << 3 - zeroesInBitsPerChar;
/*  470 */       this.bytesPerChunk = this.bitsPerChar >> zeroesInBitsPerChar;
/*      */       
/*  472 */       this.mask = chars.length - 1;
/*      */       
/*  474 */       this.decodabet = decodabet;
/*      */       
/*  476 */       boolean[] validPadding = new boolean[this.charsPerChunk];
/*  477 */       for (int i = 0; i < this.bytesPerChunk; i++) {
/*  478 */         validPadding[IntMath.divide(i * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
/*      */       }
/*  480 */       this.validPadding = validPadding;
/*  481 */       this.ignoreCase = ignoreCase;
/*      */     }
/*      */     
/*      */     private static byte[] decodabetFor(char[] chars) {
/*  485 */       byte[] decodabet = new byte[128];
/*  486 */       Arrays.fill(decodabet, (byte)-1);
/*  487 */       for (int i = 0; i < chars.length; i++) {
/*  488 */         char c = chars[i];
/*  489 */         Preconditions.checkArgument((c < decodabet.length), "Non-ASCII character: %s", c);
/*  490 */         Preconditions.checkArgument((decodabet[c] == -1), "Duplicate character: %s", c);
/*  491 */         decodabet[c] = (byte)i;
/*      */       } 
/*  493 */       return decodabet;
/*      */     }
/*      */ 
/*      */     
/*      */     Alphabet ignoreCase() {
/*  498 */       if (this.ignoreCase) {
/*  499 */         return this;
/*      */       }
/*      */ 
/*      */       
/*  503 */       byte[] newDecodabet = Arrays.copyOf(this.decodabet, this.decodabet.length);
/*  504 */       for (int upper = 65; upper <= 90; upper++) {
/*  505 */         int lower = upper | 0x20;
/*  506 */         byte decodeUpper = this.decodabet[upper];
/*  507 */         byte decodeLower = this.decodabet[lower];
/*  508 */         if (decodeUpper == -1) {
/*  509 */           newDecodabet[upper] = decodeLower;
/*      */         } else {
/*  511 */           Preconditions.checkState((decodeLower == -1), "Can't ignoreCase() since '%s' and '%s' encode different values", (char)upper, (char)lower);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  516 */           newDecodabet[lower] = decodeUpper;
/*      */         } 
/*      */       } 
/*  519 */       return new Alphabet(this.name + ".ignoreCase()", this.chars, newDecodabet, true);
/*      */     }
/*      */     
/*      */     char encode(int bits) {
/*  523 */       return this.chars[bits];
/*      */     }
/*      */     
/*      */     boolean isValidPaddingStartPosition(int index) {
/*  527 */       return this.validPadding[index % this.charsPerChunk];
/*      */     }
/*      */     
/*      */     boolean canDecode(char ch) {
/*  531 */       return (ch <= '' && this.decodabet[ch] != -1);
/*      */     }
/*      */     
/*      */     int decode(char ch) throws BaseEncoding.DecodingException {
/*  535 */       if (ch > '') {
/*  536 */         throw new BaseEncoding.DecodingException("Unrecognized character: 0x" + Integer.toHexString(ch));
/*      */       }
/*  538 */       int result = this.decodabet[ch];
/*  539 */       if (result == -1) {
/*  540 */         if (ch <= ' ' || ch == '') {
/*  541 */           throw new BaseEncoding.DecodingException("Unrecognized character: 0x" + Integer.toHexString(ch));
/*      */         }
/*  543 */         throw new BaseEncoding.DecodingException("Unrecognized character: " + ch);
/*      */       } 
/*      */       
/*  546 */       return result;
/*      */     }
/*      */     
/*      */     private boolean hasLowerCase() {
/*  550 */       for (char c : this.chars) {
/*  551 */         if (Ascii.isLowerCase(c)) {
/*  552 */           return true;
/*      */         }
/*      */       } 
/*  555 */       return false;
/*      */     }
/*      */     
/*      */     private boolean hasUpperCase() {
/*  559 */       for (char c : this.chars) {
/*  560 */         if (Ascii.isUpperCase(c)) {
/*  561 */           return true;
/*      */         }
/*      */       } 
/*  564 */       return false;
/*      */     }
/*      */     
/*      */     Alphabet upperCase() {
/*  568 */       if (!hasLowerCase()) {
/*  569 */         return this;
/*      */       }
/*  571 */       Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
/*  572 */       char[] upperCased = new char[this.chars.length];
/*  573 */       for (int i = 0; i < this.chars.length; i++) {
/*  574 */         upperCased[i] = Ascii.toUpperCase(this.chars[i]);
/*      */       }
/*  576 */       Alphabet upperCase = new Alphabet(this.name + ".upperCase()", upperCased);
/*  577 */       return this.ignoreCase ? upperCase.ignoreCase() : upperCase;
/*      */     }
/*      */     
/*      */     Alphabet lowerCase() {
/*  581 */       if (!hasUpperCase()) {
/*  582 */         return this;
/*      */       }
/*  584 */       Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
/*  585 */       char[] lowerCased = new char[this.chars.length];
/*  586 */       for (int i = 0; i < this.chars.length; i++) {
/*  587 */         lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
/*      */       }
/*  589 */       Alphabet lowerCase = new Alphabet(this.name + ".lowerCase()", lowerCased);
/*  590 */       return this.ignoreCase ? lowerCase.ignoreCase() : lowerCase;
/*      */     }
/*      */     
/*      */     public boolean matches(char c) {
/*  594 */       return (c < this.decodabet.length && this.decodabet[c] != -1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  599 */       return this.name;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object other) {
/*  604 */       if (other instanceof Alphabet) {
/*  605 */         Alphabet that = (Alphabet)other;
/*  606 */         return (this.ignoreCase == that.ignoreCase && Arrays.equals(this.chars, that.chars));
/*      */       } 
/*  608 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  613 */       return Arrays.hashCode(this.chars) + (this.ignoreCase ? 1231 : 1237);
/*      */     }
/*      */   }
/*      */   
/*      */   static class StandardBaseEncoding extends BaseEncoding {
/*      */     final BaseEncoding.Alphabet alphabet;
/*      */     @CheckForNull
/*      */     final Character paddingChar;
/*      */     
/*      */     StandardBaseEncoding(String name, String alphabetChars, @CheckForNull Character paddingChar) {
/*  623 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar); } @LazyInit @CheckForNull private volatile BaseEncoding upperCase; @LazyInit
/*      */     @CheckForNull
/*      */     private volatile BaseEncoding lowerCase; @LazyInit
/*      */     @CheckForNull
/*  627 */     private volatile BaseEncoding ignoreCase; StandardBaseEncoding(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) { this.alphabet = (BaseEncoding.Alphabet)Preconditions.checkNotNull(alphabet);
/*  628 */       Preconditions.checkArgument((paddingChar == null || 
/*  629 */           !alphabet.matches(paddingChar.charValue())), "Padding character %s was already in alphabet", paddingChar);
/*      */ 
/*      */       
/*  632 */       this.paddingChar = paddingChar; }
/*      */ 
/*      */ 
/*      */     
/*      */     int maxEncodedSize(int bytes) {
/*  637 */       return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(final Writer out) {
/*  644 */       Preconditions.checkNotNull(out);
/*  645 */       return new OutputStream() {
/*  646 */           int bitBuffer = 0;
/*  647 */           int bitBufferLength = 0;
/*  648 */           int writtenChars = 0;
/*      */ 
/*      */           
/*      */           public void write(int b) throws IOException {
/*  652 */             this.bitBuffer <<= 8;
/*  653 */             this.bitBuffer |= b & 0xFF;
/*  654 */             this.bitBufferLength += 8;
/*  655 */             while (this.bitBufferLength >= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar) {
/*  656 */               int charIndex = this.bitBuffer >> this.bitBufferLength - BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  657 */               out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  658 */               this.writtenChars++;
/*  659 */               this.bitBufferLength -= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void flush() throws IOException {
/*  665 */             out.flush();
/*      */           }
/*      */ 
/*      */           
/*      */           public void close() throws IOException {
/*  670 */             if (this.bitBufferLength > 0) {
/*  671 */               int charIndex = this.bitBuffer << BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & BaseEncoding.StandardBaseEncoding.this.alphabet.mask;
/*  672 */               out.write(BaseEncoding.StandardBaseEncoding.this.alphabet.encode(charIndex));
/*  673 */               this.writtenChars++;
/*  674 */               if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null) {
/*  675 */                 while (this.writtenChars % BaseEncoding.StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
/*  676 */                   out.write(BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue());
/*  677 */                   this.writtenChars++;
/*      */                 } 
/*      */               }
/*      */             } 
/*  681 */             out.close();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  688 */       Preconditions.checkNotNull(target);
/*  689 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length); int i;
/*  690 */       for (i = 0; i < len; i += this.alphabet.bytesPerChunk) {
/*  691 */         encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
/*      */       }
/*      */     }
/*      */     
/*      */     void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  696 */       Preconditions.checkNotNull(target);
/*  697 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  698 */       Preconditions.checkArgument((len <= this.alphabet.bytesPerChunk));
/*  699 */       long bitBuffer = 0L;
/*  700 */       for (int i = 0; i < len; i++) {
/*  701 */         bitBuffer |= (bytes[off + i] & 0xFF);
/*  702 */         bitBuffer <<= 8L;
/*      */       } 
/*      */       
/*  705 */       int bitOffset = (len + 1) * 8 - this.alphabet.bitsPerChar;
/*  706 */       int bitsProcessed = 0;
/*  707 */       while (bitsProcessed < len * 8) {
/*  708 */         int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & this.alphabet.mask;
/*  709 */         target.append(this.alphabet.encode(charIndex));
/*  710 */         bitsProcessed += this.alphabet.bitsPerChar;
/*      */       } 
/*  712 */       if (this.paddingChar != null) {
/*  713 */         while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
/*  714 */           target.append(this.paddingChar.charValue());
/*  715 */           bitsProcessed += this.alphabet.bitsPerChar;
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int maxDecodedSize(int chars) {
/*  722 */       return (int)((this.alphabet.bitsPerChar * chars + 7L) / 8L);
/*      */     }
/*      */ 
/*      */     
/*      */     CharSequence trimTrailingPadding(CharSequence chars) {
/*  727 */       Preconditions.checkNotNull(chars);
/*  728 */       if (this.paddingChar == null) {
/*  729 */         return chars;
/*      */       }
/*  731 */       char padChar = this.paddingChar.charValue();
/*      */       int l;
/*  733 */       for (l = chars.length() - 1; l >= 0 && 
/*  734 */         chars.charAt(l) == padChar; l--);
/*      */ 
/*      */ 
/*      */       
/*  738 */       return chars.subSequence(0, l + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDecode(CharSequence chars) {
/*  743 */       Preconditions.checkNotNull(chars);
/*  744 */       chars = trimTrailingPadding(chars);
/*  745 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  746 */         return false;
/*      */       }
/*  748 */       for (int i = 0; i < chars.length(); i++) {
/*  749 */         if (!this.alphabet.canDecode(chars.charAt(i))) {
/*  750 */           return false;
/*      */         }
/*      */       } 
/*  753 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  758 */       Preconditions.checkNotNull(target);
/*  759 */       chars = trimTrailingPadding(chars);
/*  760 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/*  761 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  763 */       int bytesWritten = 0; int charIdx;
/*  764 */       for (charIdx = 0; charIdx < chars.length(); charIdx += this.alphabet.charsPerChunk) {
/*  765 */         long chunk = 0L;
/*  766 */         int charsProcessed = 0;
/*  767 */         for (int i = 0; i < this.alphabet.charsPerChunk; i++) {
/*  768 */           chunk <<= this.alphabet.bitsPerChar;
/*  769 */           if (charIdx + i < chars.length()) {
/*  770 */             chunk |= this.alphabet.decode(chars.charAt(charIdx + charsProcessed++));
/*      */           }
/*      */         } 
/*  773 */         int minOffset = this.alphabet.bytesPerChunk * 8 - charsProcessed * this.alphabet.bitsPerChar;
/*  774 */         for (int offset = (this.alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
/*  775 */           target[bytesWritten++] = (byte)(int)(chunk >>> offset & 0xFFL);
/*      */         }
/*      */       } 
/*  778 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(final Reader reader) {
/*  785 */       Preconditions.checkNotNull(reader);
/*  786 */       return new InputStream() {
/*  787 */           int bitBuffer = 0;
/*  788 */           int bitBufferLength = 0;
/*  789 */           int readChars = 0;
/*      */           
/*      */           boolean hitPadding = false;
/*      */           
/*      */           public int read() throws IOException {
/*      */             while (true) {
/*  795 */               int readChar = reader.read();
/*  796 */               if (readChar == -1) {
/*  797 */                 if (!this.hitPadding && !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
/*  798 */                   throw new BaseEncoding.DecodingException("Invalid input length " + this.readChars);
/*      */                 }
/*  800 */                 return -1;
/*      */               } 
/*  802 */               this.readChars++;
/*  803 */               char ch = (char)readChar;
/*  804 */               if (BaseEncoding.StandardBaseEncoding.this.paddingChar != null && BaseEncoding.StandardBaseEncoding.this.paddingChar.charValue() == ch) {
/*  805 */                 if (!this.hitPadding && (this.readChars == 1 || 
/*  806 */                   !BaseEncoding.StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
/*  807 */                   throw new BaseEncoding.DecodingException("Padding cannot start at index " + this.readChars);
/*      */                 }
/*  809 */                 this.hitPadding = true; continue;
/*  810 */               }  if (this.hitPadding) {
/*  811 */                 throw new BaseEncoding.DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars);
/*      */               }
/*      */               
/*  814 */               this.bitBuffer <<= BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*  815 */               this.bitBuffer |= BaseEncoding.StandardBaseEncoding.this.alphabet.decode(ch);
/*  816 */               this.bitBufferLength += BaseEncoding.StandardBaseEncoding.this.alphabet.bitsPerChar;
/*      */               
/*  818 */               if (this.bitBufferLength >= 8) {
/*  819 */                 this.bitBufferLength -= 8;
/*  820 */                 return this.bitBuffer >> this.bitBufferLength & 0xFF;
/*      */               } 
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           public int read(byte[] buf, int off, int len) throws IOException {
/*  833 */             Preconditions.checkPositionIndexes(off, off + len, buf.length);
/*      */             
/*  835 */             int i = off;
/*  836 */             for (; i < off + len; i++) {
/*  837 */               int b = read();
/*  838 */               if (b == -1) {
/*  839 */                 int read = i - off;
/*  840 */                 return (read == 0) ? -1 : read;
/*      */               } 
/*  842 */               buf[i] = (byte)b;
/*      */             } 
/*  844 */             return i - off;
/*      */           }
/*      */ 
/*      */           
/*      */           public void close() throws IOException {
/*  849 */             reader.close();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding omitPadding() {
/*  856 */       return (this.paddingChar == null) ? this : newInstance(this.alphabet, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar) {
/*  861 */       if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar
/*  862 */         .charValue() == padChar)) {
/*  863 */         return this;
/*      */       }
/*  865 */       return newInstance(this.alphabet, Character.valueOf(padChar));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars) {
/*  871 */       for (int i = 0; i < separator.length(); i++) {
/*  872 */         Preconditions.checkArgument(
/*  873 */             !this.alphabet.matches(separator.charAt(i)), "Separator (%s) cannot contain alphabet characters", separator);
/*      */       }
/*      */ 
/*      */       
/*  877 */       if (this.paddingChar != null) {
/*  878 */         Preconditions.checkArgument(
/*  879 */             (separator.indexOf(this.paddingChar.charValue()) < 0), "Separator (%s) cannot contain padding character", separator);
/*      */       }
/*      */ 
/*      */       
/*  883 */       return new BaseEncoding.SeparatedBaseEncoding(this, separator, afterEveryChars);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseEncoding upperCase() {
/*  892 */       BaseEncoding result = this.upperCase;
/*  893 */       if (result == null) {
/*  894 */         BaseEncoding.Alphabet upper = this.alphabet.upperCase();
/*  895 */         result = this.upperCase = (upper == this.alphabet) ? this : newInstance(upper, this.paddingChar);
/*      */       } 
/*  897 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding lowerCase() {
/*  902 */       BaseEncoding result = this.lowerCase;
/*  903 */       if (result == null) {
/*  904 */         BaseEncoding.Alphabet lower = this.alphabet.lowerCase();
/*  905 */         result = this.lowerCase = (lower == this.alphabet) ? this : newInstance(lower, this.paddingChar);
/*      */       } 
/*  907 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding ignoreCase() {
/*  912 */       BaseEncoding result = this.ignoreCase;
/*  913 */       if (result == null) {
/*  914 */         BaseEncoding.Alphabet ignore = this.alphabet.ignoreCase();
/*  915 */         result = this.ignoreCase = (ignore == this.alphabet) ? this : newInstance(ignore, this.paddingChar);
/*      */       } 
/*  917 */       return result;
/*      */     }
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
/*  921 */       return new StandardBaseEncoding(alphabet, paddingChar);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  926 */       StringBuilder builder = new StringBuilder("BaseEncoding.");
/*  927 */       builder.append(this.alphabet);
/*  928 */       if (8 % this.alphabet.bitsPerChar != 0) {
/*  929 */         if (this.paddingChar == null) {
/*  930 */           builder.append(".omitPadding()");
/*      */         } else {
/*  932 */           builder.append(".withPadChar('").append(this.paddingChar).append("')");
/*      */         } 
/*      */       }
/*  935 */       return builder.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@CheckForNull Object other) {
/*  940 */       if (other instanceof StandardBaseEncoding) {
/*  941 */         StandardBaseEncoding that = (StandardBaseEncoding)other;
/*  942 */         return (this.alphabet.equals(that.alphabet) && 
/*  943 */           Objects.equals(this.paddingChar, that.paddingChar));
/*      */       } 
/*  945 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  950 */       return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base16Encoding extends StandardBaseEncoding {
/*  955 */     final char[] encoding = new char[512];
/*      */     
/*      */     Base16Encoding(String name, String alphabetChars) {
/*  958 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()));
/*      */     }
/*      */     
/*      */     private Base16Encoding(BaseEncoding.Alphabet alphabet) {
/*  962 */       super(alphabet, null);
/*  963 */       Preconditions.checkArgument((alphabet.chars.length == 16));
/*  964 */       for (int i = 0; i < 256; i++) {
/*  965 */         this.encoding[i] = alphabet.encode(i >>> 4);
/*  966 */         this.encoding[i | 0x100] = alphabet.encode(i & 0xF);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/*  972 */       Preconditions.checkNotNull(target);
/*  973 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  974 */       for (int i = 0; i < len; i++) {
/*  975 */         int b = bytes[off + i] & 0xFF;
/*  976 */         target.append(this.encoding[b]);
/*  977 */         target.append(this.encoding[b | 0x100]);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/*  983 */       Preconditions.checkNotNull(target);
/*  984 */       if (chars.length() % 2 == 1) {
/*  985 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/*  987 */       int bytesWritten = 0;
/*  988 */       for (int i = 0; i < chars.length(); i += 2) {
/*  989 */         int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
/*  990 */         target[bytesWritten++] = (byte)decoded;
/*      */       } 
/*  992 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
/*  997 */       return new Base16Encoding(alphabet);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class Base64Encoding extends StandardBaseEncoding {
/*      */     Base64Encoding(String name, String alphabetChars, @CheckForNull Character paddingChar) {
/* 1003 */       this(new BaseEncoding.Alphabet(name, alphabetChars.toCharArray()), paddingChar);
/*      */     }
/*      */     
/*      */     private Base64Encoding(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
/* 1007 */       super(alphabet, paddingChar);
/* 1008 */       Preconditions.checkArgument((alphabet.chars.length == 64));
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/* 1013 */       Preconditions.checkNotNull(target);
/* 1014 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/* 1015 */       int i = off;
/* 1016 */       for (int remaining = len; remaining >= 3; remaining -= 3) {
/* 1017 */         int chunk = (bytes[i++] & 0xFF) << 16 | (bytes[i++] & 0xFF) << 8 | bytes[i++] & 0xFF;
/* 1018 */         target.append(this.alphabet.encode(chunk >>> 18));
/* 1019 */         target.append(this.alphabet.encode(chunk >>> 12 & 0x3F));
/* 1020 */         target.append(this.alphabet.encode(chunk >>> 6 & 0x3F));
/* 1021 */         target.append(this.alphabet.encode(chunk & 0x3F));
/*      */       } 
/* 1023 */       if (i < off + len) {
/* 1024 */         encodeChunkTo(target, bytes, i, off + len - i);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/* 1030 */       Preconditions.checkNotNull(target);
/* 1031 */       chars = trimTrailingPadding(chars);
/* 1032 */       if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
/* 1033 */         throw new BaseEncoding.DecodingException("Invalid input length " + chars.length());
/*      */       }
/* 1035 */       int bytesWritten = 0;
/* 1036 */       for (int i = 0; i < chars.length(); ) {
/* 1037 */         int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
/* 1038 */         chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
/* 1039 */         target[bytesWritten++] = (byte)(chunk >>> 16);
/* 1040 */         if (i < chars.length()) {
/* 1041 */           chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
/* 1042 */           target[bytesWritten++] = (byte)(chunk >>> 8 & 0xFF);
/* 1043 */           if (i < chars.length()) {
/* 1044 */             chunk |= this.alphabet.decode(chars.charAt(i++));
/* 1045 */             target[bytesWritten++] = (byte)(chunk & 0xFF);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1049 */       return bytesWritten;
/*      */     }
/*      */ 
/*      */     
/*      */     BaseEncoding newInstance(BaseEncoding.Alphabet alphabet, @CheckForNull Character paddingChar) {
/* 1054 */       return new Base64Encoding(alphabet, paddingChar);
/*      */     }
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static Reader ignoringReader(final Reader delegate, final String toIgnore) {
/* 1061 */     Preconditions.checkNotNull(delegate);
/* 1062 */     Preconditions.checkNotNull(toIgnore);
/* 1063 */     return new Reader()
/*      */       {
/*      */         public int read() throws IOException {
/*      */           int readChar;
/*      */           do {
/* 1068 */             readChar = delegate.read();
/* 1069 */           } while (readChar != -1 && toIgnore.indexOf((char)readChar) >= 0);
/* 1070 */           return readChar;
/*      */         }
/*      */ 
/*      */         
/*      */         public int read(char[] cbuf, int off, int len) throws IOException {
/* 1075 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() throws IOException {
/* 1080 */           delegate.close();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static Appendable separatingAppendable(final Appendable delegate, final String separator, final int afterEveryChars) {
/* 1087 */     Preconditions.checkNotNull(delegate);
/* 1088 */     Preconditions.checkNotNull(separator);
/* 1089 */     Preconditions.checkArgument((afterEveryChars > 0));
/* 1090 */     return new Appendable() {
/* 1091 */         int charsUntilSeparator = afterEveryChars;
/*      */ 
/*      */         
/*      */         public Appendable append(char c) throws IOException {
/* 1095 */           if (this.charsUntilSeparator == 0) {
/* 1096 */             delegate.append(separator);
/* 1097 */             this.charsUntilSeparator = afterEveryChars;
/*      */           } 
/* 1099 */           delegate.append(c);
/* 1100 */           this.charsUntilSeparator--;
/* 1101 */           return this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Appendable append(@CheckForNull CharSequence chars, int off, int len) {
/* 1106 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public Appendable append(@CheckForNull CharSequence chars) {
/* 1111 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars) {
/* 1119 */     final Appendable separatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
/* 1120 */     return new Writer()
/*      */       {
/*      */         public void write(int c) throws IOException {
/* 1123 */           separatingAppendable.append((char)c);
/*      */         }
/*      */ 
/*      */         
/*      */         public void write(char[] chars, int off, int len) throws IOException {
/* 1128 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public void flush() throws IOException {
/* 1133 */           delegate.flush();
/*      */         }
/*      */ 
/*      */         
/*      */         public void close() throws IOException {
/* 1138 */           delegate.close();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static final class SeparatedBaseEncoding extends BaseEncoding {
/*      */     private final BaseEncoding delegate;
/*      */     private final String separator;
/*      */     private final int afterEveryChars;
/*      */     
/*      */     SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
/* 1149 */       this.delegate = (BaseEncoding)Preconditions.checkNotNull(delegate);
/* 1150 */       this.separator = (String)Preconditions.checkNotNull(separator);
/* 1151 */       this.afterEveryChars = afterEveryChars;
/* 1152 */       Preconditions.checkArgument((afterEveryChars > 0), "Cannot add a separator after every %s chars", afterEveryChars);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     CharSequence trimTrailingPadding(CharSequence chars) {
/* 1158 */       return this.delegate.trimTrailingPadding(chars);
/*      */     }
/*      */ 
/*      */     
/*      */     int maxEncodedSize(int bytes) {
/* 1163 */       int unseparatedSize = this.delegate.maxEncodedSize(bytes);
/* 1164 */       return unseparatedSize + this.separator
/* 1165 */         .length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     public OutputStream encodingStream(Writer output) {
/* 1172 */       return this.delegate.encodingStream(separatingWriter(output, this.separator, this.afterEveryChars));
/*      */     }
/*      */ 
/*      */     
/*      */     void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
/* 1177 */       this.delegate.encodeTo(separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
/*      */     }
/*      */ 
/*      */     
/*      */     int maxDecodedSize(int chars) {
/* 1182 */       return this.delegate.maxDecodedSize(chars);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canDecode(CharSequence chars) {
/* 1187 */       StringBuilder builder = new StringBuilder();
/* 1188 */       for (int i = 0; i < chars.length(); i++) {
/* 1189 */         char c = chars.charAt(i);
/* 1190 */         if (this.separator.indexOf(c) < 0) {
/* 1191 */           builder.append(c);
/*      */         }
/*      */       } 
/* 1194 */       return this.delegate.canDecode(builder);
/*      */     }
/*      */ 
/*      */     
/*      */     int decodeTo(byte[] target, CharSequence chars) throws BaseEncoding.DecodingException {
/* 1199 */       StringBuilder stripped = new StringBuilder(chars.length());
/* 1200 */       for (int i = 0; i < chars.length(); i++) {
/* 1201 */         char c = chars.charAt(i);
/* 1202 */         if (this.separator.indexOf(c) < 0) {
/* 1203 */           stripped.append(c);
/*      */         }
/*      */       } 
/* 1206 */       return this.delegate.decodeTo(target, stripped);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     public InputStream decodingStream(Reader reader) {
/* 1213 */       return this.delegate.decodingStream(ignoringReader(reader, this.separator));
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding omitPadding() {
/* 1218 */       return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withPadChar(char padChar) {
/* 1223 */       return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding withSeparator(String separator, int afterEveryChars) {
/* 1228 */       throw new UnsupportedOperationException("Already have a separator");
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding upperCase() {
/* 1233 */       return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding lowerCase() {
/* 1238 */       return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public BaseEncoding ignoreCase() {
/* 1243 */       return this.delegate.ignoreCase().withSeparator(this.separator, this.afterEveryChars);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1248 */       return this.delegate + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/BaseEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */