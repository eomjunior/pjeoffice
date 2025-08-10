/*     */ package org.json;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
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
/*     */ public class XMLParserConfiguration
/*     */   extends ParserConfiguration
/*     */ {
/*  26 */   public static final XMLParserConfiguration ORIGINAL = new XMLParserConfiguration();
/*     */ 
/*     */   
/*  29 */   public static final XMLParserConfiguration KEEP_STRINGS = (new XMLParserConfiguration())
/*  30 */     .withKeepStrings(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String cDataTagName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean convertNilAttributeToNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, XMLXsiTypeConverter<?>> xsiTypeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> forceList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLParserConfiguration() {
/*  63 */     this.cDataTagName = "content";
/*  64 */     this.convertNilAttributeToNull = false;
/*  65 */     this.xsiTypeMap = Collections.emptyMap();
/*  66 */     this.forceList = Collections.emptySet();
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
/*     */   @Deprecated
/*     */   public XMLParserConfiguration(boolean keepStrings) {
/*  79 */     this(keepStrings, "content", false);
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
/*     */   @Deprecated
/*     */   public XMLParserConfiguration(String cDataTagName) {
/*  94 */     this(false, cDataTagName, false);
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
/*     */   @Deprecated
/*     */   public XMLParserConfiguration(boolean keepStrings, String cDataTagName) {
/* 109 */     super(keepStrings, 512);
/* 110 */     this.cDataTagName = cDataTagName;
/* 111 */     this.convertNilAttributeToNull = false;
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
/*     */   @Deprecated
/*     */   public XMLParserConfiguration(boolean keepStrings, String cDataTagName, boolean convertNilAttributeToNull) {
/* 128 */     super(keepStrings, 512);
/* 129 */     this.cDataTagName = cDataTagName;
/* 130 */     this.convertNilAttributeToNull = convertNilAttributeToNull;
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
/*     */   private XMLParserConfiguration(boolean keepStrings, String cDataTagName, boolean convertNilAttributeToNull, Map<String, XMLXsiTypeConverter<?>> xsiTypeMap, Set<String> forceList, int maxNestingDepth) {
/* 149 */     super(keepStrings, maxNestingDepth);
/* 150 */     this.cDataTagName = cDataTagName;
/* 151 */     this.convertNilAttributeToNull = convertNilAttributeToNull;
/* 152 */     this.xsiTypeMap = Collections.unmodifiableMap(xsiTypeMap);
/* 153 */     this.forceList = Collections.unmodifiableSet(forceList);
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
/*     */   protected XMLParserConfiguration clone() {
/* 166 */     return new XMLParserConfiguration(this.keepStrings, this.cDataTagName, this.convertNilAttributeToNull, this.xsiTypeMap, this.forceList, this.maxNestingDepth);
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
/*     */   public XMLParserConfiguration withKeepStrings(boolean newVal) {
/* 187 */     return super.<XMLParserConfiguration>withKeepStrings(newVal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getcDataTagName() {
/* 198 */     return this.cDataTagName;
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
/*     */   public XMLParserConfiguration withcDataTagName(String newVal) {
/* 212 */     XMLParserConfiguration newConfig = clone();
/* 213 */     newConfig.cDataTagName = newVal;
/* 214 */     return newConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConvertNilAttributeToNull() {
/* 225 */     return this.convertNilAttributeToNull;
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
/*     */   public XMLParserConfiguration withConvertNilAttributeToNull(boolean newVal) {
/* 239 */     XMLParserConfiguration newConfig = clone();
/* 240 */     newConfig.convertNilAttributeToNull = newVal;
/* 241 */     return newConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, XMLXsiTypeConverter<?>> getXsiTypeMap() {
/* 252 */     return this.xsiTypeMap;
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
/*     */   public XMLParserConfiguration withXsiTypeMap(Map<String, XMLXsiTypeConverter<?>> xsiTypeMap) {
/* 265 */     XMLParserConfiguration newConfig = clone();
/* 266 */     Map<String, XMLXsiTypeConverter<?>> cloneXsiTypeMap = new HashMap<>(xsiTypeMap);
/* 267 */     newConfig.xsiTypeMap = Collections.unmodifiableMap(cloneXsiTypeMap);
/* 268 */     return newConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getForceList() {
/* 277 */     return this.forceList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLParserConfiguration withForceList(Set<String> forceList) {
/* 287 */     XMLParserConfiguration newConfig = clone();
/* 288 */     Set<String> cloneForceList = new HashSet<>(forceList);
/* 289 */     newConfig.forceList = Collections.unmodifiableSet(cloneForceList);
/* 290 */     return newConfig;
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
/*     */   public XMLParserConfiguration withMaxNestingDepth(int maxNestingDepth) {
/* 304 */     return super.<XMLParserConfiguration>withMaxNestingDepth(maxNestingDepth);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/json/XMLParserConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */