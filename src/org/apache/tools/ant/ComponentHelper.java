/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.tools.ant.launch.Launcher;
/*      */ import org.apache.tools.ant.taskdefs.Definer;
/*      */ import org.apache.tools.ant.taskdefs.Property;
/*      */ import org.apache.tools.ant.taskdefs.Typedef;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ComponentHelper
/*      */ {
/*   62 */   private final Map<String, List<AntTypeDefinition>> restrictedDefinitions = new HashMap<>();
/*      */ 
/*      */   
/*   65 */   private final Hashtable<String, AntTypeDefinition> antTypeTable = new Hashtable<>();
/*      */ 
/*      */   
/*   68 */   private final Hashtable<String, Class<?>> taskClassDefinitions = new Hashtable<>();
/*      */ 
/*      */   
/*      */   private boolean rebuildTaskClassDefinitions = true;
/*      */ 
/*      */   
/*   74 */   private final Hashtable<String, Class<?>> typeClassDefinitions = new Hashtable<>();
/*      */ 
/*      */   
/*      */   private boolean rebuildTypeClassDefinitions = true;
/*      */ 
/*      */   
/*   80 */   private final HashSet<String> checkedNamespaces = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   private Stack<String> antLibStack = new Stack<>();
/*      */ 
/*      */   
/*   89 */   private String antLibCurrentUri = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ComponentHelper next;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Project project;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String ERROR_NO_TASK_LIST_LOAD = "Can't load default task list";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String ERROR_NO_TYPE_LIST_LOAD = "Can't load default type list";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String COMPONENT_HELPER_REFERENCE = "ant.ComponentHelper";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String BUILD_SYSCLASSPATH_ONLY = "only";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String ANT_PROPERTY_TASK = "property";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  129 */   private static Properties[] defaultDefinitions = new Properties[2];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Project getProject() {
/*  136 */     return this.project;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ComponentHelper getComponentHelper(Project project) {
/*  146 */     if (project == null) {
/*  147 */       return null;
/*      */     }
/*      */     
/*  150 */     ComponentHelper ph = project.<ComponentHelper>getReference("ant.ComponentHelper");
/*  151 */     if (ph != null) {
/*  152 */       return ph;
/*      */     }
/*  154 */     ph = new ComponentHelper();
/*  155 */     ph.setProject(project);
/*      */     
/*  157 */     project.addReference("ant.ComponentHelper", ph);
/*  158 */     return ph;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNext(ComponentHelper next) {
/*  173 */     this.next = next;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentHelper getNext() {
/*  182 */     return this.next;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProject(Project project) {
/*  191 */     this.project = project;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized Set<String> getCheckedNamespace() {
/*  199 */     Set<String> result = (Set<String>)this.checkedNamespaces.clone();
/*  200 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, List<AntTypeDefinition>> getRestrictedDefinition() {
/*  207 */     Map<String, List<AntTypeDefinition>> result = new HashMap<>();
/*  208 */     synchronized (this.restrictedDefinitions) {
/*  209 */       for (Map.Entry<String, List<AntTypeDefinition>> entry : this.restrictedDefinitions.entrySet()) {
/*  210 */         List<AntTypeDefinition> entryVal = entry.getValue();
/*  211 */         synchronized (entryVal) {
/*      */           
/*  213 */           entryVal = new ArrayList<>(entryVal);
/*      */         } 
/*  215 */         result.put(entry.getKey(), entryVal);
/*      */       } 
/*      */     } 
/*  218 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initSubProject(ComponentHelper helper) {
/*  232 */     Hashtable<String, AntTypeDefinition> typeTable = (Hashtable<String, AntTypeDefinition>)helper.antTypeTable.clone();
/*  233 */     synchronized (this.antTypeTable) {
/*  234 */       for (AntTypeDefinition def : typeTable.values()) {
/*  235 */         this.antTypeTable.put(def.getName(), def);
/*      */       }
/*      */     } 
/*      */     
/*  239 */     Set<String> inheritedCheckedNamespace = helper.getCheckedNamespace();
/*  240 */     synchronized (this) {
/*  241 */       this.checkedNamespaces.addAll(inheritedCheckedNamespace);
/*      */     } 
/*      */     
/*  244 */     Map<String, List<AntTypeDefinition>> inheritedRestrictedDef = helper.getRestrictedDefinition();
/*  245 */     synchronized (this.restrictedDefinitions) {
/*  246 */       this.restrictedDefinitions.putAll(inheritedRestrictedDef);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createComponent(UnknownElement ue, String ns, String componentType) throws BuildException {
/*  264 */     Object component = createComponent(componentType);
/*  265 */     if (component instanceof Task) {
/*  266 */       Task task = (Task)component;
/*  267 */       task.setLocation(ue.getLocation());
/*  268 */       task.setTaskType(componentType);
/*  269 */       task.setTaskName(ue.getTaskName());
/*  270 */       task.setOwningTarget(ue.getOwningTarget());
/*  271 */       task.init();
/*      */     } 
/*  273 */     return component;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createComponent(String componentName) {
/*  285 */     AntTypeDefinition def = getDefinition(componentName);
/*  286 */     return (def == null) ? null : def.create(this.project);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> getComponentClass(String componentName) {
/*  298 */     AntTypeDefinition def = getDefinition(componentName);
/*  299 */     return (def == null) ? null : def.getExposedClass(this.project);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AntTypeDefinition getDefinition(String componentName) {
/*  308 */     checkNamespace(componentName);
/*  309 */     return this.antTypeTable.get(componentName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initDefaultDefinitions() {
/*  318 */     initTasks();
/*  319 */     initTypes();
/*  320 */     (new DefaultDefinitions(this)).execute();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTaskDefinition(String taskName, Class<?> taskClass) {
/*  342 */     checkTaskClass(taskClass);
/*  343 */     AntTypeDefinition def = new AntTypeDefinition();
/*  344 */     def.setName(taskName);
/*  345 */     def.setClassLoader(taskClass.getClassLoader());
/*  346 */     def.setClass(taskClass);
/*  347 */     def.setAdapterClass(TaskAdapter.class);
/*  348 */     def.setClassName(taskClass.getName());
/*  349 */     def.setAdaptToClass(Task.class);
/*  350 */     updateDataTypeDefinition(def);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkTaskClass(Class<?> taskClass) throws BuildException {
/*  366 */     if (!Modifier.isPublic(taskClass.getModifiers())) {
/*  367 */       String message = taskClass + " is not public";
/*  368 */       this.project.log(message, 0);
/*  369 */       throw new BuildException(message);
/*      */     } 
/*  371 */     if (Modifier.isAbstract(taskClass.getModifiers())) {
/*  372 */       String message = taskClass + " is abstract";
/*  373 */       this.project.log(message, 0);
/*  374 */       throw new BuildException(message);
/*      */     } 
/*      */     try {
/*  377 */       taskClass.getConstructor((Class[])null);
/*      */     
/*      */     }
/*  380 */     catch (NoSuchMethodException e) {
/*  381 */       String message = "No public no-arg constructor in " + taskClass;
/*  382 */       this.project.log(message, 0);
/*  383 */       throw new BuildException(message);
/*      */     } 
/*  385 */     if (!Task.class.isAssignableFrom(taskClass)) {
/*  386 */       TaskAdapter.checkTaskClass(taskClass, this.project);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Class<?>> getTaskDefinitions() {
/*  399 */     synchronized (this.taskClassDefinitions) {
/*  400 */       synchronized (this.antTypeTable) {
/*  401 */         if (this.rebuildTaskClassDefinitions) {
/*  402 */           this.taskClassDefinitions.clear();
/*  403 */           this.antTypeTable.entrySet().stream()
/*  404 */             .filter(e -> (((AntTypeDefinition)e.getValue()).getExposedClass(this.project) != null && Task.class.isAssignableFrom(((AntTypeDefinition)e.getValue()).getExposedClass(this.project))))
/*      */             
/*  406 */             .forEach(e -> this.taskClassDefinitions.put((String)e.getKey(), ((AntTypeDefinition)e.getValue()).getTypeClass(this.project)));
/*      */           
/*  408 */           this.rebuildTaskClassDefinitions = false;
/*      */         } 
/*      */       } 
/*      */     } 
/*  412 */     return this.taskClassDefinitions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Class<?>> getDataTypeDefinitions() {
/*  423 */     synchronized (this.typeClassDefinitions) {
/*  424 */       synchronized (this.antTypeTable) {
/*  425 */         if (this.rebuildTypeClassDefinitions) {
/*  426 */           this.typeClassDefinitions.clear();
/*  427 */           this.antTypeTable.entrySet().stream()
/*  428 */             .filter(e -> (((AntTypeDefinition)e.getValue()).getExposedClass(this.project) != null && !Task.class.isAssignableFrom(((AntTypeDefinition)e.getValue()).getExposedClass(this.project))))
/*      */             
/*  430 */             .forEach(e -> this.typeClassDefinitions.put((String)e.getKey(), ((AntTypeDefinition)e.getValue()).getTypeClass(this.project)));
/*      */           
/*  432 */           this.rebuildTypeClassDefinitions = false;
/*      */         } 
/*      */       } 
/*      */     } 
/*  436 */     return this.typeClassDefinitions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<AntTypeDefinition> getRestrictedDefinitions(String componentName) {
/*  449 */     synchronized (this.restrictedDefinitions) {
/*  450 */       return this.restrictedDefinitions.get(componentName);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDataTypeDefinition(String typeName, Class<?> typeClass) {
/*  468 */     AntTypeDefinition def = new AntTypeDefinition();
/*  469 */     def.setName(typeName);
/*  470 */     def.setClass(typeClass);
/*  471 */     updateDataTypeDefinition(def);
/*  472 */     this.project.log(" +User datatype: " + typeName + "     " + typeClass.getName(), 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDataTypeDefinition(AntTypeDefinition def) {
/*  482 */     if (!def.isRestrict()) {
/*  483 */       updateDataTypeDefinition(def);
/*      */     } else {
/*  485 */       updateRestrictedDefinition(def);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, AntTypeDefinition> getAntTypeTable() {
/*  497 */     return this.antTypeTable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Task createTask(String taskType) throws BuildException {
/*  515 */     Task task = createNewTask(taskType);
/*  516 */     if (task == null && taskType.equals("property")) {
/*      */ 
/*      */       
/*  519 */       addTaskDefinition("property", Property.class);
/*  520 */       task = createNewTask(taskType);
/*      */     } 
/*  522 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Task createNewTask(String taskType) throws BuildException {
/*  538 */     Class<?> c = getComponentClass(taskType);
/*  539 */     if (c == null || !Task.class.isAssignableFrom(c)) {
/*  540 */       return null;
/*      */     }
/*  542 */     Object obj = createComponent(taskType);
/*  543 */     if (obj == null) {
/*  544 */       return null;
/*      */     }
/*  546 */     if (!(obj instanceof Task)) {
/*  547 */       throw new BuildException("Expected a Task from '" + taskType + "' but got an instance of " + obj
/*  548 */           .getClass().getName() + " instead");
/*      */     }
/*  550 */     Task task = (Task)obj;
/*  551 */     task.setTaskType(taskType);
/*      */ 
/*      */     
/*  554 */     task.setTaskName(taskType);
/*      */     
/*  556 */     this.project.log("   +Task: " + taskType, 4);
/*  557 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createDataType(String typeName) throws BuildException {
/*  573 */     return createComponent(typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getElementName(Object element) {
/*  589 */     return getElementName(element, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getElementName(Object o, boolean brief) {
/*  608 */     Class<?> elementClass = o.getClass();
/*  609 */     String elementClassname = elementClass.getName();
/*  610 */     synchronized (this.antTypeTable) {
/*  611 */       for (AntTypeDefinition def : this.antTypeTable.values()) {
/*  612 */         if (elementClassname.equals(def.getClassName()) && elementClass == def
/*  613 */           .getExposedClass(this.project)) {
/*  614 */           String name = def.getName();
/*  615 */           return brief ? name : ("The <" + name + "> type");
/*      */         } 
/*      */       } 
/*      */     } 
/*  619 */     return getUnmappedElementName(o.getClass(), brief);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getElementName(Project p, Object o, boolean brief) {
/*  633 */     if (p == null) {
/*  634 */       p = Project.getProject(o);
/*      */     }
/*  636 */     return (p == null) ? getUnmappedElementName(o.getClass(), brief) : getComponentHelper(
/*  637 */         p).getElementName(o, brief);
/*      */   }
/*      */   
/*      */   private static String getUnmappedElementName(Class<?> c, boolean brief) {
/*  641 */     if (brief) {
/*  642 */       String name = c.getName();
/*  643 */       return name.substring(name.lastIndexOf('.') + 1);
/*      */     } 
/*  645 */     return c.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean validDefinition(AntTypeDefinition def) {
/*  655 */     return (def.getTypeClass(this.project) != null && def.getExposedClass(this.project) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean sameDefinition(AntTypeDefinition def, AntTypeDefinition old) {
/*  665 */     boolean defValid = validDefinition(def);
/*  666 */     boolean sameValidity = (defValid == validDefinition(old));
/*      */     
/*  668 */     return (sameValidity && (!defValid || def.sameDefinition(old, this.project)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateRestrictedDefinition(AntTypeDefinition def) {
/*  676 */     String name = def.getName();
/*  677 */     List<AntTypeDefinition> list = null;
/*  678 */     synchronized (this.restrictedDefinitions) {
/*  679 */       list = this.restrictedDefinitions.computeIfAbsent(name, k -> new ArrayList());
/*      */     } 
/*      */ 
/*      */     
/*  683 */     synchronized (list) {
/*  684 */       for (Iterator<AntTypeDefinition> i = list.iterator(); i.hasNext(); ) {
/*  685 */         AntTypeDefinition current = i.next();
/*  686 */         if (current.getClassName().equals(def.getClassName())) {
/*  687 */           i.remove();
/*      */           break;
/*      */         } 
/*      */       } 
/*  691 */       list.add(def);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateDataTypeDefinition(AntTypeDefinition def) {
/*  701 */     String name = def.getName();
/*  702 */     synchronized (this.antTypeTable) {
/*  703 */       this.rebuildTaskClassDefinitions = true;
/*  704 */       this.rebuildTypeClassDefinitions = true;
/*  705 */       AntTypeDefinition old = this.antTypeTable.get(name);
/*  706 */       if (old != null) {
/*  707 */         if (sameDefinition(def, old)) {
/*      */           return;
/*      */         }
/*  710 */         Class<?> oldClass = old.getExposedClass(this.project);
/*  711 */         boolean isTask = (oldClass != null && Task.class.isAssignableFrom(oldClass));
/*  712 */         this.project.log("Trying to override old definition of " + (
/*  713 */             isTask ? "task " : "datatype ") + name, def.similarDefinition(old, this.project) ? 
/*  714 */             3 : 1);
/*      */       } 
/*  716 */       this.project.log(" +Datatype " + name + " " + def.getClassName(), 4);
/*  717 */       this.antTypeTable.put(name, def);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterAntLib(String uri) {
/*  726 */     this.antLibCurrentUri = uri;
/*  727 */     this.antLibStack.push(uri);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCurrentAntlibUri() {
/*  734 */     return this.antLibCurrentUri;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void exitAntLib() {
/*  741 */     this.antLibStack.pop();
/*  742 */     this.antLibCurrentUri = this.antLibStack.isEmpty() ? null : this.antLibStack.peek();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initTasks() {
/*  749 */     ClassLoader classLoader = getClassLoader(null);
/*  750 */     Properties props = getDefaultDefinitions(false);
/*  751 */     for (String name : props.stringPropertyNames()) {
/*  752 */       AntTypeDefinition def = new AntTypeDefinition();
/*  753 */       def.setName(name);
/*  754 */       def.setClassName(props.getProperty(name));
/*  755 */       def.setClassLoader(classLoader);
/*  756 */       def.setAdaptToClass(Task.class);
/*  757 */       def.setAdapterClass(TaskAdapter.class);
/*  758 */       this.antTypeTable.put(name, def);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ClassLoader getClassLoader(ClassLoader classLoader) {
/*  763 */     String buildSysclasspath = this.project.getProperty("build.sysclasspath");
/*  764 */     if (this.project.getCoreLoader() != null && 
/*  765 */       !"only".equals(buildSysclasspath)) {
/*  766 */       classLoader = this.project.getCoreLoader();
/*      */     }
/*  768 */     return classLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static synchronized Properties getDefaultDefinitions(boolean type) throws BuildException {
/*  782 */     int idx = type ? 1 : 0;
/*  783 */     if (defaultDefinitions[idx] == null) {
/*      */       
/*  785 */       String resource = type ? "/org/apache/tools/ant/types/defaults.properties" : "/org/apache/tools/ant/taskdefs/defaults.properties";
/*  786 */       String errorString = type ? "Can't load default type list" : "Can't load default task list"; 
/*  787 */       try { InputStream in = ComponentHelper.class.getResourceAsStream(resource); 
/*  788 */         try { if (in == null) {
/*  789 */             throw new BuildException(errorString);
/*      */           }
/*  791 */           Properties p = new Properties();
/*  792 */           p.load(in);
/*  793 */           defaultDefinitions[idx] = p;
/*  794 */           if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  795 */       { throw new BuildException(errorString, e); }
/*      */     
/*      */     } 
/*  798 */     return defaultDefinitions[idx];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initTypes() {
/*  805 */     ClassLoader classLoader = getClassLoader(null);
/*  806 */     Properties props = getDefaultDefinitions(true);
/*  807 */     for (String name : props.stringPropertyNames()) {
/*  808 */       AntTypeDefinition def = new AntTypeDefinition();
/*  809 */       def.setName(name);
/*  810 */       def.setClassName(props.getProperty(name));
/*  811 */       def.setClassLoader(classLoader);
/*  812 */       this.antTypeTable.put(name, def);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void checkNamespace(String componentName) {
/*  823 */     String uri = ProjectHelper.extractUriFromComponentName(componentName);
/*  824 */     if (uri.isEmpty()) {
/*  825 */       uri = "antlib:org.apache.tools.ant";
/*      */     }
/*  827 */     if (!uri.startsWith("antlib:")) {
/*      */       return;
/*      */     }
/*  830 */     if (this.checkedNamespaces.contains(uri)) {
/*      */       return;
/*      */     }
/*  833 */     this.checkedNamespaces.add(uri);
/*      */     
/*  835 */     if (this.antTypeTable.isEmpty())
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  840 */       initDefaultDefinitions();
/*      */     }
/*  842 */     Typedef definer = new Typedef();
/*  843 */     definer.setProject(this.project);
/*  844 */     definer.init();
/*  845 */     definer.setURI(uri);
/*      */     
/*  847 */     definer.setTaskName(uri);
/*      */ 
/*      */     
/*  850 */     definer.setResource(Definer.makeResourceFromURI(uri));
/*      */     
/*  852 */     definer.setOnError(new Definer.OnError("ignore"));
/*  853 */     definer.execute();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String diagnoseCreationFailure(String componentName, String type) {
/*      */     String antHomeLib;
/*  863 */     StringWriter errorText = new StringWriter();
/*  864 */     PrintWriter out = new PrintWriter(errorText);
/*  865 */     out.println("Problem: failed to create " + type + " " + componentName);
/*      */     
/*  867 */     boolean lowlevel = false;
/*  868 */     boolean jars = false;
/*  869 */     boolean definitions = false;
/*      */     
/*  871 */     String home = System.getProperty("user.home");
/*  872 */     File libDir = new File(home, Launcher.USER_LIBDIR);
/*      */     
/*  874 */     boolean probablyIDE = false;
/*  875 */     String anthome = System.getProperty("ant.home");
/*  876 */     if (anthome != null) {
/*  877 */       File antHomeLibDir = new File(anthome, "lib");
/*  878 */       antHomeLib = antHomeLibDir.getAbsolutePath();
/*      */     } else {
/*      */       
/*  881 */       probablyIDE = true;
/*  882 */       antHomeLib = "ANT_HOME" + File.separatorChar + "lib";
/*      */     } 
/*  884 */     StringBuilder dirListingText = new StringBuilder();
/*  885 */     String tab = "        -";
/*  886 */     dirListingText.append("        -");
/*  887 */     dirListingText.append(antHomeLib);
/*  888 */     dirListingText.append('\n');
/*  889 */     if (probablyIDE) {
/*  890 */       dirListingText.append("        -");
/*  891 */       dirListingText.append("the IDE Ant configuration dialogs");
/*      */     } else {
/*  893 */       dirListingText.append("        -");
/*  894 */       dirListingText.append(libDir);
/*  895 */       dirListingText.append('\n');
/*  896 */       dirListingText.append("        -");
/*  897 */       dirListingText.append("a directory added on the command line with the -lib argument");
/*      */     } 
/*  899 */     String dirListing = dirListingText.toString();
/*      */ 
/*      */     
/*  902 */     AntTypeDefinition def = getDefinition(componentName);
/*  903 */     if (def == null) {
/*      */       
/*  905 */       printUnknownDefinition(out, componentName, dirListing);
/*  906 */       definitions = true;
/*      */     } else {
/*      */       
/*  909 */       String classname = def.getClassName();
/*  910 */       boolean antTask = classname.startsWith("org.apache.tools.ant.");
/*      */       
/*  912 */       boolean optional = (classname.startsWith("org.apache.tools.ant.types.optional") || classname.startsWith("org.apache.tools.ant.taskdefs.optional"));
/*      */ 
/*      */       
/*  915 */       Class<?> clazz = null;
/*      */       try {
/*  917 */         clazz = def.innerGetTypeClass();
/*  918 */       } catch (ClassNotFoundException e) {
/*  919 */         jars = true;
/*  920 */         if (!optional) {
/*  921 */           definitions = true;
/*      */         }
/*  923 */         printClassNotFound(out, classname, optional, dirListing);
/*  924 */       } catch (NoClassDefFoundError ncdfe) {
/*  925 */         jars = true;
/*  926 */         printNotLoadDependentClass(out, optional, ncdfe, dirListing);
/*      */       } 
/*      */       
/*  929 */       if (clazz != null) {
/*      */         
/*      */         try {
/*  932 */           def.innerCreateAndSet(clazz, this.project);
/*      */           
/*  934 */           out.println("The component could be instantiated.");
/*  935 */         } catch (NoSuchMethodException e) {
/*  936 */           lowlevel = true;
/*  937 */           out.println("Cause: The class " + classname + " has no compatible constructor.");
/*      */         
/*      */         }
/*  940 */         catch (InstantiationException e) {
/*  941 */           lowlevel = true;
/*  942 */           out.println("Cause: The class " + classname + " is abstract and cannot be instantiated.");
/*      */         }
/*  944 */         catch (IllegalAccessException e) {
/*  945 */           lowlevel = true;
/*  946 */           out.println("Cause: The constructor for " + classname + " is private and cannot be invoked.");
/*      */         }
/*  948 */         catch (InvocationTargetException ex) {
/*  949 */           lowlevel = true;
/*  950 */           Throwable t = ex.getTargetException();
/*  951 */           out.println("Cause: The constructor threw the exception");
/*  952 */           out.println(t.toString());
/*  953 */           t.printStackTrace(out);
/*  954 */         } catch (NoClassDefFoundError ncdfe) {
/*  955 */           jars = true;
/*  956 */           out.println("Cause:  A class needed by class " + classname + " cannot be found: ");
/*      */           
/*  958 */           out.println("       " + ncdfe.getMessage());
/*  959 */           out.println("Action: Determine what extra JAR files are needed, and place them in:");
/*      */           
/*  961 */           out.println(dirListing);
/*      */         } 
/*      */       }
/*  964 */       out.println();
/*  965 */       out.println("Do not panic, this is a common problem.");
/*  966 */       if (definitions) {
/*  967 */         out.println("It may just be a typographical error in the build file or the task/type declaration.");
/*      */       }
/*      */       
/*  970 */       if (jars) {
/*  971 */         out.println("The commonest cause is a missing JAR.");
/*      */       }
/*  973 */       if (lowlevel) {
/*  974 */         out.println("This is quite a low level problem, which may need consultation with the author of the task.");
/*      */         
/*  976 */         if (antTask) {
/*  977 */           out.println("This may be the Ant team. Please file a defect or contact the developer team.");
/*      */         } else {
/*      */           
/*  980 */           out.println("This does not appear to be a task bundled with Ant.");
/*  981 */           out.println("Please take it up with the supplier of the third-party " + type + ".");
/*      */           
/*  983 */           out.println("If you have written it yourself, you probably have a bug to fix.");
/*      */         } 
/*      */       } else {
/*  986 */         out.println();
/*  987 */         out.println("This is not a bug; it is a configuration problem");
/*      */       } 
/*      */     } 
/*  990 */     out.flush();
/*  991 */     out.close();
/*  992 */     return errorText.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void printUnknownDefinition(PrintWriter out, String componentName, String dirListing) {
/*  999 */     boolean isAntlib = componentName.startsWith("antlib:");
/* 1000 */     String uri = ProjectHelper.extractUriFromComponentName(componentName);
/* 1001 */     out.println("Cause: The name is undefined.");
/* 1002 */     out.println("Action: Check the spelling.");
/* 1003 */     out.println("Action: Check that any custom tasks/types have been declared.");
/* 1004 */     out.println("Action: Check that any <presetdef>/<macrodef> declarations have taken place.");
/*      */     
/* 1006 */     if (!uri.isEmpty()) {
/* 1007 */       List<AntTypeDefinition> matches = findTypeMatches(uri);
/* 1008 */       if (matches.isEmpty()) {
/* 1009 */         out.println("No types or tasks have been defined in this namespace yet");
/* 1010 */         if (isAntlib) {
/* 1011 */           out.println();
/* 1012 */           out.println("This appears to be an antlib declaration. ");
/* 1013 */           out.println("Action: Check that the implementing library exists in one of:");
/* 1014 */           out.println(dirListing);
/*      */         } 
/*      */       } else {
/* 1017 */         out.println();
/* 1018 */         out.println("The definitions in the namespace " + uri + " are:");
/* 1019 */         for (AntTypeDefinition def : matches) {
/* 1020 */           String local = ProjectHelper.extractNameFromComponentName(def.getName());
/* 1021 */           out.println("    " + local);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void printClassNotFound(PrintWriter out, String classname, boolean optional, String dirListing) {
/* 1032 */     out.println("Cause: the class " + classname + " was not found.");
/* 1033 */     if (optional) {
/* 1034 */       out.println("        This looks like one of Ant's optional components.");
/* 1035 */       out.println("Action: Check that the appropriate optional JAR exists in");
/* 1036 */       out.println(dirListing);
/*      */     } else {
/* 1038 */       out.println("Action: Check that the component has been correctly declared");
/* 1039 */       out.println("        and that the implementing JAR is in one of:");
/* 1040 */       out.println(dirListing);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void printNotLoadDependentClass(PrintWriter out, boolean optional, NoClassDefFoundError ncdfe, String dirListing) {
/* 1049 */     out.println("Cause: Could not load a dependent class " + ncdfe
/* 1050 */         .getMessage());
/* 1051 */     if (optional) {
/* 1052 */       out.println("       It is not enough to have Ant's optional JARs");
/* 1053 */       out.println("       you need the JAR files that the optional tasks depend upon.");
/* 1054 */       out.println("       Ant's optional task dependencies are listed in the manual.");
/*      */     } else {
/* 1056 */       out.println("       This class may be in a separate JAR that is not installed.");
/*      */     } 
/* 1058 */     out.println("Action: Determine what extra JAR files are needed, and place them in one of:");
/*      */     
/* 1060 */     out.println(dirListing);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<AntTypeDefinition> findTypeMatches(String prefix) {
/* 1070 */     synchronized (this.antTypeTable) {
/* 1071 */       return (List<AntTypeDefinition>)this.antTypeTable.values().stream().filter(def -> def.getName().startsWith(prefix))
/* 1072 */         .collect(Collectors.toList());
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ComponentHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */