/*     */ package com.yworks.yshrink.core;
/*     */ 
/*     */ import com.yworks.util.graph.Network;
/*     */ import com.yworks.yshrink.model.AbstractDescriptor;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.EdgeType;
/*     */ import com.yworks.yshrink.model.MethodDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.model.NodeType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Shrinker
/*     */ {
/*     */   public void shrink(Model model) {
/*  35 */     ShrinkDfs shrinkDfs = new ShrinkDfs(model);
/*  36 */     shrinkDfs.setDirectedMode(true);
/*     */ 
/*     */     
/*  39 */     Iterator nodeIterator = model.getNetwork().nodes();
/*  40 */     while (nodeIterator.hasNext()) {
/*  41 */       Object node = nodeIterator.next();
/*  42 */       model.markObsolete(node);
/*     */     } 
/*     */     
/*  45 */     shrinkDfs.init(model.getEntryPointNode());
/*     */     
/*  47 */     int numInstantiated = -1;
/*  48 */     while (shrinkDfs.numInstantiated > numInstantiated) {
/*  49 */       numInstantiated = shrinkDfs.numInstantiated;
/*  50 */       shrinkDfs.nextRound();
/*     */     } 
/*     */     
/*  53 */     shrinkDfs.markReachableNodes();
/*     */   }
/*     */   
/*     */   private class ShrinkDfs
/*     */     extends Dfs {
/*     */     private Model model;
/*     */     private Network network;
/*     */     private Object entryPointNode;
/*     */     private Map<Object, Object> instanceMap;
/*  62 */     private int numInstantiated = 0;
/*  63 */     private int round = 0;
/*  64 */     private int numSkipped = 0;
/*     */     
/*     */     private static final int EXPLORE_MODE = 0;
/*     */     
/*     */     private static final int RESULT_MODE = 1;
/*  69 */     private int mode = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ShrinkDfs(Model model) {
/*  77 */       this.model = model;
/*  78 */       this.network = model.getNetwork();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void init(Object entryPointNode) {
/*  88 */       this.entryPointNode = entryPointNode;
/*     */       
/*  90 */       this.round = 0;
/*  91 */       if (this.instanceMap == null) {
/*  92 */         this.instanceMap = new HashMap<>();
/*     */       }
/*  94 */       Iterator nodeIterator = this.network.nodes();
/*  95 */       while (nodeIterator.hasNext()) {
/*  96 */         Object n = nodeIterator.next();
/*  97 */         this.instanceMap.put(n, Integer.valueOf(-1));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected int nextRound() {
/* 107 */       this.round++;
/* 108 */       this.numSkipped = 0;
/* 109 */       this.numInstantiated = 0;
/* 110 */       start(this.network, this.entryPointNode);
/* 111 */       return this.numInstantiated;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void postVisit(Object node, int i, int j) {
/* 117 */       if (this.mode == 0 && 
/* 118 */         NodeType.isNewNode(this.model.getNodeType(node))) {
/*     */         
/* 120 */         Object classNode = this.model.getClassNode(node);
/*     */         
/* 122 */         this.instanceMap.put(classNode, Integer.valueOf(this.round));
/* 123 */         this.numInstantiated++;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void preVisit(Object node, int dfsNumber) {
/* 131 */       if (this.mode == 1)
/*     */       {
/* 133 */         this.model.markNotObsolete(node);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void markReachableNodes() {
/* 141 */       int oldMode = this.mode;
/* 142 */       this.mode = 1;
/* 143 */       start(this.network, this.entryPointNode);
/* 144 */       this.mode = oldMode;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean doTraverse(Object edge) {
/* 150 */       boolean allowed = false;
/*     */ 
/*     */       
/* 153 */       Object target = this.network.getTarget(edge);
/*     */ 
/*     */       
/* 156 */       if (!NodeType.isMethodNode(this.model.getNodeType(target))) {
/*     */         
/* 158 */         allowed = true;
/*     */       
/*     */       }
/* 161 */       else if (!this.model.getDependencyType(edge).equals(EdgeType.RESOLVE) && 
/* 162 */         !this.model.getDependencyType(edge).equals(EdgeType.ENCLOSE)) {
/*     */         
/* 164 */         AbstractDescriptor targetDescriptor = this.model.getDescriptor(target);
/* 165 */         MethodDescriptor targetMethod = (MethodDescriptor)targetDescriptor;
/* 166 */         Object classNode = this.model.getClassNode(target);
/* 167 */         ClassDescriptor targetClass = (ClassDescriptor)this.model.getDescriptor(classNode);
/*     */         
/* 169 */         allowed = (allowed || targetMethod.isStatic());
/*     */ 
/*     */         
/* 172 */         allowed = (allowed || (targetMethod.hasFlag(1) && !targetMethod.hasFlag(1024)));
/*     */         
/* 174 */         allowed = (allowed || targetClass.isAnnotation());
/*     */         
/* 176 */         allowed = (allowed || this.model.getDependencyType(edge).equals(EdgeType.SUPER));
/*     */ 
/*     */         
/* 179 */         allowed = (allowed || NodeType.isNewNode(this.model.getNodeType(target)));
/*     */         
/* 181 */         allowed = (allowed || "<init>".equals(targetMethod.getName()));
/*     */         
/* 183 */         allowed = (allowed || targetMethod.isPrivate());
/*     */         
/* 185 */         allowed = (allowed || wasClassInstantiated(edge));
/*     */         
/* 187 */         allowed = (allowed || isMethodNeeded(targetClass, targetMethod));
/*     */         
/* 189 */         allowed = (allowed || this.model.getDependencyType(edge).equals(EdgeType.INVOKEDYNAMIC));
/*     */       
/*     */       }
/* 192 */       else if (this.mode == 1) {
/* 193 */         this.model.markStubNeeded(target);
/*     */       } 
/*     */ 
/*     */       
/* 197 */       return allowed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isMethodNeeded(ClassDescriptor cd, MethodDescriptor md) {
/* 206 */       List<ClassDescriptor> descendants = new ArrayList<>(5);
/* 207 */       this.model.getInternalDescendants(cd, descendants);
/*     */       
/* 209 */       for (ClassDescriptor descendant : descendants) {
/*     */         
/* 211 */         if (!descendant.implementsMethod(md.getName(), md.getDesc()) && ((Integer)this.instanceMap
/* 212 */           .get(descendant.getNode())).intValue() >= this.round - 1) {
/* 213 */           return true;
/*     */         }
/*     */       } 
/* 216 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean wasClassInstantiated(Object edge) {
/* 221 */       Object targetNode = this.network.getTarget(edge);
/* 222 */       Object classNode = this.model.getClassNode(targetNode);
/* 223 */       if (((Integer)this.instanceMap.get(classNode)).intValue() >= this.round - 1) {
/* 224 */         return true;
/*     */       }
/*     */       
/* 227 */       this.numSkipped++;
/*     */       
/* 229 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected int getNumSkipped() {
/* 239 */       return this.numSkipped;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/Shrinker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */