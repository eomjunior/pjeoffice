/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.function.Predicate;
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
/*      */ public abstract class CharMatcher
/*      */   implements Predicate<Character>
/*      */ {
/*      */   private static final int DISTINCT_CHARS = 65536;
/*      */   
/*      */   public static CharMatcher any() {
/*  120 */     return Any.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher none() {
/*  129 */     return None.INSTANCE;
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
/*      */   public static CharMatcher whitespace() {
/*  147 */     return Whitespace.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher breakingWhitespace() {
/*  158 */     return BreakingWhitespace.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher ascii() {
/*  167 */     return Ascii.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher digit() {
/*  180 */     return Digit.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher javaDigit() {
/*  193 */     return JavaDigit.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher javaLetter() {
/*  206 */     return JavaLetter.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static CharMatcher javaLetterOrDigit() {
/*  218 */     return JavaLetterOrDigit.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher javaUpperCase() {
/*  231 */     return JavaUpperCase.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher javaLowerCase() {
/*  244 */     return JavaLowerCase.INSTANCE;
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
/*      */   public static CharMatcher javaIsoControl() {
/*  256 */     return JavaIsoControl.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher invisible() {
/*  272 */     return Invisible.INSTANCE;
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
/*      */   @Deprecated
/*      */   public static CharMatcher singleWidth() {
/*  290 */     return SingleWidth.INSTANCE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher is(char match) {
/*  297 */     return new Is(match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher isNot(char match) {
/*  306 */     return new IsNot(match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher anyOf(CharSequence sequence) {
/*  314 */     switch (sequence.length()) {
/*      */       case 0:
/*  316 */         return none();
/*      */       case 1:
/*  318 */         return is(sequence.charAt(0));
/*      */       case 2:
/*  320 */         return isEither(sequence.charAt(0), sequence.charAt(1));
/*      */     } 
/*      */ 
/*      */     
/*  324 */     return new AnyOf(sequence);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher noneOf(CharSequence sequence) {
/*  333 */     return anyOf(sequence).negate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher inRange(char startInclusive, char endInclusive) {
/*  344 */     return new InRange(startInclusive, endInclusive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharMatcher forPredicate(Predicate<? super Character> predicate) {
/*  352 */     return (predicate instanceof CharMatcher) ? (CharMatcher)predicate : new ForPredicate(predicate);
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
/*      */   public CharMatcher negate() {
/*  374 */     return new Negated(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher and(CharMatcher other) {
/*  381 */     return new And(this, other);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharMatcher or(CharMatcher other) {
/*  388 */     return new Or(this, other);
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
/*      */   public CharMatcher precomputed() {
/*  401 */     return Platform.precomputeCharMatcher(this);
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
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   CharMatcher precomputedInternal() {
/*  419 */     BitSet table = new BitSet();
/*  420 */     setBits(table);
/*  421 */     int totalCharacters = table.cardinality();
/*  422 */     if (totalCharacters * 2 <= 65536) {
/*  423 */       return precomputedPositive(totalCharacters, table, toString());
/*      */     }
/*      */     
/*  426 */     table.flip(0, 65536);
/*  427 */     int negatedCharacters = 65536 - totalCharacters;
/*  428 */     String suffix = ".negate()";
/*  429 */     final String description = toString();
/*      */ 
/*      */ 
/*      */     
/*  433 */     String negatedDescription = description.endsWith(suffix) ? description.substring(0, description.length() - suffix.length()) : (description + suffix);
/*  434 */     return new NegatedFastMatcher(this, 
/*  435 */         precomputedPositive(negatedCharacters, table, negatedDescription))
/*      */       {
/*      */         public String toString() {
/*  438 */           return description;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
/*      */     char c1;
/*      */     char c2;
/*  451 */     switch (totalCharacters) {
/*      */       case 0:
/*  453 */         return none();
/*      */       case 1:
/*  455 */         return is((char)table.nextSetBit(0));
/*      */       case 2:
/*  457 */         c1 = (char)table.nextSetBit(0);
/*  458 */         c2 = (char)table.nextSetBit(c1 + 1);
/*  459 */         return isEither(c1, c2);
/*      */     } 
/*  461 */     return isSmall(totalCharacters, table.length()) ? 
/*  462 */       SmallCharMatcher.from(table, description) : 
/*  463 */       new BitSetMatcher(table, description);
/*      */   }
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static boolean isSmall(int totalCharacters, int tableLength) {
/*  470 */     return (totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   void setBits(BitSet table) {
/*  479 */     for (int c = 65535; c >= 0; c--) {
/*  480 */       if (matches((char)c)) {
/*  481 */         table.set(c);
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean matchesAnyOf(CharSequence sequence) {
/*  500 */     return !matchesNoneOf(sequence);
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
/*      */   public boolean matchesAllOf(CharSequence sequence) {
/*  514 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  515 */       if (!matches(sequence.charAt(i))) {
/*  516 */         return false;
/*      */       }
/*      */     } 
/*  519 */     return true;
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
/*      */   public boolean matchesNoneOf(CharSequence sequence) {
/*  534 */     return (indexIn(sequence) == -1);
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
/*      */   public int indexIn(CharSequence sequence) {
/*  548 */     return indexIn(sequence, 0);
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
/*      */   public int indexIn(CharSequence sequence, int start) {
/*  567 */     int length = sequence.length();
/*  568 */     Preconditions.checkPositionIndex(start, length);
/*  569 */     for (int i = start; i < length; i++) {
/*  570 */       if (matches(sequence.charAt(i))) {
/*  571 */         return i;
/*      */       }
/*      */     } 
/*  574 */     return -1;
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
/*      */   public int lastIndexIn(CharSequence sequence) {
/*  588 */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*  589 */       if (matches(sequence.charAt(i))) {
/*  590 */         return i;
/*      */       }
/*      */     } 
/*  593 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int countIn(CharSequence sequence) {
/*  602 */     int count = 0;
/*  603 */     for (int i = 0; i < sequence.length(); i++) {
/*  604 */       if (matches(sequence.charAt(i))) {
/*  605 */         count++;
/*      */       }
/*      */     } 
/*  608 */     return count;
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
/*      */   public String removeFrom(CharSequence sequence) {
/*  622 */     String string = sequence.toString();
/*  623 */     int pos = indexIn(string);
/*  624 */     if (pos == -1) {
/*  625 */       return string;
/*      */     }
/*      */     
/*  628 */     char[] chars = string.toCharArray();
/*  629 */     int spread = 1;
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  634 */       pos++;
/*      */       
/*  636 */       while (pos != chars.length) {
/*      */ 
/*      */         
/*  639 */         if (matches(chars[pos]))
/*      */         
/*      */         { 
/*      */ 
/*      */ 
/*      */           
/*  645 */           spread++; continue; }  chars[pos - spread] = chars[pos]; pos++;
/*      */       }  break;
/*  647 */     }  return new String(chars, 0, pos - spread);
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
/*      */   public String retainFrom(CharSequence sequence) {
/*  661 */     return negate().removeFrom(sequence);
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
/*      */   public String replaceFrom(CharSequence sequence, char replacement) {
/*  684 */     String string = sequence.toString();
/*  685 */     int pos = indexIn(string);
/*  686 */     if (pos == -1) {
/*  687 */       return string;
/*      */     }
/*  689 */     char[] chars = string.toCharArray();
/*  690 */     chars[pos] = replacement;
/*  691 */     for (int i = pos + 1; i < chars.length; i++) {
/*  692 */       if (matches(chars[i])) {
/*  693 */         chars[i] = replacement;
/*      */       }
/*      */     } 
/*  696 */     return new String(chars);
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
/*      */   public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  718 */     int replacementLen = replacement.length();
/*  719 */     if (replacementLen == 0) {
/*  720 */       return removeFrom(sequence);
/*      */     }
/*  722 */     if (replacementLen == 1) {
/*  723 */       return replaceFrom(sequence, replacement.charAt(0));
/*      */     }
/*      */     
/*  726 */     String string = sequence.toString();
/*  727 */     int pos = indexIn(string);
/*  728 */     if (pos == -1) {
/*  729 */       return string;
/*      */     }
/*      */     
/*  732 */     int len = string.length();
/*  733 */     StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
/*      */     
/*  735 */     int oldpos = 0;
/*      */     do {
/*  737 */       buf.append(string, oldpos, pos);
/*  738 */       buf.append(replacement);
/*  739 */       oldpos = pos + 1;
/*  740 */       pos = indexIn(string, oldpos);
/*  741 */     } while (pos != -1);
/*      */     
/*  743 */     buf.append(string, oldpos, len);
/*  744 */     return buf.toString();
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
/*      */   public String trimFrom(CharSequence sequence) {
/*  766 */     int len = sequence.length();
/*      */     
/*      */     int first;
/*      */     
/*  770 */     for (first = 0; first < len && 
/*  771 */       matches(sequence.charAt(first)); first++);
/*      */     
/*      */     int last;
/*      */     
/*  775 */     for (last = len - 1; last > first && 
/*  776 */       matches(sequence.charAt(last)); last--);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  781 */     return sequence.subSequence(first, last + 1).toString();
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
/*      */   public String trimLeadingFrom(CharSequence sequence) {
/*  795 */     int len = sequence.length();
/*  796 */     for (int first = 0; first < len; first++) {
/*  797 */       if (!matches(sequence.charAt(first))) {
/*  798 */         return sequence.subSequence(first, len).toString();
/*      */       }
/*      */     } 
/*  801 */     return "";
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
/*      */   public String trimTrailingFrom(CharSequence sequence) {
/*  815 */     int len = sequence.length();
/*  816 */     for (int last = len - 1; last >= 0; last--) {
/*  817 */       if (!matches(sequence.charAt(last))) {
/*  818 */         return sequence.subSequence(0, last + 1).toString();
/*      */       }
/*      */     } 
/*  821 */     return "";
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
/*      */   public String collapseFrom(CharSequence sequence, char replacement) {
/*  845 */     int len = sequence.length();
/*  846 */     for (int i = 0; i < len; i++) {
/*  847 */       char c = sequence.charAt(i);
/*  848 */       if (matches(c)) {
/*  849 */         if (c == replacement && (i == len - 1 || !matches(sequence.charAt(i + 1)))) {
/*      */           
/*  851 */           i++;
/*      */         } else {
/*  853 */           StringBuilder builder = (new StringBuilder(len)).append(sequence, 0, i).append(replacement);
/*  854 */           return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  859 */     return sequence.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
/*  869 */     int len = sequence.length();
/*  870 */     int first = 0;
/*  871 */     int last = len - 1;
/*      */     
/*  873 */     while (first < len && matches(sequence.charAt(first))) {
/*  874 */       first++;
/*      */     }
/*      */     
/*  877 */     while (last > first && matches(sequence.charAt(last))) {
/*  878 */       last--;
/*      */     }
/*      */     
/*  881 */     return (first == 0 && last == len - 1) ? 
/*  882 */       collapseFrom(sequence, replacement) : 
/*  883 */       finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
/*  894 */     for (int i = start; i < end; i++) {
/*  895 */       char c = sequence.charAt(i);
/*  896 */       if (matches(c)) {
/*  897 */         if (!inMatchingGroup) {
/*  898 */           builder.append(replacement);
/*  899 */           inMatchingGroup = true;
/*      */         } 
/*      */       } else {
/*  902 */         builder.append(c);
/*  903 */         inMatchingGroup = false;
/*      */       } 
/*      */     } 
/*  906 */     return builder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean apply(Character character) {
/*  916 */     return matches(character.charValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  925 */     return super.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String showCharacter(char c) {
/*  933 */     String hex = "0123456789ABCDEF";
/*  934 */     char[] tmp = { '\\', 'u', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE };
/*  935 */     for (int i = 0; i < 4; i++) {
/*  936 */       tmp[5 - i] = hex.charAt(c & 0xF);
/*  937 */       c = (char)(c >> 4);
/*      */     } 
/*  939 */     return String.copyValueOf(tmp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class FastMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     public final CharMatcher precomputed() {
/*  949 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/*  954 */       return new CharMatcher.NegatedFastMatcher(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class NamedFastMatcher
/*      */     extends FastMatcher
/*      */   {
/*      */     private final String description;
/*      */     
/*      */     NamedFastMatcher(String description) {
/*  964 */       this.description = Preconditions.<String>checkNotNull(description);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String toString() {
/*  969 */       return this.description;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class NegatedFastMatcher
/*      */     extends Negated
/*      */   {
/*      */     NegatedFastMatcher(CharMatcher original) {
/*  977 */       super(original);
/*      */     }
/*      */ 
/*      */     
/*      */     public final CharMatcher precomputed() {
/*  982 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private static final class BitSetMatcher
/*      */     extends NamedFastMatcher
/*      */   {
/*      */     private final BitSet table;
/*      */     
/*      */     private BitSetMatcher(BitSet table, String description) {
/*  994 */       super(description);
/*  995 */       if (table.length() + 64 < table.size()) {
/*  996 */         table = (BitSet)table.clone();
/*      */       }
/*      */       
/*  999 */       this.table = table;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1004 */       return this.table.get(c);
/*      */     }
/*      */ 
/*      */     
/*      */     void setBits(BitSet bitSet) {
/* 1009 */       bitSet.or(this.table);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Any
/*      */     extends NamedFastMatcher
/*      */   {
/* 1018 */     static final CharMatcher INSTANCE = new Any();
/*      */     
/*      */     private Any() {
/* 1021 */       super("CharMatcher.any()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1026 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence) {
/* 1031 */       return (sequence.length() == 0) ? -1 : 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence, int start) {
/* 1036 */       int length = sequence.length();
/* 1037 */       Preconditions.checkPositionIndex(start, length);
/* 1038 */       return (start == length) ? -1 : start;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexIn(CharSequence sequence) {
/* 1043 */       return sequence.length() - 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1048 */       Preconditions.checkNotNull(sequence);
/* 1049 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1054 */       return (sequence.length() == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public String removeFrom(CharSequence sequence) {
/* 1059 */       Preconditions.checkNotNull(sequence);
/* 1060 */       return "";
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1065 */       char[] array = new char[sequence.length()];
/* 1066 */       Arrays.fill(array, replacement);
/* 1067 */       return new String(array);
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1072 */       StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
/* 1073 */       for (int i = 0; i < sequence.length(); i++) {
/* 1074 */         result.append(replacement);
/*      */       }
/* 1076 */       return result.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String collapseFrom(CharSequence sequence, char replacement) {
/* 1081 */       return (sequence.length() == 0) ? "" : String.valueOf(replacement);
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimFrom(CharSequence sequence) {
/* 1086 */       Preconditions.checkNotNull(sequence);
/* 1087 */       return "";
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1092 */       return sequence.length();
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1097 */       return Preconditions.<CharMatcher>checkNotNull(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1102 */       Preconditions.checkNotNull(other);
/* 1103 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1108 */       return none();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class None
/*      */     extends NamedFastMatcher
/*      */   {
/* 1115 */     static final CharMatcher INSTANCE = new None();
/*      */     
/*      */     private None() {
/* 1118 */       super("CharMatcher.none()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1123 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence) {
/* 1128 */       Preconditions.checkNotNull(sequence);
/* 1129 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexIn(CharSequence sequence, int start) {
/* 1134 */       int length = sequence.length();
/* 1135 */       Preconditions.checkPositionIndex(start, length);
/* 1136 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexIn(CharSequence sequence) {
/* 1141 */       Preconditions.checkNotNull(sequence);
/* 1142 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1147 */       return (sequence.length() == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1152 */       Preconditions.checkNotNull(sequence);
/* 1153 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public String removeFrom(CharSequence sequence) {
/* 1158 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1163 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/* 1168 */       Preconditions.checkNotNull(replacement);
/* 1169 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String collapseFrom(CharSequence sequence, char replacement) {
/* 1174 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimFrom(CharSequence sequence) {
/* 1179 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimLeadingFrom(CharSequence sequence) {
/* 1184 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public String trimTrailingFrom(CharSequence sequence) {
/* 1189 */       return sequence.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1194 */       Preconditions.checkNotNull(sequence);
/* 1195 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1200 */       Preconditions.checkNotNull(other);
/* 1201 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1206 */       return Preconditions.<CharMatcher>checkNotNull(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1211 */       return any();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static final class Whitespace
/*      */     extends NamedFastMatcher
/*      */   {
/*      */     static final String TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
/*      */ 
/*      */ 
/*      */     
/*      */     static final int MULTIPLIER = 1682554634;
/*      */ 
/*      */     
/* 1229 */     static final int SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
/*      */     
/* 1231 */     static final CharMatcher INSTANCE = new Whitespace();
/*      */     
/*      */     Whitespace() {
/* 1234 */       super("CharMatcher.whitespace()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1239 */       return (" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> SHIFT) == c);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1246 */       for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++) {
/* 1247 */         table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class BreakingWhitespace
/*      */     extends CharMatcher
/*      */   {
/* 1255 */     static final CharMatcher INSTANCE = new BreakingWhitespace();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1259 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\013':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */         case '':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case ' ':
/*      */         case '　':
/* 1272 */           return true;
/*      */         case ' ':
/* 1274 */           return false;
/*      */       } 
/* 1276 */       return (c >= ' ' && c <= ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1282 */       return "CharMatcher.breakingWhitespace()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Ascii
/*      */     extends NamedFastMatcher
/*      */   {
/* 1289 */     static final CharMatcher INSTANCE = new Ascii();
/*      */     
/*      */     Ascii() {
/* 1292 */       super("CharMatcher.ascii()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1297 */       return (c <= '');
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RangesMatcher
/*      */     extends CharMatcher
/*      */   {
/*      */     private final String description;
/*      */     private final char[] rangeStarts;
/*      */     private final char[] rangeEnds;
/*      */     
/*      */     RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
/* 1309 */       this.description = description;
/* 1310 */       this.rangeStarts = rangeStarts;
/* 1311 */       this.rangeEnds = rangeEnds;
/* 1312 */       Preconditions.checkArgument((rangeStarts.length == rangeEnds.length));
/* 1313 */       for (int i = 0; i < rangeStarts.length; i++) {
/* 1314 */         Preconditions.checkArgument((rangeStarts[i] <= rangeEnds[i]));
/* 1315 */         if (i + 1 < rangeStarts.length) {
/* 1316 */           Preconditions.checkArgument((rangeEnds[i] < rangeStarts[i + 1]));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1323 */       int index = Arrays.binarySearch(this.rangeStarts, c);
/* 1324 */       if (index >= 0) {
/* 1325 */         return true;
/*      */       }
/* 1327 */       index = (index ^ 0xFFFFFFFF) - 1;
/* 1328 */       return (index >= 0 && c <= this.rangeEnds[index]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1334 */       return this.description;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Digit
/*      */     extends RangesMatcher
/*      */   {
/*      */     private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static char[] zeroes() {
/* 1352 */       return "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".toCharArray();
/*      */     }
/*      */     
/*      */     private static char[] nines() {
/* 1356 */       char[] nines = new char["0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".length()];
/* 1357 */       for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".length(); i++) {
/* 1358 */         nines[i] = (char)("0٠۰߀०০੦૦୦௦౦೦൦෦๐໐༠၀႐០᠐᥆᧐᪀᪐᭐᮰᱀᱐꘠꣐꤀꧐꧰꩐꯰０".charAt(i) + 9);
/*      */       }
/* 1360 */       return nines;
/*      */     }
/*      */     
/* 1363 */     static final CharMatcher INSTANCE = new Digit();
/*      */     
/*      */     private Digit() {
/* 1366 */       super("CharMatcher.digit()", zeroes(), nines());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaDigit
/*      */     extends CharMatcher
/*      */   {
/* 1373 */     static final CharMatcher INSTANCE = new JavaDigit();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1377 */       return Character.isDigit(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1382 */       return "CharMatcher.javaDigit()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLetter
/*      */     extends CharMatcher
/*      */   {
/* 1389 */     static final CharMatcher INSTANCE = new JavaLetter();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1393 */       return Character.isLetter(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1398 */       return "CharMatcher.javaLetter()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLetterOrDigit
/*      */     extends CharMatcher
/*      */   {
/* 1405 */     static final CharMatcher INSTANCE = new JavaLetterOrDigit();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1409 */       return Character.isLetterOrDigit(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1414 */       return "CharMatcher.javaLetterOrDigit()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaUpperCase
/*      */     extends CharMatcher
/*      */   {
/* 1421 */     static final CharMatcher INSTANCE = new JavaUpperCase();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1425 */       return Character.isUpperCase(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1430 */       return "CharMatcher.javaUpperCase()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaLowerCase
/*      */     extends CharMatcher
/*      */   {
/* 1437 */     static final CharMatcher INSTANCE = new JavaLowerCase();
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1441 */       return Character.isLowerCase(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1446 */       return "CharMatcher.javaLowerCase()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class JavaIsoControl
/*      */     extends NamedFastMatcher
/*      */   {
/* 1453 */     static final CharMatcher INSTANCE = new JavaIsoControl();
/*      */     
/*      */     private JavaIsoControl() {
/* 1456 */       super("CharMatcher.javaIsoControl()");
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1461 */       return (c <= '\037' || (c >= '' && c <= ''));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Invisible
/*      */     extends RangesMatcher
/*      */   {
/*      */     private static final String RANGE_STARTS = "\000­؀؜۝܏࢐࣢ ᠎   ⁦　?﻿￹";
/*      */ 
/*      */ 
/*      */     
/*      */     private static final String RANGE_ENDS = "  ­؅؜۝܏࢑࣢ ᠎‏ ⁤⁯　﻿￻";
/*      */ 
/*      */     
/* 1478 */     static final CharMatcher INSTANCE = new Invisible();
/*      */     
/*      */     private Invisible() {
/* 1481 */       super("CharMatcher.invisible()", "\000­؀؜۝܏࢐࣢ ᠎   ⁦　?﻿￹".toCharArray(), "  ­؅؜۝܏࢑࣢ ᠎‏ ⁤⁯　﻿￻".toCharArray());
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class SingleWidth
/*      */     extends RangesMatcher
/*      */   {
/* 1488 */     static final CharMatcher INSTANCE = new SingleWidth();
/*      */     
/*      */     private SingleWidth() {
/* 1491 */       super("CharMatcher.singleWidth()", "\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡"
/*      */           
/* 1493 */           .toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ"
/* 1494 */           .toCharArray());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Negated
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher original;
/*      */ 
/*      */     
/*      */     Negated(CharMatcher original) {
/* 1506 */       this.original = Preconditions.<CharMatcher>checkNotNull(original);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1511 */       return !this.original.matches(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) {
/* 1516 */       return this.original.matchesNoneOf(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) {
/* 1521 */       return this.original.matchesAllOf(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     public int countIn(CharSequence sequence) {
/* 1526 */       return sequence.length() - this.original.countIn(sequence);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1533 */       BitSet tmp = new BitSet();
/* 1534 */       this.original.setBits(tmp);
/* 1535 */       tmp.flip(0, 65536);
/* 1536 */       table.or(tmp);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1541 */       return this.original;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1546 */       return this.original + ".negate()";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class And
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     And(CharMatcher a, CharMatcher b) {
/* 1557 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/* 1558 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1563 */       return (this.first.matches(c) && this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1570 */       BitSet tmp1 = new BitSet();
/* 1571 */       this.first.setBits(tmp1);
/* 1572 */       BitSet tmp2 = new BitSet();
/* 1573 */       this.second.setBits(tmp2);
/* 1574 */       tmp1.and(tmp2);
/* 1575 */       table.or(tmp1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1580 */       return "CharMatcher.and(" + this.first + ", " + this.second + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Or
/*      */     extends CharMatcher
/*      */   {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b) {
/* 1591 */       this.first = Preconditions.<CharMatcher>checkNotNull(a);
/* 1592 */       this.second = Preconditions.<CharMatcher>checkNotNull(b);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1599 */       this.first.setBits(table);
/* 1600 */       this.second.setBits(table);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1605 */       return (this.first.matches(c) || this.second.matches(c));
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1610 */       return "CharMatcher.or(" + this.first + ", " + this.second + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class Is
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char match;
/*      */ 
/*      */     
/*      */     Is(char match) {
/* 1622 */       this.match = match;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1627 */       return (c == this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String replaceFrom(CharSequence sequence, char replacement) {
/* 1632 */       return sequence.toString().replace(this.match, replacement);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1637 */       return other.matches(this.match) ? this : none();
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1642 */       return other.matches(this.match) ? other : super.or(other);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1647 */       return isNot(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1654 */       table.set(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1659 */       return "CharMatcher.is('" + CharMatcher.showCharacter(this.match) + "')";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class IsNot
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char match;
/*      */     
/*      */     IsNot(char match) {
/* 1669 */       this.match = match;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1674 */       return (c != this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher and(CharMatcher other) {
/* 1679 */       return other.matches(this.match) ? super.and(other) : other;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher or(CharMatcher other) {
/* 1684 */       return other.matches(this.match) ? any() : this;
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1691 */       table.set(0, this.match);
/* 1692 */       table.set(this.match + 1, 65536);
/*      */     }
/*      */ 
/*      */     
/*      */     public CharMatcher negate() {
/* 1697 */       return is(this.match);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1702 */       return "CharMatcher.isNot('" + CharMatcher.showCharacter(this.match) + "')";
/*      */     }
/*      */   }
/*      */   
/*      */   private static IsEither isEither(char c1, char c2) {
/* 1707 */     return new IsEither(c1, c2);
/*      */   }
/*      */   
/*      */   public abstract boolean matches(char paramChar);
/*      */   
/*      */   private static final class IsEither extends FastMatcher {
/*      */     private final char match1;
/*      */     private final char match2;
/*      */     
/*      */     IsEither(char match1, char match2) {
/* 1717 */       this.match1 = match1;
/* 1718 */       this.match2 = match2;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1723 */       return (c == this.match1 || c == this.match2);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1730 */       table.set(this.match1);
/* 1731 */       table.set(this.match2);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1736 */       return "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(this.match1) + CharMatcher.showCharacter(this.match2) + "\")";
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class AnyOf
/*      */     extends CharMatcher
/*      */   {
/*      */     private final char[] chars;
/*      */     
/*      */     public AnyOf(CharSequence chars) {
/* 1746 */       this.chars = chars.toString().toCharArray();
/* 1747 */       Arrays.sort(this.chars);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1752 */       return (Arrays.binarySearch(this.chars, c) >= 0);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1759 */       for (char c : this.chars) {
/* 1760 */         table.set(c);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1766 */       StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
/* 1767 */       for (char c : this.chars) {
/* 1768 */         description.append(CharMatcher.showCharacter(c));
/*      */       }
/* 1770 */       description.append("\")");
/* 1771 */       return description.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class InRange
/*      */     extends FastMatcher
/*      */   {
/*      */     private final char startInclusive;
/*      */     private final char endInclusive;
/*      */     
/*      */     InRange(char startInclusive, char endInclusive) {
/* 1782 */       Preconditions.checkArgument((endInclusive >= startInclusive));
/* 1783 */       this.startInclusive = startInclusive;
/* 1784 */       this.endInclusive = endInclusive;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1789 */       return (this.startInclusive <= c && c <= this.endInclusive);
/*      */     }
/*      */ 
/*      */     
/*      */     @J2ktIncompatible
/*      */     @GwtIncompatible
/*      */     void setBits(BitSet table) {
/* 1796 */       table.set(this.startInclusive, this.endInclusive + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1801 */       return "CharMatcher.inRange('" + CharMatcher
/* 1802 */         .showCharacter(this.startInclusive) + "', '" + CharMatcher
/*      */         
/* 1804 */         .showCharacter(this.endInclusive) + "')";
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class ForPredicate
/*      */     extends CharMatcher
/*      */   {
/*      */     private final Predicate<? super Character> predicate;
/*      */     
/*      */     ForPredicate(Predicate<? super Character> predicate) {
/* 1815 */       this.predicate = Preconditions.<Predicate<? super Character>>checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean matches(char c) {
/* 1820 */       return this.predicate.apply(Character.valueOf(c));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean apply(Character character) {
/* 1826 */       return this.predicate.apply(Preconditions.checkNotNull(character));
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1831 */       return "CharMatcher.forPredicate(" + this.predicate + ")";
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/CharMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */