/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class LoadCycle
/*    */   implements ILoadCycle
/*    */ {
/* 35 */   protected static final Logger LOGGER = LoggerFactory.getLogger(LoadCycle.class);
/*    */   
/*    */   private boolean loaded = false;
/*    */ 
/*    */   
/*    */   public final boolean isLoaded() {
/* 41 */     return this.loaded;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void reload() {
/* 46 */     unload();
/* 47 */     load();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void unload() {
/* 52 */     if (isLoaded()) {
/*    */       try {
/* 54 */         doUnload();
/* 55 */       } catch (Exception e) {
/* 56 */         LOGGER.warn("Unabled to unload gracefully", e);
/*    */       } finally {
/* 58 */         this.loaded = false;
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void load() {
/* 65 */     if (!isLoaded())
/*    */       try {
/* 67 */         doLoad();
/* 68 */       } catch (Exception e) {
/* 69 */         LOGGER.warn("Unabled to load gracefully", e);
/*    */       } finally {
/* 71 */         this.loaded = true;
/*    */       }  
/*    */   }
/*    */   
/*    */   protected abstract void doUnload() throws Exception;
/*    */   
/*    */   protected abstract void doLoad() throws Exception;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/LoadCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */