/*     */ package net.java.balloontip;
/*     */ 
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.TableColumnModelEvent;
/*     */ import javax.swing.event.TableColumnModelListener;
/*     */ import net.java.balloontip.positioners.BalloonTipPositioner;
/*     */ import net.java.balloontip.styles.BalloonTipStyle;
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
/*     */ public class TableCellBalloonTip
/*     */   extends CustomBalloonTip
/*     */ {
/*     */   protected int row;
/*     */   protected int column;
/*     */   
/*  32 */   private final TableColumnModelListener columnListener = new TableColumnModelListener() {
/*     */       public void columnAdded(TableColumnModelEvent e) {
/*  34 */         if (e.getToIndex() <= TableCellBalloonTip.this.column) {
/*  35 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column + 1);
/*     */         } else {
/*  37 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column);
/*     */         } 
/*     */       } public void columnMarginChanged(ChangeEvent e) {
/*  40 */         TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column);
/*     */       }
/*     */       public void columnMoved(TableColumnModelEvent e) {
/*  43 */         if (TableCellBalloonTip.this.column == e.getFromIndex()) {
/*  44 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, e.getToIndex());
/*     */         }
/*  46 */         else if ((e.getFromIndex() > TableCellBalloonTip.this.column && e.getToIndex() > TableCellBalloonTip.this.column) || (e.getFromIndex() < TableCellBalloonTip.this.column && e.getToIndex() < TableCellBalloonTip.this.column)) {
/*     */           
/*  48 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column);
/*     */         }
/*  50 */         else if (e.getFromIndex() < TableCellBalloonTip.this.column && e.getToIndex() >= TableCellBalloonTip.this.column) {
/*  51 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column - 1);
/*     */         }
/*  53 */         else if (e.getFromIndex() > TableCellBalloonTip.this.column && e.getToIndex() <= TableCellBalloonTip.this.column) {
/*  54 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column + 1);
/*     */         } 
/*     */       }
/*     */       public void columnRemoved(TableColumnModelEvent e) {
/*  58 */         if (e.getFromIndex() == TableCellBalloonTip.this.column) {
/*  59 */           TableCellBalloonTip.this.closeBalloon();
/*  60 */         } else if (e.getFromIndex() < TableCellBalloonTip.this.column) {
/*  61 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column - 1);
/*     */         } else {
/*  63 */           TableCellBalloonTip.this.setCellPosition(TableCellBalloonTip.this.row, TableCellBalloonTip.this.column);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void columnSelectionChanged(ListSelectionEvent e) {}
/*     */     };
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -8760012691273527057L;
/*     */ 
/*     */   
/*     */   public TableCellBalloonTip(JTable table, JComponent component, int row, int column, BalloonTipStyle style, BalloonTip.Orientation alignment, BalloonTip.AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
/*  76 */     super(table, component, table.getCellRect(row, column, true), style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
/*  77 */     setup(row, column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TableCellBalloonTip(JTable table, JComponent component, int row, int column, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
/*  87 */     super(table, component, table.getCellRect(row, column, true), style, positioner, closeButton);
/*  88 */     setup(row, column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCellPosition(int row, int column) {
/*  97 */     this.row = row;
/*  98 */     this.column = column;
/*  99 */     setOffset(((JTable)this.attachedComponent).getCellRect(row, column, true));
/*     */   }
/*     */   
/*     */   public void closeBalloon() {
/* 103 */     JTable table = (JTable)this.attachedComponent;
/* 104 */     table.getColumnModel().removeColumnModelListener(this.columnListener);
/* 105 */     super.closeBalloon();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setup(int row, int column) {
/* 114 */     this.row = row;
/* 115 */     this.column = column;
/*     */     
/* 117 */     JTable table = (JTable)this.attachedComponent;
/* 118 */     table.getColumnModel().addColumnModelListener(this.columnListener);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/TableCellBalloonTip.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */