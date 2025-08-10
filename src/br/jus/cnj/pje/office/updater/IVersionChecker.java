package br.jus.cnj.pje.office.updater;

import com.github.progress4j.IProgressView;

public interface IVersionChecker {
  IStatusChecking check(IProgressView paramIProgressView, boolean paramBoolean);
  
  IPackage getPackage(IProgressView paramIProgressView, IStatusChecking paramIStatusChecking) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/IVersionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */