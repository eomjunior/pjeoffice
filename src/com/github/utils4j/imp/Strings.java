/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
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
/*     */ public final class Strings
/*     */ {
/*     */   private static final char SPACE = ' ';
/*     */   private static final String EMPTY = "";
/*     */   private static final String HTML_SPACE = "%20";
/*     */   private static final String SPACE_STRING = " ";
/*  54 */   private static final String[] EMPTY_ARRAY = new String[0];
/*     */   
/*  56 */   private static final String[] HTML_SPACE_ARRAY = new String[] { "%20" };
/*     */   
/*  58 */   private static final String[] SPACE_STRING_ARRAY = new String[] { " " };
/*     */   
/*  60 */   private static final Optional<String> TRUE = Optional.of("true");
/*     */   
/*  62 */   private static final Optional<String> FALSE = Optional.of("false");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Optional<T> get(IProvider<T> p, T defaultIfFail) {
/*     */     try {
/*  69 */       return Optional.ofNullable((T)p.get());
/*  70 */     } catch (Exception e) {
/*  71 */       return Optional.ofNullable(defaultIfFail);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String truncate(String s, int length) {
/*  76 */     return (s == null) ? "" : (s = trim(s)).substring(0, Math.min(length, s.length()));
/*     */   }
/*     */   
/*     */   public static Optional<String> optional(String text) {
/*  80 */     return Optional.ofNullable(textOrNull(text));
/*     */   }
/*     */   
/*     */   public static Optional<String> optional(String text, String defaultIfEmpty) {
/*  84 */     return Optional.ofNullable(optional(text).orElse(defaultIfEmpty));
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(String text) {
/*  88 */     return (text == null || text.isEmpty());
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(String[] text) {
/*  92 */     return (text == null || text.length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isNotEmpty(String[] text) {
/*  96 */     return !isEmpty(text);
/*     */   }
/*     */   
/*     */   public static String[] emptyArray() {
/* 100 */     return EMPTY_ARRAY;
/*     */   }
/*     */   
/*     */   public static String empty() {
/* 104 */     return "";
/*     */   }
/*     */   
/*     */   public static String space() {
/* 108 */     return " ";
/*     */   }
/*     */   
/*     */   public static Optional<String> trueOptional() {
/* 112 */     return TRUE;
/*     */   }
/*     */   
/*     */   public static Optional<String> falseOptional() {
/* 116 */     return FALSE;
/*     */   }
/*     */   
/*     */   public static Iterator<String> emptyIterator() {
/* 120 */     return Collections.emptyIterator();
/*     */   }
/*     */   
/*     */   public static String text(String text) {
/* 124 */     return text(text, "");
/*     */   }
/*     */   
/*     */   public static String text(Object o) {
/* 128 */     return (o == null) ? "" : text(o.toString());
/*     */   }
/*     */   
/*     */   public static String textOrNull(String text) {
/* 132 */     return needText(text, null);
/*     */   }
/*     */   
/*     */   public static String textOrNull(Object t) {
/* 136 */     return (t == null) ? null : textOrNull(t.toString());
/*     */   }
/*     */   
/*     */   public static String needText(String text, String defaultIfNull) {
/* 140 */     return !hasText(text) ? defaultIfNull : trim(text);
/*     */   }
/*     */   
/*     */   public static String text(String text, String defaultIfNull) {
/* 144 */     return (text == null) ? defaultIfNull : text;
/*     */   }
/*     */   
/*     */   public static String trim(String text) {
/* 148 */     return trim(text, "");
/*     */   }
/*     */   
/*     */   public static String trim(String text, String defaultIfNull) {
/* 152 */     return (text == null) ? defaultIfNull : text.trim();
/*     */   }
/*     */   
/*     */   public static String trim(Object o) {
/* 156 */     return (o == null) ? "" : trim(o.toString());
/*     */   }
/*     */   
/*     */   public static int length(String text) {
/* 160 */     return (text == null) ? 0 : text.length();
/*     */   }
/*     */   
/*     */   public static boolean isInt(String value) {
/*     */     try {
/* 165 */       Integer.parseInt(value.trim());
/* 166 */       return true;
/* 167 */     } catch (Throwable e) {
/* 168 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isLong(String value) {
/*     */     try {
/* 174 */       Long.parseLong(value);
/* 175 */       return true;
/* 176 */     } catch (Throwable e) {
/* 177 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isBoolean(String value) {
/*     */     try {
/* 183 */       Boolean.parseBoolean(value);
/* 184 */       return true;
/* 185 */     } catch (Throwable e) {
/* 186 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isFloat(String value) {
/*     */     try {
/* 192 */       Float.parseFloat(value);
/* 193 */       return true;
/* 194 */     } catch (Throwable e) {
/* 195 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isDouble(String value) {
/*     */     try {
/* 201 */       Double.parseDouble(value);
/* 202 */       return true;
/* 203 */     } catch (Throwable e) {
/* 204 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int toInt(String value, int defaultValue) {
/*     */     try {
/* 210 */       return Integer.parseInt(value);
/* 211 */     } catch (Exception e) {
/* 212 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static long toLong(String value, long defaultValue) {
/*     */     try {
/* 218 */       return Long.parseLong(value);
/* 219 */     } catch (Exception e) {
/* 220 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean toBoolean(String value, boolean defaultValue) {
/*     */     try {
/* 226 */       return Boolean.parseBoolean(value);
/* 227 */     } catch (Exception e) {
/* 228 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static float toFloat(String value, float defaultValue) {
/*     */     try {
/* 234 */       return Float.parseFloat(value);
/* 235 */     } catch (Exception e) {
/* 236 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static double toDouble(String value, double defaultValue) {
/*     */     try {
/* 242 */       return Double.parseDouble(value);
/* 243 */     } catch (Exception e) {
/* 244 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<String> mix(Iterable<Iterable<String>> text, char separator) {
/* 249 */     List<String> r = new ArrayList<>();
/* 250 */     for (Iterable<String> it : text) {
/* 251 */       r.add(merge(it, separator));
/*     */     }
/* 253 */     return r;
/*     */   }
/*     */   
/*     */   public static String merge(Iterable<String> it, char separator) {
/* 257 */     StringBuilder member = new StringBuilder();
/* 258 */     for (String m : it) {
/* 259 */       if (member.length() > 0)
/* 260 */         member.append(separator); 
/* 261 */       member.append(m);
/*     */     } 
/* 263 */     return member.toString();
/*     */   }
/*     */   
/*     */   public static String replace(String value, char find, char replace) {
/* 267 */     StringBuilder s = new StringBuilder(value.length());
/* 268 */     for (int i = 0; i < value.length(); i++) {
/* 269 */       char chr = value.charAt(i);
/* 270 */       s.append((chr == find) ? replace : chr);
/*     */     } 
/* 272 */     return s.toString();
/*     */   }
/*     */   
/*     */   public static String quotes(String value) {
/* 276 */     return (value == null) ? empty() : ("\"" + value + "\"");
/*     */   }
/*     */   
/*     */   public static String quotes(String separator, String... value) {
/* 280 */     if (value == null)
/* 281 */       return empty(); 
/* 282 */     StringBuilder b = new StringBuilder();
/* 283 */     Arrays.<String>stream(value).map(Strings::quotes).forEach(p -> b.append(p).append(separator));
/* 284 */     return b.toString().trim();
/*     */   }
/*     */   
/*     */   public static StringBuilder computeTabs(int tabSize) {
/* 288 */     StringBuilder b = new StringBuilder(6);
/* 289 */     while (tabSize-- > 0)
/* 290 */       b.append("  "); 
/* 291 */     return b;
/*     */   }
/*     */   
/*     */   public static boolean hasText(String text) {
/* 295 */     if (text == null)
/* 296 */       return false; 
/* 297 */     int size = text.length();
/* 298 */     for (int i = 0; i < size; i++) {
/* 299 */       switch (text.charAt(i)) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\r':
/*     */         case ' ':
/*     */           break;
/*     */         default:
/* 306 */           return true;
/*     */       } 
/* 308 */     }  return false;
/*     */   }
/*     */   
/*     */   public static List<String> split(String text, char separator) {
/* 312 */     List<String> r = new ArrayList<>();
/* 313 */     int begin = 0, end = begin + 1;
/* 314 */     text = text(text);
/* 315 */     while (end <= text.length()) {
/* 316 */       if (text.charAt(end - 1) == separator) {
/* 317 */         if (end - begin > 1) {
/* 318 */           String member = text.substring(begin, end - 1);
/* 319 */           if (hasText(member))
/* 320 */             r.add(member.trim()); 
/*     */         } 
/* 322 */         begin = end++; continue;
/*     */       } 
/* 324 */       end++;
/*     */     } 
/* 326 */     if (begin < text.length()) {
/* 327 */       String member = text.substring(begin, text.length());
/* 328 */       if (hasText(member))
/* 329 */         r.add(member.trim()); 
/*     */     } 
/* 331 */     return r;
/*     */   }
/*     */   
/*     */   public static String padStart(long in, int minLength) {
/* 335 */     return padStart(Long.toString(in), minLength, '0');
/*     */   }
/*     */   
/*     */   public static String padStart(String input, int minLength, char padChar) {
/*     */     int length;
/* 340 */     if ((length = input.length()) >= minLength) {
/* 341 */       return input;
/*     */     }
/* 343 */     StringBuilder sb = new StringBuilder(minLength);
/* 344 */     for (int i = length; i < minLength; i++) {
/* 345 */       sb.append(padChar);
/*     */     }
/* 347 */     sb.append(input);
/* 348 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String padEnd(String input, int minLength, char padChar) {
/*     */     int length;
/* 353 */     if ((length = input.length()) >= minLength) {
/* 354 */       return input;
/*     */     }
/* 356 */     StringBuilder sb = new StringBuilder(minLength);
/* 357 */     sb.append(input);
/* 358 */     for (int i = length; i < minLength; i++) {
/* 359 */       sb.append(padChar);
/*     */     }
/* 361 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static Set<String> stringSet(Collection<?> container) {
/* 365 */     Set<String> out = new HashSet<>(container.size());
/* 366 */     container.forEach(o -> out.add(o.toString()));
/* 367 */     return out;
/*     */   }
/*     */   
/*     */   public static List<String> stringList(Collection<?> container) {
/* 371 */     List<String> out = new ArrayList<>(container.size());
/* 372 */     container.forEach(o -> out.add(o.toString()));
/* 373 */     return out;
/*     */   }
/*     */   
/*     */   public static String firstCase(String input) {
/* 377 */     input = trim(input);
/* 378 */     StringBuilder out = new StringBuilder();
/* 379 */     boolean cs = true;
/* 380 */     for (int i = 0; i < input.length(); i++) {
/* 381 */       char chr = input.charAt(i);
/* 382 */       out.append(cs ? Character.toUpperCase(chr) : Character.toLowerCase(chr));
/* 383 */       cs = (chr == ' ');
/*     */     } 
/* 385 */     return out.toString();
/*     */   }
/*     */   
/*     */   public static String firstChars(String input) {
/* 389 */     input = trim(input);
/* 390 */     StringBuilder out = new StringBuilder();
/* 391 */     boolean cs = true;
/* 392 */     for (int i = 0; i < input.length(); i++) {
/* 393 */       char chr = input.charAt(i);
/* 394 */       if (cs) {
/* 395 */         out.append(chr);
/* 396 */         cs = false;
/*     */       } 
/* 398 */       cs = (chr == ' ');
/*     */     } 
/* 400 */     return out.toString();
/*     */   }
/*     */   
/*     */   public static String[] toArray(String... string) {
/* 404 */     return string;
/*     */   }
/*     */   
/*     */   public static String toString(String[] value, char sep) {
/* 408 */     if (Containers.isEmpty(value))
/* 409 */       return ""; 
/* 410 */     StringBuilder b = new StringBuilder(value.length * 5);
/* 411 */     for (int i = 0; i < value.length; i++) {
/* 412 */       if (b.length() > 0)
/* 413 */         b.append(sep); 
/* 414 */       b.append(value[i]);
/*     */     } 
/* 416 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static boolean isTrue(Object value) {
/* 420 */     return isTrue(Objects.toString(value, "false"));
/*     */   }
/*     */   
/*     */   public static boolean isTrue(String value) {
/* 424 */     value = trim(value).toLowerCase();
/* 425 */     if (!hasText(value))
/* 426 */       return false; 
/* 427 */     switch (value) {
/*     */       case "1":
/*     */       case "yes":
/*     */       case "on":
/*     */       case "sim":
/*     */       case "true":
/*     */       case "ok":
/* 434 */         return true;
/*     */     } 
/* 436 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isFalse(String devmode) {
/* 441 */     devmode = trim(devmode).toLowerCase();
/* 442 */     if (!hasText(devmode))
/* 443 */       return false; 
/* 444 */     switch (devmode) {
/*     */       case "0":
/*     */       case "no":
/*     */       case "off":
/*     */       case "não":
/*     */       case "nao":
/*     */       case "false":
/* 451 */         return true;
/*     */     } 
/* 453 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String separate(Set<?> set, char separator, boolean quotes) {
/* 458 */     if (Containers.isEmpty(set))
/* 459 */       return ""; 
/* 460 */     StringBuilder b = new StringBuilder();
/* 461 */     set.forEach(s -> {
/*     */           String str = s.toString(); if (hasText(str)) {
/*     */             if (b.length() > 0)
/*     */               b.append(separator); 
/*     */             if (quotes) {
/*     */               b.append('\'').append(trim(str)).append('\'');
/*     */             } else {
/*     */               b.append(trim(str));
/*     */             } 
/*     */           } 
/*     */         });
/* 472 */     return b.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
/* 477 */     if (timeToLive < 0) {
/* 478 */       Set<String> searchSet = new HashSet<>(Arrays.asList(searchList));
/* 479 */       Set<String> replacementSet = new HashSet<>(Arrays.asList(replacementList));
/* 480 */       searchSet.retainAll(replacementSet);
/* 481 */       if (searchSet.size() > 0) {
/* 482 */         throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
/*     */       }
/*     */     } 
/*     */     
/* 486 */     if (isEmpty(text) || isEmpty(searchList) || isEmpty(replacementList) || (isNotEmpty(searchList) && timeToLive == -1)) {
/* 487 */       return text;
/*     */     }
/*     */     
/* 490 */     int searchLength = searchList.length;
/* 491 */     int replacementLength = replacementList.length;
/*     */     
/* 493 */     if (searchLength != replacementLength) {
/* 494 */       throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 500 */     boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
/*     */     
/* 502 */     int textIndex = -1;
/* 503 */     int replaceIndex = -1;
/* 504 */     int tempIndex = -1;
/*     */     
/* 506 */     for (int i = 0; i < searchLength; i++) {
/* 507 */       if (!noMoreMatchesForReplIndex[i] && !isEmpty(searchList[i]) && replacementList[i] != null) {
/*     */ 
/*     */         
/* 510 */         tempIndex = text.indexOf(searchList[i]);
/* 511 */         if (tempIndex == -1) {
/* 512 */           noMoreMatchesForReplIndex[i] = true;
/*     */         }
/* 514 */         else if (textIndex == -1 || tempIndex < textIndex) {
/* 515 */           textIndex = tempIndex;
/* 516 */           replaceIndex = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 521 */     if (textIndex == -1) {
/* 522 */       return text;
/*     */     }
/*     */     
/* 525 */     int start = 0;
/* 526 */     int increase = 0;
/*     */     
/* 528 */     for (int j = 0; j < searchList.length; j++) {
/* 529 */       if (searchList[j] != null && replacementList[j] != null) {
/*     */ 
/*     */         
/* 532 */         int greater = replacementList[j].length() - searchList[j].length();
/* 533 */         if (greater > 0)
/* 534 */           increase += 3 * greater; 
/*     */       } 
/*     */     } 
/* 537 */     increase = Math.min(increase, text.length() / 5);
/*     */     
/* 539 */     StringBuilder buf = new StringBuilder(text.length() + increase);
/*     */     
/* 541 */     while (textIndex != -1) {
/*     */       int m;
/* 543 */       for (m = start; m < textIndex; m++) {
/* 544 */         buf.append(text.charAt(m));
/*     */       }
/* 546 */       buf.append(replacementList[replaceIndex]);
/*     */       
/* 548 */       start = textIndex + searchList[replaceIndex].length();
/*     */       
/* 550 */       textIndex = -1;
/* 551 */       replaceIndex = -1;
/* 552 */       tempIndex = -1;
/* 553 */       for (m = 0; m < searchLength; m++) {
/* 554 */         if (!noMoreMatchesForReplIndex[m] && searchList[m] != null && 
/* 555 */           !searchList[m].isEmpty() && replacementList[m] != null) {
/*     */ 
/*     */           
/* 558 */           tempIndex = text.indexOf(searchList[m], start);
/* 559 */           if (tempIndex == -1) {
/* 560 */             noMoreMatchesForReplIndex[m] = true;
/*     */           }
/* 562 */           else if (textIndex == -1 || tempIndex < textIndex) {
/* 563 */             textIndex = tempIndex;
/* 564 */             replaceIndex = m;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 569 */     int textLength = text.length();
/* 570 */     for (int k = start; k < textLength; k++) {
/* 571 */       buf.append(text.charAt(k));
/*     */     }
/* 573 */     String result = buf.toString();
/* 574 */     if (!repeat) {
/* 575 */       return result;
/*     */     }
/*     */     
/* 578 */     return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
/*     */   }
/*     */   
/*     */   public static String replaceEach(String text, String[] searchList, String[] replacementList) {
/* 582 */     int timeToLive = (searchList == null) ? 0 : searchList.length;
/* 583 */     return replaceEach(text, searchList, replacementList, true, timeToLive);
/*     */   }
/*     */   
/*     */   public static String encodeHtmlSpace(String url) {
/* 587 */     return replaceEach(url, SPACE_STRING_ARRAY, HTML_SPACE_ARRAY);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */