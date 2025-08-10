/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.util.IdentityStack;
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
/*     */ public abstract class DataType
/*     */   extends ProjectComponent
/*     */   implements Cloneable
/*     */ {
/*     */   @Deprecated
/*     */   protected Reference ref;
/*     */   @Deprecated
/*     */   protected boolean checked = true;
/*     */   
/*     */   public boolean isReference() {
/*  75 */     return (this.ref != null);
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
/*     */   public void setRefid(Reference ref) {
/*  88 */     this.ref = ref;
/*  89 */     this.checked = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDataTypeName() {
/*  97 */     return ComponentHelper.getElementName(getProject(), this, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dieOnCircularReference() {
/* 105 */     dieOnCircularReference(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dieOnCircularReference(Project p) {
/* 114 */     if (this.checked || !isReference()) {
/*     */       return;
/*     */     }
/* 117 */     dieOnCircularReference((Stack<Object>)new IdentityStack(this), p);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dieOnCircularReference(Stack<Object> stack, Project project) throws BuildException {
/* 143 */     if (this.checked || !isReference()) {
/*     */       return;
/*     */     }
/* 146 */     Object o = this.ref.getReferencedObject(project);
/*     */     
/* 148 */     if (o instanceof DataType) {
/* 149 */       IdentityStack<Object> id = IdentityStack.getInstance(stack);
/*     */       
/* 151 */       if (id.contains(o)) {
/* 152 */         throw circularReference();
/*     */       }
/* 154 */       id.push(o);
/* 155 */       ((DataType)o).dieOnCircularReference((Stack<Object>)id, project);
/* 156 */       id.pop();
/*     */     } 
/*     */     
/* 159 */     this.checked = true;
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
/*     */   public static void invokeCircularReferenceCheck(DataType dt, Stack<Object> stk, Project p) {
/* 173 */     dt.dieOnCircularReference(stk, p);
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
/*     */   public static void pushAndInvokeCircularReferenceCheck(DataType dt, Stack<Object> stk, Project p) {
/* 191 */     stk.push(dt);
/* 192 */     dt.dieOnCircularReference(stk, p);
/* 193 */     stk.pop();
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
/*     */   @Deprecated
/*     */   protected <T> T getCheckedRef() {
/* 207 */     return getCheckedRef(getProject());
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
/*     */   protected <T> T getCheckedRef(Class<T> requiredClass) {
/* 220 */     return getCheckedRef(requiredClass, getDataTypeName(), getProject());
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
/*     */   @Deprecated
/*     */   protected <T> T getCheckedRef(Project p) {
/* 236 */     return getCheckedRef((Class)getClass(), getDataTypeName(), p);
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
/*     */   protected <T> T getCheckedRef(Class<T> requiredClass, String dataTypeName) {
/* 251 */     return getCheckedRef(requiredClass, dataTypeName, getProject());
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
/*     */   protected <T> T getCheckedRef(Class<T> requiredClass, String dataTypeName, Project project) {
/* 269 */     if (project == null) {
/* 270 */       throw new BuildException("No Project specified");
/*     */     }
/* 272 */     dieOnCircularReference(project);
/* 273 */     T o = this.ref.getReferencedObject(project);
/* 274 */     if (requiredClass.isAssignableFrom(o.getClass())) {
/* 275 */       return o;
/*     */     }
/* 277 */     log("Class " + displayName(o.getClass()) + " is not a subclass of " + 
/*     */         
/* 279 */         displayName(requiredClass), 3);
/*     */     
/* 281 */     throw new BuildException(this.ref.getRefId() + " doesn't denote a " + dataTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BuildException tooManyAttributes() {
/* 290 */     return new BuildException("You must not specify more than one attribute when using refid");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BuildException noChildrenAllowed() {
/* 300 */     return new BuildException("You must not specify nested elements when using refid");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BuildException circularReference() {
/* 310 */     return new BuildException("This data type contains a circular reference.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isChecked() {
/* 319 */     return this.checked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setChecked(boolean checked) {
/* 327 */     this.checked = checked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference getRefid() {
/* 335 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkAttributesAllowed() {
/* 344 */     if (isReference()) {
/* 345 */       throw tooManyAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkChildrenAllowed() {
/* 355 */     if (isReference()) {
/* 356 */       throw noChildrenAllowed();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 366 */     String d = getDescription();
/* 367 */     return (d == null) ? getDataTypeName() : (getDataTypeName() + " " + d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 377 */     DataType dt = (DataType)super.clone();
/* 378 */     dt.setDescription(getDescription());
/* 379 */     if (getRefid() != null) {
/* 380 */       dt.setRefid(getRefid());
/*     */     }
/* 382 */     dt.setChecked(isChecked());
/* 383 */     return dt;
/*     */   }
/*     */   
/*     */   private String displayName(Class<?> clazz) {
/* 387 */     return clazz.getName() + " (loaded via " + clazz.getClassLoader() + ")";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/DataType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */