/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.spi.LocationInfo;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternParser
/*     */ {
/*     */   private static final char LEFT_BRACKET = '{';
/*     */   private static final char RIGHT_BRACKET = '}';
/*     */   private static final char N_CHAR = 'n';
/*     */   private static final char DOT_CHAR = '.';
/*     */   private static final char DASH_CHAR = '-';
/*     */   private static final char ESCAPE_CHAR = '%';
/*     */   private static final int LITERAL_STATE = 0;
/*     */   private static final int CONVERTER_STATE = 1;
/*     */   private static final int DOT_STATE = 3;
/*     */   private static final int MIN_STATE = 4;
/*     */   private static final int MAX_STATE = 5;
/*     */   static final int FULL_LOCATION_CONVERTER = 1000;
/*     */   static final int METHOD_LOCATION_CONVERTER = 1001;
/*     */   static final int CLASS_LOCATION_CONVERTER = 1002;
/*     */   static final int LINE_LOCATION_CONVERTER = 1003;
/*     */   static final int FILE_LOCATION_CONVERTER = 1004;
/*     */   static final int RELATIVE_TIME_CONVERTER = 2000;
/*     */   static final int THREAD_CONVERTER = 2001;
/*     */   static final int LEVEL_CONVERTER = 2002;
/*     */   static final int NDC_CONVERTER = 2003;
/*     */   static final int MESSAGE_CONVERTER = 2004;
/*     */   int state;
/*  74 */   protected StringBuffer currentLiteral = new StringBuffer(32);
/*     */   protected int patternLength;
/*     */   protected int i;
/*     */   PatternConverter head;
/*     */   PatternConverter tail;
/*  79 */   protected FormattingInfo formattingInfo = new FormattingInfo();
/*     */   protected String pattern;
/*     */   
/*     */   public PatternParser(String pattern) {
/*  83 */     this.pattern = pattern;
/*  84 */     this.patternLength = pattern.length();
/*  85 */     this.state = 0;
/*     */   }
/*     */   
/*     */   private void addToList(PatternConverter pc) {
/*  89 */     if (this.head == null) {
/*  90 */       this.head = this.tail = pc;
/*     */     } else {
/*  92 */       this.tail.next = pc;
/*  93 */       this.tail = pc;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String extractOption() {
/*  98 */     if (this.i < this.patternLength && this.pattern.charAt(this.i) == '{') {
/*  99 */       int end = this.pattern.indexOf('}', this.i);
/* 100 */       if (end > this.i) {
/* 101 */         String r = this.pattern.substring(this.i + 1, end);
/* 102 */         this.i = end + 1;
/* 103 */         return r;
/*     */       } 
/*     */     } 
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int extractPrecisionOption() {
/* 114 */     String opt = extractOption();
/* 115 */     int r = 0;
/* 116 */     if (opt != null) {
/*     */       try {
/* 118 */         r = Integer.parseInt(opt);
/* 119 */         if (r <= 0) {
/* 120 */           LogLog.error("Precision option (" + opt + ") isn't a positive integer.");
/* 121 */           r = 0;
/*     */         } 
/* 123 */       } catch (NumberFormatException e) {
/* 124 */         LogLog.error("Category option \"" + opt + "\" not a decimal integer.", e);
/*     */       } 
/*     */     }
/* 127 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public PatternConverter parse() {
/* 132 */     this.i = 0;
/* 133 */     while (this.i < this.patternLength) {
/* 134 */       char c = this.pattern.charAt(this.i++);
/* 135 */       switch (this.state) {
/*     */         
/*     */         case 0:
/* 138 */           if (this.i == this.patternLength) {
/* 139 */             this.currentLiteral.append(c);
/*     */             continue;
/*     */           } 
/* 142 */           if (c == '%') {
/*     */             
/* 144 */             switch (this.pattern.charAt(this.i)) {
/*     */               case '%':
/* 146 */                 this.currentLiteral.append(c);
/* 147 */                 this.i++;
/*     */                 continue;
/*     */               case 'n':
/* 150 */                 this.currentLiteral.append(Layout.LINE_SEP);
/* 151 */                 this.i++;
/*     */                 continue;
/*     */             } 
/* 154 */             if (this.currentLiteral.length() != 0) {
/* 155 */               addToList(new LiteralPatternConverter(this.currentLiteral.toString()));
/*     */             }
/*     */ 
/*     */             
/* 159 */             this.currentLiteral.setLength(0);
/* 160 */             this.currentLiteral.append(c);
/* 161 */             this.state = 1;
/* 162 */             this.formattingInfo.reset();
/*     */             continue;
/*     */           } 
/* 165 */           this.currentLiteral.append(c);
/*     */ 
/*     */         
/*     */         case 1:
/* 169 */           this.currentLiteral.append(c);
/* 170 */           switch (c) {
/*     */             case '-':
/* 172 */               this.formattingInfo.leftAlign = true;
/*     */               continue;
/*     */             case '.':
/* 175 */               this.state = 3;
/*     */               continue;
/*     */           } 
/* 178 */           if (c >= '0' && c <= '9') {
/* 179 */             this.formattingInfo.min = c - 48;
/* 180 */             this.state = 4; continue;
/*     */           } 
/* 182 */           finalizeConverter(c);
/*     */ 
/*     */         
/*     */         case 4:
/* 186 */           this.currentLiteral.append(c);
/* 187 */           if (c >= '0' && c <= '9') {
/* 188 */             this.formattingInfo.min = this.formattingInfo.min * 10 + c - 48; continue;
/* 189 */           }  if (c == '.') {
/* 190 */             this.state = 3; continue;
/*     */           } 
/* 192 */           finalizeConverter(c);
/*     */ 
/*     */         
/*     */         case 3:
/* 196 */           this.currentLiteral.append(c);
/* 197 */           if (c >= '0' && c <= '9') {
/* 198 */             this.formattingInfo.max = c - 48;
/* 199 */             this.state = 5; continue;
/*     */           } 
/* 201 */           LogLog.error("Error occured in position " + this.i + ".\n Was expecting digit, instead got char \"" + c + "\".");
/*     */           
/* 203 */           this.state = 0;
/*     */ 
/*     */         
/*     */         case 5:
/* 207 */           this.currentLiteral.append(c);
/* 208 */           if (c >= '0' && c <= '9') {
/* 209 */             this.formattingInfo.max = this.formattingInfo.max * 10 + c - 48; continue;
/*     */           } 
/* 211 */           finalizeConverter(c);
/* 212 */           this.state = 0;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 217 */     if (this.currentLiteral.length() != 0) {
/* 218 */       addToList(new LiteralPatternConverter(this.currentLiteral.toString()));
/*     */     }
/*     */     
/* 221 */     return this.head; } protected void finalizeConverter(char c) {
/*     */     String dateFormatStr;
/*     */     DateFormat df;
/*     */     String dOpt, xOpt;
/* 225 */     PatternConverter pc = null;
/* 226 */     switch (c) {
/*     */       case 'c':
/* 228 */         pc = new CategoryPatternConverter(this.formattingInfo, extractPrecisionOption());
/*     */ 
/*     */         
/* 231 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'C':
/* 234 */         pc = new ClassNamePatternConverter(this.formattingInfo, extractPrecisionOption());
/*     */ 
/*     */         
/* 237 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'd':
/* 240 */         dateFormatStr = "ISO8601";
/*     */         
/* 242 */         dOpt = extractOption();
/* 243 */         if (dOpt != null) {
/* 244 */           dateFormatStr = dOpt;
/*     */         }
/* 246 */         if (dateFormatStr.equalsIgnoreCase("ISO8601")) {
/* 247 */           df = new ISO8601DateFormat();
/* 248 */         } else if (dateFormatStr.equalsIgnoreCase("ABSOLUTE")) {
/* 249 */           df = new AbsoluteTimeDateFormat();
/* 250 */         } else if (dateFormatStr.equalsIgnoreCase("DATE")) {
/* 251 */           df = new DateTimeDateFormat();
/*     */         } else {
/*     */           try {
/* 254 */             df = new SimpleDateFormat(dateFormatStr);
/* 255 */           } catch (IllegalArgumentException e) {
/* 256 */             LogLog.error("Could not instantiate SimpleDateFormat with " + dateFormatStr, e);
/* 257 */             df = (DateFormat)OptionConverter.instantiateByClassName("org.apache.log4j.helpers.ISO8601DateFormat", DateFormat.class, null);
/*     */           } 
/*     */         } 
/*     */         
/* 261 */         pc = new DatePatternConverter(this.formattingInfo, df);
/*     */ 
/*     */         
/* 264 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'F':
/* 267 */         pc = new LocationPatternConverter(this.formattingInfo, 1004);
/*     */ 
/*     */         
/* 270 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'l':
/* 273 */         pc = new LocationPatternConverter(this.formattingInfo, 1000);
/*     */ 
/*     */         
/* 276 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'L':
/* 279 */         pc = new LocationPatternConverter(this.formattingInfo, 1003);
/*     */ 
/*     */         
/* 282 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'm':
/* 285 */         pc = new BasicPatternConverter(this.formattingInfo, 2004);
/*     */ 
/*     */         
/* 288 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'M':
/* 291 */         pc = new LocationPatternConverter(this.formattingInfo, 1001);
/*     */ 
/*     */         
/* 294 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'p':
/* 297 */         pc = new BasicPatternConverter(this.formattingInfo, 2002);
/*     */ 
/*     */         
/* 300 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'r':
/* 303 */         pc = new BasicPatternConverter(this.formattingInfo, 2000);
/*     */ 
/*     */         
/* 306 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 't':
/* 309 */         pc = new BasicPatternConverter(this.formattingInfo, 2001);
/*     */ 
/*     */         
/* 312 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 'x':
/* 322 */         pc = new BasicPatternConverter(this.formattingInfo, 2003);
/*     */         
/* 324 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'X':
/* 327 */         xOpt = extractOption();
/* 328 */         pc = new MDCPatternConverter(this.formattingInfo, xOpt);
/* 329 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       default:
/* 332 */         LogLog.error("Unexpected char [" + c + "] at position " + this.i + " in conversion patterrn.");
/* 333 */         pc = new LiteralPatternConverter(this.currentLiteral.toString());
/* 334 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */     } 
/* 337 */     addConverter(pc);
/*     */   }
/*     */   
/*     */   protected void addConverter(PatternConverter pc) {
/* 341 */     this.currentLiteral.setLength(0);
/*     */     
/* 343 */     addToList(pc);
/*     */     
/* 345 */     this.state = 0;
/*     */     
/* 347 */     this.formattingInfo.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BasicPatternConverter
/*     */     extends PatternConverter
/*     */   {
/*     */     int type;
/*     */ 
/*     */     
/*     */     BasicPatternConverter(FormattingInfo formattingInfo, int type) {
/* 358 */       super(formattingInfo);
/* 359 */       this.type = type;
/*     */     }
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 363 */       switch (this.type) {
/*     */         case 2000:
/* 365 */           return Long.toString(event.timeStamp - LoggingEvent.getStartTime());
/*     */         case 2001:
/* 367 */           return event.getThreadName();
/*     */         case 2002:
/* 369 */           return event.getLevel().toString();
/*     */         case 2003:
/* 371 */           return event.getNDC();
/*     */         case 2004:
/* 373 */           return event.getRenderedMessage();
/*     */       } 
/*     */       
/* 376 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LiteralPatternConverter
/*     */     extends PatternConverter {
/*     */     private String literal;
/*     */     
/*     */     LiteralPatternConverter(String value) {
/* 385 */       this.literal = value;
/*     */     }
/*     */     
/*     */     public final void format(StringBuffer sbuf, LoggingEvent event) {
/* 389 */       sbuf.append(this.literal);
/*     */     }
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 393 */       return this.literal;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DatePatternConverter extends PatternConverter {
/*     */     private DateFormat df;
/*     */     private Date date;
/*     */     
/*     */     DatePatternConverter(FormattingInfo formattingInfo, DateFormat df) {
/* 402 */       super(formattingInfo);
/* 403 */       this.date = new Date();
/* 404 */       this.df = df;
/*     */     }
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 408 */       this.date.setTime(event.timeStamp);
/* 409 */       String converted = null;
/*     */       try {
/* 411 */         converted = this.df.format(this.date);
/* 412 */       } catch (Exception ex) {
/* 413 */         LogLog.error("Error occured while converting date.", ex);
/*     */       } 
/* 415 */       return converted;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MDCPatternConverter extends PatternConverter {
/*     */     private String key;
/*     */     
/*     */     MDCPatternConverter(FormattingInfo formattingInfo, String key) {
/* 423 */       super(formattingInfo);
/* 424 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 428 */       if (this.key == null) {
/* 429 */         StringBuilder buf = new StringBuilder("{");
/* 430 */         Map properties = event.getProperties();
/* 431 */         if (properties.size() > 0) {
/* 432 */           Object[] keys = properties.keySet().toArray();
/* 433 */           Arrays.sort(keys);
/* 434 */           for (int i = 0; i < keys.length; i++) {
/* 435 */             buf.append('{');
/* 436 */             buf.append(keys[i]);
/* 437 */             buf.append(',');
/* 438 */             buf.append(properties.get(keys[i]));
/* 439 */             buf.append('}');
/*     */           } 
/*     */         } 
/* 442 */         buf.append('}');
/* 443 */         return buf.toString();
/*     */       } 
/* 445 */       Object val = event.getMDC(this.key);
/* 446 */       if (val == null) {
/* 447 */         return null;
/*     */       }
/* 449 */       return val.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class LocationPatternConverter
/*     */     extends PatternConverter
/*     */   {
/*     */     int type;
/*     */     
/*     */     LocationPatternConverter(FormattingInfo formattingInfo, int type) {
/* 459 */       super(formattingInfo);
/* 460 */       this.type = type;
/*     */     }
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 464 */       LocationInfo locationInfo = event.getLocationInformation();
/* 465 */       switch (this.type) {
/*     */         case 1000:
/* 467 */           return locationInfo.fullInfo;
/*     */         case 1001:
/* 469 */           return locationInfo.getMethodName();
/*     */         case 1003:
/* 471 */           return locationInfo.getLineNumber();
/*     */         case 1004:
/* 473 */           return locationInfo.getFileName();
/*     */       } 
/* 475 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class NamedPatternConverter
/*     */     extends PatternConverter {
/*     */     int precision;
/*     */     
/*     */     NamedPatternConverter(FormattingInfo formattingInfo, int precision) {
/* 484 */       super(formattingInfo);
/* 485 */       this.precision = precision;
/*     */     }
/*     */     
/*     */     abstract String getFullyQualifiedName(LoggingEvent param1LoggingEvent);
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 491 */       String n = getFullyQualifiedName(event);
/* 492 */       if (this.precision <= 0) {
/* 493 */         return n;
/*     */       }
/* 495 */       int len = n.length();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 500 */       int end = len - 1;
/* 501 */       for (int i = this.precision; i > 0; i--) {
/* 502 */         end = n.lastIndexOf('.', end - 1);
/* 503 */         if (end == -1)
/* 504 */           return n; 
/*     */       } 
/* 506 */       return n.substring(end + 1, len);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ClassNamePatternConverter
/*     */     extends NamedPatternConverter
/*     */   {
/*     */     ClassNamePatternConverter(FormattingInfo formattingInfo, int precision) {
/* 514 */       super(formattingInfo, precision);
/*     */     }
/*     */     
/*     */     String getFullyQualifiedName(LoggingEvent event) {
/* 518 */       return event.getLocationInformation().getClassName();
/*     */     }
/*     */   }
/*     */   
/*     */   private class CategoryPatternConverter
/*     */     extends NamedPatternConverter {
/*     */     CategoryPatternConverter(FormattingInfo formattingInfo, int precision) {
/* 525 */       super(formattingInfo, precision);
/*     */     }
/*     */     
/*     */     String getFullyQualifiedName(LoggingEvent event) {
/* 529 */       return event.getLoggerName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/PatternParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */