/*     */ package org.apache.tools.ant.taskdefs.cvslib;
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
/*     */ public class CvsTagEntry
/*     */ {
/*     */   private String filename;
/*     */   private String prevRevision;
/*     */   private String revision;
/*     */   
/*     */   public CvsTagEntry(String filename) {
/*  39 */     this(filename, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CvsTagEntry(String filename, String revision) {
/*  48 */     this(filename, revision, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CvsTagEntry(String filename, String revision, String prevRevision) {
/*  59 */     this.filename = filename;
/*  60 */     this.revision = revision;
/*  61 */     this.prevRevision = prevRevision;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFile() {
/*  69 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRevision() {
/*  77 */     return this.revision;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreviousRevision() {
/*  85 */     return this.prevRevision;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  94 */     StringBuilder buffer = new StringBuilder();
/*  95 */     buffer.append(this.filename);
/*  96 */     if (this.revision == null) {
/*  97 */       buffer.append(" was removed");
/*  98 */       if (this.prevRevision != null) {
/*  99 */         buffer.append("; previous revision was ").append(this.prevRevision);
/*     */       }
/* 101 */     } else if (this.prevRevision == null) {
/* 102 */       buffer.append(" is new; current revision is ")
/* 103 */         .append(this.revision);
/*     */     } else {
/* 105 */       buffer.append(" has changed from ")
/* 106 */         .append(this.prevRevision).append(" to ").append(this.revision);
/*     */     } 
/* 108 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/CvsTagEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */