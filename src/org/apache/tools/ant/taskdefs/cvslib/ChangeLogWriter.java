/*     */ package org.apache.tools.ant.taskdefs.cvslib;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.DOMElementWriter;
/*     */ import org.apache.tools.ant.util.DOMUtils;
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
/*     */ public class ChangeLogWriter
/*     */ {
/*  37 */   private final SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
/*     */ 
/*     */ 
/*     */   
/*  41 */   private SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm");
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final DOMElementWriter DOM_WRITER = new DOMElementWriter();
/*     */   
/*     */   public ChangeLogWriter() {
/*  48 */     TimeZone utc = TimeZone.getTimeZone("UTC");
/*  49 */     this.outputDate.setTimeZone(utc);
/*  50 */     this.outputTime.setTimeZone(utc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printChangeLog(PrintWriter output, CVSEntry[] entries) {
/*     */     try {
/*  62 */       output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/*  63 */       Document doc = DOMUtils.newDocument();
/*  64 */       Element root = doc.createElement("changelog");
/*  65 */       DOM_WRITER.openElement(root, output, 0, "\t");
/*  66 */       output.println();
/*  67 */       for (CVSEntry entry : entries) {
/*  68 */         printEntry(doc, output, entry);
/*     */       }
/*  70 */       DOM_WRITER.closeElement(root, output, 0, "\t", true);
/*  71 */       output.flush();
/*  72 */       output.close();
/*  73 */     } catch (IOException e) {
/*  74 */       throw new BuildException(e);
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
/*     */   private void printEntry(Document doc, PrintWriter output, CVSEntry entry) throws IOException {
/*  87 */     Element ent = doc.createElement("entry");
/*  88 */     DOMUtils.appendTextElement(ent, "date", this.outputDate
/*  89 */         .format(entry.getDate()));
/*  90 */     DOMUtils.appendTextElement(ent, "time", this.outputTime
/*  91 */         .format(entry.getDate()));
/*  92 */     DOMUtils.appendCDATAElement(ent, "author", entry.getAuthor());
/*     */     
/*  94 */     for (RCSFile file : entry.getFiles()) {
/*  95 */       Element f = DOMUtils.createChildElement(ent, "file");
/*  96 */       DOMUtils.appendCDATAElement(f, "name", file.getName());
/*  97 */       DOMUtils.appendTextElement(f, "revision", file.getRevision());
/*     */       
/*  99 */       String previousRevision = file.getPreviousRevision();
/* 100 */       if (previousRevision != null) {
/* 101 */         DOMUtils.appendTextElement(f, "prevrevision", previousRevision);
/*     */       }
/*     */     } 
/*     */     
/* 105 */     DOMUtils.appendCDATAElement(ent, "msg", entry.getComment());
/* 106 */     DOM_WRITER.write(ent, output, 1, "\t");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/ChangeLogWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */