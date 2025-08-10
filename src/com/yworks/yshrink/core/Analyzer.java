/*     */ package com.yworks.yshrink.core;
/*     */ 
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.util.abstractjar.Factory;
/*     */ import com.yworks.util.abstractjar.StreamProvider;
/*     */ import com.yworks.yshrink.model.AbstractDescriptor;
/*     */ import com.yworks.yshrink.model.AnnotationUsage;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.EdgeType;
/*     */ import com.yworks.yshrink.model.FieldDescriptor;
/*     */ import com.yworks.yshrink.model.Invocation;
/*     */ import com.yworks.yshrink.model.MethodDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.model.ModelVisitor;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import org.objectweb.asm.ClassReader;
/*     */ import org.objectweb.asm.ClassVisitor;
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
/*     */ public class Analyzer
/*     */ {
/*     */   private static final String SYNTHETIC_DOT_CLASS_FIELD_START = "class$";
/*     */   private static final String CLASS_DESC = "Ljava/lang/Class;";
/*     */   
/*     */   public void createEdges(Model model) {
/*  50 */     createInheritanceEdges(model);
/*  51 */     createDependencyEdges(model);
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
/*     */   public void initModel(Model model, List<ShrinkBag> bags) throws IOException {
/*  64 */     for (ShrinkBag bag : bags) {
/*  65 */       ModelVisitor mv = new ModelVisitor(model, bag.getIn());
/*  66 */       Logger.log("parsing " + bag.getIn());
/*  67 */       visitAllClasses((ClassVisitor)mv, bag.getIn());
/*     */     } 
/*     */     
/*  70 */     for (ClassDescriptor cd : model.getAllClassDescriptors()) {
/*     */ 
/*     */       
/*  73 */       MethodDescriptor clinit = cd.getMethod("<clinit>", Model.VOID_DESC);
/*     */       
/*  75 */       if (!cd.isInnerClass())
/*     */       {
/*  77 */         if (clinit == null) {
/*  78 */           clinit = model.newMethodDescriptor(cd, 8, "<clinit>", Model.VOID_DESC, null, cd
/*  79 */               .getSourceJar());
/*     */         }
/*     */       }
/*     */       
/*  83 */       if (null != clinit)
/*     */       {
/*     */         
/*  86 */         model.createDependencyEdge((AbstractDescriptor)cd, (AbstractDescriptor)clinit, EdgeType.INVOKES);
/*     */       }
/*     */       
/*  89 */       createEnumEdges(model, cd);
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
/*     */   private void visitAllClasses(ClassVisitor v, File jarFile) throws IOException {
/* 102 */     StreamProvider provider = Factory.newStreamProvider(jarFile);
/* 103 */     InputStream stream = provider.getNextClassEntryStream();
/* 104 */     for (; stream != null; 
/* 105 */       stream = provider.getNextClassEntryStream()) {
/* 106 */       ClassReader cr = new ClassReader(stream);
/*     */ 
/*     */       
/* 109 */       cr.accept(v, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 114 */       close(stream);
/*     */     } 
/*     */     try {
/* 117 */       provider.close();
/* 118 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void close(InputStream is) {
/*     */     try {
/* 129 */       is.close();
/* 130 */     } catch (Exception exception) {}
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
/*     */   public void createInheritanceEdges(Model model) {
/* 142 */     for (ClassDescriptor cm : model.getAllClassDescriptors()) {
/*     */ 
/*     */       
/* 145 */       if (model.isClassModeled(cm.getSuperName())) {
/* 146 */         ClassDescriptor superDescriptor = model.getClassDescriptor(cm.getSuperName());
/* 147 */         model.createDependencyEdge((AbstractDescriptor)cm, (AbstractDescriptor)superDescriptor, EdgeType.EXTENDS);
/*     */       } 
/*     */ 
/*     */       
/* 151 */       for (String interfc : cm.getInterfaces()) {
/* 152 */         if (model.isClassModeled(interfc)) {
/* 153 */           ClassDescriptor superDescriptor = model.getClassDescriptor(interfc);
/* 154 */           model.createDependencyEdge((AbstractDescriptor)cm, (AbstractDescriptor)superDescriptor, EdgeType.IMPLEMENTS);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 159 */     model.setSimpleModelSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createDependencyEdges(Model model) {
/* 169 */     for (ClassDescriptor cd : model.getAllClassDescriptors()) {
/*     */       
/* 171 */       createAnnotationEdges((AbstractDescriptor)cd, model);
/*     */       
/* 173 */       model.createDependencyEdge(cd.getNewNode(), cd.getNode(), EdgeType.MEMBER_OF);
/*     */       
/* 175 */       createInnerClassEdges(model, cd);
/* 176 */       createAssumeEdges(model, cd);
/*     */       
/* 178 */       for (MethodDescriptor md : cd.getMethods()) {
/*     */         
/* 180 */         createAnnotationEdges((AbstractDescriptor)md, model);
/*     */         
/* 182 */         model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)cd, EdgeType.MEMBER_OF);
/*     */         
/* 184 */         createReferenceEdges(model, md);
/* 185 */         createMethodSignatureEdges(model, md);
/* 186 */         createInvokeEdges(model, cd, md);
/* 187 */         createTypeInstructionEdges(model, md);
/*     */       } 
/* 189 */       for (FieldDescriptor fd : cd.getFields()) {
/* 190 */         createAnnotationEdges((AbstractDescriptor)fd, model);
/* 191 */         model.createDependencyEdge((AbstractDescriptor)fd, (AbstractDescriptor)cd, EdgeType.MEMBER_OF);
/*     */ 
/*     */ 
/*     */         
/* 195 */         String fieldTypeName = Util.getTypeNameFromDescriptor(fd.getDesc());
/* 196 */         if (model.isClassModeled(fieldTypeName)) {
/* 197 */           ClassDescriptor fieldType = model.getClassDescriptor(fieldTypeName);
/* 198 */           model.createDependencyEdge((AbstractDescriptor)fd, (AbstractDescriptor)fieldType, EdgeType.RESOLVE);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void createAnnotationEdges(AbstractDescriptor cd, Model model) {
/* 206 */     for (AnnotationUsage annotationUsage : cd.getAnnotations()) {
/* 207 */       if (model.isClassModeled(annotationUsage.getDescriptor())) {
/* 208 */         ClassDescriptor annotationClassDescriptor = model.getClassDescriptor(annotationUsage.getDescriptor());
/* 209 */         model.createDependencyEdge(cd, (AbstractDescriptor)annotationClassDescriptor, EdgeType.REFERENCES);
/*     */         
/* 211 */         for (String field : annotationUsage.getFieldUsages()) {
/* 212 */           for (MethodDescriptor methodDescriptor : annotationClassDescriptor.getMethods()) {
/* 213 */             if (methodDescriptor.getName().equals(field)) {
/* 214 */               model.createDependencyEdge(cd, (AbstractDescriptor)methodDescriptor, EdgeType.RESOLVE);
/*     */             }
/*     */           } 
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
/*     */   private void createTypeInstructionEdges(Model model, MethodDescriptor md) {
/* 229 */     for (AbstractMap.SimpleEntry<Object, Object> typeInstruction : (Iterable<AbstractMap.SimpleEntry<Object, Object>>)md.getTypeInstructions()) {
/* 230 */       int opcode = ((Integer)typeInstruction.getKey()).intValue();
/* 231 */       String desc = (String)typeInstruction.getValue();
/*     */       
/* 233 */       String type = Util.getTypeNameFromDescriptor(desc);
/*     */       
/* 235 */       if (opcode == 189 || opcode == 197 || opcode == 193 || opcode == 192 || opcode == 18) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 242 */         if (model.isClassModeled(type)) {
/* 243 */           ClassDescriptor cd = model.getClassDescriptor(type);
/* 244 */           model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)cd, EdgeType.RESOLVE);
/*     */         }  continue;
/* 246 */       }  if (opcode == 187)
/*     */       {
/* 248 */         if (model.isClassModeled(type)) {
/* 249 */           ClassDescriptor targetClass = model.getClassDescriptor(type);
/* 250 */           model.createDependencyEdge(md.getNode(), targetClass.getNewNode(), EdgeType.CREATES);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createEnumEdges(Model model, ClassDescriptor cd) {
/* 259 */     Set parents = new HashSet();
/* 260 */     model.getAllAncestorClasses(cd.getName(), parents);
/*     */     
/* 262 */     if (parents.contains("java/lang/Enum")) {
/*     */       
/* 264 */       String enumName = cd.getName();
/* 265 */       Type enumType = Type.getType(Util.verboseToNativeType(enumName));
/* 266 */       Type stringType = Type.getType(Util.verboseToNativeType("java/lang/String"));
/* 267 */       Type enumArray = Type.getType(Util.verboseToNativeType(enumName + "[]"));
/*     */       
/* 269 */       Collection<MethodDescriptor> methods = cd.getMethods();
/* 270 */       for (MethodDescriptor method : methods) {
/* 271 */         boolean isStatic = method.isStatic();
/* 272 */         Type retval = method.getReturnType();
/* 273 */         String name = method.getName();
/* 274 */         Type[] args = method.getArgumentTypes();
/*     */ 
/*     */         
/* 277 */         if (isStatic && retval
/* 278 */           .equals(enumType) && "valueOf"
/* 279 */           .equals(name) && args.length == 1 && args[0]
/*     */           
/* 281 */           .equals(stringType)) {
/* 282 */           model.createDependencyEdge((AbstractDescriptor)cd, (AbstractDescriptor)method, EdgeType.INVOKES);
/*     */           continue;
/*     */         } 
/* 285 */         if (isStatic && args.length == 0 && "values"
/*     */           
/* 287 */           .equals(name) && retval
/* 288 */           .equals(enumArray)) {
/* 289 */           model.createDependencyEdge((AbstractDescriptor)cd, (AbstractDescriptor)method, EdgeType.INVOKES);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private void createAssumeEdges(Model model, ClassDescriptor cd) {
/* 307 */     if (cd.isInterface()) {
/*     */       return;
/*     */     }
/*     */     
/* 311 */     Object newNode = cd.getNewNode();
/*     */     
/* 313 */     if (newNode == null) {
/* 314 */       Logger.err("no NEW-Node found for " + cd.getName());
/*     */       
/*     */       return;
/*     */     } 
/* 318 */     List<Method> externalMethods = new ArrayList<>(5);
/*     */     
/* 320 */     boolean resolvable = model.getAllExternalAncestorMethods(cd.getName(), externalMethods);
/*     */     
/* 322 */     if (resolvable) {
/* 323 */       for (Method method : externalMethods) {
/* 324 */         String mName = method.getName();
/* 325 */         String mDesc = Type.getMethodDescriptor(method);
/*     */         
/* 327 */         if (cd.implementsMethod(mName, mDesc)) {
/*     */           
/* 329 */           model.createDependencyEdge(newNode, cd.getMethod(mName, mDesc).getNode(), EdgeType.ASSUME); continue;
/*     */         } 
/* 331 */         List<ClassDescriptor> modeledClasses = new ArrayList<>();
/* 332 */         for (String interfaceName : cd.getInterfaces()) {
/* 333 */           if (model.isClassModeled(interfaceName)) modeledClasses.add(model.getClassDescriptor(interfaceName)); 
/*     */         } 
/* 335 */         if (model.isClassModeled(cd.getSuperName())) modeledClasses.add(model.getClassDescriptor(cd.getSuperName()));
/*     */         
/* 337 */         for (ClassDescriptor superDescriptor : modeledClasses) {
/* 338 */           createEdgeToImplementingMethod(superDescriptor, mName, mDesc, model, newNode, EdgeType.ASSUME, false);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 343 */       for (MethodDescriptor md : cd.getMethods()) {
/* 344 */         if (!md.isPrivate()) {
/* 345 */           model.createDependencyEdge(newNode, md.getNode(), EdgeType.ASSUME);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 350 */     List<MethodDescriptor> internalMethods = new ArrayList<>();
/* 351 */     model.getAllInternalAncestorEntrypointMethods(cd.getName(), internalMethods);
/*     */     
/* 353 */     for (MethodDescriptor md : internalMethods) {
/*     */       
/* 355 */       String mName = md.getName();
/* 356 */       String mDesc = md.getDesc();
/*     */       
/* 358 */       if (!md.isStatic() || mName.equals("<init>")) {
/*     */         
/* 360 */         if (cd.implementsMethod(mName, mDesc)) {
/* 361 */           model.createDependencyEdge(newNode, cd.getMethod(mName, mDesc).getNode(), EdgeType.ASSUME); continue;
/*     */         } 
/* 363 */         if (model.isClassModeled(cd.getSuperName())) {
/* 364 */           ClassDescriptor superCd = model.getClassDescriptor(cd.getSuperName());
/*     */           
/* 366 */           createEdgeToImplementingMethod(superCd, mName, mDesc, model, newNode, EdgeType.ASSUME, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createInvokeEdges(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 392 */     for (Invocation invocation : md.getInvocations()) {
/*     */       
/* 394 */       int opcode = invocation.getOpcode();
/* 395 */       String targetType = invocation.getType();
/* 396 */       String targetMethod = invocation.getName();
/* 397 */       String targetDesc = invocation.getDesc();
/*     */       
/* 399 */       if (model.isClassModeled(targetType)) {
/*     */         
/* 401 */         ClassDescriptor target = model.getClassDescriptor(targetType);
/*     */ 
/*     */         
/* 404 */         if (opcode == 183 && targetType
/* 405 */           .equals(cd.getSuperName())) {
/*     */           
/* 407 */           if ("<init>".equals(targetMethod) && "<init>"
/* 408 */             .equals(md.getName()))
/*     */           {
/* 410 */             MethodDescriptor initMethod = target.getMethod(targetMethod, targetDesc);
/*     */             
/* 412 */             model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)initMethod, EdgeType.CHAIN);
/*     */           
/*     */           }
/*     */           else
/*     */           {
/* 417 */             while (!target.implementsMethod(targetMethod, targetDesc) && model
/* 418 */               .isClassModeled(target.getSuperName())) {
/* 419 */               target = model.getClassDescriptor(target.getSuperName());
/*     */             }
/*     */             
/* 422 */             if (target.implementsMethod(targetMethod, targetDesc)) {
/* 423 */               model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)target
/* 424 */                   .getMethod(targetMethod, targetDesc), EdgeType.SUPER);
/*     */             }
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 430 */           if (target.isInterface() || target.isAbstract())
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 436 */             createEdgeToDeclaration(model, target, targetMethod, targetDesc, md);
/*     */           }
/*     */ 
/*     */           
/* 440 */           createEdgesToAncestorMethods(model, target, md, targetMethod, targetDesc);
/*     */           
/* 442 */           if (!targetMethod.equals("<init>"))
/*     */           {
/* 444 */             createSubtreeEdges(model, cd, target, md, targetMethod, targetDesc);
/*     */           }
/*     */         } 
/*     */         
/* 448 */         if (opcode == 186) {
/* 449 */           MethodDescriptor initMethod = target.getMethod(targetMethod, targetDesc);
/* 450 */           model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)initMethod, EdgeType.INVOKEDYNAMIC);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createEdgeToDeclaration(Model model, ClassDescriptor targetClass, String targetMethod, String targetDesc, MethodDescriptor source) {
/* 473 */     if (targetClass.implementsMethod(targetMethod, targetDesc) && (targetClass
/* 474 */       .isAbstract() || targetClass.isInterface())) {
/* 475 */       model.createDependencyEdge((AbstractDescriptor)source, (AbstractDescriptor)targetClass.getMethod(targetMethod, targetDesc), EdgeType.RESOLVE);
/*     */       
/*     */       return;
/*     */     } 
/* 479 */     String[] interfaces = targetClass.getInterfaces();
/* 480 */     if (null != interfaces)
/*     */     {
/* 482 */       for (String interfc : interfaces) {
/* 483 */         if (model.isClassModeled(interfc)) {
/* 484 */           ClassDescriptor interfaceDesc = model.getClassDescriptor(interfc);
/* 485 */           createEdgeToDeclaration(model, interfaceDesc, targetMethod, targetDesc, source);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 490 */     if (!targetClass.isInterface()) {
/* 491 */       String superName = targetClass.getSuperName();
/* 492 */       if (model.isClassModeled(superName)) {
/* 493 */         ClassDescriptor superDesc = model.getClassDescriptor(superName);
/* 494 */         createEdgeToDeclaration(model, superDesc, targetMethod, targetDesc, source);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createEdgesToAncestorMethods(Model model, ClassDescriptor owner, MethodDescriptor md, String targetMethod, String targetDesc) {
/* 518 */     if (owner.isInterface()) {
/*     */       
/* 520 */       Set<ClassDescriptor> implementingClasses = model.getAllImplementingClasses(owner);
/*     */       
/* 522 */       if (implementingClasses != null) {
/* 523 */         for (ClassDescriptor ownerImpl : implementingClasses) {
/* 524 */           createEdgesToAncestorMethods(model, ownerImpl, md, targetMethod, targetDesc);
/* 525 */           createSubtreeEdges(model, owner, ownerImpl, md, targetMethod, targetDesc);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 530 */     createEdgeToImplementingMethod(owner, targetMethod, targetDesc, model, md, EdgeType.INVOKES, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createEdgeToImplementingMethod(ClassDescriptor owner, String targetMethod, String targetDesc, Model model, MethodDescriptor md, EdgeType type, boolean createResolveEdge) {
/* 537 */     createEdgeToImplementingMethod(owner, targetMethod, targetDesc, model, md.getNode(), type, createResolveEdge);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findSuperInterfaces(Model model, ClassDescriptor start, List<ClassDescriptor> path, List<List<ClassDescriptor>> paths) {
/* 548 */     path.add(start);
/*     */     
/* 550 */     boolean hasModeled = false;
/*     */     
/* 552 */     int oldSize = path.size();
/* 553 */     for (String interfaceName : start.getInterfaces()) {
/* 554 */       if (model.isClassModeled(interfaceName)) {
/* 555 */         hasModeled = true;
/* 556 */         if (path.size() > oldSize) {
/* 557 */           path = new ArrayList<>(path.subList(0, oldSize));
/*     */         }
/* 559 */         findSuperInterfaces(model, model.getClassDescriptor(interfaceName), path, paths);
/*     */       } 
/*     */     } 
/*     */     
/* 563 */     if (!hasModeled) {
/* 564 */       paths.add(path);
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
/*     */ 
/*     */   
/*     */   private void createEdgeToImplementingMethod(ClassDescriptor owner, String targetMethod, String targetDesc, Model model, Object node, EdgeType type, boolean createResolveEdge) {
/* 584 */     ArrayList<ClassDescriptor> classHierarchy = new ArrayList<>();
/* 585 */     classHierarchy.add(owner);
/* 586 */     while (!owner.implementsMethod(targetMethod, targetDesc) && model
/* 587 */       .isClassModeled(owner.getSuperName())) {
/* 588 */       model.createDependencyEdge(node, owner.getNode(), EdgeType.RESOLVE);
/* 589 */       owner = model.getClassDescriptor(owner.getSuperName());
/* 590 */       classHierarchy.add(owner);
/*     */     } 
/* 592 */     if (owner.implementsMethod(targetMethod, targetDesc)) {
/*     */       
/* 594 */       MethodDescriptor targetMethodImp = owner.getMethod(targetMethod, targetDesc);
/*     */       
/* 596 */       model.createDependencyEdge(node, targetMethodImp.getNode(), type);
/*     */       
/* 598 */       if (createResolveEdge && !owner.isInterface()) {
/* 599 */         model.createDependencyEdge(node, targetMethodImp.getNode(), EdgeType.RESOLVE);
/*     */       }
/*     */ 
/*     */       
/* 603 */       if (targetMethodImp.isStatic()) {
/* 604 */         model.createDependencyEdge(node, owner.getNode(), EdgeType.RESOLVE);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 609 */       HashSet<String> seen = new HashSet<>();
/* 610 */       ArrayList<ClassDescriptor> interfaceDescriptors = new ArrayList<>();
/* 611 */       if (owner.isInterface()) {
/* 612 */         seen.add(owner.getName());
/* 613 */         interfaceDescriptors.add(owner);
/*     */       } 
/* 615 */       for (ClassDescriptor cd : classHierarchy) {
/* 616 */         for (String interfaceName : cd.getInterfaces()) {
/* 617 */           if (seen.add(interfaceName) && model.isClassModeled(interfaceName)) {
/* 618 */             interfaceDescriptors.add(model.getClassDescriptor(interfaceName));
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 624 */       List<List<ClassDescriptor>> interfaceHierarchies = new ArrayList<>();
/* 625 */       for (ClassDescriptor cd : interfaceDescriptors) {
/* 626 */         findSuperInterfaces(model, cd, new ArrayList<>(), interfaceHierarchies);
/*     */       }
/*     */ 
/*     */       
/* 630 */       int mostSpecificDist = 0;
/* 631 */       ClassDescriptor mostSpecific = null;
/* 632 */       for (List<ClassDescriptor> hierarchy : interfaceHierarchies) {
/* 633 */         int idx = indexOf(hierarchy, targetMethod, targetDesc);
/* 634 */         if (idx > -1) {
/* 635 */           int dist = lastIndexOf(hierarchy, targetMethod, targetDesc) - idx + 1;
/* 636 */           if (mostSpecificDist < dist) {
/* 637 */             mostSpecificDist = dist;
/* 638 */             mostSpecific = hierarchy.get(idx);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 643 */       if (mostSpecific != null) {
/* 644 */         MethodDescriptor targetMethodImp = mostSpecific.getMethod(targetMethod, targetDesc);
/*     */         
/* 646 */         model.createDependencyEdge(node, targetMethodImp.getNode(), type);
/*     */         
/* 648 */         if (createResolveEdge) {
/* 649 */           model.createDependencyEdge(node, targetMethodImp.getNode(), EdgeType.RESOLVE);
/*     */         }
/*     */ 
/*     */         
/* 653 */         if (targetMethodImp.hasFlag(1) && !targetMethodImp.hasFlag(1024)) {
/* 654 */           model.createDependencyEdge(node, owner.getNode(), EdgeType.RESOLVE);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(List<ClassDescriptor> interfaces, String methodName, String methodDescriptor) {
/* 672 */     int idx = -1;
/* 673 */     for (ClassDescriptor cd : interfaces) {
/* 674 */       idx++;
/* 675 */       if (cd.implementsMethod(methodName, methodDescriptor)) {
/* 676 */         return idx;
/*     */       }
/*     */     } 
/* 679 */     return -1;
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
/*     */   private static int lastIndexOf(List<ClassDescriptor> interfaces, String methodName, String methodDescriptor) {
/* 693 */     int idx = interfaces.size();
/* 694 */     for (ListIterator<ClassDescriptor> it = interfaces.listIterator(idx); it.hasPrevious(); ) {
/* 695 */       idx--;
/* 696 */       ClassDescriptor cd = it.previous();
/* 697 */       if (cd.implementsMethod(methodName, methodDescriptor)) {
/* 698 */         return idx;
/*     */       }
/*     */     } 
/* 701 */     return -1;
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
/*     */   private void createSubtreeEdges(Model model, ClassDescriptor cd, ClassDescriptor target, MethodDescriptor mm, String targetMethod, String targetDesc) {
/* 718 */     List<ClassDescriptor> subClasses = new ArrayList<>();
/* 719 */     model.getInternalDescendants(target, subClasses);
/*     */     
/* 721 */     for (ClassDescriptor targetSubclass : subClasses) {
/* 722 */       if (targetSubclass != cd && 
/* 723 */         targetSubclass.implementsMethod(targetMethod, targetDesc)) {
/* 724 */         model.createDependencyEdge((AbstractDescriptor)mm, (AbstractDescriptor)targetSubclass.getMethod(targetMethod, targetDesc), EdgeType.INVOKES);
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
/*     */ 
/*     */   
/*     */   private void createMethodSignatureEdges(Model model, MethodDescriptor source) {
/* 740 */     for (Type argumentType : source.getArgumentTypes()) {
/* 741 */       String str = Util.getTypeNameFromDescriptor(argumentType.getDescriptor());
/* 742 */       if (model.isClassModeled(str)) {
/* 743 */         model.createDependencyEdge((AbstractDescriptor)source, (AbstractDescriptor)model.getClassDescriptor(str), EdgeType.RESOLVE);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 748 */     Type returnType = source.getReturnType();
/* 749 */     String className = Util.getTypeNameFromDescriptor(returnType.getDescriptor());
/* 750 */     if (model.isClassModeled(className)) {
/* 751 */       model.createDependencyEdge((AbstractDescriptor)source, (AbstractDescriptor)model.getClassDescriptor(className), EdgeType.RESOLVE);
/*     */     }
/*     */ 
/*     */     
/* 755 */     if (source.getExceptions() != null) {
/* 756 */       for (String exception : source.getExceptions()) {
/* 757 */         if (model.isClassModeled(exception)) {
/* 758 */           ClassDescriptor target = model.getClassDescriptor(exception);
/* 759 */           model.createDependencyEdge((AbstractDescriptor)source, (AbstractDescriptor)target, EdgeType.RESOLVE);
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
/*     */   private void createInnerClassEdges(Model model, ClassDescriptor cd) {
/* 773 */     if (cd.isInnerClass()) {
/* 774 */       ClassDescriptor enclosingClass = model.getClassDescriptor(cd.getEnclosingClass());
/* 775 */       model.createDependencyEdge((AbstractDescriptor)cd, (AbstractDescriptor)enclosingClass, EdgeType.ENCLOSE);
/*     */     } 
/* 777 */     if (cd.getEnclosingMethod() != null) {
/* 778 */       ClassDescriptor enclosingClass = model.getClassDescriptor(cd.getEnclosingClass());
/* 779 */       MethodDescriptor enclosingMethodDescriptor = enclosingClass.getMethod(cd.getEnclosingMethod());
/* 780 */       if (null == enclosingMethodDescriptor) {
/* 781 */         Logger.log("Missing enclosing method declaration in " + enclosingClass.getName() + ": " + cd.getEnclosingMethod().getValue());
/*     */       } else {
/* 783 */         model.createDependencyEdge((AbstractDescriptor)cd, (AbstractDescriptor)enclosingMethodDescriptor, EdgeType.ENCLOSE);
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
/*     */   
/*     */   private void createReferenceEdges(Model model, MethodDescriptor md) {
/* 798 */     for (String[] fieldRef : md.getFieldRefs()) {
/*     */       
/* 800 */       String refDesc = fieldRef[0];
/* 801 */       String refName = fieldRef[1];
/*     */       
/* 803 */       if (model.isClassModeled(refDesc)) {
/*     */         
/* 805 */         ClassDescriptor owner = model.getClassDescriptor(refDesc);
/* 806 */         boolean declarationFound = owner.declaresField(refName);
/*     */         
/* 808 */         while (model.isClassModeled(owner.getSuperName()) && !declarationFound) {
/* 809 */           if (!owner.declaresField(refName)) {
/* 810 */             model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)owner, EdgeType.RESOLVE);
/*     */             
/* 812 */             for (String interfc : owner.getInterfaces()) {
/* 813 */               if (model.isClassModeled(interfc)) {
/* 814 */                 ClassDescriptor interfcDesc = model.getClassDescriptor(interfc);
/* 815 */                 if (interfcDesc.declaresField(refName)) {
/* 816 */                   model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)interfcDesc.getField(refName), EdgeType.REFERENCES);
/* 817 */                   declarationFound = true;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } else {
/* 822 */             declarationFound = true;
/*     */           } 
/* 824 */           owner = model.getClassDescriptor(owner.getSuperName());
/*     */         } 
/*     */         
/* 827 */         if (owner.declaresField(refName)) {
/*     */           
/* 829 */           model.createDependencyEdge((AbstractDescriptor)md, (AbstractDescriptor)owner.getField(refName), EdgeType.REFERENCES);
/* 830 */           checkLegacyDotClassField(refName, owner, model);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkLegacyDotClassField(String refName, ClassDescriptor owner, Model model) {
/* 838 */     if (refName.startsWith("class$")) {
/* 839 */       FieldDescriptor fd = owner.getField(refName);
/* 840 */       if ("Ljava/lang/Class;".equals(fd.getDesc()) && fd.isSynthetic()) {
/*     */         
/* 842 */         StringBuilder[] possibleClassNames = getPossibleClassNames(refName);
/*     */         
/* 844 */         for (StringBuilder possibleClassName : possibleClassNames) {
/* 845 */           String className = possibleClassName.toString();
/* 846 */           if (model.isClassModeled(className)) {
/* 847 */             ClassDescriptor cd = model.getClassDescriptor(className);
/* 848 */             model.createDependencyEdge((AbstractDescriptor)fd, (AbstractDescriptor)cd, EdgeType.RESOLVE);
/*     */           } 
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
/*     */   private StringBuilder[] getPossibleClassNames(String fieldName) {
/* 864 */     String[] toks = fieldName.substring(6).split("\\$");
/*     */     
/* 866 */     StringBuilder[] possibleClassNames = new StringBuilder[toks.length];
/*     */     int i;
/* 868 */     for (i = 0; i < possibleClassNames.length; i++) {
/* 869 */       possibleClassNames[i] = new StringBuilder();
/* 870 */       possibleClassNames[i].append(toks[0]);
/*     */     } 
/*     */     
/* 873 */     for (i = 1; i < toks.length; i++) {
/* 874 */       int j; for (j = i - 1; j >= 0; j--) {
/* 875 */         possibleClassNames[j].append("$").append(toks[i]);
/*     */       }
/* 877 */       for (j = 0; j < i; j++) {
/* 878 */         possibleClassNames[i].append("/").append(toks[j + 1]);
/*     */       }
/*     */     } 
/* 881 */     return possibleClassNames;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/Analyzer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */