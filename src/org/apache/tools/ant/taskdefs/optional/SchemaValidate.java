/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaValidate
/*     */   extends XMLValidateTask
/*     */ {
/*     */   public static final String ERROR_SAX_1 = "SAX1 parsers are not supported";
/*     */   public static final String ERROR_NO_XSD_SUPPORT = "Parser does not support Xerces or JAXP schema features";
/*     */   public static final String ERROR_TOO_MANY_DEFAULT_SCHEMAS = "Only one of defaultSchemaFile and defaultSchemaURL allowed";
/*     */   public static final String ERROR_PARSER_CREATION_FAILURE = "Could not create parser";
/*     */   public static final String MESSAGE_ADDING_SCHEMA = "Adding schema ";
/*     */   public static final String ERROR_DUPLICATE_SCHEMA = "Duplicate declaration of schema ";
/*  76 */   private Map<String, SchemaLocation> schemaLocations = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fullChecking = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean disableDTD = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SchemaLocation anonymousSchema;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws BuildException {
/*  99 */     super.init();
/*     */     
/* 101 */     setLenient(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean enableXercesSchemaValidation() {
/*     */     try {
/* 110 */       setFeature("http://apache.org/xml/features/validation/schema", true);
/*     */       
/* 112 */       setNoNamespaceSchemaProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation");
/* 113 */     } catch (BuildException e) {
/* 114 */       log(e.toString(), 3);
/* 115 */       return false;
/*     */     } 
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setNoNamespaceSchemaProperty(String property) {
/* 125 */     String anonSchema = getNoNamespaceSchemaURL();
/* 126 */     if (anonSchema != null) {
/* 127 */       setProperty(property, anonSchema);
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
/*     */   public boolean enableJAXP12SchemaValidation() {
/*     */     try {
/* 140 */       setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */       
/* 142 */       setNoNamespaceSchemaProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
/* 143 */     } catch (BuildException e) {
/* 144 */       log(e.toString(), 3);
/* 145 */       return false;
/*     */     } 
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredSchema(SchemaLocation location) {
/* 157 */     log("adding schema " + location, 4);
/* 158 */     location.validateNamespace();
/* 159 */     SchemaLocation old = this.schemaLocations.get(location.getNamespace());
/* 160 */     if (old != null && !old.equals(location)) {
/* 161 */       throw new BuildException("Duplicate declaration of schema " + location);
/*     */     }
/* 163 */     this.schemaLocations.put(location.getNamespace(), location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFullChecking(boolean fullChecking) {
/* 171 */     this.fullChecking = fullChecking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createAnonymousSchema() {
/* 179 */     if (this.anonymousSchema == null) {
/* 180 */       this.anonymousSchema = new SchemaLocation();
/*     */     }
/* 182 */     this.anonymousSchema.setNamespace("(no namespace)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoNamespaceURL(String defaultSchemaURL) {
/* 190 */     createAnonymousSchema();
/* 191 */     this.anonymousSchema.setUrl(defaultSchemaURL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoNamespaceFile(File defaultSchemaFile) {
/* 199 */     createAnonymousSchema();
/* 200 */     this.anonymousSchema.setFile(defaultSchemaFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisableDTD(boolean disableDTD) {
/* 208 */     this.disableDTD = disableDTD;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initValidator() {
/* 219 */     super.initValidator();
/*     */     
/* 221 */     if (isSax1Parser()) {
/* 222 */       throw new BuildException("SAX1 parsers are not supported");
/*     */     }
/*     */ 
/*     */     
/* 226 */     setFeature("http://xml.org/sax/features/namespaces", true);
/* 227 */     if (!enableXercesSchemaValidation() && !enableJAXP12SchemaValidation())
/*     */     {
/* 229 */       throw new BuildException("Parser does not support Xerces or JAXP schema features");
/*     */     }
/*     */ 
/*     */     
/* 233 */     setFeature("http://apache.org/xml/features/validation/schema-full-checking", this.fullChecking);
/*     */ 
/*     */     
/* 236 */     setFeatureIfSupported("http://apache.org/xml/features/disallow-doctype-decl", this.disableDTD);
/*     */ 
/*     */     
/* 239 */     addSchemaLocations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMLReader createDefaultReader() {
/* 250 */     SAXParserFactory factory = SAXParserFactory.newInstance();
/* 251 */     factory.setValidating(true);
/* 252 */     factory.setNamespaceAware(true);
/* 253 */     XMLReader reader = null;
/*     */     try {
/* 255 */       SAXParser saxParser = factory.newSAXParser();
/* 256 */       reader = saxParser.getXMLReader();
/* 257 */     } catch (ParserConfigurationException|org.xml.sax.SAXException e) {
/* 258 */       throw new BuildException("Could not create parser", e);
/*     */     } 
/* 260 */     return reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSchemaLocations() {
/* 268 */     if (!this.schemaLocations.isEmpty()) {
/*     */ 
/*     */ 
/*     */       
/* 272 */       String joinedValue = this.schemaLocations.values().stream().map(SchemaLocation::getURIandLocation).peek(tuple -> log("Adding schema " + tuple, 3)).collect(Collectors.joining(" "));
/*     */       
/* 274 */       setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", joinedValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getNoNamespaceSchemaURL() {
/* 283 */     return (this.anonymousSchema == null) ? null : 
/* 284 */       this.anonymousSchema.getSchemaLocationURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setFeatureIfSupported(String feature, boolean value) {
/*     */     try {
/* 295 */       getXmlReader().setFeature(feature, value);
/* 296 */     } catch (SAXNotRecognizedException e) {
/* 297 */       log("Not recognized: " + feature, 3);
/* 298 */     } catch (SAXNotSupportedException e) {
/* 299 */       log("Not supported: " + feature, 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onSuccessfulValidation(int fileProcessed) {
/* 310 */     log(fileProcessed + " file(s) have been successfully validated.", 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SchemaLocation
/*     */   {
/*     */     private String namespace;
/*     */ 
/*     */ 
/*     */     
/*     */     private File file;
/*     */ 
/*     */ 
/*     */     
/*     */     private String url;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String ERROR_NO_URI = "No namespace URI";
/*     */ 
/*     */     
/*     */     public static final String ERROR_TWO_LOCATIONS = "Both URL and File were given for schema ";
/*     */ 
/*     */     
/*     */     public static final String ERROR_NO_FILE = "File not found: ";
/*     */ 
/*     */     
/*     */     public static final String ERROR_NO_URL_REPRESENTATION = "Cannot make a URL of ";
/*     */ 
/*     */     
/*     */     public static final String ERROR_NO_LOCATION = "No file or URL supplied for the schema ";
/*     */ 
/*     */ 
/*     */     
/*     */     public String getNamespace() {
/* 347 */       return this.namespace;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setNamespace(String namespace) {
/* 355 */       this.namespace = namespace;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public File getFile() {
/* 363 */       return this.file;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFile(File file) {
/* 372 */       this.file = file;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getUrl() {
/* 380 */       return this.url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setUrl(String url) {
/* 388 */       this.url = url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSchemaLocationURL() {
/* 397 */       boolean hasFile = (this.file != null);
/* 398 */       boolean hasURL = isSet(this.url);
/*     */       
/* 400 */       if (!hasFile && !hasURL) {
/* 401 */         throw new BuildException("No file or URL supplied for the schema " + this.namespace);
/*     */       }
/* 403 */       if (hasFile && hasURL) {
/* 404 */         throw new BuildException("Both URL and File were given for schema " + this.namespace);
/*     */       }
/* 406 */       String schema = this.url;
/* 407 */       if (hasFile) {
/* 408 */         if (!this.file.exists()) {
/* 409 */           throw new BuildException("File not found: " + this.file);
/*     */         }
/*     */         
/*     */         try {
/* 413 */           schema = FileUtils.getFileUtils().getFileURL(this.file).toString();
/* 414 */         } catch (MalformedURLException e) {
/*     */           
/* 416 */           throw new BuildException("Cannot make a URL of " + this.file, e);
/*     */         } 
/*     */       } 
/* 419 */       return schema;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getURIandLocation() throws BuildException {
/* 429 */       validateNamespace();
/* 430 */       return this.namespace + ' ' + getSchemaLocationURL();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void validateNamespace() {
/* 438 */       if (!isSet(getNamespace())) {
/* 439 */         throw new BuildException("No namespace URI");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isSet(String property) {
/* 449 */       return (property != null && !property.isEmpty());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 460 */       if (this == o) {
/* 461 */         return true;
/*     */       }
/* 463 */       if (!(o instanceof SchemaLocation)) {
/* 464 */         return false;
/*     */       }
/*     */       
/* 467 */       SchemaLocation schemaLocation = (SchemaLocation)o;
/*     */       
/* 469 */       if ((this.file == null) ? (schemaLocation.file == null) : this.file.equals(schemaLocation.file)) if (((this.namespace == null) ? (schemaLocation.namespace == null) : this.namespace
/* 470 */           .equals(schemaLocation.namespace)) && ((this.url == null) ? (schemaLocation.url == null) : this.url
/* 471 */           .equals(schemaLocation.url)));
/*     */       
/*     */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 482 */       int result = (this.namespace == null) ? 0 : this.namespace.hashCode();
/* 483 */       result = 29 * result + ((this.file == null) ? 0 : this.file.hashCode());
/* 484 */       result = 29 * result + ((this.url == null) ? 0 : this.url.hashCode());
/*     */       
/* 486 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 496 */       return ((this.namespace == null) ? "(anonymous)" : this.namespace) + (
/* 497 */         (this.url == null) ? "" : (" " + this.url)) + (
/* 498 */         (this.file == null) ? "" : (" " + this.file.getAbsolutePath()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/SchemaValidate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */