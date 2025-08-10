/*     */ package net.java.balloontip;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.event.TreeExpansionEvent;
/*     */ import javax.swing.event.TreeExpansionListener;
/*     */ import javax.swing.event.TreeModelEvent;
/*     */ import javax.swing.event.TreeModelListener;
/*     */ import javax.swing.tree.TreePath;
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
/*     */ 
/*     */ public class TreeNodeBalloonTip
/*     */   extends CustomBalloonTip
/*     */ {
/*     */   protected TreePath path;
/*     */   
/*  34 */   private TreeExpansionListener expansionListener = new TreeExpansionListener() {
/*     */       public void treeExpanded(TreeExpansionEvent e) {
/*  36 */         if (TreeNodeBalloonTip.this.getTree().isVisible(TreeNodeBalloonTip.this.path)) {
/*  37 */           TreeNodeBalloonTip.this.visibilityControl.setCriterionAndUpdate("treeExpansion", Boolean.valueOf(true));
/*     */         }
/*  39 */         TreeNodeBalloonTip.this.setTreePath(TreeNodeBalloonTip.this.path);
/*     */       }
/*     */       
/*     */       public void treeCollapsed(TreeExpansionEvent e) {
/*  43 */         if (!TreeNodeBalloonTip.this.getTree().isVisible(TreeNodeBalloonTip.this.path)) {
/*  44 */           TreeNodeBalloonTip.this.visibilityControl.setCriterionAndUpdate("treeExpansion", Boolean.valueOf(false));
/*     */         } else {
/*  46 */           TreeNodeBalloonTip.this.setTreePath(TreeNodeBalloonTip.this.path);
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  52 */   private TreeModelListener modelListener = new TreeModelListener() {
/*     */       public void treeStructureChanged(TreeModelEvent e) {
/*  54 */         TreeNodeBalloonTip.this.setTreePath(TreeNodeBalloonTip.this.path);
/*     */       }
/*     */       
/*     */       public void treeNodesRemoved(TreeModelEvent e) {
/*  58 */         boolean closeBalloon = false;
/*  59 */         for (Object child : e.getChildren()) {
/*     */           
/*  61 */           if (e.getTreePath().pathByAddingChild(child).isDescendant(TreeNodeBalloonTip.this.path)) {
/*  62 */             closeBalloon = true;
/*     */           }
/*     */         } 
/*     */         
/*  66 */         if (closeBalloon) {
/*  67 */           TreeNodeBalloonTip.this.closeBalloon();
/*     */         } else {
/*  69 */           TreeNodeBalloonTip.this.setTreePath(TreeNodeBalloonTip.this.path);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void treeNodesInserted(TreeModelEvent e) {
/*  74 */         TreeNodeBalloonTip.this.setTreePath(TreeNodeBalloonTip.this.path);
/*     */       }
/*     */       
/*     */       public void treeNodesChanged(TreeModelEvent e) {
/*  78 */         TreeNodeBalloonTip.this.setTreePath(TreeNodeBalloonTip.this.path);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -7270789090236631717L;
/*     */ 
/*     */   
/*     */   public TreeNodeBalloonTip(JTree tree, JComponent component, TreePath path, BalloonTipStyle style, BalloonTip.Orientation alignment, BalloonTip.AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
/*  88 */     super(tree, component, tree.getPathBounds(path), style, alignment, attachLocation, horizontalOffset, verticalOffset, useCloseButton);
/*  89 */     setup(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeNodeBalloonTip(JTree tree, JComponent component, TreePath path, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
/*  98 */     super(tree, component, tree.getPathBounds(path), style, positioner, closeButton);
/*  99 */     setup(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTreePath(TreePath path) {
/* 107 */     this.path = path;
/* 108 */     Rectangle bounds = getTree().getPathBounds(path);
/* 109 */     if (bounds != null) {
/* 110 */       setOffset(bounds);
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeBalloon() {
/* 115 */     JTree tree = getTree();
/* 116 */     tree.removeTreeExpansionListener(this.expansionListener);
/* 117 */     tree.getModel().removeTreeModelListener(this.modelListener);
/* 118 */     super.closeBalloon();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setup(TreePath path) {
/* 126 */     this.path = path;
/* 127 */     if (!getTree().isVisible(path)) {
/* 128 */       this.visibilityControl.setCriterionAndUpdate("treePath", Boolean.valueOf(false));
/*     */     }
/*     */     
/* 131 */     JTree tree = getTree();
/* 132 */     tree.addTreeExpansionListener(this.expansionListener);
/* 133 */     tree.getModel().addTreeModelListener(this.modelListener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JTree getTree() {
/* 141 */     return (JTree)this.attachedComponent;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/TreeNodeBalloonTip.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */