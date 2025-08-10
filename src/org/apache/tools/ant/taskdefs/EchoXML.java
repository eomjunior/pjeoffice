/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.util.DOMElementWriter;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.XMLFragment;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EchoXML
/*     */   extends XMLFragment
/*     */ {
/*     */   private File file;
/*     */   private boolean append;
/*  47 */   private NamespacePolicy namespacePolicy = NamespacePolicy.DEFAULT;
/*     */ 
/*     */   
/*     */   private static final String ERROR_NO_XML = "No nested XML specified";
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File f) {
/*  55 */     this.file = f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespacePolicy(NamespacePolicy n) {
/*  64 */     this.namespacePolicy = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppend(boolean b) {
/*  72 */     this.append = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/*  79 */     Node n = getFragment().getFirstChild();
/*  80 */     if (n == null) {
/*  81 */       throw new BuildException("No nested XML specified");
/*     */     }
/*     */     
/*  84 */     DOMElementWriter writer = new DOMElementWriter(!this.append, this.namespacePolicy.getPolicy());
/*     */     
/*  86 */     try { OutputStream os = (this.file == null) ? (OutputStream)new LogOutputStream((ProjectComponent)this, 2) : FileUtils.newOutputStream(this.file.toPath(), this.append); 
/*  87 */       try { writer.write((Element)n, os);
/*  88 */         if (os != null) os.close();  } catch (Throwable throwable) { if (os != null) try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (BuildException be)
/*  89 */     { throw be; }
/*  90 */     catch (Exception e)
/*  91 */     { throw new BuildException(e); }
/*     */   
/*     */   }
/*     */   
/*     */   public static class NamespacePolicy
/*     */     extends EnumeratedAttribute {
/*     */     private static final String IGNORE = "ignore";
/*     */     private static final String ELEMENTS = "elementsOnly";
/*     */     private static final String ALL = "all";
/* 100 */     public static final NamespacePolicy DEFAULT = new NamespacePolicy("ignore");
/*     */ 
/*     */     
/*     */     public NamespacePolicy() {}
/*     */ 
/*     */     
/*     */     public NamespacePolicy(String s) {
/* 107 */       setValue(s);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 113 */       return new String[] { "ignore", "elementsOnly", "all" };
/*     */     }
/*     */     
/*     */     public DOMElementWriter.XmlNamespacePolicy getPolicy() {
/* 117 */       String s = getValue();
/* 118 */       if ("ignore".equalsIgnoreCase(s)) {
/* 119 */         return DOMElementWriter.XmlNamespacePolicy.IGNORE;
/*     */       }
/* 121 */       if ("elementsOnly".equalsIgnoreCase(s)) {
/* 122 */         return DOMElementWriter.XmlNamespacePolicy.ONLY_QUALIFY_ELEMENTS;
/*     */       }
/*     */       
/* 125 */       if ("all".equalsIgnoreCase(s)) {
/* 126 */         return DOMElementWriter.XmlNamespacePolicy.QUALIFY_ALL;
/*     */       }
/* 128 */       throw new BuildException("Invalid namespace policy: " + s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/EchoXML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */