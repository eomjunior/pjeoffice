/*    */ package org.apache.tools.ant.attribute;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.UnknownElement;
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
/*    */ public abstract class BaseIfAttribute
/*    */   extends ProjectComponent
/*    */   implements EnableAttribute
/*    */ {
/*    */   private boolean positive = true;
/*    */   
/*    */   protected void setPositive(boolean positive) {
/* 42 */     this.positive = positive;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isPositive() {
/* 50 */     return this.positive;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean convertResult(boolean val) {
/* 59 */     return (this.positive == val);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Map<String, String> getParams(UnknownElement el) {
/* 70 */     return (Map<String, String>)el.getWrapper().getAttributeMap().entrySet().stream()
/* 71 */       .filter(e -> ((String)e.getKey()).startsWith("ant-attribute:param"))
/* 72 */       .collect(Collectors.toMap(e -> ((String)e.getKey()).substring(((String)e.getKey()).lastIndexOf(':') + 1), e -> el.getProject().replaceProperties((String)e.getValue()), (a, b) -> b));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/attribute/BaseIfAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */