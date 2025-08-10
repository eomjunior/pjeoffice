/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.XMLCatalog;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlProperty
/*     */   extends Task
/*     */ {
/*     */   private static final String ID = "id";
/*     */   private static final String REF_ID = "refid";
/*     */   private static final String LOCATION = "location";
/*     */   private static final String VALUE = "value";
/*     */   private static final String PATH = "path";
/*     */   private static final String PATHID = "pathid";
/* 187 */   private static final String[] ATTRIBUTES = new String[] { "id", "refid", "location", "value", "path", "pathid" };
/*     */ 
/*     */ 
/*     */   
/* 191 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private Resource src;
/* 194 */   private String prefix = "";
/*     */   private boolean keepRoot = true;
/*     */   private boolean validate = false;
/*     */   private boolean collapseAttributes = false;
/*     */   private boolean semanticAttributes = false;
/*     */   private boolean includeSemanticAttribute = false;
/* 200 */   private File rootDirectory = null;
/* 201 */   private Map<String, String> addedAttributes = new Hashtable<>();
/* 202 */   private XMLCatalog xmlCatalog = new XMLCatalog();
/* 203 */   private String delimiter = ",";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 210 */     super.init();
/* 211 */     this.xmlCatalog.setProject(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityResolver getEntityResolver() {
/* 218 */     return (EntityResolver)this.xmlCatalog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 229 */     Resource r = getResource();
/*     */     
/* 231 */     if (r == null) {
/* 232 */       throw new BuildException("XmlProperty task requires a source resource");
/*     */     }
/*     */     try {
/* 235 */       log("Loading " + this.src, 3);
/*     */       
/* 237 */       if (r.isExists()) {
/*     */         Document document;
/* 239 */         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 240 */         factory.setValidating(this.validate);
/* 241 */         factory.setNamespaceAware(false);
/* 242 */         DocumentBuilder builder = factory.newDocumentBuilder();
/* 243 */         builder.setEntityResolver(getEntityResolver());
/*     */         
/* 245 */         FileProvider fp = (FileProvider)this.src.as(FileProvider.class);
/* 246 */         if (fp != null) {
/* 247 */           document = builder.parse(fp.getFile());
/*     */         } else {
/* 249 */           document = builder.parse(this.src.getInputStream());
/*     */         } 
/* 251 */         Element topElement = document.getDocumentElement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 257 */         this.addedAttributes = new Hashtable<>();
/*     */         
/* 259 */         if (this.keepRoot) {
/* 260 */           addNodeRecursively(topElement, this.prefix, (Object)null);
/*     */         } else {
/* 262 */           NodeList topChildren = topElement.getChildNodes();
/* 263 */           int numChildren = topChildren.getLength();
/* 264 */           for (int i = 0; i < numChildren; i++) {
/* 265 */             addNodeRecursively(topChildren.item(i), this.prefix, (Object)null);
/*     */           }
/*     */         } 
/*     */       } else {
/* 269 */         log("Unable to find property resource: " + r, 3);
/*     */       }
/*     */     
/* 272 */     } catch (SAXException sxe) {
/*     */       
/* 274 */       Exception x = sxe;
/* 275 */       if (sxe.getException() != null) {
/* 276 */         x = sxe.getException();
/*     */       }
/* 278 */       throw new BuildException("Failed to load " + this.src, x);
/* 279 */     } catch (ParserConfigurationException pce) {
/*     */       
/* 281 */       throw new BuildException(pce);
/* 282 */     } catch (IOException ioe) {
/*     */       
/* 284 */       throw new BuildException("Failed to load " + this.src, ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addNodeRecursively(Node node, String prefix, Object container) {
/* 291 */     String nodePrefix = prefix;
/* 292 */     if (node.getNodeType() != 3) {
/* 293 */       if (!prefix.trim().isEmpty()) {
/* 294 */         nodePrefix = nodePrefix + ".";
/*     */       }
/* 296 */       nodePrefix = nodePrefix + node.getNodeName();
/*     */     } 
/*     */     
/* 299 */     Object nodeObject = processNode(node, nodePrefix, container);
/*     */ 
/*     */     
/* 302 */     if (node.hasChildNodes()) {
/* 303 */       NodeList nodeChildren = node.getChildNodes();
/* 304 */       int numChildren = nodeChildren.getLength();
/*     */       
/* 306 */       for (int i = 0; i < numChildren; i++)
/*     */       {
/*     */ 
/*     */         
/* 310 */         addNodeRecursively(nodeChildren.item(i), nodePrefix, nodeObject);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   void addNodeRecursively(Node node, String prefix) {
/* 316 */     addNodeRecursively(node, prefix, (Object)null);
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
/*     */   public Object processNode(Node node, String prefix, Object container) {
/* 340 */     Object addedPath = null;
/*     */ 
/*     */     
/* 343 */     String id = null;
/*     */     
/* 345 */     if (node.hasAttributes()) {
/*     */       
/* 347 */       NamedNodeMap nodeAttributes = node.getAttributes();
/*     */ 
/*     */       
/* 350 */       Node idNode = nodeAttributes.getNamedItem("id");
/* 351 */       id = (this.semanticAttributes && idNode != null) ? idNode.getNodeValue() : null;
/*     */ 
/*     */       
/* 354 */       for (int i = 0; i < nodeAttributes.getLength(); i++) {
/*     */         
/* 356 */         Node attributeNode = nodeAttributes.item(i);
/*     */         
/* 358 */         if (!this.semanticAttributes) {
/* 359 */           String attributeName = getAttributeName(attributeNode);
/* 360 */           String attributeValue = getAttributeValue(attributeNode);
/* 361 */           addProperty(prefix + attributeName, attributeValue, (String)null);
/*     */         } else {
/* 363 */           String nodeName = attributeNode.getNodeName();
/* 364 */           String attributeValue = getAttributeValue(attributeNode);
/*     */ 
/*     */           
/* 367 */           Path containingPath = (container instanceof Path) ? (Path)container : null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 374 */           if (!"id".equals(nodeName))
/*     */           {
/*     */ 
/*     */             
/* 378 */             if (containingPath != null && "path".equals(nodeName)) {
/*     */               
/* 380 */               containingPath.setPath(attributeValue);
/* 381 */             } else if (containingPath != null && container instanceof Path && "refid"
/* 382 */               .equals(nodeName)) {
/*     */               
/* 384 */               containingPath.setPath(attributeValue);
/* 385 */             } else if (containingPath != null && container instanceof Path && "location"
/* 386 */               .equals(nodeName)) {
/*     */ 
/*     */               
/* 389 */               containingPath.setLocation(resolveFile(attributeValue));
/* 390 */             } else if ("pathid".equals(nodeName)) {
/*     */               
/* 392 */               if (container != null) {
/* 393 */                 throw new BuildException("XmlProperty does not support nested paths");
/*     */               }
/* 395 */               addedPath = new Path(getProject());
/* 396 */               getProject().addReference(attributeValue, addedPath);
/*     */             } else {
/*     */               
/* 399 */               String attributeName = getAttributeName(attributeNode);
/* 400 */               addProperty(prefix + attributeName, attributeValue, id);
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 405 */     String nodeText = null;
/* 406 */     boolean emptyNode = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     boolean semanticEmptyOverride = (node.getNodeType() == 1 && this.semanticAttributes && node.hasAttributes() && (node.getAttributes().getNamedItem("value") != null || node.getAttributes().getNamedItem("location") != null || node.getAttributes().getNamedItem("refid") != null || node.getAttributes().getNamedItem("path") != null || node.getAttributes().getNamedItem("pathid") != null));
/* 415 */     if (node.getNodeType() == 3) {
/*     */       
/* 417 */       nodeText = getAttributeValue(node);
/* 418 */     } else if (node.getNodeType() == 1 && node
/* 419 */       .getChildNodes().getLength() == 1 && node
/* 420 */       .getFirstChild().getNodeType() == 4) {
/*     */       
/* 422 */       nodeText = node.getFirstChild().getNodeValue();
/* 423 */       if (nodeText.isEmpty() && !semanticEmptyOverride) {
/* 424 */         emptyNode = true;
/*     */       }
/* 426 */     } else if (node.getNodeType() == 1 && node
/* 427 */       .getChildNodes().getLength() == 0 && !semanticEmptyOverride) {
/*     */       
/* 429 */       nodeText = "";
/* 430 */       emptyNode = true;
/* 431 */     } else if (node.getNodeType() == 1 && node
/* 432 */       .getChildNodes().getLength() == 1 && node
/* 433 */       .getFirstChild().getNodeType() == 3 && node
/* 434 */       .getFirstChild().getNodeValue().isEmpty() && !semanticEmptyOverride) {
/*     */       
/* 436 */       nodeText = "";
/* 437 */       emptyNode = true;
/*     */     } 
/* 439 */     if (nodeText != null) {
/*     */       
/* 441 */       if (this.semanticAttributes && id == null && container instanceof String) {
/* 442 */         id = (String)container;
/*     */       }
/* 444 */       if (!nodeText.trim().isEmpty() || emptyNode) {
/* 445 */         addProperty(prefix, nodeText, id);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 452 */     return (addedPath != null) ? addedPath : id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addProperty(String name, String value, String id) {
/* 460 */     String msg = name + ":" + value;
/* 461 */     if (id != null) {
/* 462 */       msg = msg + "(id=" + id + ")";
/*     */     }
/* 464 */     log(msg, 4);
/*     */     
/* 466 */     if (this.addedAttributes.containsKey(name)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 475 */       value = (String)this.addedAttributes.get(name) + getDelimiter() + value;
/* 476 */       getProject().setProperty(name, value);
/* 477 */       this.addedAttributes.put(name, value);
/* 478 */     } else if (getProject().getProperty(name) == null) {
/* 479 */       getProject().setNewProperty(name, value);
/* 480 */       this.addedAttributes.put(name, value);
/*     */     } else {
/* 482 */       log("Override ignored for property " + name, 3);
/*     */     } 
/* 484 */     if (id != null) {
/* 485 */       getProject().addReference(id, value);
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
/*     */   private String getAttributeName(Node attributeNode) {
/* 497 */     String attributeName = attributeNode.getNodeName();
/*     */     
/* 499 */     if (this.semanticAttributes) {
/*     */ 
/*     */       
/* 502 */       if ("refid".equals(attributeName)) {
/* 503 */         return "";
/*     */       }
/*     */       
/* 506 */       if (!isSemanticAttribute(attributeName) || this.includeSemanticAttribute) {
/* 507 */         return "." + attributeName;
/*     */       }
/* 509 */       return "";
/*     */     } 
/* 511 */     return this.collapseAttributes ? ("." + attributeName) : ("(" + attributeName + ")");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSemanticAttribute(String attributeName) {
/* 518 */     return Arrays.<String>asList(ATTRIBUTES).contains(attributeName);
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
/*     */   private String getAttributeValue(Node attributeNode) {
/* 534 */     String nodeValue = attributeNode.getNodeValue().trim();
/* 535 */     if (this.semanticAttributes) {
/* 536 */       String attributeName = attributeNode.getNodeName();
/* 537 */       nodeValue = getProject().replaceProperties(nodeValue);
/* 538 */       if ("location".equals(attributeName)) {
/* 539 */         File f = resolveFile(nodeValue);
/* 540 */         return f.getPath();
/*     */       } 
/* 542 */       if ("refid".equals(attributeName)) {
/* 543 */         Object ref = getProject().getReference(nodeValue);
/* 544 */         if (ref != null) {
/* 545 */           return ref.toString();
/*     */         }
/*     */       } 
/*     */     } 
/* 549 */     return nodeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File src) {
/* 557 */     setSrcResource((Resource)new FileResource(src));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcResource(Resource src) {
/* 565 */     if (src.isDirectory()) {
/* 566 */       throw new BuildException("the source can't be a directory");
/*     */     }
/* 568 */     if (src.as(FileProvider.class) != null || supportsNonFileResources()) {
/* 569 */       this.src = src;
/*     */     } else {
/* 571 */       throw new BuildException("Only FileSystem resources are supported.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/* 580 */     if (a.size() != 1) {
/* 581 */       throw new BuildException("only single argument resource collections are supported as archives");
/*     */     }
/*     */     
/* 584 */     setSrcResource(a.iterator().next());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 592 */     this.prefix = prefix.trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeeproot(boolean keepRoot) {
/* 602 */     this.keepRoot = keepRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidate(boolean validate) {
/* 610 */     this.validate = validate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCollapseAttributes(boolean collapseAttributes) {
/* 619 */     this.collapseAttributes = collapseAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSemanticAttributes(boolean semanticAttributes) {
/* 627 */     this.semanticAttributes = semanticAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootDirectory(File rootDirectory) {
/* 636 */     this.rootDirectory = rootDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeSemanticAttribute(boolean includeSemanticAttribute) {
/* 646 */     this.includeSemanticAttribute = includeSemanticAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredXMLCatalog(XMLCatalog catalog) {
/* 654 */     this.xmlCatalog.addConfiguredXMLCatalog(catalog);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFile() {
/* 663 */     FileProvider fp = (FileProvider)this.src.as(FileProvider.class);
/* 664 */     return (fp != null) ? fp.getFile() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResource() {
/* 673 */     File f = getFile();
/* 674 */     FileProvider fp = (FileProvider)this.src.as(FileProvider.class);
/* 675 */     return (f == null) ? this.src : (
/* 676 */       (fp != null && fp.getFile().equals(f)) ? this.src : (Resource)new FileResource(f));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/* 683 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getKeeproot() {
/* 690 */     return this.keepRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getValidate() {
/* 697 */     return this.validate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getCollapseAttributes() {
/* 704 */     return this.collapseAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getSemanticAttributes() {
/* 711 */     return this.semanticAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getRootDirectory() {
/* 718 */     return this.rootDirectory;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected boolean getIncludeSementicAttribute() {
/* 723 */     return getIncludeSemanticAttribute();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getIncludeSemanticAttribute() {
/* 729 */     return this.includeSemanticAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File resolveFile(String fileName) {
/* 737 */     return FILE_UTILS.resolveFile((this.rootDirectory == null) ? getProject().getBaseDir() : 
/* 738 */         this.rootDirectory, fileName);
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
/*     */   protected boolean supportsNonFileResources() {
/* 753 */     return getClass().equals(XmlProperty.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDelimiter() {
/* 761 */     return this.delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimiter(String delimiter) {
/* 770 */     this.delimiter = delimiter;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/XmlProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */