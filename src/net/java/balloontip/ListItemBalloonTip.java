/*    */ package net.java.balloontip;
/*    */ 
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.event.ListDataEvent;
/*    */ import javax.swing.event.ListDataListener;
/*    */ import net.java.balloontip.positioners.BalloonTipPositioner;
/*    */ import net.java.balloontip.styles.BalloonTipStyle;
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
/*    */ public class ListItemBalloonTip
/*    */   extends CustomBalloonTip
/*    */ {
/*    */   protected int index;
/*    */   
/* 29 */   private final ListDataListener dataListener = new ListDataListener()
/*    */     {
/*    */       public void intervalAdded(ListDataEvent e) {
/* 32 */         if (e.getIndex1() <= ListItemBalloonTip.this.index) {
/* 33 */           ListItemBalloonTip.this.index += e.getIndex1() - e.getIndex0() + 1;
/*    */         }
/* 35 */         ListItemBalloonTip.this.setItemPosition(ListItemBalloonTip.this.index);
/*    */       }
/*    */ 
/*    */       
/*    */       public void intervalRemoved(ListDataEvent e) {
/* 40 */         if (e.getIndex1() < ListItemBalloonTip.this.index) {
/* 41 */           ListItemBalloonTip.this.index -= e.getIndex1() - e.getIndex0() + 1;
/* 42 */           ListItemBalloonTip.this.setItemPosition(ListItemBalloonTip.this.index);
/*    */         }
/* 44 */         else if (ListItemBalloonTip.this.index >= e.getIndex0() && ListItemBalloonTip.this.index <= e.getIndex1()) {
/* 45 */           ListItemBalloonTip.this.closeBalloon();
/*    */         } else {
/* 47 */           ListItemBalloonTip.this.setItemPosition(ListItemBalloonTip.this.index);
/*    */         } 
/*    */       }
/*    */       
/*    */       public void contentsChanged(ListDataEvent e) {
/* 52 */         ListItemBalloonTip.this.setItemPosition(ListItemBalloonTip.this.index);
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = -7270789090236631717L;
/*    */ 
/*    */   
/*    */   public ListItemBalloonTip(JList<?> list, JComponent component, int index, BalloonTipStyle style, BalloonTip.Orientation alignment, BalloonTip.AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
/* 62 */     super(list, component, list.getCellBounds(index, index), style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
/* 63 */     setup(index);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ListItemBalloonTip(JList<?> list, JComponent component, int index, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
/* 72 */     super(list, component, list.getCellBounds(index, index), style, positioner, closeButton);
/* 73 */     setup(index);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setItemPosition(int index) {
/* 81 */     setOffset(((JList)this.attachedComponent).getCellBounds(index, index));
/*    */   }
/*    */   
/*    */   public void closeBalloon() {
/* 85 */     JList<?> list = (JList)this.attachedComponent;
/* 86 */     list.getModel().removeListDataListener(this.dataListener);
/* 87 */     super.closeBalloon();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setup(int index) {
/* 95 */     this.index = index;
/* 96 */     JList<?> list = (JList)this.attachedComponent;
/* 97 */     list.getModel().addListDataListener(this.dataListener);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/ListItemBalloonTip.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */