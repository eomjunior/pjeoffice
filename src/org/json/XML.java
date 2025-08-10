/*     */ package org.json;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XML
/*     */ {
/*  25 */   public static final Character AMP = Character.valueOf('&');
/*     */ 
/*     */   
/*  28 */   public static final Character APOS = Character.valueOf('\'');
/*     */ 
/*     */   
/*  31 */   public static final Character BANG = Character.valueOf('!');
/*     */ 
/*     */   
/*  34 */   public static final Character EQ = Character.valueOf('=');
/*     */ 
/*     */   
/*  37 */   public static final Character GT = Character.valueOf('>');
/*     */ 
/*     */   
/*  40 */   public static final Character LT = Character.valueOf('<');
/*     */ 
/*     */   
/*  43 */   public static final Character QUEST = Character.valueOf('?');
/*     */ 
/*     */   
/*  46 */   public static final Character QUOT = Character.valueOf('"');
/*     */ 
/*     */   
/*  49 */   public static final Character SLASH = Character.valueOf('/');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String NULL_ATTR = "xsi:nil";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TYPE_ATTR = "xsi:type";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Iterable<Integer> codePointIterator(final String string) {
/*  70 */     return new Iterable<Integer>()
/*     */       {
/*     */         public Iterator<Integer> iterator() {
/*  73 */           return new Iterator<Integer>() {
/*  74 */               private int nextIndex = 0;
/*  75 */               private int length = string.length();
/*     */ 
/*     */               
/*     */               public boolean hasNext() {
/*  79 */                 return (this.nextIndex < this.length);
/*     */               }
/*     */ 
/*     */               
/*     */               public Integer next() {
/*  84 */                 int result = string.codePointAt(this.nextIndex);
/*  85 */                 this.nextIndex += Character.charCount(result);
/*  86 */                 return Integer.valueOf(result);
/*     */               }
/*     */ 
/*     */               
/*     */               public void remove() {
/*  91 */                 throw new UnsupportedOperationException();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
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
/*     */   public static String escape(String string) {
/* 114 */     StringBuilder sb = new StringBuilder(string.length());
/* 115 */     for (Iterator<Integer> iterator = codePointIterator(string).iterator(); iterator.hasNext(); ) { int cp = ((Integer)iterator.next()).intValue();
/* 116 */       switch (cp) {
/*     */         case 38:
/* 118 */           sb.append("&amp;");
/*     */           continue;
/*     */         case 60:
/* 121 */           sb.append("&lt;");
/*     */           continue;
/*     */         case 62:
/* 124 */           sb.append("&gt;");
/*     */           continue;
/*     */         case 34:
/* 127 */           sb.append("&quot;");
/*     */           continue;
/*     */         case 39:
/* 130 */           sb.append("&apos;");
/*     */           continue;
/*     */       } 
/* 133 */       if (mustEscape(cp)) {
/* 134 */         sb.append("&#x");
/* 135 */         sb.append(Integer.toHexString(cp));
/* 136 */         sb.append(';'); continue;
/*     */       } 
/* 138 */       sb.appendCodePoint(cp); }
/*     */ 
/*     */ 
/*     */     
/* 142 */     return sb.toString();
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
/*     */   private static boolean mustEscape(int cp) {
/* 158 */     return ((Character.isISOControl(cp) && cp != 9 && cp != 10 && cp != 13) || ((cp < 32 || cp > 55295) && (cp < 57344 || cp > 65533) && (cp < 65536 || cp > 1114111)));
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
/*     */   public static String unescape(String string) {
/* 179 */     StringBuilder sb = new StringBuilder(string.length());
/* 180 */     for (int i = 0, length = string.length(); i < length; i++) {
/* 181 */       char c = string.charAt(i);
/* 182 */       if (c == '&') {
/* 183 */         int semic = string.indexOf(';', i);
/* 184 */         if (semic > i) {
/* 185 */           String entity = string.substring(i + 1, semic);
/* 186 */           sb.append(XMLTokener.unescapeEntity(entity));
/*     */           
/* 188 */           i += entity.length() + 1;
/*     */         }
/*     */         else {
/*     */           
/* 192 */           sb.append(c);
/*     */         } 
/*     */       } else {
/*     */         
/* 196 */         sb.append(c);
/*     */       } 
/*     */     } 
/* 199 */     return sb.toString();
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
/*     */   public static void noSpace(String string) throws JSONException {
/* 211 */     int length = string.length();
/* 212 */     if (length == 0) {
/* 213 */       throw new JSONException("Empty string.");
/*     */     }
/* 215 */     for (int i = 0; i < length; i++) {
/* 216 */       if (Character.isWhitespace(string.charAt(i))) {
/* 217 */         throw new JSONException("'" + string + "' contains a space character.");
/*     */       }
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
/*     */ 
/*     */   
/*     */   private static boolean parse(XMLTokener x, JSONObject context, String name, XMLParserConfiguration config, int currentNestingDepth) throws JSONException {
/* 243 */     JSONObject jsonObject = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     Object token = x.nextToken();
/*     */ 
/*     */ 
/*     */     
/* 263 */     if (token == BANG) {
/* 264 */       char c = x.next();
/* 265 */       if (c == '-') {
/* 266 */         if (x.next() == '-') {
/* 267 */           x.skipPast("-->");
/* 268 */           return false;
/*     */         } 
/* 270 */         x.back();
/* 271 */       } else if (c == '[') {
/* 272 */         token = x.nextToken();
/* 273 */         if ("CDATA".equals(token) && 
/* 274 */           x.next() == '[') {
/* 275 */           String string = x.nextCDATA();
/* 276 */           if (string.length() > 0) {
/* 277 */             context.accumulate(config.getcDataTagName(), string);
/*     */           }
/* 279 */           return false;
/*     */         } 
/*     */         
/* 282 */         throw x.syntaxError("Expected 'CDATA['");
/*     */       } 
/* 284 */       int i = 1;
/*     */       while (true)
/* 286 */       { token = x.nextMeta();
/* 287 */         if (token == null)
/* 288 */           throw x.syntaxError("Missing '>' after '<!'."); 
/* 289 */         if (token == LT) {
/* 290 */           i++;
/* 291 */         } else if (token == GT) {
/* 292 */           i--;
/*     */         } 
/* 294 */         if (i <= 0)
/* 295 */           return false;  } 
/* 296 */     }  if (token == QUEST) {
/*     */ 
/*     */       
/* 299 */       x.skipPast("?>");
/* 300 */       return false;
/* 301 */     }  if (token == SLASH) {
/*     */ 
/*     */ 
/*     */       
/* 305 */       token = x.nextToken();
/* 306 */       if (name == null) {
/* 307 */         throw x.syntaxError("Mismatched close tag " + token);
/*     */       }
/* 309 */       if (!token.equals(name)) {
/* 310 */         throw x.syntaxError("Mismatched " + name + " and " + token);
/*     */       }
/* 312 */       if (x.nextToken() != GT) {
/* 313 */         throw x.syntaxError("Misshaped close tag");
/*     */       }
/* 315 */       return true;
/*     */     } 
/* 317 */     if (token instanceof Character) {
/* 318 */       throw x.syntaxError("Misshaped tag");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 323 */     String tagName = (String)token;
/* 324 */     token = null;
/* 325 */     jsonObject = new JSONObject();
/* 326 */     boolean nilAttributeFound = false;
/* 327 */     XMLXsiTypeConverter<?> xmlXsiTypeConverter = null;
/*     */     while (true) {
/* 329 */       if (token == null) {
/* 330 */         token = x.nextToken();
/*     */       }
/*     */       
/* 333 */       if (token instanceof String) {
/* 334 */         String string = (String)token;
/* 335 */         token = x.nextToken();
/* 336 */         if (token == EQ) {
/* 337 */           token = x.nextToken();
/* 338 */           if (!(token instanceof String)) {
/* 339 */             throw x.syntaxError("Missing value");
/*     */           }
/*     */           
/* 342 */           if (config.isConvertNilAttributeToNull() && "xsi:nil"
/* 343 */             .equals(string) && 
/* 344 */             Boolean.parseBoolean((String)token)) {
/* 345 */             nilAttributeFound = true;
/* 346 */           } else if (config.getXsiTypeMap() != null && !config.getXsiTypeMap().isEmpty() && "xsi:type"
/* 347 */             .equals(string)) {
/* 348 */             xmlXsiTypeConverter = config.getXsiTypeMap().get(token);
/* 349 */           } else if (!nilAttributeFound) {
/* 350 */             jsonObject.accumulate(string, 
/* 351 */                 config.isKeepStrings() ? token : 
/*     */                 
/* 353 */                 stringToValue((String)token));
/*     */           } 
/* 355 */           token = null; continue;
/*     */         } 
/* 357 */         jsonObject.accumulate(string, ""); continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 361 */     if (token == SLASH) {
/*     */       
/* 363 */       if (x.nextToken() != GT) {
/* 364 */         throw x.syntaxError("Misshaped tag");
/*     */       }
/* 366 */       if (config.getForceList().contains(tagName)) {
/*     */         
/* 368 */         if (nilAttributeFound) {
/* 369 */           context.append(tagName, JSONObject.NULL);
/* 370 */         } else if (jsonObject.length() > 0) {
/* 371 */           context.append(tagName, jsonObject);
/*     */         } else {
/* 373 */           context.put(tagName, new JSONArray());
/*     */         }
/*     */       
/* 376 */       } else if (nilAttributeFound) {
/* 377 */         context.accumulate(tagName, JSONObject.NULL);
/* 378 */       } else if (jsonObject.length() > 0) {
/* 379 */         context.accumulate(tagName, jsonObject);
/*     */       } else {
/* 381 */         context.accumulate(tagName, "");
/*     */       } 
/*     */       
/* 384 */       return false;
/*     */     } 
/* 386 */     if (token == GT) {
/*     */       while (true) {
/*     */         
/* 389 */         token = x.nextContent();
/* 390 */         if (token == null) {
/* 391 */           if (tagName != null) {
/* 392 */             throw x.syntaxError("Unclosed tag " + tagName);
/*     */           }
/* 394 */           return false;
/* 395 */         }  if (token instanceof String) {
/* 396 */           String string = (String)token;
/* 397 */           if (string.length() > 0) {
/* 398 */             if (xmlXsiTypeConverter != null) {
/* 399 */               jsonObject.accumulate(config.getcDataTagName(), 
/* 400 */                   stringToValue(string, xmlXsiTypeConverter)); continue;
/*     */             } 
/* 402 */             jsonObject.accumulate(config.getcDataTagName(), 
/* 403 */                 config.isKeepStrings() ? string : stringToValue(string));
/*     */           } 
/*     */           continue;
/*     */         } 
/* 407 */         if (token == LT) {
/*     */           
/* 409 */           if (currentNestingDepth == config.getMaxNestingDepth()) {
/* 410 */             throw x.syntaxError("Maximum nesting depth of " + config.getMaxNestingDepth() + " reached");
/*     */           }
/*     */           
/* 413 */           if (parse(x, jsonObject, tagName, config, currentNestingDepth + 1)) {
/* 414 */             if (config.getForceList().contains(tagName)) {
/*     */               
/* 416 */               if (jsonObject.length() == 0) {
/* 417 */                 context.put(tagName, new JSONArray());
/* 418 */               } else if (jsonObject.length() == 1 && jsonObject
/* 419 */                 .opt(config.getcDataTagName()) != null) {
/* 420 */                 context.append(tagName, jsonObject.opt(config.getcDataTagName()));
/*     */               } else {
/* 422 */                 context.append(tagName, jsonObject);
/*     */               }
/*     */             
/* 425 */             } else if (jsonObject.length() == 0) {
/* 426 */               context.accumulate(tagName, "");
/* 427 */             } else if (jsonObject.length() == 1 && jsonObject
/* 428 */               .opt(config.getcDataTagName()) != null) {
/* 429 */               context.accumulate(tagName, jsonObject.opt(config.getcDataTagName()));
/*     */             } else {
/* 431 */               context.accumulate(tagName, jsonObject);
/*     */             } 
/*     */ 
/*     */             
/* 435 */             return false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 440 */     throw x.syntaxError("Misshaped tag");
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
/*     */   public static Object stringToValue(String string, XMLXsiTypeConverter<?> typeConverter) {
/* 453 */     if (typeConverter != null) {
/* 454 */       return typeConverter.convert(string);
/*     */     }
/* 456 */     return stringToValue(string);
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
/*     */   public static Object stringToValue(String string) {
/* 469 */     if ("".equals(string)) {
/* 470 */       return string;
/*     */     }
/*     */ 
/*     */     
/* 474 */     if ("true".equalsIgnoreCase(string)) {
/* 475 */       return Boolean.TRUE;
/*     */     }
/* 477 */     if ("false".equalsIgnoreCase(string)) {
/* 478 */       return Boolean.FALSE;
/*     */     }
/* 480 */     if ("null".equalsIgnoreCase(string)) {
/* 481 */       return JSONObject.NULL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     char initial = string.charAt(0);
/* 490 */     if ((initial >= '0' && initial <= '9') || initial == '-') {
/*     */       try {
/* 492 */         return stringToNumber(string);
/* 493 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 496 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Number stringToNumber(String val) throws NumberFormatException {
/* 503 */     char initial = val.charAt(0);
/* 504 */     if ((initial >= '0' && initial <= '9') || initial == '-') {
/*     */       
/* 506 */       if (isDecimalNotation(val)) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 511 */           BigDecimal bd = new BigDecimal(val);
/* 512 */           if (initial == '-' && BigDecimal.ZERO.compareTo(bd) == 0) {
/* 513 */             return Double.valueOf(-0.0D);
/*     */           }
/* 515 */           return bd;
/* 516 */         } catch (NumberFormatException retryAsDouble) {
/*     */           
/*     */           try {
/* 519 */             Double d = Double.valueOf(val);
/* 520 */             if (d.isNaN() || d.isInfinite()) {
/* 521 */               throw new NumberFormatException("val [" + val + "] is not a valid number.");
/*     */             }
/* 523 */             return d;
/* 524 */           } catch (NumberFormatException ignore) {
/* 525 */             throw new NumberFormatException("val [" + val + "] is not a valid number.");
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 530 */       if (initial == '0' && val.length() > 1) {
/* 531 */         char at1 = val.charAt(1);
/* 532 */         if (at1 >= '0' && at1 <= '9') {
/* 533 */           throw new NumberFormatException("val [" + val + "] is not a valid number.");
/*     */         }
/* 535 */       } else if (initial == '-' && val.length() > 2) {
/* 536 */         char at1 = val.charAt(1);
/* 537 */         char at2 = val.charAt(2);
/* 538 */         if (at1 == '0' && at2 >= '0' && at2 <= '9') {
/* 539 */           throw new NumberFormatException("val [" + val + "] is not a valid number.");
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 550 */       BigInteger bi = new BigInteger(val);
/* 551 */       if (bi.bitLength() <= 31) {
/* 552 */         return Integer.valueOf(bi.intValue());
/*     */       }
/* 554 */       if (bi.bitLength() <= 63) {
/* 555 */         return Long.valueOf(bi.longValue());
/*     */       }
/* 557 */       return bi;
/*     */     } 
/* 559 */     throw new NumberFormatException("val [" + val + "] is not a valid number.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDecimalNotation(String val) {
/* 566 */     return (val.indexOf('.') > -1 || val.indexOf('e') > -1 || val
/* 567 */       .indexOf('E') > -1 || "-0".equals(val));
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
/*     */   public static JSONObject toJSONObject(String string) throws JSONException {
/* 589 */     return toJSONObject(string, XMLParserConfiguration.ORIGINAL);
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
/*     */   public static JSONObject toJSONObject(Reader reader) throws JSONException {
/* 609 */     return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
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
/*     */   public static JSONObject toJSONObject(Reader reader, boolean keepStrings) throws JSONException {
/* 634 */     if (keepStrings) {
/* 635 */       return toJSONObject(reader, XMLParserConfiguration.KEEP_STRINGS);
/*     */     }
/* 637 */     return toJSONObject(reader, XMLParserConfiguration.ORIGINAL);
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
/*     */   public static JSONObject toJSONObject(Reader reader, XMLParserConfiguration config) throws JSONException {
/* 661 */     JSONObject jo = new JSONObject();
/* 662 */     XMLTokener x = new XMLTokener(reader);
/* 663 */     while (x.more()) {
/* 664 */       x.skipPast("<");
/* 665 */       if (x.more()) {
/* 666 */         parse(x, jo, null, config, 0);
/*     */       }
/*     */     } 
/* 669 */     return jo;
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
/*     */   public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
/* 695 */     return toJSONObject(new StringReader(string), keepStrings);
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
/*     */   public static JSONObject toJSONObject(String string, XMLParserConfiguration config) throws JSONException {
/* 720 */     return toJSONObject(new StringReader(string), config);
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
/*     */   public static String toString(Object object) throws JSONException {
/* 732 */     return toString(object, (String)null, XMLParserConfiguration.ORIGINAL);
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
/*     */   public static String toString(Object object, String tagName) {
/* 746 */     return toString(object, tagName, XMLParserConfiguration.ORIGINAL);
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
/*     */   public static String toString(Object object, String tagName, XMLParserConfiguration config) throws JSONException {
/* 763 */     return toString(object, tagName, config, 0, 0);
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
/*     */   private static String toString(Object object, String tagName, XMLParserConfiguration config, int indentFactor, int indent) throws JSONException {
/* 785 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 790 */     if (object instanceof JSONObject) {
/*     */ 
/*     */       
/* 793 */       if (tagName != null) {
/* 794 */         sb.append(indent(indent));
/* 795 */         sb.append('<');
/* 796 */         sb.append(tagName);
/* 797 */         sb.append('>');
/* 798 */         if (indentFactor > 0) {
/* 799 */           sb.append("\n");
/* 800 */           indent += indentFactor;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 806 */       JSONObject jo = (JSONObject)object;
/* 807 */       for (String key : jo.keySet()) {
/* 808 */         Object value = jo.opt(key);
/* 809 */         if (value == null) {
/* 810 */           value = "";
/* 811 */         } else if (value.getClass().isArray()) {
/* 812 */           value = new JSONArray(value);
/*     */         } 
/*     */ 
/*     */         
/* 816 */         if (key.equals(config.getcDataTagName())) {
/* 817 */           if (value instanceof JSONArray) {
/* 818 */             JSONArray ja = (JSONArray)value;
/* 819 */             int jaLength = ja.length();
/*     */             
/* 821 */             for (int i = 0; i < jaLength; i++) {
/* 822 */               if (i > 0) {
/* 823 */                 sb.append('\n');
/*     */               }
/* 825 */               Object val = ja.opt(i);
/* 826 */               sb.append(escape(val.toString()));
/*     */             }  continue;
/*     */           } 
/* 829 */           sb.append(escape(value.toString()));
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 834 */         if (value instanceof JSONArray) {
/* 835 */           JSONArray ja = (JSONArray)value;
/* 836 */           int jaLength = ja.length();
/*     */           
/* 838 */           for (int i = 0; i < jaLength; i++) {
/* 839 */             Object val = ja.opt(i);
/* 840 */             if (val instanceof JSONArray) {
/* 841 */               sb.append('<');
/* 842 */               sb.append(key);
/* 843 */               sb.append('>');
/* 844 */               sb.append(toString(val, null, config, indentFactor, indent));
/* 845 */               sb.append("</");
/* 846 */               sb.append(key);
/* 847 */               sb.append('>');
/*     */             } else {
/* 849 */               sb.append(toString(val, key, config, indentFactor, indent));
/*     */             } 
/*     */           }  continue;
/* 852 */         }  if ("".equals(value)) {
/* 853 */           sb.append(indent(indent));
/* 854 */           sb.append('<');
/* 855 */           sb.append(key);
/* 856 */           sb.append("/>");
/* 857 */           if (indentFactor > 0) {
/* 858 */             sb.append("\n");
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 864 */         sb.append(toString(value, key, config, indentFactor, indent));
/*     */       } 
/*     */       
/* 867 */       if (tagName != null) {
/*     */ 
/*     */         
/* 870 */         sb.append(indent(indent - indentFactor));
/* 871 */         sb.append("</");
/* 872 */         sb.append(tagName);
/* 873 */         sb.append('>');
/* 874 */         if (indentFactor > 0) {
/* 875 */           sb.append("\n");
/*     */         }
/*     */       } 
/* 878 */       return sb.toString();
/*     */     } 
/*     */ 
/*     */     
/* 882 */     if (object != null && (object instanceof JSONArray || object.getClass().isArray())) {
/* 883 */       JSONArray ja; if (object.getClass().isArray()) {
/* 884 */         ja = new JSONArray(object);
/*     */       } else {
/* 886 */         ja = (JSONArray)object;
/*     */       } 
/* 888 */       int jaLength = ja.length();
/*     */       
/* 890 */       for (int i = 0; i < jaLength; i++) {
/* 891 */         Object val = ja.opt(i);
/*     */ 
/*     */ 
/*     */         
/* 895 */         sb.append(toString(val, (tagName == null) ? "array" : tagName, config, indentFactor, indent));
/*     */       } 
/* 897 */       return sb.toString();
/*     */     } 
/*     */ 
/*     */     
/* 901 */     String string = (object == null) ? "null" : escape(object.toString());
/*     */     
/* 903 */     if (tagName == null)
/* 904 */       return indent(indent) + "\"" + string + "\"" + ((indentFactor > 0) ? "\n" : ""); 
/* 905 */     if (string.length() == 0) {
/* 906 */       return indent(indent) + "<" + tagName + "/>" + ((indentFactor > 0) ? "\n" : "");
/*     */     }
/* 908 */     return indent(indent) + "<" + tagName + ">" + string + "</" + tagName + ">" + ((indentFactor > 0) ? "\n" : "");
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
/*     */   public static String toString(Object object, int indentFactor) {
/* 924 */     return toString(object, null, XMLParserConfiguration.ORIGINAL, indentFactor);
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
/*     */   public static String toString(Object object, String tagName, int indentFactor) {
/* 940 */     return toString(object, tagName, XMLParserConfiguration.ORIGINAL, indentFactor);
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
/*     */   public static String toString(Object object, String tagName, XMLParserConfiguration config, int indentFactor) throws JSONException {
/* 959 */     return toString(object, tagName, config, indentFactor, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String indent(int indent) {
/* 970 */     StringBuilder sb = new StringBuilder();
/* 971 */     for (int i = 0; i < indent; i++) {
/* 972 */       sb.append(' ');
/*     */     }
/* 974 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/XML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */