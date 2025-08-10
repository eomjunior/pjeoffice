/*     */ package com.yworks.yguard.ant;
/*     */ 
/*     */ import com.yworks.yguard.ObfuscatorTask;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MapParser
/*     */   implements ContentHandler
/*     */ {
/*     */   private int state;
/*  26 */   private ArrayList entries = new ArrayList(50);
/*  27 */   private Map ownerProperties = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ObfuscatorTask obfuscatorTask;
/*     */ 
/*     */ 
/*     */   
/*     */   public MapParser(ObfuscatorTask obfuscatorTask) {
/*  36 */     this.obfuscatorTask = obfuscatorTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getEntries() {
/*  45 */     return this.entries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Map getOwnerProperties() {
/*  54 */     return this.ownerProperties;
/*     */   }
/*     */   
/*     */   public void characters(char[] values, int param, int param2) {}
/*     */   
/*     */   public void endDocument() {
/*  60 */     this.state = 0;
/*     */   }
/*     */   
/*     */   public void endElement(String str, String str1, String str2) {
/*  64 */     if (this.state == 3 && "map".equals(str2)) {
/*  65 */       this.state = 2;
/*     */     }
/*  67 */     if (this.state == 2 && "yguard".equals(str2)) {
/*  68 */       this.state = 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String str) {}
/*     */   
/*     */   public void ignorableWhitespace(char[] values, int param, int param2) {}
/*     */   
/*     */   public void processingInstruction(String str, String str1) {}
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void skippedEntity(String str) {}
/*     */   
/*     */   public void startDocument() {
/*  84 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String str, String str1, String str2, Attributes attributes) throws SAXException {
/*  89 */     switch (this.state) {
/*     */       case 2:
/*  91 */         if ("map".equals(str2)) {
/*  92 */           this.state = 3; break;
/*     */         } 
/*  94 */         if ("property".equals(str2)) {
/*  95 */           String key = attributes.getValue("key");
/*  96 */           String value = attributes.getValue("key");
/*  97 */           String owner = attributes.getValue("owner");
/*  98 */           Map<Object, Object> map = (Map)this.ownerProperties.get(owner);
/*  99 */           if (map == null) {
/* 100 */             map = new HashMap<>();
/* 101 */             this.ownerProperties.put(owner, map);
/*     */           } 
/* 103 */           map.put(key, value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 111 */         if ("yguard".equals(str2)) {
/* 112 */           String version = attributes.getValue("version");
/* 113 */           if ("1.0".equals(version) || "1.1".equals(version) || "1.5".equals(version)) {
/* 114 */             this.state = 2; break;
/*     */           } 
/* 116 */           throw new SAXNotRecognizedException("Version '" + version + "' of yguard logfile not supported!");
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 123 */         if (str2.equals("package")) {
/* 124 */           PackageSection ps = new PackageSection();
/* 125 */           ps.setName(attributes.getValue("name"));
/* 126 */           ps.setMap(attributes.getValue("map"));
/* 127 */           ps.addMapEntries(this.entries); break;
/*     */         } 
/* 129 */         if (str2.equals("class")) {
/* 130 */           ClassSection cs = new ClassSection();
/* 131 */           cs.setName(attributes.getValue("name"));
/* 132 */           cs.setMap(attributes.getValue("map"));
/* 133 */           cs.addMapEntries(this.entries); break;
/*     */         } 
/* 135 */         if (str2.equals("method")) {
/* 136 */           MethodSection ms = new MethodSection();
/* 137 */           ms.setClass(attributes.getValue("class"));
/* 138 */           ms.setName(attributes.getValue("name"));
/* 139 */           ms.setMap(attributes.getValue("map"));
/* 140 */           ms.addMapEntries(this.entries); break;
/*     */         } 
/* 142 */         if (str2.equals("field")) {
/* 143 */           FieldSection fs = new FieldSection();
/* 144 */           fs.setClass(attributes.getValue("class"));
/* 145 */           fs.setName(attributes.getValue("name"));
/* 146 */           fs.setMap(attributes.getValue("map"));
/* 147 */           fs.addMapEntries(this.entries); break;
/*     */         } 
/* 149 */         throw new SAXNotRecognizedException("Unknown child element " + str2 + " in map element!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String str, String str1) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/MapParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */