/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.html.HtmlUtilities;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class StyleSheet
/*     */ {
/*  67 */   protected Map<String, Map<String, String>> tagMap = new HashMap<String, Map<String, String>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   protected Map<String, Map<String, String>> classMap = new HashMap<String, Map<String, String>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadTagStyle(String tag, Map<String, String> attrs) {
/*  88 */     this.tagMap.put(tag.toLowerCase(), attrs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadTagStyle(String tag, String key, String value) {
/*  99 */     tag = tag.toLowerCase();
/* 100 */     Map<String, String> styles = this.tagMap.get(tag);
/* 101 */     if (styles == null) {
/* 102 */       styles = new HashMap<String, String>();
/* 103 */       this.tagMap.put(tag, styles);
/*     */     } 
/* 105 */     styles.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadStyle(String className, HashMap<String, String> attrs) {
/* 114 */     this.classMap.put(className.toLowerCase(), attrs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadStyle(String className, String key, String value) {
/* 125 */     className = className.toLowerCase();
/* 126 */     Map<String, String> styles = this.classMap.get(className);
/* 127 */     if (styles == null) {
/* 128 */       styles = new HashMap<String, String>();
/* 129 */       this.classMap.put(className, styles);
/*     */     } 
/* 131 */     styles.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyStyle(String tag, Map<String, String> attrs) {
/* 142 */     Map<String, String> map = this.tagMap.get(tag.toLowerCase());
/* 143 */     if (map != null) {
/*     */       
/* 145 */       Map<String, String> map1 = new HashMap<String, String>(map);
/*     */       
/* 147 */       map1.putAll(attrs);
/*     */       
/* 149 */       attrs.putAll(map1);
/*     */     } 
/*     */     
/* 152 */     String cm = attrs.get("class");
/* 153 */     if (cm == null) {
/*     */       return;
/*     */     }
/* 156 */     map = this.classMap.get(cm.toLowerCase());
/* 157 */     if (map == null) {
/*     */       return;
/*     */     }
/* 160 */     attrs.remove("class");
/*     */     
/* 162 */     Map<String, String> temp = new HashMap<String, String>(map);
/*     */     
/* 164 */     temp.putAll(attrs);
/*     */     
/* 166 */     attrs.putAll(temp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resolveStyleAttribute(Map<String, String> h, ChainedProperties chain) {
/* 176 */     String style = h.get("style");
/* 177 */     if (style == null)
/*     */       return; 
/* 179 */     Properties prop = HtmlUtilities.parseAttributes(style);
/* 180 */     for (Object element : prop.keySet()) {
/* 181 */       String key = (String)element;
/* 182 */       if (key.equals("font-family")) {
/* 183 */         h.put("face", prop.getProperty(key)); continue;
/* 184 */       }  if (key.equals("font-size")) {
/* 185 */         float actualFontSize = HtmlUtilities.parseLength(chain
/* 186 */             .getProperty("size"), 12.0F);
/*     */         
/* 188 */         if (actualFontSize <= 0.0F)
/* 189 */           actualFontSize = 12.0F; 
/* 190 */         h.put("size", Float.toString(HtmlUtilities.parseLength(prop
/* 191 */                 .getProperty(key), actualFontSize)) + "pt"); continue;
/*     */       } 
/* 193 */       if (key.equals("font-style")) {
/* 194 */         String ss = prop.getProperty(key).trim().toLowerCase();
/* 195 */         if (ss.equals("italic") || ss.equals("oblique"))
/* 196 */           h.put("i", null);  continue;
/* 197 */       }  if (key.equals("font-weight")) {
/* 198 */         String ss = prop.getProperty(key).trim().toLowerCase();
/* 199 */         if (ss.equals("bold") || ss.equals("700") || ss.equals("800") || ss
/* 200 */           .equals("900"))
/* 201 */           h.put("b", null);  continue;
/* 202 */       }  if (key.equals("text-decoration")) {
/* 203 */         String ss = prop.getProperty(key).trim().toLowerCase();
/* 204 */         if (ss.equals("underline"))
/* 205 */           h.put("u", null);  continue;
/* 206 */       }  if (key.equals("color")) {
/* 207 */         BaseColor c = HtmlUtilities.decodeColor(prop.getProperty(key));
/* 208 */         if (c != null) {
/* 209 */           int hh = c.getRGB();
/* 210 */           String hs = Integer.toHexString(hh);
/* 211 */           hs = "000000" + hs;
/* 212 */           hs = "#" + hs.substring(hs.length() - 6);
/* 213 */           h.put("color", hs);
/*     */         }  continue;
/* 215 */       }  if (key.equals("line-height")) {
/* 216 */         String ss = prop.getProperty(key).trim();
/* 217 */         float actualFontSize = HtmlUtilities.parseLength(chain
/* 218 */             .getProperty("size"), 12.0F);
/*     */         
/* 220 */         if (actualFontSize <= 0.0F)
/* 221 */           actualFontSize = 12.0F; 
/* 222 */         float v = HtmlUtilities.parseLength(prop.getProperty(key), actualFontSize);
/*     */         
/* 224 */         if (ss.endsWith("%")) {
/* 225 */           h.put("leading", "0," + (v / 100.0F));
/*     */           return;
/*     */         } 
/* 228 */         if ("normal".equalsIgnoreCase(ss)) {
/* 229 */           h.put("leading", "0,1.5");
/*     */           return;
/*     */         } 
/* 232 */         h.put("leading", v + ",0"); continue;
/* 233 */       }  if (key.equals("text-align")) {
/* 234 */         String ss = prop.getProperty(key).trim().toLowerCase();
/* 235 */         h.put("align", ss); continue;
/* 236 */       }  if (key.equals("padding-left")) {
/* 237 */         String ss = prop.getProperty(key).trim().toLowerCase();
/* 238 */         h.put("indent", Float.toString(HtmlUtilities.parseLength(ss)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/StyleSheet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */