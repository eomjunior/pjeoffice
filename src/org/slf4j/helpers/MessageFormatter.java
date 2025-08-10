/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   public static final FormattingTuple format(String messagePattern, Object arg) {
/* 124 */     return arrayFormat(messagePattern, new Object[] { arg });
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
/*     */   public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
/* 151 */     return arrayFormat(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
/* 155 */     Throwable throwableCandidate = getThrowableCandidate(argArray);
/* 156 */     Object[] args = argArray;
/* 157 */     if (throwableCandidate != null) {
/* 158 */       args = trimmedCopy(argArray);
/*     */     }
/* 160 */     return arrayFormat(messagePattern, args, throwableCandidate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String basicArrayFormat(String messagePattern, Object[] argArray) {
/* 170 */     FormattingTuple ft = arrayFormat(messagePattern, argArray, null);
/* 171 */     return ft.getMessage();
/*     */   }
/*     */   
/*     */   public static String basicArrayFormat(NormalizedParameters np) {
/* 175 */     return basicArrayFormat(np.getMessage(), np.getArguments());
/*     */   }
/*     */ 
/*     */   
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray, Throwable throwable) {
/* 180 */     if (messagePattern == null) {
/* 181 */       return new FormattingTuple(null, argArray, throwable);
/*     */     }
/*     */     
/* 184 */     if (argArray == null) {
/* 185 */       return new FormattingTuple(messagePattern);
/*     */     }
/*     */     
/* 188 */     int i = 0;
/*     */ 
/*     */     
/* 191 */     StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
/*     */ 
/*     */     
/* 194 */     for (int L = 0; L < argArray.length; L++) {
/*     */       
/* 196 */       int j = messagePattern.indexOf("{}", i);
/*     */       
/* 198 */       if (j == -1) {
/*     */         
/* 200 */         if (i == 0) {
/* 201 */           return new FormattingTuple(messagePattern, argArray, throwable);
/*     */         }
/*     */         
/* 204 */         sbuf.append(messagePattern, i, messagePattern.length());
/* 205 */         return new FormattingTuple(sbuf.toString(), argArray, throwable);
/*     */       } 
/*     */       
/* 208 */       if (isEscapedDelimeter(messagePattern, j)) {
/* 209 */         if (!isDoubleEscaped(messagePattern, j)) {
/* 210 */           L--;
/* 211 */           sbuf.append(messagePattern, i, j - 1);
/* 212 */           sbuf.append('{');
/* 213 */           i = j + 1;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 218 */           sbuf.append(messagePattern, i, j - 1);
/* 219 */           deeplyAppendParameter(sbuf, argArray[L], (Map)new HashMap<>());
/* 220 */           i = j + 2;
/*     */         } 
/*     */       } else {
/*     */         
/* 224 */         sbuf.append(messagePattern, i, j);
/* 225 */         deeplyAppendParameter(sbuf, argArray[L], (Map)new HashMap<>());
/* 226 */         i = j + 2;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 231 */     sbuf.append(messagePattern, i, messagePattern.length());
/* 232 */     return new FormattingTuple(sbuf.toString(), argArray, throwable);
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
/* 237 */     if (delimeterStartIndex == 0) {
/* 238 */       return false;
/*     */     }
/* 240 */     char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
/* 241 */     if (potentialEscape == '\\') {
/* 242 */       return true;
/*     */     }
/* 244 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
/* 249 */     if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\') {
/* 250 */       return true;
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Map<Object[], Object> seenMap) {
/* 258 */     if (o == null) {
/* 259 */       sbuf.append("null");
/*     */       return;
/*     */     } 
/* 262 */     if (!o.getClass().isArray()) {
/* 263 */       safeObjectAppend(sbuf, o);
/*     */ 
/*     */     
/*     */     }
/* 267 */     else if (o instanceof boolean[]) {
/* 268 */       booleanArrayAppend(sbuf, (boolean[])o);
/* 269 */     } else if (o instanceof byte[]) {
/* 270 */       byteArrayAppend(sbuf, (byte[])o);
/* 271 */     } else if (o instanceof char[]) {
/* 272 */       charArrayAppend(sbuf, (char[])o);
/* 273 */     } else if (o instanceof short[]) {
/* 274 */       shortArrayAppend(sbuf, (short[])o);
/* 275 */     } else if (o instanceof int[]) {
/* 276 */       intArrayAppend(sbuf, (int[])o);
/* 277 */     } else if (o instanceof long[]) {
/* 278 */       longArrayAppend(sbuf, (long[])o);
/* 279 */     } else if (o instanceof float[]) {
/* 280 */       floatArrayAppend(sbuf, (float[])o);
/* 281 */     } else if (o instanceof double[]) {
/* 282 */       doubleArrayAppend(sbuf, (double[])o);
/*     */     } else {
/* 284 */       objectArrayAppend(sbuf, (Object[])o, seenMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void safeObjectAppend(StringBuilder sbuf, Object o) {
/*     */     try {
/* 291 */       String oAsString = o.toString();
/* 292 */       sbuf.append(oAsString);
/* 293 */     } catch (Throwable t) {
/* 294 */       Reporter.error("Failed toString() invocation on an object of type [" + o.getClass().getName() + "]", t);
/* 295 */       sbuf.append("[FAILED toString()]");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Map<Object[], Object> seenMap) {
/* 301 */     sbuf.append('[');
/* 302 */     if (!seenMap.containsKey(a)) {
/* 303 */       seenMap.put(a, null);
/* 304 */       int len = a.length;
/* 305 */       for (int i = 0; i < len; i++) {
/* 306 */         deeplyAppendParameter(sbuf, a[i], seenMap);
/* 307 */         if (i != len - 1) {
/* 308 */           sbuf.append(", ");
/*     */         }
/*     */       } 
/* 311 */       seenMap.remove(a);
/*     */     } else {
/* 313 */       sbuf.append("...");
/*     */     } 
/* 315 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
/* 319 */     sbuf.append('[');
/* 320 */     int len = a.length;
/* 321 */     for (int i = 0; i < len; i++) {
/* 322 */       sbuf.append(a[i]);
/* 323 */       if (i != len - 1)
/* 324 */         sbuf.append(", "); 
/*     */     } 
/* 326 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
/* 330 */     sbuf.append('[');
/* 331 */     int len = a.length;
/* 332 */     for (int i = 0; i < len; i++) {
/* 333 */       sbuf.append(a[i]);
/* 334 */       if (i != len - 1)
/* 335 */         sbuf.append(", "); 
/*     */     } 
/* 337 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuilder sbuf, char[] a) {
/* 341 */     sbuf.append('[');
/* 342 */     int len = a.length;
/* 343 */     for (int i = 0; i < len; i++) {
/* 344 */       sbuf.append(a[i]);
/* 345 */       if (i != len - 1)
/* 346 */         sbuf.append(", "); 
/*     */     } 
/* 348 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
/* 352 */     sbuf.append('[');
/* 353 */     int len = a.length;
/* 354 */     for (int i = 0; i < len; i++) {
/* 355 */       sbuf.append(a[i]);
/* 356 */       if (i != len - 1)
/* 357 */         sbuf.append(", "); 
/*     */     } 
/* 359 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuilder sbuf, int[] a) {
/* 363 */     sbuf.append('[');
/* 364 */     int len = a.length;
/* 365 */     for (int i = 0; i < len; i++) {
/* 366 */       sbuf.append(a[i]);
/* 367 */       if (i != len - 1)
/* 368 */         sbuf.append(", "); 
/*     */     } 
/* 370 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuilder sbuf, long[] a) {
/* 374 */     sbuf.append('[');
/* 375 */     int len = a.length;
/* 376 */     for (int i = 0; i < len; i++) {
/* 377 */       sbuf.append(a[i]);
/* 378 */       if (i != len - 1)
/* 379 */         sbuf.append(", "); 
/*     */     } 
/* 381 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
/* 385 */     sbuf.append('[');
/* 386 */     int len = a.length;
/* 387 */     for (int i = 0; i < len; i++) {
/* 388 */       sbuf.append(a[i]);
/* 389 */       if (i != len - 1)
/* 390 */         sbuf.append(", "); 
/*     */     } 
/* 392 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
/* 396 */     sbuf.append('[');
/* 397 */     int len = a.length;
/* 398 */     for (int i = 0; i < len; i++) {
/* 399 */       sbuf.append(a[i]);
/* 400 */       if (i != len - 1)
/* 401 */         sbuf.append(", "); 
/*     */     } 
/* 403 */     sbuf.append(']');
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
/*     */   public static Throwable getThrowableCandidate(Object[] argArray) {
/* 415 */     return NormalizedParameters.getThrowableCandidate(argArray);
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
/*     */   public static Object[] trimmedCopy(Object[] argArray) {
/* 427 */     return NormalizedParameters.trimmedCopy(argArray);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/slf4j/helpers/MessageFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */