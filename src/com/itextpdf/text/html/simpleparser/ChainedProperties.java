/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.html.HtmlUtilities;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @Deprecated
/*     */ public class ChainedProperties
/*     */ {
/*     */   private static final class TagAttributes
/*     */   {
/*     */     final String tag;
/*     */     final Map<String, String> attrs;
/*     */     
/*     */     TagAttributes(String tag, Map<String, String> attrs) {
/*  75 */       this.tag = tag;
/*  76 */       this.attrs = attrs;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  81 */   public List<TagAttributes> chain = new ArrayList<TagAttributes>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProperty(String key) {
/*  95 */     for (int k = this.chain.size() - 1; k >= 0; k--) {
/*  96 */       TagAttributes p = this.chain.get(k);
/*  97 */       Map<String, String> attrs = p.attrs;
/*  98 */       String ret = attrs.get(key);
/*  99 */       if (ret != null)
/* 100 */         return ret; 
/*     */     } 
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasProperty(String key) {
/* 113 */     for (int k = this.chain.size() - 1; k >= 0; k--) {
/* 114 */       TagAttributes p = this.chain.get(k);
/* 115 */       Map<String, String> attrs = p.attrs;
/* 116 */       if (attrs.containsKey(key))
/* 117 */         return true; 
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToChain(String tag, Map<String, String> props) {
/* 128 */     adjustFontSize(props);
/* 129 */     this.chain.add(new TagAttributes(tag, props));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeChain(String tag) {
/* 138 */     for (int k = this.chain.size() - 1; k >= 0; k--) {
/* 139 */       if (tag.equals(((TagAttributes)this.chain.get(k)).tag)) {
/* 140 */         this.chain.remove(k);
/*     */         return;
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
/*     */   protected void adjustFontSize(Map<String, String> attrs) {
/* 154 */     String value = attrs.get("size");
/*     */     
/* 156 */     if (value == null) {
/*     */       return;
/*     */     }
/* 159 */     if (value.endsWith("pt")) {
/* 160 */       attrs.put("size", value
/* 161 */           .substring(0, value.length() - 2));
/*     */       return;
/*     */     } 
/* 164 */     String old = getProperty("size");
/* 165 */     attrs.put("size", Integer.toString(HtmlUtilities.getIndexedFontSize(value, old)));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/ChainedProperties.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */