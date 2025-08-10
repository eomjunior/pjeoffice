/*      */ package com.itextpdf.xmp.impl;
/*      */ 
/*      */ import com.itextpdf.xmp.XMPException;
/*      */ import com.itextpdf.xmp.XMPMeta;
/*      */ import com.itextpdf.xmp.XMPMetaFactory;
/*      */ import com.itextpdf.xmp.options.SerializeOptions;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XMPSerializerRDF
/*      */ {
/*      */   private static final int DEFAULT_PAD = 2048;
/*      */   private static final String PACKET_HEADER = "<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
/*      */   private static final String PACKET_TRAILER = "<?xpacket end=\"";
/*      */   private static final String PACKET_TRAILER2 = "\"?>";
/*      */   private static final String RDF_XMPMETA_START = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"";
/*      */   private static final String RDF_XMPMETA_END = "</x:xmpmeta>";
/*      */   private static final String RDF_RDF_START = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">";
/*      */   private static final String RDF_RDF_END = "</rdf:RDF>";
/*      */   private static final String RDF_SCHEMA_START = "<rdf:Description rdf:about=";
/*      */   private static final String RDF_SCHEMA_END = "</rdf:Description>";
/*      */   private static final String RDF_STRUCT_START = "<rdf:Description";
/*      */   private static final String RDF_STRUCT_END = "</rdf:Description>";
/*      */   private static final String RDF_EMPTY_STRUCT = "<rdf:Description/>";
/*   90 */   static final Set RDF_ATTR_QUALIFIER = new HashSet(Arrays.asList((Object[])new String[] { "xml:lang", "rdf:resource", "rdf:ID", "rdf:bagID", "rdf:nodeID" }));
/*      */ 
/*      */   
/*      */   private XMPMetaImpl xmp;
/*      */ 
/*      */   
/*      */   private CountOutputStream outputStream;
/*      */ 
/*      */   
/*      */   private OutputStreamWriter writer;
/*      */ 
/*      */   
/*      */   private SerializeOptions options;
/*      */   
/*  104 */   private int unicodeSize = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int padding;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void serialize(XMPMeta xmp, OutputStream out, SerializeOptions options) throws XMPException {
/*      */     try {
/*  124 */       this.outputStream = new CountOutputStream(out);
/*      */       
/*  126 */       this.writer = new OutputStreamWriter(this.outputStream, options.getEncoding());
/*      */       
/*  128 */       this.xmp = (XMPMetaImpl)xmp;
/*  129 */       this.options = options;
/*  130 */       this.padding = options.getPadding();
/*      */       
/*  132 */       this.writer = new OutputStreamWriter(this.outputStream, options.getEncoding());
/*      */       
/*  134 */       checkOptionsConsistence();
/*      */ 
/*      */ 
/*      */       
/*  138 */       String tailStr = serializeAsRDF();
/*  139 */       this.writer.flush();
/*      */ 
/*      */       
/*  142 */       addPadding(tailStr.length());
/*      */ 
/*      */       
/*  145 */       write(tailStr);
/*  146 */       this.writer.flush();
/*      */       
/*  148 */       this.outputStream.close();
/*      */     }
/*  150 */     catch (IOException e) {
/*      */       
/*  152 */       throw new XMPException("Error writing to the OutputStream", 0);
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
/*      */   private void addPadding(int tailLength) throws XMPException, IOException {
/*  165 */     if (this.options.getExactPacketLength()) {
/*      */ 
/*      */       
/*  168 */       int minSize = this.outputStream.getBytesWritten() + tailLength * this.unicodeSize;
/*  169 */       if (minSize > this.padding)
/*      */       {
/*  171 */         throw new XMPException("Can't fit into specified packet size", 107);
/*      */       }
/*      */       
/*  174 */       this.padding -= minSize;
/*      */     } 
/*      */ 
/*      */     
/*  178 */     this.padding /= this.unicodeSize;
/*      */     
/*  180 */     int newlineLen = this.options.getNewline().length();
/*  181 */     if (this.padding >= newlineLen) {
/*      */       
/*  183 */       this.padding -= newlineLen;
/*  184 */       while (this.padding >= 100 + newlineLen) {
/*      */         
/*  186 */         writeChars(100, ' ');
/*  187 */         writeNewline();
/*  188 */         this.padding -= 100 + newlineLen;
/*      */       } 
/*  190 */       writeChars(this.padding, ' ');
/*  191 */       writeNewline();
/*      */     }
/*      */     else {
/*      */       
/*  195 */       writeChars(this.padding, ' ');
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkOptionsConsistence() throws XMPException {
/*  206 */     if ((this.options.getEncodeUTF16BE() | this.options.getEncodeUTF16LE()) != 0)
/*      */     {
/*  208 */       this.unicodeSize = 2;
/*      */     }
/*      */     
/*  211 */     if (this.options.getExactPacketLength()) {
/*      */       
/*  213 */       if ((this.options.getOmitPacketWrapper() | this.options.getIncludeThumbnailPad()) != 0)
/*      */       {
/*  215 */         throw new XMPException("Inconsistent options for exact size serialize", 103);
/*      */       }
/*      */       
/*  218 */       if ((this.options.getPadding() & this.unicodeSize - 1) != 0)
/*      */       {
/*  220 */         throw new XMPException("Exact size must be a multiple of the Unicode element", 103);
/*      */       
/*      */       }
/*      */     }
/*  224 */     else if (this.options.getReadOnlyPacket()) {
/*      */       
/*  226 */       if ((this.options.getOmitPacketWrapper() | this.options.getIncludeThumbnailPad()) != 0)
/*      */       {
/*  228 */         throw new XMPException("Inconsistent options for read-only packet", 103);
/*      */       }
/*      */       
/*  231 */       this.padding = 0;
/*      */     }
/*  233 */     else if (this.options.getOmitPacketWrapper()) {
/*      */       
/*  235 */       if (this.options.getIncludeThumbnailPad())
/*      */       {
/*  237 */         throw new XMPException("Inconsistent options for non-packet serialize", 103);
/*      */       }
/*      */       
/*  240 */       this.padding = 0;
/*      */     }
/*      */     else {
/*      */       
/*  244 */       if (this.padding == 0)
/*      */       {
/*  246 */         this.padding = 2048 * this.unicodeSize;
/*      */       }
/*      */       
/*  249 */       if (this.options.getIncludeThumbnailPad())
/*      */       {
/*  251 */         if (!this.xmp.doesPropertyExist("http://ns.adobe.com/xap/1.0/", "Thumbnails"))
/*      */         {
/*  253 */           this.padding += 10000 * this.unicodeSize;
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
/*      */   private String serializeAsRDF() throws IOException, XMPException {
/*  268 */     int level = 0;
/*      */ 
/*      */     
/*  271 */     if (!this.options.getOmitPacketWrapper()) {
/*      */       
/*  273 */       writeIndent(level);
/*  274 */       write("<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>");
/*  275 */       writeNewline();
/*      */     } 
/*      */ 
/*      */     
/*  279 */     if (!this.options.getOmitXmpMetaElement()) {
/*      */       
/*  281 */       writeIndent(level);
/*  282 */       write("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"");
/*      */       
/*  284 */       if (!this.options.getOmitVersionAttribute())
/*      */       {
/*  286 */         write(XMPMetaFactory.getVersionInfo().getMessage());
/*      */       }
/*  288 */       write("\">");
/*  289 */       writeNewline();
/*  290 */       level++;
/*      */     } 
/*      */ 
/*      */     
/*  294 */     writeIndent(level);
/*  295 */     write("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">");
/*  296 */     writeNewline();
/*      */ 
/*      */     
/*  299 */     if (this.options.getUseCanonicalFormat()) {
/*      */       
/*  301 */       serializeCanonicalRDFSchemas(level);
/*      */     }
/*      */     else {
/*      */       
/*  305 */       serializeCompactRDFSchemas(level);
/*      */     } 
/*      */ 
/*      */     
/*  309 */     writeIndent(level);
/*  310 */     write("</rdf:RDF>");
/*  311 */     writeNewline();
/*      */ 
/*      */     
/*  314 */     if (!this.options.getOmitXmpMetaElement()) {
/*      */       
/*  316 */       level--;
/*  317 */       writeIndent(level);
/*  318 */       write("</x:xmpmeta>");
/*  319 */       writeNewline();
/*      */     } 
/*      */     
/*  322 */     String tailStr = "";
/*  323 */     if (!this.options.getOmitPacketWrapper()) {
/*      */       
/*  325 */       for (level = this.options.getBaseIndent(); level > 0; level--)
/*      */       {
/*  327 */         tailStr = tailStr + this.options.getIndent();
/*      */       }
/*      */       
/*  330 */       tailStr = tailStr + "<?xpacket end=\"";
/*  331 */       tailStr = tailStr + (this.options.getReadOnlyPacket() ? 114 : 119);
/*  332 */       tailStr = tailStr + "\"?>";
/*      */     } 
/*      */     
/*  335 */     return tailStr;
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
/*      */   private void serializeCanonicalRDFSchemas(int level) throws IOException, XMPException {
/*  347 */     if (this.xmp.getRoot().getChildrenLength() > 0) {
/*      */       
/*  349 */       startOuterRDFDescription(this.xmp.getRoot(), level);
/*      */       
/*  351 */       for (Iterator<XMPNode> it = this.xmp.getRoot().iterateChildren(); it.hasNext(); ) {
/*      */         
/*  353 */         XMPNode currSchema = it.next();
/*  354 */         serializeCanonicalRDFSchema(currSchema, level);
/*      */       } 
/*      */       
/*  357 */       endOuterRDFDescription(level);
/*      */     }
/*      */     else {
/*      */       
/*  361 */       writeIndent(level + 1);
/*  362 */       write("<rdf:Description rdf:about=");
/*  363 */       writeTreeName();
/*  364 */       write("/>");
/*  365 */       writeNewline();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeTreeName() throws IOException {
/*  375 */     write(34);
/*  376 */     String name = this.xmp.getRoot().getName();
/*  377 */     if (name != null)
/*      */     {
/*  379 */       appendNodeValue(name, true);
/*      */     }
/*  381 */     write(34);
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
/*      */   private void serializeCompactRDFSchemas(int level) throws IOException, XMPException {
/*  394 */     writeIndent(level + 1);
/*  395 */     write("<rdf:Description rdf:about=");
/*  396 */     writeTreeName();
/*      */ 
/*      */     
/*  399 */     Set<String> usedPrefixes = new HashSet();
/*  400 */     usedPrefixes.add("xml");
/*  401 */     usedPrefixes.add("rdf");
/*      */     
/*  403 */     for (Iterator<XMPNode> it = this.xmp.getRoot().iterateChildren(); it.hasNext(); ) {
/*      */       
/*  405 */       XMPNode schema = it.next();
/*  406 */       declareUsedNamespaces(schema, usedPrefixes, level + 3);
/*      */     } 
/*      */ 
/*      */     
/*  410 */     boolean allAreAttrs = true;
/*  411 */     for (Iterator<XMPNode> iterator2 = this.xmp.getRoot().iterateChildren(); iterator2.hasNext(); ) {
/*      */       
/*  413 */       XMPNode schema = iterator2.next();
/*  414 */       allAreAttrs &= serializeCompactRDFAttrProps(schema, level + 2);
/*      */     } 
/*      */     
/*  417 */     if (!allAreAttrs) {
/*      */       
/*  419 */       write(62);
/*  420 */       writeNewline();
/*      */     }
/*      */     else {
/*      */       
/*  424 */       write("/>");
/*  425 */       writeNewline();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  430 */     for (Iterator<XMPNode> iterator1 = this.xmp.getRoot().iterateChildren(); iterator1.hasNext(); ) {
/*      */       
/*  432 */       XMPNode schema = iterator1.next();
/*  433 */       serializeCompactRDFElementProps(schema, level + 2);
/*      */     } 
/*      */ 
/*      */     
/*  437 */     writeIndent(level + 1);
/*  438 */     write("</rdf:Description>");
/*  439 */     writeNewline();
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
/*      */   private boolean serializeCompactRDFAttrProps(XMPNode parentNode, int indent) throws IOException {
/*  455 */     boolean allAreAttrs = true;
/*      */     
/*  457 */     for (Iterator<XMPNode> it = parentNode.iterateChildren(); it.hasNext(); ) {
/*      */       
/*  459 */       XMPNode prop = it.next();
/*      */       
/*  461 */       if (canBeRDFAttrProp(prop)) {
/*      */         
/*  463 */         writeNewline();
/*  464 */         writeIndent(indent);
/*  465 */         write(prop.getName());
/*  466 */         write("=\"");
/*  467 */         appendNodeValue(prop.getValue(), true);
/*  468 */         write(34);
/*      */         
/*      */         continue;
/*      */       } 
/*  472 */       allAreAttrs = false;
/*      */     } 
/*      */     
/*  475 */     return allAreAttrs;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void serializeCompactRDFElementProps(XMPNode parentNode, int indent) throws IOException, XMPException {
/*  531 */     for (Iterator<XMPNode> it = parentNode.iterateChildren(); it.hasNext(); ) {
/*      */       
/*  533 */       XMPNode node = it.next();
/*  534 */       if (canBeRDFAttrProp(node)) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/*  539 */       boolean emitEndTag = true;
/*  540 */       boolean indentEndTag = true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  545 */       String elemName = node.getName();
/*  546 */       if ("[]".equals(elemName))
/*      */       {
/*  548 */         elemName = "rdf:li";
/*      */       }
/*      */       
/*  551 */       writeIndent(indent);
/*  552 */       write(60);
/*  553 */       write(elemName);
/*      */       
/*  555 */       boolean hasGeneralQualifiers = false;
/*  556 */       boolean hasRDFResourceQual = false;
/*      */       
/*  558 */       for (Iterator<XMPNode> iq = node.iterateQualifier(); iq.hasNext(); ) {
/*      */         
/*  560 */         XMPNode qualifier = iq.next();
/*  561 */         if (!RDF_ATTR_QUALIFIER.contains(qualifier.getName())) {
/*      */           
/*  563 */           hasGeneralQualifiers = true;
/*      */           
/*      */           continue;
/*      */         } 
/*  567 */         hasRDFResourceQual = "rdf:resource".equals(qualifier.getName());
/*  568 */         write(32);
/*  569 */         write(qualifier.getName());
/*  570 */         write("=\"");
/*  571 */         appendNodeValue(qualifier.getValue(), true);
/*  572 */         write(34);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  578 */       if (hasGeneralQualifiers) {
/*      */         
/*  580 */         serializeCompactRDFGeneralQualifier(indent, node);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  585 */       else if (!node.getOptions().isCompositeProperty()) {
/*      */         
/*  587 */         Object[] result = serializeCompactRDFSimpleProp(node);
/*  588 */         emitEndTag = ((Boolean)result[0]).booleanValue();
/*  589 */         indentEndTag = ((Boolean)result[1]).booleanValue();
/*      */       }
/*  591 */       else if (node.getOptions().isArray()) {
/*      */         
/*  593 */         serializeCompactRDFArrayProp(node, indent);
/*      */       }
/*      */       else {
/*      */         
/*  597 */         emitEndTag = serializeCompactRDFStructProp(node, indent, hasRDFResourceQual);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  604 */       if (emitEndTag) {
/*      */         
/*  606 */         if (indentEndTag)
/*      */         {
/*  608 */           writeIndent(indent);
/*      */         }
/*  610 */         write("</");
/*  611 */         write(elemName);
/*  612 */         write(62);
/*  613 */         writeNewline();
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
/*      */   private Object[] serializeCompactRDFSimpleProp(XMPNode node) throws IOException {
/*  630 */     Boolean emitEndTag = Boolean.TRUE;
/*  631 */     Boolean indentEndTag = Boolean.TRUE;
/*      */     
/*  633 */     if (node.getOptions().isURI()) {
/*      */       
/*  635 */       write(" rdf:resource=\"");
/*  636 */       appendNodeValue(node.getValue(), true);
/*  637 */       write("\"/>");
/*  638 */       writeNewline();
/*  639 */       emitEndTag = Boolean.FALSE;
/*      */     }
/*  641 */     else if (node.getValue() == null || node.getValue().length() == 0) {
/*      */       
/*  643 */       write("/>");
/*  644 */       writeNewline();
/*  645 */       emitEndTag = Boolean.FALSE;
/*      */     }
/*      */     else {
/*      */       
/*  649 */       write(62);
/*  650 */       appendNodeValue(node.getValue(), false);
/*  651 */       indentEndTag = Boolean.FALSE;
/*      */     } 
/*      */     
/*  654 */     return new Object[] { emitEndTag, indentEndTag };
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
/*      */   private void serializeCompactRDFArrayProp(XMPNode node, int indent) throws IOException, XMPException {
/*  670 */     write(62);
/*  671 */     writeNewline();
/*  672 */     emitRDFArrayTag(node, true, indent + 1);
/*      */     
/*  674 */     if (node.getOptions().isArrayAltText())
/*      */     {
/*  676 */       XMPNodeUtils.normalizeLangArray(node);
/*      */     }
/*      */     
/*  679 */     serializeCompactRDFElementProps(node, indent + 2);
/*      */     
/*  681 */     emitRDFArrayTag(node, false, indent + 1);
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
/*      */   private boolean serializeCompactRDFStructProp(XMPNode node, int indent, boolean hasRDFResourceQual) throws XMPException, IOException {
/*  699 */     boolean hasAttrFields = false;
/*  700 */     boolean hasElemFields = false;
/*  701 */     boolean emitEndTag = true;
/*      */     
/*  703 */     for (Iterator<XMPNode> ic = node.iterateChildren(); ic.hasNext(); ) {
/*      */       
/*  705 */       XMPNode field = ic.next();
/*  706 */       if (canBeRDFAttrProp(field)) {
/*      */         
/*  708 */         hasAttrFields = true;
/*      */       }
/*      */       else {
/*      */         
/*  712 */         hasElemFields = true;
/*      */       } 
/*      */       
/*  715 */       if (hasAttrFields && hasElemFields) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  721 */     if (hasRDFResourceQual && hasElemFields)
/*      */     {
/*  723 */       throw new XMPException("Can't mix rdf:resource qualifier and element fields", 202);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  728 */     if (!node.hasChildren()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  734 */       write(" rdf:parseType=\"Resource\"/>");
/*  735 */       writeNewline();
/*  736 */       emitEndTag = false;
/*      */     
/*      */     }
/*  739 */     else if (!hasElemFields) {
/*      */ 
/*      */ 
/*      */       
/*  743 */       serializeCompactRDFAttrProps(node, indent + 1);
/*  744 */       write("/>");
/*  745 */       writeNewline();
/*  746 */       emitEndTag = false;
/*      */     
/*      */     }
/*  749 */     else if (!hasAttrFields) {
/*      */ 
/*      */ 
/*      */       
/*  753 */       write(" rdf:parseType=\"Resource\">");
/*  754 */       writeNewline();
/*  755 */       serializeCompactRDFElementProps(node, indent + 1);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  761 */       write(62);
/*  762 */       writeNewline();
/*  763 */       writeIndent(indent + 1);
/*  764 */       write("<rdf:Description");
/*  765 */       serializeCompactRDFAttrProps(node, indent + 2);
/*  766 */       write(">");
/*  767 */       writeNewline();
/*  768 */       serializeCompactRDFElementProps(node, indent + 1);
/*  769 */       writeIndent(indent + 1);
/*  770 */       write("</rdf:Description>");
/*  771 */       writeNewline();
/*      */     } 
/*  773 */     return emitEndTag;
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
/*      */   private void serializeCompactRDFGeneralQualifier(int indent, XMPNode node) throws IOException, XMPException {
/*  792 */     write(" rdf:parseType=\"Resource\">");
/*  793 */     writeNewline();
/*      */     
/*  795 */     serializeCanonicalRDFProperty(node, false, true, indent + 1);
/*      */     
/*  797 */     for (Iterator<XMPNode> iq = node.iterateQualifier(); iq.hasNext(); ) {
/*      */       
/*  799 */       XMPNode qualifier = iq.next();
/*  800 */       serializeCanonicalRDFProperty(qualifier, false, false, indent + 1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void serializeCanonicalRDFSchema(XMPNode schemaNode, int level) throws IOException, XMPException {
/*  837 */     for (Iterator<XMPNode> it = schemaNode.iterateChildren(); it.hasNext(); ) {
/*      */       
/*  839 */       XMPNode propNode = it.next();
/*  840 */       serializeCanonicalRDFProperty(propNode, this.options.getUseCanonicalFormat(), false, level + 2);
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
/*      */   private void declareUsedNamespaces(XMPNode node, Set usedPrefixes, int indent) throws IOException {
/*  856 */     if (node.getOptions().isSchemaNode()) {
/*      */ 
/*      */       
/*  859 */       String prefix = node.getValue().substring(0, node.getValue().length() - 1);
/*  860 */       declareNamespace(prefix, node.getName(), usedPrefixes, indent);
/*      */     }
/*  862 */     else if (node.getOptions().isStruct()) {
/*      */       
/*  864 */       for (Iterator<XMPNode> iterator = node.iterateChildren(); iterator.hasNext(); ) {
/*      */         
/*  866 */         XMPNode field = iterator.next();
/*  867 */         declareNamespace(field.getName(), null, usedPrefixes, indent);
/*      */       } 
/*      */     } 
/*      */     
/*  871 */     for (Iterator<XMPNode> iterator1 = node.iterateChildren(); iterator1.hasNext(); ) {
/*      */       
/*  873 */       XMPNode child = iterator1.next();
/*  874 */       declareUsedNamespaces(child, usedPrefixes, indent);
/*      */     } 
/*      */     
/*  877 */     for (Iterator<XMPNode> it = node.iterateQualifier(); it.hasNext(); ) {
/*      */       
/*  879 */       XMPNode qualifier = it.next();
/*  880 */       declareNamespace(qualifier.getName(), null, usedPrefixes, indent);
/*  881 */       declareUsedNamespaces(qualifier, usedPrefixes, indent);
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
/*      */   private void declareNamespace(String prefix, String namespace, Set<String> usedPrefixes, int indent) throws IOException {
/*  897 */     if (namespace == null) {
/*      */ 
/*      */       
/*  900 */       QName qname = new QName(prefix);
/*  901 */       if (qname.hasPrefix()) {
/*      */         
/*  903 */         prefix = qname.getPrefix();
/*      */         
/*  905 */         namespace = XMPMetaFactory.getSchemaRegistry().getNamespaceURI(prefix + ":");
/*      */         
/*  907 */         declareNamespace(prefix, namespace, usedPrefixes, indent);
/*      */       } else {
/*      */         return;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  915 */     if (!usedPrefixes.contains(prefix)) {
/*      */       
/*  917 */       writeNewline();
/*  918 */       writeIndent(indent);
/*  919 */       write("xmlns:");
/*  920 */       write(prefix);
/*  921 */       write("=\"");
/*  922 */       write(namespace);
/*  923 */       write(34);
/*  924 */       usedPrefixes.add(prefix);
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
/*      */   private void startOuterRDFDescription(XMPNode schemaNode, int level) throws IOException {
/*  937 */     writeIndent(level + 1);
/*  938 */     write("<rdf:Description rdf:about=");
/*  939 */     writeTreeName();
/*      */     
/*  941 */     Set<String> usedPrefixes = new HashSet();
/*  942 */     usedPrefixes.add("xml");
/*  943 */     usedPrefixes.add("rdf");
/*      */     
/*  945 */     declareUsedNamespaces(schemaNode, usedPrefixes, level + 3);
/*      */     
/*  947 */     write(62);
/*  948 */     writeNewline();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endOuterRDFDescription(int level) throws IOException {
/*  957 */     writeIndent(level + 1);
/*  958 */     write("</rdf:Description>");
/*  959 */     writeNewline();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void serializeCanonicalRDFProperty(XMPNode node, boolean useCanonicalRDF, boolean emitAsRDFValue, int indent) throws IOException, XMPException {
/* 1016 */     boolean emitEndTag = true;
/* 1017 */     boolean indentEndTag = true;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1022 */     String elemName = node.getName();
/* 1023 */     if (emitAsRDFValue) {
/*      */       
/* 1025 */       elemName = "rdf:value";
/*      */     }
/* 1027 */     else if ("[]".equals(elemName)) {
/*      */       
/* 1029 */       elemName = "rdf:li";
/*      */     } 
/*      */     
/* 1032 */     writeIndent(indent);
/* 1033 */     write(60);
/* 1034 */     write(elemName);
/*      */     
/* 1036 */     boolean hasGeneralQualifiers = false;
/* 1037 */     boolean hasRDFResourceQual = false;
/*      */     
/* 1039 */     for (Iterator<XMPNode> it = node.iterateQualifier(); it.hasNext(); ) {
/*      */       
/* 1041 */       XMPNode qualifier = it.next();
/* 1042 */       if (!RDF_ATTR_QUALIFIER.contains(qualifier.getName())) {
/*      */         
/* 1044 */         hasGeneralQualifiers = true;
/*      */         
/*      */         continue;
/*      */       } 
/* 1048 */       hasRDFResourceQual = "rdf:resource".equals(qualifier.getName());
/* 1049 */       if (!emitAsRDFValue) {
/*      */         
/* 1051 */         write(32);
/* 1052 */         write(qualifier.getName());
/* 1053 */         write("=\"");
/* 1054 */         appendNodeValue(qualifier.getValue(), true);
/* 1055 */         write(34);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1062 */     if (hasGeneralQualifiers && !emitAsRDFValue) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1069 */       if (hasRDFResourceQual)
/*      */       {
/* 1071 */         throw new XMPException("Can't mix rdf:resource and general qualifiers", 202);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1077 */       if (useCanonicalRDF) {
/*      */         
/* 1079 */         write(">");
/* 1080 */         writeNewline();
/*      */         
/* 1082 */         indent++;
/* 1083 */         writeIndent(indent);
/* 1084 */         write("<rdf:Description");
/* 1085 */         write(">");
/*      */       }
/*      */       else {
/*      */         
/* 1089 */         write(" rdf:parseType=\"Resource\">");
/*      */       } 
/* 1091 */       writeNewline();
/*      */       
/* 1093 */       serializeCanonicalRDFProperty(node, useCanonicalRDF, true, indent + 1);
/*      */       
/* 1095 */       for (Iterator<XMPNode> iterator = node.iterateQualifier(); iterator.hasNext(); ) {
/*      */         
/* 1097 */         XMPNode qualifier = iterator.next();
/* 1098 */         if (!RDF_ATTR_QUALIFIER.contains(qualifier.getName()))
/*      */         {
/* 1100 */           serializeCanonicalRDFProperty(qualifier, useCanonicalRDF, false, indent + 1);
/*      */         }
/*      */       } 
/*      */       
/* 1104 */       if (useCanonicalRDF)
/*      */       {
/* 1106 */         writeIndent(indent);
/* 1107 */         write("</rdf:Description>");
/* 1108 */         writeNewline();
/* 1109 */         indent--;
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/* 1116 */     else if (!node.getOptions().isCompositeProperty()) {
/*      */ 
/*      */ 
/*      */       
/* 1120 */       if (node.getOptions().isURI())
/*      */       {
/* 1122 */         write(" rdf:resource=\"");
/* 1123 */         appendNodeValue(node.getValue(), true);
/* 1124 */         write("\"/>");
/* 1125 */         writeNewline();
/* 1126 */         emitEndTag = false;
/*      */       }
/* 1128 */       else if (node.getValue() == null || "".equals(node.getValue()))
/*      */       {
/* 1130 */         write("/>");
/* 1131 */         writeNewline();
/* 1132 */         emitEndTag = false;
/*      */       }
/*      */       else
/*      */       {
/* 1136 */         write(62);
/* 1137 */         appendNodeValue(node.getValue(), false);
/* 1138 */         indentEndTag = false;
/*      */       }
/*      */     
/* 1141 */     } else if (node.getOptions().isArray()) {
/*      */ 
/*      */       
/* 1144 */       write(62);
/* 1145 */       writeNewline();
/* 1146 */       emitRDFArrayTag(node, true, indent + 1);
/* 1147 */       if (node.getOptions().isArrayAltText())
/*      */       {
/* 1149 */         XMPNodeUtils.normalizeLangArray(node);
/*      */       }
/* 1151 */       for (Iterator<XMPNode> iterator = node.iterateChildren(); iterator.hasNext(); ) {
/*      */         
/* 1153 */         XMPNode child = iterator.next();
/* 1154 */         serializeCanonicalRDFProperty(child, useCanonicalRDF, false, indent + 2);
/*      */       } 
/* 1156 */       emitRDFArrayTag(node, false, indent + 1);
/*      */ 
/*      */     
/*      */     }
/* 1160 */     else if (!hasRDFResourceQual) {
/*      */ 
/*      */       
/* 1163 */       if (!node.hasChildren())
/*      */       {
/*      */ 
/*      */         
/* 1167 */         if (useCanonicalRDF) {
/*      */           
/* 1169 */           write(">");
/* 1170 */           writeNewline();
/* 1171 */           writeIndent(indent + 1);
/* 1172 */           write("<rdf:Description/>");
/*      */         }
/*      */         else {
/*      */           
/* 1176 */           write(" rdf:parseType=\"Resource\"/>");
/* 1177 */           emitEndTag = false;
/*      */         } 
/* 1179 */         writeNewline();
/*      */       
/*      */       }
/*      */       else
/*      */       {
/*      */         
/* 1185 */         if (useCanonicalRDF) {
/*      */           
/* 1187 */           write(">");
/* 1188 */           writeNewline();
/* 1189 */           indent++;
/* 1190 */           writeIndent(indent);
/* 1191 */           write("<rdf:Description");
/* 1192 */           write(">");
/*      */         }
/*      */         else {
/*      */           
/* 1196 */           write(" rdf:parseType=\"Resource\">");
/*      */         } 
/* 1198 */         writeNewline();
/*      */         
/* 1200 */         for (Iterator<XMPNode> iterator = node.iterateChildren(); iterator.hasNext(); ) {
/*      */           
/* 1202 */           XMPNode child = iterator.next();
/* 1203 */           serializeCanonicalRDFProperty(child, useCanonicalRDF, false, indent + 1);
/*      */         } 
/*      */         
/* 1206 */         if (useCanonicalRDF)
/*      */         {
/* 1208 */           writeIndent(indent);
/* 1209 */           write("</rdf:Description>");
/* 1210 */           writeNewline();
/* 1211 */           indent--;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1219 */       for (Iterator<XMPNode> iterator = node.iterateChildren(); iterator.hasNext(); ) {
/*      */         
/* 1221 */         XMPNode child = iterator.next();
/* 1222 */         if (!canBeRDFAttrProp(child))
/*      */         {
/* 1224 */           throw new XMPException("Can't mix rdf:resource and complex fields", 202);
/*      */         }
/*      */         
/* 1227 */         writeNewline();
/* 1228 */         writeIndent(indent + 1);
/* 1229 */         write(32);
/* 1230 */         write(child.getName());
/* 1231 */         write("=\"");
/* 1232 */         appendNodeValue(child.getValue(), true);
/* 1233 */         write(34);
/*      */       } 
/* 1235 */       write("/>");
/* 1236 */       writeNewline();
/* 1237 */       emitEndTag = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1242 */     if (emitEndTag) {
/*      */       
/* 1244 */       if (indentEndTag)
/*      */       {
/* 1246 */         writeIndent(indent);
/*      */       }
/* 1248 */       write("</");
/* 1249 */       write(elemName);
/* 1250 */       write(62);
/* 1251 */       writeNewline();
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
/*      */   private void emitRDFArrayTag(XMPNode arrayNode, boolean isStartTag, int indent) throws IOException {
/* 1267 */     if (isStartTag || arrayNode.hasChildren()) {
/*      */       
/* 1269 */       writeIndent(indent);
/* 1270 */       write(isStartTag ? "<rdf:" : "</rdf:");
/*      */       
/* 1272 */       if (arrayNode.getOptions().isArrayAlternate()) {
/*      */         
/* 1274 */         write("Alt");
/*      */       }
/* 1276 */       else if (arrayNode.getOptions().isArrayOrdered()) {
/*      */         
/* 1278 */         write("Seq");
/*      */       }
/*      */       else {
/*      */         
/* 1282 */         write("Bag");
/*      */       } 
/*      */       
/* 1285 */       if (isStartTag && !arrayNode.hasChildren()) {
/*      */         
/* 1287 */         write("/>");
/*      */       }
/*      */       else {
/*      */         
/* 1291 */         write(">");
/*      */       } 
/*      */       
/* 1294 */       writeNewline();
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
/*      */   private void appendNodeValue(String value, boolean forAttribute) throws IOException {
/* 1312 */     if (value == null)
/*      */     {
/* 1314 */       value = "";
/*      */     }
/* 1316 */     write(Utils.escapeXML(value, forAttribute, true));
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
/*      */   private boolean canBeRDFAttrProp(XMPNode node) {
/* 1334 */     return (
/* 1335 */       !node.hasQualifier() && 
/* 1336 */       !node.getOptions().isURI() && 
/* 1337 */       !node.getOptions().isCompositeProperty() && 
/* 1338 */       !node.getOptions().containsOneOf(1073741824) && 
/* 1339 */       !"[]".equals(node.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeIndent(int times) throws IOException {
/* 1350 */     for (int i = this.options.getBaseIndent() + times; i > 0; i--)
/*      */     {
/* 1352 */       this.writer.write(this.options.getIndent());
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
/*      */   private void write(int c) throws IOException {
/* 1364 */     this.writer.write(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void write(String str) throws IOException {
/* 1375 */     this.writer.write(str);
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
/*      */   private void writeChars(int number, char c) throws IOException {
/* 1387 */     for (; number > 0; number--)
/*      */     {
/* 1389 */       this.writer.write(c);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeNewline() throws IOException {
/* 1400 */     this.writer.write(this.options.getNewline());
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPSerializerRDF.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */