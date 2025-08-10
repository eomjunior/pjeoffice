/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Enumeration;
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
/*     */ 
/*     */ public class SelectSelector
/*     */   extends BaseSelectorContainer
/*     */ {
/*     */   private Object ifCondition;
/*     */   private Object unlessCondition;
/*     */   
/*     */   public String toString() {
/*  46 */     StringBuilder buf = new StringBuilder();
/*  47 */     if (hasSelectors()) {
/*  48 */       buf.append("{select");
/*  49 */       if (this.ifCondition != null) {
/*  50 */         buf.append(" if: ");
/*  51 */         buf.append(this.ifCondition);
/*     */       } 
/*  53 */       if (this.unlessCondition != null) {
/*  54 */         buf.append(" unless: ");
/*  55 */         buf.append(this.unlessCondition);
/*     */       } 
/*  57 */       buf.append(" ");
/*  58 */       buf.append(super.toString());
/*  59 */       buf.append("}");
/*     */     } 
/*  61 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SelectSelector getRef() {
/*  69 */     return (SelectSelector)getCheckedRef(SelectSelector.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSelectors() {
/*  78 */     if (isReference()) {
/*  79 */       return getRef().hasSelectors();
/*     */     }
/*  81 */     return super.hasSelectors();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int selectorCount() {
/*  90 */     if (isReference()) {
/*  91 */       return getRef().selectorCount();
/*     */     }
/*  93 */     return super.selectorCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSelector[] getSelectors(Project p) {
/* 103 */     if (isReference()) {
/* 104 */       return getRef().getSelectors(p);
/*     */     }
/* 106 */     return super.getSelectors(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<FileSelector> selectorElements() {
/* 115 */     if (isReference()) {
/* 116 */       return getRef().selectorElements();
/*     */     }
/* 118 */     return super.selectorElements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendSelector(FileSelector selector) {
/* 128 */     if (isReference()) {
/* 129 */       throw noChildrenAllowed();
/*     */     }
/* 131 */     super.appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/* 141 */     int cnt = selectorCount();
/* 142 */     if (cnt < 0 || cnt > 1) {
/* 143 */       setError("Only one selector is allowed within the <selector> tag");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean passesConditions() {
/* 153 */     PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
/* 154 */     return (ph.testIfCondition(this.ifCondition) && ph
/* 155 */       .testUnlessCondition(this.unlessCondition));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIf(Object ifProperty) {
/* 166 */     this.ifCondition = ifProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIf(String ifProperty) {
/* 176 */     setIf(ifProperty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnless(Object unlessProperty) {
/* 187 */     this.unlessCondition = unlessProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnless(String unlessProperty) {
/* 197 */     setUnless(unlessProperty);
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
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 215 */     validate();
/*     */ 
/*     */     
/* 218 */     if (!passesConditions()) {
/* 219 */       return false;
/*     */     }
/*     */     
/* 222 */     Enumeration<FileSelector> e = selectorElements();
/* 223 */     return (!e.hasMoreElements() || ((FileSelector)e.nextElement()).isSelected(basedir, filename, file));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/SelectSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */