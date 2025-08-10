/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.HandlerBase;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class DescriptorHandler
/*     */   extends HandlerBase
/*     */ {
/*     */   private static final int DEFAULT_HASH_TABLE_SIZE = 10;
/*     */   private static final int STATE_LOOKING_EJBJAR = 1;
/*     */   private static final int STATE_IN_EJBJAR = 2;
/*     */   private static final int STATE_IN_BEANS = 3;
/*     */   private static final int STATE_IN_SESSION = 4;
/*     */   private static final int STATE_IN_ENTITY = 5;
/*     */   private static final int STATE_IN_MESSAGE = 6;
/*     */   private Task owningTask;
/*  57 */   private String publicId = null;
/*     */   
/*     */   private static final String EJB_REF = "ejb-ref";
/*     */   
/*     */   private static final String EJB_LOCAL_REF = "ejb-local-ref";
/*     */   
/*     */   private static final String HOME_INTERFACE = "home";
/*     */   
/*     */   private static final String REMOTE_INTERFACE = "remote";
/*     */   
/*     */   private static final String LOCAL_HOME_INTERFACE = "local-home";
/*     */   
/*     */   private static final String LOCAL_INTERFACE = "local";
/*     */   
/*     */   private static final String BEAN_CLASS = "ejb-class";
/*     */   
/*     */   private static final String PK_CLASS = "prim-key-class";
/*     */   
/*     */   private static final String EJB_NAME = "ejb-name";
/*     */   private static final String EJB_JAR = "ejb-jar";
/*     */   private static final String ENTERPRISE_BEANS = "enterprise-beans";
/*     */   private static final String ENTITY_BEAN = "entity";
/*     */   private static final String SESSION_BEAN = "session";
/*     */   private static final String MESSAGE_BEAN = "message-driven";
/*  81 */   private int parseState = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected String currentElement = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected String currentText = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected Hashtable<String, File> ejbFiles = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   protected String ejbName = null;
/*     */   
/* 108 */   private Map<String, File> fileDTDs = new Hashtable<>();
/*     */   
/* 110 */   private Map<String, String> resourceDTDs = new Hashtable<>();
/*     */   
/*     */   private boolean inEJBRef = false;
/*     */   
/* 114 */   private Map<String, URL> urlDTDs = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File srcDir;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DescriptorHandler(Task task, File srcDir) {
/* 129 */     this.owningTask = task;
/* 130 */     this.srcDir = srcDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDTD(String publicId, String location) {
/* 141 */     if (location == null) {
/*     */       return;
/*     */     }
/*     */     
/* 145 */     File fileDTD = new File(location);
/* 146 */     if (!fileDTD.exists())
/*     */     {
/* 148 */       fileDTD = this.owningTask.getProject().resolveFile(location);
/*     */     }
/*     */     
/* 151 */     if (fileDTD.exists()) {
/* 152 */       if (publicId != null) {
/* 153 */         this.fileDTDs.put(publicId, fileDTD);
/* 154 */         this.owningTask.log("Mapped publicId " + publicId + " to file " + fileDTD, 3);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 160 */     if (getClass().getResource(location) != null && 
/* 161 */       publicId != null) {
/* 162 */       this.resourceDTDs.put(publicId, location);
/* 163 */       this.owningTask.log("Mapped publicId " + publicId + " to resource " + location, 3);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 169 */       if (publicId != null) {
/* 170 */         URL urldtd = new URL(location);
/* 171 */         this.urlDTDs.put(publicId, urldtd);
/*     */       } 
/* 173 */     } catch (MalformedURLException malformedURLException) {}
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
/*     */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
/* 192 */     this.publicId = publicId;
/*     */     
/* 194 */     File dtdFile = this.fileDTDs.get(publicId);
/* 195 */     if (dtdFile != null) {
/*     */       try {
/* 197 */         this.owningTask.log("Resolved " + publicId + " to local file " + dtdFile, 3);
/*     */         
/* 199 */         return new InputSource(Files.newInputStream(dtdFile.toPath(), new java.nio.file.OpenOption[0]));
/* 200 */       } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 205 */     String dtdResourceName = this.resourceDTDs.get(publicId);
/* 206 */     if (dtdResourceName != null) {
/* 207 */       InputStream is = getClass().getResourceAsStream(dtdResourceName);
/* 208 */       if (is != null) {
/* 209 */         this.owningTask.log("Resolved " + publicId + " to local resource " + dtdResourceName, 3);
/*     */         
/* 211 */         return new InputSource(is);
/*     */       } 
/*     */     } 
/*     */     
/* 215 */     URL dtdUrl = this.urlDTDs.get(publicId);
/* 216 */     if (dtdUrl != null) {
/*     */       try {
/* 218 */         InputStream is = dtdUrl.openStream();
/* 219 */         this.owningTask.log("Resolved " + publicId + " to url " + dtdUrl, 3);
/*     */         
/* 221 */         return new InputSource(is);
/* 222 */       } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 227 */     this.owningTask.log("Could not resolve (publicId: " + publicId + ", systemId: " + systemId + ") to a local entity", 2);
/*     */ 
/*     */     
/* 230 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hashtable<String, File> getFiles() {
/* 238 */     return (this.ejbFiles == null) ? new Hashtable<>(Collections.emptyMap()) : this.ejbFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPublicId() {
/* 246 */     return this.publicId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEjbName() {
/* 254 */     return this.ejbName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/* 264 */     this.ejbFiles = new Hashtable<>(10, 1.0F);
/* 265 */     this.currentElement = null;
/* 266 */     this.inEJBRef = false;
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
/*     */   public void startElement(String name, AttributeList attrs) throws SAXException {
/* 280 */     this.currentElement = name;
/* 281 */     this.currentText = "";
/* 282 */     if ("ejb-ref".equals(name) || "ejb-local-ref".equals(name)) {
/* 283 */       this.inEJBRef = true;
/* 284 */     } else if (this.parseState == 1 && "ejb-jar".equals(name)) {
/* 285 */       this.parseState = 2;
/* 286 */     } else if (this.parseState == 2 && "enterprise-beans".equals(name)) {
/* 287 */       this.parseState = 3;
/* 288 */     } else if (this.parseState == 3 && "session".equals(name)) {
/* 289 */       this.parseState = 4;
/* 290 */     } else if (this.parseState == 3 && "entity".equals(name)) {
/* 291 */       this.parseState = 5;
/* 292 */     } else if (this.parseState == 3 && "message-driven".equals(name)) {
/* 293 */       this.parseState = 6;
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
/*     */   public void endElement(String name) throws SAXException {
/* 309 */     processElement();
/* 310 */     this.currentText = "";
/* 311 */     this.currentElement = "";
/* 312 */     if (name.equals("ejb-ref") || name.equals("ejb-local-ref")) {
/* 313 */       this.inEJBRef = false;
/* 314 */     } else if (this.parseState == 5 && name.equals("entity")) {
/* 315 */       this.parseState = 3;
/* 316 */     } else if (this.parseState == 4 && name.equals("session")) {
/* 317 */       this.parseState = 3;
/* 318 */     } else if (this.parseState == 6 && name.equals("message-driven")) {
/* 319 */       this.parseState = 3;
/* 320 */     } else if (this.parseState == 3 && name.equals("enterprise-beans")) {
/* 321 */       this.parseState = 2;
/* 322 */     } else if (this.parseState == 2 && name.equals("ejb-jar")) {
/* 323 */       this.parseState = 1;
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
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 346 */     this.currentText += new String(ch, start, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processElement() {
/* 356 */     if (this.inEJBRef || (this.parseState != 5 && this.parseState != 4 && this.parseState != 6)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 363 */     if ("home".equals(this.currentElement) || "remote"
/* 364 */       .equals(this.currentElement) || "local"
/* 365 */       .equals(this.currentElement) || "local-home"
/* 366 */       .equals(this.currentElement) || "ejb-class"
/* 367 */       .equals(this.currentElement) || "prim-key-class"
/* 368 */       .equals(this.currentElement)) {
/*     */ 
/*     */       
/* 371 */       String className = this.currentText.trim();
/*     */ 
/*     */ 
/*     */       
/* 375 */       if (!className.startsWith("java.") && 
/* 376 */         !className.startsWith("javax.")) {
/*     */ 
/*     */         
/* 379 */         className = className.replace('.', File.separatorChar);
/* 380 */         className = className + ".class";
/* 381 */         this.ejbFiles.put(className, new File(this.srcDir, className));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 386 */     if (this.currentElement.equals("ejb-name") && 
/* 387 */       this.ejbName == null)
/* 388 */       this.ejbName = this.currentText.trim(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/DescriptorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */