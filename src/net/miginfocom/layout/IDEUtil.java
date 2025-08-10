package net.miginfocom.layout;

import java.util.HashMap;

public class IDEUtil {
  public static final UnitValue ZERO = UnitValue.ZERO;
  
  public static final UnitValue TOP = UnitValue.TOP;
  
  public static final UnitValue LEADING = UnitValue.LEADING;
  
  public static final UnitValue LEFT = UnitValue.LEFT;
  
  public static final UnitValue CENTER = UnitValue.CENTER;
  
  public static final UnitValue TRAILING = UnitValue.TRAILING;
  
  public static final UnitValue RIGHT = UnitValue.RIGHT;
  
  public static final UnitValue BOTTOM = UnitValue.BOTTOM;
  
  public static final UnitValue LABEL = UnitValue.LABEL;
  
  public static final UnitValue INF = UnitValue.INF;
  
  public static final UnitValue BASELINE_IDENTITY = UnitValue.BASELINE_IDENTITY;
  
  private static final String[] X_Y_STRINGS = new String[] { "x", "y", "x2", "y2" };
  
  public String getIDEUtilVersion() {
    return "1.0";
  }
  
  public static HashMap<Object, int[]> getGridPositions(Object paramObject) {
    return Grid.getGridPositions(paramObject);
  }
  
  public static int[][] getRowSizes(Object paramObject) {
    return Grid.getSizesAndIndexes(paramObject, true);
  }
  
  public static int[][] getColumnSizes(Object paramObject) {
    return Grid.getSizesAndIndexes(paramObject, false);
  }
  
  public static final String getConstraintString(AC paramAC, boolean paramBoolean1, boolean paramBoolean2) {
    StringBuffer stringBuffer = new StringBuffer(32);
    DimConstraint[] arrayOfDimConstraint = paramAC.getConstaints();
    BoundSize boundSize = paramBoolean2 ? PlatformDefaults.getGridGapX() : PlatformDefaults.getGridGapY();
    for (byte b = 0; b < arrayOfDimConstraint.length; b++) {
      DimConstraint dimConstraint = arrayOfDimConstraint[b];
      addRowDimConstraintString(dimConstraint, stringBuffer, paramBoolean1);
      if (b < arrayOfDimConstraint.length - 1) {
        BoundSize boundSize1 = dimConstraint.getGapAfter();
        if (boundSize1 == boundSize || boundSize1 == null)
          boundSize1 = arrayOfDimConstraint[b + 1].getGapBefore(); 
        if (boundSize1 != null) {
          String str = getBS(boundSize1);
          if (paramBoolean1) {
            stringBuffer.append(".gap(\"").append(str).append("\")");
          } else {
            stringBuffer.append(str);
          } 
        } else if (paramBoolean1) {
          stringBuffer.append(".gap()");
        } 
      } 
    } 
    return stringBuffer.toString();
  }
  
  private static final void addRowDimConstraintString(DimConstraint paramDimConstraint, StringBuffer paramStringBuffer, boolean paramBoolean) {
    int i = paramDimConstraint.getGrowPriority();
    int j = paramStringBuffer.length();
    BoundSize boundSize = paramDimConstraint.getSize();
    if (!boundSize.isUnset())
      if (paramBoolean) {
        paramStringBuffer.append(".size(\"").append(getBS(boundSize)).append("\")");
      } else {
        paramStringBuffer.append(',').append(getBS(boundSize));
      }  
    if (i != 100)
      if (paramBoolean) {
        paramStringBuffer.append(".growPrio(").append(i).append("\")");
      } else {
        paramStringBuffer.append(",growprio ").append(i);
      }  
    Float float_1 = paramDimConstraint.getGrow();
    if (float_1 != null) {
      String str = (float_1.floatValue() != 100.0F) ? floatToString(float_1.floatValue(), paramBoolean) : "";
      if (paramBoolean) {
        if (str.length() == 0) {
          paramStringBuffer.append(".grow()");
        } else {
          paramStringBuffer.append(".grow(\"").append(str).append("\")");
        } 
      } else {
        paramStringBuffer.append(",grow").append((str.length() > 0) ? (" " + str) : "");
      } 
    } 
    int k = paramDimConstraint.getShrinkPriority();
    if (k != 100)
      if (paramBoolean) {
        paramStringBuffer.append(".shrinkPrio(").append(k).append("\")");
      } else {
        paramStringBuffer.append(",shrinkprio ").append(k);
      }  
    Float float_2 = paramDimConstraint.getShrink();
    if (float_2 != null && float_2.intValue() != 100) {
      String str = floatToString(float_2.floatValue(), paramBoolean);
      if (paramBoolean) {
        paramStringBuffer.append(".shrink(\"").append(str).append("\")");
      } else {
        paramStringBuffer.append(",shrink ").append(str);
      } 
    } 
    String str1 = paramDimConstraint.getEndGroup();
    if (str1 != null)
      if (paramBoolean) {
        paramStringBuffer.append(".endGroup(\"").append(str1).append("\")");
      } else {
        paramStringBuffer.append(",endgroup ").append(str1);
      }  
    String str2 = paramDimConstraint.getSizeGroup();
    if (str2 != null)
      if (paramBoolean) {
        paramStringBuffer.append(".sizeGroup(\"").append(str2).append("\")");
      } else {
        paramStringBuffer.append(",sizegroup ").append(str2);
      }  
    UnitValue unitValue = paramDimConstraint.getAlign();
    if (unitValue != null)
      if (paramBoolean) {
        paramStringBuffer.append(".align(\"").append(getUV(unitValue)).append("\")");
      } else {
        String str3 = getUV(unitValue);
        String str4 = (str3.equals("top") || str3.equals("bottom") || str3.equals("left") || str3.equals("label") || str3.equals("leading") || str3.equals("center") || str3.equals("trailing") || str3.equals("right") || str3.equals("baseline")) ? "" : "align ";
        paramStringBuffer.append(',').append(str4).append(str3);
      }  
    if (paramDimConstraint.isNoGrid())
      if (paramBoolean) {
        paramStringBuffer.append(".noGrid()");
      } else {
        paramStringBuffer.append(",nogrid");
      }  
    if (paramDimConstraint.isFill())
      if (paramBoolean) {
        paramStringBuffer.append(".fill()");
      } else {
        paramStringBuffer.append(",fill");
      }  
    if (!paramBoolean)
      if (paramStringBuffer.length() > j) {
        paramStringBuffer.setCharAt(j, '[');
        paramStringBuffer.append(']');
      } else {
        paramStringBuffer.append("[]");
      }  
  }
  
  private static final void addComponentDimConstraintString(DimConstraint paramDimConstraint, StringBuffer paramStringBuffer, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    int i = paramDimConstraint.getGrowPriority();
    if (i != 100)
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".growPrioX(" : ".growPrioY(").append(i).append(')');
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",growpriox " : ",growprioy ").append(i);
      }  
    if (!paramBoolean3) {
      Float float_1 = paramDimConstraint.getGrow();
      if (float_1 != null) {
        String str = (float_1.floatValue() != 100.0F) ? floatToString(float_1.floatValue(), paramBoolean1) : "";
        if (paramBoolean1) {
          paramStringBuffer.append(paramBoolean2 ? ".growX(" : ".growY(").append(str).append(')');
        } else {
          paramStringBuffer.append(paramBoolean2 ? ",growx" : ",growy").append((str.length() > 0) ? (" " + str) : "");
        } 
      } 
    } 
    int j = paramDimConstraint.getShrinkPriority();
    if (j != 100)
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".shrinkPrioX(" : ".shrinkPrioY(").append(j).append(')');
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",shrinkpriox " : ",shrinkprioy ").append(j);
      }  
    Float float_ = paramDimConstraint.getShrink();
    if (float_ != null && float_.intValue() != 100) {
      String str = floatToString(float_.floatValue(), paramBoolean1);
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".shrinkX(" : ".shrinkY(").append(str).append(')');
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",shrinkx " : ",shrinky ").append(str);
      } 
    } 
    String str1 = paramDimConstraint.getEndGroup();
    if (str1 != null)
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".endGroupX(\"" : ".endGroupY(\"").append(str1).append("\")");
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",endgroupx " : ",endgroupy ").append(str1);
      }  
    String str2 = paramDimConstraint.getSizeGroup();
    if (str2 != null)
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".sizeGroupX(\"" : ".sizeGroupY(\"").append(str2).append("\")");
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",sizegroupx " : ",sizegroupy ").append(str2);
      }  
    appendBoundSize(paramDimConstraint.getSize(), paramStringBuffer, paramBoolean2, paramBoolean1);
    UnitValue unitValue = paramDimConstraint.getAlign();
    if (unitValue != null)
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".alignX(\"" : ".alignY(\"").append(getUV(unitValue)).append("\")");
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",alignx " : ",aligny ").append(getUV(unitValue));
      }  
    BoundSize boundSize1 = paramDimConstraint.getGapBefore();
    BoundSize boundSize2 = paramDimConstraint.getGapAfter();
    if (boundSize1 != null || boundSize2 != null)
      if (paramBoolean1) {
        paramStringBuffer.append(paramBoolean2 ? ".gapX(\"" : ".gapY(\"").append(getBS(boundSize1)).append("\", \"").append(getBS(boundSize2)).append("\")");
      } else {
        paramStringBuffer.append(paramBoolean2 ? ",gapx " : ",gapy ").append(getBS(boundSize1));
        if (boundSize2 != null)
          paramStringBuffer.append(' ').append(getBS(boundSize2)); 
      }  
  }
  
  private static void appendBoundSize(BoundSize paramBoundSize, StringBuffer paramStringBuffer, boolean paramBoolean1, boolean paramBoolean2) {
    if (!paramBoundSize.isUnset())
      if (paramBoundSize.getPreferred() == null) {
        if (paramBoundSize.getMin() == null) {
          if (paramBoolean2) {
            paramStringBuffer.append(paramBoolean1 ? ".maxWidth(\"" : ".maxHeight(\"").append(getUV(paramBoundSize.getMax())).append("\")");
          } else {
            paramStringBuffer.append(paramBoolean1 ? ",wmax " : ",hmax ").append(getUV(paramBoundSize.getMax()));
          } 
        } else if (paramBoundSize.getMax() == null) {
          if (paramBoolean2) {
            paramStringBuffer.append(paramBoolean1 ? ".minWidth(\"" : ".minHeight(\"").append(getUV(paramBoundSize.getMin())).append("\")");
          } else {
            paramStringBuffer.append(paramBoolean1 ? ",wmin " : ",hmin ").append(getUV(paramBoundSize.getMin()));
          } 
        } else if (paramBoolean2) {
          paramStringBuffer.append(paramBoolean1 ? ".width(\"" : ".height(\"").append(getUV(paramBoundSize.getMin())).append("::").append(getUV(paramBoundSize.getMax())).append("\")");
        } else {
          paramStringBuffer.append(paramBoolean1 ? ",width " : ",height ").append(getUV(paramBoundSize.getMin())).append("::").append(getUV(paramBoundSize.getMax()));
        } 
      } else if (paramBoolean2) {
        paramStringBuffer.append(paramBoolean1 ? ".width(\"" : ".height(\"").append(getBS(paramBoundSize)).append("\")");
      } else {
        paramStringBuffer.append(paramBoolean1 ? ",width " : ",height ").append(getBS(paramBoundSize));
      }  
  }
  
  public static final String getConstraintString(CC paramCC, boolean paramBoolean) {
    StringBuffer stringBuffer = new StringBuffer(16);
    if (paramCC.isNewline())
      stringBuffer.append(paramBoolean ? ".newline()" : ",newline"); 
    if (paramCC.isExternal())
      stringBuffer.append(paramBoolean ? ".external()" : ",external"); 
    Boolean bool = paramCC.getFlowX();
    if (bool != null)
      if (paramBoolean) {
        stringBuffer.append(bool.booleanValue() ? ".flowX()" : ".flowY()");
      } else {
        stringBuffer.append(bool.booleanValue() ? ",flowx" : ",flowy");
      }  
    UnitValue[] arrayOfUnitValue1 = paramCC.getPadding();
    if (arrayOfUnitValue1 != null) {
      stringBuffer.append(paramBoolean ? ".pad(\"" : ",pad ");
      for (byte b = 0; b < arrayOfUnitValue1.length; b++)
        stringBuffer.append(getUV(arrayOfUnitValue1[b])).append((b < arrayOfUnitValue1.length - 1) ? " " : ""); 
      if (paramBoolean)
        stringBuffer.append("\")"); 
    } 
    UnitValue[] arrayOfUnitValue2 = paramCC.getPos();
    if (arrayOfUnitValue2 != null)
      if (paramCC.isBoundsInGrid()) {
        for (byte b = 0; b < 4; b++) {
          if (arrayOfUnitValue2[b] != null)
            if (paramBoolean) {
              stringBuffer.append('.').append(X_Y_STRINGS[b]).append("(\"").append(getUV(arrayOfUnitValue2[b])).append("\")");
            } else {
              stringBuffer.append(',').append(X_Y_STRINGS[b]).append(getUV(arrayOfUnitValue2[b]));
            }  
        } 
      } else {
        stringBuffer.append(paramBoolean ? ".pos(\"" : ",pos ");
        byte b1 = (arrayOfUnitValue2[2] != null || arrayOfUnitValue2[3] != null) ? 4 : 2;
        for (byte b2 = 0; b2 < b1; b2++)
          stringBuffer.append(getUV(arrayOfUnitValue2[b2])).append((b2 < b1 - 1) ? " " : ""); 
        if (paramBoolean)
          stringBuffer.append("\")"); 
      }  
    String str1 = paramCC.getId();
    if (str1 != null)
      if (paramBoolean) {
        stringBuffer.append(".id(\"").append(str1).append("\")");
      } else {
        stringBuffer.append(",id ").append(str1);
      }  
    String str2 = paramCC.getTag();
    if (str2 != null)
      if (paramBoolean) {
        stringBuffer.append(".tag(\"").append(str2).append("\")");
      } else {
        stringBuffer.append(",tag ").append(str2);
      }  
    int i = paramCC.getHideMode();
    if (i >= 0)
      if (paramBoolean) {
        stringBuffer.append(".hideMode(").append(i).append(')');
      } else {
        stringBuffer.append(",hideMode ").append(i);
      }  
    int j = paramCC.getSkip();
    if (j > 0)
      if (paramBoolean) {
        stringBuffer.append(".skip(").append(j).append(')');
      } else {
        stringBuffer.append(",skip ").append(j);
      }  
    int k = paramCC.getSplit();
    if (k > 1) {
      String str = (k == 2097051) ? "" : String.valueOf(k);
      if (paramBoolean) {
        stringBuffer.append(".split(").append(str).append(')');
      } else {
        stringBuffer.append(",split ").append(str);
      } 
    } 
    int m = paramCC.getCellX();
    int n = paramCC.getCellY();
    int i1 = paramCC.getSpanX();
    int i2 = paramCC.getSpanY();
    if (m >= 0 && n >= 0) {
      if (paramBoolean) {
        stringBuffer.append(".cell(").append(m).append(", ").append(n);
        if (i1 > 1 || i2 > 1)
          stringBuffer.append(", ").append(i1).append(", ").append(i2); 
        stringBuffer.append(')');
      } else {
        stringBuffer.append(",cell ").append(m).append(' ').append(n);
        if (i1 > 1 || i2 > 1)
          stringBuffer.append(' ').append(i1).append(' ').append(i2); 
      } 
    } else if (i1 > 1 || i2 > 1) {
      if (i1 > 1 && i2 > 1) {
        stringBuffer.append(paramBoolean ? ".span(" : ",span ").append(i1).append(paramBoolean ? ", " : " ").append(i2);
      } else if (i1 > 1) {
        stringBuffer.append(paramBoolean ? ".spanX(" : ",spanx ").append((i1 == 2097051) ? "" : String.valueOf(i1));
      } else if (i2 > 1) {
        stringBuffer.append(paramBoolean ? ".spanY(" : ",spany ").append((i2 == 2097051) ? "" : String.valueOf(i2));
      } 
      if (paramBoolean)
        stringBuffer.append(')'); 
    } 
    Float float_1 = paramCC.getPushX();
    Float float_2 = paramCC.getPushY();
    if (float_1 != null || float_2 != null) {
      if (float_1 != null && float_2 != null) {
        stringBuffer.append(paramBoolean ? ".push(" : ",push ");
        if (float_1.floatValue() != 100.0D || float_2.floatValue() != 100.0D)
          stringBuffer.append(float_1).append(paramBoolean ? ", " : " ").append(float_2); 
      } else if (float_1 != null) {
        stringBuffer.append(paramBoolean ? ".pushX(" : ",pushx ").append((float_1.floatValue() == 100.0F) ? "" : String.valueOf(float_1));
      } else if (float_2 != null) {
        stringBuffer.append(paramBoolean ? ".pushY(" : ",pushy ").append((float_2.floatValue() == 100.0F) ? "" : String.valueOf(float_2));
      } 
      if (paramBoolean)
        stringBuffer.append(')'); 
    } 
    int i3 = paramCC.getDockSide();
    if (i3 >= 0) {
      String str = CC.DOCK_SIDES[i3];
      if (paramBoolean) {
        stringBuffer.append(".dock").append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).append("()");
      } else {
        stringBuffer.append(",").append(str);
      } 
    } 
    boolean bool1 = (paramCC.getHorizontal().getGrow() != null && paramCC.getHorizontal().getGrow().intValue() == 100 && paramCC.getVertical().getGrow() != null && paramCC.getVertical().getGrow().intValue() == 100) ? true : false;
    addComponentDimConstraintString(paramCC.getHorizontal(), stringBuffer, paramBoolean, true, bool1);
    addComponentDimConstraintString(paramCC.getVertical(), stringBuffer, paramBoolean, false, bool1);
    if (bool1)
      stringBuffer.append(paramBoolean ? ".grow()" : ",grow"); 
    if (paramCC.isWrap())
      stringBuffer.append(paramBoolean ? ".wrap()" : ",wrap"); 
    String str3 = stringBuffer.toString();
    return (str3.length() == 0 || str3.charAt(0) != ',') ? str3 : str3.substring(1);
  }
  
  public static final String getConstraintString(LC paramLC, boolean paramBoolean) {
    StringBuffer stringBuffer = new StringBuffer(16);
    if (!paramLC.isFlowX())
      stringBuffer.append(paramBoolean ? ".flowY()" : ",flowy"); 
    boolean bool1 = paramLC.isFillX();
    boolean bool2 = paramLC.isFillY();
    if (bool1 || bool2)
      if (bool1 == bool2) {
        stringBuffer.append(paramBoolean ? ".fill()" : ",fill");
      } else {
        stringBuffer.append(paramBoolean ? (bool1 ? ".fillX()" : ".fillY()") : (bool1 ? ",fillx" : ",filly"));
      }  
    Boolean bool = paramLC.getLeftToRight();
    if (bool != null)
      if (paramBoolean) {
        stringBuffer.append(".leftToRight(").append(bool).append(')');
      } else {
        stringBuffer.append(bool.booleanValue() ? ",ltr" : ",rtl");
      }  
    if (!paramLC.getPackWidth().isUnset() || !paramLC.getPackHeight().isUnset())
      if (paramBoolean) {
        String str1 = getBS(paramLC.getPackWidth());
        String str2 = getBS(paramLC.getPackHeight());
        stringBuffer.append(".pack(");
        if (str1.equals("pref") && str2.equals("pref")) {
          stringBuffer.append(')');
        } else {
          stringBuffer.append('"').append(str1).append("\", \"").append(str2).append("\")");
        } 
      } else {
        stringBuffer.append(",pack");
        String str1 = getBS(paramLC.getPackWidth()) + " " + getBS(paramLC.getPackHeight());
        if (!str1.equals("pref pref"))
          stringBuffer.append(' ').append(str1); 
      }  
    if (paramLC.getPackWidthAlign() != 0.5F || paramLC.getPackHeightAlign() != 1.0F)
      if (paramBoolean) {
        stringBuffer.append(".packAlign(").append(floatToString(paramLC.getPackWidthAlign(), paramBoolean)).append(", ").append(floatToString(paramLC.getPackHeightAlign(), paramBoolean)).append(')');
      } else {
        stringBuffer.append(",packalign ").append(floatToString(paramLC.getPackWidthAlign(), paramBoolean)).append(' ').append(floatToString(paramLC.getPackHeightAlign(), paramBoolean));
      }  
    if (!paramLC.isTopToBottom())
      stringBuffer.append(paramBoolean ? ".bottomToTop()" : ",btt"); 
    UnitValue[] arrayOfUnitValue = paramLC.getInsets();
    if (arrayOfUnitValue != null) {
      String str1 = LayoutUtil.getCCString(arrayOfUnitValue);
      if (str1 != null) {
        if (paramBoolean) {
          stringBuffer.append(".insets(\"").append(str1).append("\")");
        } else {
          stringBuffer.append(",insets ").append(str1);
        } 
      } else {
        stringBuffer.append(paramBoolean ? ".insets(\"" : ",insets ");
        for (byte b = 0; b < arrayOfUnitValue.length; b++)
          stringBuffer.append(getUV(arrayOfUnitValue[b])).append((b < arrayOfUnitValue.length - 1) ? " " : ""); 
        if (paramBoolean)
          stringBuffer.append("\")"); 
      } 
    } 
    if (paramLC.isNoGrid())
      stringBuffer.append(paramBoolean ? ".noGrid()" : ",nogrid"); 
    if (!paramLC.isVisualPadding())
      stringBuffer.append(paramBoolean ? ".noVisualPadding()" : ",novisualpadding"); 
    int i = paramLC.getHideMode();
    if (i > 0)
      if (paramBoolean) {
        stringBuffer.append(".hideMode(").append(i).append(')');
      } else {
        stringBuffer.append(",hideMode ").append(i);
      }  
    appendBoundSize(paramLC.getWidth(), stringBuffer, true, paramBoolean);
    appendBoundSize(paramLC.getHeight(), stringBuffer, false, paramBoolean);
    UnitValue unitValue1 = paramLC.getAlignX();
    UnitValue unitValue2 = paramLC.getAlignY();
    if (unitValue1 != null || unitValue2 != null) {
      if (unitValue1 != null && unitValue2 != null) {
        stringBuffer.append(paramBoolean ? ".align(\"" : ",align ").append(getUV(unitValue1)).append(' ').append(getUV(unitValue2));
      } else if (unitValue1 != null) {
        stringBuffer.append(paramBoolean ? ".alignX(\"" : ",alignx ").append(getUV(unitValue1));
      } else if (unitValue2 != null) {
        stringBuffer.append(paramBoolean ? ".alignY(\"" : ",aligny ").append(getUV(unitValue2));
      } 
      if (paramBoolean)
        stringBuffer.append("\")"); 
    } 
    BoundSize boundSize1 = paramLC.getGridGapX();
    BoundSize boundSize2 = paramLC.getGridGapY();
    if (boundSize1 != null || boundSize2 != null) {
      if (boundSize1 != null && boundSize2 != null) {
        stringBuffer.append(paramBoolean ? ".gridGap(\"" : ",gap ").append(getBS(boundSize1)).append(' ').append(getBS(boundSize2));
      } else if (boundSize1 != null) {
        stringBuffer.append(paramBoolean ? ".gridGapX(\"" : ",gapx ").append(getBS(boundSize1));
      } else if (boundSize2 != null) {
        stringBuffer.append(paramBoolean ? ".gridGapY(\"" : ",gapy ").append(getBS(boundSize2));
      } 
      if (paramBoolean)
        stringBuffer.append("\")"); 
    } 
    int j = paramLC.getWrapAfter();
    if (j != 2097051) {
      String str1 = (j > 0) ? String.valueOf(j) : "";
      if (paramBoolean) {
        stringBuffer.append(".wrap(").append(str1).append(')');
      } else {
        stringBuffer.append(",wrap ").append(str1);
      } 
    } 
    int k = paramLC.getDebugMillis();
    if (k > 0)
      if (paramBoolean) {
        stringBuffer.append(".debug(").append(k).append(')');
      } else {
        stringBuffer.append(",debug ").append(k);
      }  
    String str = stringBuffer.toString();
    return (str.length() == 0 || str.charAt(0) != ',') ? str : str.substring(1);
  }
  
  private static String getUV(UnitValue paramUnitValue) {
    return (paramUnitValue != null) ? paramUnitValue.getConstraintString() : "null";
  }
  
  private static String getBS(BoundSize paramBoundSize) {
    return (paramBoundSize != null) ? paramBoundSize.getConstraintString() : "null";
  }
  
  private static final String floatToString(float paramFloat, boolean paramBoolean) {
    String str = String.valueOf(paramFloat);
    return str.endsWith(".0") ? str.substring(0, str.length() - 2) : (str + (paramBoolean ? "f" : ""));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/net/miginfocom/layout/IDEUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */