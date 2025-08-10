/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.types.Parameter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StripLineComments
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String COMMENTS_KEY = "comment";
/*  58 */   private Vector<String> comments = new Vector<>();
/*     */ 
/*     */   
/*  61 */   private String line = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StripLineComments() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StripLineComments(Reader in) {
/*  79 */     super(in);
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
/*     */   public int read() throws IOException {
/*  94 */     if (!getInitialized()) {
/*  95 */       initialize();
/*  96 */       setInitialized(true);
/*     */     } 
/*     */     
/*  99 */     int ch = -1;
/*     */     
/* 101 */     if (this.line != null) {
/* 102 */       ch = this.line.charAt(0);
/* 103 */       if (this.line.length() == 1) {
/* 104 */         this.line = null;
/*     */       } else {
/* 106 */         this.line = this.line.substring(1);
/*     */       } 
/*     */     } else {
/* 109 */       this.line = readLine();
/* 110 */       int commentsSize = this.comments.size();
/*     */       
/* 112 */       while (this.line != null) {
/* 113 */         for (int i = 0; i < commentsSize; i++) {
/* 114 */           String comment = this.comments.elementAt(i);
/* 115 */           if (this.line.startsWith(comment)) {
/* 116 */             this.line = null;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 121 */         if (this.line == null)
/*     */         {
/* 123 */           this.line = readLine();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 129 */       if (this.line != null) {
/* 130 */         return read();
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredComment(Comment comment) {
/* 144 */     this.comments.addElement(comment.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setComments(Vector<String> comments) {
/* 154 */     this.comments = comments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Vector<String> getComments() {
/* 163 */     return this.comments;
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
/* 177 */     StripLineComments newFilter = new StripLineComments(rdr);
/* 178 */     newFilter.setComments(getComments());
/* 179 */     newFilter.setInitialized(true);
/* 180 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 187 */     Parameter[] params = getParameters();
/* 188 */     if (params != null) {
/* 189 */       for (Parameter param : params) {
/* 190 */         if ("comment".equals(param.getType())) {
/* 191 */           this.comments.addElement(param.getValue());
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Comment
/*     */   {
/*     */     private String value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setValue(String comment) {
/* 212 */       if (this.value != null) {
/* 213 */         throw new IllegalStateException("Comment value already set.");
/*     */       }
/* 215 */       this.value = comment;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getValue() {
/* 224 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addText(String comment) {
/* 234 */       setValue(comment);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/StripLineComments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */