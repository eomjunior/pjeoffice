/*     */ package org.apache.tools.ant.property;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.PropertyHelper;
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
/*     */ public class LocalProperties
/*     */   extends InheritableThreadLocal<LocalPropertyStack>
/*     */   implements PropertyHelper.PropertyEvaluator, PropertyHelper.PropertySetter, PropertyHelper.PropertyEnumerator
/*     */ {
/*     */   public static synchronized LocalProperties get(Project project) {
/*  42 */     LocalProperties l = (LocalProperties)project.getReference("ant.LocalProperties");
/*  43 */     if (l == null) {
/*  44 */       l = new LocalProperties();
/*  45 */       project.addReference("ant.LocalProperties", l);
/*  46 */       PropertyHelper.getPropertyHelper(project).add((PropertyHelper.Delegate)l);
/*     */     } 
/*  48 */     return l;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized LocalPropertyStack initialValue() {
/*  69 */     return new LocalPropertyStack();
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
/*     */   public void addLocal(String property) {
/*  83 */     get().addLocal(property);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterScope() {
/*  88 */     get().enterScope();
/*     */   }
/*     */ 
/*     */   
/*     */   public void exitScope() {
/*  93 */     get().exitScope();
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
/*     */   public void copy() {
/* 107 */     set(get().copy());
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
/*     */ 
/*     */   
/*     */   public Object evaluate(String property, PropertyHelper helper) {
/* 124 */     return get().evaluate(property, helper);
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
/*     */   public boolean setNew(String property, Object value, PropertyHelper propertyHelper) {
/* 137 */     return get().setNew(property, value, propertyHelper);
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
/*     */   public boolean set(String property, Object value, PropertyHelper propertyHelper) {
/* 150 */     return get().set(property, value, propertyHelper);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getPropertyNames() {
/* 155 */     return get().getPropertyNames();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/property/LocalProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */