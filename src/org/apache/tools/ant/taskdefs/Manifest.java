/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Vector;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.util.StreamUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Manifest
/*      */ {
/*      */   public static final String ATTRIBUTE_MANIFEST_VERSION = "Manifest-Version";
/*      */   public static final String ATTRIBUTE_SIGNATURE_VERSION = "Signature-Version";
/*      */   public static final String ATTRIBUTE_NAME = "Name";
/*      */   public static final String ATTRIBUTE_FROM = "From";
/*      */   public static final String ATTRIBUTE_CLASSPATH = "Class-Path";
/*      */   public static final String DEFAULT_MANIFEST_VERSION = "1.0";
/*      */   public static final int MAX_LINE_LENGTH = 72;
/*      */   public static final int MAX_SECTION_LENGTH = 70;
/*      */   public static final String EOL = "\r\n";
/*      */   public static final String ERROR_FROM_FORBIDDEN = "Manifest attributes should not start with \"From\" in \"";
/*   95 */   public static final Charset JAR_CHARSET = StandardCharsets.UTF_8;
/*      */   
/*      */   @Deprecated
/*   98 */   public static final String JAR_ENCODING = JAR_CHARSET.name();
/*      */   
/*  100 */   private static final String ATTRIBUTE_MANIFEST_VERSION_LC = "Manifest-Version"
/*  101 */     .toLowerCase(Locale.ENGLISH);
/*  102 */   private static final String ATTRIBUTE_NAME_LC = "Name"
/*  103 */     .toLowerCase(Locale.ENGLISH);
/*  104 */   private static final String ATTRIBUTE_FROM_LC = "From"
/*  105 */     .toLowerCase(Locale.ENGLISH);
/*  106 */   private static final String ATTRIBUTE_CLASSPATH_LC = "Class-Path"
/*  107 */     .toLowerCase(Locale.ENGLISH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Attribute
/*      */   {
/*      */     private static final int MAX_NAME_VALUE_LENGTH = 68;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final int MAX_NAME_LENGTH = 70;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  133 */     private String name = null;
/*      */ 
/*      */     
/*  136 */     private Vector<String> values = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  142 */     private int currentIndex = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Attribute() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Attribute(String line) throws ManifestException {
/*  157 */       parse(line);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Attribute(String name, String value) {
/*  167 */       this.name = name;
/*  168 */       setValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  177 */       return Objects.hash(new Object[] { getKey(), this.values });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object rhs) {
/*  187 */       if (rhs == null || rhs.getClass() != getClass()) {
/*  188 */         return false;
/*      */       }
/*      */       
/*  191 */       if (rhs == this) {
/*  192 */         return true;
/*      */       }
/*      */       
/*  195 */       Attribute rhsAttribute = (Attribute)rhs;
/*  196 */       String lhsKey = getKey();
/*  197 */       String rhsKey = rhsAttribute.getKey();
/*  198 */       return ((lhsKey != null || rhsKey == null) && (lhsKey == null || lhsKey
/*  199 */         .equals(rhsKey)) && this.values.equals(rhsAttribute.values));
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
/*      */     public void parse(String line) throws ManifestException {
/*  211 */       int index = line.indexOf(": ");
/*  212 */       if (index == -1) {
/*  213 */         throw new ManifestException("Manifest line \"" + line + "\" is not valid as it does not contain a name and a value separated by ': '");
/*      */       }
/*      */       
/*  216 */       this.name = line.substring(0, index);
/*  217 */       setValue(line.substring(index + 2));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  226 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  235 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getKey() {
/*  244 */       return (this.name == null) ? null : this.name.toLowerCase(Locale.ENGLISH);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValue(String value) {
/*  253 */       if (this.currentIndex >= this.values.size()) {
/*  254 */         this.values.addElement(value);
/*  255 */         this.currentIndex = this.values.size() - 1;
/*      */       } else {
/*  257 */         this.values.setElementAt(value, this.currentIndex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getValue() {
/*  267 */       return this.values.isEmpty() ? null : 
/*  268 */         String.join(" ", (Iterable)this.values);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addValue(String value) {
/*  277 */       this.currentIndex++;
/*  278 */       setValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Enumeration<String> getValues() {
/*  287 */       return this.values.elements();
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
/*      */     
/*      */     public void addContinuation(String line) {
/*  300 */       setValue((String)this.values.elementAt(this.currentIndex) + line.substring(1));
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
/*      */     public void write(PrintWriter writer) throws IOException {
/*  312 */       write(writer, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(PrintWriter writer, boolean flatten) throws IOException {
/*  328 */       if (flatten) {
/*  329 */         writeValue(writer, getValue());
/*      */       } else {
/*  331 */         for (String value : this.values) {
/*  332 */           writeValue(writer, value);
/*      */         }
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
/*      */     
/*      */     private void writeValue(PrintWriter writer, String value) throws IOException {
/*      */       String line;
/*  348 */       int nameLength = (this.name.getBytes(Manifest.JAR_CHARSET)).length;
/*  349 */       if (nameLength > 68) {
/*  350 */         if (nameLength > 70) {
/*  351 */           throw new IOException("Unable to write manifest line " + this.name + ": " + value);
/*      */         }
/*      */         
/*  354 */         writer.print(this.name + ": " + "\r\n");
/*  355 */         line = " " + value;
/*      */       } else {
/*  357 */         line = this.name + ": " + value;
/*      */       } 
/*  359 */       while ((line.getBytes(Manifest.JAR_CHARSET)).length > 70) {
/*      */         
/*  361 */         int breakIndex = 70;
/*  362 */         if (breakIndex >= line.length()) {
/*  363 */           breakIndex = line.length() - 1;
/*      */         }
/*  365 */         String section = line.substring(0, breakIndex);
/*  366 */         while ((section.getBytes(Manifest.JAR_CHARSET)).length > 70 && breakIndex > 0) {
/*      */           
/*  368 */           breakIndex--;
/*  369 */           section = line.substring(0, breakIndex);
/*      */         } 
/*  371 */         if (breakIndex == 0) {
/*  372 */           throw new IOException("Unable to write manifest line " + this.name + ": " + value);
/*      */         }
/*      */         
/*  375 */         writer.print(section + "\r\n");
/*  376 */         line = " " + line.substring(breakIndex);
/*      */       } 
/*  378 */       writer.print(line + "\r\n");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Section
/*      */   {
/*  389 */     private List<String> warnings = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  395 */     private String name = null;
/*      */ 
/*      */     
/*  398 */     private Map<String, Manifest.Attribute> attributes = new LinkedHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  405 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  414 */       return this.name;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String read(BufferedReader reader) throws ManifestException, IOException {
/*  432 */       Manifest.Attribute attribute = null;
/*      */       while (true) {
/*  434 */         String line = reader.readLine();
/*  435 */         if (line == null || line.isEmpty()) {
/*  436 */           return null;
/*      */         }
/*  438 */         if (line.charAt(0) == ' ') {
/*      */           
/*  440 */           if (attribute == null) {
/*  441 */             if (this.name == null) {
/*  442 */               throw new ManifestException("Can't start an attribute with a continuation line " + line);
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  448 */             this.name += line.substring(1); continue;
/*      */           } 
/*  450 */           attribute.addContinuation(line);
/*      */           continue;
/*      */         } 
/*  453 */         attribute = new Manifest.Attribute(line);
/*  454 */         String nameReadAhead = addAttributeAndCheck(attribute);
/*      */         
/*  456 */         attribute = getAttribute(attribute.getKey());
/*  457 */         if (nameReadAhead != null) {
/*  458 */           return nameReadAhead;
/*      */         }
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
/*      */     public void merge(Section section) throws ManifestException {
/*  472 */       merge(section, false);
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
/*      */ 
/*      */     
/*      */     public void merge(Section section, boolean mergeClassPaths) throws ManifestException {
/*  486 */       if ((this.name == null && section.getName() != null) || (this.name != null && section
/*  487 */         .getName() != null && 
/*      */         
/*  489 */         !this.name.toLowerCase(Locale.ENGLISH).equals(section.getName().toLowerCase(Locale.ENGLISH))))
/*      */       {
/*  491 */         throw new ManifestException("Unable to merge sections with different names");
/*      */       }
/*      */ 
/*      */       
/*  495 */       Manifest.Attribute classpathAttribute = null;
/*  496 */       for (String attributeName : Collections.<String>list(section.getAttributeKeys())) {
/*  497 */         Manifest.Attribute attribute = section.getAttribute(attributeName);
/*  498 */         if ("Class-Path".equalsIgnoreCase(attributeName)) {
/*  499 */           if (classpathAttribute == null) {
/*  500 */             classpathAttribute = new Manifest.Attribute();
/*  501 */             classpathAttribute.setName("Class-Path");
/*      */           } 
/*  503 */           Objects.requireNonNull(classpathAttribute); Collections.<String>list(attribute.getValues()).forEach(classpathAttribute::addValue);
/*      */           continue;
/*      */         } 
/*  506 */         storeAttribute(attribute);
/*      */       } 
/*      */ 
/*      */       
/*  510 */       if (classpathAttribute != null) {
/*  511 */         if (mergeClassPaths) {
/*  512 */           Manifest.Attribute currentCp = getAttribute("Class-Path");
/*  513 */           if (currentCp != null) {
/*  514 */             Objects.requireNonNull(classpathAttribute); Collections.<String>list(currentCp.getValues()).forEach(classpathAttribute::addValue);
/*      */           } 
/*      */         } 
/*  517 */         storeAttribute(classpathAttribute);
/*      */       } 
/*      */ 
/*      */       
/*  521 */       this.warnings.addAll(section.warnings);
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
/*      */     public void write(PrintWriter writer) throws IOException {
/*  533 */       write(writer, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(PrintWriter writer, boolean flatten) throws IOException {
/*  549 */       if (this.name != null) {
/*  550 */         Manifest.Attribute nameAttr = new Manifest.Attribute("Name", this.name);
/*  551 */         nameAttr.write(writer);
/*      */       } 
/*  553 */       for (String key : Collections.<String>list(getAttributeKeys())) {
/*  554 */         getAttribute(key).write(writer, flatten);
/*      */       }
/*  556 */       writer.print("\r\n");
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
/*      */     public Manifest.Attribute getAttribute(String attributeName) {
/*  568 */       return this.attributes.get(attributeName.toLowerCase(Locale.ENGLISH));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Enumeration<String> getAttributeKeys() {
/*  578 */       return Collections.enumeration(this.attributes.keySet());
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
/*      */     public String getAttributeValue(String attributeName) {
/*  590 */       Manifest.Attribute attribute = getAttribute(attributeName.toLowerCase(Locale.ENGLISH));
/*  591 */       return (attribute == null) ? null : attribute.getValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeAttribute(String attributeName) {
/*  600 */       String key = attributeName.toLowerCase(Locale.ENGLISH);
/*  601 */       this.attributes.remove(key);
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
/*      */     public void addConfiguredAttribute(Manifest.Attribute attribute) throws ManifestException {
/*  613 */       String check = addAttributeAndCheck(attribute);
/*  614 */       if (check != null) {
/*  615 */         throw new BuildException("Specify the section name using the \"name\" attribute of the <section> element rather than using a \"Name\" manifest attribute");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String addAttributeAndCheck(Manifest.Attribute attribute) throws ManifestException {
/*  633 */       if (attribute.getName() == null || attribute.getValue() == null) {
/*  634 */         throw new BuildException("Attributes must have name and value");
/*      */       }
/*  636 */       String attributeKey = attribute.getKey();
/*  637 */       if (attributeKey.equals(Manifest.ATTRIBUTE_NAME_LC)) {
/*  638 */         this.warnings.add("\"Name\" attributes should not occur in the main section and must be the first element in all other sections: \"" + attribute
/*      */             
/*  640 */             .getName() + ": " + attribute.getValue() + "\"");
/*  641 */         return attribute.getValue();
/*      */       } 
/*      */       
/*  644 */       if (attributeKey.startsWith(Manifest.ATTRIBUTE_FROM_LC))
/*  645 */       { this.warnings.add("Manifest attributes should not start with \"From\" in \"" + attribute
/*  646 */             .getName() + ": " + attribute.getValue() + "\"");
/*      */          }
/*      */       
/*  649 */       else if (attributeKey.equals(Manifest.ATTRIBUTE_CLASSPATH_LC))
/*      */       
/*  651 */       { Manifest.Attribute classpathAttribute = this.attributes.get(attributeKey);
/*      */         
/*  653 */         if (classpathAttribute == null) {
/*  654 */           storeAttribute(attribute);
/*      */         } else {
/*  656 */           this.warnings.add("Multiple Class-Path attributes are supported but violate the Jar specification and may not be correctly processed in all environments");
/*      */           
/*  658 */           Objects.requireNonNull(classpathAttribute); Collections.<String>list(attribute.getValues()).forEach(classpathAttribute::addValue);
/*      */         }  }
/*  660 */       else { if (this.attributes.containsKey(attributeKey)) {
/*  661 */           throw new ManifestException("The attribute \"" + attribute
/*  662 */               .getName() + "\" may not occur more than once in the same section");
/*      */         }
/*      */         
/*  665 */         storeAttribute(attribute); }
/*      */ 
/*      */       
/*  668 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object clone() {
/*  679 */       Section cloned = new Section();
/*  680 */       cloned.setName(this.name);
/*      */ 
/*      */       
/*  683 */       Objects.requireNonNull(cloned); StreamUtils.enumerationAsStream(getAttributeKeys()).map(key -> new Manifest.Attribute(getAttribute(key).getName(), getAttribute(key).getValue())).forEach(cloned::storeAttribute);
/*  684 */       return cloned;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void storeAttribute(Manifest.Attribute attribute) {
/*  693 */       if (attribute == null) {
/*      */         return;
/*      */       }
/*  696 */       this.attributes.put(attribute.getKey(), attribute);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Enumeration<String> getWarnings() {
/*  705 */       return Collections.enumeration(this.warnings);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  714 */       return this.attributes.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object rhs) {
/*  724 */       if (rhs == null || rhs.getClass() != getClass()) {
/*  725 */         return false;
/*      */       }
/*      */       
/*  728 */       if (rhs == this) {
/*  729 */         return true;
/*      */       }
/*      */       
/*  732 */       Section rhsSection = (Section)rhs;
/*      */       
/*  734 */       return this.attributes.equals(rhsSection.attributes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  740 */   private String manifestVersion = "1.0";
/*      */ 
/*      */   
/*  743 */   private Section mainSection = new Section();
/*      */ 
/*      */   
/*  746 */   private Map<String, Section> sections = new LinkedHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Manifest getDefaultManifest() throws BuildException {
/*  756 */     String defManifest = "/org/apache/tools/ant/defaultManifest.mf"; 
/*  757 */     try { InputStream in = Manifest.class.getResourceAsStream(defManifest); 
/*  758 */       try { if (in == null) {
/*  759 */           throw new BuildException("Could not find default manifest: %s", new Object[] { defManifest });
/*      */         }
/*      */         
/*  762 */         Manifest defaultManifest = new Manifest(new InputStreamReader(in, JAR_CHARSET));
/*  763 */         String version = System.getProperty("java.runtime.version");
/*  764 */         if (version == null) {
/*  765 */           version = System.getProperty("java.vm.version");
/*      */         }
/*      */         
/*  768 */         Attribute createdBy = new Attribute("Created-By", version + " (" + System.getProperty("java.vm.vendor") + ")");
/*  769 */         defaultManifest.getMainSection().storeAttribute(createdBy);
/*  770 */         Manifest manifest1 = defaultManifest;
/*  771 */         if (in != null) in.close();  return manifest1; } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (ManifestException e)
/*  772 */     { throw new BuildException("Default manifest is invalid !!", e); }
/*  773 */     catch (IOException e)
/*  774 */     { throw new BuildException("Unable to read default manifest", e); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   public Manifest() {
/*  780 */     this.manifestVersion = null;
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
/*      */   public Manifest(Reader r) throws ManifestException, IOException {
/*  793 */     BufferedReader reader = new BufferedReader(r);
/*      */     
/*  795 */     String nextSectionName = this.mainSection.read(reader);
/*      */     
/*  797 */     String readManifestVersion = this.mainSection.getAttributeValue("Manifest-Version");
/*  798 */     if (readManifestVersion != null) {
/*  799 */       this.manifestVersion = readManifestVersion;
/*  800 */       this.mainSection.removeAttribute("Manifest-Version");
/*      */     } 
/*      */     
/*      */     String line;
/*  804 */     while ((line = reader.readLine()) != null) {
/*  805 */       if (line.isEmpty()) {
/*      */         continue;
/*      */       }
/*      */       
/*  809 */       Section section = new Section();
/*  810 */       if (nextSectionName == null) {
/*  811 */         Attribute sectionName = new Attribute(line);
/*  812 */         if (!"Name".equalsIgnoreCase(sectionName.getName())) {
/*  813 */           throw new ManifestException("Manifest sections should start with a \"Name\" attribute and not \"" + sectionName
/*      */ 
/*      */               
/*  816 */               .getName() + "\"");
/*      */         }
/*  818 */         nextSectionName = sectionName.getValue();
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  823 */         Attribute firstAttribute = new Attribute(line);
/*  824 */         section.addAttributeAndCheck(firstAttribute);
/*      */       } 
/*      */       
/*  827 */       section.setName(nextSectionName);
/*  828 */       nextSectionName = section.read(reader);
/*  829 */       addConfiguredSection(section);
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
/*      */   public void addConfiguredSection(Section section) throws ManifestException {
/*  842 */     String sectionName = section.getName();
/*  843 */     if (sectionName == null) {
/*  844 */       throw new BuildException("Sections must have a name");
/*      */     }
/*  846 */     this.sections.put(sectionName, section);
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
/*      */   public void addConfiguredAttribute(Attribute attribute) throws ManifestException {
/*  858 */     if (attribute.getKey() == null || attribute.getValue() == null) {
/*  859 */       throw new BuildException("Attributes must have name and value");
/*      */     }
/*  861 */     if (ATTRIBUTE_MANIFEST_VERSION_LC.equals(attribute.getKey())) {
/*  862 */       this.manifestVersion = attribute.getValue();
/*      */     } else {
/*  864 */       this.mainSection.addConfiguredAttribute(attribute);
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
/*      */   public void merge(Manifest other) throws ManifestException {
/*  878 */     merge(other, false);
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
/*      */   public void merge(Manifest other, boolean overwriteMain) throws ManifestException {
/*  894 */     merge(other, overwriteMain, false);
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
/*      */   public void merge(Manifest other, boolean overwriteMain, boolean mergeClassPaths) throws ManifestException {
/*  914 */     if (other != null) {
/*  915 */       if (overwriteMain) {
/*  916 */         this.mainSection = (Section)other.mainSection.clone();
/*      */       } else {
/*  918 */         this.mainSection.merge(other.mainSection, mergeClassPaths);
/*      */       } 
/*      */       
/*  921 */       if (other.manifestVersion != null) {
/*  922 */         this.manifestVersion = other.manifestVersion;
/*      */       }
/*      */       
/*  925 */       for (String sectionName : Collections.<String>list(other.getSectionNames())) {
/*  926 */         Section ourSection = this.sections.get(sectionName);
/*  927 */         Section otherSection = other.sections.get(sectionName);
/*  928 */         if (ourSection == null) {
/*  929 */           if (otherSection != null)
/*  930 */             addConfiguredSection((Section)otherSection.clone()); 
/*      */           continue;
/*      */         } 
/*  933 */         ourSection.merge(otherSection, mergeClassPaths);
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
/*      */   public void write(PrintWriter writer) throws IOException {
/*  948 */     write(writer, false);
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
/*      */   public void write(PrintWriter writer, boolean flatten) throws IOException {
/*  963 */     writer.print("Manifest-Version: " + this.manifestVersion + "\r\n");
/*      */     
/*  965 */     String signatureVersion = this.mainSection.getAttributeValue("Signature-Version");
/*  966 */     if (signatureVersion != null) {
/*  967 */       writer.print("Signature-Version: " + signatureVersion + "\r\n");
/*      */       
/*  969 */       this.mainSection.removeAttribute("Signature-Version");
/*      */     } 
/*  971 */     this.mainSection.write(writer, flatten);
/*      */ 
/*      */     
/*  974 */     if (signatureVersion != null) {
/*      */       try {
/*  976 */         Attribute svAttr = new Attribute("Signature-Version", signatureVersion);
/*      */         
/*  978 */         this.mainSection.addConfiguredAttribute(svAttr);
/*  979 */       } catch (ManifestException manifestException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  984 */     for (String sectionName : this.sections.keySet()) {
/*  985 */       Section section = getSection(sectionName);
/*  986 */       section.write(writer, flatten);
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
/*      */   public String toString() {
/*  998 */     StringWriter sw = new StringWriter();
/*      */     try {
/* 1000 */       write(new PrintWriter(sw));
/* 1001 */     } catch (IOException e) {
/* 1002 */       return "";
/*      */     } 
/* 1004 */     return sw.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<String> getWarnings() {
/* 1014 */     List<String> warnings = Collections.list(this.mainSection.getWarnings());
/*      */ 
/*      */ 
/*      */     
/* 1018 */     Objects.requireNonNull(warnings); this.sections.values().stream().map(section -> Collections.list(section.getWarnings())).forEach(warnings::addAll);
/*      */     
/* 1020 */     return Collections.enumeration(warnings);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1029 */     int hashCode = 0;
/*      */     
/* 1031 */     if (this.manifestVersion != null) {
/* 1032 */       hashCode += this.manifestVersion.hashCode();
/*      */     }
/* 1034 */     hashCode += this.mainSection.hashCode();
/* 1035 */     hashCode += this.sections.hashCode();
/*      */     
/* 1037 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object rhs) {
/* 1047 */     if (rhs == null || rhs.getClass() != getClass()) {
/* 1048 */       return false;
/*      */     }
/*      */     
/* 1051 */     if (rhs == this) {
/* 1052 */       return true;
/*      */     }
/*      */     
/* 1055 */     Manifest rhsManifest = (Manifest)rhs;
/* 1056 */     if (this.manifestVersion == null) {
/* 1057 */       if (rhsManifest.manifestVersion != null) {
/* 1058 */         return false;
/*      */       }
/* 1060 */     } else if (!this.manifestVersion.equals(rhsManifest.manifestVersion)) {
/* 1061 */       return false;
/*      */     } 
/*      */     
/* 1064 */     return (this.mainSection.equals(rhsManifest.mainSection) && this.sections.equals(rhsManifest.sections));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getManifestVersion() {
/* 1074 */     return this.manifestVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Section getMainSection() {
/* 1083 */     return this.mainSection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Section getSection(String name) {
/* 1094 */     return this.sections.get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<String> getSectionNames() {
/* 1103 */     return Collections.enumeration(this.sections.keySet());
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Manifest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */