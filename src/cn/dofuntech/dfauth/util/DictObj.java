package cn.dofuntech.dfauth.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictObj
{
  private Map<String, String> attrMap = new HashMap();
  private List<DictObj> childList = new ArrayList();

  public void setAttrMap(String sKey, String sVal)
  {
    this.attrMap.put(sKey, sVal);
  }

  public String getAttrMap(String sKey)
  {
    return (String)this.attrMap.get(sKey);
  }

  public Map<String, String> getAttrMap()
  {
    return this.attrMap;
  }

  public List<DictObj> getChildList() {
    return this.childList;
  }

  public void setChildList(List<DictObj> childList) {
    this.childList = childList;
  }

  public void setChild(DictObj co)
  {
    this.childList.add(co);
  }

  public String toString()
  {
    return "属性：" + this.attrMap.toString() + " 子节点数量：" + this.childList.size();
  }
}