/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DynamicAttribute;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.RuntimeConfigurable;
/*     */ import org.apache.tools.ant.Target;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ import org.apache.tools.ant.UnknownElement;
/*     */ import org.apache.tools.ant.property.LocalProperties;
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
/*     */ public class MacroInstance
/*     */   extends Task
/*     */   implements DynamicAttribute, TaskContainer
/*     */ {
/*     */   private MacroDef macroDef;
/*  51 */   private Map<String, String> map = new HashMap<>();
/*  52 */   private Map<String, MacroDef.TemplateElement> nsElements = null;
/*     */   private Map<String, UnknownElement> presentElements;
/*     */   private Map<String, String> localAttributes;
/*  55 */   private String text = null;
/*  56 */   private String implicitTag = null;
/*  57 */   private List<Task> unknownElements = new ArrayList<>();
/*     */   
/*     */   private static final int STATE_NORMAL = 0;
/*     */   
/*     */   private static final int STATE_EXPECT_BRACKET = 1;
/*     */   private static final int STATE_EXPECT_NAME = 2;
/*     */   
/*     */   public void setMacroDef(MacroDef macroDef) {
/*  65 */     this.macroDef = macroDef;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MacroDef getMacroDef() {
/*  72 */     return this.macroDef;
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
/*  83 */     this.map.put(name.toLowerCase(Locale.ENGLISH), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Object createDynamicElement(String name) throws BuildException {
/*  95 */     throw new BuildException("Not implemented any more");
/*     */   }
/*     */   
/*     */   private Map<String, MacroDef.TemplateElement> getNsElements() {
/*  99 */     if (this.nsElements == null) {
/* 100 */       this.nsElements = new HashMap<>();
/* 101 */       for (Map.Entry<String, MacroDef.TemplateElement> entry : this.macroDef
/* 102 */         .getElements().entrySet()) {
/* 103 */         this.nsElements.put(entry.getKey(), entry.getValue());
/* 104 */         MacroDef.TemplateElement te = entry.getValue();
/* 105 */         if (te.isImplicit()) {
/* 106 */           this.implicitTag = te.getName();
/*     */         }
/*     */       } 
/*     */     } 
/* 110 */     return this.nsElements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTask(Task nestedTask) {
/* 120 */     this.unknownElements.add(nestedTask);
/*     */   }
/*     */   
/*     */   private void processTasks() {
/* 124 */     if (this.implicitTag != null) {
/*     */       return;
/*     */     }
/* 127 */     for (Task task : this.unknownElements) {
/* 128 */       UnknownElement ue = (UnknownElement)task;
/*     */       
/* 130 */       String name = ProjectHelper.extractNameFromComponentName(ue.getTag()).toLowerCase(Locale.ENGLISH);
/* 131 */       if (getNsElements().get(name) == null) {
/* 132 */         throw new BuildException("unsupported element %s", new Object[] { name });
/*     */       }
/* 134 */       if (this.presentElements.get(name) != null) {
/* 135 */         throw new BuildException("Element %s already present", new Object[] { name });
/*     */       }
/* 137 */       this.presentElements.put(name, ue);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Element
/*     */     implements TaskContainer
/*     */   {
/* 145 */     private List<Task> unknownElements = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addTask(Task nestedTask) {
/* 154 */       this.unknownElements.add(nestedTask);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Task> getUnknownElements() {
/* 161 */       return this.unknownElements;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String macroSubs(String s, Map<String, String> macroMapping) {
/* 170 */     if (s == null) {
/* 171 */       return null;
/*     */     }
/* 173 */     StringBuilder ret = new StringBuilder();
/* 174 */     StringBuilder macroName = null;
/*     */     
/* 176 */     int state = 0;
/* 177 */     for (char ch : s.toCharArray()) {
/* 178 */       switch (state) {
/*     */         case 0:
/* 180 */           if (ch == '@') {
/* 181 */             state = 1; break;
/*     */           } 
/* 183 */           ret.append(ch);
/*     */           break;
/*     */         
/*     */         case 1:
/* 187 */           if (ch == '{') {
/* 188 */             state = 2;
/* 189 */             macroName = new StringBuilder(); break;
/* 190 */           }  if (ch == '@') {
/* 191 */             state = 0;
/* 192 */             ret.append('@'); break;
/*     */           } 
/* 194 */           state = 0;
/* 195 */           ret.append('@');
/* 196 */           ret.append(ch);
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 203 */           if (ch == '}') {
/* 204 */             state = 0;
/* 205 */             String name = macroName.toString().toLowerCase(Locale.ENGLISH);
/* 206 */             String value = macroMapping.get(name);
/* 207 */             if (value == null) {
/* 208 */               ret.append("@{");
/* 209 */               ret.append(name);
/* 210 */               ret.append("}");
/*     */             } else {
/* 212 */               ret.append(value);
/*     */             } 
/* 214 */             macroName = null; break;
/*     */           } 
/* 216 */           macroName.append(ch);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 223 */     switch (state) {
/*     */ 
/*     */       
/*     */       case 1:
/* 227 */         ret.append('@');
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 233 */         ret.append("@{");
/* 234 */         ret.append(macroName.toString());
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 240 */     return ret.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(String text) {
/* 249 */     this.text = text;
/*     */   }
/*     */   
/*     */   private UnknownElement copy(UnknownElement ue, boolean nested) {
/* 253 */     UnknownElement ret = new UnknownElement(ue.getTag());
/* 254 */     ret.setNamespace(ue.getNamespace());
/* 255 */     ret.setProject(getProject());
/* 256 */     ret.setQName(ue.getQName());
/* 257 */     ret.setTaskType(ue.getTaskType());
/* 258 */     ret.setTaskName(ue.getTaskName());
/* 259 */     ret.setLocation(
/* 260 */         this.macroDef.getBackTrace() ? ue.getLocation() : getLocation());
/* 261 */     if (getOwningTarget() == null) {
/* 262 */       Target t = new Target();
/* 263 */       t.setProject(getProject());
/* 264 */       ret.setOwningTarget(t);
/*     */     } else {
/* 266 */       ret.setOwningTarget(getOwningTarget());
/*     */     } 
/*     */     
/* 269 */     RuntimeConfigurable rc = new RuntimeConfigurable(ret, ue.getTaskName());
/* 270 */     rc.setPolyType(ue.getWrapper().getPolyType());
/* 271 */     Map<String, Object> m = ue.getWrapper().getAttributeMap();
/* 272 */     for (Map.Entry<String, Object> entry : m.entrySet()) {
/* 273 */       rc.setAttribute(entry
/* 274 */           .getKey(), 
/* 275 */           macroSubs((String)entry.getValue(), this.localAttributes));
/*     */     }
/* 277 */     rc.addText(macroSubs(ue.getWrapper().getText().toString(), this.localAttributes));
/*     */ 
/*     */     
/* 280 */     for (RuntimeConfigurable r : Collections.list(ue.getWrapper().getChildren())) {
/* 281 */       UnknownElement unknownElement = (UnknownElement)r.getProxy();
/* 282 */       String tag = unknownElement.getTaskType();
/* 283 */       if (tag != null) {
/* 284 */         tag = tag.toLowerCase(Locale.ENGLISH);
/*     */       }
/*     */       
/* 287 */       MacroDef.TemplateElement templateElement = getNsElements().get(tag);
/* 288 */       if (templateElement == null || nested) {
/* 289 */         UnknownElement child = copy(unknownElement, nested);
/* 290 */         rc.addChild(child.getWrapper());
/* 291 */         ret.addChild(child); continue;
/* 292 */       }  if (templateElement.isImplicit()) {
/* 293 */         if (this.unknownElements.isEmpty() && !templateElement.isOptional()) {
/* 294 */           throw new BuildException("Missing nested elements for implicit element %s", new Object[] { templateElement
/*     */                 
/* 296 */                 .getName() });
/*     */         }
/* 298 */         for (Task task : this.unknownElements) {
/* 299 */           UnknownElement child = copy((UnknownElement)task, true);
/* 300 */           rc.addChild(child.getWrapper());
/* 301 */           ret.addChild(child);
/*     */         } 
/*     */         continue;
/*     */       } 
/* 305 */       UnknownElement presentElement = this.presentElements.get(tag);
/* 306 */       if (presentElement == null) {
/* 307 */         if (!templateElement.isOptional()) {
/* 308 */           throw new BuildException("Required nested element %s missing", new Object[] { templateElement
/*     */                 
/* 310 */                 .getName() });
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 315 */       String presentText = presentElement.getWrapper().getText().toString();
/* 316 */       if (!presentText.isEmpty()) {
/* 317 */         rc.addText(macroSubs(presentText, this.localAttributes));
/*     */       }
/* 319 */       List<UnknownElement> list = presentElement.getChildren();
/* 320 */       if (list != null) {
/* 321 */         for (UnknownElement unknownElement2 : list) {
/* 322 */           UnknownElement child = copy(unknownElement2, true);
/* 323 */           rc.addChild(child.getWrapper());
/* 324 */           ret.addChild(child);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 329 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 340 */     this.presentElements = new HashMap<>();
/* 341 */     getNsElements();
/* 342 */     processTasks();
/* 343 */     this.localAttributes = new Hashtable<>();
/* 344 */     Set<String> copyKeys = new HashSet<>(this.map.keySet());
/* 345 */     for (MacroDef.Attribute attribute : this.macroDef.getAttributes()) {
/* 346 */       String value = this.map.get(attribute.getName());
/* 347 */       if (value == null && "description".equals(attribute.getName())) {
/* 348 */         value = getDescription();
/*     */       }
/* 350 */       if (value == null) {
/* 351 */         value = attribute.getDefault();
/* 352 */         value = macroSubs(value, this.localAttributes);
/*     */       } 
/* 354 */       if (value == null) {
/* 355 */         throw new BuildException("required attribute %s not set", new Object[] { attribute
/* 356 */               .getName() });
/*     */       }
/* 358 */       this.localAttributes.put(attribute.getName(), value);
/* 359 */       copyKeys.remove(attribute.getName());
/*     */     } 
/* 361 */     copyKeys.remove("id");
/*     */     
/* 363 */     if (this.macroDef.getText() != null) {
/* 364 */       if (this.text == null) {
/* 365 */         String defaultText = this.macroDef.getText().getDefault();
/* 366 */         if (!this.macroDef.getText().getOptional() && defaultText == null) {
/* 367 */           throw new BuildException("required text missing");
/*     */         }
/* 369 */         this.text = (defaultText == null) ? "" : defaultText;
/*     */       } 
/* 371 */       if (this.macroDef.getText().getTrim()) {
/* 372 */         this.text = this.text.trim();
/*     */       }
/* 374 */       this.localAttributes.put(this.macroDef.getText().getName(), this.text);
/* 375 */     } else if (this.text != null && !this.text.trim().isEmpty()) {
/* 376 */       throw new BuildException("The \"%s\" macro does not support nested text data.", new Object[] {
/*     */             
/* 378 */             getTaskName() });
/*     */     } 
/* 380 */     if (!copyKeys.isEmpty()) {
/* 381 */       throw new BuildException("Unknown attribute" + (
/* 382 */           (copyKeys.size() > 1) ? "s " : " ") + copyKeys);
/*     */     }
/*     */ 
/*     */     
/* 386 */     UnknownElement c = copy(this.macroDef.getNestedTask(), false);
/* 387 */     c.init();
/* 388 */     LocalProperties localProperties = LocalProperties.get(getProject());
/* 389 */     localProperties.enterScope();
/*     */     try {
/* 391 */       c.perform();
/* 392 */     } catch (BuildException ex) {
/* 393 */       if (this.macroDef.getBackTrace()) {
/* 394 */         throw ProjectHelper.addLocationToBuildException(ex, 
/* 395 */             getLocation());
/*     */       }
/* 397 */       ex.setLocation(getLocation());
/* 398 */       throw ex;
/*     */     } finally {
/*     */       
/* 401 */       this.presentElements = null;
/* 402 */       this.localAttributes = null;
/* 403 */       localProperties.exitScope();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/MacroInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */