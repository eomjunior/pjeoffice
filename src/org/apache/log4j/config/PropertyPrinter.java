/*     */ package org.apache.log4j.config;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.Category;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.LogManager;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyPrinter
/*     */   implements PropertyGetter.PropertyCallback
/*     */ {
/*  37 */   protected int numAppenders = 0;
/*  38 */   protected Hashtable appenderNames = new Hashtable<Object, Object>();
/*  39 */   protected Hashtable layoutNames = new Hashtable<Object, Object>();
/*     */   protected PrintWriter out;
/*     */   protected boolean doCapitalize;
/*     */   
/*     */   public PropertyPrinter(PrintWriter out) {
/*  44 */     this(out, false);
/*     */   }
/*     */   
/*     */   public PropertyPrinter(PrintWriter out, boolean doCapitalize) {
/*  48 */     this.out = out;
/*  49 */     this.doCapitalize = doCapitalize;
/*     */     
/*  51 */     print(out);
/*  52 */     out.flush();
/*     */   }
/*     */   
/*     */   protected String genAppName() {
/*  56 */     return "A" + this.numAppenders++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isGenAppName(String name) {
/*  64 */     if (name.length() < 2 || name.charAt(0) != 'A') {
/*  65 */       return false;
/*     */     }
/*  67 */     for (int i = 0; i < name.length(); i++) {
/*  68 */       if (name.charAt(i) < '0' || name.charAt(i) > '9')
/*  69 */         return false; 
/*     */     } 
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintWriter out) {
/*  82 */     printOptions(out, Logger.getRootLogger());
/*     */     
/*  84 */     Enumeration<Logger> cats = LogManager.getCurrentLoggers();
/*  85 */     while (cats.hasMoreElements()) {
/*  86 */       printOptions(out, cats.nextElement());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void printOptions(PrintWriter out, Category cat) {
/*  94 */     Enumeration<Appender> appenders = cat.getAllAppenders();
/*  95 */     Level prio = cat.getLevel();
/*  96 */     String appenderString = (prio == null) ? "" : prio.toString();
/*     */     
/*  98 */     while (appenders.hasMoreElements()) {
/*  99 */       Appender app = appenders.nextElement();
/*     */       
/*     */       String name;
/* 102 */       if ((name = (String)this.appenderNames.get(app)) == null) {
/*     */ 
/*     */         
/* 105 */         if ((name = app.getName()) == null || isGenAppName(name)) {
/* 106 */           name = genAppName();
/*     */         }
/* 108 */         this.appenderNames.put(app, name);
/*     */         
/* 110 */         printOptions(out, app, "log4j.appender." + name);
/* 111 */         if (app.getLayout() != null) {
/* 112 */           printOptions(out, app.getLayout(), "log4j.appender." + name + ".layout");
/*     */         }
/*     */       } 
/* 115 */       appenderString = appenderString + ", " + name;
/*     */     } 
/* 117 */     String catKey = (cat == Logger.getRootLogger()) ? "log4j.rootLogger" : ("log4j.logger." + cat.getName());
/* 118 */     if (appenderString != "") {
/* 119 */       out.println(catKey + "=" + appenderString);
/*     */     }
/* 121 */     if (!cat.getAdditivity() && cat != Logger.getRootLogger()) {
/* 122 */       out.println("log4j.additivity." + cat.getName() + "=false");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void printOptions(PrintWriter out, Logger cat) {
/* 127 */     printOptions(out, (Category)cat);
/*     */   }
/*     */   
/*     */   protected void printOptions(PrintWriter out, Object obj, String fullname) {
/* 131 */     out.println(fullname + "=" + obj.getClass().getName());
/* 132 */     PropertyGetter.getProperties(obj, this, fullname + ".");
/*     */   }
/*     */ 
/*     */   
/*     */   public void foundProperty(Object obj, String prefix, String name, Object value) {
/* 137 */     if (obj instanceof Appender && "name".equals(name)) {
/*     */       return;
/*     */     }
/* 140 */     if (this.doCapitalize) {
/* 141 */       name = capitalize(name);
/*     */     }
/* 143 */     this.out.println(prefix + name + "=" + value.toString());
/*     */   }
/*     */   
/*     */   public static String capitalize(String name) {
/* 147 */     if (Character.isLowerCase(name.charAt(0)) && (
/* 148 */       name.length() == 1 || Character.isLowerCase(name.charAt(1)))) {
/* 149 */       StringBuilder newname = new StringBuilder(name);
/* 150 */       newname.setCharAt(0, Character.toUpperCase(name.charAt(0)));
/* 151 */       return newname.toString();
/*     */     } 
/*     */     
/* 154 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 159 */     new PropertyPrinter(new PrintWriter(System.out));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/config/PropertyPrinter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */