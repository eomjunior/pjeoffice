/*    */ package org.apache.log4j.or.sax;
/*    */ 
/*    */ import org.apache.log4j.or.ObjectRenderer;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class AttributesRenderer
/*    */   implements ObjectRenderer
/*    */ {
/*    */   public String doRender(Object o) {
/* 39 */     if (o instanceof Attributes) {
/* 40 */       StringBuilder sbuf = new StringBuilder();
/* 41 */       Attributes a = (Attributes)o;
/* 42 */       int len = a.getLength();
/* 43 */       boolean first = true;
/* 44 */       for (int i = 0; i < len; i++) {
/* 45 */         if (first) {
/* 46 */           first = false;
/*    */         } else {
/* 48 */           sbuf.append(", ");
/*    */         } 
/* 50 */         sbuf.append(a.getQName(i));
/* 51 */         sbuf.append('=');
/* 52 */         sbuf.append(a.getValue(i));
/*    */       } 
/* 54 */       return sbuf.toString();
/*    */     } 
/*    */     try {
/* 57 */       return o.toString();
/* 58 */     } catch (Exception ex) {
/* 59 */       return ex.toString();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/or/sax/AttributesRenderer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */