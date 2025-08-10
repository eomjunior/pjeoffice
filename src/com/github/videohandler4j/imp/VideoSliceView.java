/*     */ package com.github.videohandler4j.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IFileSlice;
/*     */ import com.github.videohandler4j.IVideoSliceView;
/*     */ import com.github.videohandler4j.gui.imp.VideoSlicePanel;
/*     */ import java.io.File;
/*     */ import java.util.function.Consumer;
/*     */ import javax.swing.JPanel;
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
/*     */ public class VideoSliceView
/*     */   extends VideoSliceWrapper
/*     */   implements IVideoSliceView
/*     */ {
/*     */   private final VideoSlicePanel panel;
/*     */   
/*     */   public VideoSliceView() {
/*  43 */     this(0L);
/*     */   }
/*     */   
/*     */   public VideoSliceView(long startTime) {
/*  47 */     this(startTime, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public VideoSliceView(long startTime, long endTime) {
/*  51 */     super(new DefaultVideoSlice(startTime, endTime));
/*  52 */     this.panel = new VideoSlicePanel(slice());
/*     */   }
/*     */ 
/*     */   
/*     */   public IVideoSliceView setOnClosed(Consumer<IVideoSliceView> onClosed) {
/*  57 */     this.panel.setOnClosed(c -> onClosed.accept(this));
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IVideoSliceView setOnPlay(Consumer<IVideoSliceView> onPlay) {
/*  63 */     this.panel.setOnPlay(c -> onPlay.accept(this));
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IVideoSliceView setOnStop(Consumer<IVideoSliceView> onStop) {
/*  69 */     this.panel.setOnStop(c -> onStop.accept(this));
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IVideoSliceView setOnSave(Consumer<IVideoSliceView> onSave) {
/*  75 */     this.panel.setOnSave(c -> onSave.accept(this));
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IVideoSliceView setOnSelected(Consumer<IVideoSliceView> onSelect) {
/*  81 */     this.panel.setOnSelected(c -> onSelect.accept(this));
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public IVideoSliceView setOnDoSelect(Consumer<IVideoSliceView> onDoSelect) {
/*  87 */     this.panel.setOnDoSelect(c -> onDoSelect.accept(this));
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JPanel asPanel() {
/*  93 */     return (JPanel)this.panel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnd(long endTime) {
/*  98 */     setSlice((IFileSlice)this.panel.refresh(new DefaultVideoSlice(start(), endTime)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void splitAndSave(File input, File folder) {
/* 103 */     this.panel.splitAndSave(input, folder);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/imp/VideoSliceView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */