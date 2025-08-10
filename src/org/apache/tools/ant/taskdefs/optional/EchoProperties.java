/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.PropertySet;
/*     */ import org.apache.tools.ant.util.DOMElementWriter;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EchoProperties
/*     */   extends Task
/*     */ {
/*     */   private static final String PROPERTIES = "properties";
/*     */   private static final String PROPERTY = "property";
/*     */   private static final String ATTR_NAME = "name";
/*     */   private static final String ATTR_VALUE = "value";
/* 118 */   private File inFile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private File destfile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failonerror = true;
/*     */ 
/*     */ 
/*     */   
/* 133 */   private List<PropertySet> propertySets = new Vector<>();
/*     */   
/* 135 */   private String format = "text";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String prefix;
/*     */ 
/*     */ 
/*     */   
/*     */   private String regex;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcfile(File file) {
/* 150 */     this.inFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File destfile) {
/* 160 */     this.destfile = destfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failonerror) {
/* 171 */     this.failonerror = failonerror;
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
/*     */   public void setPrefix(String prefix) {
/* 188 */     if (prefix != null && !prefix.isEmpty()) {
/* 189 */       this.prefix = prefix;
/* 190 */       PropertySet ps = new PropertySet();
/* 191 */       ps.setProject(getProject());
/* 192 */       ps.appendPrefix(prefix);
/* 193 */       addPropertyset(ps);
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
/*     */   public void setRegex(String regex) {
/* 213 */     if (regex != null && !regex.isEmpty()) {
/* 214 */       this.regex = regex;
/* 215 */       PropertySet ps = new PropertySet();
/* 216 */       ps.setProject(getProject());
/* 217 */       ps.appendRegex(regex);
/* 218 */       addPropertyset(ps);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyset(PropertySet ps) {
/* 228 */     this.propertySets.add(ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormat(FormatAttribute ea) {
/* 236 */     this.format = ea.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FormatAttribute
/*     */     extends EnumeratedAttribute
/*     */   {
/* 244 */     private String[] formats = new String[] { "xml", "text" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 252 */       return this.formats;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 263 */     if (this.prefix != null && this.regex != null) {
/* 264 */       throw new BuildException("Please specify either prefix or regex, but not both", 
/*     */           
/* 266 */           getLocation());
/*     */     }
/*     */     
/* 269 */     Hashtable<Object, Object> allProps = new Hashtable<>();
/*     */ 
/*     */ 
/*     */     
/* 273 */     if (this.inFile == null && this.propertySets.isEmpty()) {
/*     */       
/* 275 */       allProps.putAll(getProject().getProperties());
/* 276 */     } else if (this.inFile != null) {
/* 277 */       if (this.inFile.isDirectory()) {
/* 278 */         String message = "srcfile is a directory!";
/* 279 */         if (this.failonerror) {
/* 280 */           throw new BuildException(message, getLocation());
/*     */         }
/* 282 */         log(message, 0);
/*     */         
/*     */         return;
/*     */       } 
/* 286 */       if (this.inFile.exists() && !this.inFile.canRead()) {
/* 287 */         String message = "Can not read from the specified srcfile!";
/* 288 */         if (this.failonerror) {
/* 289 */           throw new BuildException(message, getLocation());
/*     */         }
/* 291 */         log(message, 0);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 296 */       try { InputStream in = Files.newInputStream(this.inFile.toPath(), new java.nio.file.OpenOption[0]); 
/* 297 */         try { Properties props = new Properties();
/* 298 */           props.load(in);
/* 299 */           allProps.putAll(props);
/* 300 */           if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (FileNotFoundException fnfe)
/*     */       
/* 302 */       { String message = "Could not find file " + this.inFile.getAbsolutePath();
/* 303 */         if (this.failonerror) {
/* 304 */           throw new BuildException(message, fnfe, getLocation());
/*     */         }
/* 306 */         log(message, 1);
/*     */         return; }
/* 308 */       catch (IOException ioe)
/*     */       
/* 310 */       { String message = "Could not read file " + this.inFile.getAbsolutePath();
/* 311 */         if (this.failonerror) {
/* 312 */           throw new BuildException(message, ioe, getLocation());
/*     */         }
/* 314 */         log(message, 1);
/*     */         
/*     */         return; }
/*     */     
/*     */     } 
/*     */     
/* 320 */     Objects.requireNonNull(allProps); this.propertySets.stream().map(PropertySet::getProperties).forEach(allProps::putAll);
/*     */     
/* 322 */     try { OutputStream os = createOutputStream(); 
/* 323 */       try { if (os != null) {
/* 324 */           saveProperties(allProps, os);
/*     */         }
/* 326 */         if (os != null) os.close();  } catch (Throwable throwable) { if (os != null) try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 327 */     { if (this.failonerror) {
/* 328 */         throw new BuildException(ioe, getLocation());
/*     */       }
/* 330 */       log(ioe.getMessage(), 2); }
/*     */   
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
/*     */   protected void saveProperties(Hashtable<Object, Object> allProps, OutputStream os) throws IOException, BuildException {
/* 347 */     final List<Object> keyList = new ArrayList(allProps.keySet());
/*     */     
/* 349 */     Properties props = new Properties()
/*     */       {
/*     */         private static final long serialVersionUID = 5090936442309201654L;
/*     */         
/*     */         public Enumeration<Object> keys() {
/* 354 */           return (Enumeration<Object>)keyList.stream()
/* 355 */             .sorted(Comparator.comparing(Object::toString))
/* 356 */             .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::enumeration));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Set<Map.Entry<Object, Object>> entrySet() {
/* 362 */           Set<Map.Entry<Object, Object>> result = super.entrySet();
/* 363 */           if (JavaEnvUtils.isKaffe()) {
/*     */             
/* 365 */             Set<Map.Entry<Object, Object>> t = new TreeSet<>(Comparator.comparing(Map.Entry::getKey
/*     */                   
/* 367 */                   .andThen(Object::toString)));
/* 368 */             t.addAll(result);
/* 369 */             return t;
/*     */           } 
/* 371 */           return result;
/*     */         }
/*     */       };
/* 374 */     allProps.forEach((k, v) -> props.put(String.valueOf(k), String.valueOf(v)));
/*     */     
/* 376 */     if ("text".equals(this.format)) {
/* 377 */       jdkSaveProperties(props, os, "Ant properties");
/* 378 */     } else if ("xml".equals(this.format)) {
/* 379 */       xmlSaveProperties(props, os);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Tuple
/*     */     implements Comparable<Tuple>
/*     */   {
/*     */     private String key;
/*     */     private String value;
/*     */     
/*     */     private Tuple(String key, String value) {
/* 391 */       this.key = key;
/* 392 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(Tuple o) {
/* 405 */       return Comparator.<String>naturalOrder().compare(this.key, o.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 410 */       if (o == this) {
/* 411 */         return true;
/*     */       }
/* 413 */       if (o == null || o.getClass() != getClass()) {
/* 414 */         return false;
/*     */       }
/* 416 */       Tuple that = (Tuple)o;
/* 417 */       return (Objects.equals(this.key, that.key) && 
/* 418 */         Objects.equals(this.value, that.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 423 */       return Objects.hash(new Object[] { this.key });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Tuple> sortProperties(Properties props) {
/* 429 */     return (List<Tuple>)props.stringPropertyNames().stream()
/* 430 */       .map(k -> new Tuple(k, props.getProperty(k))).sorted()
/* 431 */       .collect(Collectors.toList());
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
/*     */   protected void xmlSaveProperties(Properties props, OutputStream os) throws IOException {
/* 443 */     Document doc = getDocumentBuilder().newDocument();
/* 444 */     Element rootElement = doc.createElement("properties");
/*     */     
/* 446 */     List<Tuple> sorted = sortProperties(props);
/*     */ 
/*     */     
/* 449 */     for (Tuple tuple : sorted) {
/* 450 */       Element propElement = doc.createElement("property");
/* 451 */       propElement.setAttribute("name", tuple.key);
/* 452 */       propElement.setAttribute("value", tuple.value);
/* 453 */       rootElement.appendChild(propElement);
/*     */     } 
/*     */     
/* 456 */     try { Writer wri = new OutputStreamWriter(os, StandardCharsets.UTF_8); 
/* 457 */       try { wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/* 458 */         (new DOMElementWriter()).write(rootElement, wri, 0, "\t");
/* 459 */         wri.flush();
/* 460 */         wri.close(); } catch (Throwable throwable) { try { wri.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 461 */     { throw new BuildException("Unable to write XML file", ioe); }
/*     */   
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
/*     */   protected void jdkSaveProperties(Properties props, OutputStream os, String header) throws IOException {
/*     */     try {
/* 478 */       props.store(os, header);
/* 479 */     } catch (IOException ioe) {
/* 480 */       throw new BuildException(ioe, getLocation());
/*     */     } finally {
/* 482 */       if (os != null) {
/*     */         try {
/* 484 */           os.close();
/* 485 */         } catch (IOException ioex) {
/* 486 */           log("Failed to close output stream");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private OutputStream createOutputStream() throws IOException {
/* 493 */     if (this.destfile == null) {
/* 494 */       return (OutputStream)new LogOutputStream((ProjectComponent)this);
/*     */     }
/* 496 */     if (this.destfile.exists() && this.destfile.isDirectory()) {
/* 497 */       String message = "destfile is a directory!";
/* 498 */       if (this.failonerror) {
/* 499 */         throw new BuildException(message, getLocation());
/*     */       }
/* 501 */       log(message, 0);
/* 502 */       return null;
/*     */     } 
/* 504 */     if (this.destfile.exists() && !this.destfile.canWrite()) {
/* 505 */       String message = "Can not write to the specified destfile!";
/*     */       
/* 507 */       if (this.failonerror) {
/* 508 */         throw new BuildException(message, getLocation());
/*     */       }
/* 510 */       log(message, 0);
/* 511 */       return null;
/*     */     } 
/* 513 */     return Files.newOutputStream(this.destfile.toPath(), new java.nio.file.OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DocumentBuilder getDocumentBuilder() {
/*     */     try {
/* 523 */       return DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 524 */     } catch (Exception e) {
/* 525 */       throw new ExceptionInInitializerError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/EchoProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */