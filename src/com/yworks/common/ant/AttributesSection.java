/*    */ package com.yworks.common.ant;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributesSection
/*    */   extends PatternMatchedSection
/*    */ {
/* 14 */   private Set<String> attributes = new HashSet<>();
/*    */ 
/*    */ 
/*    */   
/*    */   private String attributesStr;
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<String> getAttributes() {
/* 23 */     return this.attributes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAttributesStr() {
/* 32 */     return this.attributesStr;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String attributeStr) {
/* 42 */     this.attributesStr = attributeStr;
/* 43 */     StringTokenizer tok = new StringTokenizer(attributeStr, ",");
/* 44 */     while (tok.hasMoreElements())
/* 45 */       this.attributes.add(tok.nextToken().trim()); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/AttributesSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */