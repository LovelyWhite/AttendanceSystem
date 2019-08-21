package Pojo;


public class Plugins {

  private long pluginid;
  private String name;
  private String fileName;
  private String mainClass;
  private String version;
  private String isOpen;


  public long getPluginid() {
    return pluginid;
  }

  public void setPluginid(long pluginid) {
    this.pluginid = pluginid;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


  public String getMainClass() {
    return mainClass;
  }

  public void setMainClass(String mainClass) {
    this.mainClass = mainClass;
  }


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  public String getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(String isOpen) {
    this.isOpen = isOpen;
  }

}
