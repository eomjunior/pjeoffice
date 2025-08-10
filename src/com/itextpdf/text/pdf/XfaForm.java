/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.xml.XmlDomWriter;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.EmptyStackException;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XfaForm
/*      */ {
/*      */   private Xml2SomTemplate templateSom;
/*      */   private Node templateNode;
/*      */   private Xml2SomDatasets datasetsSom;
/*      */   private Node datasetsNode;
/*      */   private AcroFieldsSearch acroFieldsSom;
/*      */   private PdfReader reader;
/*      */   private boolean xfaPresent;
/*      */   private Document domDocument;
/*      */   private boolean changed;
/*      */   public static final String XFA_DATA_SCHEMA = "http://www.xfa.org/schema/xfa-data/1.0/";
/*      */   
/*      */   public XfaForm() {}
/*      */   
/*      */   public static PdfObject getXfaObject(PdfReader reader) {
/*  104 */     PdfDictionary af = (PdfDictionary)PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.ACROFORM));
/*  105 */     if (af == null) {
/*  106 */       return null;
/*      */     }
/*  108 */     return PdfReader.getPdfObjectRelease(af.get(PdfName.XFA));
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
/*      */   public XfaForm(PdfReader reader) throws IOException, ParserConfigurationException, SAXException {
/*  120 */     this.reader = reader;
/*  121 */     PdfObject xfa = getXfaObject(reader);
/*  122 */     if (xfa == null) {
/*  123 */       this.xfaPresent = false;
/*      */       return;
/*      */     } 
/*  126 */     this.xfaPresent = true;
/*  127 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*  128 */     if (xfa.isArray()) {
/*  129 */       PdfArray ar = (PdfArray)xfa;
/*  130 */       for (int k = 1; k < ar.size(); k += 2) {
/*  131 */         PdfObject ob = ar.getDirectObject(k);
/*  132 */         if (ob instanceof PRStream) {
/*  133 */           byte[] b = PdfReader.getStreamBytes((PRStream)ob);
/*  134 */           bout.write(b);
/*      */         }
/*      */       
/*      */       } 
/*  138 */     } else if (xfa instanceof PRStream) {
/*  139 */       byte[] b = PdfReader.getStreamBytes((PRStream)xfa);
/*  140 */       bout.write(b);
/*      */     } 
/*  142 */     bout.close();
/*  143 */     DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
/*  144 */     fact.setNamespaceAware(true);
/*  145 */     DocumentBuilder db = fact.newDocumentBuilder();
/*  146 */     db.setEntityResolver(new SafeEmptyEntityResolver());
/*  147 */     this.domDocument = db.parse(new ByteArrayInputStream(bout.toByteArray()));
/*  148 */     extractNodes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void extractNodes() {
/*  156 */     Map<String, Node> xfaNodes = extractXFANodes(this.domDocument);
/*      */     
/*  158 */     if (xfaNodes.containsKey("template")) {
/*  159 */       this.templateNode = xfaNodes.get("template");
/*  160 */       this.templateSom = new Xml2SomTemplate(this.templateNode);
/*      */     } 
/*  162 */     if (xfaNodes.containsKey("datasets")) {
/*  163 */       this.datasetsNode = xfaNodes.get("datasets");
/*  164 */       Node dataNode = findDataNode(this.datasetsNode);
/*  165 */       this.datasetsSom = new Xml2SomDatasets((dataNode != null) ? dataNode : this.datasetsNode.getFirstChild());
/*      */     } 
/*  167 */     if (this.datasetsNode == null)
/*  168 */       createDatasetsNode(this.domDocument.getFirstChild()); 
/*      */   }
/*      */   
/*      */   private Node findDataNode(Node datasetsNode) {
/*  172 */     NodeList childNodes = datasetsNode.getChildNodes();
/*  173 */     for (int i = 0; i < childNodes.getLength(); i++) {
/*  174 */       if (childNodes.item(i).getNodeName().equals("xfa:data")) {
/*  175 */         return childNodes.item(i);
/*      */       }
/*      */     } 
/*  178 */     return null;
/*      */   }
/*      */   
/*      */   public static Map<String, Node> extractXFANodes(Document domDocument) {
/*  182 */     Map<String, Node> xfaNodes = new HashMap<String, Node>();
/*  183 */     Node n = domDocument.getFirstChild();
/*  184 */     while (n.getChildNodes().getLength() == 0) {
/*  185 */       n = n.getNextSibling();
/*      */     }
/*  187 */     n = n.getFirstChild();
/*  188 */     while (n != null) {
/*  189 */       if (n.getNodeType() == 1) {
/*  190 */         String s = n.getLocalName();
/*  191 */         xfaNodes.put(s, n);
/*      */       } 
/*  193 */       n = n.getNextSibling();
/*      */     } 
/*      */     
/*  196 */     return xfaNodes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createDatasetsNode(Node n) {
/*  203 */     while (n.getChildNodes().getLength() == 0) {
/*  204 */       n = n.getNextSibling();
/*      */     }
/*  206 */     if (n != null) {
/*  207 */       Element e = n.getOwnerDocument().createElement("xfa:datasets");
/*  208 */       e.setAttribute("xmlns:xfa", "http://www.xfa.org/schema/xfa-data/1.0/");
/*  209 */       this.datasetsNode = e;
/*  210 */       n.appendChild(this.datasetsNode);
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
/*      */   public static void setXfa(XfaForm form, PdfReader reader, PdfWriter writer) throws IOException {
/*  222 */     PdfDictionary af = (PdfDictionary)PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.ACROFORM));
/*  223 */     if (af == null) {
/*      */       return;
/*      */     }
/*  226 */     PdfObject xfa = getXfaObject(reader);
/*  227 */     if (xfa.isArray()) {
/*  228 */       PdfArray ar = (PdfArray)xfa;
/*  229 */       int t = -1;
/*  230 */       int d = -1;
/*  231 */       for (int k = 0; k < ar.size(); k += 2) {
/*  232 */         PdfString s = ar.getAsString(k);
/*  233 */         if ("template".equals(s.toString())) {
/*  234 */           t = k + 1;
/*      */         }
/*  236 */         if ("datasets".equals(s.toString())) {
/*  237 */           d = k + 1;
/*      */         }
/*      */       } 
/*  240 */       if (t > -1 && d > -1) {
/*  241 */         reader.killXref(ar.getAsIndirectObject(t));
/*  242 */         reader.killXref(ar.getAsIndirectObject(d));
/*  243 */         PdfStream tStream = new PdfStream(serializeDoc(form.templateNode));
/*  244 */         tStream.flateCompress(writer.getCompressionLevel());
/*  245 */         ar.set(t, writer.addToBody(tStream).getIndirectReference());
/*  246 */         PdfStream dStream = new PdfStream(serializeDoc(form.datasetsNode));
/*  247 */         dStream.flateCompress(writer.getCompressionLevel());
/*  248 */         ar.set(d, writer.addToBody(dStream).getIndirectReference());
/*  249 */         af.put(PdfName.XFA, new PdfArray(ar));
/*      */         return;
/*      */       } 
/*      */     } 
/*  253 */     reader.killXref(af.get(PdfName.XFA));
/*  254 */     PdfStream str = new PdfStream(serializeDoc(form.domDocument));
/*  255 */     str.flateCompress(writer.getCompressionLevel());
/*  256 */     PdfIndirectReference ref = writer.addToBody(str).getIndirectReference();
/*  257 */     af.put(PdfName.XFA, ref);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setXfa(PdfWriter writer) throws IOException {
/*  266 */     setXfa(this, this.reader, writer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] serializeDoc(Node n) throws IOException {
/*  276 */     XmlDomWriter xw = new XmlDomWriter();
/*  277 */     ByteArrayOutputStream fout = new ByteArrayOutputStream();
/*  278 */     xw.setOutput(fout, null);
/*  279 */     xw.setCanonical(false);
/*  280 */     xw.write(n);
/*  281 */     fout.close();
/*  282 */     return fout.toByteArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isXfaPresent() {
/*  290 */     return this.xfaPresent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Document getDomDocument() {
/*  298 */     return this.domDocument;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String findFieldName(String name, AcroFields af) {
/*  309 */     Map<String, AcroFields.Item> items = af.getFields();
/*  310 */     if (items.containsKey(name))
/*  311 */       return name; 
/*  312 */     if (this.acroFieldsSom == null)
/*  313 */       if (items.isEmpty() && this.xfaPresent) {
/*  314 */         this.acroFieldsSom = new AcroFieldsSearch(this.datasetsSom.getName2Node().keySet());
/*      */       } else {
/*  316 */         this.acroFieldsSom = new AcroFieldsSearch(items.keySet());
/*      */       }  
/*  318 */     if (this.acroFieldsSom.getAcroShort2LongName().containsKey(name))
/*  319 */       return this.acroFieldsSom.getAcroShort2LongName().get(name); 
/*  320 */     return this.acroFieldsSom.inverseSearchGlobal(Xml2Som.splitParts(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String findDatasetsName(String name) {
/*  330 */     if (this.datasetsSom.getName2Node().containsKey(name))
/*  331 */       return name; 
/*  332 */     return this.datasetsSom.inverseSearchGlobal(Xml2Som.splitParts(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Node findDatasetsNode(String name) {
/*  342 */     if (name == null)
/*  343 */       return null; 
/*  344 */     name = findDatasetsName(name);
/*  345 */     if (name == null)
/*  346 */       return null; 
/*  347 */     return this.datasetsSom.getName2Node().get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getNodeText(Node n) {
/*  356 */     if (n == null)
/*  357 */       return ""; 
/*  358 */     return getNodeText(n, "");
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getNodeText(Node n, String name) {
/*  363 */     Node n2 = n.getFirstChild();
/*  364 */     while (n2 != null) {
/*  365 */       if (n2.getNodeType() == 1) {
/*  366 */         name = getNodeText(n2, name);
/*      */       }
/*  368 */       else if (n2.getNodeType() == 3) {
/*  369 */         name = name + n2.getNodeValue();
/*      */       } 
/*  371 */       n2 = n2.getNextSibling();
/*      */     } 
/*  373 */     return name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNodeText(Node n, String text) {
/*  383 */     if (n == null)
/*      */       return; 
/*  385 */     Node nc = null;
/*  386 */     while ((nc = n.getFirstChild()) != null) {
/*  387 */       n.removeChild(nc);
/*      */     }
/*  389 */     if (n.getAttributes().getNamedItemNS("http://www.xfa.org/schema/xfa-data/1.0/", "dataNode") != null)
/*  390 */       n.getAttributes().removeNamedItemNS("http://www.xfa.org/schema/xfa-data/1.0/", "dataNode"); 
/*  391 */     n.appendChild(this.domDocument.createTextNode(text));
/*  392 */     this.changed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setXfaPresent(boolean xfaPresent) {
/*  400 */     this.xfaPresent = xfaPresent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDomDocument(Document domDocument) {
/*  408 */     this.domDocument = domDocument;
/*  409 */     extractNodes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfReader getReader() {
/*  417 */     return this.reader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReader(PdfReader reader) {
/*  425 */     this.reader = reader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isChanged() {
/*  433 */     return this.changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChanged(boolean changed) {
/*  441 */     this.changed = changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InverseStore
/*      */   {
/*  449 */     protected ArrayList<String> part = new ArrayList<String>();
/*  450 */     protected ArrayList<Object> follow = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getDefaultName() {
/*  458 */       InverseStore store = this;
/*      */       while (true) {
/*  460 */         Object obj = store.follow.get(0);
/*  461 */         if (obj instanceof String)
/*  462 */           return (String)obj; 
/*  463 */         store = (InverseStore)obj;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isSimilar(String name) {
/*  476 */       int idx = name.indexOf('[');
/*  477 */       name = name.substring(0, idx + 1);
/*  478 */       for (int k = 0; k < this.part.size(); k++) {
/*  479 */         if (((String)this.part.get(k)).startsWith(name))
/*  480 */           return true; 
/*      */       } 
/*  482 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Stack2<T>
/*      */     extends ArrayList<T>
/*      */   {
/*      */     private static final long serialVersionUID = -7451476576174095212L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T peek() {
/*  498 */       if (size() == 0)
/*  499 */         throw new EmptyStackException(); 
/*  500 */       return get(size() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T pop() {
/*  508 */       if (size() == 0)
/*  509 */         throw new EmptyStackException(); 
/*  510 */       T ret = get(size() - 1);
/*  511 */       remove(size() - 1);
/*  512 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T push(T item) {
/*  521 */       add(item);
/*  522 */       return item;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean empty() {
/*  530 */       return (size() == 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Xml2Som
/*      */   {
/*      */     protected ArrayList<String> order;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected HashMap<String, Node> name2Node;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected HashMap<String, XfaForm.InverseStore> inverseSearch;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected XfaForm.Stack2<String> stack;
/*      */ 
/*      */ 
/*      */     
/*      */     protected int anform;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static String escapeSom(String s) {
/*  565 */       if (s == null)
/*  566 */         return ""; 
/*  567 */       int idx = s.indexOf('.');
/*  568 */       if (idx < 0)
/*  569 */         return s; 
/*  570 */       StringBuffer sb = new StringBuffer();
/*  571 */       int last = 0;
/*  572 */       while (idx >= 0) {
/*  573 */         sb.append(s.substring(last, idx));
/*  574 */         sb.append('\\');
/*  575 */         last = idx;
/*  576 */         idx = s.indexOf('.', idx + 1);
/*      */       } 
/*  578 */       sb.append(s.substring(last));
/*  579 */       return sb.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static String unescapeSom(String s) {
/*  588 */       int idx = s.indexOf('\\');
/*  589 */       if (idx < 0)
/*  590 */         return s; 
/*  591 */       StringBuffer sb = new StringBuffer();
/*  592 */       int last = 0;
/*  593 */       while (idx >= 0) {
/*  594 */         sb.append(s.substring(last, idx));
/*  595 */         last = idx + 1;
/*  596 */         idx = s.indexOf('\\', idx + 1);
/*      */       } 
/*  598 */       sb.append(s.substring(last));
/*  599 */       return sb.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected String printStack() {
/*  608 */       if (this.stack.empty())
/*  609 */         return ""; 
/*  610 */       StringBuffer s = new StringBuffer();
/*  611 */       for (int k = 0; k < this.stack.size(); k++)
/*  612 */         s.append('.').append(this.stack.get(k)); 
/*  613 */       return s.substring(1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static String getShortName(String s) {
/*  622 */       int idx = s.indexOf(".#subform[");
/*  623 */       if (idx < 0)
/*  624 */         return s; 
/*  625 */       int last = 0;
/*  626 */       StringBuffer sb = new StringBuffer();
/*  627 */       while (idx >= 0) {
/*  628 */         sb.append(s.substring(last, idx));
/*  629 */         idx = s.indexOf("]", idx + 10);
/*  630 */         if (idx < 0)
/*  631 */           return sb.toString(); 
/*  632 */         last = idx + 1;
/*  633 */         idx = s.indexOf(".#subform[", last);
/*      */       } 
/*  635 */       sb.append(s.substring(last));
/*  636 */       return sb.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void inverseSearchAdd(String unstack) {
/*  644 */       inverseSearchAdd(this.inverseSearch, this.stack, unstack);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static void inverseSearchAdd(HashMap<String, XfaForm.InverseStore> inverseSearch, XfaForm.Stack2<String> stack, String unstack) {
/*  654 */       String last = stack.peek();
/*  655 */       XfaForm.InverseStore store = inverseSearch.get(last);
/*  656 */       if (store == null) {
/*  657 */         store = new XfaForm.InverseStore();
/*  658 */         inverseSearch.put(last, store);
/*      */       } 
/*  660 */       for (int k = stack.size() - 2; k >= 0; k--) {
/*  661 */         XfaForm.InverseStore store2; last = stack.get(k);
/*      */         
/*  663 */         int idx = store.part.indexOf(last);
/*  664 */         if (idx < 0) {
/*  665 */           store.part.add(last);
/*  666 */           store2 = new XfaForm.InverseStore();
/*  667 */           store.follow.add(store2);
/*      */         } else {
/*      */           
/*  670 */           store2 = (XfaForm.InverseStore)store.follow.get(idx);
/*  671 */         }  store = store2;
/*      */       } 
/*  673 */       store.part.add("");
/*  674 */       store.follow.add(unstack);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String inverseSearchGlobal(ArrayList<String> parts) {
/*  683 */       if (parts.isEmpty())
/*  684 */         return null; 
/*  685 */       XfaForm.InverseStore store = this.inverseSearch.get(parts.get(parts.size() - 1));
/*  686 */       if (store == null)
/*  687 */         return null; 
/*  688 */       for (int k = parts.size() - 2; k >= 0; k--) {
/*  689 */         String part = parts.get(k);
/*  690 */         int idx = store.part.indexOf(part);
/*  691 */         if (idx < 0) {
/*  692 */           if (store.isSimilar(part))
/*  693 */             return null; 
/*  694 */           return store.getDefaultName();
/*      */         } 
/*  696 */         store = (XfaForm.InverseStore)store.follow.get(idx);
/*      */       } 
/*  698 */       return store.getDefaultName();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static XfaForm.Stack2<String> splitParts(String name) {
/*  707 */       while (name.startsWith("."))
/*  708 */         name = name.substring(1); 
/*  709 */       XfaForm.Stack2<String> parts = new XfaForm.Stack2<String>();
/*  710 */       int last = 0;
/*  711 */       int pos = 0;
/*      */       
/*      */       while (true) {
/*  714 */         pos = last;
/*      */         while (true) {
/*  716 */           pos = name.indexOf('.', pos);
/*  717 */           if (pos < 0)
/*      */             break; 
/*  719 */           if (name.charAt(pos - 1) == '\\') {
/*  720 */             pos++; continue;
/*      */           } 
/*      */           break;
/*      */         } 
/*  724 */         if (pos < 0)
/*      */           break; 
/*  726 */         String str = name.substring(last, pos);
/*  727 */         if (!str.endsWith("]"))
/*  728 */           str = str + "[0]"; 
/*  729 */         parts.add(str);
/*  730 */         last = pos + 1;
/*      */       } 
/*  732 */       String part = name.substring(last);
/*  733 */       if (!part.endsWith("]"))
/*  734 */         part = part + "[0]"; 
/*  735 */       parts.add(part);
/*  736 */       return parts;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ArrayList<String> getOrder() {
/*  744 */       return this.order;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setOrder(ArrayList<String> order) {
/*  752 */       this.order = order;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public HashMap<String, Node> getName2Node() {
/*  760 */       return this.name2Node;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName2Node(HashMap<String, Node> name2Node) {
/*  768 */       this.name2Node = name2Node;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public HashMap<String, XfaForm.InverseStore> getInverseSearch() {
/*  776 */       return this.inverseSearch;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setInverseSearch(HashMap<String, XfaForm.InverseStore> inverseSearch) {
/*  784 */       this.inverseSearch = inverseSearch;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Xml2SomDatasets
/*      */     extends Xml2Som
/*      */   {
/*      */     public Xml2SomDatasets(Node n) {
/*  798 */       this.order = new ArrayList<String>();
/*  799 */       this.name2Node = new HashMap<String, Node>();
/*  800 */       this.stack = new XfaForm.Stack2<String>();
/*  801 */       this.anform = 0;
/*  802 */       this.inverseSearch = new HashMap<String, XfaForm.InverseStore>();
/*  803 */       processDatasetsInternal(n);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Node insertNode(Node n, String shortName) {
/*  813 */       XfaForm.Stack2<String> stack = splitParts(shortName);
/*  814 */       Document doc = n.getOwnerDocument();
/*  815 */       Node n2 = null;
/*  816 */       n = n.getFirstChild();
/*  817 */       while (n.getNodeType() != 1)
/*  818 */         n = n.getNextSibling(); 
/*  819 */       for (int k = 0; k < stack.size(); k++) {
/*  820 */         String part = stack.get(k);
/*  821 */         int idx = part.lastIndexOf('[');
/*  822 */         String name = part.substring(0, idx);
/*  823 */         idx = Integer.parseInt(part.substring(idx + 1, part.length() - 1));
/*  824 */         int found = -1;
/*  825 */         for (n2 = n.getFirstChild(); n2 != null; n2 = n2.getNextSibling()) {
/*  826 */           if (n2.getNodeType() == 1) {
/*  827 */             String s = escapeSom(n2.getLocalName());
/*      */             
/*  829 */             found++;
/*  830 */             if (s.equals(name) && found == idx) {
/*      */               break;
/*      */             }
/*      */           } 
/*      */         } 
/*  835 */         for (; found < idx; found++) {
/*  836 */           n2 = doc.createElementNS((String)null, name);
/*  837 */           n2 = n.appendChild(n2);
/*  838 */           Node attr = doc.createAttributeNS("http://www.xfa.org/schema/xfa-data/1.0/", "dataNode");
/*  839 */           attr.setNodeValue("dataGroup");
/*  840 */           n2.getAttributes().setNamedItemNS(attr);
/*      */         } 
/*  842 */         n = n2;
/*      */       } 
/*  844 */       inverseSearchAdd(this.inverseSearch, stack, shortName);
/*  845 */       this.name2Node.put(shortName, n2);
/*  846 */       this.order.add(shortName);
/*  847 */       return n2;
/*      */     }
/*      */     
/*      */     private static boolean hasChildren(Node n) {
/*  851 */       Node dataNodeN = n.getAttributes().getNamedItemNS("http://www.xfa.org/schema/xfa-data/1.0/", "dataNode");
/*  852 */       if (dataNodeN != null) {
/*  853 */         String dataNode = dataNodeN.getNodeValue();
/*  854 */         if ("dataGroup".equals(dataNode))
/*  855 */           return true; 
/*  856 */         if ("dataValue".equals(dataNode))
/*  857 */           return false; 
/*      */       } 
/*  859 */       if (!n.hasChildNodes())
/*  860 */         return false; 
/*  861 */       Node n2 = n.getFirstChild();
/*  862 */       while (n2 != null) {
/*  863 */         if (n2.getNodeType() == 1) {
/*  864 */           return true;
/*      */         }
/*  866 */         n2 = n2.getNextSibling();
/*      */       } 
/*  868 */       return false;
/*      */     }
/*      */     
/*      */     private void processDatasetsInternal(Node n) {
/*  872 */       if (n != null) {
/*  873 */         HashMap<String, Integer> ss = new HashMap<String, Integer>();
/*  874 */         Node n2 = n.getFirstChild();
/*  875 */         while (n2 != null) {
/*  876 */           if (n2.getNodeType() == 1) {
/*  877 */             String s = escapeSom(n2.getLocalName());
/*  878 */             Integer i = ss.get(s);
/*  879 */             if (i == null) {
/*  880 */               i = Integer.valueOf(0);
/*      */             } else {
/*  882 */               i = Integer.valueOf(i.intValue() + 1);
/*  883 */             }  ss.put(s, i);
/*  884 */             this.stack.push(s + "[" + i.toString() + "]");
/*  885 */             if (hasChildren(n2)) {
/*  886 */               processDatasetsInternal(n2);
/*      */             }
/*  888 */             String unstack = printStack();
/*  889 */             this.order.add(unstack);
/*  890 */             inverseSearchAdd(unstack);
/*  891 */             this.name2Node.put(unstack, n2);
/*  892 */             this.stack.pop();
/*      */           } 
/*  894 */           n2 = n2.getNextSibling();
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
/*      */   public static class AcroFieldsSearch
/*      */     extends Xml2Som
/*      */   {
/*  912 */     private HashMap<String, String> acroShort2LongName = new HashMap<String, String>(); public AcroFieldsSearch(Collection<String> items) {
/*  913 */       for (String string : items) {
/*  914 */         String itemName = string;
/*  915 */         String itemShort = getShortName(itemName);
/*  916 */         this.acroShort2LongName.put(itemShort, itemName);
/*  917 */         inverseSearchAdd(this.inverseSearch, splitParts(itemShort), itemName);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public HashMap<String, String> getAcroShort2LongName() {
/*  927 */       return this.acroShort2LongName;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAcroShort2LongName(HashMap<String, String> acroShort2LongName) {
/*  936 */       this.acroShort2LongName = acroShort2LongName;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Xml2SomTemplate
/*      */     extends Xml2Som
/*      */   {
/*      */     private boolean dynamicForm;
/*      */ 
/*      */     
/*      */     private int templateLevel;
/*      */ 
/*      */     
/*      */     public Xml2SomTemplate(Node n) {
/*  952 */       this.order = new ArrayList<String>();
/*  953 */       this.name2Node = new HashMap<String, Node>();
/*  954 */       this.stack = new XfaForm.Stack2<String>();
/*  955 */       this.anform = 0;
/*  956 */       this.templateLevel = 0;
/*  957 */       this.inverseSearch = new HashMap<String, XfaForm.InverseStore>();
/*  958 */       processTemplate(n, (HashMap<String, Integer>)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getFieldType(String s) {
/*  967 */       Node n = this.name2Node.get(s);
/*  968 */       if (n == null)
/*  969 */         return null; 
/*  970 */       if ("exclGroup".equals(n.getLocalName()))
/*  971 */         return "exclGroup"; 
/*  972 */       Node ui = n.getFirstChild();
/*  973 */       while (ui != null && (
/*  974 */         ui.getNodeType() != 1 || !"ui".equals(ui.getLocalName())))
/*      */       {
/*      */         
/*  977 */         ui = ui.getNextSibling();
/*      */       }
/*  979 */       if (ui == null)
/*  980 */         return null; 
/*  981 */       Node type = ui.getFirstChild();
/*  982 */       while (type != null) {
/*  983 */         if (type.getNodeType() == 1 && (!"extras".equals(type.getLocalName()) || !"picture".equals(type.getLocalName()))) {
/*  984 */           return type.getLocalName();
/*      */         }
/*  986 */         type = type.getNextSibling();
/*      */       } 
/*  988 */       return null;
/*      */     }
/*      */     
/*      */     private void processTemplate(Node n, HashMap<String, Integer> ff) {
/*  992 */       if (ff == null)
/*  993 */         ff = new HashMap<String, Integer>(); 
/*  994 */       HashMap<String, Integer> ss = new HashMap<String, Integer>();
/*  995 */       Node n2 = n.getFirstChild();
/*  996 */       while (n2 != null) {
/*  997 */         if (n2.getNodeType() == 1) {
/*  998 */           String s = n2.getLocalName();
/*  999 */           if ("subform".equals(s)) {
/* 1000 */             Integer i; Node name = n2.getAttributes().getNamedItem("name");
/* 1001 */             String nn = "#subform";
/* 1002 */             boolean annon = true;
/* 1003 */             if (name != null) {
/* 1004 */               nn = escapeSom(name.getNodeValue());
/* 1005 */               annon = false;
/*      */             } 
/*      */             
/* 1008 */             if (annon) {
/* 1009 */               i = Integer.valueOf(this.anform);
/* 1010 */               this.anform++;
/*      */             } else {
/*      */               
/* 1013 */               i = ss.get(nn);
/* 1014 */               if (i == null) {
/* 1015 */                 i = Integer.valueOf(0);
/*      */               } else {
/* 1017 */                 i = Integer.valueOf(i.intValue() + 1);
/* 1018 */               }  ss.put(nn, i);
/*      */             } 
/* 1020 */             this.stack.push(nn + "[" + i.toString() + "]");
/* 1021 */             this.templateLevel++;
/* 1022 */             if (annon) {
/* 1023 */               processTemplate(n2, ff);
/*      */             } else {
/* 1025 */               processTemplate(n2, (HashMap<String, Integer>)null);
/* 1026 */             }  this.templateLevel--;
/* 1027 */             this.stack.pop();
/*      */           }
/* 1029 */           else if ("field".equals(s) || "exclGroup".equals(s)) {
/* 1030 */             Node name = n2.getAttributes().getNamedItem("name");
/* 1031 */             if (name != null) {
/* 1032 */               String nn = escapeSom(name.getNodeValue());
/* 1033 */               Integer i = ff.get(nn);
/* 1034 */               if (i == null) {
/* 1035 */                 i = Integer.valueOf(0);
/*      */               } else {
/* 1037 */                 i = Integer.valueOf(i.intValue() + 1);
/* 1038 */               }  ff.put(nn, i);
/* 1039 */               this.stack.push(nn + "[" + i.toString() + "]");
/* 1040 */               String unstack = printStack();
/* 1041 */               this.order.add(unstack);
/* 1042 */               inverseSearchAdd(unstack);
/* 1043 */               this.name2Node.put(unstack, n2);
/* 1044 */               this.stack.pop();
/*      */             }
/*      */           
/* 1047 */           } else if (!this.dynamicForm && this.templateLevel > 0 && "occur".equals(s)) {
/* 1048 */             int initial = 1;
/* 1049 */             int min = 1;
/* 1050 */             int max = 1;
/* 1051 */             Node a = n2.getAttributes().getNamedItem("initial");
/* 1052 */             if (a != null)
/* 1053 */               try { initial = Integer.parseInt(a.getNodeValue().trim()); } catch (Exception exception) {} 
/* 1054 */             a = n2.getAttributes().getNamedItem("min");
/* 1055 */             if (a != null)
/* 1056 */               try { min = Integer.parseInt(a.getNodeValue().trim()); } catch (Exception exception) {} 
/* 1057 */             a = n2.getAttributes().getNamedItem("max");
/* 1058 */             if (a != null)
/* 1059 */               try { max = Integer.parseInt(a.getNodeValue().trim()); } catch (Exception exception) {} 
/* 1060 */             if (initial != min || min != max)
/* 1061 */               this.dynamicForm = true; 
/*      */           } 
/*      */         } 
/* 1064 */         n2 = n2.getNextSibling();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isDynamicForm() {
/* 1075 */       return this.dynamicForm;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setDynamicForm(boolean dynamicForm) {
/* 1083 */       this.dynamicForm = dynamicForm;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Xml2SomTemplate getTemplateSom() {
/* 1092 */     return this.templateSom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTemplateSom(Xml2SomTemplate templateSom) {
/* 1100 */     this.templateSom = templateSom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Xml2SomDatasets getDatasetsSom() {
/* 1108 */     return this.datasetsSom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDatasetsSom(Xml2SomDatasets datasetsSom) {
/* 1116 */     this.datasetsSom = datasetsSom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AcroFieldsSearch getAcroFieldsSom() {
/* 1124 */     return this.acroFieldsSom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcroFieldsSom(AcroFieldsSearch acroFieldsSom) {
/* 1132 */     this.acroFieldsSom = acroFieldsSom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Node getDatasetsNode() {
/* 1140 */     return this.datasetsNode;
/*      */   }
/*      */   
/*      */   public void fillXfaForm(File file) throws IOException {
/* 1144 */     fillXfaForm(file, false);
/*      */   }
/*      */   public void fillXfaForm(File file, boolean readOnly) throws IOException {
/* 1147 */     fillXfaForm(new FileInputStream(file), readOnly);
/*      */   }
/*      */   
/*      */   public void fillXfaForm(InputStream is) throws IOException {
/* 1151 */     fillXfaForm(is, false);
/*      */   }
/*      */   public void fillXfaForm(InputStream is, boolean readOnly) throws IOException {
/* 1154 */     fillXfaForm(new InputSource(is), readOnly);
/*      */   }
/*      */   
/*      */   public void fillXfaForm(InputSource is) throws IOException {
/* 1158 */     fillXfaForm(is, false);
/*      */   }
/*      */   public void fillXfaForm(InputSource is, boolean readOnly) throws IOException {
/* 1161 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*      */     
/*      */     try {
/* 1164 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 1165 */       db.setEntityResolver(new SafeEmptyEntityResolver());
/* 1166 */       Document newdoc = db.parse(is);
/* 1167 */       fillXfaForm(newdoc.getDocumentElement(), readOnly);
/* 1168 */     } catch (ParserConfigurationException e) {
/* 1169 */       throw new ExceptionConverter(e);
/* 1170 */     } catch (SAXException e) {
/* 1171 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void fillXfaForm(Node node) {
/* 1176 */     fillXfaForm(node, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillXfaForm(Node node, boolean readOnly) {
/* 1183 */     if (readOnly) {
/* 1184 */       NodeList nodeList = this.domDocument.getElementsByTagName("field");
/* 1185 */       for (int i = 0; i < nodeList.getLength(); i++) {
/* 1186 */         ((Element)nodeList.item(i)).setAttribute("access", "readOnly");
/*      */       }
/*      */     } 
/* 1189 */     NodeList allChilds = this.datasetsNode.getChildNodes();
/* 1190 */     int len = allChilds.getLength();
/* 1191 */     Node data = null;
/* 1192 */     for (int k = 0; k < len; k++) {
/* 1193 */       Node n = allChilds.item(k);
/* 1194 */       if (n.getNodeType() == 1 && n.getLocalName().equals("data") && "http://www.xfa.org/schema/xfa-data/1.0/".equals(n.getNamespaceURI())) {
/* 1195 */         data = n;
/*      */         break;
/*      */       } 
/*      */     } 
/* 1199 */     if (data == null) {
/* 1200 */       data = this.datasetsNode.getOwnerDocument().createElementNS("http://www.xfa.org/schema/xfa-data/1.0/", "xfa:data");
/* 1201 */       this.datasetsNode.appendChild(data);
/*      */     } 
/* 1203 */     NodeList list = data.getChildNodes();
/* 1204 */     if (list.getLength() == 0) {
/* 1205 */       data.appendChild(this.domDocument.importNode(node, true));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1210 */       Node firstNode = getFirstElementNode(data);
/* 1211 */       if (firstNode != null)
/* 1212 */         data.replaceChild(this.domDocument.importNode(node, true), firstNode); 
/*      */     } 
/* 1214 */     extractNodes();
/* 1215 */     setChanged(true);
/*      */   }
/*      */   
/*      */   private Node getFirstElementNode(Node src) {
/* 1219 */     Node result = null;
/* 1220 */     NodeList list = src.getChildNodes();
/* 1221 */     for (int i = 0; i < list.getLength(); i++) {
/* 1222 */       if (list.item(i).getNodeType() == 1) {
/* 1223 */         result = list.item(i);
/*      */         break;
/*      */       } 
/*      */     } 
/* 1227 */     return result;
/*      */   }
/*      */   private static class SafeEmptyEntityResolver implements EntityResolver { private SafeEmptyEntityResolver() {}
/*      */     
/*      */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 1232 */       return new InputSource(new StringReader(""));
/*      */     } }
/*      */ 
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/XfaForm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */