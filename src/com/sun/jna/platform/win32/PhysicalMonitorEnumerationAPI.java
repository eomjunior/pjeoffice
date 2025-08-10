/*    */ package com.sun.jna.platform.win32;
/*    */ 
/*    */ import com.sun.jna.Structure;
/*    */ import com.sun.jna.Structure.FieldOrder;
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
/*    */ public interface PhysicalMonitorEnumerationAPI
/*    */ {
/*    */   public static final int PHYSICAL_MONITOR_DESCRIPTION_SIZE = 128;
/*    */   
/*    */   @FieldOrder({"hPhysicalMonitor", "szPhysicalMonitorDescription"})
/*    */   public static class PHYSICAL_MONITOR
/*    */     extends Structure
/*    */   {
/*    */     public WinNT.HANDLE hPhysicalMonitor;
/* 67 */     public char[] szPhysicalMonitorDescription = new char[128];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/sun/jna/platform/win32/PhysicalMonitorEnumerationAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */