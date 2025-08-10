package com.github.utils4j.gui;

import javax.swing.Icon;
import javax.swing.KeyStroke;

public interface IResourceAction {
  String name();
  
  Integer mnemonic();
  
  KeyStroke shortcut();
  
  String tooltip();
  
  Icon menuIcon();
  
  Icon buttonIcon();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/IResourceAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */