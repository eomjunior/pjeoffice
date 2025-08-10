/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.util.LoaderUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArgumentProcessorRegistry
/*     */ {
/*     */   private static final String DEBUG_ARGUMENT_PROCESSOR_REPOSITORY = "ant.argument-processor-repo.debug";
/*  52 */   private static final boolean DEBUG = "true".equals(System.getProperty("ant.argument-processor-repo.debug"));
/*     */   
/*     */   private static final String SERVICE_ID = "META-INF/services/org.apache.tools.ant.ArgumentProcessor";
/*     */   
/*  56 */   private static ArgumentProcessorRegistry instance = new ArgumentProcessorRegistry();
/*     */   
/*  58 */   private List<ArgumentProcessor> processors = new ArrayList<>();
/*     */   
/*     */   public static ArgumentProcessorRegistry getInstance() {
/*  61 */     return instance;
/*     */   }
/*     */   
/*     */   private ArgumentProcessorRegistry() {
/*  65 */     collectArgumentProcessors();
/*     */   }
/*     */   
/*     */   public List<ArgumentProcessor> getProcessors() {
/*  69 */     return this.processors;
/*     */   }
/*     */   
/*     */   private void collectArgumentProcessors() {
/*     */     try {
/*  74 */       ClassLoader classLoader = LoaderUtils.getContextClassLoader();
/*  75 */       if (classLoader != null) {
/*  76 */         for (URL resource : Collections.<URL>list(classLoader.getResources("META-INF/services/org.apache.tools.ant.ArgumentProcessor"))) {
/*  77 */           URLConnection conn = resource.openConnection();
/*  78 */           conn.setUseCaches(false);
/*  79 */           ArgumentProcessor processor = getProcessorByService(conn.getInputStream());
/*  80 */           registerArgumentProcessor(processor);
/*     */         } 
/*     */       }
/*     */       
/*  84 */       InputStream systemResource = ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.tools.ant.ArgumentProcessor");
/*  85 */       if (systemResource != null) {
/*  86 */         ArgumentProcessor processor = getProcessorByService(systemResource);
/*  87 */         registerArgumentProcessor(processor);
/*     */       } 
/*  89 */     } catch (Exception e) {
/*  90 */       System.err.println("Unable to load ArgumentProcessor from service META-INF/services/org.apache.tools.ant.ArgumentProcessor (" + e
/*  91 */           .getClass().getName() + ": " + e
/*  92 */           .getMessage() + ")");
/*  93 */       if (DEBUG) {
/*  94 */         e.printStackTrace(System.err);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerArgumentProcessor(String helperClassName) throws BuildException {
/* 101 */     registerArgumentProcessor(getProcessor(helperClassName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerArgumentProcessor(Class<? extends ArgumentProcessor> helperClass) throws BuildException {
/* 107 */     registerArgumentProcessor(getProcessor(helperClass));
/*     */   }
/*     */ 
/*     */   
/*     */   private ArgumentProcessor getProcessor(String helperClassName) {
/*     */     try {
/* 113 */       Class<? extends ArgumentProcessor> cl = (Class)Class.forName(helperClassName);
/* 114 */       return getProcessor(cl);
/* 115 */     } catch (ClassNotFoundException e) {
/* 116 */       throw new BuildException("Argument processor class " + helperClassName + " was not found", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ArgumentProcessor getProcessor(Class<? extends ArgumentProcessor> processorClass) {
/*     */     ArgumentProcessor processor;
/*     */     try {
/* 125 */       processor = processorClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 126 */     } catch (Exception e) {
/* 127 */       throw new BuildException("The argument processor class" + processorClass
/* 128 */           .getName() + " could not be instantiated with a default constructor", e);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     return processor;
/*     */   }
/*     */   
/*     */   public void registerArgumentProcessor(ArgumentProcessor processor) {
/* 136 */     if (processor == null) {
/*     */       return;
/*     */     }
/* 139 */     this.processors.add(processor);
/* 140 */     if (DEBUG) {
/* 141 */       System.out.println("Argument processor " + processor
/* 142 */           .getClass().getName() + " registered.");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private ArgumentProcessor getProcessorByService(InputStream is) throws IOException {
/* 148 */     BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)); 
/* 149 */     try { String processorClassName = rd.readLine();
/* 150 */       if (processorClassName != null && !processorClassName.isEmpty())
/* 151 */       { ArgumentProcessor argumentProcessor = getProcessor(processorClassName);
/*     */         
/* 153 */         rd.close(); return argumentProcessor; }  rd.close(); } catch (Throwable throwable) { try { rd.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 154 */      return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ArgumentProcessorRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */