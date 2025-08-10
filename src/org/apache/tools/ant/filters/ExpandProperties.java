/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
/*     */ import org.apache.tools.ant.property.GetProperty;
/*     */ import org.apache.tools.ant.property.ParseProperties;
/*     */ import org.apache.tools.ant.types.PropertySet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExpandProperties
/*     */   extends BaseFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final int EOF = -1;
/*     */   private char[] buffer;
/*     */   private int index;
/*     */   private PropertySet propertySet;
/*     */   
/*     */   public ExpandProperties() {}
/*     */   
/*     */   public ExpandProperties(Reader in) {
/*  65 */     super(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(PropertySet propertySet) {
/*  73 */     if (this.propertySet != null) {
/*  74 */       throw new BuildException("expandproperties filter accepts only one propertyset");
/*     */     }
/*  76 */     this.propertySet = propertySet;
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
/*     */   public int read() throws IOException {
/*  92 */     if (this.index > -1) {
/*  93 */       if (this.buffer == null) {
/*  94 */         GetProperty getProperty; String data = readFully();
/*  95 */         Project project = getProject();
/*     */         
/*  97 */         if (this.propertySet == null) {
/*  98 */           PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(project);
/*     */         } else {
/* 100 */           Objects.requireNonNull(this.propertySet.getProperties()); getProperty = this.propertySet.getProperties()::getProperty;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 106 */         Object expanded = (new ParseProperties(project, PropertyHelper.getPropertyHelper(project).getExpanders(), getProperty)).parseProperties(data);
/* 107 */         this
/* 108 */           .buffer = (expanded == null) ? new char[0] : expanded.toString().toCharArray();
/*     */       } 
/* 110 */       if (this.index < this.buffer.length) {
/* 111 */         return this.buffer[this.index++];
/*     */       }
/* 113 */       this.index = -1;
/*     */     } 
/* 115 */     return -1;
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
/*     */   public Reader chain(Reader rdr) {
/* 129 */     ExpandProperties newFilter = new ExpandProperties(rdr);
/* 130 */     newFilter.setProject(getProject());
/* 131 */     newFilter.add(this.propertySet);
/* 132 */     return newFilter;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/ExpandProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */