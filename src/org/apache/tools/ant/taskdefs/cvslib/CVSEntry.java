/*     */ package org.apache.tools.ant.taskdefs.cvslib;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Vector;
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
/*     */ public class CVSEntry
/*     */ {
/*     */   private Date date;
/*     */   private String author;
/*     */   private final String comment;
/*  31 */   private final Vector<RCSFile> files = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CVSEntry(Date date, String author, String comment) {
/*  40 */     this.date = date;
/*  41 */     this.author = author;
/*  42 */     this.comment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFile(String file, String revision) {
/*  51 */     this.files.add(new RCSFile(file, revision));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFile(String file, String revision, String previousRevision) {
/*  61 */     this.files.add(new RCSFile(file, revision, previousRevision));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate() {
/*  69 */     return this.date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthor(String author) {
/*  77 */     this.author = author;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthor() {
/*  85 */     return this.author;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/*  93 */     return this.comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<RCSFile> getFiles() {
/* 101 */     return this.files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return getAuthor() + "\n" + getDate() + "\n" + getFiles() + "\n" + 
/* 111 */       getComment();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/CVSEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */