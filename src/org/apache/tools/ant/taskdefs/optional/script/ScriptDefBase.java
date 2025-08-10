/*     */ package org.apache.tools.ant.taskdefs.optional.script;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DynamicConfigurator;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public class ScriptDefBase
/*     */   extends Task
/*     */   implements DynamicConfigurator
/*     */ {
/*  39 */   private Map<String, List<Object>> nestedElementMap = new HashMap<>();
/*     */ 
/*     */   
/*  42 */   private Map<String, String> attributes = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private String text;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/*  52 */     getScript().executeScript(this.attributes, this.nestedElementMap, this);
/*     */   }
/*     */   
/*     */   private ScriptDef getScript() {
/*  56 */     String name = getTaskType();
/*     */     
/*  58 */     Map<String, ScriptDef> scriptRepository = (Map<String, ScriptDef>)getProject().getReference("org.apache.ant.scriptrepo");
/*  59 */     if (scriptRepository == null) {
/*  60 */       throw new BuildException("Script repository not found for " + name);
/*     */     }
/*     */     
/*  63 */     ScriptDef definition = scriptRepository.get(getTaskType());
/*  64 */     if (definition == null) {
/*  65 */       throw new BuildException("Script definition not found for " + name);
/*     */     }
/*  67 */     return definition;
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
/*     */   public Object createDynamicElement(String name) {
/*  79 */     List<Object> nestedElementList = this.nestedElementMap.computeIfAbsent(name, k -> new ArrayList());
/*  80 */     Object element = getScript().createNestedElement(name);
/*  81 */     nestedElementList.add(element);
/*  82 */     return element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDynamicAttribute(String name, String value) {
/*  93 */     ScriptDef definition = getScript();
/*  94 */     if (!definition.isAttributeSupported(name)) {
/*  95 */       throw new BuildException("<%s> does not support the \"%s\" attribute", new Object[] {
/*  96 */             getTaskType(), name
/*     */           });
/*     */     }
/*  99 */     this.attributes.put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 109 */     this.text = getProject().replaceProperties(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/* 118 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fail(String message) {
/* 129 */     throw new BuildException(message);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/script/ScriptDefBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */