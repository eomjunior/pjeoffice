/*      */ package com.itextpdf.xmp.impl;
/*      */ 
/*      */ import com.itextpdf.xmp.XMPConst;
/*      */ import com.itextpdf.xmp.XMPException;
/*      */ import com.itextpdf.xmp.XMPMeta;
/*      */ import com.itextpdf.xmp.XMPMetaFactory;
/*      */ import com.itextpdf.xmp.impl.xpath.XMPPath;
/*      */ import com.itextpdf.xmp.impl.xpath.XMPPathParser;
/*      */ import com.itextpdf.xmp.options.PropertyOptions;
/*      */ import com.itextpdf.xmp.properties.XMPAliasInfo;
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
/*      */ public class XMPUtilsImpl
/*      */   implements XMPConst
/*      */ {
/*      */   private static final int UCK_NORMAL = 0;
/*      */   private static final int UCK_SPACE = 1;
/*      */   private static final int UCK_COMMA = 2;
/*      */   private static final int UCK_SEMICOLON = 3;
/*      */   private static final int UCK_QUOTE = 4;
/*      */   private static final int UCK_CONTROL = 5;
/*      */   private static final String SPACES = " 　〿";
/*      */   private static final String COMMAS = ",，､﹐﹑、،՝";
/*      */   private static final String SEMICOLA = ";；﹔؛;";
/*      */   private static final String QUOTES = "\"«»〝〞〟―‹›";
/*      */   private static final String CONTROLS = "  ";
/*      */   
/*      */   public static String catenateArrayItems(XMPMeta xmp, String schemaNS, String arrayName, String separator, String quotes, boolean allowCommas) throws XMPException {
/*  105 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  106 */     ParameterAsserts.assertArrayName(arrayName);
/*  107 */     ParameterAsserts.assertImplementation(xmp);
/*  108 */     if (separator == null || separator.length() == 0)
/*      */     {
/*  110 */       separator = "; ";
/*      */     }
/*  112 */     if (quotes == null || quotes.length() == 0)
/*      */     {
/*  114 */       quotes = "\"";
/*      */     }
/*      */     
/*  117 */     XMPMetaImpl xmpImpl = (XMPMetaImpl)xmp;
/*  118 */     XMPNode arrayNode = null;
/*  119 */     XMPNode currItem = null;
/*      */ 
/*      */ 
/*      */     
/*  123 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
/*  124 */     arrayNode = XMPNodeUtils.findNode(xmpImpl.getRoot(), arrayPath, false, null);
/*  125 */     if (arrayNode == null)
/*      */     {
/*  127 */       return "";
/*      */     }
/*  129 */     if (!arrayNode.getOptions().isArray() || arrayNode.getOptions().isArrayAlternate())
/*      */     {
/*  131 */       throw new XMPException("Named property must be non-alternate array", 4);
/*      */     }
/*      */ 
/*      */     
/*  135 */     checkSeparator(separator);
/*      */     
/*  137 */     char openQuote = quotes.charAt(0);
/*  138 */     char closeQuote = checkQuotes(quotes, openQuote);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  143 */     StringBuffer catinatedString = new StringBuffer();
/*      */     
/*  145 */     for (Iterator<XMPNode> it = arrayNode.iterateChildren(); it.hasNext(); ) {
/*      */       
/*  147 */       currItem = it.next();
/*  148 */       if (currItem.getOptions().isCompositeProperty())
/*      */       {
/*  150 */         throw new XMPException("Array items must be simple", 4);
/*      */       }
/*  152 */       String str = applyQuotes(currItem.getValue(), openQuote, closeQuote, allowCommas);
/*      */       
/*  154 */       catinatedString.append(str);
/*  155 */       if (it.hasNext())
/*      */       {
/*  157 */         catinatedString.append(separator);
/*      */       }
/*      */     } 
/*      */     
/*  161 */     return catinatedString.toString();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void separateArrayItems(XMPMeta xmp, String schemaNS, String arrayName, String catedStr, PropertyOptions arrayOptions, boolean preserveCommas) throws XMPException {
/*  192 */     ParameterAsserts.assertSchemaNS(schemaNS);
/*  193 */     ParameterAsserts.assertArrayName(arrayName);
/*  194 */     if (catedStr == null)
/*      */     {
/*  196 */       throw new XMPException("Parameter must not be null", 4);
/*      */     }
/*  198 */     ParameterAsserts.assertImplementation(xmp);
/*  199 */     XMPMetaImpl xmpImpl = (XMPMetaImpl)xmp;
/*      */ 
/*      */     
/*  202 */     XMPNode arrayNode = separateFindCreateArray(schemaNS, arrayName, arrayOptions, xmpImpl);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  207 */     int nextKind = 0, charKind = 0;
/*  208 */     char ch = Character.MIN_VALUE, nextChar = Character.MIN_VALUE;
/*      */     
/*  210 */     int itemEnd = 0;
/*  211 */     int endPos = catedStr.length();
/*  212 */     while (itemEnd < endPos) {
/*      */       String itemValue;
/*      */       
/*      */       int itemStart;
/*  216 */       for (itemStart = itemEnd; itemStart < endPos; itemStart++) {
/*      */         
/*  218 */         ch = catedStr.charAt(itemStart);
/*  219 */         charKind = classifyCharacter(ch);
/*  220 */         if (charKind == 0 || charKind == 4) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */       
/*  225 */       if (itemStart >= endPos) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  230 */       if (charKind != 4) {
/*      */ 
/*      */ 
/*      */         
/*  234 */         for (itemEnd = itemStart; itemEnd < endPos; itemEnd++) {
/*      */           
/*  236 */           ch = catedStr.charAt(itemEnd);
/*  237 */           charKind = classifyCharacter(ch);
/*      */           
/*  239 */           if (charKind == 0 || charKind == 4 || (charKind == 2 && preserveCommas)) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */           
/*  244 */           if (charKind != 1) {
/*      */             break;
/*      */           }
/*      */           
/*  248 */           if (itemEnd + 1 < endPos) {
/*      */             
/*  250 */             ch = catedStr.charAt(itemEnd + 1);
/*  251 */             nextKind = classifyCharacter(ch);
/*  252 */             if (nextKind == 0 || nextKind == 4 || (nextKind == 2 && preserveCommas)) {
/*      */               continue;
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  263 */         itemValue = catedStr.substring(itemStart, itemEnd);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */         
/*  272 */         char openQuote = ch;
/*  273 */         char closeQuote = getClosingQuote(openQuote);
/*      */         
/*  275 */         itemStart++;
/*  276 */         itemValue = "";
/*      */         
/*  278 */         for (itemEnd = itemStart; itemEnd < endPos; itemEnd++) {
/*      */           
/*  280 */           ch = catedStr.charAt(itemEnd);
/*  281 */           charKind = classifyCharacter(ch);
/*      */           
/*  283 */           if (charKind != 4 || !isSurroundingQuote(ch, openQuote, closeQuote)) {
/*      */ 
/*      */ 
/*      */             
/*  287 */             itemValue = itemValue + ch;
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  297 */             if (itemEnd + 1 < endPos) {
/*      */               
/*  299 */               nextChar = catedStr.charAt(itemEnd + 1);
/*  300 */               nextKind = classifyCharacter(nextChar);
/*      */             }
/*      */             else {
/*      */               
/*  304 */               nextKind = 3;
/*  305 */               nextChar = ';';
/*      */             } 
/*      */             
/*  308 */             if (ch == nextChar) {
/*      */ 
/*      */               
/*  311 */               itemValue = itemValue + ch;
/*      */               
/*  313 */               itemEnd++;
/*      */             }
/*  315 */             else if (!isClosingingQuote(ch, openQuote, closeQuote)) {
/*      */ 
/*      */               
/*  318 */               itemValue = itemValue + ch;
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/*  324 */               itemEnd++;
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  333 */       int foundIndex = -1;
/*  334 */       for (int oldChild = 1; oldChild <= arrayNode.getChildrenLength(); oldChild++) {
/*      */         
/*  336 */         if (itemValue.equals(arrayNode.getChild(oldChild).getValue())) {
/*      */           
/*  338 */           foundIndex = oldChild;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  343 */       XMPNode newItem = null;
/*  344 */       if (foundIndex < 0) {
/*      */         
/*  346 */         newItem = new XMPNode("[]", itemValue, null);
/*  347 */         arrayNode.addChild(newItem);
/*      */       } 
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
/*      */   private static XMPNode separateFindCreateArray(String schemaNS, String arrayName, PropertyOptions arrayOptions, XMPMetaImpl xmp) throws XMPException {
/*  365 */     arrayOptions = XMPNodeUtils.verifySetOptions(arrayOptions, null);
/*  366 */     if (!arrayOptions.isOnlyArrayOptions())
/*      */     {
/*  368 */       throw new XMPException("Options can only provide array form", 103);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  373 */     XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
/*  374 */     XMPNode arrayNode = XMPNodeUtils.findNode(xmp.getRoot(), arrayPath, false, null);
/*  375 */     if (arrayNode != null) {
/*      */ 
/*      */ 
/*      */       
/*  379 */       PropertyOptions arrayForm = arrayNode.getOptions();
/*  380 */       if (!arrayForm.isArray() || arrayForm.isArrayAlternate())
/*      */       {
/*  382 */         throw new XMPException("Named property must be non-alternate array", 102);
/*      */       }
/*      */       
/*  385 */       if (arrayOptions.equalArrayTypes(arrayForm))
/*      */       {
/*  387 */         throw new XMPException("Mismatch of specified and existing array form", 102);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  395 */       arrayNode = XMPNodeUtils.findNode(xmp.getRoot(), arrayPath, true, arrayOptions
/*  396 */           .setArray(true));
/*  397 */       if (arrayNode == null)
/*      */       {
/*  399 */         throw new XMPException("Failed to create named array", 102);
/*      */       }
/*      */     } 
/*  402 */     return arrayNode;
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
/*      */ 
/*      */   
/*      */   public static void removeProperties(XMPMeta xmp, String schemaNS, String propName, boolean doAllProperties, boolean includeAliases) throws XMPException {
/*  430 */     ParameterAsserts.assertImplementation(xmp);
/*  431 */     XMPMetaImpl xmpImpl = (XMPMetaImpl)xmp;
/*      */     
/*  433 */     if (propName != null && propName.length() > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  439 */       if (schemaNS == null || schemaNS.length() == 0)
/*      */       {
/*  441 */         throw new XMPException("Property name requires schema namespace", 4);
/*      */       }
/*      */ 
/*      */       
/*  445 */       XMPPath expPath = XMPPathParser.expandXPath(schemaNS, propName);
/*      */       
/*  447 */       XMPNode propNode = XMPNodeUtils.findNode(xmpImpl.getRoot(), expPath, false, null);
/*  448 */       if (propNode != null)
/*      */       {
/*  450 */         if (doAllProperties || 
/*  451 */           !Utils.isInternalProperty(expPath.getSegment(0)
/*  452 */             .getName(), expPath.getSegment(1).getName()))
/*      */         {
/*  454 */           XMPNode parent = propNode.getParent();
/*  455 */           parent.removeChild(propNode);
/*  456 */           if (parent.getOptions().isSchemaNode() && !parent.hasChildren())
/*      */           {
/*      */             
/*  459 */             parent.getParent().removeChild(parent);
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*  465 */     else if (schemaNS != null && schemaNS.length() > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  473 */       XMPNode schemaNode = XMPNodeUtils.findSchemaNode(xmpImpl.getRoot(), schemaNS, false);
/*  474 */       if (schemaNode != null)
/*      */       {
/*  476 */         if (removeSchemaChildren(schemaNode, doAllProperties))
/*      */         {
/*  478 */           xmpImpl.getRoot().removeChild(schemaNode);
/*      */         }
/*      */       }
/*      */       
/*  482 */       if (includeAliases)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  490 */         XMPAliasInfo[] aliases = XMPMetaFactory.getSchemaRegistry().findAliases(schemaNS);
/*  491 */         for (int i = 0; i < aliases.length; i++)
/*      */         {
/*  493 */           XMPAliasInfo info = aliases[i];
/*  494 */           XMPPath path = XMPPathParser.expandXPath(info.getNamespace(), info
/*  495 */               .getPropName());
/*      */           
/*  497 */           XMPNode actualProp = XMPNodeUtils.findNode(xmpImpl.getRoot(), path, false, null);
/*  498 */           if (actualProp != null)
/*      */           {
/*  500 */             XMPNode parent = actualProp.getParent();
/*  501 */             parent.removeChild(actualProp);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  512 */       for (Iterator<XMPNode> it = xmpImpl.getRoot().iterateChildren(); it.hasNext(); ) {
/*      */         
/*  514 */         XMPNode schema = it.next();
/*  515 */         if (removeSchemaChildren(schema, doAllProperties))
/*      */         {
/*  517 */           it.remove();
/*      */         }
/*      */       } 
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
/*      */   public static void appendProperties(XMPMeta source, XMPMeta destination, boolean doAllProperties, boolean replaceOldValues, boolean deleteEmptyValues) throws XMPException {
/*  537 */     ParameterAsserts.assertImplementation(source);
/*  538 */     ParameterAsserts.assertImplementation(destination);
/*      */     
/*  540 */     XMPMetaImpl src = (XMPMetaImpl)source;
/*  541 */     XMPMetaImpl dest = (XMPMetaImpl)destination;
/*      */     
/*  543 */     for (Iterator<XMPNode> it = src.getRoot().iterateChildren(); it.hasNext(); ) {
/*      */       
/*  545 */       XMPNode sourceSchema = it.next();
/*      */ 
/*      */       
/*  548 */       XMPNode destSchema = XMPNodeUtils.findSchemaNode(dest.getRoot(), sourceSchema
/*  549 */           .getName(), false);
/*  550 */       boolean createdSchema = false;
/*  551 */       if (destSchema == null) {
/*      */ 
/*      */         
/*  554 */         destSchema = new XMPNode(sourceSchema.getName(), sourceSchema.getValue(), (new PropertyOptions()).setSchemaNode(true));
/*  555 */         dest.getRoot().addChild(destSchema);
/*  556 */         createdSchema = true;
/*      */       } 
/*      */ 
/*      */       
/*  560 */       for (Iterator<XMPNode> ic = sourceSchema.iterateChildren(); ic.hasNext(); ) {
/*      */         
/*  562 */         XMPNode sourceProp = ic.next();
/*  563 */         if (doAllProperties || 
/*  564 */           !Utils.isInternalProperty(sourceSchema.getName(), sourceProp.getName()))
/*      */         {
/*  566 */           appendSubtree(dest, sourceProp, destSchema, replaceOldValues, deleteEmptyValues);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  571 */       if (!destSchema.hasChildren() && (createdSchema || deleteEmptyValues))
/*      */       {
/*      */         
/*  574 */         dest.getRoot().removeChild(destSchema);
/*      */       }
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
/*      */   private static boolean removeSchemaChildren(XMPNode schemaNode, boolean doAllProperties) {
/*  593 */     for (Iterator<XMPNode> it = schemaNode.iterateChildren(); it.hasNext(); ) {
/*      */       
/*  595 */       XMPNode currProp = it.next();
/*  596 */       if (doAllProperties || 
/*  597 */         !Utils.isInternalProperty(schemaNode.getName(), currProp.getName()))
/*      */       {
/*  599 */         it.remove();
/*      */       }
/*      */     } 
/*      */     
/*  603 */     return !schemaNode.hasChildren();
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
/*      */   private static void appendSubtree(XMPMetaImpl destXMP, XMPNode sourceNode, XMPNode destParent, boolean replaceOldValues, boolean deleteEmptyValues) throws XMPException {
/*  620 */     XMPNode destNode = XMPNodeUtils.findChildNode(destParent, sourceNode.getName(), false);
/*      */     
/*  622 */     boolean valueIsEmpty = false;
/*  623 */     if (deleteEmptyValues)
/*      */     {
/*      */ 
/*      */       
/*  627 */       valueIsEmpty = sourceNode.getOptions().isSimple() ? ((sourceNode.getValue() == null || sourceNode.getValue().length() == 0)) : (!sourceNode.hasChildren());
/*      */     }
/*      */     
/*  630 */     if (deleteEmptyValues && valueIsEmpty) {
/*      */       
/*  632 */       if (destNode != null)
/*      */       {
/*  634 */         destParent.removeChild(destNode);
/*      */       }
/*      */     }
/*  637 */     else if (destNode == null) {
/*      */ 
/*      */       
/*  640 */       destParent.addChild((XMPNode)sourceNode.clone());
/*      */     }
/*  642 */     else if (replaceOldValues) {
/*      */ 
/*      */       
/*  645 */       destXMP.setNode(destNode, sourceNode.getValue(), sourceNode.getOptions(), true);
/*  646 */       destParent.removeChild(destNode);
/*  647 */       destNode = (XMPNode)sourceNode.clone();
/*  648 */       destParent.addChild(destNode);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  655 */       PropertyOptions sourceForm = sourceNode.getOptions();
/*  656 */       PropertyOptions destForm = destNode.getOptions();
/*  657 */       if (sourceForm != destForm) {
/*      */         return;
/*      */       }
/*      */       
/*  661 */       if (sourceForm.isStruct()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  666 */         for (Iterator<XMPNode> it = sourceNode.iterateChildren(); it.hasNext(); )
/*      */         {
/*  668 */           XMPNode sourceField = it.next();
/*  669 */           appendSubtree(destXMP, sourceField, destNode, replaceOldValues, deleteEmptyValues);
/*      */           
/*  671 */           if (deleteEmptyValues && !destNode.hasChildren())
/*      */           {
/*  673 */             destParent.removeChild(destNode);
/*      */           }
/*      */         }
/*      */       
/*  677 */       } else if (sourceForm.isArrayAltText()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  682 */         for (Iterator<XMPNode> it = sourceNode.iterateChildren(); it.hasNext(); )
/*      */         {
/*  684 */           XMPNode sourceItem = it.next();
/*  685 */           if (!sourceItem.hasQualifier() || 
/*  686 */             !"xml:lang".equals(sourceItem.getQualifier(1).getName())) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */           
/*  691 */           int destIndex = XMPNodeUtils.lookupLanguageItem(destNode, sourceItem
/*  692 */               .getQualifier(1).getValue());
/*  693 */           if (deleteEmptyValues && (sourceItem
/*  694 */             .getValue() == null || sourceItem
/*  695 */             .getValue().length() == 0)) {
/*      */             
/*  697 */             if (destIndex != -1) {
/*      */               
/*  699 */               destNode.removeChild(destIndex);
/*  700 */               if (!destNode.hasChildren())
/*      */               {
/*  702 */                 destParent.removeChild(destNode); } 
/*      */             } 
/*      */             continue;
/*      */           } 
/*  706 */           if (destIndex == -1)
/*      */           {
/*      */             
/*  709 */             if (!"x-default".equals(sourceItem.getQualifier(1).getValue()) || 
/*  710 */               !destNode.hasChildren()) {
/*      */               
/*  712 */               sourceItem.cloneSubtree(destNode);
/*      */ 
/*      */               
/*      */               continue;
/*      */             } 
/*      */ 
/*      */             
/*  719 */             XMPNode destItem = new XMPNode(sourceItem.getName(), sourceItem.getValue(), sourceItem.getOptions());
/*  720 */             sourceItem.cloneSubtree(destItem);
/*  721 */             destNode.addChild(1, destItem);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  726 */       } else if (sourceForm.isArray()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  732 */         for (Iterator<XMPNode> is = sourceNode.iterateChildren(); is.hasNext(); ) {
/*      */           
/*  734 */           XMPNode sourceItem = is.next();
/*      */           
/*  736 */           boolean match = false;
/*  737 */           for (Iterator<XMPNode> id = destNode.iterateChildren(); id.hasNext(); ) {
/*      */             
/*  739 */             XMPNode destItem = id.next();
/*  740 */             if (itemValuesMatch(sourceItem, destItem))
/*      */             {
/*  742 */               match = true;
/*      */             }
/*      */           } 
/*  745 */           if (!match) {
/*      */             
/*  747 */             destNode = (XMPNode)sourceItem.clone();
/*  748 */             destParent.addChild(destNode);
/*      */           } 
/*      */         } 
/*      */       } 
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
/*      */   private static boolean itemValuesMatch(XMPNode leftNode, XMPNode rightNode) throws XMPException {
/*  765 */     PropertyOptions leftForm = leftNode.getOptions();
/*  766 */     PropertyOptions rightForm = rightNode.getOptions();
/*      */     
/*  768 */     if (leftForm.equals(rightForm))
/*      */     {
/*  770 */       return false;
/*      */     }
/*      */     
/*  773 */     if (leftForm.getOptions() == 0) {
/*      */ 
/*      */       
/*  776 */       if (!leftNode.getValue().equals(rightNode.getValue()))
/*      */       {
/*  778 */         return false;
/*      */       }
/*  780 */       if (leftNode.getOptions().getHasLanguage() != rightNode.getOptions().getHasLanguage())
/*      */       {
/*  782 */         return false;
/*      */       }
/*  784 */       if (leftNode.getOptions().getHasLanguage() && 
/*  785 */         !leftNode.getQualifier(1).getValue().equals(rightNode
/*  786 */           .getQualifier(1).getValue()))
/*      */       {
/*  788 */         return false;
/*      */       }
/*      */     }
/*  791 */     else if (leftForm.isStruct()) {
/*      */ 
/*      */ 
/*      */       
/*  795 */       if (leftNode.getChildrenLength() != rightNode.getChildrenLength())
/*      */       {
/*  797 */         return false;
/*      */       }
/*      */       
/*  800 */       for (Iterator<XMPNode> it = leftNode.iterateChildren(); it.hasNext(); )
/*      */       {
/*  802 */         XMPNode leftField = it.next();
/*  803 */         XMPNode rightField = XMPNodeUtils.findChildNode(rightNode, leftField.getName(), false);
/*      */         
/*  805 */         if (rightField == null || !itemValuesMatch(leftField, rightField))
/*      */         {
/*  807 */           return false;
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  818 */       assert leftForm.isArray();
/*      */       
/*  820 */       for (Iterator<XMPNode> il = leftNode.iterateChildren(); il.hasNext(); ) {
/*      */         
/*  822 */         XMPNode leftItem = il.next();
/*      */         
/*  824 */         boolean match = false;
/*  825 */         for (Iterator<XMPNode> ir = rightNode.iterateChildren(); ir.hasNext(); ) {
/*      */           
/*  827 */           XMPNode rightItem = ir.next();
/*  828 */           if (itemValuesMatch(leftItem, rightItem)) {
/*      */             
/*  830 */             match = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*  834 */         if (!match)
/*      */         {
/*  836 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  840 */     return true;
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
/*      */   private static void checkSeparator(String separator) throws XMPException {
/*  854 */     boolean haveSemicolon = false;
/*  855 */     for (int i = 0; i < separator.length(); i++) {
/*      */       
/*  857 */       int charKind = classifyCharacter(separator.charAt(i));
/*  858 */       if (charKind == 3) {
/*      */         
/*  860 */         if (haveSemicolon)
/*      */         {
/*  862 */           throw new XMPException("Separator can have only one semicolon", 4);
/*      */         }
/*      */         
/*  865 */         haveSemicolon = true;
/*      */       }
/*  867 */       else if (charKind != 1) {
/*      */         
/*  869 */         throw new XMPException("Separator can have only spaces and one semicolon", 4);
/*      */       } 
/*      */     } 
/*      */     
/*  873 */     if (!haveSemicolon)
/*      */     {
/*  875 */       throw new XMPException("Separator must have one semicolon", 4);
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
/*      */   private static char checkQuotes(String quotes, char openQuote) throws XMPException {
/*      */     char closeQuote;
/*  895 */     int charKind = classifyCharacter(openQuote);
/*  896 */     if (charKind != 4)
/*      */     {
/*  898 */       throw new XMPException("Invalid quoting character", 4);
/*      */     }
/*      */     
/*  901 */     if (quotes.length() == 1) {
/*      */       
/*  903 */       closeQuote = openQuote;
/*      */     }
/*      */     else {
/*      */       
/*  907 */       closeQuote = quotes.charAt(1);
/*  908 */       charKind = classifyCharacter(closeQuote);
/*  909 */       if (charKind != 4)
/*      */       {
/*  911 */         throw new XMPException("Invalid quoting character", 4);
/*      */       }
/*      */     } 
/*      */     
/*  915 */     if (closeQuote != getClosingQuote(openQuote))
/*      */     {
/*  917 */       throw new XMPException("Mismatched quote pair", 4);
/*      */     }
/*  919 */     return closeQuote;
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
/*      */   private static int classifyCharacter(char ch) {
/*  933 */     if (" 　〿".indexOf(ch) >= 0 || (' ' <= ch && ch <= '​'))
/*      */     {
/*  935 */       return 1;
/*      */     }
/*  937 */     if (",，､﹐﹑、،՝".indexOf(ch) >= 0)
/*      */     {
/*  939 */       return 2;
/*      */     }
/*  941 */     if (";；﹔؛;".indexOf(ch) >= 0)
/*      */     {
/*  943 */       return 3;
/*      */     }
/*  945 */     if ("\"«»〝〞〟―‹›".indexOf(ch) >= 0 || ('〈' <= ch && ch <= '』') || ('‘' <= ch && ch <= '‟'))
/*      */     {
/*      */       
/*  948 */       return 4;
/*      */     }
/*  950 */     if (ch < ' ' || "  ".indexOf(ch) >= 0)
/*      */     {
/*  952 */       return 5;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  957 */     return 0;
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
/*      */   private static char getClosingQuote(char openQuote) {
/*  969 */     switch (openQuote) {
/*      */       
/*      */       case '"':
/*  972 */         return '"';
/*      */ 
/*      */ 
/*      */       
/*      */       case '«':
/*  977 */         return '»';
/*      */       case '»':
/*  979 */         return '«';
/*      */       case '―':
/*  981 */         return '―';
/*      */       case '‘':
/*  983 */         return '’';
/*      */       case '‚':
/*  985 */         return '‛';
/*      */       case '“':
/*  987 */         return '”';
/*      */       case '„':
/*  989 */         return '‟';
/*      */       case '‹':
/*  991 */         return '›';
/*      */       case '›':
/*  993 */         return '‹';
/*      */       case '〈':
/*  995 */         return '〉';
/*      */       case '《':
/*  997 */         return '》';
/*      */       case '「':
/*  999 */         return '」';
/*      */       case '『':
/* 1001 */         return '』';
/*      */       case '〝':
/* 1003 */         return '〟';
/*      */     } 
/* 1005 */     return Character.MIN_VALUE;
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
/*      */   private static String applyQuotes(String item, char openQuote, char closeQuote, boolean allowCommas) {
/* 1026 */     if (item == null)
/*      */     {
/* 1028 */       item = "";
/*      */     }
/*      */     
/* 1031 */     boolean prevSpace = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int i;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1049 */     for (i = 0; i < item.length(); i++) {
/*      */       
/* 1051 */       char ch = item.charAt(i);
/* 1052 */       int charKind = classifyCharacter(ch);
/* 1053 */       if (i == 0 && charKind == 4) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/* 1058 */       if (charKind == 1) {
/*      */ 
/*      */         
/* 1061 */         if (prevSpace) {
/*      */           break;
/*      */         }
/*      */         
/* 1065 */         prevSpace = true;
/*      */       }
/*      */       else {
/*      */         
/* 1069 */         prevSpace = false;
/* 1070 */         if (charKind == 3 || charKind == 5 || (charKind == 2 && !allowCommas)) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1079 */     if (i < item.length()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1088 */       StringBuffer newItem = new StringBuffer(item.length() + 2);
/*      */       int splitPoint;
/* 1090 */       for (splitPoint = 0; splitPoint <= i; splitPoint++) {
/*      */         
/* 1092 */         if (classifyCharacter(item.charAt(i)) == 4) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1099 */       newItem.append(openQuote).append(item.substring(0, splitPoint));
/*      */       
/* 1101 */       for (int charOffset = splitPoint; charOffset < item.length(); charOffset++) {
/*      */         
/* 1103 */         newItem.append(item.charAt(charOffset));
/* 1104 */         if (classifyCharacter(item.charAt(charOffset)) == 4 && 
/* 1105 */           isSurroundingQuote(item.charAt(charOffset), openQuote, closeQuote))
/*      */         {
/* 1107 */           newItem.append(item.charAt(charOffset));
/*      */         }
/*      */       } 
/*      */       
/* 1111 */       newItem.append(closeQuote);
/*      */       
/* 1113 */       item = newItem.toString();
/*      */     } 
/*      */     
/* 1116 */     return item;
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
/*      */   private static boolean isSurroundingQuote(char ch, char openQuote, char closeQuote) {
/* 1128 */     return (ch == openQuote || isClosingingQuote(ch, openQuote, closeQuote));
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
/*      */   private static boolean isClosingingQuote(char ch, char openQuote, char closeQuote) {
/* 1140 */     return (ch == closeQuote || (openQuote == '〝' && ch == '〞') || ch == '〟');
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPUtilsImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */