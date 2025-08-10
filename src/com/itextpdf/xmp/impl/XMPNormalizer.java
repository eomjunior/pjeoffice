/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPDateTime;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMeta;
/*     */ import com.itextpdf.xmp.XMPMetaFactory;
/*     */ import com.itextpdf.xmp.XMPUtils;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPath;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPathParser;
/*     */ import com.itextpdf.xmp.options.ParseOptions;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ import com.itextpdf.xmp.properties.XMPAliasInfo;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class XMPNormalizer
/*     */ {
/*     */   private static Map dcArrayForms;
/*     */   
/*     */   static {
/*  61 */     initDCArrays();
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
/*     */   static XMPMeta process(XMPMetaImpl xmp, ParseOptions options) throws XMPException {
/*  83 */     XMPNode tree = xmp.getRoot();
/*     */     
/*  85 */     touchUpDataModel(xmp);
/*  86 */     moveExplicitAliases(tree, options);
/*     */     
/*  88 */     tweakOldXMP(tree);
/*     */     
/*  90 */     deleteEmptySchemas(tree);
/*     */     
/*  92 */     return xmp;
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
/*     */   private static void tweakOldXMP(XMPNode tree) throws XMPException {
/* 111 */     if (tree.getName() != null && tree.getName().length() >= 36) {
/*     */       
/* 113 */       String nameStr = tree.getName().toLowerCase();
/* 114 */       if (nameStr.startsWith("uuid:"))
/*     */       {
/* 116 */         nameStr = nameStr.substring(5);
/*     */       }
/*     */       
/* 119 */       if (Utils.checkUUIDFormat(nameStr)) {
/*     */ 
/*     */         
/* 122 */         XMPPath path = XMPPathParser.expandXPath("http://ns.adobe.com/xap/1.0/mm/", "InstanceID");
/* 123 */         XMPNode idNode = XMPNodeUtils.findNode(tree, path, true, null);
/* 124 */         if (idNode != null) {
/*     */           
/* 126 */           idNode.setOptions(null);
/* 127 */           idNode.setValue("uuid:" + nameStr);
/* 128 */           idNode.removeChildren();
/* 129 */           idNode.removeQualifiers();
/* 130 */           tree.setName(null);
/*     */         }
/*     */         else {
/*     */           
/* 134 */           throw new XMPException("Failure creating xmpMM:InstanceID", 9);
/*     */         } 
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
/*     */   private static void touchUpDataModel(XMPMetaImpl xmp) throws XMPException {
/* 152 */     XMPNodeUtils.findSchemaNode(xmp.getRoot(), "http://purl.org/dc/elements/1.1/", true);
/*     */ 
/*     */     
/* 155 */     for (Iterator<XMPNode> it = xmp.getRoot().iterateChildren(); it.hasNext(); ) {
/*     */       
/* 157 */       XMPNode currSchema = it.next();
/* 158 */       if ("http://purl.org/dc/elements/1.1/".equals(currSchema.getName())) {
/*     */         
/* 160 */         normalizeDCArrays(currSchema); continue;
/*     */       } 
/* 162 */       if ("http://ns.adobe.com/exif/1.0/".equals(currSchema.getName())) {
/*     */ 
/*     */         
/* 165 */         fixGPSTimeStamp(currSchema);
/* 166 */         XMPNode arrayNode = XMPNodeUtils.findChildNode(currSchema, "exif:UserComment", false);
/*     */         
/* 168 */         if (arrayNode != null)
/*     */         {
/* 170 */           repairAltText(arrayNode); } 
/*     */         continue;
/*     */       } 
/* 173 */       if ("http://ns.adobe.com/xmp/1.0/DynamicMedia/".equals(currSchema.getName())) {
/*     */ 
/*     */ 
/*     */         
/* 177 */         XMPNode dmCopyright = XMPNodeUtils.findChildNode(currSchema, "xmpDM:copyright", false);
/*     */         
/* 179 */         if (dmCopyright != null)
/*     */         {
/* 181 */           migrateAudioCopyright(xmp, dmCopyright); } 
/*     */         continue;
/*     */       } 
/* 184 */       if ("http://ns.adobe.com/xap/1.0/rights/".equals(currSchema.getName())) {
/*     */         
/* 186 */         XMPNode arrayNode = XMPNodeUtils.findChildNode(currSchema, "xmpRights:UsageTerms", false);
/*     */         
/* 188 */         if (arrayNode != null)
/*     */         {
/* 190 */           repairAltText(arrayNode);
/*     */         }
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
/*     */   private static void normalizeDCArrays(XMPNode dcSchema) throws XMPException {
/* 209 */     for (int i = 1; i <= dcSchema.getChildrenLength(); i++) {
/*     */       
/* 211 */       XMPNode currProp = dcSchema.getChild(i);
/*     */       
/* 213 */       PropertyOptions arrayForm = (PropertyOptions)dcArrayForms.get(currProp.getName());
/* 214 */       if (arrayForm != null)
/*     */       {
/*     */ 
/*     */         
/* 218 */         if (currProp.getOptions().isSimple()) {
/*     */ 
/*     */ 
/*     */           
/* 222 */           XMPNode newArray = new XMPNode(currProp.getName(), arrayForm);
/* 223 */           currProp.setName("[]");
/* 224 */           newArray.addChild(currProp);
/* 225 */           dcSchema.replaceChild(i, newArray);
/*     */ 
/*     */           
/* 228 */           if (arrayForm.isArrayAltText() && !currProp.getOptions().getHasLanguage())
/*     */           {
/* 230 */             XMPNode newLang = new XMPNode("xml:lang", "x-default", null);
/* 231 */             currProp.addQualifier(newLang);
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 237 */           currProp.getOptions().setOption(7680, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 243 */           currProp.getOptions().mergeWith(arrayForm);
/*     */           
/* 245 */           if (arrayForm.isArrayAltText())
/*     */           {
/*     */             
/* 248 */             repairAltText(currProp);
/*     */           }
/*     */         } 
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
/*     */   private static void repairAltText(XMPNode arrayNode) throws XMPException {
/* 265 */     if (arrayNode == null || 
/* 266 */       !arrayNode.getOptions().isArray()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     arrayNode.getOptions().setArrayOrdered(true).setArrayAlternate(true).setArrayAltText(true);
/*     */     
/* 275 */     for (Iterator<XMPNode> it = arrayNode.iterateChildren(); it.hasNext(); ) {
/*     */       
/* 277 */       XMPNode currChild = it.next();
/* 278 */       if (currChild.getOptions().isCompositeProperty()) {
/*     */ 
/*     */         
/* 281 */         it.remove(); continue;
/*     */       } 
/* 283 */       if (!currChild.getOptions().getHasLanguage()) {
/*     */         
/* 285 */         String childValue = currChild.getValue();
/* 286 */         if (childValue == null || childValue.length() == 0) {
/*     */ 
/*     */           
/* 289 */           it.remove();
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 294 */         XMPNode repairLang = new XMPNode("xml:lang", "x-repair", null);
/* 295 */         currChild.addQualifier(repairLang);
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
/*     */   private static void moveExplicitAliases(XMPNode tree, ParseOptions options) throws XMPException {
/* 314 */     if (!tree.getHasAliases()) {
/*     */       return;
/*     */     }
/*     */     
/* 318 */     tree.setHasAliases(false);
/*     */     
/* 320 */     boolean strictAliasing = options.getStrictAliasing();
/*     */     
/* 322 */     for (Iterator<XMPNode> schemaIt = tree.getUnmodifiableChildren().iterator(); schemaIt.hasNext(); ) {
/*     */       
/* 324 */       XMPNode currSchema = schemaIt.next();
/* 325 */       if (!currSchema.getHasAliases()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 330 */       for (Iterator<XMPNode> propertyIt = currSchema.iterateChildren(); propertyIt.hasNext(); ) {
/*     */         
/* 332 */         XMPNode currProp = propertyIt.next();
/*     */         
/* 334 */         if (!currProp.isAlias()) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 339 */         currProp.setAlias(false);
/*     */ 
/*     */ 
/*     */         
/* 343 */         XMPAliasInfo info = XMPMetaFactory.getSchemaRegistry().findAlias(currProp.getName());
/* 344 */         if (info != null) {
/*     */ 
/*     */           
/* 347 */           XMPNode baseSchema = XMPNodeUtils.findSchemaNode(tree, info
/* 348 */               .getNamespace(), null, true);
/* 349 */           baseSchema.setImplicit(false);
/*     */ 
/*     */           
/* 352 */           XMPNode baseNode = XMPNodeUtils.findChildNode(baseSchema, info
/* 353 */               .getPrefix() + info.getPropName(), false);
/* 354 */           if (baseNode == null) {
/*     */             
/* 356 */             if (info.getAliasForm().isSimple()) {
/*     */ 
/*     */ 
/*     */               
/* 360 */               String qname = info.getPrefix() + info.getPropName();
/* 361 */               currProp.setName(qname);
/* 362 */               baseSchema.addChild(currProp);
/*     */               
/* 364 */               propertyIt.remove();
/*     */ 
/*     */               
/*     */               continue;
/*     */             } 
/*     */ 
/*     */             
/* 371 */             baseNode = new XMPNode(info.getPrefix() + info.getPropName(), info.getAliasForm().toPropertyOptions());
/* 372 */             baseSchema.addChild(baseNode);
/* 373 */             transplantArrayItemAlias(propertyIt, currProp, baseNode);
/*     */             
/*     */             continue;
/*     */           } 
/* 377 */           if (info.getAliasForm().isSimple()) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 382 */             if (strictAliasing)
/*     */             {
/* 384 */               compareAliasedSubtrees(currProp, baseNode, true);
/*     */             }
/*     */             
/* 387 */             propertyIt.remove();
/*     */ 
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */ 
/*     */           
/* 395 */           XMPNode itemNode = null;
/* 396 */           if (info.getAliasForm().isArrayAltText()) {
/*     */             
/* 398 */             int xdIndex = XMPNodeUtils.lookupLanguageItem(baseNode, "x-default");
/*     */             
/* 400 */             if (xdIndex != -1)
/*     */             {
/* 402 */               itemNode = baseNode.getChild(xdIndex);
/*     */             }
/*     */           }
/* 405 */           else if (baseNode.hasChildren()) {
/*     */             
/* 407 */             itemNode = baseNode.getChild(1);
/*     */           } 
/*     */           
/* 410 */           if (itemNode == null) {
/*     */             
/* 412 */             transplantArrayItemAlias(propertyIt, currProp, baseNode);
/*     */             
/*     */             continue;
/*     */           } 
/* 416 */           if (strictAliasing)
/*     */           {
/* 418 */             compareAliasedSubtrees(currProp, itemNode, true);
/*     */           }
/*     */           
/* 421 */           propertyIt.remove();
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 426 */       currSchema.setHasAliases(false);
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
/*     */   private static void transplantArrayItemAlias(Iterator propertyIt, XMPNode childNode, XMPNode baseArray) throws XMPException {
/* 441 */     if (baseArray.getOptions().isArrayAltText()) {
/*     */       
/* 443 */       if (childNode.getOptions().getHasLanguage())
/*     */       {
/* 445 */         throw new XMPException("Alias to x-default already has a language qualifier", 203);
/*     */       }
/*     */ 
/*     */       
/* 449 */       XMPNode langQual = new XMPNode("xml:lang", "x-default", null);
/* 450 */       childNode.addQualifier(langQual);
/*     */     } 
/*     */     
/* 453 */     propertyIt.remove();
/* 454 */     childNode.setName("[]");
/* 455 */     baseArray.addChild(childNode);
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
/*     */   private static void fixGPSTimeStamp(XMPNode exifSchema) throws XMPException {
/* 469 */     XMPNode gpsDateTime = XMPNodeUtils.findChildNode(exifSchema, "exif:GPSTimeStamp", false);
/* 470 */     if (gpsDateTime == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 480 */       XMPDateTime binGPSStamp = XMPUtils.convertToDate(gpsDateTime.getValue());
/* 481 */       if (binGPSStamp.getYear() != 0 || binGPSStamp
/* 482 */         .getMonth() != 0 || binGPSStamp
/* 483 */         .getDay() != 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 488 */       XMPNode otherDate = XMPNodeUtils.findChildNode(exifSchema, "exif:DateTimeOriginal", false);
/*     */       
/* 490 */       if (otherDate == null)
/*     */       {
/* 492 */         otherDate = XMPNodeUtils.findChildNode(exifSchema, "exif:DateTimeDigitized", false);
/*     */       }
/*     */       
/* 495 */       XMPDateTime binOtherDate = XMPUtils.convertToDate(otherDate.getValue());
/* 496 */       Calendar cal = binGPSStamp.getCalendar();
/* 497 */       cal.set(1, binOtherDate.getYear());
/* 498 */       cal.set(2, binOtherDate.getMonth());
/* 499 */       cal.set(5, binOtherDate.getDay());
/* 500 */       binGPSStamp = new XMPDateTimeImpl(cal);
/* 501 */       gpsDateTime.setValue(XMPUtils.convertFromDate(binGPSStamp));
/*     */     }
/* 503 */     catch (XMPException e) {
/*     */       return;
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
/*     */   private static void deleteEmptySchemas(XMPNode tree) {
/* 521 */     for (Iterator<XMPNode> it = tree.iterateChildren(); it.hasNext(); ) {
/*     */       
/* 523 */       XMPNode schema = it.next();
/* 524 */       if (!schema.hasChildren())
/*     */       {
/* 526 */         it.remove();
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
/*     */   private static void compareAliasedSubtrees(XMPNode aliasNode, XMPNode baseNode, boolean outerCall) throws XMPException {
/* 545 */     if (!aliasNode.getValue().equals(baseNode.getValue()) || aliasNode
/* 546 */       .getChildrenLength() != baseNode.getChildrenLength())
/*     */     {
/* 548 */       throw new XMPException("Mismatch between alias and base nodes", 203);
/*     */     }
/*     */     
/* 551 */     if (!outerCall && (
/*     */       
/* 553 */       !aliasNode.getName().equals(baseNode.getName()) || 
/* 554 */       !aliasNode.getOptions().equals(baseNode.getOptions()) || aliasNode
/* 555 */       .getQualifierLength() != baseNode.getQualifierLength()))
/*     */     {
/*     */       
/* 558 */       throw new XMPException("Mismatch between alias and base nodes", 203);
/*     */     }
/*     */ 
/*     */     
/* 562 */     Iterator<XMPNode> iterator1 = aliasNode.iterateChildren();
/* 563 */     Iterator<XMPNode> iterator2 = baseNode.iterateChildren();
/* 564 */     while (iterator1.hasNext() && iterator2.hasNext()) {
/*     */       
/* 566 */       XMPNode aliasChild = iterator1.next();
/* 567 */       XMPNode baseChild = iterator2.next();
/* 568 */       compareAliasedSubtrees(aliasChild, baseChild, false);
/*     */     } 
/*     */ 
/*     */     
/* 572 */     Iterator<XMPNode> an = aliasNode.iterateQualifier();
/* 573 */     Iterator<XMPNode> bn = baseNode.iterateQualifier();
/* 574 */     while (an.hasNext() && bn.hasNext()) {
/*     */       
/* 576 */       XMPNode aliasQual = an.next();
/* 577 */       XMPNode baseQual = bn.next();
/* 578 */       compareAliasedSubtrees(aliasQual, baseQual, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void migrateAudioCopyright(XMPMeta xmp, XMPNode dmCopyright) {
/*     */     try {
/* 615 */       XMPNode dcSchema = XMPNodeUtils.findSchemaNode(((XMPMetaImpl)xmp)
/* 616 */           .getRoot(), "http://purl.org/dc/elements/1.1/", true);
/*     */       
/* 618 */       String dmValue = dmCopyright.getValue();
/* 619 */       String doubleLF = "\n\n";
/*     */       
/* 621 */       XMPNode dcRightsArray = XMPNodeUtils.findChildNode(dcSchema, "dc:rights", false);
/*     */       
/* 623 */       if (dcRightsArray == null || !dcRightsArray.hasChildren()) {
/*     */ 
/*     */         
/* 626 */         dmValue = doubleLF + dmValue;
/* 627 */         xmp.setLocalizedText("http://purl.org/dc/elements/1.1/", "rights", "", "x-default", dmValue, null);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 632 */         int xdIndex = XMPNodeUtils.lookupLanguageItem(dcRightsArray, "x-default");
/*     */         
/* 634 */         if (xdIndex < 0) {
/*     */ 
/*     */           
/* 637 */           String firstValue = dcRightsArray.getChild(1).getValue();
/* 638 */           xmp.setLocalizedText("http://purl.org/dc/elements/1.1/", "rights", "", "x-default", firstValue, null);
/*     */           
/* 640 */           xdIndex = XMPNodeUtils.lookupLanguageItem(dcRightsArray, "x-default");
/*     */         } 
/*     */ 
/*     */         
/* 644 */         XMPNode defaultNode = dcRightsArray.getChild(xdIndex);
/* 645 */         String defaultValue = defaultNode.getValue();
/* 646 */         int lfPos = defaultValue.indexOf(doubleLF);
/*     */         
/* 648 */         if (lfPos < 0) {
/*     */ 
/*     */           
/* 651 */           if (!dmValue.equals(defaultValue))
/*     */           {
/*     */ 
/*     */             
/* 655 */             defaultNode.setValue(defaultValue + doubleLF + dmValue);
/*     */ 
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 661 */         else if (!defaultValue.substring(lfPos + 2).equals(dmValue)) {
/*     */ 
/*     */           
/* 664 */           defaultNode.setValue(defaultValue.substring(0, lfPos + 2) + dmValue);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 671 */       dmCopyright.getParent().removeChild(dmCopyright);
/*     */     }
/* 673 */     catch (XMPException xMPException) {}
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
/*     */   private static void initDCArrays() {
/* 687 */     dcArrayForms = new HashMap<Object, Object>();
/*     */ 
/*     */     
/* 690 */     PropertyOptions bagForm = new PropertyOptions();
/* 691 */     bagForm.setArray(true);
/* 692 */     dcArrayForms.put("dc:contributor", bagForm);
/* 693 */     dcArrayForms.put("dc:language", bagForm);
/* 694 */     dcArrayForms.put("dc:publisher", bagForm);
/* 695 */     dcArrayForms.put("dc:relation", bagForm);
/* 696 */     dcArrayForms.put("dc:subject", bagForm);
/* 697 */     dcArrayForms.put("dc:type", bagForm);
/*     */ 
/*     */     
/* 700 */     PropertyOptions seqForm = new PropertyOptions();
/* 701 */     seqForm.setArray(true);
/* 702 */     seqForm.setArrayOrdered(true);
/* 703 */     dcArrayForms.put("dc:creator", seqForm);
/* 704 */     dcArrayForms.put("dc:date", seqForm);
/*     */ 
/*     */     
/* 707 */     PropertyOptions altTextForm = new PropertyOptions();
/* 708 */     altTextForm.setArray(true);
/* 709 */     altTextForm.setArrayOrdered(true);
/* 710 */     altTextForm.setArrayAlternate(true);
/* 711 */     altTextForm.setArrayAltText(true);
/* 712 */     dcArrayForms.put("dc:description", altTextForm);
/* 713 */     dcArrayForms.put("dc:rights", altTextForm);
/* 714 */     dcArrayForms.put("dc:title", altTextForm);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPNormalizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */