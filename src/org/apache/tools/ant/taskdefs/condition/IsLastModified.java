/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.taskdefs.Touch;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IsLastModified
/*     */   extends ProjectComponent
/*     */   implements Condition
/*     */ {
/*  40 */   private long millis = -1L;
/*  41 */   private String dateTime = null;
/*  42 */   private Touch.DateFormatFactory dfFactory = Touch.DEFAULT_DF_FACTORY;
/*     */   private Resource resource;
/*  44 */   private CompareMode mode = CompareMode.EQUALS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMillis(long millis) {
/*  52 */     this.millis = millis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatetime(String dateTime) {
/*  62 */     this.dateTime = dateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(final String pattern) {
/*  71 */     this.dfFactory = new Touch.DateFormatFactory()
/*     */       {
/*     */         public DateFormat getPrimaryFormat() {
/*  74 */           return new SimpleDateFormat(pattern);
/*     */         }
/*     */         
/*     */         public DateFormat getFallbackFormat() {
/*  78 */           return null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Resource r) {
/*  88 */     if (this.resource != null) {
/*  89 */       throw new BuildException("only one resource can be tested");
/*     */     }
/*  91 */     this.resource = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(CompareMode mode) {
/*  99 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate() throws BuildException {
/* 108 */     if (this.millis >= 0L && this.dateTime != null) {
/* 109 */       throw new BuildException("Only one of dateTime and millis can be set");
/*     */     }
/*     */     
/* 112 */     if (this.millis < 0L && this.dateTime == null) {
/* 113 */       throw new BuildException("millis or dateTime is required");
/*     */     }
/* 115 */     if (this.resource == null) {
/* 116 */       throw new BuildException("resource is required");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getMillis() throws BuildException {
/* 127 */     if (this.millis >= 0L) {
/* 128 */       return this.millis;
/*     */     }
/* 130 */     if ("now".equalsIgnoreCase(this.dateTime)) {
/* 131 */       return System.currentTimeMillis();
/*     */     }
/* 133 */     DateFormat df = this.dfFactory.getPrimaryFormat();
/*     */     
/*     */     try {
/* 136 */       return df.parse(this.dateTime).getTime();
/* 137 */     } catch (ParseException peOne) {
/* 138 */       ParseException pe; df = this.dfFactory.getFallbackFormat();
/* 139 */       if (df == null) {
/* 140 */         pe = peOne;
/*     */       } else {
/*     */         try {
/* 143 */           return df.parse(this.dateTime).getTime();
/* 144 */         } catch (ParseException peTwo) {
/* 145 */           pe = peTwo;
/*     */         } 
/*     */       } 
/*     */       
/* 149 */       throw new BuildException(pe.getMessage(), pe, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/* 159 */     validate();
/* 160 */     long expected = getMillis();
/* 161 */     long actual = this.resource.getLastModified();
/* 162 */     log("expected timestamp: " + expected + " (" + new Date(expected) + "), actual timestamp: " + actual + " (" + new Date(actual) + ")", 3);
/*     */ 
/*     */     
/* 165 */     if ("equals".equals(this.mode.getValue())) {
/* 166 */       return (expected == actual);
/*     */     }
/* 168 */     if ("before".equals(this.mode.getValue())) {
/* 169 */       return (expected > actual);
/*     */     }
/* 171 */     if ("not-before".equals(this.mode.getValue())) {
/* 172 */       return (expected <= actual);
/*     */     }
/* 174 */     if ("after".equals(this.mode.getValue())) {
/* 175 */       return (expected < actual);
/*     */     }
/* 177 */     if ("not-after".equals(this.mode.getValue())) {
/* 178 */       return (expected >= actual);
/*     */     }
/* 180 */     throw new BuildException("Unknown mode " + this.mode.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static class CompareMode
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     private static final String EQUALS_TEXT = "equals";
/*     */     
/*     */     private static final String BEFORE_TEXT = "before";
/*     */     private static final String AFTER_TEXT = "after";
/*     */     private static final String NOT_BEFORE_TEXT = "not-before";
/*     */     private static final String NOT_AFTER_TEXT = "not-after";
/* 193 */     private static final CompareMode EQUALS = new CompareMode("equals");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CompareMode() {
/* 199 */       this("equals");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CompareMode(String s) {
/* 208 */       setValue(s);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 214 */       return new String[] { "equals", "before", "after", "not-before", "not-after" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsLastModified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */