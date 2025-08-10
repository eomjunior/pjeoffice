/*    */ package com.itextpdf.text.xml.xmp;
/*    */ 
/*    */ import com.itextpdf.text.xml.XMLUtil;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class XmpArray
/*    */   extends ArrayList<String>
/*    */ {
/*    */   private static final long serialVersionUID = 5722854116328732742L;
/*    */   public static final String UNORDERED = "rdf:Bag";
/*    */   public static final String ORDERED = "rdf:Seq";
/*    */   public static final String ALTERNATIVE = "rdf:Alt";
/*    */   protected String type;
/*    */   
/*    */   public XmpArray(String type) {
/* 72 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     StringBuffer buf = new StringBuffer("<");
/* 82 */     buf.append(this.type);
/* 83 */     buf.append('>');
/*    */     
/* 85 */     for (String string : this) {
/* 86 */       String s = string;
/* 87 */       buf.append("<rdf:li>");
/* 88 */       buf.append(XMLUtil.escapeXML(s, false));
/* 89 */       buf.append("</rdf:li>");
/*    */     } 
/* 91 */     buf.append("</");
/* 92 */     buf.append(this.type);
/* 93 */     buf.append('>');
/* 94 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/XmpArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */