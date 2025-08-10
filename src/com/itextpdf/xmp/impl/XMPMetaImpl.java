/*      */ package com.itextpdf.xmp.impl;
/*      */ 
/*      */ import com.itextpdf.xmp.XMPConst;
/*      */ import com.itextpdf.xmp.XMPDateTime;
/*      */ import com.itextpdf.xmp.XMPException;
/*      */ import com.itextpdf.xmp.XMPIterator;
/*      */ import com.itextpdf.xmp.XMPMeta;
/*      */ import com.itextpdf.xmp.XMPPathFactory;
/*      */ import com.itextpdf.xmp.XMPUtils;
/*      */ import com.itextpdf.xmp.impl.xpath.XMPPath;
/*      */ import com.itextpdf.xmp.impl.xpath.XMPPathParser;
/*      */ import com.itextpdf.xmp.options.IteratorOptions;
/*      */ import com.itextpdf.xmp.options.ParseOptions;
/*      */ import com.itextpdf.xmp.options.PropertyOptions;
/*      */ import com.itextpdf.xmp.properties.XMPProperty;
/*      */ import java.util.Calendar;
/*      */ import java.util.Iterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XMPMetaImpl
/*      */   implements XMPMeta, XMPConst
/*      */ {
/*      */   private static final int VALUE_STRING = 0;
/*      */   private static final int VALUE_BOOLEAN = 1;
/*      */   private static final int VALUE_INTEGER = 2;
/*      */   private static final int VALUE_LONG = 3;
/*      */   private static final int VALUE_DOUBLE = 4;
/*      */   private static final int VALUE_DATE = 5;
/*      */   private static final int VALUE_CALENDAR = 6;
/*      */   private static final int VALUE_BASE64 = 7;
/*      */   private XMPNode tree;
/*   79 */   private String packetHeader = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPMetaImpl() {
/*   88 */     this.tree = new XMPNode(null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPMetaImpl(XMPNode tree) {
/*  101 */     this.tree = tree;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendArrayItem(String schemaNS, String arrayName, PropertyOptions arrayOptions, String itemValue, PropertyOptions itemOptions) throws XMPException {
/*  112 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  113 */     ParameterAsserts.assertArrayName(arrayName);
/*      */     
/*  115 */     if (arrayOptions == null)
/*      */     {
/*  117 */       arrayOptions = new PropertyOptions();
/*      */     }
/*  119 */     if (!arrayOptions.isOnlyArrayOptions())
/*      */     {
/*  121 */       throw new XMPException("Only array form flags allowed for arrayOptions", 103);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  126 */     arrayOptions = XMPNodeUtils.verifySetOptions(arrayOptions, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  132 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
/*      */ 
/*      */ 
/*      */     
/*  136 */     XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
/*      */     
/*  138 */     if (arrayNode != null) {
/*      */ 
/*      */ 
/*      */       
/*  142 */       if (!arrayNode.getOptions().isArray())
/*      */       {
/*  144 */         throw new XMPException("The named property is not an array", 102);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  154 */     else if (arrayOptions.isArray()) {
/*      */       
/*  156 */       arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, true, arrayOptions);
/*  157 */       if (arrayNode == null)
/*      */       {
/*  159 */         throw new XMPException("Failure creating array node", 102);
/*      */       
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  165 */       throw new XMPException("Explicit arrayOptions required to create new array", 103);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  170 */     doSetArrayItem(arrayNode, -1, itemValue, itemOptions, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendArrayItem(String schemaNS, String arrayName, String itemValue) throws XMPException {
/*  180 */     appendArrayItem(schemaNS, arrayName, null, itemValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int countArrayItems(String schemaNS, String arrayName) throws XMPException {
/*  190 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  191 */     ParameterAsserts.assertArrayName(arrayName);
/*      */     
/*  193 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
/*  194 */     XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
/*      */     
/*  196 */     if (arrayNode == null)
/*      */     {
/*  198 */       return 0;
/*      */     }
/*      */     
/*  201 */     if (arrayNode.getOptions().isArray())
/*      */     {
/*  203 */       return arrayNode.getChildrenLength();
/*      */     }
/*      */ 
/*      */     
/*  207 */     throw new XMPException("The named property is not an array", 102);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteArrayItem(String schemaNS, String arrayName, int itemIndex) {
/*      */     try {
/*  219 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  220 */       ParameterAsserts.assertArrayName(arrayName);
/*      */       
/*  222 */       String itemPath = XMPPathFactory.composeArrayItemPath(arrayName, itemIndex);
/*  223 */       deleteProperty(schemaNS, itemPath);
/*      */     }
/*  225 */     catch (XMPException xMPException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteProperty(String schemaNS, String propName) {
/*      */     try {
/*  239 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  240 */       ParameterAsserts.assertPropName(propName);
/*      */       
/*  242 */       XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
/*      */       
/*  244 */       XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
/*  245 */       if (propNode != null)
/*      */       {
/*  247 */         XMPNodeUtils.deleteNode(propNode);
/*      */       }
/*      */     }
/*  250 */     catch (XMPException xMPException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteQualifier(String schemaNS, String propName, String qualNS, String qualName) {
/*      */     try {
/*  265 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  266 */       ParameterAsserts.assertPropName(propName);
/*      */       
/*  268 */       String qualPath = propName + XMPPathFactory.composeQualifierPath(qualNS, qualName);
/*  269 */       deleteProperty(schemaNS, qualPath);
/*      */     }
/*  271 */     catch (XMPException xMPException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteStructField(String schemaNS, String structName, String fieldNS, String fieldName) {
/*      */     try {
/*  287 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  288 */       ParameterAsserts.assertStructName(structName);
/*      */ 
/*      */       
/*  291 */       String fieldPath = structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
/*  292 */       deleteProperty(schemaNS, fieldPath);
/*      */     }
/*  294 */     catch (XMPException xMPException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doesPropertyExist(String schemaNS, String propName) {
/*      */     try {
/*  308 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  309 */       ParameterAsserts.assertPropName(propName);
/*      */       
/*  311 */       XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
/*  312 */       XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
/*  313 */       return (propNode != null);
/*      */     }
/*  315 */     catch (XMPException e) {
/*      */       
/*  317 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doesArrayItemExist(String schemaNS, String arrayName, int itemIndex) {
/*      */     try {
/*  329 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  330 */       ParameterAsserts.assertArrayName(arrayName);
/*      */       
/*  332 */       String path = XMPPathFactory.composeArrayItemPath(arrayName, itemIndex);
/*  333 */       return doesPropertyExist(schemaNS, path);
/*      */     }
/*  335 */     catch (XMPException e) {
/*      */       
/*  337 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doesStructFieldExist(String schemaNS, String structName, String fieldNS, String fieldName) {
/*      */     try {
/*  351 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  352 */       ParameterAsserts.assertStructName(structName);
/*      */       
/*  354 */       String path = XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
/*  355 */       return doesPropertyExist(schemaNS, structName + path);
/*      */     }
/*  357 */     catch (XMPException e) {
/*      */       
/*  359 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doesQualifierExist(String schemaNS, String propName, String qualNS, String qualName) {
/*      */     try {
/*  373 */       ParameterAsserts.assertSchemaNS(schemaNS);
/*  374 */       ParameterAsserts.assertPropName(propName);
/*      */       
/*  376 */       String path = XMPPathFactory.composeQualifierPath(qualNS, qualName);
/*  377 */       return doesPropertyExist(schemaNS, propName + path);
/*      */     }
/*  379 */     catch (XMPException e) {
/*      */       
/*  381 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPProperty getArrayItem(String schemaNS, String arrayName, int itemIndex) throws XMPException {
/*  392 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  393 */     ParameterAsserts.assertArrayName(arrayName);
/*      */     
/*  395 */     String itemPath = XMPPathFactory.composeArrayItemPath(arrayName, itemIndex);
/*  396 */     return getProperty(schemaNS, itemPath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPProperty getLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang) throws XMPException {
/*  407 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  408 */     ParameterAsserts.assertArrayName(altTextName);
/*  409 */     ParameterAsserts.assertSpecificLang(specificLang);
/*      */     
/*  411 */     genericLang = (genericLang != null) ? Utils.normalizeLangValue(genericLang) : null;
/*  412 */     specificLang = Utils.normalizeLangValue(specificLang);
/*      */     
/*  414 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, altTextName);
/*  415 */     XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
/*  416 */     if (arrayNode == null)
/*      */     {
/*  418 */       return null;
/*      */     }
/*      */     
/*  421 */     Object[] result = XMPNodeUtils.chooseLocalizedText(arrayNode, genericLang, specificLang);
/*  422 */     int match = ((Integer)result[0]).intValue();
/*  423 */     final XMPNode itemNode = (XMPNode)result[1];
/*      */     
/*  425 */     if (match != 0)
/*      */     {
/*  427 */       return new XMPProperty()
/*      */         {
/*      */           public String getValue()
/*      */           {
/*  431 */             return itemNode.getValue();
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public PropertyOptions getOptions() {
/*  437 */             return itemNode.getOptions();
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public String getLanguage() {
/*  443 */             return itemNode.getQualifier(1).getValue();
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public String toString() {
/*  449 */             return itemNode.getValue().toString();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*  455 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang, String itemValue, PropertyOptions options) throws XMPException {
/*      */     Iterator<XMPNode> iterator1;
/*  467 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  468 */     ParameterAsserts.assertArrayName(altTextName);
/*  469 */     ParameterAsserts.assertSpecificLang(specificLang);
/*      */     
/*  471 */     genericLang = (genericLang != null) ? Utils.normalizeLangValue(genericLang) : null;
/*  472 */     specificLang = Utils.normalizeLangValue(specificLang);
/*      */     
/*  474 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, altTextName);
/*      */ 
/*      */     
/*  477 */     XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, true, new PropertyOptions(7680));
/*      */ 
/*      */ 
/*      */     
/*  481 */     if (arrayNode == null)
/*      */     {
/*  483 */       throw new XMPException("Failed to find or create array node", 102);
/*      */     }
/*  485 */     if (!arrayNode.getOptions().isArrayAltText())
/*      */     {
/*  487 */       if (!arrayNode.hasChildren() && arrayNode.getOptions().isArrayAlternate()) {
/*      */         
/*  489 */         arrayNode.getOptions().setArrayAltText(true);
/*      */       }
/*      */       else {
/*      */         
/*  493 */         throw new XMPException("Specified property is no alt-text array", 102);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  499 */     boolean haveXDefault = false;
/*  500 */     XMPNode xdItem = null;
/*      */     
/*  502 */     for (Iterator<XMPNode> it = arrayNode.iterateChildren(); it.hasNext(); ) {
/*      */       
/*  504 */       XMPNode currItem = it.next();
/*  505 */       if (!currItem.hasQualifier() || 
/*  506 */         !"xml:lang".equals(currItem.getQualifier(1).getName()))
/*      */       {
/*  508 */         throw new XMPException("Language qualifier must be first", 102);
/*      */       }
/*  510 */       if ("x-default".equals(currItem.getQualifier(1).getValue())) {
/*      */         
/*  512 */         xdItem = currItem;
/*  513 */         haveXDefault = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  519 */     if (xdItem != null && arrayNode.getChildrenLength() > 1) {
/*      */       
/*  521 */       arrayNode.removeChild(xdItem);
/*  522 */       arrayNode.addChild(1, xdItem);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  528 */     Object[] result = XMPNodeUtils.chooseLocalizedText(arrayNode, genericLang, specificLang);
/*  529 */     int match = ((Integer)result[0]).intValue();
/*  530 */     XMPNode itemNode = (XMPNode)result[1];
/*      */     
/*  532 */     boolean specificXDefault = "x-default".equals(specificLang);
/*      */     
/*  534 */     switch (match) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*  540 */         XMPNodeUtils.appendLangItem(arrayNode, "x-default", itemValue);
/*  541 */         haveXDefault = true;
/*  542 */         if (!specificXDefault)
/*      */         {
/*  544 */           XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/*  550 */         if (!specificXDefault) {
/*      */ 
/*      */ 
/*      */           
/*  554 */           if (haveXDefault && xdItem != itemNode && xdItem != null && xdItem
/*  555 */             .getValue().equals(itemNode.getValue()))
/*      */           {
/*  557 */             xdItem.setValue(itemValue);
/*      */           }
/*      */           
/*  560 */           itemNode.setValue(itemValue);
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*  565 */         assert haveXDefault && xdItem == itemNode;
/*  566 */         for (iterator1 = arrayNode.iterateChildren(); iterator1.hasNext(); ) {
/*      */           
/*  568 */           XMPNode currItem = iterator1.next();
/*  569 */           if (currItem == xdItem || 
/*  570 */             !currItem.getValue().equals((xdItem != null) ? xdItem
/*  571 */               .getValue() : null)) {
/*      */             continue;
/*      */           }
/*      */           
/*  575 */           currItem.setValue(itemValue);
/*      */         } 
/*      */         
/*  578 */         if (xdItem != null)
/*      */         {
/*  580 */           xdItem.setValue(itemValue);
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*  589 */         if (haveXDefault && xdItem != itemNode && xdItem != null && xdItem
/*  590 */           .getValue().equals(itemNode.getValue()))
/*      */         {
/*  592 */           xdItem.setValue(itemValue);
/*      */         }
/*  594 */         itemNode.setValue(itemValue);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 3:
/*  602 */         XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
/*  603 */         if (specificXDefault)
/*      */         {
/*  605 */           haveXDefault = true;
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/*  613 */         if (xdItem != null && arrayNode.getChildrenLength() == 1)
/*      */         {
/*  615 */           xdItem.setValue(itemValue);
/*      */         }
/*  617 */         XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*  623 */         XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
/*  624 */         if (specificXDefault)
/*      */         {
/*  626 */           haveXDefault = true;
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*  632 */         throw new XMPException("Unexpected result from ChooseLocalizedText", 9);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  638 */     if (!haveXDefault && arrayNode.getChildrenLength() == 1)
/*      */     {
/*  640 */       XMPNodeUtils.appendLangItem(arrayNode, "x-default", itemValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang, String itemValue) throws XMPException {
/*  651 */     setLocalizedText(schemaNS, altTextName, genericLang, specificLang, itemValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPProperty getProperty(String schemaNS, String propName) throws XMPException {
/*  661 */     return getProperty(schemaNS, propName, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected XMPProperty getProperty(String schemaNS, String propName, int valueType) throws XMPException {
/*  686 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  687 */     ParameterAsserts.assertPropName(propName);
/*      */     
/*  689 */     XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
/*  690 */     final XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
/*      */     
/*  692 */     if (propNode != null) {
/*      */       
/*  694 */       if (valueType != 0 && propNode.getOptions().isCompositeProperty())
/*      */       {
/*  696 */         throw new XMPException("Property must be simple when a value type is requested", 102);
/*      */       }
/*      */ 
/*      */       
/*  700 */       final Object value = evaluateNodeValue(valueType, propNode);
/*      */       
/*  702 */       return new XMPProperty()
/*      */         {
/*      */           public String getValue()
/*      */           {
/*  706 */             return (value != null) ? value.toString() : null;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public PropertyOptions getOptions() {
/*  712 */             return propNode.getOptions();
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public String getLanguage() {
/*  718 */             return null;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public String toString() {
/*  724 */             return value.toString();
/*      */           }
/*      */         };
/*      */     } 
/*      */ 
/*      */     
/*  730 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object getPropertyObject(String schemaNS, String propName, int valueType) throws XMPException {
/*  753 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  754 */     ParameterAsserts.assertPropName(propName);
/*      */     
/*  756 */     XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
/*  757 */     XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, false, null);
/*      */     
/*  759 */     if (propNode != null) {
/*      */       
/*  761 */       if (valueType != 0 && propNode.getOptions().isCompositeProperty())
/*      */       {
/*  763 */         throw new XMPException("Property must be simple when a value type is requested", 102);
/*      */       }
/*      */ 
/*      */       
/*  767 */       return evaluateNodeValue(valueType, propNode);
/*      */     } 
/*      */ 
/*      */     
/*  771 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean getPropertyBoolean(String schemaNS, String propName) throws XMPException {
/*  781 */     return (Boolean)getPropertyObject(schemaNS, propName, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyBoolean(String schemaNS, String propName, boolean propValue, PropertyOptions options) throws XMPException {
/*  792 */     setProperty(schemaNS, propName, propValue ? "True" : "False", options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyBoolean(String schemaNS, String propName, boolean propValue) throws XMPException {
/*  802 */     setProperty(schemaNS, propName, propValue ? "True" : "False", null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer getPropertyInteger(String schemaNS, String propName) throws XMPException {
/*  811 */     return (Integer)getPropertyObject(schemaNS, propName, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyInteger(String schemaNS, String propName, int propValue, PropertyOptions options) throws XMPException {
/*  821 */     setProperty(schemaNS, propName, new Integer(propValue), options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyInteger(String schemaNS, String propName, int propValue) throws XMPException {
/*  831 */     setProperty(schemaNS, propName, new Integer(propValue), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long getPropertyLong(String schemaNS, String propName) throws XMPException {
/*  840 */     return (Long)getPropertyObject(schemaNS, propName, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyLong(String schemaNS, String propName, long propValue, PropertyOptions options) throws XMPException {
/*  850 */     setProperty(schemaNS, propName, new Long(propValue), options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyLong(String schemaNS, String propName, long propValue) throws XMPException {
/*  860 */     setProperty(schemaNS, propName, new Long(propValue), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double getPropertyDouble(String schemaNS, String propName) throws XMPException {
/*  869 */     return (Double)getPropertyObject(schemaNS, propName, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyDouble(String schemaNS, String propName, double propValue, PropertyOptions options) throws XMPException {
/*  879 */     setProperty(schemaNS, propName, new Double(propValue), options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyDouble(String schemaNS, String propName, double propValue) throws XMPException {
/*  889 */     setProperty(schemaNS, propName, new Double(propValue), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPDateTime getPropertyDate(String schemaNS, String propName) throws XMPException {
/*  898 */     return (XMPDateTime)getPropertyObject(schemaNS, propName, 5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyDate(String schemaNS, String propName, XMPDateTime propValue, PropertyOptions options) throws XMPException {
/*  909 */     setProperty(schemaNS, propName, propValue, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyDate(String schemaNS, String propName, XMPDateTime propValue) throws XMPException {
/*  919 */     setProperty(schemaNS, propName, propValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Calendar getPropertyCalendar(String schemaNS, String propName) throws XMPException {
/*  928 */     return (Calendar)getPropertyObject(schemaNS, propName, 6);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyCalendar(String schemaNS, String propName, Calendar propValue, PropertyOptions options) throws XMPException {
/*  939 */     setProperty(schemaNS, propName, propValue, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyCalendar(String schemaNS, String propName, Calendar propValue) throws XMPException {
/*  949 */     setProperty(schemaNS, propName, propValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getPropertyBase64(String schemaNS, String propName) throws XMPException {
/*  958 */     return (byte[])getPropertyObject(schemaNS, propName, 7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPropertyString(String schemaNS, String propName) throws XMPException {
/*  967 */     return (String)getPropertyObject(schemaNS, propName, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyBase64(String schemaNS, String propName, byte[] propValue, PropertyOptions options) throws XMPException {
/*  977 */     setProperty(schemaNS, propName, propValue, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPropertyBase64(String schemaNS, String propName, byte[] propValue) throws XMPException {
/*  987 */     setProperty(schemaNS, propName, propValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPProperty getQualifier(String schemaNS, String propName, String qualNS, String qualName) throws XMPException {
/*  999 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1000 */     ParameterAsserts.assertPropName(propName);
/*      */     
/* 1002 */     String qualPath = propName + XMPPathFactory.composeQualifierPath(qualNS, qualName);
/* 1003 */     return getProperty(schemaNS, qualPath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPProperty getStructField(String schemaNS, String structName, String fieldNS, String fieldName) throws XMPException {
/* 1014 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1015 */     ParameterAsserts.assertStructName(structName);
/*      */     
/* 1017 */     String fieldPath = structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
/* 1018 */     return getProperty(schemaNS, fieldPath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPIterator iterator() throws XMPException {
/* 1028 */     return iterator(null, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPIterator iterator(IteratorOptions options) throws XMPException {
/* 1037 */     return iterator(null, null, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPIterator iterator(String schemaNS, String propName, IteratorOptions options) throws XMPException {
/* 1047 */     return new XMPIteratorImpl(this, schemaNS, propName, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue, PropertyOptions options) throws XMPException {
/* 1058 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1059 */     ParameterAsserts.assertArrayName(arrayName);
/*      */ 
/*      */     
/* 1062 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
/* 1063 */     XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
/*      */     
/* 1065 */     if (arrayNode != null) {
/*      */       
/* 1067 */       doSetArrayItem(arrayNode, itemIndex, itemValue, options, false);
/*      */     }
/*      */     else {
/*      */       
/* 1071 */       throw new XMPException("Specified array does not exist", 102);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue) throws XMPException {
/* 1082 */     setArrayItem(schemaNS, arrayName, itemIndex, itemValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue, PropertyOptions options) throws XMPException {
/* 1094 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1095 */     ParameterAsserts.assertArrayName(arrayName);
/*      */ 
/*      */     
/* 1098 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
/* 1099 */     XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
/*      */     
/* 1101 */     if (arrayNode != null) {
/*      */       
/* 1103 */       doSetArrayItem(arrayNode, itemIndex, itemValue, options, true);
/*      */     }
/*      */     else {
/*      */       
/* 1107 */       throw new XMPException("Specified array does not exist", 102);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue) throws XMPException {
/* 1118 */     insertArrayItem(schemaNS, arrayName, itemIndex, itemValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperty(String schemaNS, String propName, Object propValue, PropertyOptions options) throws XMPException {
/* 1129 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1130 */     ParameterAsserts.assertPropName(propName);
/*      */     
/* 1132 */     options = XMPNodeUtils.verifySetOptions(options, propValue);
/*      */     
/* 1134 */     XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
/*      */     
/* 1136 */     XMPNode propNode = XMPNodeUtils.findNode(this.tree, expPath, true, options);
/* 1137 */     if (propNode != null) {
/*      */       
/* 1139 */       setNode(propNode, propValue, options, false);
/*      */     }
/*      */     else {
/*      */       
/* 1143 */       throw new XMPException("Specified property does not exist", 102);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperty(String schemaNS, String propName, Object propValue) throws XMPException {
/* 1153 */     setProperty(schemaNS, propName, propValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setQualifier(String schemaNS, String propName, String qualNS, String qualName, String qualValue, PropertyOptions options) throws XMPException {
/* 1165 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1166 */     ParameterAsserts.assertPropName(propName);
/*      */     
/* 1168 */     if (!doesPropertyExist(schemaNS, propName))
/*      */     {
/* 1170 */       throw new XMPException("Specified property does not exist!", 102);
/*      */     }
/*      */     
/* 1173 */     String qualPath = propName + XMPPathFactory.composeQualifierPath(qualNS, qualName);
/* 1174 */     setProperty(schemaNS, qualPath, qualValue, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setQualifier(String schemaNS, String propName, String qualNS, String qualName, String qualValue) throws XMPException {
/* 1184 */     setQualifier(schemaNS, propName, qualNS, qualName, qualValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStructField(String schemaNS, String structName, String fieldNS, String fieldName, String fieldValue, PropertyOptions options) throws XMPException {
/* 1196 */     ParameterAsserts.assertSchemaNS(schemaNS);
/* 1197 */     ParameterAsserts.assertStructName(structName);
/*      */     
/* 1199 */     String fieldPath = structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName);
/* 1200 */     setProperty(schemaNS, fieldPath, fieldValue, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStructField(String schemaNS, String structName, String fieldNS, String fieldName, String fieldValue) throws XMPException {
/* 1210 */     setStructField(schemaNS, structName, fieldNS, fieldName, fieldValue, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getObjectName() {
/* 1219 */     return (this.tree.getName() != null) ? this.tree.getName() : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObjectName(String name) {
/* 1228 */     this.tree.setName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPacketHeader() {
/* 1237 */     return this.packetHeader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPacketHeader(String packetHeader) {
/* 1247 */     this.packetHeader = packetHeader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/* 1258 */     XMPNode clonedTree = (XMPNode)this.tree.clone();
/* 1259 */     return new XMPMetaImpl(clonedTree);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String dumpObject() {
/* 1269 */     return getRoot().dumpNode(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sort() {
/* 1278 */     this.tree.sort();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void normalize(ParseOptions options) throws XMPException {
/* 1287 */     if (options == null)
/*      */     {
/* 1289 */       options = new ParseOptions();
/*      */     }
/* 1291 */     XMPNormalizer.process(this, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMPNode getRoot() {
/* 1300 */     return this.tree;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doSetArrayItem(XMPNode arrayNode, int itemIndex, String itemValue, PropertyOptions itemOptions, boolean insert) throws XMPException {
/* 1326 */     XMPNode itemNode = new XMPNode("[]", null);
/* 1327 */     itemOptions = XMPNodeUtils.verifySetOptions(itemOptions, itemValue);
/*      */ 
/*      */ 
/*      */     
/* 1331 */     int maxIndex = insert ? (arrayNode.getChildrenLength() + 1) : arrayNode.getChildrenLength();
/* 1332 */     if (itemIndex == -1)
/*      */     {
/* 1334 */       itemIndex = maxIndex;
/*      */     }
/*      */     
/* 1337 */     if (1 <= itemIndex && itemIndex <= maxIndex) {
/*      */       
/* 1339 */       if (!insert)
/*      */       {
/* 1341 */         arrayNode.removeChild(itemIndex);
/*      */       }
/* 1343 */       arrayNode.addChild(itemIndex, itemNode);
/* 1344 */       setNode(itemNode, itemValue, itemOptions, false);
/*      */     }
/*      */     else {
/*      */       
/* 1348 */       throw new XMPException("Array index out of bounds", 104);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setNode(XMPNode node, Object value, PropertyOptions newOptions, boolean deleteExisting) throws XMPException {
/* 1369 */     if (deleteExisting)
/*      */     {
/* 1371 */       node.clear();
/*      */     }
/*      */ 
/*      */     
/* 1375 */     node.getOptions().mergeWith(newOptions);
/*      */     
/* 1377 */     if (!node.getOptions().isCompositeProperty()) {
/*      */ 
/*      */       
/* 1380 */       XMPNodeUtils.setNodeValue(node, value);
/*      */     }
/*      */     else {
/*      */       
/* 1384 */       if (value != null && value.toString().length() > 0)
/*      */       {
/* 1386 */         throw new XMPException("Composite nodes can't have values", 102);
/*      */       }
/*      */       
/* 1389 */       node.removeChildren();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object evaluateNodeValue(int valueType, XMPNode propNode) throws XMPException {
/*      */     XMPDateTime dt;
/* 1409 */     String rawValue = propNode.getValue();
/* 1410 */     switch (valueType)
/*      */     
/*      */     { case 1:
/* 1413 */         value = new Boolean(XMPUtils.convertToBoolean(rawValue));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1443 */         return value;case 2: value = new Integer(XMPUtils.convertToInteger(rawValue)); return value;case 3: value = new Long(XMPUtils.convertToLong(rawValue)); return value;case 4: value = new Double(XMPUtils.convertToDouble(rawValue)); return value;case 5: value = XMPUtils.convertToDate(rawValue); return value;case 6: dt = XMPUtils.convertToDate(rawValue); value = dt.getCalendar(); return value;case 7: value = XMPUtils.decodeBase64(rawValue); return value; }  Object value = (rawValue != null || propNode.getOptions().isCompositeProperty()) ? rawValue : ""; return value;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPMetaImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */