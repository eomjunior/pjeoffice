/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Pair;
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.swing.table.AbstractTableModel;
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
/*     */ class FileTableModel
/*     */   extends AbstractTableModel
/*     */ {
/*     */   private static final int COLUMN_NAME = 0;
/*     */   private static final int COLUMN_DATE = 1;
/*     */   private static final int COLUMN_SIZE = 2;
/*  46 */   private String[] columnNames = new String[] { "Nome", "Data", "Tamanho" };
/*     */   private List<File> entries;
/*     */   
/*     */   public FileTableModel(List<File> entries) {
/*  50 */     this.entries = (List<File>)Args.requireNonNull(entries, "entries is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/*  55 */     return this.columnNames.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/*  60 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnName(int columnIndex) {
/*  65 */     return this.columnNames[columnIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getColumnClass(int columnIndex) {
/*  70 */     if (columnIndex == 2)
/*  71 */       return Long.class; 
/*  72 */     if (columnIndex == 1)
/*  73 */       return Date.class; 
/*  74 */     return String.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getValueAt(int rowIndex, int columnIndex) {
/*  79 */     File file = this.entries.get(rowIndex);
/*  80 */     switch (columnIndex) {
/*     */       case 0:
/*  82 */         return file.getName();
/*     */       case 1:
/*  84 */         return new Date(file.lastModified());
/*     */       case 2:
/*  86 */         return new Long(file.length());
/*     */     } 
/*  88 */     throw new IllegalArgumentException("Invalid column index");
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  93 */     this.entries.clear();
/*  94 */     fireTableDataChanged();
/*     */   }
/*     */   
/*     */   public Pair<Integer, Integer> sortUp(int[] reordering) {
/*  98 */     int diff = 0;
/*  99 */     for (int i = 0; i < reordering.length; i++) {
/* 100 */       int index = reordering[i];
/* 101 */       if (index == 0) {
/*     */         break;
/*     */       }
/* 104 */       diff = 1;
/* 105 */       File f = this.entries.get(index - 1);
/* 106 */       this.entries.set(index - 1, this.entries.get(index));
/* 107 */       this.entries.set(index, f);
/*     */     } 
/* 109 */     int begin = reordering[0] - diff;
/* 110 */     int end = reordering[reordering.length - 1] - diff;
/* 111 */     Pair<Integer, Integer> r = Pair.of(Integer.valueOf(begin), Integer.valueOf(end));
/* 112 */     fireTableDataChanged();
/* 113 */     return r;
/*     */   }
/*     */   
/*     */   public Pair<Integer, Integer> sortDown(int[] reordering) {
/* 117 */     int diff = 0;
/* 118 */     for (int i = reordering.length - 1; i >= 0; i--) {
/* 119 */       int index = reordering[i];
/* 120 */       if (index == this.entries.size() - 1) {
/*     */         break;
/*     */       }
/* 123 */       diff = 1;
/* 124 */       File f = this.entries.get(index + 1);
/* 125 */       this.entries.set(index + 1, this.entries.get(index));
/* 126 */       this.entries.set(index, f);
/*     */     } 
/* 128 */     int begin = reordering[0] + diff;
/* 129 */     int end = reordering[reordering.length - 1] + diff;
/* 130 */     Pair<Integer, Integer> r = Pair.of(Integer.valueOf(begin), Integer.valueOf(end));
/* 131 */     fireTableDataChanged();
/* 132 */     return r;
/*     */   }
/*     */   
/*     */   public void delete(int[] rows) {
/* 136 */     ((List)Arrays.stream(rows).mapToObj(i -> (File)this.entries.get(i)).collect(Collectors.toList())).forEach(this.entries::remove);
/* 137 */     fireTableDataChanged();
/*     */   }
/*     */   
/*     */   public void add(File file) {
/* 141 */     this.entries.add(file);
/* 142 */     fireTableDataChanged();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/FileTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */