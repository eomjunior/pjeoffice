/*     */ package com.yworks.yshrink.model;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NodeType
/*     */ {
/*     */   public static final int METHOD = 1;
/*     */   public static final int FIELD = 2;
/*     */   public static final int CLASS = 4;
/*     */   public static final int INTERFACE = 16;
/*     */   public static final int NEW = 32;
/*     */   public static final int ENTRYPOINT = 64;
/*     */   public static final int STATIC = 256;
/*     */   public static final int OBSOLETE = 8192;
/*     */   public static final int STUB = 16384;
/*     */   
/*     */   public static boolean isObsolete(int nodeType) {
/*  62 */     return ((nodeType & 0x2000) == 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStatic(int nodeType) {
/*  72 */     return ((nodeType & 0x100) == 256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStubNeeded(int nodeType) {
/*  82 */     return ((nodeType & 0x4000) == 16384);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMethodNode(int nodeType) {
/*  92 */     return ((nodeType & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInterfaceNode(int nodeType) {
/* 102 */     return ((nodeType & 0x10) == 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNewNode(int nodeType) {
/* 112 */     return ((nodeType & 0x20) == 32);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/NodeType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */