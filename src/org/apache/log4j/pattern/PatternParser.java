/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.log4j.helpers.Loader;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternParser
/*     */ {
/*     */   private static final char ESCAPE_CHAR = '%';
/*     */   private static final int LITERAL_STATE = 0;
/*     */   private static final int CONVERTER_STATE = 1;
/*     */   private static final int DOT_STATE = 3;
/*     */   private static final int MIN_STATE = 4;
/*     */   private static final int MAX_STATE = 5;
/*     */   private static final Map PATTERN_LAYOUT_RULES;
/*     */   private static final Map FILENAME_PATTERN_RULES;
/*     */   
/*     */   static {
/*  92 */     Map<Object, Object> rules = new HashMap<Object, Object>(17);
/*  93 */     rules.put("c", LoggerPatternConverter.class);
/*  94 */     rules.put("logger", LoggerPatternConverter.class);
/*     */     
/*  96 */     rules.put("C", ClassNamePatternConverter.class);
/*  97 */     rules.put("class", ClassNamePatternConverter.class);
/*     */     
/*  99 */     rules.put("d", DatePatternConverter.class);
/* 100 */     rules.put("date", DatePatternConverter.class);
/*     */     
/* 102 */     rules.put("F", FileLocationPatternConverter.class);
/* 103 */     rules.put("file", FileLocationPatternConverter.class);
/*     */     
/* 105 */     rules.put("l", FullLocationPatternConverter.class);
/*     */     
/* 107 */     rules.put("L", LineLocationPatternConverter.class);
/* 108 */     rules.put("line", LineLocationPatternConverter.class);
/*     */     
/* 110 */     rules.put("m", MessagePatternConverter.class);
/* 111 */     rules.put("message", MessagePatternConverter.class);
/*     */     
/* 113 */     rules.put("n", LineSeparatorPatternConverter.class);
/*     */     
/* 115 */     rules.put("M", MethodLocationPatternConverter.class);
/* 116 */     rules.put("method", MethodLocationPatternConverter.class);
/*     */     
/* 118 */     rules.put("p", LevelPatternConverter.class);
/* 119 */     rules.put("level", LevelPatternConverter.class);
/*     */     
/* 121 */     rules.put("r", RelativeTimePatternConverter.class);
/* 122 */     rules.put("relative", RelativeTimePatternConverter.class);
/*     */     
/* 124 */     rules.put("t", ThreadPatternConverter.class);
/* 125 */     rules.put("thread", ThreadPatternConverter.class);
/*     */     
/* 127 */     rules.put("x", NDCPatternConverter.class);
/* 128 */     rules.put("ndc", NDCPatternConverter.class);
/*     */     
/* 130 */     rules.put("X", PropertiesPatternConverter.class);
/* 131 */     rules.put("properties", PropertiesPatternConverter.class);
/*     */     
/* 133 */     rules.put("sn", SequenceNumberPatternConverter.class);
/* 134 */     rules.put("sequenceNumber", SequenceNumberPatternConverter.class);
/*     */     
/* 136 */     rules.put("throwable", ThrowableInformationPatternConverter.class);
/* 137 */     PATTERN_LAYOUT_RULES = new ReadOnlyMap(rules);
/*     */     
/* 139 */     Map<Object, Object> fnameRules = new HashMap<Object, Object>(4);
/* 140 */     fnameRules.put("d", FileDatePatternConverter.class);
/* 141 */     fnameRules.put("date", FileDatePatternConverter.class);
/* 142 */     fnameRules.put("i", IntegerPatternConverter.class);
/* 143 */     fnameRules.put("index", IntegerPatternConverter.class);
/*     */     
/* 145 */     FILENAME_PATTERN_RULES = new ReadOnlyMap(fnameRules);
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
/*     */   public static Map getPatternLayoutRules() {
/* 161 */     return PATTERN_LAYOUT_RULES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map getFileNamePatternRules() {
/* 171 */     return FILENAME_PATTERN_RULES;
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
/*     */   private static int extractConverter(char lastChar, String pattern, int i, StringBuilder convBuf, StringBuilder currentLiteral) {
/* 193 */     convBuf.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     if (!Character.isUnicodeIdentifierStart(lastChar)) {
/* 201 */       return i;
/*     */     }
/*     */     
/* 204 */     convBuf.append(lastChar);
/*     */     
/* 206 */     while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
/* 207 */       convBuf.append(pattern.charAt(i));
/* 208 */       currentLiteral.append(pattern.charAt(i));
/*     */ 
/*     */       
/* 211 */       i++;
/*     */     } 
/*     */     
/* 214 */     return i;
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
/*     */   private static int extractOptions(String pattern, int i, List<String> options) {
/* 226 */     while (i < pattern.length() && pattern.charAt(i) == '{') {
/* 227 */       int end = pattern.indexOf('}', i);
/*     */       
/* 229 */       if (end == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 233 */       String r = pattern.substring(i + 1, end);
/* 234 */       options.add(r);
/* 235 */       i = end + 1;
/*     */     } 
/*     */     
/* 238 */     return i;
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
/*     */   public static void parse(String pattern, List<LiteralPatternConverter> patternConverters, List<FormattingInfo> formattingInfos, Map converterRegistry, Map rules) {
/* 255 */     if (pattern == null) {
/* 256 */       throw new NullPointerException("pattern");
/*     */     }
/*     */     
/* 259 */     StringBuilder currentLiteral = new StringBuilder(32);
/*     */     
/* 261 */     int patternLength = pattern.length();
/* 262 */     int state = 0;
/*     */     
/* 264 */     int i = 0;
/* 265 */     FormattingInfo formattingInfo = FormattingInfo.getDefault();
/*     */     
/* 267 */     while (i < patternLength) {
/* 268 */       char c = pattern.charAt(i++);
/*     */       
/* 270 */       switch (state) {
/*     */ 
/*     */         
/*     */         case 0:
/* 274 */           if (i == patternLength) {
/* 275 */             currentLiteral.append(c);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 280 */           if (c == '%') {
/*     */             
/* 282 */             switch (pattern.charAt(i)) {
/*     */               case '%':
/* 284 */                 currentLiteral.append(c);
/* 285 */                 i++;
/*     */                 continue;
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 291 */             if (currentLiteral.length() != 0) {
/* 292 */               patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
/* 293 */               formattingInfos.add(FormattingInfo.getDefault());
/*     */             } 
/*     */             
/* 296 */             currentLiteral.setLength(0);
/* 297 */             currentLiteral.append(c);
/* 298 */             state = 1;
/* 299 */             formattingInfo = FormattingInfo.getDefault();
/*     */             continue;
/*     */           } 
/* 302 */           currentLiteral.append(c);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 308 */           currentLiteral.append(c);
/*     */           
/* 310 */           switch (c) {
/*     */             
/*     */             case '-':
/* 313 */               formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(), formattingInfo.getMaxLength());
/*     */               continue;
/*     */ 
/*     */             
/*     */             case '.':
/* 318 */               state = 3;
/*     */               continue;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 324 */           if (c >= '0' && c <= '9') {
/*     */             
/* 326 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), c - 48, formattingInfo.getMaxLength());
/* 327 */             state = 4; continue;
/*     */           } 
/* 329 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
/*     */ 
/*     */ 
/*     */           
/* 333 */           state = 0;
/* 334 */           formattingInfo = FormattingInfo.getDefault();
/* 335 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 342 */           currentLiteral.append(c);
/*     */           
/* 344 */           if (c >= '0' && c <= '9') {
/*     */             
/* 346 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength() * 10 + c - 48, formattingInfo.getMaxLength()); continue;
/* 347 */           }  if (c == '.') {
/* 348 */             state = 3; continue;
/*     */           } 
/* 350 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
/*     */           
/* 352 */           state = 0;
/* 353 */           formattingInfo = FormattingInfo.getDefault();
/* 354 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 360 */           currentLiteral.append(c);
/*     */           
/* 362 */           if (c >= '0' && c <= '9') {
/* 363 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), c - 48);
/*     */             
/* 365 */             state = 5; continue;
/*     */           } 
/* 367 */           LogLog.error("Error occured in position " + i + ".\n Was expecting digit, instead got char \"" + c + "\".");
/*     */ 
/*     */           
/* 370 */           state = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 5:
/* 376 */           currentLiteral.append(c);
/*     */           
/* 378 */           if (c >= '0' && c <= '9') {
/*     */             
/* 380 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength() * 10 + c - 48); continue;
/*     */           } 
/* 382 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, converterRegistry, rules, patternConverters, formattingInfos);
/*     */           
/* 384 */           state = 0;
/* 385 */           formattingInfo = FormattingInfo.getDefault();
/* 386 */           currentLiteral.setLength(0);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 394 */     if (currentLiteral.length() != 0) {
/* 395 */       patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
/* 396 */       formattingInfos.add(FormattingInfo.getDefault());
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
/*     */   private static PatternConverter createConverter(String converterId, StringBuilder currentLiteral, Map converterRegistry, Map rules, List options) {
/* 417 */     String converterName = converterId;
/* 418 */     Object converterObj = null;
/*     */     
/* 420 */     for (int i = converterId.length(); i > 0 && converterObj == null; i--) {
/* 421 */       converterName = converterName.substring(0, i);
/*     */       
/* 423 */       if (converterRegistry != null) {
/* 424 */         converterObj = converterRegistry.get(converterName);
/*     */       }
/*     */       
/* 427 */       if (converterObj == null && rules != null) {
/* 428 */         converterObj = rules.get(converterName);
/*     */       }
/*     */     } 
/*     */     
/* 432 */     if (converterObj == null) {
/* 433 */       LogLog.error("Unrecognized format specifier [" + converterId + "]");
/*     */       
/* 435 */       return null;
/*     */     } 
/*     */     
/* 438 */     Class<PatternConverter> converterClass = null;
/*     */     
/* 440 */     if (converterObj instanceof Class) {
/* 441 */       converterClass = (Class)converterObj;
/*     */     }
/* 443 */     else if (converterObj instanceof String) {
/*     */       try {
/* 445 */         converterClass = Loader.loadClass((String)converterObj);
/* 446 */       } catch (ClassNotFoundException ex) {
/* 447 */         LogLog.warn("Class for conversion pattern %" + converterName + " not found", ex);
/*     */         
/* 449 */         return null;
/*     */       } 
/*     */     } else {
/* 452 */       LogLog.warn("Bad map entry for conversion pattern %" + converterName + ".");
/*     */       
/* 454 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 459 */       Method factory = converterClass.getMethod("newInstance", new Class[] {
/* 460 */             Class.forName("[Ljava.lang.String;") });
/* 461 */       String[] optionsArray = new String[options.size()];
/* 462 */       optionsArray = (String[])options.toArray((Object[])optionsArray);
/*     */       
/* 464 */       Object newObj = factory.invoke(null, new Object[] { optionsArray });
/*     */       
/* 466 */       if (newObj instanceof PatternConverter) {
/* 467 */         currentLiteral.delete(0, currentLiteral.length() - converterId.length() - converterName.length());
/*     */         
/* 469 */         return (PatternConverter)newObj;
/*     */       } 
/* 471 */       LogLog.warn("Class " + converterClass.getName() + " does not extend PatternConverter.");
/*     */     }
/* 473 */     catch (Exception ex) {
/* 474 */       LogLog.error("Error creating converter for " + converterId, ex);
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 479 */         PatternConverter pc = converterClass.newInstance();
/* 480 */         currentLiteral.delete(0, currentLiteral.length() - converterId.length() - converterName.length());
/*     */         
/* 482 */         return pc;
/* 483 */       } catch (Exception ex2) {
/* 484 */         LogLog.error("Error creating converter for " + converterId, ex2);
/*     */       } 
/*     */     } 
/*     */     
/* 488 */     return null;
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
/*     */   private static int finalizeConverter(char c, String pattern, int i, StringBuilder currentLiteral, FormattingInfo formattingInfo, Map converterRegistry, Map rules, List<LiteralPatternConverter> patternConverters, List<FormattingInfo> formattingInfos) {
/* 510 */     StringBuilder convBuf = new StringBuilder();
/* 511 */     i = extractConverter(c, pattern, i, convBuf, currentLiteral);
/*     */     
/* 513 */     String converterId = convBuf.toString();
/*     */     
/* 515 */     List options = new ArrayList();
/* 516 */     i = extractOptions(pattern, i, options);
/*     */     
/* 518 */     PatternConverter pc = createConverter(converterId, currentLiteral, converterRegistry, rules, options);
/*     */     
/* 520 */     if (pc == null) {
/*     */       StringBuilder msg;
/*     */       
/* 523 */       if (converterId == null || converterId.length() == 0) {
/* 524 */         msg = new StringBuilder("Empty conversion specifier starting at position ");
/*     */       } else {
/* 526 */         msg = new StringBuilder("Unrecognized conversion specifier [");
/* 527 */         msg.append(converterId);
/* 528 */         msg.append("] starting at position ");
/*     */       } 
/*     */       
/* 531 */       msg.append(Integer.toString(i));
/* 532 */       msg.append(" in conversion pattern.");
/*     */       
/* 534 */       LogLog.error(msg.toString());
/*     */       
/* 536 */       patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
/* 537 */       formattingInfos.add(FormattingInfo.getDefault());
/*     */     } else {
/* 539 */       patternConverters.add(pc);
/* 540 */       formattingInfos.add(formattingInfo);
/*     */       
/* 542 */       if (currentLiteral.length() > 0) {
/* 543 */         patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
/* 544 */         formattingInfos.add(FormattingInfo.getDefault());
/*     */       } 
/*     */     } 
/*     */     
/* 548 */     currentLiteral.setLength(0);
/*     */     
/* 550 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ReadOnlyMap
/*     */     implements Map
/*     */   {
/*     */     private final Map map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadOnlyMap(Map src) {
/* 569 */       this.map = src;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 576 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 583 */       return this.map.containsKey(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 590 */       return this.map.containsValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set entrySet() {
/* 597 */       return this.map.entrySet();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object get(Object key) {
/* 604 */       return this.map.get(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 611 */       return this.map.isEmpty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set keySet() {
/* 618 */       return this.map.keySet();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object put(Object key, Object value) {
/* 625 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putAll(Map t) {
/* 632 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object remove(Object key) {
/* 639 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 646 */       return this.map.size();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Collection values() {
/* 653 */       return this.map.values();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/PatternParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */