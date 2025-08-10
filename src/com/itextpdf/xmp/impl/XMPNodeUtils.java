/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPConst;
/*     */ import com.itextpdf.xmp.XMPDateTime;
/*     */ import com.itextpdf.xmp.XMPDateTimeFactory;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMetaFactory;
/*     */ import com.itextpdf.xmp.XMPUtils;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPath;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPathSegment;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMPNodeUtils
/*     */   implements XMPConst
/*     */ {
/*     */   static final int CLT_NO_VALUES = 0;
/*     */   static final int CLT_SPECIFIC_MATCH = 1;
/*     */   static final int CLT_SINGLE_GENERIC = 2;
/*     */   static final int CLT_MULTIPLE_GENERIC = 3;
/*     */   static final int CLT_XDEFAULT = 4;
/*     */   static final int CLT_FIRST_ITEM = 5;
/*     */   
/*     */   static XMPNode findSchemaNode(XMPNode tree, String namespaceURI, boolean createNodes) throws XMPException {
/*  97 */     return findSchemaNode(tree, namespaceURI, null, createNodes);
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
/*     */   static XMPNode findSchemaNode(XMPNode tree, String namespaceURI, String suggestedPrefix, boolean createNodes) throws XMPException {
/* 120 */     assert tree.getParent() == null;
/* 121 */     XMPNode schemaNode = tree.findChildByName(namespaceURI);
/*     */     
/* 123 */     if (schemaNode == null && createNodes) {
/*     */ 
/*     */ 
/*     */       
/* 127 */       schemaNode = new XMPNode(namespaceURI, (new PropertyOptions()).setSchemaNode(true));
/* 128 */       schemaNode.setImplicit(true);
/*     */ 
/*     */       
/* 131 */       String prefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(namespaceURI);
/* 132 */       if (prefix == null)
/*     */       {
/* 134 */         if (suggestedPrefix != null && suggestedPrefix.length() != 0) {
/*     */           
/* 136 */           prefix = XMPMetaFactory.getSchemaRegistry().registerNamespace(namespaceURI, suggestedPrefix);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 141 */           throw new XMPException("Unregistered schema namespace URI", 101);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 146 */       schemaNode.setValue(prefix);
/*     */       
/* 148 */       tree.addChild(schemaNode);
/*     */     } 
/*     */     
/* 151 */     return schemaNode;
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
/*     */   static XMPNode findChildNode(XMPNode parent, String childName, boolean createNodes) throws XMPException {
/* 171 */     if (!parent.getOptions().isSchemaNode() && !parent.getOptions().isStruct()) {
/*     */       
/* 173 */       if (!parent.isImplicit())
/*     */       {
/* 175 */         throw new XMPException("Named children only allowed for schemas and structs", 102);
/*     */       }
/*     */       
/* 178 */       if (parent.getOptions().isArray())
/*     */       {
/* 180 */         throw new XMPException("Named children not allowed for arrays", 102);
/*     */       }
/*     */       
/* 183 */       if (createNodes)
/*     */       {
/* 185 */         parent.getOptions().setStruct(true);
/*     */       }
/*     */     } 
/*     */     
/* 189 */     XMPNode childNode = parent.findChildByName(childName);
/*     */     
/* 191 */     if (childNode == null && createNodes) {
/*     */       
/* 193 */       PropertyOptions options = new PropertyOptions();
/* 194 */       childNode = new XMPNode(childName, options);
/* 195 */       childNode.setImplicit(true);
/* 196 */       parent.addChild(childNode);
/*     */     } 
/*     */     
/* 199 */     assert childNode != null || !createNodes;
/*     */     
/* 201 */     return childNode;
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
/*     */   static XMPNode findNode(XMPNode xmpTree, XMPPath xpath, boolean createNodes, PropertyOptions leafOptions) throws XMPException {
/* 222 */     if (xpath == null || xpath.size() == 0)
/*     */     {
/* 224 */       throw new XMPException("Empty XMPPath", 102);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 229 */     XMPNode rootImplicitNode = null;
/* 230 */     XMPNode currNode = null;
/*     */ 
/*     */     
/* 233 */     currNode = findSchemaNode(xmpTree, xpath
/* 234 */         .getSegment(0).getName(), createNodes);
/* 235 */     if (currNode == null)
/*     */     {
/* 237 */       return null;
/*     */     }
/* 239 */     if (currNode.isImplicit()) {
/*     */       
/* 241 */       currNode.setImplicit(false);
/* 242 */       rootImplicitNode = currNode;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 249 */       for (int i = 1; i < xpath.size(); i++) {
/*     */         
/* 251 */         currNode = followXPathStep(currNode, xpath.getSegment(i), createNodes);
/* 252 */         if (currNode == null) {
/*     */           
/* 254 */           if (createNodes)
/*     */           {
/*     */             
/* 257 */             deleteNode(rootImplicitNode);
/*     */           }
/* 259 */           return null;
/*     */         } 
/* 261 */         if (currNode.isImplicit())
/*     */         {
/*     */           
/* 264 */           currNode.setImplicit(false);
/*     */ 
/*     */ 
/*     */           
/* 268 */           if (i == 1 && xpath
/* 269 */             .getSegment(i).isAlias() && xpath
/* 270 */             .getSegment(i).getAliasForm() != 0) {
/*     */             
/* 272 */             currNode.getOptions().setOption(xpath.getSegment(i).getAliasForm(), true);
/*     */           
/*     */           }
/* 275 */           else if (i < xpath.size() - 1 && xpath
/* 276 */             .getSegment(i).getKind() == 1 && 
/* 277 */             !currNode.getOptions().isCompositeProperty()) {
/*     */             
/* 279 */             currNode.getOptions().setStruct(true);
/*     */           } 
/*     */           
/* 282 */           if (rootImplicitNode == null)
/*     */           {
/* 284 */             rootImplicitNode = currNode;
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 289 */     } catch (XMPException e) {
/*     */ 
/*     */       
/* 292 */       if (rootImplicitNode != null)
/*     */       {
/* 294 */         deleteNode(rootImplicitNode);
/*     */       }
/* 296 */       throw e;
/*     */     } 
/*     */ 
/*     */     
/* 300 */     if (rootImplicitNode != null) {
/*     */ 
/*     */       
/* 303 */       currNode.getOptions().mergeWith(leafOptions);
/* 304 */       currNode.setOptions(currNode.getOptions());
/*     */     } 
/*     */     
/* 307 */     return currNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void deleteNode(XMPNode node) {
/* 318 */     XMPNode parent = node.getParent();
/*     */     
/* 320 */     if (node.getOptions().isQualifier()) {
/*     */ 
/*     */       
/* 323 */       parent.removeQualifier(node);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 328 */       parent.removeChild(node);
/*     */     } 
/*     */ 
/*     */     
/* 332 */     if (!parent.hasChildren() && parent.getOptions().isSchemaNode())
/*     */     {
/* 334 */       parent.getParent().removeChild(parent);
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
/*     */   static void setNodeValue(XMPNode node, Object value) {
/* 347 */     String strValue = serializeNodeValue(value);
/* 348 */     if (!node.getOptions().isQualifier() || !"xml:lang".equals(node.getName())) {
/*     */       
/* 350 */       node.setValue(strValue);
/*     */     }
/*     */     else {
/*     */       
/* 354 */       node.setValue(Utils.normalizeLangValue(strValue));
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
/*     */   static PropertyOptions verifySetOptions(PropertyOptions options, Object itemValue) throws XMPException {
/* 372 */     if (options == null)
/*     */     {
/*     */       
/* 375 */       options = new PropertyOptions();
/*     */     }
/*     */     
/* 378 */     if (options.isArrayAltText())
/*     */     {
/* 380 */       options.setArrayAlternate(true);
/*     */     }
/*     */     
/* 383 */     if (options.isArrayAlternate())
/*     */     {
/* 385 */       options.setArrayOrdered(true);
/*     */     }
/*     */     
/* 388 */     if (options.isArrayOrdered())
/*     */     {
/* 390 */       options.setArray(true);
/*     */     }
/*     */     
/* 393 */     if (options.isCompositeProperty() && itemValue != null && itemValue.toString().length() > 0)
/*     */     {
/* 395 */       throw new XMPException("Structs and arrays can't have values", 103);
/*     */     }
/*     */ 
/*     */     
/* 399 */     options.assertConsistency(options.getOptions());
/*     */     
/* 401 */     return options;
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
/*     */   static String serializeNodeValue(Object value) {
/*     */     String strValue;
/* 416 */     if (value == null) {
/*     */       
/* 418 */       strValue = null;
/*     */     }
/* 420 */     else if (value instanceof Boolean) {
/*     */       
/* 422 */       strValue = XMPUtils.convertFromBoolean(((Boolean)value).booleanValue());
/*     */     }
/* 424 */     else if (value instanceof Integer) {
/*     */       
/* 426 */       strValue = XMPUtils.convertFromInteger(((Integer)value).intValue());
/*     */     }
/* 428 */     else if (value instanceof Long) {
/*     */       
/* 430 */       strValue = XMPUtils.convertFromLong(((Long)value).longValue());
/*     */     }
/* 432 */     else if (value instanceof Double) {
/*     */       
/* 434 */       strValue = XMPUtils.convertFromDouble(((Double)value).doubleValue());
/*     */     }
/* 436 */     else if (value instanceof XMPDateTime) {
/*     */       
/* 438 */       strValue = XMPUtils.convertFromDate((XMPDateTime)value);
/*     */     }
/* 440 */     else if (value instanceof GregorianCalendar) {
/*     */       
/* 442 */       XMPDateTime dt = XMPDateTimeFactory.createFromCalendar((GregorianCalendar)value);
/* 443 */       strValue = XMPUtils.convertFromDate(dt);
/*     */     }
/* 445 */     else if (value instanceof byte[]) {
/*     */       
/* 447 */       strValue = XMPUtils.encodeBase64((byte[])value);
/*     */     }
/*     */     else {
/*     */       
/* 451 */       strValue = value.toString();
/*     */     } 
/*     */     
/* 454 */     return (strValue != null) ? Utils.removeControlChars(strValue) : null;
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
/*     */   private static XMPNode followXPathStep(XMPNode parentNode, XMPPathSegment nextStep, boolean createNodes) throws XMPException {
/* 481 */     XMPNode nextNode = null;
/* 482 */     int index = 0;
/* 483 */     int stepKind = nextStep.getKind();
/*     */     
/* 485 */     if (stepKind == 1) {
/*     */       
/* 487 */       nextNode = findChildNode(parentNode, nextStep.getName(), createNodes);
/*     */     }
/* 489 */     else if (stepKind == 2) {
/*     */       
/* 491 */       nextNode = findQualifierNode(parentNode, nextStep
/* 492 */           .getName().substring(1), createNodes);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 498 */       if (!parentNode.getOptions().isArray())
/*     */       {
/* 500 */         throw new XMPException("Indexing applied to non-array", 102);
/*     */       }
/*     */       
/* 503 */       if (stepKind == 3) {
/*     */         
/* 505 */         index = findIndexedItem(parentNode, nextStep.getName(), createNodes);
/*     */       }
/* 507 */       else if (stepKind == 4) {
/*     */         
/* 509 */         index = parentNode.getChildrenLength();
/*     */       }
/* 511 */       else if (stepKind == 6) {
/*     */         
/* 513 */         String[] result = Utils.splitNameAndValue(nextStep.getName());
/* 514 */         String fieldName = result[0];
/* 515 */         String fieldValue = result[1];
/* 516 */         index = lookupFieldSelector(parentNode, fieldName, fieldValue);
/*     */       }
/* 518 */       else if (stepKind == 5) {
/*     */         
/* 520 */         String[] result = Utils.splitNameAndValue(nextStep.getName());
/* 521 */         String qualName = result[0];
/* 522 */         String qualValue = result[1];
/* 523 */         index = lookupQualSelector(parentNode, qualName, qualValue, nextStep
/* 524 */             .getAliasForm());
/*     */       }
/*     */       else {
/*     */         
/* 528 */         throw new XMPException("Unknown array indexing step in FollowXPathStep", 9);
/*     */       } 
/*     */ 
/*     */       
/* 532 */       if (1 <= index && index <= parentNode.getChildrenLength())
/*     */       {
/* 534 */         nextNode = parentNode.getChild(index);
/*     */       }
/*     */     } 
/*     */     
/* 538 */     return nextNode;
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
/*     */   private static XMPNode findQualifierNode(XMPNode parent, String qualName, boolean createNodes) throws XMPException {
/* 559 */     assert !qualName.startsWith("?");
/*     */     
/* 561 */     XMPNode qualNode = parent.findQualifierByName(qualName);
/*     */     
/* 563 */     if (qualNode == null && createNodes) {
/*     */       
/* 565 */       qualNode = new XMPNode(qualName, null);
/* 566 */       qualNode.setImplicit(true);
/*     */       
/* 568 */       parent.addQualifier(qualNode);
/*     */     } 
/*     */     
/* 571 */     return qualNode;
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
/*     */   private static int findIndexedItem(XMPNode arrayNode, String segment, boolean createNodes) throws XMPException {
/* 585 */     int index = 0;
/*     */ 
/*     */     
/*     */     try {
/* 589 */       segment = segment.substring(1, segment.length() - 1);
/* 590 */       index = Integer.parseInt(segment);
/* 591 */       if (index < 1)
/*     */       {
/* 593 */         throw new XMPException("Array index must be larger than zero", 102);
/*     */       
/*     */       }
/*     */     }
/* 597 */     catch (NumberFormatException e) {
/*     */       
/* 599 */       throw new XMPException("Array index not digits.", 102);
/*     */     } 
/*     */     
/* 602 */     if (createNodes && index == arrayNode.getChildrenLength() + 1) {
/*     */ 
/*     */       
/* 605 */       XMPNode newItem = new XMPNode("[]", null);
/* 606 */       newItem.setImplicit(true);
/* 607 */       arrayNode.addChild(newItem);
/*     */     } 
/*     */     
/* 610 */     return index;
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
/*     */   private static int lookupFieldSelector(XMPNode arrayNode, String fieldName, String fieldValue) throws XMPException {
/* 628 */     int result = -1;
/*     */     
/* 630 */     for (int index = 1; index <= arrayNode.getChildrenLength() && result < 0; index++) {
/*     */       
/* 632 */       XMPNode currItem = arrayNode.getChild(index);
/*     */       
/* 634 */       if (!currItem.getOptions().isStruct())
/*     */       {
/* 636 */         throw new XMPException("Field selector must be used on array of struct", 102);
/*     */       }
/*     */ 
/*     */       
/* 640 */       for (int f = 1; f <= currItem.getChildrenLength(); f++) {
/*     */         
/* 642 */         XMPNode currField = currItem.getChild(f);
/* 643 */         if (fieldName.equals(currField.getName()))
/*     */         {
/*     */ 
/*     */           
/* 647 */           if (fieldValue.equals(currField.getValue())) {
/*     */             
/* 649 */             result = index;
/*     */             break;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 655 */     return result;
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
/*     */   private static int lookupQualSelector(XMPNode arrayNode, String qualName, String qualValue, int aliasForm) throws XMPException {
/* 676 */     if ("xml:lang".equals(qualName)) {
/*     */       
/* 678 */       qualValue = Utils.normalizeLangValue(qualValue);
/* 679 */       int i = lookupLanguageItem(arrayNode, qualValue);
/* 680 */       if (i < 0 && (aliasForm & 0x1000) > 0) {
/*     */         
/* 682 */         XMPNode langNode = new XMPNode("[]", null);
/* 683 */         XMPNode xdefault = new XMPNode("xml:lang", "x-default", null);
/* 684 */         langNode.addQualifier(xdefault);
/* 685 */         arrayNode.addChild(1, langNode);
/* 686 */         return 1;
/*     */       } 
/*     */ 
/*     */       
/* 690 */       return i;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 695 */     for (int index = 1; index < arrayNode.getChildrenLength(); index++) {
/*     */       
/* 697 */       XMPNode currItem = arrayNode.getChild(index);
/*     */       
/* 699 */       for (Iterator<XMPNode> it = currItem.iterateQualifier(); it.hasNext(); ) {
/*     */         
/* 701 */         XMPNode qualifier = it.next();
/* 702 */         if (qualName.equals(qualifier.getName()) && qualValue
/* 703 */           .equals(qualifier.getValue()))
/*     */         {
/* 705 */           return index;
/*     */         }
/*     */       } 
/*     */     } 
/* 709 */     return -1;
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
/*     */   static void normalizeLangArray(XMPNode arrayNode) {
/* 726 */     if (!arrayNode.getOptions().isArrayAltText()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 732 */     for (int i = 2; i <= arrayNode.getChildrenLength(); i++) {
/*     */       
/* 734 */       XMPNode child = arrayNode.getChild(i);
/* 735 */       if (child.hasQualifier() && "x-default".equals(child.getQualifier(1).getValue())) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 740 */           arrayNode.removeChild(i);
/* 741 */           arrayNode.addChild(1, child);
/*     */         }
/* 743 */         catch (XMPException e) {
/*     */           assert false;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 749 */         if (i == 2)
/*     */         {
/* 751 */           arrayNode.getChild(2).setValue(child.getValue());
/*     */         }
/*     */         break;
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
/*     */   static void detectAltText(XMPNode arrayNode) {
/* 768 */     if (arrayNode.getOptions().isArrayAlternate() && arrayNode.hasChildren()) {
/*     */       
/* 770 */       boolean isAltText = false;
/* 771 */       for (Iterator<XMPNode> it = arrayNode.iterateChildren(); it.hasNext(); ) {
/*     */         
/* 773 */         XMPNode child = it.next();
/* 774 */         if (child.getOptions().getHasLanguage()) {
/*     */           
/* 776 */           isAltText = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 781 */       if (isAltText) {
/*     */         
/* 783 */         arrayNode.getOptions().setArrayAltText(true);
/* 784 */         normalizeLangArray(arrayNode);
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
/*     */   static void appendLangItem(XMPNode arrayNode, String itemLang, String itemValue) throws XMPException {
/* 801 */     XMPNode newItem = new XMPNode("[]", itemValue, null);
/* 802 */     XMPNode langQual = new XMPNode("xml:lang", itemLang, null);
/* 803 */     newItem.addQualifier(langQual);
/*     */     
/* 805 */     if (!"x-default".equals(langQual.getValue())) {
/*     */       
/* 807 */       arrayNode.addChild(newItem);
/*     */     }
/*     */     else {
/*     */       
/* 811 */       arrayNode.addChild(1, newItem);
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
/*     */   static Object[] chooseLocalizedText(XMPNode arrayNode, String genericLang, String specificLang) throws XMPException {
/* 840 */     if (!arrayNode.getOptions().isArrayAltText())
/*     */     {
/* 842 */       throw new XMPException("Localized text array is not alt-text", 102);
/*     */     }
/* 844 */     if (!arrayNode.hasChildren())
/*     */     {
/* 846 */       return new Object[] { new Integer(0), null };
/*     */     }
/*     */     
/* 849 */     int foundGenericMatches = 0;
/* 850 */     XMPNode resultNode = null;
/* 851 */     XMPNode xDefault = null;
/*     */ 
/*     */     
/* 854 */     for (Iterator<XMPNode> it = arrayNode.iterateChildren(); it.hasNext(); ) {
/*     */       
/* 856 */       XMPNode currItem = it.next();
/*     */ 
/*     */       
/* 859 */       if (currItem.getOptions().isCompositeProperty())
/*     */       {
/* 861 */         throw new XMPException("Alt-text array item is not simple", 102);
/*     */       }
/* 863 */       if (!currItem.hasQualifier() || 
/* 864 */         !"xml:lang".equals(currItem.getQualifier(1).getName()))
/*     */       {
/* 866 */         throw new XMPException("Alt-text array item has no language qualifier", 102);
/*     */       }
/*     */ 
/*     */       
/* 870 */       String currLang = currItem.getQualifier(1).getValue();
/*     */ 
/*     */       
/* 873 */       if (specificLang.equals(currLang))
/*     */       {
/* 875 */         return new Object[] { new Integer(1), currItem };
/*     */       }
/* 877 */       if (genericLang != null && currLang.startsWith(genericLang)) {
/*     */         
/* 879 */         if (resultNode == null)
/*     */         {
/* 881 */           resultNode = currItem;
/*     */         }
/*     */         
/* 884 */         foundGenericMatches++; continue;
/*     */       } 
/* 886 */       if ("x-default".equals(currLang))
/*     */       {
/* 888 */         xDefault = currItem;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 893 */     if (foundGenericMatches == 1)
/*     */     {
/* 895 */       return new Object[] { new Integer(2), resultNode };
/*     */     }
/* 897 */     if (foundGenericMatches > 1)
/*     */     {
/* 899 */       return new Object[] { new Integer(3), resultNode };
/*     */     }
/* 901 */     if (xDefault != null)
/*     */     {
/* 903 */       return new Object[] { new Integer(4), xDefault };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 908 */     return new Object[] { new Integer(5), arrayNode.getChild(1) };
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
/*     */   static int lookupLanguageItem(XMPNode arrayNode, String language) throws XMPException {
/* 925 */     if (!arrayNode.getOptions().isArray())
/*     */     {
/* 927 */       throw new XMPException("Language item must be used on array", 102);
/*     */     }
/*     */     
/* 930 */     for (int index = 1; index <= arrayNode.getChildrenLength(); index++) {
/*     */       
/* 932 */       XMPNode child = arrayNode.getChild(index);
/* 933 */       if (child.hasQualifier() && "xml:lang".equals(child.getQualifier(1).getName()))
/*     */       {
/*     */ 
/*     */         
/* 937 */         if (language.equals(child.getQualifier(1).getValue()))
/*     */         {
/* 939 */           return index;
/*     */         }
/*     */       }
/*     */     } 
/* 943 */     return -1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPNodeUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */