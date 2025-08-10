/*     */ package com.yworks.yshrink.model;
/*     */ 
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.util.graph.DefaultNetwork;
/*     */ import com.yworks.util.graph.Network;
/*     */ import com.yworks.yshrink.core.ClassResolver;
/*     */ import com.yworks.yshrink.util.MultiReleaseException;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.objectweb.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Model
/*     */ {
/*     */   Map<String, ClassDescriptor> model;
/*     */   protected Network network;
/*     */   private Map<Object, Object> dependencyTypes;
/*     */   protected Map<Object, Object> node2Descriptor;
/*     */   protected Map<Object, Object> node2Type;
/*     */   private Object entryPointNode;
/*     */   private boolean simpleModelSet = false;
/*     */   private ClassResolver resolver;
/*     */   private boolean allResolved = true;
/*  62 */   public static String VOID_DESC = Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String CONSTRUCTOR_NAME = "<init>";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassResolver(ClassResolver res) {
/*  75 */     if (res != null) {
/*  76 */       this.resolver = res;
/*     */     } else {
/*  78 */       this.resolver = new DefaultClassResolver();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimpleModelSet() {
/*  86 */     this.simpleModelSet = true;
/*     */   }
/*     */   private static class DefaultClassResolver implements ClassResolver { private DefaultClassResolver() {}
/*     */     
/*     */     public Class resolve(String className) throws ClassNotFoundException {
/*  91 */       return Class.forName(className, false, getClass().getClassLoader());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws Exception {} }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSimpleModelSet() {
/* 104 */     return this.simpleModelSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model() {
/* 111 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Model(Network network) {
/* 120 */     if (network != null) {
/* 121 */       this.network = network;
/*     */     } else {
/* 123 */       this.network = (Network)new DefaultNetwork();
/*     */     } 
/*     */     
/* 126 */     setClassResolver(null);
/* 127 */     this.node2Descriptor = new HashMap<>();
/* 128 */     this.node2Type = new HashMap<>();
/* 129 */     this.dependencyTypes = new HashMap<>();
/* 130 */     this.model = new HashMap<>();
/*     */     
/* 132 */     this.entryPointNode = this.network.createNode();
/* 133 */     this.node2Type.put(this.entryPointNode, Integer.valueOf(64));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getEntryPointNode() {
/* 142 */     return this.entryPointNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassModeled(String className) {
/* 152 */     return this.model.containsKey(className);
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
/*     */   public Object createDependencyEdge(AbstractDescriptor source, AbstractDescriptor target, EdgeType type) {
/* 167 */     if (!source.equals(target)) {
/* 168 */       return createDependencyEdge(source.getNode(), target.getNode(), type);
/*     */     }
/* 170 */     return null;
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
/*     */   public Object createDependencyEdge(Object sourceNode, Object targetNode, EdgeType edgeType) {
/* 183 */     if (hasEdge(sourceNode, targetNode, edgeType)) {
/* 184 */       return null;
/*     */     }
/* 186 */     Object e = this.network.createEdge(sourceNode, targetNode);
/* 187 */     this.dependencyTypes.put(e, edgeType);
/* 188 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasEdge(Object src, Object tgt, EdgeType type) {
/* 193 */     Iterator connectingEdgesIterator = this.network.edgesConnecting(src, tgt);
/* 194 */     while (connectingEdgesIterator.hasNext()) {
/* 195 */       Object currentEdge = connectingEdgesIterator.next();
/* 196 */       if (this.dependencyTypes.get(currentEdge) == type) {
/* 197 */         return true;
/*     */       }
/*     */     } 
/* 200 */     return false;
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
/*     */   public ClassDescriptor newClassDescriptor(String name, String superName, String[] interfaces, int access, File sourceJar) {
/* 216 */     Object newNode = this.network.createNode();
/* 217 */     AbstractDescriptor newNodeDescriptor = new NewNodeDescriptor(1, sourceJar);
/* 218 */     this.node2Descriptor.put(newNode, newNodeDescriptor);
/* 219 */     this.node2Type.put(newNode, Integer.valueOf(32));
/* 220 */     newNodeDescriptor.setNode(newNode);
/*     */     
/* 222 */     ClassDescriptor cd = new ClassDescriptor(name, superName, interfaces, access, newNode, sourceJar);
/*     */     
/* 224 */     Object classNode = this.network.createNode();
/* 225 */     this.node2Descriptor.put(classNode, cd);
/* 226 */     this.node2Type.put(classNode, Integer.valueOf(4));
/* 227 */     cd.setNode(classNode);
/* 228 */     this.model.put(name, cd);
/*     */     
/* 230 */     return cd;
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
/*     */   public MethodDescriptor newMethodDescriptor(ClassDescriptor cd, int access, String name, String desc, String[] exceptions, File sourceJar) {
/* 248 */     MethodDescriptor md = new MethodDescriptor(name, access, desc, exceptions, sourceJar);
/* 249 */     cd.addMethod(md);
/* 250 */     Object n = this.network.createNode();
/* 251 */     this.node2Descriptor.put(n, md);
/* 252 */     this.node2Type.put(n, Integer.valueOf(1));
/* 253 */     md.setNode(n);
/* 254 */     return md;
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
/*     */   public FieldDescriptor newFieldDescriptor(ClassDescriptor cd, String desc, String name, int access, File sourceJar) {
/* 269 */     FieldDescriptor fd = new FieldDescriptor(desc, name, access, sourceJar);
/* 270 */     cd.addField(fd);
/* 271 */     Object n = this.network.createNode();
/* 272 */     this.node2Descriptor.put(n, fd);
/* 273 */     this.node2Type.put(n, Integer.valueOf(2));
/* 274 */     fd.setNode(n);
/*     */     
/* 276 */     return fd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ClassDescriptor> getAllClassDescriptors() {
/* 285 */     return this.model.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getAllClassNames() {
/* 294 */     return this.model.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDescriptor getClassDescriptor(String className) {
/* 304 */     if (isClassModeled(className)) {
/* 305 */       return this.model.get(className);
/*     */     }
/* 307 */     if (className.startsWith("META-INF")) {
/* 308 */       throw new MultiReleaseException();
/*     */     }
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractDescriptor getDescriptor(Object n) {
/* 321 */     return (AbstractDescriptor)this.node2Descriptor.get(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getClassNode(Object memberNode) {
/* 332 */     if (getDescriptor(memberNode) instanceof ClassDescriptor) {
/* 333 */       throw new IllegalArgumentException("Node " + memberNode + " is a classNode ");
/*     */     }
/*     */     
/* 336 */     Iterator outEdgesIterator = this.network.outEdges(memberNode);
/* 337 */     while (outEdgesIterator.hasNext()) {
/* 338 */       Object e = outEdgesIterator.next();
/* 339 */       if (getDependencyType(e).equals(EdgeType.MEMBER_OF)) {
/* 340 */         return this.network.getTarget(e);
/*     */       }
/*     */     } 
/*     */     
/* 344 */     throw new RuntimeException("Node " + memberNode + " is homeless.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EdgeType getDependencyType(Object e) {
/* 354 */     return (EdgeType)this.dependencyTypes.get(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ClassDescriptor> getAllImplementingClasses(ClassDescriptor cd) {
/* 364 */     Set<ClassDescriptor> ret = null;
/*     */     
/* 366 */     Iterator inEdgesIterator = this.network.inEdges(cd.getNode());
/* 367 */     while (inEdgesIterator.hasNext()) {
/* 368 */       Object e = inEdgesIterator.next();
/* 369 */       if (this.dependencyTypes.get(e).equals(EdgeType.IMPLEMENTS)) {
/* 370 */         if (ret == null) ret = new HashSet<>(); 
/* 371 */         ClassDescriptor subClass = (ClassDescriptor)this.node2Descriptor.get(this.network.getSource(e));
/* 372 */         ret.add(subClass);
/*     */       } 
/*     */     } 
/*     */     
/* 376 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getAllImplementedInterfaces(String className, Set<String> interfaces) {
/* 387 */     if ("java/lang/Object".equals(className)) {
/*     */       return;
/*     */     }
/*     */     
/* 391 */     if (isClassModeled(className)) {
/* 392 */       ClassDescriptor cd = getClassDescriptor(className);
/* 393 */       String[] cInterfaces = cd.getInterfaces();
/* 394 */       interfaces.addAll(Arrays.asList(cInterfaces));
/* 395 */       for (String interfc : cInterfaces) {
/* 396 */         getAllImplementedInterfaces(interfc, interfaces);
/*     */       }
/* 398 */       getAllImplementedInterfaces(cd.getSuperName(), interfaces);
/*     */     } else {
/* 400 */       Class clazz = resolve(className);
/*     */       
/* 402 */       if (null != clazz) {
/* 403 */         Class[] cInterfaces = clazz.getInterfaces();
/* 404 */         for (Class cInterface : cInterfaces) {
/* 405 */           String internalClassName = Util.toInternalClass(cInterface.getName());
/* 406 */           interfaces.add(internalClassName);
/* 407 */           getAllImplementedInterfaces(internalClassName, interfaces);
/*     */         } 
/* 409 */         Class superclass = clazz.getSuperclass();
/* 410 */         if (superclass != null) {
/* 411 */           getAllImplementedInterfaces(Util.toInternalClass(superclass.getName()), interfaces);
/*     */         }
/*     */       } 
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
/*     */   public void getAllAncestorClasses(String className, Set<String> parents) {
/* 426 */     if ("java/lang/Object".equals(className)) {
/*     */       return;
/*     */     }
/*     */     
/* 430 */     if (isClassModeled(className)) {
/* 431 */       String superName = getClassDescriptor(className).getSuperName();
/* 432 */       parents.add(superName);
/* 433 */       getAllAncestorClasses(superName, parents);
/*     */     } else {
/* 435 */       Class clazz = resolve(className);
/* 436 */       if (null != clazz) {
/* 437 */         Class superclass = clazz.getSuperclass();
/* 438 */         if (null != superclass) {
/* 439 */           String superName = Util.toInternalClass(superclass.getName());
/* 440 */           parents.add(superName);
/* 441 */           getAllAncestorClasses(superName, parents);
/*     */         } 
/*     */       } 
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
/*     */   public void getAllInternalAncestorEntrypointMethods(String className, List<MethodDescriptor> methods) {
/* 456 */     if (null == className || !isClassModeled(className)) {
/*     */       return;
/*     */     }
/*     */     
/* 460 */     ClassDescriptor cd = getClassDescriptor(className);
/*     */     
/* 462 */     Collection<MethodDescriptor> classMethods = cd.getMethods();
/* 463 */     for (MethodDescriptor md : classMethods) {
/* 464 */       if (md.isEntryPoint()) {
/* 465 */         methods.add(md);
/*     */       }
/*     */     } 
/*     */     
/* 469 */     if (!cd.isInterface() || !"java/lang/Object".equals(cd.getSuperName())) {
/* 470 */       getAllInternalAncestorEntrypointMethods(cd.getSuperName(), methods);
/*     */     }
/* 472 */     String[] interfaces = cd.getInterfaces();
/* 473 */     for (String interfc : interfaces) {
/* 474 */       getAllInternalAncestorEntrypointMethods(interfc, methods);
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
/*     */   public boolean getAllExternalAncestorMethods(String className, List<Method> methods) {
/* 488 */     boolean r = true;
/*     */     
/* 490 */     if (null == className) {
/* 491 */       return true;
/*     */     }
/*     */     
/* 494 */     if (isClassModeled(className)) {
/*     */       
/* 496 */       ClassDescriptor cd = getClassDescriptor(className);
/* 497 */       if (!cd.isInterface() || !"java/lang/Object".equals(cd.getSuperName())) {
/* 498 */         r &= getAllExternalAncestorMethods(cd.getSuperName(), methods);
/*     */       }
/* 500 */       String[] interfaces = cd.getInterfaces();
/* 501 */       for (String interfc : interfaces) {
/* 502 */         r &= getAllExternalAncestorMethods(interfc, methods);
/*     */       }
/*     */     } else {
/*     */       
/* 506 */       Class clazz = resolve(className);
/*     */       
/* 508 */       if (null != clazz) {
/*     */ 
/*     */         
/* 511 */         Method[] clazzMethods = clazz.getDeclaredMethods();
/*     */         
/* 513 */         for (Method method : clazzMethods) {
/* 514 */           methods.add(method);
/*     */         }
/*     */ 
/*     */         
/* 518 */         Class superClass = clazz.getSuperclass();
/* 519 */         if (null != superClass) {
/* 520 */           r &= getAllExternalAncestorMethods(superClass.getName(), methods);
/*     */         }
/*     */ 
/*     */         
/* 524 */         Class[] interfaces = clazz.getInterfaces();
/*     */         
/* 526 */         for (Class interfc : interfaces) {
/* 527 */           r &= getAllExternalAncestorMethods(interfc.getName(), methods);
/*     */         }
/*     */       } else {
/* 530 */         return false;
/*     */       } 
/*     */     } 
/* 533 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getInternalDescendants(ClassDescriptor cd, List<ClassDescriptor> descendants) {
/* 543 */     Iterator inEdgesIterator = this.network.inEdges(cd.getNode());
/* 544 */     while (inEdgesIterator.hasNext()) {
/* 545 */       Object e = inEdgesIterator.next();
/* 546 */       if (this.dependencyTypes.get(e).equals(EdgeType.EXTENDS)) {
/* 547 */         ClassDescriptor subClass = (ClassDescriptor)this.node2Descriptor.get(this.network.getSource(e));
/* 548 */         descendants.add(subClass);
/* 549 */         getInternalDescendants(subClass, descendants);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isMethodDefinedInExternalInterface(ClassDescriptor origClass, MethodDescriptor md) {
/* 556 */     boolean found = false;
/*     */     
/* 558 */     String[] interfaces = origClass.getInterfaces();
/* 559 */     for (String interfc : interfaces) {
/* 560 */       if (!isClassModeled(interfc)) {
/* 561 */         Class clazz = resolve(interfc);
/* 562 */         if (null != clazz) {
/* 563 */           found = (found || containsNonPrivateMethod(clazz, md));
/* 564 */           for (Class<?> clazzz : clazz.getInterfaces()) {
/* 565 */             found = (found || isMethodDefinedInExternalInterfaceRec(clazzz, md));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 571 */     return found;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isMethodDefinedInExternalInterfaceRec(Class clazz, MethodDescriptor md) {
/* 576 */     boolean found = false;
/* 577 */     if (containsNonPrivateMethod(clazz, md)) {
/* 578 */       return true;
/*     */     }
/* 580 */     for (Class<?> clazzz : clazz.getInterfaces()) {
/* 581 */       found = (found || isMethodDefinedInExternalInterfaceRec(clazzz, md));
/*     */     }
/* 583 */     return found;
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
/*     */   public boolean isMethodExternallyDefined(ClassDescriptor origClass, MethodDescriptor md) {
/* 598 */     boolean found = false;
/*     */     
/* 600 */     found = (found || isMethodExternallyDefinedRec(origClass.getSuperName(), md));
/*     */     
/* 602 */     for (String interfc : origClass.getInterfaces()) {
/* 603 */       found = (found || isMethodExternallyDefinedRec(interfc, md));
/*     */     }
/*     */     
/* 606 */     List<ClassDescriptor> descendants = new ArrayList<>();
/* 607 */     getInternalDescendants(origClass, descendants);
/* 608 */     for (ClassDescriptor cd : descendants) {
/* 609 */       found = (found || isMethodDefinedInExternalInterface(cd, md));
/*     */     }
/*     */     
/* 612 */     return found;
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
/*     */   private boolean isMethodExternallyDefinedRec(String className, MethodDescriptor md) {
/* 630 */     boolean found = false;
/*     */     
/* 632 */     if (isClassModeled(className)) {
/*     */       
/* 634 */       ClassDescriptor cd = getClassDescriptor(className);
/*     */       
/* 636 */       boolean internallyDefined = false;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 641 */       if (!internallyDefined) {
/*     */         
/* 643 */         String[] interfaces = cd.getInterfaces();
/* 644 */         String superClass = cd.getSuperName();
/*     */         
/* 646 */         if (interfaces.length > 0) {
/* 647 */           for (int i = 0; i < interfaces.length; i++) {
/* 648 */             found = (found || isMethodExternallyDefinedRec(interfaces[i], md));
/*     */           }
/*     */         }
/*     */         
/* 652 */         if (className != "java/lang/Object") {
/* 653 */           found = (found || isMethodExternallyDefinedRec(superClass, md));
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 658 */       Class clazz = resolve(className);
/*     */       
/* 660 */       if (clazz != null) {
/*     */ 
/*     */         
/* 663 */         found = containsNonPrivateMethod(clazz, md);
/*     */         
/* 665 */         if (!found && !"java/lang/Object".equals(clazz.getName())) {
/*     */           
/* 667 */           Class superClass = clazz.getSuperclass();
/* 668 */           if (superClass != null) {
/* 669 */             found = (found || isMethodExternallyDefinedRec(superClass.getName(), md));
/*     */           }
/*     */           
/* 672 */           Class[] interfaces = clazz.getInterfaces();
/*     */           
/* 674 */           for (Class interfc : interfaces) {
/* 675 */             found = (found || isMethodExternallyDefinedRec(interfc.getName(), md));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 681 */     return found;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean containsNonPrivateMethod(Class clazz, MethodDescriptor md) {
/* 686 */     boolean found = false;
/* 687 */     Method[] methods = clazz.getDeclaredMethods();
/*     */     
/* 689 */     for (int i = 0; i < methods.length; i++) {
/*     */       
/* 691 */       if (!Modifier.isPrivate(methods[i].getModifiers()) && 
/* 692 */         md.overrides(methods[i])) found = true;
/*     */     
/*     */     } 
/* 695 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createEntryPointEdges(List<AbstractDescriptor> entryPoints) {
/* 705 */     Object entryPointNode = getEntryPointNode();
/*     */     
/* 707 */     for (AbstractDescriptor descriptor : entryPoints) {
/* 708 */       if (descriptor instanceof MethodDescriptor) {
/*     */         
/* 710 */         MethodDescriptor md = (MethodDescriptor)descriptor;
/*     */         
/* 712 */         createDependencyEdge(entryPointNode, md.getNode(), EdgeType.INVOKES);
/* 713 */         createDependencyEdge(entryPointNode, md.getNode(), EdgeType.RESOLVE); continue;
/*     */       } 
/* 715 */       createDependencyEdge(entryPointNode, descriptor.getNode(), EdgeType.ENTRYPOINT);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Class resolve(String className) {
/* 721 */     Class clazz = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 731 */     try { return clazz; } catch (ClassNotFoundException e) { Logger.warnToLog("Unresolved external dependency: " + Util.toJavaClass(className) + " not found!"); return clazz; } catch (RuntimeException e) { Logger.warnToLog("error resolving class " + className); return clazz; } finally { Exception exception = null; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllResolved() {
/* 741 */     return this.allResolved;
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
/*     */   private MethodDescriptor method2Descriptor(Method m, File sourceJar) {
/* 754 */     int access = m.getModifiers();
/* 755 */     String desc = Type.getMethodDescriptor(m);
/* 756 */     Class[] exceptionClasses = m.getExceptionTypes();
/* 757 */     String[] exceptions = new String[exceptionClasses.length];
/* 758 */     for (int i = 0; i < exceptionClasses.length; i++) {
/* 759 */       exceptions[i] = exceptionClasses[i].getName();
/*     */     }
/*     */     
/* 762 */     return new MethodDescriptor(m.getName(), access, desc, exceptions, sourceJar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNodeType(Object n) {
/* 772 */     return ((Integer)this.node2Type.get(n)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markObsolete(Object n) {
/* 782 */     int type = getNodeType(n);
/* 783 */     if (!NodeType.isObsolete(type)) {
/* 784 */       type += 8192;
/* 785 */       this.node2Type.put(n, Integer.valueOf(type));
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
/*     */   public void markNotObsolete(Object n) {
/* 797 */     int type = getNodeType(n);
/* 798 */     if (NodeType.isObsolete(type)) {
/* 799 */       type -= 8192;
/* 800 */       this.node2Type.put(n, Integer.valueOf(type));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markStubNeeded(Object n) {
/* 810 */     int type = getNodeType(n);
/* 811 */     if (!NodeType.isStubNeeded(type)) {
/* 812 */       type += 16384;
/* 813 */       this.node2Type.put(n, Integer.valueOf(type));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isObsolete(Object n) {
/* 824 */     return NodeType.isObsolete(((Integer)this.node2Type.get(n)).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStubNeeded(Object n) {
/* 834 */     return NodeType.isStubNeeded(((Integer)this.node2Type.get(n)).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Network getNetwork() {
/* 843 */     return this.network;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/Model.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */