/*     */ package com.yworks.common.ant;
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
/*     */ public abstract class Exclude
/*     */ {
/*     */   protected boolean source = false;
/*     */   protected boolean vtable = false;
/*     */   protected boolean ltable = false;
/*     */   protected boolean lttable = false;
/*     */   protected boolean rvAnn = true;
/*     */   protected boolean rvTypeAnn = true;
/*     */   protected boolean riAnn = false;
/*     */   protected boolean riTypeAnn = false;
/*     */   protected boolean rvPann = true;
/*     */   protected boolean riPann = false;
/*     */   protected boolean debugExtension = false;
/*     */   protected YGuardBaseTask task;
/*     */   
/*     */   public Exclude(YGuardBaseTask task) {
/*  64 */     this.task = task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourcefile(boolean sf) {
/*  73 */     this.source = sf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalvariabletable(boolean vt) {
/*  82 */     this.vtable = vt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinenumbertable(boolean lt) {
/*  91 */     this.ltable = lt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRuntimeVisibleAnnotations(boolean v) {
/* 100 */     this.rvAnn = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRuntimeVisibleTypeAnnotations(boolean v) {
/* 109 */     this.rvTypeAnn = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRuntimeInvisibleAnnotations(boolean v) {
/* 118 */     this.riAnn = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRuntimeInvisibleTypeAnnotations(boolean v) {
/* 127 */     this.riTypeAnn = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRuntimeVisibleParameterAnnotations(boolean v) {
/* 136 */     this.rvPann = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRuntimeInvisibleParameterAnnotations(boolean v) {
/* 145 */     this.riPann = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalVariableTypeTable(boolean lt) {
/* 154 */     this.lttable = lt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceDebugExtension(boolean b) {
/* 163 */     this.debugExtension = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSource() {
/* 172 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVtable() {
/* 181 */     return this.vtable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLtable() {
/* 190 */     return this.ltable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLttable() {
/* 199 */     return this.lttable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRvAnn() {
/* 208 */     return this.rvAnn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRiAnn() {
/* 217 */     return this.riAnn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRvPann() {
/* 226 */     return this.rvPann;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRiPann() {
/* 235 */     return this.riPann;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRvTypeAnn() {
/* 244 */     return this.rvTypeAnn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRiTypeAnn() {
/* 253 */     return this.riTypeAnn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugExtension() {
/* 262 */     return this.debugExtension;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/Exclude.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */