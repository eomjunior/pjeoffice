/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.taskdefs.PreSetDef;
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
/*     */ public class UnknownElement
/*     */   extends Task
/*     */ {
/*     */   private final String elementName;
/*  49 */   private String namespace = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String qname;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object realThing;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private List<UnknownElement> children = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean presetDefed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnknownElement(String elementName) {
/*  76 */     this.elementName = elementName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<UnknownElement> getChildren() {
/*  83 */     return this.children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTag() {
/*  94 */     return this.elementName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/* 103 */     return this.namespace;
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
/*     */   public void setNamespace(String namespace) {
/* 115 */     if (namespace.equals("ant:current")) {
/* 116 */       ComponentHelper helper = ComponentHelper.getComponentHelper(
/* 117 */           getProject());
/* 118 */       namespace = helper.getCurrentAntlibUri();
/*     */     } 
/* 120 */     this.namespace = (namespace == null) ? "" : namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQName() {
/* 129 */     return this.qname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQName(String qname) {
/* 139 */     this.qname = qname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeConfigurable getWrapper() {
/* 150 */     return super.getWrapper();
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
/*     */   public void maybeConfigure() throws BuildException {
/* 162 */     Object copy = this.realThing;
/* 163 */     if (copy != null) {
/*     */       return;
/*     */     }
/* 166 */     configure(makeObject(this, getWrapper()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(Object realObject) {
/* 176 */     if (realObject == null) {
/*     */       return;
/*     */     }
/* 179 */     this.realThing = realObject;
/*     */     
/* 181 */     getWrapper().setProxy(realObject);
/* 182 */     Task task = null;
/* 183 */     if (realObject instanceof Task) {
/* 184 */       task = (Task)realObject;
/*     */       
/* 186 */       task.setRuntimeConfigurableWrapper(getWrapper());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       if (getWrapper().getId() != null) {
/* 192 */         getOwningTarget().replaceChild(this, (Task)realObject);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     if (task != null) {
/* 202 */       task.maybeConfigure();
/*     */     } else {
/* 204 */       getWrapper().maybeConfigure(getProject());
/*     */     } 
/*     */     
/* 207 */     handleChildren(realObject, getWrapper());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleOutput(String output) {
/* 216 */     Object copy = this.realThing;
/* 217 */     if (copy instanceof Task) {
/* 218 */       ((Task)copy).handleOutput(output);
/*     */     } else {
/* 220 */       super.handleOutput(output);
/*     */     } 
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
/*     */   protected int handleInput(byte[] buffer, int offset, int length) throws IOException {
/* 238 */     Object copy = this.realThing;
/* 239 */     if (copy instanceof Task) {
/* 240 */       return ((Task)copy).handleInput(buffer, offset, length);
/*     */     }
/* 242 */     return super.handleInput(buffer, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleFlush(String output) {
/* 251 */     Object copy = this.realThing;
/* 252 */     if (copy instanceof Task) {
/* 253 */       ((Task)copy).handleFlush(output);
/*     */     } else {
/* 255 */       super.handleFlush(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleErrorOutput(String output) {
/* 265 */     Object copy = this.realThing;
/* 266 */     if (copy instanceof Task) {
/* 267 */       ((Task)copy).handleErrorOutput(output);
/*     */     } else {
/* 269 */       super.handleErrorOutput(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleErrorFlush(String output) {
/* 279 */     Object copy = this.realThing;
/* 280 */     if (copy instanceof Task) {
/* 281 */       ((Task)copy).handleErrorFlush(output);
/*     */     } else {
/* 283 */       super.handleErrorFlush(output);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 292 */     Object copy = this.realThing;
/* 293 */     if (copy == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 298 */       if (copy instanceof Task) {
/* 299 */         ((Task)copy).execute();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 306 */       if (getWrapper().getId() == null) {
/* 307 */         this.realThing = null;
/* 308 */         getWrapper().setProxy(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChild(UnknownElement child) {
/* 319 */     if (this.children == null) {
/* 320 */       this.children = new ArrayList<>();
/*     */     }
/* 322 */     this.children.add(child);
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
/*     */   protected void handleChildren(Object parent, RuntimeConfigurable parentWrapper) throws BuildException {
/* 342 */     if (parent instanceof TypeAdapter) {
/* 343 */       parent = ((TypeAdapter)parent).getProxy();
/*     */     }
/*     */     
/* 346 */     String parentUri = getNamespace();
/* 347 */     Class<?> parentClass = parent.getClass();
/* 348 */     IntrospectionHelper ih = IntrospectionHelper.getHelper(getProject(), parentClass);
/*     */     
/* 350 */     if (this.children != null) {
/* 351 */       Iterator<UnknownElement> it = this.children.iterator();
/* 352 */       for (int i = 0; it.hasNext(); i++) {
/* 353 */         RuntimeConfigurable childWrapper = parentWrapper.getChild(i);
/* 354 */         UnknownElement child = it.next();
/*     */         try {
/* 356 */           if (!childWrapper.isEnabled(child)) {
/* 357 */             if (ih.supportsNestedElement(parentUri, 
/* 358 */                 ProjectHelper.genComponentName(child
/* 359 */                   .getNamespace(), child.getTag()))) {
/*     */               continue;
/*     */             }
/*     */           }
/*     */           
/* 364 */           if (!handleChild(parentUri, ih, parent, child, childWrapper))
/*     */           {
/* 366 */             if (!(parent instanceof TaskContainer)) {
/* 367 */               ih.throwNotSupported(getProject(), parent, child
/* 368 */                   .getTag());
/*     */             }
/*     */             else {
/*     */               
/* 372 */               TaskContainer container = (TaskContainer)parent;
/* 373 */               container.addTask(child);
/*     */             } 
/*     */           }
/* 376 */         } catch (UnsupportedElementException ex) {
/* 377 */           throw new BuildException(parentWrapper
/* 378 */               .getElementTag() + " doesn't support the nested \"" + ex
/* 379 */               .getElement() + "\" element.", ex);
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getComponentName() {
/* 390 */     return ProjectHelper.genComponentName(getNamespace(), getTag());
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
/*     */   public void applyPreSet(UnknownElement u) {
/* 402 */     if (this.presetDefed) {
/*     */       return;
/*     */     }
/*     */     
/* 406 */     getWrapper().applyPreSet(u.getWrapper());
/* 407 */     if (u.children != null) {
/* 408 */       List<UnknownElement> newChildren = new ArrayList<>(u.children);
/* 409 */       if (this.children != null) {
/* 410 */         newChildren.addAll(this.children);
/*     */       }
/* 412 */       this.children = newChildren;
/*     */     } 
/* 414 */     this.presetDefed = true;
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
/*     */   protected Object makeObject(UnknownElement ue, RuntimeConfigurable w) {
/* 428 */     if (!w.isEnabled(ue)) {
/* 429 */       return null;
/*     */     }
/* 431 */     ComponentHelper helper = ComponentHelper.getComponentHelper(
/* 432 */         getProject());
/* 433 */     String name = ue.getComponentName();
/* 434 */     Object o = helper.createComponent(ue, ue.getNamespace(), name);
/* 435 */     if (o == null) {
/* 436 */       throw getNotFoundException("task or type", name);
/*     */     }
/* 438 */     if (o instanceof PreSetDef.PreSetDefinition) {
/* 439 */       PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition)o;
/* 440 */       o = def.createObject(ue.getProject());
/* 441 */       if (o == null) {
/* 442 */         throw getNotFoundException("preset " + name, def
/*     */             
/* 444 */             .getPreSets().getComponentName());
/*     */       }
/* 446 */       ue.applyPreSet(def.getPreSets());
/* 447 */       if (o instanceof Task) {
/* 448 */         Task task = (Task)o;
/* 449 */         task.setTaskType(ue.getTaskType());
/* 450 */         task.setTaskName(ue.getTaskName());
/* 451 */         task.init();
/*     */       } 
/*     */     } 
/* 454 */     if (o instanceof UnknownElement) {
/* 455 */       o = ((UnknownElement)o).makeObject((UnknownElement)o, w);
/*     */     }
/* 457 */     if (o instanceof Task) {
/* 458 */       ((Task)o).setOwningTarget(getOwningTarget());
/*     */     }
/* 460 */     if (o instanceof ProjectComponent) {
/* 461 */       ((ProjectComponent)o).setLocation(getLocation());
/*     */     }
/* 463 */     return o;
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
/*     */   protected Task makeTask(UnknownElement ue, RuntimeConfigurable w) {
/* 477 */     Task task = getProject().createTask(ue.getTag());
/*     */     
/* 479 */     if (task != null) {
/* 480 */       task.setLocation(getLocation());
/*     */       
/* 482 */       task.setOwningTarget(getOwningTarget());
/* 483 */       task.init();
/*     */     } 
/* 485 */     return task;
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
/*     */   protected BuildException getNotFoundException(String what, String name) {
/* 502 */     ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
/* 503 */     String msg = helper.diagnoseCreationFailure(name, what);
/* 504 */     return new BuildException(msg, getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskName() {
/* 513 */     Object copy = this.realThing;
/* 514 */     return !(copy instanceof Task) ? super.getTaskName() : (
/* 515 */       (Task)copy).getTaskName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Task getTask() {
/* 525 */     Object copy = this.realThing;
/* 526 */     if (copy instanceof Task) {
/* 527 */       return (Task)copy;
/*     */     }
/* 529 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getRealThing() {
/* 540 */     return this.realThing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRealThing(Object realThing) {
/* 549 */     this.realThing = realThing;
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
/*     */   private boolean handleChild(String parentUri, IntrospectionHelper ih, Object parent, UnknownElement child, RuntimeConfigurable childWrapper) {
/* 563 */     String childName = ProjectHelper.genComponentName(child
/* 564 */         .getNamespace(), child.getTag());
/* 565 */     if (ih.supportsNestedElement(parentUri, childName, getProject(), parent)) {
/*     */       
/* 567 */       IntrospectionHelper.Creator creator = null;
/*     */       try {
/* 569 */         creator = ih.getElementCreator(getProject(), parentUri, parent, childName, child);
/*     */       }
/* 571 */       catch (UnsupportedElementException use) {
/* 572 */         if (!ih.isDynamic()) {
/* 573 */           throw use;
/*     */         }
/*     */         
/* 576 */         return false;
/*     */       } 
/* 578 */       creator.setPolyType(childWrapper.getPolyType());
/* 579 */       Object realChild = creator.create();
/* 580 */       if (realChild instanceof PreSetDef.PreSetDefinition) {
/* 581 */         PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition)realChild;
/*     */         
/* 583 */         realChild = creator.getRealObject();
/* 584 */         child.applyPreSet(def.getPreSets());
/*     */       } 
/* 586 */       childWrapper.setCreator(creator);
/* 587 */       childWrapper.setProxy(realChild);
/* 588 */       if (realChild instanceof Task) {
/* 589 */         Task childTask = (Task)realChild;
/* 590 */         childTask.setRuntimeConfigurableWrapper(childWrapper);
/* 591 */         childTask.setTaskName(childName);
/* 592 */         childTask.setTaskType(childName);
/*     */       } 
/* 594 */       if (realChild instanceof ProjectComponent) {
/* 595 */         ((ProjectComponent)realChild).setLocation(child.getLocation());
/*     */       }
/* 597 */       childWrapper.maybeConfigure(getProject());
/* 598 */       child.handleChildren(realChild, childWrapper);
/* 599 */       creator.store();
/* 600 */       return true;
/*     */     } 
/* 602 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean similar(Object obj) {
/* 611 */     if (obj == null) {
/* 612 */       return false;
/*     */     }
/* 614 */     if (!getClass().getName().equals(obj.getClass().getName())) {
/* 615 */       return false;
/*     */     }
/* 617 */     UnknownElement other = (UnknownElement)obj;
/*     */     
/* 619 */     if (!Objects.equals(this.elementName, other.elementName)) {
/* 620 */       return false;
/*     */     }
/* 622 */     if (!this.namespace.equals(other.namespace)) {
/* 623 */       return false;
/*     */     }
/* 625 */     if (!this.qname.equals(other.qname)) {
/* 626 */       return false;
/*     */     }
/*     */     
/* 629 */     if (!getWrapper().getAttributeMap().equals(other
/* 630 */         .getWrapper().getAttributeMap())) {
/* 631 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 637 */     if (!getWrapper().getText().toString().equals(other
/* 638 */         .getWrapper().getText().toString())) {
/* 639 */       return false;
/*     */     }
/*     */     
/* 642 */     int childrenSize = (this.children == null) ? 0 : this.children.size();
/* 643 */     if (childrenSize == 0) {
/* 644 */       return (other.children == null || other.children.isEmpty());
/*     */     }
/* 646 */     if (other.children == null) {
/* 647 */       return false;
/*     */     }
/* 649 */     if (childrenSize != other.children.size()) {
/* 650 */       return false;
/*     */     }
/* 652 */     for (int i = 0; i < childrenSize; i++) {
/*     */       
/* 654 */       UnknownElement child = this.children.get(i);
/* 655 */       if (!child.similar(other.children.get(i))) {
/* 656 */         return false;
/*     */       }
/*     */     } 
/* 659 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnknownElement copy(Project newProject) {
/* 668 */     UnknownElement ret = new UnknownElement(getTag());
/* 669 */     ret.setNamespace(getNamespace());
/* 670 */     ret.setProject(newProject);
/* 671 */     ret.setQName(getQName());
/* 672 */     ret.setTaskType(getTaskType());
/* 673 */     ret.setTaskName(getTaskName());
/* 674 */     ret.setLocation(getLocation());
/* 675 */     if (getOwningTarget() == null) {
/* 676 */       Target t = new Target();
/* 677 */       t.setProject(getProject());
/* 678 */       ret.setOwningTarget(t);
/*     */     } else {
/* 680 */       ret.setOwningTarget(getOwningTarget());
/*     */     } 
/*     */     
/* 683 */     RuntimeConfigurable copyRC = new RuntimeConfigurable(ret, getTaskName());
/* 684 */     copyRC.setPolyType(getWrapper().getPolyType());
/* 685 */     Map<String, Object> m = getWrapper().getAttributeMap();
/* 686 */     for (Map.Entry<String, Object> entry : m.entrySet()) {
/* 687 */       copyRC.setAttribute(entry.getKey(), (String)entry.getValue());
/*     */     }
/* 689 */     copyRC.addText(getWrapper().getText().toString());
/*     */     
/* 691 */     for (RuntimeConfigurable r : Collections.<RuntimeConfigurable>list(getWrapper().getChildren())) {
/* 692 */       UnknownElement ueChild = (UnknownElement)r.getProxy();
/* 693 */       UnknownElement copyChild = ueChild.copy(newProject);
/* 694 */       copyRC.addChild(copyChild.getWrapper());
/* 695 */       ret.addChild(copyChild);
/*     */     } 
/* 697 */     return ret;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/UnknownElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */