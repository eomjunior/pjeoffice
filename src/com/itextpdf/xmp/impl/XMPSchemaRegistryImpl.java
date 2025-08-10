/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPConst;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPSchemaRegistry;
/*     */ import com.itextpdf.xmp.options.AliasOptions;
/*     */ import com.itextpdf.xmp.properties.XMPAliasInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMPSchemaRegistryImpl
/*     */   implements XMPSchemaRegistry, XMPConst
/*     */ {
/*  59 */   private Map namespaceToPrefixMap = new HashMap<Object, Object>();
/*     */ 
/*     */   
/*  62 */   private Map prefixToNamespaceMap = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */   
/*  66 */   private Map aliasMap = new HashMap<Object, Object>();
/*     */   
/*  68 */   private Pattern p = Pattern.compile("[/*?\\[\\]]");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPSchemaRegistryImpl() {
/*     */     try {
/*  79 */       registerStandardNamespaces();
/*  80 */       registerStandardAliases();
/*     */     }
/*  82 */     catch (XMPException e) {
/*     */       
/*  84 */       throw new RuntimeException("The XMPSchemaRegistry cannot be initialized!");
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
/*     */   public synchronized String registerNamespace(String namespaceURI, String suggestedPrefix) throws XMPException {
/*  99 */     ParameterAsserts.assertSchemaNS(namespaceURI);
/* 100 */     ParameterAsserts.assertPrefix(suggestedPrefix);
/*     */     
/* 102 */     if (suggestedPrefix.charAt(suggestedPrefix.length() - 1) != ':')
/*     */     {
/* 104 */       suggestedPrefix = suggestedPrefix + ':';
/*     */     }
/*     */     
/* 107 */     if (!Utils.isXMLNameNS(suggestedPrefix.substring(0, suggestedPrefix
/* 108 */           .length() - 1)))
/*     */     {
/* 110 */       throw new XMPException("The prefix is a bad XML name", 201);
/*     */     }
/*     */     
/* 113 */     String registeredPrefix = (String)this.namespaceToPrefixMap.get(namespaceURI);
/* 114 */     String registeredNS = (String)this.prefixToNamespaceMap.get(suggestedPrefix);
/* 115 */     if (registeredPrefix != null)
/*     */     {
/*     */       
/* 118 */       return registeredPrefix;
/*     */     }
/*     */ 
/*     */     
/* 122 */     if (registeredNS != null) {
/*     */ 
/*     */ 
/*     */       
/* 126 */       String generatedPrefix = suggestedPrefix;
/* 127 */       for (int i = 1; this.prefixToNamespaceMap.containsKey(generatedPrefix); i++)
/*     */       {
/*     */         
/* 130 */         generatedPrefix = suggestedPrefix.substring(0, suggestedPrefix.length() - 1) + "_" + i + "_:";
/*     */       }
/*     */       
/* 133 */       suggestedPrefix = generatedPrefix;
/*     */     } 
/* 135 */     this.prefixToNamespaceMap.put(suggestedPrefix, namespaceURI);
/* 136 */     this.namespaceToPrefixMap.put(namespaceURI, suggestedPrefix);
/*     */ 
/*     */     
/* 139 */     return suggestedPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void deleteNamespace(String namespaceURI) {
/* 149 */     String prefixToDelete = getNamespacePrefix(namespaceURI);
/* 150 */     if (prefixToDelete != null) {
/*     */       
/* 152 */       this.namespaceToPrefixMap.remove(namespaceURI);
/* 153 */       this.prefixToNamespaceMap.remove(prefixToDelete);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getNamespacePrefix(String namespaceURI) {
/* 163 */     return (String)this.namespaceToPrefixMap.get(namespaceURI);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getNamespaceURI(String namespacePrefix) {
/* 172 */     if (namespacePrefix != null && !namespacePrefix.endsWith(":"))
/*     */     {
/* 174 */       namespacePrefix = namespacePrefix + ":";
/*     */     }
/* 176 */     return (String)this.prefixToNamespaceMap.get(namespacePrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Map getNamespaces() {
/* 185 */     return Collections.unmodifiableMap(new TreeMap<Object, Object>(this.namespaceToPrefixMap));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Map getPrefixes() {
/* 194 */     return Collections.unmodifiableMap(new TreeMap<Object, Object>(this.prefixToNamespaceMap));
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
/*     */   private void registerStandardNamespaces() throws XMPException {
/* 208 */     registerNamespace("http://www.w3.org/XML/1998/namespace", "xml");
/* 209 */     registerNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
/* 210 */     registerNamespace("http://purl.org/dc/elements/1.1/", "dc");
/* 211 */     registerNamespace("http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/", "Iptc4xmpCore");
/* 212 */     registerNamespace("http://iptc.org/std/Iptc4xmpExt/2008-02-29/", "Iptc4xmpExt");
/* 213 */     registerNamespace("http://ns.adobe.com/DICOM/", "DICOM");
/* 214 */     registerNamespace("http://ns.useplus.org/ldf/xmp/1.0/", "plus");
/*     */ 
/*     */     
/* 217 */     registerNamespace("adobe:ns:meta/", "x");
/* 218 */     registerNamespace("http://ns.adobe.com/iX/1.0/", "iX");
/*     */     
/* 220 */     registerNamespace("http://ns.adobe.com/xap/1.0/", "xmp");
/* 221 */     registerNamespace("http://ns.adobe.com/xap/1.0/rights/", "xmpRights");
/* 222 */     registerNamespace("http://ns.adobe.com/xap/1.0/mm/", "xmpMM");
/* 223 */     registerNamespace("http://ns.adobe.com/xap/1.0/bj/", "xmpBJ");
/* 224 */     registerNamespace("http://ns.adobe.com/xmp/note/", "xmpNote");
/*     */     
/* 226 */     registerNamespace("http://ns.adobe.com/pdf/1.3/", "pdf");
/* 227 */     registerNamespace("http://ns.adobe.com/pdfx/1.3/", "pdfx");
/* 228 */     registerNamespace("http://www.npes.org/pdfx/ns/id/", "pdfxid");
/* 229 */     registerNamespace("http://www.aiim.org/pdfa/ns/schema#", "pdfaSchema");
/* 230 */     registerNamespace("http://www.aiim.org/pdfa/ns/property#", "pdfaProperty");
/* 231 */     registerNamespace("http://www.aiim.org/pdfa/ns/type#", "pdfaType");
/* 232 */     registerNamespace("http://www.aiim.org/pdfa/ns/field#", "pdfaField");
/* 233 */     registerNamespace("http://www.aiim.org/pdfa/ns/id/", "pdfaid");
/* 234 */     registerNamespace("http://www.aiim.org/pdfua/ns/id/", "pdfuaid");
/* 235 */     registerNamespace("http://www.aiim.org/pdfa/ns/extension/", "pdfaExtension");
/* 236 */     registerNamespace("http://ns.adobe.com/photoshop/1.0/", "photoshop");
/* 237 */     registerNamespace("http://ns.adobe.com/album/1.0/", "album");
/* 238 */     registerNamespace("http://ns.adobe.com/exif/1.0/", "exif");
/* 239 */     registerNamespace("http://cipa.jp/exif/1.0/", "exifEX");
/* 240 */     registerNamespace("http://ns.adobe.com/exif/1.0/aux/", "aux");
/* 241 */     registerNamespace("http://ns.adobe.com/tiff/1.0/", "tiff");
/* 242 */     registerNamespace("http://ns.adobe.com/png/1.0/", "png");
/* 243 */     registerNamespace("http://ns.adobe.com/jpeg/1.0/", "jpeg");
/* 244 */     registerNamespace("http://ns.adobe.com/jp2k/1.0/", "jp2k");
/* 245 */     registerNamespace("http://ns.adobe.com/camera-raw-settings/1.0/", "crs");
/* 246 */     registerNamespace("http://ns.adobe.com/StockPhoto/1.0/", "bmsp");
/* 247 */     registerNamespace("http://ns.adobe.com/creatorAtom/1.0/", "creatorAtom");
/* 248 */     registerNamespace("http://ns.adobe.com/asf/1.0/", "asf");
/* 249 */     registerNamespace("http://ns.adobe.com/xmp/wav/1.0/", "wav");
/* 250 */     registerNamespace("http://ns.adobe.com/bwf/bext/1.0/", "bext");
/* 251 */     registerNamespace("http://ns.adobe.com/riff/info/", "riffinfo");
/* 252 */     registerNamespace("http://ns.adobe.com/xmp/1.0/Script/", "xmpScript");
/* 253 */     registerNamespace("http://ns.adobe.com/TransformXMP/", "txmp");
/* 254 */     registerNamespace("http://ns.adobe.com/swf/1.0/", "swf");
/*     */ 
/*     */     
/* 257 */     registerNamespace("http://ns.adobe.com/xmp/1.0/DynamicMedia/", "xmpDM");
/* 258 */     registerNamespace("http://ns.adobe.com/xmp/transient/1.0/", "xmpx");
/*     */ 
/*     */     
/* 261 */     registerNamespace("http://ns.adobe.com/xap/1.0/t/", "xmpT");
/* 262 */     registerNamespace("http://ns.adobe.com/xap/1.0/t/pg/", "xmpTPg");
/* 263 */     registerNamespace("http://ns.adobe.com/xap/1.0/g/", "xmpG");
/* 264 */     registerNamespace("http://ns.adobe.com/xap/1.0/g/img/", "xmpGImg");
/* 265 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/Font#", "stFnt");
/* 266 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/Dimensions#", "stDim");
/* 267 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/ResourceEvent#", "stEvt");
/* 268 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/ResourceRef#", "stRef");
/* 269 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/Version#", "stVer");
/* 270 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/Job#", "stJob");
/* 271 */     registerNamespace("http://ns.adobe.com/xap/1.0/sType/ManifestItem#", "stMfs");
/* 272 */     registerNamespace("http://ns.adobe.com/xmp/Identifier/qual/1.0/", "xmpidq");
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
/*     */   public synchronized XMPAliasInfo resolveAlias(String aliasNS, String aliasProp) {
/* 286 */     String aliasPrefix = getNamespacePrefix(aliasNS);
/* 287 */     if (aliasPrefix == null)
/*     */     {
/* 289 */       return null;
/*     */     }
/*     */     
/* 292 */     return (XMPAliasInfo)this.aliasMap.get(aliasPrefix + aliasProp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized XMPAliasInfo findAlias(String qname) {
/* 301 */     return (XMPAliasInfo)this.aliasMap.get(qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized XMPAliasInfo[] findAliases(String aliasNS) {
/* 310 */     String prefix = getNamespacePrefix(aliasNS);
/* 311 */     List<XMPAliasInfo> result = new ArrayList();
/* 312 */     if (prefix != null)
/*     */     {
/* 314 */       for (Iterator<String> it = this.aliasMap.keySet().iterator(); it.hasNext(); ) {
/*     */         
/* 316 */         String qname = it.next();
/* 317 */         if (qname.startsWith(prefix))
/*     */         {
/* 319 */           result.add(findAlias(qname));
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 324 */     return result.<XMPAliasInfo>toArray(new XMPAliasInfo[result.size()]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void registerAlias(String aliasNS, String aliasProp, final String actualNS, final String actualProp, AliasOptions aliasForm) throws XMPException {
/* 367 */     ParameterAsserts.assertSchemaNS(aliasNS);
/* 368 */     ParameterAsserts.assertPropName(aliasProp);
/* 369 */     ParameterAsserts.assertSchemaNS(actualNS);
/* 370 */     ParameterAsserts.assertPropName(actualProp);
/*     */ 
/*     */     
/* 373 */     if (aliasForm != null) {  }
/*     */     else {  }
/* 375 */      final AliasOptions aliasOpts = new AliasOptions();
/*     */ 
/*     */     
/* 378 */     if (this.p.matcher(aliasProp).find() || this.p.matcher(actualProp).find())
/*     */     {
/* 380 */       throw new XMPException("Alias and actual property names must be simple", 102);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 385 */     String aliasPrefix = getNamespacePrefix(aliasNS);
/* 386 */     final String actualPrefix = getNamespacePrefix(actualNS);
/* 387 */     if (aliasPrefix == null)
/*     */     {
/* 389 */       throw new XMPException("Alias namespace is not registered", 101);
/*     */     }
/* 391 */     if (actualPrefix == null)
/*     */     {
/* 393 */       throw new XMPException("Actual namespace is not registered", 101);
/*     */     }
/*     */ 
/*     */     
/* 397 */     String key = aliasPrefix + aliasProp;
/*     */ 
/*     */     
/* 400 */     if (this.aliasMap.containsKey(key))
/*     */     {
/* 402 */       throw new XMPException("Alias is already existing", 4);
/*     */     }
/* 404 */     if (this.aliasMap.containsKey(actualPrefix + actualProp))
/*     */     {
/* 406 */       throw new XMPException("Actual property is already an alias, use the base property", 4);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 411 */     XMPAliasInfo aliasInfo = new XMPAliasInfo()
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public String getNamespace()
/*     */         {
/* 418 */           return actualNS;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public String getPrefix() {
/* 426 */           return actualPrefix;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public String getPropName() {
/* 434 */           return actualProp;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public AliasOptions getAliasForm() {
/* 442 */           return aliasOpts;
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 447 */           return actualPrefix + actualProp + " NS(" + actualNS + "), FORM (" + 
/* 448 */             getAliasForm() + ")";
/*     */         }
/*     */       };
/*     */     
/* 452 */     this.aliasMap.put(key, aliasInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Map getAliases() {
/* 461 */     return Collections.unmodifiableMap(new TreeMap<Object, Object>(this.aliasMap));
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
/*     */   private void registerStandardAliases() throws XMPException {
/* 473 */     AliasOptions aliasToArrayOrdered = (new AliasOptions()).setArrayOrdered(true);
/* 474 */     AliasOptions aliasToArrayAltText = (new AliasOptions()).setArrayAltText(true);
/*     */ 
/*     */ 
/*     */     
/* 478 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Author", "http://purl.org/dc/elements/1.1/", "creator", aliasToArrayOrdered);
/* 479 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Authors", "http://purl.org/dc/elements/1.1/", "creator", null);
/* 480 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Description", "http://purl.org/dc/elements/1.1/", "description", null);
/* 481 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Format", "http://purl.org/dc/elements/1.1/", "format", null);
/* 482 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Keywords", "http://purl.org/dc/elements/1.1/", "subject", null);
/* 483 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Locale", "http://purl.org/dc/elements/1.1/", "language", null);
/* 484 */     registerAlias("http://ns.adobe.com/xap/1.0/", "Title", "http://purl.org/dc/elements/1.1/", "title", null);
/* 485 */     registerAlias("http://ns.adobe.com/xap/1.0/rights/", "Copyright", "http://purl.org/dc/elements/1.1/", "rights", null);
/*     */ 
/*     */     
/* 488 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "Author", "http://purl.org/dc/elements/1.1/", "creator", aliasToArrayOrdered);
/* 489 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "BaseURL", "http://ns.adobe.com/xap/1.0/", "BaseURL", null);
/* 490 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "CreationDate", "http://ns.adobe.com/xap/1.0/", "CreateDate", null);
/* 491 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "Creator", "http://ns.adobe.com/xap/1.0/", "CreatorTool", null);
/* 492 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "ModDate", "http://ns.adobe.com/xap/1.0/", "ModifyDate", null);
/* 493 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "Subject", "http://purl.org/dc/elements/1.1/", "description", aliasToArrayAltText);
/* 494 */     registerAlias("http://ns.adobe.com/pdf/1.3/", "Title", "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
/*     */ 
/*     */     
/* 497 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "Author", "http://purl.org/dc/elements/1.1/", "creator", aliasToArrayOrdered);
/* 498 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "Caption", "http://purl.org/dc/elements/1.1/", "description", aliasToArrayAltText);
/* 499 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "Copyright", "http://purl.org/dc/elements/1.1/", "rights", aliasToArrayAltText);
/* 500 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "Keywords", "http://purl.org/dc/elements/1.1/", "subject", null);
/* 501 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "Marked", "http://ns.adobe.com/xap/1.0/rights/", "Marked", null);
/* 502 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "Title", "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
/* 503 */     registerAlias("http://ns.adobe.com/photoshop/1.0/", "WebStatement", "http://ns.adobe.com/xap/1.0/rights/", "WebStatement", null);
/*     */ 
/*     */     
/* 506 */     registerAlias("http://ns.adobe.com/tiff/1.0/", "Artist", "http://purl.org/dc/elements/1.1/", "creator", aliasToArrayOrdered);
/* 507 */     registerAlias("http://ns.adobe.com/tiff/1.0/", "Copyright", "http://purl.org/dc/elements/1.1/", "rights", null);
/* 508 */     registerAlias("http://ns.adobe.com/tiff/1.0/", "DateTime", "http://ns.adobe.com/xap/1.0/", "ModifyDate", null);
/* 509 */     registerAlias("http://ns.adobe.com/tiff/1.0/", "ImageDescription", "http://purl.org/dc/elements/1.1/", "description", null);
/* 510 */     registerAlias("http://ns.adobe.com/tiff/1.0/", "Software", "http://ns.adobe.com/xap/1.0/", "CreatorTool", null);
/*     */ 
/*     */     
/* 513 */     registerAlias("http://ns.adobe.com/png/1.0/", "Author", "http://purl.org/dc/elements/1.1/", "creator", aliasToArrayOrdered);
/* 514 */     registerAlias("http://ns.adobe.com/png/1.0/", "Copyright", "http://purl.org/dc/elements/1.1/", "rights", aliasToArrayAltText);
/* 515 */     registerAlias("http://ns.adobe.com/png/1.0/", "CreationTime", "http://ns.adobe.com/xap/1.0/", "CreateDate", null);
/* 516 */     registerAlias("http://ns.adobe.com/png/1.0/", "Description", "http://purl.org/dc/elements/1.1/", "description", aliasToArrayAltText);
/* 517 */     registerAlias("http://ns.adobe.com/png/1.0/", "ModificationTime", "http://ns.adobe.com/xap/1.0/", "ModifyDate", null);
/* 518 */     registerAlias("http://ns.adobe.com/png/1.0/", "Software", "http://ns.adobe.com/xap/1.0/", "CreatorTool", null);
/* 519 */     registerAlias("http://ns.adobe.com/png/1.0/", "Title", "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPSchemaRegistryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */