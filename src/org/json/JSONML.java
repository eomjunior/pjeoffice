/*     */ package org.json;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONML
/*     */ {
/*     */   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja, boolean keepStrings, int currentNestingDepth) throws JSONException {
/*  33 */     return parse(x, arrayForm, ja, keepStrings ? JSONMLParserConfiguration.KEEP_STRINGS : JSONMLParserConfiguration.ORIGINAL, currentNestingDepth);
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
/*     */   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja, JSONMLParserConfiguration config, int currentNestingDepth) throws JSONException {
/*  59 */     String closeTag = null;
/*     */     
/*  61 */     JSONArray newja = null;
/*  62 */     JSONObject newjo = null;
/*     */     
/*  64 */     String tagName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     label118: while (true) {
/*  73 */       if (!x.more()) {
/*  74 */         throw x.syntaxError("Bad XML");
/*     */       }
/*  76 */       Object token = x.nextContent();
/*  77 */       if (token == XML.LT) {
/*  78 */         token = x.nextToken();
/*  79 */         if (token instanceof Character) {
/*  80 */           if (token == XML.SLASH) {
/*     */ 
/*     */ 
/*     */             
/*  84 */             token = x.nextToken();
/*  85 */             if (!(token instanceof String)) {
/*  86 */               throw new JSONException("Expected a closing name instead of '" + token + "'.");
/*     */             }
/*     */ 
/*     */             
/*  90 */             if (x.nextToken() != XML.GT) {
/*  91 */               throw x.syntaxError("Misshaped close tag");
/*     */             }
/*  93 */             return token;
/*  94 */           }  if (token == XML.BANG) {
/*     */ 
/*     */ 
/*     */             
/*  98 */             char c = x.next();
/*  99 */             if (c == '-') {
/* 100 */               if (x.next() == '-') {
/* 101 */                 x.skipPast("-->"); continue;
/*     */               } 
/* 103 */               x.back(); continue;
/*     */             } 
/* 105 */             if (c == '[') {
/* 106 */               token = x.nextToken();
/* 107 */               if (token.equals("CDATA") && x.next() == '[') {
/* 108 */                 if (ja != null)
/* 109 */                   ja.put(x.nextCDATA()); 
/*     */                 continue;
/*     */               } 
/* 112 */               throw x.syntaxError("Expected 'CDATA['");
/*     */             } 
/*     */             
/* 115 */             int i = 1;
/*     */             while (true)
/* 117 */             { token = x.nextMeta();
/* 118 */               if (token == null)
/* 119 */                 throw x.syntaxError("Missing '>' after '<!'."); 
/* 120 */               if (token == XML.LT) {
/* 121 */                 i++;
/* 122 */               } else if (token == XML.GT) {
/* 123 */                 i--;
/*     */               } 
/* 125 */               if (i <= 0)
/*     */                 continue label118;  }  break;
/* 127 */           }  if (token == XML.QUEST) {
/*     */ 
/*     */ 
/*     */             
/* 131 */             x.skipPast("?>"); continue;
/*     */           } 
/* 133 */           throw x.syntaxError("Misshaped tag");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 139 */         if (!(token instanceof String)) {
/* 140 */           throw x.syntaxError("Bad tagName '" + token + "'.");
/*     */         }
/* 142 */         tagName = (String)token;
/* 143 */         newja = new JSONArray();
/* 144 */         newjo = new JSONObject();
/* 145 */         if (arrayForm) {
/* 146 */           newja.put(tagName);
/* 147 */           if (ja != null) {
/* 148 */             ja.put(newja);
/*     */           }
/*     */         } else {
/* 151 */           newjo.put("tagName", tagName);
/* 152 */           if (ja != null) {
/* 153 */             ja.put(newjo);
/*     */           }
/*     */         } 
/* 156 */         token = null;
/*     */         while (true) {
/* 158 */           if (token == null) {
/* 159 */             token = x.nextToken();
/*     */           }
/* 161 */           if (token == null) {
/* 162 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/* 164 */           if (!(token instanceof String)) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 170 */           String attribute = (String)token;
/* 171 */           if (!arrayForm && ("tagName".equals(attribute) || "childNode".equals(attribute))) {
/* 172 */             throw x.syntaxError("Reserved attribute.");
/*     */           }
/* 174 */           token = x.nextToken();
/* 175 */           if (token == XML.EQ) {
/* 176 */             token = x.nextToken();
/* 177 */             if (!(token instanceof String)) {
/* 178 */               throw x.syntaxError("Missing value");
/*     */             }
/* 180 */             newjo.accumulate(attribute, config.isKeepStrings() ? token : XML.stringToValue((String)token));
/* 181 */             token = null; continue;
/*     */           } 
/* 183 */           newjo.accumulate(attribute, "");
/*     */         } 
/*     */         
/* 186 */         if (arrayForm && newjo.length() > 0) {
/* 187 */           newja.put(newjo);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 192 */         if (token == XML.SLASH) {
/* 193 */           if (x.nextToken() != XML.GT) {
/* 194 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/* 196 */           if (ja == null) {
/* 197 */             if (arrayForm) {
/* 198 */               return newja;
/*     */             }
/* 200 */             return newjo;
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 206 */         if (token != XML.GT) {
/* 207 */           throw x.syntaxError("Misshaped tag");
/*     */         }
/*     */         
/* 210 */         if (currentNestingDepth == config.getMaxNestingDepth()) {
/* 211 */           throw x.syntaxError("Maximum nesting depth of " + config.getMaxNestingDepth() + " reached");
/*     */         }
/*     */         
/* 214 */         closeTag = (String)parse(x, arrayForm, newja, config, currentNestingDepth + 1);
/* 215 */         if (closeTag != null) {
/* 216 */           if (!closeTag.equals(tagName)) {
/* 217 */             throw x.syntaxError("Mismatched '" + tagName + "' and '" + closeTag + "'");
/*     */           }
/*     */           
/* 220 */           tagName = null;
/* 221 */           if (!arrayForm && newja.length() > 0) {
/* 222 */             newjo.put("childNodes", newja);
/*     */           }
/* 224 */           if (ja == null) {
/* 225 */             if (arrayForm) {
/* 226 */               return newja;
/*     */             }
/* 228 */             return newjo;
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 234 */       if (ja != null) {
/* 235 */         ja.put((token instanceof String) ? (
/* 236 */             config.isKeepStrings() ? XML.unescape((String)token) : XML.stringToValue((String)token)) : token);
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
/*     */   public static JSONArray toJSONArray(String string) throws JSONException {
/* 257 */     return (JSONArray)parse(new XMLTokener(string), true, (JSONArray)null, JSONMLParserConfiguration.ORIGINAL, 0);
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
/*     */   public static JSONArray toJSONArray(String string, boolean keepStrings) throws JSONException {
/* 279 */     return (JSONArray)parse(new XMLTokener(string), true, (JSONArray)null, keepStrings, 0);
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
/*     */   public static JSONArray toJSONArray(String string, JSONMLParserConfiguration config) throws JSONException {
/* 304 */     return (JSONArray)parse(new XMLTokener(string), true, (JSONArray)null, config, 0);
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
/*     */   public static JSONArray toJSONArray(XMLTokener x, JSONMLParserConfiguration config) throws JSONException {
/* 328 */     return (JSONArray)parse(x, true, (JSONArray)null, config, 0);
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
/*     */   public static JSONArray toJSONArray(XMLTokener x, boolean keepStrings) throws JSONException {
/* 350 */     return (JSONArray)parse(x, true, (JSONArray)null, keepStrings, 0);
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
/*     */   public static JSONArray toJSONArray(XMLTokener x) throws JSONException {
/* 367 */     return (JSONArray)parse(x, true, (JSONArray)null, false, 0);
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
/*     */   public static JSONObject toJSONObject(String string) throws JSONException {
/* 385 */     return (JSONObject)parse(new XMLTokener(string), false, (JSONArray)null, false, 0);
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
/*     */   public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
/* 405 */     return (JSONObject)parse(new XMLTokener(string), false, (JSONArray)null, keepStrings, 0);
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
/*     */   public static JSONObject toJSONObject(String string, JSONMLParserConfiguration config) throws JSONException {
/* 427 */     return (JSONObject)parse(new XMLTokener(string), false, (JSONArray)null, config, 0);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x) throws JSONException {
/* 445 */     return (JSONObject)parse(x, false, (JSONArray)null, false, 0);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x, boolean keepStrings) throws JSONException {
/* 465 */     return (JSONObject)parse(x, false, (JSONArray)null, keepStrings, 0);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x, JSONMLParserConfiguration config) throws JSONException {
/* 487 */     return (JSONObject)parse(x, false, (JSONArray)null, config, 0);
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
/*     */   public static String toString(JSONArray ja) throws JSONException {
/*     */     int i;
/* 502 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 507 */     String tagName = ja.getString(0);
/* 508 */     XML.noSpace(tagName);
/* 509 */     tagName = XML.escape(tagName);
/* 510 */     sb.append('<');
/* 511 */     sb.append(tagName);
/*     */     
/* 513 */     Object object = ja.opt(1);
/* 514 */     if (object instanceof JSONObject) {
/* 515 */       i = 2;
/* 516 */       JSONObject jo = (JSONObject)object;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 521 */       for (String key : jo.keySet()) {
/* 522 */         Object value = jo.opt(key);
/* 523 */         XML.noSpace(key);
/* 524 */         if (value != null) {
/* 525 */           sb.append(' ');
/* 526 */           sb.append(XML.escape(key));
/* 527 */           sb.append('=');
/* 528 */           sb.append('"');
/* 529 */           sb.append(XML.escape(value.toString()));
/* 530 */           sb.append('"');
/*     */         } 
/*     */       } 
/*     */     } else {
/* 534 */       i = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 539 */     int length = ja.length();
/* 540 */     if (i >= length)
/* 541 */     { sb.append('/');
/* 542 */       sb.append('>'); }
/*     */     else
/* 544 */     { sb.append('>');
/*     */       while (true)
/* 546 */       { object = ja.get(i);
/* 547 */         i++;
/* 548 */         if (object != null) {
/* 549 */           if (object instanceof String) {
/* 550 */             sb.append(XML.escape(object.toString()));
/* 551 */           } else if (object instanceof JSONObject) {
/* 552 */             sb.append(toString((JSONObject)object));
/* 553 */           } else if (object instanceof JSONArray) {
/* 554 */             sb.append(toString((JSONArray)object));
/*     */           } else {
/* 556 */             sb.append(object.toString());
/*     */           } 
/*     */         }
/* 559 */         if (i >= length)
/* 560 */         { sb.append('<');
/* 561 */           sb.append('/');
/* 562 */           sb.append(tagName);
/* 563 */           sb.append('>');
/*     */           
/* 565 */           return sb.toString(); }  }  }  return sb.toString();
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
/*     */   public static String toString(JSONObject jo) throws JSONException {
/* 579 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 589 */     String tagName = jo.optString("tagName");
/* 590 */     if (tagName == null) {
/* 591 */       return XML.escape(jo.toString());
/*     */     }
/* 593 */     XML.noSpace(tagName);
/* 594 */     tagName = XML.escape(tagName);
/* 595 */     sb.append('<');
/* 596 */     sb.append(tagName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 601 */     for (String key : jo.keySet()) {
/* 602 */       if (!"tagName".equals(key) && !"childNodes".equals(key)) {
/* 603 */         XML.noSpace(key);
/* 604 */         Object value = jo.opt(key);
/* 605 */         if (value != null) {
/* 606 */           sb.append(' ');
/* 607 */           sb.append(XML.escape(key));
/* 608 */           sb.append('=');
/* 609 */           sb.append('"');
/* 610 */           sb.append(XML.escape(value.toString()));
/* 611 */           sb.append('"');
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 618 */     JSONArray ja = jo.optJSONArray("childNodes");
/* 619 */     if (ja == null) {
/* 620 */       sb.append('/');
/* 621 */       sb.append('>');
/*     */     } else {
/* 623 */       sb.append('>');
/* 624 */       int length = ja.length();
/* 625 */       for (int i = 0; i < length; i++) {
/* 626 */         Object object = ja.get(i);
/* 627 */         if (object != null) {
/* 628 */           if (object instanceof String) {
/* 629 */             sb.append(XML.escape(object.toString()));
/* 630 */           } else if (object instanceof JSONObject) {
/* 631 */             sb.append(toString((JSONObject)object));
/* 632 */           } else if (object instanceof JSONArray) {
/* 633 */             sb.append(toString((JSONArray)object));
/*     */           } else {
/* 635 */             sb.append(object.toString());
/*     */           } 
/*     */         }
/*     */       } 
/* 639 */       sb.append('<');
/* 640 */       sb.append('/');
/* 641 */       sb.append(tagName);
/* 642 */       sb.append('>');
/*     */     } 
/* 644 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/JSONML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */