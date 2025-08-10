/*     */ package net.java.balloontip.utils;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import javax.swing.Timer;
/*     */ import net.java.balloontip.BalloonTip;
/*     */ import net.java.balloontip.CustomBalloonTip;
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
/*     */ public final class ToolTipUtils
/*     */ {
/*     */   private static class ToolTipController
/*     */     extends MouseAdapter
/*     */     implements MouseMotionListener
/*     */   {
/*     */     private final BalloonTip balloonTip;
/*     */     private final Timer initialTimer;
/*     */     private final Timer showTimer;
/*     */     
/*     */     public ToolTipController(final BalloonTip balloonTip, int initialDelay, int showDelay) {
/*  52 */       this.balloonTip = balloonTip;
/*  53 */       this.initialTimer = new Timer(initialDelay, new ActionListener() {
/*     */             public void actionPerformed(ActionEvent e) {
/*  55 */               balloonTip.setVisible(true);
/*  56 */               ToolTipUtils.ToolTipController.this.showTimer.start();
/*     */             }
/*     */           });
/*  59 */       this.initialTimer.setRepeats(false);
/*     */       
/*  61 */       this.showTimer = new Timer(showDelay, new ActionListener() {
/*     */             public void actionPerformed(ActionEvent e) {
/*  63 */               balloonTip.setVisible(false);
/*     */             }
/*     */           });
/*  66 */       this.showTimer.setRepeats(false);
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {
/*  70 */       this.initialTimer.start();
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/*  74 */       if (this.balloonTip instanceof CustomBalloonTip)
/*     */       {
/*  76 */         if (((CustomBalloonTip)this.balloonTip).getOffset().contains(e.getPoint())) {
/*  77 */           if (!this.balloonTip.isVisible() && !this.initialTimer.isRunning()) {
/*  78 */             this.initialTimer.start();
/*     */           }
/*     */         } else {
/*  81 */           stopTimers();
/*  82 */           this.balloonTip.setVisible(false);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/*  88 */       stopTimers();
/*  89 */       this.balloonTip.setVisible(false);
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/*  93 */       stopTimers();
/*  94 */       this.balloonTip.setVisible(false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void stopTimers() {
/* 101 */       this.initialTimer.stop();
/* 102 */       this.showTimer.stop();
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
/*     */   public static void balloonToToolTip(BalloonTip bT, int initialDelay, int showDelay) {
/* 116 */     bT.setVisible(false);
/*     */     
/* 118 */     ToolTipController tTC = new ToolTipController(bT, initialDelay, showDelay);
/* 119 */     bT.getAttachedComponent().addMouseListener(tTC);
/* 120 */     bT.getAttachedComponent().addMouseMotionListener(tTC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void toolTipToBalloon(BalloonTip bT) {
/* 129 */     for (MouseListener m : bT.getAttachedComponent().getMouseListeners()) {
/* 130 */       if (m instanceof ToolTipController) {
/* 131 */         bT.getAttachedComponent().removeMouseListener(m);
/* 132 */         ((ToolTipController)m).stopTimers();
/*     */         break;
/*     */       } 
/*     */     } 
/* 136 */     for (MouseMotionListener m : bT.getAttachedComponent().getMouseMotionListeners()) {
/* 137 */       if (m instanceof ToolTipController) {
/* 138 */         bT.getAttachedComponent().removeMouseMotionListener(m);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 143 */     bT.setVisible(true);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/utils/ToolTipUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */