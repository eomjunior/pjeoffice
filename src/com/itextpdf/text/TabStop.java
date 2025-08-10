/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.draw.DrawInterface;
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
/*     */ public class TabStop
/*     */ {
/*     */   protected float position;
/*     */   
/*     */   public static TabStop newInstance(float currentPosition, float tabInterval) {
/*  51 */     currentPosition = Math.round(currentPosition * 1000.0F) / 1000.0F;
/*  52 */     tabInterval = Math.round(tabInterval * 1000.0F) / 1000.0F;
/*     */     
/*  54 */     TabStop tabStop = new TabStop(currentPosition + tabInterval - currentPosition % tabInterval);
/*  55 */     return tabStop;
/*     */   }
/*     */   
/*     */   public enum Alignment {
/*  59 */     LEFT,
/*  60 */     RIGHT,
/*  61 */     CENTER,
/*  62 */     ANCHOR;
/*     */   }
/*     */ 
/*     */   
/*  66 */   protected Alignment alignment = Alignment.LEFT;
/*     */   protected DrawInterface leader;
/*  68 */   protected char anchorChar = '.';
/*     */   
/*     */   public TabStop(float position) {
/*  71 */     this(position, Alignment.LEFT);
/*     */   }
/*     */   
/*     */   public TabStop(float position, DrawInterface leader) {
/*  75 */     this(position, leader, Alignment.LEFT);
/*     */   }
/*     */   
/*     */   public TabStop(float position, Alignment alignment) {
/*  79 */     this(position, (DrawInterface)null, alignment);
/*     */   }
/*     */   
/*     */   public TabStop(float position, Alignment alignment, char anchorChar) {
/*  83 */     this(position, null, alignment, anchorChar);
/*     */   }
/*     */   
/*     */   public TabStop(float position, DrawInterface leader, Alignment alignment) {
/*  87 */     this(position, leader, alignment, '.');
/*     */   }
/*     */   
/*     */   public TabStop(float position, DrawInterface leader, Alignment alignment, char anchorChar) {
/*  91 */     this.position = position;
/*  92 */     this.leader = leader;
/*  93 */     this.alignment = alignment;
/*  94 */     this.anchorChar = anchorChar;
/*     */   }
/*     */   
/*     */   public TabStop(TabStop tabStop) {
/*  98 */     this(tabStop.getPosition(), tabStop.getLeader(), tabStop.getAlignment(), tabStop.getAnchorChar());
/*     */   }
/*     */   
/*     */   public float getPosition() {
/* 102 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setPosition(float position) {
/* 106 */     this.position = position;
/*     */   }
/*     */   
/*     */   public Alignment getAlignment() {
/* 110 */     return this.alignment;
/*     */   }
/*     */   
/*     */   public void setAlignment(Alignment alignment) {
/* 114 */     this.alignment = alignment;
/*     */   }
/*     */   
/*     */   public DrawInterface getLeader() {
/* 118 */     return this.leader;
/*     */   }
/*     */   
/*     */   public void setLeader(DrawInterface leader) {
/* 122 */     this.leader = leader;
/*     */   }
/*     */   
/*     */   public char getAnchorChar() {
/* 126 */     return this.anchorChar;
/*     */   }
/*     */   
/*     */   public void setAnchorChar(char anchorChar) {
/* 130 */     this.anchorChar = anchorChar;
/*     */   }
/*     */   
/*     */   public float getPosition(float tabPosition, float currentPosition, float anchorPosition) {
/* 134 */     float newPosition = this.position;
/* 135 */     float textWidth = currentPosition - tabPosition;
/* 136 */     switch (this.alignment) {
/*     */       case RIGHT:
/* 138 */         if (tabPosition + textWidth < this.position) {
/* 139 */           newPosition = this.position - textWidth; break;
/*     */         } 
/* 141 */         newPosition = tabPosition;
/*     */         break;
/*     */       
/*     */       case CENTER:
/* 145 */         if (tabPosition + textWidth / 2.0F < this.position) {
/* 146 */           newPosition = this.position - textWidth / 2.0F; break;
/*     */         } 
/* 148 */         newPosition = tabPosition;
/*     */         break;
/*     */       
/*     */       case ANCHOR:
/* 152 */         if (!Float.isNaN(anchorPosition)) {
/* 153 */           if (anchorPosition < this.position) {
/* 154 */             newPosition = this.position - anchorPosition - tabPosition; break;
/*     */           } 
/* 156 */           newPosition = tabPosition;
/*     */           break;
/*     */         } 
/* 159 */         if (tabPosition + textWidth < this.position) {
/* 160 */           newPosition = this.position - textWidth; break;
/*     */         } 
/* 162 */         newPosition = tabPosition;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 167 */     return newPosition;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/TabStop.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */