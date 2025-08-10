/*    */ package com.itextpdf.text.xml.simpleparser.handler;
/*    */ 
/*    */ import com.itextpdf.text.xml.simpleparser.NewLineHandler;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HTMLNewLineHandler
/*    */   implements NewLineHandler
/*    */ {
/* 60 */   private final Set<String> newLineTags = new HashSet<String>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HTMLNewLineHandler() {
/* 68 */     this.newLineTags.add("p");
/* 69 */     this.newLineTags.add("blockquote");
/* 70 */     this.newLineTags.add("br");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isNewLineTag(String tag) {
/* 81 */     return this.newLineTags.contains(tag);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/simpleparser/handler/HTMLNewLineHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */