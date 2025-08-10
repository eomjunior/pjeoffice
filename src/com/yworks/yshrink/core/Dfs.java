/*     */ package com.yworks.yshrink.core;
/*     */ 
/*     */ import com.yworks.util.graph.Network;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Dfs
/*     */ {
/*  32 */   protected static Object WHITE = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   protected static Object GRAY = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   protected static Object BLACK = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean directedMode = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectedMode(boolean directed) {
/*  60 */     this.directedMode = directed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Network network, Object start) {
/*  71 */     if (null == start)
/*  72 */       return;  this.network = network;
/*     */     
/*  74 */     this.stateMap = new HashMap<>();
/*  75 */     if (!this.directedMode) {
/*  76 */       this.edgeVisit = new HashMap<>();
/*     */     }
/*     */     
/*  79 */     this.dfsNum = 0;
/*  80 */     this.compNum = 0;
/*     */     
/*  82 */     int stackSize = Math.min(60, network.nodesSize().intValue() + 3);
/*  83 */     Stack stack = new Stack(stackSize);
/*     */     
/*     */     try {
/*  86 */       workStack(stack, start);
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */       
/*  94 */       this.stateMap.clear();
/*  95 */       if (!this.directedMode) {
/*  96 */         this.edgeVisit.clear();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object nextEdge(Object currentNode, Object currentEdge, byte[] currentMode) {
/*     */     Object edge;
/* 103 */     switch (currentMode[0]) {
/*     */       
/*     */       case 0:
/* 106 */         if (this.directedMode) {
/* 107 */           currentMode[0] = 1;
/*     */ 
/*     */           
/* 110 */           return this.network.firstOutEdge(currentNode);
/*     */         } 
/* 112 */         edge = this.network.firstOutEdge(currentNode);
/* 113 */         if (edge == null) {
/* 114 */           edge = this.network.firstInEdge(currentNode);
/* 115 */           currentMode[0] = 3;
/*     */         } else {
/* 117 */           currentMode[0] = 2;
/*     */         } 
/* 119 */         return edge;
/*     */ 
/*     */       
/*     */       case 1:
/* 123 */         return this.network.nextOutEdge(currentEdge);
/*     */       case 2:
/* 125 */         edge = this.network.nextOutEdge(currentEdge);
/* 126 */         if (edge == null) {
/* 127 */           edge = this.network.firstInEdge(currentNode);
/* 128 */           currentMode[0] = 3;
/*     */         } 
/* 130 */         return edge;
/*     */       
/*     */       case 3:
/* 133 */         return this.network.nextInEdge(currentEdge);
/*     */     } 
/* 135 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object doNextEdge(Object currentNode, Object currentEdge, byte[] currentMode) {
/* 141 */     Object edge = nextEdge(currentNode, currentEdge, currentMode);
/*     */     
/* 143 */     while (edge != null && !doTraverse(edge)) {
/* 144 */       edge = nextEdge(currentNode, edge, currentMode);
/*     */     }
/*     */     
/* 147 */     return edge;
/*     */   }
/*     */   
/* 150 */   private byte[] nextState = new byte[1]; private Map<Object, Object> edgeVisit;
/*     */   
/*     */   private void workStack(Stack stack, Object start) {
/* 153 */     this.nextState[0] = 0;
/* 154 */     Object currentNode = start;
/* 155 */     this.stateMap.put(currentNode, GRAY);
/* 156 */     preVisit(currentNode, ++this.dfsNum);
/*     */ 
/*     */     
/* 159 */     Object nextEdge = doNextEdge(currentNode, null, this.nextState);
/* 160 */     stack.pushState(currentNode, nextEdge, this.nextState[0], this.dfsNum);
/*     */ 
/*     */     
/* 163 */     while (!stack.isEmpty()) {
/*     */       
/* 165 */       Object edge = stack.peekCurrentEdge();
/* 166 */       this.nextState[0] = stack.peekIteratorState();
/*     */       
/* 168 */       while (edge != null) {
/*     */         
/* 170 */         if (this.directedMode || !((Boolean)this.edgeVisit.get(edge)).booleanValue()) {
/*     */           Object other;
/* 172 */           if (!this.directedMode) {
/* 173 */             this.edgeVisit.put(edge, Boolean.valueOf(true));
/* 174 */             other = this.network.opposite(edge, currentNode);
/*     */           } else {
/* 176 */             other = this.network.getTarget(edge);
/*     */           } 
/* 178 */           if (this.stateMap.get(other) == null) {
/*     */ 
/*     */             
/* 181 */             preTraverse(edge, other, true);
/*     */             
/* 183 */             this.stateMap.put(other, GRAY);
/* 184 */             currentNode = other;
/* 185 */             preVisit(currentNode, ++this.dfsNum);
/*     */             
/* 187 */             this.nextState[0] = 0;
/* 188 */             edge = doNextEdge(currentNode, null, this.nextState);
/*     */             
/* 190 */             stack.pushState(currentNode, edge, this.nextState[0], this.dfsNum);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 195 */           preTraverse(edge, other, false);
/*     */           
/* 197 */           edge = doNextEdge(currentNode, edge, this.nextState);
/*     */           
/* 199 */           stack.updateTop(edge, this.nextState[0]);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 205 */         edge = doNextEdge(currentNode, edge, this.nextState);
/*     */         
/* 207 */         stack.updateTop(edge, this.nextState[0]);
/*     */       } 
/*     */       
/* 210 */       postVisit(currentNode, stack.peekLocalDfsNum(), ++this.compNum);
/* 211 */       this.stateMap.put(currentNode, BLACK);
/* 212 */       stack.pop();
/* 213 */       if (!stack.isEmpty()) {
/* 214 */         Object currentEdge = stack.peekCurrentEdge();
/* 215 */         postTraverse(currentEdge, currentNode);
/* 216 */         currentNode = stack.peekNode();
/* 217 */         this.nextState[0] = stack.peekIteratorState();
/*     */         
/* 219 */         Object object1 = doNextEdge(currentNode, currentEdge, this.nextState);
/*     */         
/* 221 */         stack.updateTop(object1, this.nextState[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int dfsNum;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int compNum;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Network network;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> stateMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void preVisit(Object node, int dfsNumber) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postVisit(Object node, int dfsNumber, int compNumber) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean preTraverse(Object edge, Object node, boolean treeEdge) {
/* 260 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postTraverse(Object edge, Object node) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doTraverse(Object e) {
/* 280 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Stack
/*     */   {
/* 290 */     int stackIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     byte[] iteratorStates;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object[] currentEdges;
/*     */ 
/*     */ 
/*     */     
/*     */     int[] localDfsNums;
/*     */ 
/*     */ 
/*     */     
/*     */     Object[] nodes;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Stack(int initialSize) {
/* 314 */       this.localDfsNums = new int[initialSize];
/* 315 */       this.currentEdges = new Object[initialSize];
/* 316 */       this.iteratorStates = new byte[initialSize];
/* 317 */       this.nodes = new Object[initialSize];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isEmpty() {
/* 326 */       return (this.stackIndex < 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void pop() {
/* 333 */       this.stackIndex--;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object peekNode() {
/* 342 */       return this.nodes[this.stackIndex];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object peekCurrentEdge() {
/* 351 */       return this.currentEdges[this.stackIndex];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     byte peekIteratorState() {
/* 360 */       return this.iteratorStates[this.stackIndex];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int peekLocalDfsNum() {
/* 369 */       return this.localDfsNums[this.stackIndex];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int pushState(Object node, Object currentEdge, byte iterastorState, int localDfsNum) {
/* 382 */       this.stackIndex++;
/* 383 */       if (this.stackIndex == this.nodes.length) {
/* 384 */         int newSize = (this.stackIndex + 1) * 2;
/* 385 */         Object[] newStack = new Object[newSize];
/* 386 */         System.arraycopy(this.nodes, 0, newStack, 0, this.nodes.length);
/* 387 */         this.nodes = newStack;
/* 388 */         Object[] newEStack = new Object[newSize];
/* 389 */         System.arraycopy(this.currentEdges, 0, newEStack, 0, this.currentEdges.length);
/* 390 */         this.currentEdges = newEStack;
/* 391 */         int[] newDStack = new int[newSize];
/* 392 */         System.arraycopy(this.localDfsNums, 0, newDStack, 0, this.localDfsNums.length);
/* 393 */         this.localDfsNums = newDStack;
/* 394 */         byte[] newStateStack = new byte[newSize];
/* 395 */         System.arraycopy(this.iteratorStates, 0, newStateStack, 0, this.iteratorStates.length);
/* 396 */         this.iteratorStates = newStateStack;
/*     */       } 
/* 398 */       this.nodes[this.stackIndex] = node;
/* 399 */       this.currentEdges[this.stackIndex] = currentEdge;
/* 400 */       this.iteratorStates[this.stackIndex] = iterastorState;
/* 401 */       this.localDfsNums[this.stackIndex] = localDfsNum; return localDfsNum;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void updateTop(Object currentEdge, byte iteratorState) {
/* 411 */       this.currentEdges[this.stackIndex] = currentEdge;
/* 412 */       this.iteratorStates[this.stackIndex] = iteratorState;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/Dfs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */