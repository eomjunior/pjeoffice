/*     */ package com.itextpdf.xmp.impl.xpath;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMetaFactory;
/*     */ import com.itextpdf.xmp.impl.Utils;
/*     */ import com.itextpdf.xmp.properties.XMPAliasInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMPPathParser
/*     */ {
/*     */   public static XMPPath expandXPath(String schemaNS, String path) throws XMPException {
/* 107 */     if (schemaNS == null || path == null)
/*     */     {
/* 109 */       throw new XMPException("Parameter must not be null", 4);
/*     */     }
/*     */     
/* 112 */     XMPPath expandedXPath = new XMPPath();
/* 113 */     PathPosition pos = new PathPosition();
/* 114 */     pos.path = path;
/*     */ 
/*     */ 
/*     */     
/* 118 */     parseRootNode(schemaNS, pos, expandedXPath);
/*     */ 
/*     */     
/* 121 */     while (pos.stepEnd < path.length()) {
/*     */       XMPPathSegment segment;
/* 123 */       pos.stepBegin = pos.stepEnd;
/*     */       
/* 125 */       skipPathDelimiter(path, pos);
/*     */       
/* 127 */       pos.stepEnd = pos.stepBegin;
/*     */ 
/*     */ 
/*     */       
/* 131 */       if (path.charAt(pos.stepBegin) != '[') {
/*     */ 
/*     */         
/* 134 */         segment = parseStructSegment(pos);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 139 */         segment = parseIndexSegment(pos);
/*     */       } 
/*     */ 
/*     */       
/* 143 */       if (segment.getKind() == 1) {
/*     */         
/* 145 */         if (segment.getName().charAt(0) == '@') {
/*     */           
/* 147 */           segment.setName("?" + segment.getName().substring(1));
/* 148 */           if (!"?xml:lang".equals(segment.getName()))
/*     */           {
/* 150 */             throw new XMPException("Only xml:lang allowed with '@'", 102);
/*     */           }
/*     */         } 
/*     */         
/* 154 */         if (segment.getName().charAt(0) == '?') {
/*     */           
/* 156 */           pos.nameStart++;
/* 157 */           segment.setKind(2);
/*     */         } 
/*     */         
/* 160 */         verifyQualName(pos.path.substring(pos.nameStart, pos.nameEnd));
/*     */       }
/* 162 */       else if (segment.getKind() == 6) {
/*     */         
/* 164 */         if (segment.getName().charAt(1) == '@') {
/*     */           
/* 166 */           segment.setName("[?" + segment.getName().substring(2));
/* 167 */           if (!segment.getName().startsWith("[?xml:lang="))
/*     */           {
/* 169 */             throw new XMPException("Only xml:lang allowed with '@'", 102);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 174 */         if (segment.getName().charAt(1) == '?') {
/*     */           
/* 176 */           pos.nameStart++;
/* 177 */           segment.setKind(5);
/* 178 */           verifyQualName(pos.path.substring(pos.nameStart, pos.nameEnd));
/*     */         } 
/*     */       } 
/*     */       
/* 182 */       expandedXPath.add(segment);
/*     */     } 
/* 184 */     return expandedXPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void skipPathDelimiter(String path, PathPosition pos) throws XMPException {
/* 195 */     if (path.charAt(pos.stepBegin) == '/') {
/*     */ 
/*     */ 
/*     */       
/* 199 */       pos.stepBegin++;
/*     */ 
/*     */       
/* 202 */       if (pos.stepBegin >= path.length())
/*     */       {
/* 204 */         throw new XMPException("Empty XMPPath segment", 102);
/*     */       }
/*     */     } 
/*     */     
/* 208 */     if (path.charAt(pos.stepBegin) == '*') {
/*     */ 
/*     */ 
/*     */       
/* 212 */       pos.stepBegin++;
/* 213 */       if (pos.stepBegin >= path.length() || path.charAt(pos.stepBegin) != '[')
/*     */       {
/* 215 */         throw new XMPException("Missing '[' after '*'", 102);
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
/*     */   private static XMPPathSegment parseStructSegment(PathPosition pos) throws XMPException {
/* 229 */     pos.nameStart = pos.stepBegin;
/* 230 */     while (pos.stepEnd < pos.path.length() && "/[*".indexOf(pos.path.charAt(pos.stepEnd)) < 0)
/*     */     {
/* 232 */       pos.stepEnd++;
/*     */     }
/* 234 */     pos.nameEnd = pos.stepEnd;
/*     */     
/* 236 */     if (pos.stepEnd == pos.stepBegin)
/*     */     {
/* 238 */       throw new XMPException("Empty XMPPath segment", 102);
/*     */     }
/*     */ 
/*     */     
/* 242 */     XMPPathSegment segment = new XMPPathSegment(pos.path.substring(pos.stepBegin, pos.stepEnd), 1);
/*     */     
/* 244 */     return segment;
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
/*     */   private static XMPPathSegment parseIndexSegment(PathPosition pos) throws XMPException {
/*     */     XMPPathSegment segment;
/* 259 */     pos.stepEnd++;
/*     */     
/* 261 */     if ('0' <= pos.path.charAt(pos.stepEnd) && pos.path.charAt(pos.stepEnd) <= '9') {
/*     */ 
/*     */       
/* 264 */       while (pos.stepEnd < pos.path.length() && '0' <= pos.path.charAt(pos.stepEnd) && pos.path
/* 265 */         .charAt(pos.stepEnd) <= '9')
/*     */       {
/* 267 */         pos.stepEnd++;
/*     */       }
/*     */       
/* 270 */       segment = new XMPPathSegment(null, 3);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 276 */       while (pos.stepEnd < pos.path.length() && pos.path.charAt(pos.stepEnd) != ']' && pos.path
/* 277 */         .charAt(pos.stepEnd) != '=')
/*     */       {
/* 279 */         pos.stepEnd++;
/*     */       }
/*     */       
/* 282 */       if (pos.stepEnd >= pos.path.length())
/*     */       {
/* 284 */         throw new XMPException("Missing ']' or '=' for array index", 102);
/*     */       }
/*     */       
/* 287 */       if (pos.path.charAt(pos.stepEnd) == ']') {
/*     */         
/* 289 */         if (!"[last()".equals(pos.path.substring(pos.stepBegin, pos.stepEnd)))
/*     */         {
/* 291 */           throw new XMPException("Invalid non-numeric array index", 102);
/*     */         }
/*     */         
/* 294 */         segment = new XMPPathSegment(null, 4);
/*     */       }
/*     */       else {
/*     */         
/* 298 */         pos.nameStart = pos.stepBegin + 1;
/* 299 */         pos.nameEnd = pos.stepEnd;
/* 300 */         pos.stepEnd++;
/* 301 */         char quote = pos.path.charAt(pos.stepEnd);
/* 302 */         if (quote != '\'' && quote != '"')
/*     */         {
/* 304 */           throw new XMPException("Invalid quote in array selector", 102);
/*     */         }
/*     */ 
/*     */         
/* 308 */         pos.stepEnd++;
/* 309 */         while (pos.stepEnd < pos.path.length()) {
/*     */           
/* 311 */           if (pos.path.charAt(pos.stepEnd) == quote) {
/*     */ 
/*     */             
/* 314 */             if (pos.stepEnd + 1 >= pos.path.length() || pos.path
/* 315 */               .charAt(pos.stepEnd + 1) != quote) {
/*     */               break;
/*     */             }
/*     */             
/* 319 */             pos.stepEnd++;
/*     */           } 
/* 321 */           pos.stepEnd++;
/*     */         } 
/*     */         
/* 324 */         if (pos.stepEnd >= pos.path.length())
/*     */         {
/* 326 */           throw new XMPException("No terminating quote for array selector", 102);
/*     */         }
/*     */         
/* 329 */         pos.stepEnd++;
/*     */ 
/*     */         
/* 332 */         segment = new XMPPathSegment(null, 6);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 337 */     if (pos.stepEnd >= pos.path.length() || pos.path.charAt(pos.stepEnd) != ']')
/*     */     {
/* 339 */       throw new XMPException("Missing ']' for array index", 102);
/*     */     }
/* 341 */     pos.stepEnd++;
/* 342 */     segment.setName(pos.path.substring(pos.stepBegin, pos.stepEnd));
/*     */     
/* 344 */     return segment;
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
/*     */   private static void parseRootNode(String schemaNS, PathPosition pos, XMPPath expandedXPath) throws XMPException {
/* 359 */     while (pos.stepEnd < pos.path.length() && "/[*".indexOf(pos.path.charAt(pos.stepEnd)) < 0)
/*     */     {
/* 361 */       pos.stepEnd++;
/*     */     }
/*     */     
/* 364 */     if (pos.stepEnd == pos.stepBegin)
/*     */     {
/* 366 */       throw new XMPException("Empty initial XMPPath step", 102);
/*     */     }
/*     */     
/* 369 */     String rootProp = verifyXPathRoot(schemaNS, pos.path.substring(pos.stepBegin, pos.stepEnd));
/* 370 */     XMPAliasInfo aliasInfo = XMPMetaFactory.getSchemaRegistry().findAlias(rootProp);
/* 371 */     if (aliasInfo == null) {
/*     */ 
/*     */       
/* 374 */       expandedXPath.add(new XMPPathSegment(schemaNS, -2147483648));
/* 375 */       XMPPathSegment rootStep = new XMPPathSegment(rootProp, 1);
/* 376 */       expandedXPath.add(rootStep);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 381 */       expandedXPath.add(new XMPPathSegment(aliasInfo.getNamespace(), -2147483648));
/* 382 */       XMPPathSegment rootStep = new XMPPathSegment(verifyXPathRoot(aliasInfo.getNamespace(), aliasInfo
/* 383 */             .getPropName()), 1);
/*     */       
/* 385 */       rootStep.setAlias(true);
/* 386 */       rootStep.setAliasForm(aliasInfo.getAliasForm().getOptions());
/* 387 */       expandedXPath.add(rootStep);
/*     */       
/* 389 */       if (aliasInfo.getAliasForm().isArrayAltText()) {
/*     */         
/* 391 */         XMPPathSegment qualSelectorStep = new XMPPathSegment("[?xml:lang='x-default']", 5);
/*     */         
/* 393 */         qualSelectorStep.setAlias(true);
/* 394 */         qualSelectorStep.setAliasForm(aliasInfo.getAliasForm().getOptions());
/* 395 */         expandedXPath.add(qualSelectorStep);
/*     */       }
/* 397 */       else if (aliasInfo.getAliasForm().isArray()) {
/*     */         
/* 399 */         XMPPathSegment indexStep = new XMPPathSegment("[1]", 3);
/*     */         
/* 401 */         indexStep.setAlias(true);
/* 402 */         indexStep.setAliasForm(aliasInfo.getAliasForm().getOptions());
/* 403 */         expandedXPath.add(indexStep);
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
/*     */   private static void verifyQualName(String qualName) throws XMPException {
/* 420 */     int colonPos = qualName.indexOf(':');
/* 421 */     if (colonPos > 0) {
/*     */       
/* 423 */       String prefix = qualName.substring(0, colonPos);
/* 424 */       if (Utils.isXMLNameNS(prefix)) {
/*     */         
/* 426 */         String regURI = XMPMetaFactory.getSchemaRegistry().getNamespaceURI(prefix);
/*     */         
/* 428 */         if (regURI != null) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 433 */         throw new XMPException("Unknown namespace prefix for qualified name", 102);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 438 */     throw new XMPException("Ill-formed qualified name", 102);
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
/*     */   private static void verifySimpleXMLName(String name) throws XMPException {
/* 452 */     if (!Utils.isXMLName(name))
/*     */     {
/* 454 */       throw new XMPException("Bad XML name", 102);
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
/*     */   private static String verifyXPathRoot(String schemaNS, String rootProp) throws XMPException {
/* 479 */     if (schemaNS == null || schemaNS.length() == 0)
/*     */     {
/* 481 */       throw new XMPException("Schema namespace URI is required", 101);
/*     */     }
/*     */ 
/*     */     
/* 485 */     if (rootProp.charAt(0) == '?' || rootProp.charAt(0) == '@')
/*     */     {
/* 487 */       throw new XMPException("Top level name must not be a qualifier", 102);
/*     */     }
/*     */     
/* 490 */     if (rootProp.indexOf('/') >= 0 || rootProp.indexOf('[') >= 0)
/*     */     {
/* 492 */       throw new XMPException("Top level name must be simple", 102);
/*     */     }
/*     */     
/* 495 */     String prefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(schemaNS);
/* 496 */     if (prefix == null)
/*     */     {
/* 498 */       throw new XMPException("Unregistered schema namespace URI", 101);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 503 */     int colonPos = rootProp.indexOf(':');
/* 504 */     if (colonPos < 0) {
/*     */ 
/*     */ 
/*     */       
/* 508 */       verifySimpleXMLName(rootProp);
/* 509 */       return prefix + rootProp;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 517 */     verifySimpleXMLName(rootProp.substring(0, colonPos));
/* 518 */     verifySimpleXMLName(rootProp.substring(colonPos));
/*     */     
/* 520 */     prefix = rootProp.substring(0, colonPos + 1);
/*     */     
/* 522 */     String regPrefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(schemaNS);
/* 523 */     if (regPrefix == null)
/*     */     {
/* 525 */       throw new XMPException("Unknown schema namespace prefix", 101);
/*     */     }
/* 527 */     if (!prefix.equals(regPrefix))
/*     */     {
/* 529 */       throw new XMPException("Schema namespace URI and prefix mismatch", 101);
/*     */     }
/*     */ 
/*     */     
/* 533 */     return rootProp;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/xpath/XMPPathParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */