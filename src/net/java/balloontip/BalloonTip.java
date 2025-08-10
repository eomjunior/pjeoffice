/*     */ package net.java.balloontip;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.AncestorEvent;
/*     */ import javax.swing.event.AncestorListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import net.java.balloontip.positioners.BalloonTipPositioner;
/*     */ import net.java.balloontip.positioners.BasicBalloonTipPositioner;
/*     */ import net.java.balloontip.positioners.LeftAbovePositioner;
/*     */ import net.java.balloontip.positioners.LeftBelowPositioner;
/*     */ import net.java.balloontip.positioners.RightAbovePositioner;
/*     */ import net.java.balloontip.positioners.RightBelowPositioner;
/*     */ import net.java.balloontip.styles.BalloonTipStyle;
/*     */ import net.java.balloontip.styles.RoundedBalloonStyle;
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
/*     */ public class BalloonTip
/*     */   extends JPanel
/*     */ {
/*     */   public enum Orientation
/*     */   {
/*  69 */     LEFT_ABOVE, RIGHT_ABOVE, LEFT_BELOW, RIGHT_BELOW;
/*     */   }
/*     */   
/*     */   public enum AttachLocation {
/*  73 */     ALIGNED, CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
/*     */   }
/*  75 */   protected JComponent contents = null;
/*  76 */   protected JButton closeButton = null;
/*  77 */   protected VisibilityControl visibilityControl = new VisibilityControl();
/*     */   protected BalloonTipStyle style;
/*  79 */   protected int padding = 0;
/*  80 */   protected float opacity = 1.0F;
/*     */   protected BalloonTipPositioner positioner;
/*  82 */   protected JLayeredPane topLevelContainer = null;
/*     */   
/*     */   protected JComponent attachedComponent;
/*  85 */   private static Icon defaultCloseIcon = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_default.png"));
/*  86 */   private static Icon rolloverCloseIcon = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_rollover.png"));
/*  87 */   private static Icon pressedCloseIcon = new ImageIcon(BalloonTip.class.getResource("/net/java/balloontip/images/close_pressed.png"));
/*     */ 
/*     */   
/*  90 */   private final ComponentListener componentListener = new ComponentListener() {
/*     */       public void componentMoved(ComponentEvent e) {
/*  92 */         BalloonTip.this.refreshLocation();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void componentResized(ComponentEvent e) {
/*  98 */         BalloonTip.this.visibilityControl.setCriterionAndUpdate("attachedComponentShowing", Boolean.valueOf((BalloonTip.this.attachedComponent.getWidth() > 0 && BalloonTip.this.attachedComponent.getHeight() > 0)));
/*     */         
/* 100 */         BalloonTip.this.refreshLocation();
/*     */       }
/*     */       public void componentShown(ComponentEvent e) {
/* 103 */         BalloonTip.this.visibilityControl.setCriterionAndUpdate("attachedComponentShowing", Boolean.valueOf(BalloonTip.this.isAttachedComponentShowing()));
/* 104 */         BalloonTip.this.refreshLocation();
/*     */       }
/*     */       public void componentHidden(ComponentEvent e) {
/* 107 */         BalloonTip.this.visibilityControl.setCriterionAndUpdate("attachedComponentShowing", Boolean.valueOf(false));
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 112 */   private final ComponentAdapter topLevelContainerListener = new ComponentAdapter() {
/*     */       public void componentResized(ComponentEvent e) {
/* 114 */         BalloonTip.this.refreshLocation();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 119 */   private ComponentAdapter tabbedPaneListener = null;
/*     */ 
/*     */   
/* 122 */   protected NestedViewportListener viewportListener = null;
/*     */ 
/*     */   
/* 125 */   private MouseAdapter clickListener = null;
/*     */ 
/*     */   
/* 128 */   private AncestorListener ancestorListener = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 8883837312240932652L;
/*     */ 
/*     */ 
/*     */   
/*     */   public BalloonTip(JComponent attachedComponent, String text) {
/* 137 */     this(attachedComponent, text, (BalloonTipStyle)new RoundedBalloonStyle(5, 5, Color.WHITE, Color.BLACK), true);
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
/*     */   public BalloonTip(JComponent attachedComponent, String text, BalloonTipStyle style, boolean useCloseButton) {
/* 149 */     this(attachedComponent, new JLabel(text), style, useCloseButton);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BalloonTip(JComponent attachedComponent, JComponent contents, BalloonTipStyle style, boolean useCloseButton) {
/* 160 */     this(attachedComponent, contents, style, Orientation.LEFT_ABOVE, AttachLocation.ALIGNED, 15, 15, useCloseButton);
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
/*     */   public BalloonTip(JComponent attachedComponent, JComponent contents, BalloonTipStyle style, Orientation orientation, AttachLocation attachLocation, int horizontalOffset, int verticalOffset, boolean useCloseButton) {
/* 177 */     setup(attachedComponent, contents, style, setupPositioner(orientation, attachLocation, horizontalOffset, verticalOffset), useCloseButton ? getDefaultCloseButton() : null);
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
/*     */   public BalloonTip(JComponent attachedComponent, JComponent contents, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
/* 191 */     setup(attachedComponent, contents, style, positioner, closeButton);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContents(JComponent contents) {
/* 201 */     JComponent oldContents = this.contents;
/* 202 */     if (oldContents != null) {
/* 203 */       remove(this.contents);
/*     */     }
/* 205 */     this.contents = contents;
/*     */     
/* 207 */     if (contents != null) {
/* 208 */       setPadding(getPadding());
/* 209 */       add(this.contents, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 17, 1, new Insets(0, 0, 0, 0), 0, 0));
/* 210 */       this.visibilityControl.setCriterionAndUpdate("hasContents", Boolean.valueOf(true));
/*     */     } else {
/* 212 */       this.visibilityControl.setCriterionAndUpdate("hasContents", Boolean.valueOf(false));
/*     */     } 
/*     */ 
/*     */     
/* 216 */     firePropertyChange("contents", oldContents, this.contents);
/* 217 */     refreshLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextContents(String text) {
/* 226 */     setContents(new JLabel(text));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent getContents() {
/* 234 */     return this.contents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPadding(int padding) {
/* 243 */     this.padding = padding;
/* 244 */     this.contents.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
/* 245 */     refreshLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPadding() {
/* 253 */     return this.padding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(BalloonTipStyle style) {
/* 262 */     BalloonTipStyle oldStyle = this.style;
/* 263 */     this.style = style;
/* 264 */     setBorder((Border)this.style);
/*     */ 
/*     */     
/* 267 */     firePropertyChange("style", oldStyle, style);
/* 268 */     refreshLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BalloonTipStyle getStyle() {
/* 276 */     return this.style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPositioner(BalloonTipPositioner positioner) {
/* 285 */     BalloonTipPositioner oldPositioner = this.positioner;
/* 286 */     this.positioner = positioner;
/* 287 */     this.positioner.setBalloonTip(this);
/*     */ 
/*     */     
/* 290 */     firePropertyChange("positioner", oldPositioner, positioner);
/* 291 */     refreshLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BalloonTipPositioner getPositioner() {
/* 299 */     return this.positioner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeBalloon() {
/* 309 */     forceSetVisible(false);
/* 310 */     setCloseButton((JButton)null);
/* 311 */     for (MouseListener m : getMouseListeners()) {
/* 312 */       removeMouseListener(m);
/*     */     }
/* 314 */     tearDownHelper();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloseButton(JButton button) {
/* 324 */     if (this.closeButton != null) {
/* 325 */       for (ActionListener a : this.closeButton.getActionListeners()) {
/* 326 */         this.closeButton.removeActionListener(a);
/*     */       }
/* 328 */       remove(this.closeButton);
/* 329 */       this.closeButton = null;
/*     */     } 
/*     */ 
/*     */     
/* 333 */     if (button != null) {
/* 334 */       this.closeButton = button;
/* 335 */       add(this.closeButton, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 12, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     } 
/*     */     
/* 338 */     refreshLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloseButton(JButton button, boolean permanentClose) {
/* 348 */     if (button != null) {
/* 349 */       if (permanentClose) {
/* 350 */         button.addActionListener(new ActionListener() {
/*     */               public void actionPerformed(ActionEvent e) {
/* 352 */                 BalloonTip.this.closeBalloon();
/*     */               }
/*     */             });
/*     */       } else {
/* 356 */         button.addActionListener(new ActionListener() {
/*     */               public void actionPerformed(ActionEvent e) {
/* 358 */                 BalloonTip.this.setVisible(false);
/*     */               }
/*     */             });
/*     */       } 
/*     */     }
/* 363 */     setCloseButton(button);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JButton getCloseButton() {
/* 371 */     return this.closeButton;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JButton getDefaultCloseButton() {
/* 379 */     JButton button = new JButton();
/* 380 */     button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
/* 381 */     button.setContentAreaFilled(false);
/* 382 */     button.setIcon(defaultCloseIcon);
/* 383 */     button.setRolloverIcon(rolloverCloseIcon);
/* 384 */     button.setPressedIcon(pressedCloseIcon);
/* 385 */     return button;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultCloseButtonIcons(Icon normal, Icon pressed, Icon rollover) {
/* 396 */     defaultCloseIcon = normal;
/* 397 */     rolloverCloseIcon = rollover;
/* 398 */     pressedCloseIcon = pressed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDefaultMouseListener(boolean permanentClose) {
/* 407 */     removeMouseListener(this.clickListener);
/* 408 */     if (permanentClose) {
/* 409 */       this.clickListener = new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 411 */             e.consume();
/* 412 */             BalloonTip.this.closeBalloon();
/*     */           }
/*     */         };
/*     */     } else {
/* 416 */       this.clickListener = new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 418 */             e.consume();
/* 419 */             BalloonTip.this.setVisible(false);
/*     */           }
/*     */         };
/*     */     } 
/* 423 */     addMouseListener(this.clickListener);
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
/*     */   public void setAttachedComponent(JComponent newComponent) {
/* 435 */     JComponent oldComponent = this.attachedComponent;
/*     */     
/* 437 */     tearDownHelper();
/* 438 */     this.attachedComponent = newComponent;
/* 439 */     setupHelper();
/*     */ 
/*     */     
/* 442 */     firePropertyChange("attachedComponent", oldComponent, this.attachedComponent);
/* 443 */     refreshLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent getAttachedComponent() {
/* 451 */     return this.attachedComponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTopLevelContainer(JLayeredPane tlc) {
/* 460 */     if (this.topLevelContainer != null) {
/* 461 */       this.topLevelContainer.remove(this);
/* 462 */       this.topLevelContainer.removeComponentListener(this.topLevelContainerListener);
/*     */     } 
/*     */     
/* 465 */     this.topLevelContainer = tlc;
/*     */     
/* 467 */     this.topLevelContainer.setLayer(this, JLayeredPane.POPUP_LAYER.intValue());
/*     */     
/* 469 */     this.topLevelContainer.addComponentListener(this.topLevelContainerListener);
/*     */     
/* 471 */     this.topLevelContainer.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JLayeredPane getTopLevelContainer() {
/* 480 */     return this.topLevelContainer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getAttachedRectangle() {
/* 488 */     Point location = SwingUtilities.convertPoint(this.attachedComponent, getLocation(), this);
/* 489 */     return new Rectangle(location.x, location.y, this.attachedComponent.getWidth(), this.attachedComponent.getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshLocation() {
/* 497 */     if (this.topLevelContainer != null) {
/* 498 */       this.positioner.determineAndSetLocation(getAttachedRectangle());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpacity(float opacity) {
/* 508 */     this.opacity = opacity;
/* 509 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOpacity() {
/* 517 */     return this.opacity;
/*     */   }
/*     */   
/*     */   public void paintComponent(Graphics g) {
/* 521 */     if (this.opacity != 1.0F) {
/* 522 */       ((Graphics2D)g).setComposite(AlphaComposite.getInstance(3, this.opacity));
/*     */     }
/* 524 */     super.paintComponent(g);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 533 */     this.visibilityControl.setCriterionAndUpdate("manual", Boolean.valueOf(visible));
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 537 */     closeBalloon();
/* 538 */     super.finalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void forceSetVisible(boolean visible) {
/* 547 */     super.setVisible(visible);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAttachedComponentShowing() {
/* 556 */     return (this.attachedComponent.isShowing() && this.attachedComponent.getWidth() > 0 && this.attachedComponent.getHeight() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void notifyViewportListener() {
/* 565 */     if (this.viewportListener != null) {
/* 566 */       this.viewportListener.stateChanged(null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BalloonTipPositioner setupPositioner(Orientation orientation, AttachLocation attachLocation, int horizontalOffset, int verticalOffset) {
/*     */     LeftAbovePositioner leftAbovePositioner;
/*     */     LeftBelowPositioner leftBelowPositioner;
/*     */     RightAbovePositioner rightAbovePositioner;
/*     */     RightBelowPositioner rightBelowPositioner;
/* 581 */     BasicBalloonTipPositioner positioner = null;
/* 582 */     float attachX = 0.0F;
/* 583 */     float attachY = 0.0F;
/* 584 */     boolean fixedAttachLocation = true;
/*     */     
/* 586 */     switch (attachLocation) {
/*     */       case LEFT_ABOVE:
/* 588 */         fixedAttachLocation = false;
/*     */         break;
/*     */       case LEFT_BELOW:
/* 591 */         attachX = 0.5F;
/* 592 */         attachY = 0.5F;
/*     */         break;
/*     */       case RIGHT_ABOVE:
/* 595 */         attachX = 0.5F;
/*     */         break;
/*     */       case RIGHT_BELOW:
/* 598 */         attachX = 1.0F;
/*     */         break;
/*     */       case null:
/* 601 */         attachX = 1.0F;
/* 602 */         attachY = 0.5F;
/*     */         break;
/*     */       case null:
/* 605 */         attachX = 1.0F;
/* 606 */         attachY = 1.0F;
/*     */         break;
/*     */       case null:
/* 609 */         attachX = 0.5F;
/* 610 */         attachY = 1.0F;
/*     */         break;
/*     */       case null:
/* 613 */         attachY = 1.0F;
/*     */         break;
/*     */       case null:
/* 616 */         attachY = 0.5F;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 622 */     switch (orientation) {
/*     */       case LEFT_ABOVE:
/* 624 */         leftAbovePositioner = new LeftAbovePositioner(horizontalOffset, verticalOffset);
/*     */         break;
/*     */       case LEFT_BELOW:
/* 627 */         leftBelowPositioner = new LeftBelowPositioner(horizontalOffset, verticalOffset);
/*     */         break;
/*     */       case RIGHT_ABOVE:
/* 630 */         rightAbovePositioner = new RightAbovePositioner(horizontalOffset, verticalOffset);
/*     */         break;
/*     */       case RIGHT_BELOW:
/* 633 */         rightBelowPositioner = new RightBelowPositioner(horizontalOffset, verticalOffset);
/*     */         break;
/*     */     } 
/*     */     
/* 637 */     rightBelowPositioner.enableFixedAttachLocation(fixedAttachLocation);
/* 638 */     rightBelowPositioner.setAttachLocation(attachX, attachY);
/*     */     
/* 640 */     return (BalloonTipPositioner)rightBelowPositioner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setup(JComponent attachedComponent, JComponent contents, BalloonTipStyle style, BalloonTipPositioner positioner, JButton closeButton) {
/* 647 */     this.attachedComponent = attachedComponent;
/* 648 */     this.contents = contents;
/* 649 */     this.style = style;
/* 650 */     this.positioner = positioner;
/*     */     
/* 652 */     positioner.setBalloonTip(this);
/* 653 */     setBorder((Border)this.style);
/* 654 */     setOpaque(false);
/* 655 */     setLayout(new GridBagLayout());
/* 656 */     setPadding(4);
/*     */     
/* 658 */     add(this.contents, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 17, 1, new Insets(0, 0, 0, 0), 0, 0));
/* 659 */     setCloseButton(closeButton, true);
/*     */ 
/*     */     
/* 662 */     this.clickListener = new MouseAdapter() {
/* 663 */         public void mouseClicked(MouseEvent e) { e.consume(); }
/*     */       };
/* 665 */     addMouseListener(this.clickListener);
/*     */ 
/*     */     
/* 668 */     if (attachedComponent.isDisplayable()) {
/* 669 */       setupHelper();
/*     */     }
/*     */     else {
/*     */       
/* 673 */       this.ancestorListener = new AncestorListener() { public void ancestorMoved(AncestorEvent e) {}
/*     */           public void ancestorAdded(AncestorEvent e) {
/* 675 */             BalloonTip.this.setupHelper();
/* 676 */             e.getComponent().removeAncestorListener(this);
/* 677 */             BalloonTip.this.ancestorListener = null;
/*     */           }
/*     */           
/*     */           public void ancestorRemoved(AncestorEvent e) {} }
/*     */         ;
/* 682 */       attachedComponent.addAncestorListener(this.ancestorListener);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupHelper() {
/* 691 */     if (this.topLevelContainer == null) {
/* 692 */       setTopLevelContainer(this.attachedComponent.getRootPane().getLayeredPane());
/*     */     }
/*     */ 
/*     */     
/* 696 */     this.attachedComponent.addComponentListener(this.componentListener);
/*     */     
/* 698 */     this.visibilityControl.setCriterionAndUpdate("attachedComponentShowing", Boolean.valueOf(isAttachedComponentShowing()));
/*     */ 
/*     */     
/* 701 */     Container current = this.attachedComponent.getParent();
/* 702 */     Container previous = this.attachedComponent;
/* 703 */     while (current != null) {
/* 704 */       if (current instanceof javax.swing.JTabbedPane || current.getLayout() instanceof java.awt.CardLayout) {
/*     */ 
/*     */ 
/*     */         
/* 708 */         if (this.tabbedPaneListener == null) {
/* 709 */           this.tabbedPaneListener = getTabbedPaneListener();
/*     */         }
/* 711 */         previous.addComponentListener(this.tabbedPaneListener);
/* 712 */       } else if (current instanceof JViewport) {
/* 713 */         if (this.viewportListener == null) {
/* 714 */           this.viewportListener = new NestedViewportListener();
/*     */         }
/* 716 */         this.viewportListener.viewports.add((JViewport)current);
/* 717 */         ((JViewport)current).addChangeListener(this.viewportListener);
/* 718 */       } else if (current instanceof BalloonTip) {
/*     */ 
/*     */         
/* 721 */         current.addComponentListener(this.componentListener);
/*     */         
/* 723 */         this.topLevelContainer.setLayer(this, JLayeredPane.getLayer(this) + 1);
/*     */         
/*     */         break;
/*     */       } 
/* 727 */       previous = current;
/* 728 */       current = current.getParent();
/*     */     } 
/*     */ 
/*     */     
/* 732 */     refreshLocation();
/*     */ 
/*     */     
/* 735 */     if (this.viewportListener != null) {
/* 736 */       this.viewportListener.stateChanged(new ChangeEvent(this));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tearDownHelper() {
/* 746 */     if (this.ancestorListener != null) {
/* 747 */       this.attachedComponent.removeAncestorListener(this.ancestorListener);
/* 748 */       this.ancestorListener = null;
/*     */     } 
/*     */     
/* 751 */     this.attachedComponent.removeComponentListener(this.componentListener);
/*     */ 
/*     */     
/* 754 */     if (this.tabbedPaneListener != null) {
/* 755 */       Container current = this.attachedComponent.getParent();
/* 756 */       Container previous = this.attachedComponent;
/* 757 */       while (current != null) {
/* 758 */         if (current instanceof javax.swing.JTabbedPane || current.getLayout() instanceof java.awt.CardLayout) {
/* 759 */           previous.removeComponentListener(this.tabbedPaneListener);
/* 760 */         } else if (current instanceof BalloonTip) {
/* 761 */           current.removeComponentListener(this.componentListener);
/*     */           break;
/*     */         } 
/* 764 */         previous = current;
/* 765 */         current = current.getParent();
/*     */       } 
/* 767 */       this.tabbedPaneListener = null;
/*     */     } 
/*     */     
/* 770 */     if (this.topLevelContainer != null) {
/* 771 */       this.topLevelContainer.remove(this);
/* 772 */       this.topLevelContainer.removeComponentListener(this.topLevelContainerListener);
/* 773 */       this.topLevelContainer = null;
/*     */     } 
/*     */     
/* 776 */     if (this.viewportListener != null) {
/* 777 */       for (JViewport viewport : this.viewportListener.viewports) {
/* 778 */         viewport.removeChangeListener(this.viewportListener);
/*     */       }
/* 780 */       this.viewportListener.viewports.clear();
/* 781 */       this.viewportListener = null;
/*     */     } 
/*     */ 
/*     */     
/* 785 */     this.visibilityControl.criteria.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ComponentAdapter getTabbedPaneListener() {
/* 793 */     return new ComponentAdapter() {
/*     */         public void componentShown(ComponentEvent e) {
/* 795 */           BalloonTip.this.visibilityControl.setCriterionAndUpdate("tabShowing", Boolean.valueOf(true));
/*     */ 
/*     */ 
/*     */           
/* 799 */           BalloonTip.this.visibilityControl.setCriterionAndUpdate("attachedComponentShowing", Boolean.valueOf(BalloonTip.this.isAttachedComponentShowing()));
/* 800 */           BalloonTip.this.refreshLocation();
/*     */         }
/*     */         public void componentHidden(ComponentEvent e) {
/* 803 */           BalloonTip.this.visibilityControl.setCriterionAndUpdate("tabShowing", Boolean.valueOf(false));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected BalloonTip() {}
/*     */   
/*     */   protected class NestedViewportListener
/*     */     implements ChangeListener
/*     */   {
/* 813 */     private Vector<JViewport> viewports = new Vector<JViewport>();
/*     */     
/*     */     public void stateChanged(ChangeEvent e) {
/* 816 */       BalloonTip.this.refreshLocation();
/* 817 */       Point tipLocation = BalloonTip.this.positioner.getTipLocation();
/*     */       
/* 819 */       boolean isWithinViewport = false;
/* 820 */       for (JViewport viewport : BalloonTip.this.viewportListener.viewports) {
/* 821 */         Rectangle view = new Rectangle(SwingUtilities.convertPoint(viewport, viewport.getLocation(), BalloonTip.this.getTopLevelContainer()), viewport.getSize());
/*     */         
/* 823 */         if (viewport.getParent() instanceof JScrollPane) {
/* 824 */           JScrollPane scrollPane = (JScrollPane)viewport.getParent();
/* 825 */           if (scrollPane.getColumnHeader() != null) {
/* 826 */             view.y -= scrollPane.getColumnHeader().getHeight();
/*     */           }
/* 828 */           if (scrollPane.getRowHeader() != null) {
/* 829 */             view.x -= scrollPane.getColumnHeader().getWidth();
/*     */           }
/*     */         } 
/*     */         
/* 833 */         if (tipLocation.y >= view.y - 1 && tipLocation.y <= view.y + view.height && tipLocation.x >= view.x && tipLocation.x <= view.x + view.width) {
/*     */ 
/*     */ 
/*     */           
/* 837 */           isWithinViewport = true; continue;
/*     */         } 
/* 839 */         isWithinViewport = false;
/*     */       } 
/*     */ 
/*     */       
/* 843 */       if (!this.viewports.isEmpty()) {
/* 844 */         BalloonTip.this.visibilityControl.setCriterionAndUpdate("withinViewport", Boolean.valueOf(isWithinViewport));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class VisibilityControl
/*     */   {
/* 853 */     private HashMap<String, Boolean> criteria = new HashMap<String, Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setCriterionAndUpdate(String criterion, Boolean value) {
/* 861 */       this.criteria.put(criterion, value);
/* 862 */       update();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void update() {
/* 871 */       Iterator<Boolean> i = this.criteria.values().iterator();
/* 872 */       while (i.hasNext()) {
/* 873 */         if (!((Boolean)i.next()).booleanValue()) {
/* 874 */           BalloonTip.this.forceSetVisible(false);
/*     */           return;
/*     */         } 
/*     */       } 
/* 878 */       BalloonTip.this.forceSetVisible(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/java/balloontip/BalloonTip.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */