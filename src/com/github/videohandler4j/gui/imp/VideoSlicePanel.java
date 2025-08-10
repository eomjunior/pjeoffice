/*     */ package com.github.videohandler4j.gui.imp;
/*     */ 
/*     */ import com.github.filehandler4j.IInputFile;
/*     */ import com.github.utils4j.gui.imp.AbstractPanel;
/*     */ import com.github.utils4j.gui.imp.AlertDialog;
/*     */ import com.github.utils4j.gui.imp.ExceptionAlert;
/*     */ import com.github.utils4j.gui.imp.SwingTools;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import com.github.videohandler4j.IVideoFile;
/*     */ import com.github.videohandler4j.IVideoSlice;
/*     */ import com.github.videohandler4j.imp.BySliceVideoSplitter;
/*     */ import com.github.videohandler4j.imp.VideoDescriptor;
/*     */ import com.github.videohandler4j.imp.VideoTools;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.io.File;
/*     */ import java.util.function.Consumer;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ import net.miginfocom.swing.MigLayout;
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
/*     */ public class VideoSlicePanel
/*     */   extends AbstractPanel
/*     */ {
/*     */   private static final Consumer<JPanel> NOTHING = slice -> {
/*     */     
/*     */     };
/*  65 */   private final Icon playIcon = newIcon("play");
/*     */   
/*  67 */   private final Icon stopIcon = newIcon("stop");
/*     */   
/*  69 */   private final Icon saveIcon = newIcon("save");
/*     */   
/*  71 */   private final Icon closeIcon = newIcon("close");
/*     */   
/*  73 */   private final Icon cancelIcon = newIcon("cancel");
/*     */   
/*  75 */   private final JTextField txtFragName = new JTextField();
/*     */   
/*  77 */   private final JLabel startTime = new JLabel("--:--:--");
/*     */   
/*  79 */   private final JLabel endTime = new JLabel("--:--:--");
/*     */   
/*  81 */   private final JButton playPauseButton = (JButton)new AbstractPanel.StandardButton(this);
/*     */   
/*  83 */   private final JButton stopButton = (JButton)new AbstractPanel.StandardButton(this);
/*     */   
/*  85 */   private final JLabel lengthTime = new JLabel("00:00:00");
/*     */   
/*  87 */   private final JButton closeButton = (JButton)new AbstractPanel.StandardButton(this);
/*     */   
/*  89 */   private final JButton saveButton = (JButton)new AbstractPanel.StandardButton(this);
/*     */   
/*  91 */   private final JButton cancelButton = (JButton)new AbstractPanel.StandardButton(this);
/*     */   
/*  93 */   private final JProgressBar progress = new JProgressBar();
/*     */   
/*  95 */   private Consumer<JPanel> onClosed = NOTHING;
/*     */   
/*  97 */   private Consumer<JPanel> onPlay = NOTHING;
/*     */   
/*  99 */   private Consumer<JPanel> onStoped = NOTHING;
/*     */   
/* 101 */   private Consumer<JPanel> onSaved = NOTHING;
/*     */   
/* 103 */   private Consumer<JPanel> onSelected = NOTHING;
/*     */   
/* 105 */   private Consumer<JPanel> onDoSelect = NOTHING;
/*     */   private IVideoSlice slice;
/*     */   private volatile Thread async;
/*     */   
/*     */   public VideoSlicePanel(IVideoSlice slice) {
/* 110 */     super("/vh4j/icons/buttons/");
/*     */     
/* 112 */     this.playPauseButton.setIcon(this.playIcon);
/* 113 */     this.playPauseButton.addActionListener(e -> {
/*     */           this.onDoSelect.accept(this);
/*     */           
/*     */           this.onPlay.accept(this);
/*     */         });
/* 118 */     this.stopButton.setIcon(this.stopIcon);
/* 119 */     this.stopButton.addActionListener(e -> {
/*     */           this.onDoSelect.accept(this);
/*     */           
/*     */           this.onStoped.accept(this);
/*     */         });
/* 124 */     this.saveButton.setIcon(this.saveIcon);
/* 125 */     this.saveButton.addActionListener(e -> {
/*     */           this.onDoSelect.accept(this);
/*     */           
/*     */           this.onSaved.accept(this);
/*     */         });
/* 130 */     this.closeButton.setIcon(this.closeIcon);
/* 131 */     this.closeButton.addActionListener(e -> this.onClosed.accept(this));
/*     */ 
/*     */ 
/*     */     
/* 135 */     this.cancelButton.setIcon(this.cancelIcon);
/* 136 */     this.cancelButton.setVisible(true);
/* 137 */     this.cancelButton.addActionListener(e -> {
/*     */           if (this.async != null) {
/*     */             this.async.interrupt();
/*     */           }
/*     */         });
/*     */     
/* 143 */     this.progress.setVisible(true);
/*     */     
/* 145 */     setLayout((LayoutManager)new MigLayout());
/* 146 */     add(this.txtFragName, "span, pushx, growx, wrap");
/* 147 */     add(this.startTime);
/* 148 */     add(this.endTime, "al right, wrap");
/* 149 */     add(this.playPauseButton, "split 4");
/* 150 */     add(this.stopButton);
/* 151 */     add(this.saveButton);
/* 152 */     add(this.lengthTime);
/* 153 */     add(this.closeButton, "al right, wrap");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     MouseListener s1 = new MouseAdapter() {
/*     */         public void mouseClicked(MouseEvent e) {
/* 160 */           VideoSlicePanel.this.onSelected.accept(VideoSlicePanel.this);
/*     */         }
/*     */       };
/*     */     
/* 164 */     MouseListener s2 = new MouseAdapter() {
/*     */         public void mouseClicked(MouseEvent e) {
/* 166 */           VideoSlicePanel.this.onSelected.accept(VideoSlicePanel.this);
/*     */         }
/*     */       };
/*     */     
/* 170 */     this.txtFragName.setMaximumSize(new Dimension(190, (this.txtFragName.getPreferredSize()).height));
/* 171 */     this.txtFragName.setSize(new Dimension(190, (this.txtFragName.getPreferredSize()).height));
/* 172 */     this.txtFragName.addMouseListener(s2);
/* 173 */     addMouseListener(s1);
/* 174 */     setBorder(new EtchedBorder(1, null, null));
/* 175 */     refresh(slice);
/*     */   }
/*     */   
/*     */   public final IVideoSlice refresh(IVideoSlice slice) {
/* 179 */     this.slice = slice;
/* 180 */     this.startTime.setText(slice.startString());
/* 181 */     this.endTime.setText(slice.endString());
/* 182 */     this.lengthTime.setText(slice.timeString());
/* 183 */     this.saveButton.setEnabled((slice.end() < Long.MAX_VALUE));
/* 184 */     return this.slice;
/*     */   }
/*     */   
/*     */   public final VideoSlicePanel setOnClosed(Consumer<JPanel> onClosed) {
/* 188 */     if (onClosed != null) {
/* 189 */       this.onClosed = onClosed;
/*     */     }
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   public final VideoSlicePanel setOnPlay(Consumer<JPanel> onPlay) {
/* 195 */     if (onPlay != null) {
/* 196 */       this.onPlay = onPlay;
/*     */     }
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   public final VideoSlicePanel setOnStop(Consumer<JPanel> onStop) {
/* 202 */     if (onStop != null) {
/* 203 */       this.onStoped = onStop;
/*     */     }
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public final VideoSlicePanel setOnSave(Consumer<JPanel> onSave) {
/* 209 */     if (onSave != null) {
/* 210 */       this.onSaved = onSave;
/*     */     }
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public final VideoSlicePanel setOnSelected(Consumer<JPanel> onSelect) {
/* 216 */     if (onSelect != null) {
/* 217 */       this.onSelected = onSelect;
/*     */     }
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public final VideoSlicePanel setOnDoSelect(Consumer<JPanel> onDoSelect) {
/* 223 */     if (onDoSelect != null) {
/* 224 */       this.onDoSelect = onDoSelect;
/*     */     }
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   private void showProgress(String text) {
/* 230 */     SwingTools.invokeLater(() -> {
/*     */           this.progress.setIndeterminate(true);
/*     */           this.progress.setStringPainted(true);
/*     */           this.progress.setString(text);
/*     */           this.progress.setVisible(true);
/*     */           this.cancelButton.setVisible(true);
/*     */           add(this.progress, "span, pushx, growx");
/*     */           updateUI();
/*     */         });
/*     */   }
/*     */   
/*     */   private void hideProgress() {
/* 242 */     SwingTools.invokeLater(() -> {
/*     */           this.progress.setVisible(false);
/*     */           this.cancelButton.setVisible(false);
/*     */           remove(this.progress);
/*     */           updateUI();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void splitAndSave(File inputFile, File outputFolder) {
/* 254 */     if (!this.saveButton.isEnabled() || inputFile == null || !inputFile.exists() || outputFolder == null || !outputFolder.exists())
/*     */       return; 
/* 256 */     String sliceString = "Corte: de " + this.slice.startString() + " ate " + this.slice.endString();
/* 257 */     this.async = Threads.startAsync(sliceString, () -> {
/*     */           try {
/*     */             showProgress("Processando divisão...");
/*     */ 
/*     */             
/*     */             IVideoFile file = VideoTools.FFMPEG.create(inputFile);
/*     */ 
/*     */             
/*     */             String namePrefix = Strings.trim(this.txtFragName.getText()).replaceAll("[\\\\/:*?\"<>|]", Strings.empty());
/*     */             
/*     */             if (!namePrefix.isEmpty()) {
/*     */               namePrefix = namePrefix + '_';
/*     */             }
/*     */             
/*     */             (new BySliceVideoSplitter(false, new IVideoSlice[] { this.slice })).apply((new VideoDescriptor.Builder(".mp4")).namePrefix(namePrefix).add((IInputFile)file).output(outputFolder.toPath()).build()).subscribe();
/*     */             
/*     */             AlertDialog.info("Sucesso: " + sliceString);
/* 274 */           } catch (Throwable e) {
/*     */             ExceptionAlert.show("Não foi possível dividir o vídeo", "Arquivo: " + inputFile.getAbsolutePath(), e);
/*     */           } finally {
/*     */             this.async = null;
/*     */             hideProgress();
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/gui/imp/VideoSlicePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */