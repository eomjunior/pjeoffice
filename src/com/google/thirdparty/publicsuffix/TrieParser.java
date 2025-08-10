/*     */ package com.google.thirdparty.publicsuffix;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Deque;
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
/*     */ @GwtCompatible
/*     */ final class TrieParser
/*     */ {
/*  29 */   private static final Joiner DIRECT_JOINER = Joiner.on("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence... encodedChunks) {
/*  37 */     String encoded = DIRECT_JOINER.join((Object[])encodedChunks);
/*  38 */     return parseFullString(encoded);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableMap<String, PublicSuffixType> parseFullString(String encoded) {
/*  43 */     ImmutableMap.Builder<String, PublicSuffixType> builder = ImmutableMap.builder();
/*  44 */     int encodedLen = encoded.length();
/*  45 */     int idx = 0;
/*     */     
/*  47 */     while (idx < encodedLen) {
/*  48 */       idx += doParseTrieToBuilder(Queues.newArrayDeque(), encoded, idx, builder);
/*     */     }
/*     */     
/*  51 */     return builder.buildOrThrow();
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
/*     */   private static int doParseTrieToBuilder(Deque<CharSequence> stack, CharSequence encoded, int start, ImmutableMap.Builder<String, PublicSuffixType> builder) {
/*  70 */     int encodedLen = encoded.length();
/*  71 */     int idx = start;
/*  72 */     char c = Character.MIN_VALUE;
/*     */ 
/*     */     
/*  75 */     for (; idx < encodedLen; idx++) {
/*  76 */       c = encoded.charAt(idx);
/*     */       
/*  78 */       if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  83 */     stack.push(reverse(encoded.subSequence(start, idx)));
/*     */     
/*  85 */     if (c == '!' || c == '?' || c == ':' || c == ',') {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       String domain = DIRECT_JOINER.join(stack);
/*     */       
/*  92 */       if (domain.length() > 0) {
/*  93 */         builder.put(domain, PublicSuffixType.fromCode(c));
/*     */       }
/*     */     } 
/*     */     
/*  97 */     idx++;
/*     */     
/*  99 */     if (c != '?' && c != ',') {
/* 100 */       while (idx < encodedLen) {
/*     */         
/* 102 */         idx += doParseTrieToBuilder(stack, encoded, idx, builder);
/*     */         
/* 104 */         if (encoded.charAt(idx) == '?' || encoded.charAt(idx) == ',') {
/*     */           
/* 106 */           idx++;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 112 */     stack.pop();
/* 113 */     return idx - start;
/*     */   }
/*     */   
/*     */   private static CharSequence reverse(CharSequence s) {
/* 117 */     return (new StringBuilder(s)).reverse();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/thirdparty/publicsuffix/TrieParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */