/*     */ package com.yworks.util.compiler;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import javax.tools.DiagnosticCollector;
/*     */ import javax.tools.FileObject;
/*     */ import javax.tools.ForwardingJavaFileManager;
/*     */ import javax.tools.ForwardingJavaFileObject;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.JavaFileManager;
/*     */ import javax.tools.JavaFileObject;
/*     */ import javax.tools.ToolProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleCompiler
/*     */ {
/*     */   private List options;
/*     */   
/*     */   public void addOption(String option) {
/*  35 */     if (this.options == null) {
/*  36 */       this.options = new ArrayList();
/*     */     }
/*  38 */     this.options.add(option);
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
/*     */   public Object newInMemorySource(String typeName, String code) {
/*  50 */     return FileObjects.newInMemoryFileObject(typeName, code);
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
/*     */   public Object newUrlSource(String typeName, URL url) {
/*  62 */     return FileObjects.newUrlFileObject(typeName, url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean compile(Iterable sources, OutputStream result) {
/*     */     try {
/*  74 */       return compileImpl(sources, result);
/*  75 */     } catch (IOException ioe) {
/*  76 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean compileImpl(Iterable sources, OutputStream result) throws IOException {
/*  84 */     JarOutputStream jos = new JarOutputStream(Streams.newGuard(result));
/*     */     try {
/*  86 */       return compileCore(sources, jos);
/*     */     } finally {
/*  88 */       jos.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean compileCore(Iterable<? extends JavaFileObject> sources, JarOutputStream jos) {
/*  93 */     StringWriter compilerOut = new StringWriter();
/*  94 */     DiagnosticCollector<? super JavaFileObject> dc = new DiagnosticCollector();
/*     */     
/*  96 */     JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
/*  97 */     JavaCompiler.CompilationTask task = compiler.getTask(compilerOut, new InMemoryFileManager(jos, compiler, dc), dc, this.options, null, sources);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     Boolean result = task.call();
/* 105 */     return result.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class InMemoryFileManager
/*     */     extends ForwardingJavaFileManager
/*     */   {
/*     */     private JarOutputStream jos;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean hasEntry;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InMemoryFileManager(JarOutputStream jos, JavaCompiler compiler, DiagnosticCollector<? super JavaFileObject> dc) {
/* 126 */       super((M)compiler.getStandardFileManager(dc, Locale.US, Charset.forName("UTF-8")));
/* 127 */       this.jos = jos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
/* 136 */       this.jos.putNextEntry(new JarEntry(className.replace('.', '/') + JavaFileObject.Kind.CLASS.extension));
/* 137 */       return new SimpleCompiler.StreamFileObject(this.jos, super.getJavaFileForOutput(location, className, kind, sibling));
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/* 141 */       super.flush();
/* 142 */       this.jos.flush();
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 146 */       super.close();
/* 147 */       this.jos.close();
/* 148 */       this.jos = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StreamFileObject
/*     */     extends ForwardingJavaFileObject
/*     */   {
/*     */     private final JarOutputStream jos;
/*     */ 
/*     */ 
/*     */     
/*     */     StreamFileObject(JarOutputStream jos, JavaFileObject jfo) {
/* 162 */       super((F)jfo);
/* 163 */       this.jos = jos;
/*     */     }
/*     */     
/*     */     public OutputStream openOutputStream() throws IOException {
/* 167 */       return Streams.newGuard(this.jos);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/compiler/SimpleCompiler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */