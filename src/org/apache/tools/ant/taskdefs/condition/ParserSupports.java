/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.util.JAXPUtils;
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
/*     */ public class ParserSupports
/*     */   extends ProjectComponent
/*     */   implements Condition
/*     */ {
/*     */   public static final String ERROR_BOTH_ATTRIBUTES = "Property and feature attributes are exclusive";
/*     */   public static final String FEATURE = "feature";
/*     */   public static final String PROPERTY = "property";
/*     */   public static final String NOT_RECOGNIZED = " not recognized: ";
/*     */   public static final String NOT_SUPPORTED = " not supported: ";
/*     */   public static final String ERROR_NO_ATTRIBUTES = "Neither feature or property are set";
/*     */   public static final String ERROR_NO_VALUE = "A value is needed when testing for property support";
/*     */   private String feature;
/*     */   private String property;
/*     */   private String value;
/*     */   
/*     */   public void setFeature(String feature) {
/*  65 */     this.feature = feature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  73 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  82 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/*  88 */     if (this.feature != null && this.property != null) {
/*  89 */       throw new BuildException("Property and feature attributes are exclusive");
/*     */     }
/*  91 */     if (this.feature == null && this.property == null) {
/*  92 */       throw new BuildException("Neither feature or property are set");
/*     */     }
/*     */     
/*  95 */     if (this.feature != null) {
/*  96 */       return evalFeature();
/*     */     }
/*  98 */     if (this.value == null) {
/*  99 */       throw new BuildException("A value is needed when testing for property support");
/*     */     }
/* 101 */     return evalProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XMLReader getReader() {
/* 109 */     JAXPUtils.getParser();
/* 110 */     return JAXPUtils.getXMLReader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evalFeature() {
/* 118 */     XMLReader reader = getReader();
/* 119 */     if (this.value == null) {
/* 120 */       this.value = "true";
/*     */     }
/* 122 */     boolean v = Project.toBoolean(this.value);
/*     */     try {
/* 124 */       reader.setFeature(this.feature, v);
/* 125 */     } catch (SAXNotRecognizedException e) {
/* 126 */       log("feature not recognized: " + this.feature, 3);
/* 127 */       return false;
/* 128 */     } catch (SAXNotSupportedException e) {
/* 129 */       log("feature not supported: " + this.feature, 3);
/* 130 */       return false;
/*     */     } 
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evalProperty() {
/* 140 */     XMLReader reader = getReader();
/*     */     try {
/* 142 */       reader.setProperty(this.property, this.value);
/* 143 */     } catch (SAXNotRecognizedException e) {
/* 144 */       log("property not recognized: " + this.property, 3);
/* 145 */       return false;
/* 146 */     } catch (SAXNotSupportedException e) {
/* 147 */       log("property not supported: " + this.property, 3);
/* 148 */       return false;
/*     */     } 
/* 150 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/ParserSupports.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */