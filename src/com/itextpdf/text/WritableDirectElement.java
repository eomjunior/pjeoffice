/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.api.WriterOperation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public abstract class WritableDirectElement
/*     */   implements Element, WriterOperation
/*     */ {
/*     */   public static final int DIRECT_ELEMENT_TYPE_UNKNOWN = 0;
/*     */   public static final int DIRECT_ELEMENT_TYPE_HEADER = 1;
/*  67 */   protected int directElementType = 0;
/*     */ 
/*     */   
/*     */   public WritableDirectElement() {}
/*     */ 
/*     */   
/*     */   public WritableDirectElement(int directElementType) {
/*  74 */     this.directElementType = directElementType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(ElementListener listener) {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/*  90 */     return 666;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 117 */     return new ArrayList<Chunk>(0);
/*     */   }
/*     */   
/*     */   public int getDirectElementType() {
/* 121 */     return this.directElementType;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/WritableDirectElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */