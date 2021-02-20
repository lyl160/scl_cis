package cn.dofuntech.dfauth.util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dofuntech.dfauth.bean.Dict;

public class DictUtils
{
  private static Map<String, DictObj> dictMap = new HashMap();

  private static final Logger log = LoggerFactory.getLogger(DictUtils.class);

  public static Map<String, String> getDictCodeMap(String sDictCode)
  {
    Map resMap = new HashMap();

    Map dictMap = getDictMap();
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    DictObj dictObj = getDictObjByCode(dictObjRoot, sDictCode);

    List<DictObj> childList = dictObj.getChildList();

    for (DictObj tempDictObj : childList) {
      resMap.put(tempDictObj.getAttrMap("DICT_VALUE"), tempDictObj.getAttrMap("DICT_NAME"));
    }

    return resMap;
  }

  public static List<DictObj> getDictCodeList(String sDictCode)
  {
    Map dictMap = getDictMap();
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    DictObj dictObj = getDictObjByCode(dictObjRoot, sDictCode);
    List childList = dictObj.getChildList();
    return childList;
  }

  public static void setMap2Request(HttpServletRequest request, Map<String, String> map)
  {
    Set keySet = map.keySet();
    Iterator it = keySet.iterator();
    String sKey = null;
    while (it.hasNext()) {
      sKey = (String)it.next();
      request.setAttribute(sKey, map.get(sKey));
    }
  }

  public static String getOptionTextFromList(List<Map<String, String>> list)
  {
    String sRes = "";
    String sName = "";
    String sVal = "";
    String sSelected = "";
    for (Map map : list) {
      sName = (String)map.get("NAME");
      sVal = (String)map.get("ID");
      sSelected = (String)map.get("SELECTED");
      if ((sSelected != null) && (sSelected.equalsIgnoreCase("true")))
        sRes = sRes + "<option value=" + sVal + " selected='selected'>" + sName + "</option>";
      else {
        sRes = sRes + "<option value=" + sVal + ">" + sName + "</option>";
      }
    }
    return sRes;
  }

  public static String getJsonTextFromList(List<Map<String, String>> list)
  {
    String sRes = "";
    String sName = "";
    String sVal = "";
    String sSelected = "";
    for (Map map : list) {
      sName = (String)map.get("NAME");
      sVal = (String)map.get("ID");
      sSelected = (String)map.get("SELECTED");
      if ((sSelected != null) && (sSelected.equalsIgnoreCase("true")))
        sRes = sRes + "{\"id\"=\"" + sVal + "\",\"name\"=\"" + sName + "\",\"selected\"=true},";
      else {
        sRes = sRes + "{\"id\"=\"" + sVal + "\",\"name\"=\"" + sName + "\"},";
      }
    }
    if (!"".equals(sRes)) {
      sRes = "[" + sRes.substring(0, sRes.length() - 1) + "]";
    }
    return sRes;
  }

  public static String get(String sDictCode, String sChildDictVal)
  {
    String sRes = null;
    Map dictMap = getDictMap();
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    DictObj dictObj = getDictObjByCode(dictObjRoot, sDictCode);
    String sDictVal = null;

    if (dictObj == null) {
      return null;
    }

    for (DictObj dictObjTemp : dictObj.getChildList()) {
      sDictVal = dictObjTemp.getAttrMap("DICT_VALUE");
      if ((sChildDictVal != null) && (sChildDictVal.equals(sDictVal))) {
        sRes = dictObjTemp.getAttrMap("DICT_NAME");
        break;
      }
    }
    return sRes;
  }

  public static String get(String sDictCode)
  {
    log.debug("------------------");
    Map dictMap = getDictMap();
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    log.debug("root=" + dictObjRoot);
    DictObj dictObj = getDictObjByCode(dictObjRoot, sDictCode);
    log.debug("obj=" + dictObj);
    String value = "";

    if (dictObj == null) {
      return null;
    }

    DictObj dictObjTemp = (DictObj)dictObj.getChildList().get(0);
    if ((dictObj.getChildList() != null) && (dictObj.getChildList().size() > 0)) {
      value = dictObjTemp.getAttrMap("DICT_VALUE");
    }
    log.debug("value=" + value);
    return value;
  }

  public static int updateDictMap(List<Dict> dictList)
  {
    Map dictTempMap = new HashMap();
    DictObj dRootObj = new DictObj();
    dRootObj.setAttrMap("DICT_NAME", "ROOT");
    dRootObj.setAttrMap("DICT_CODE", "0");
    dRootObj.setAttrMap("DICT_LEVEL", "0");
    dRootObj.setAttrMap("DICT_ID", "0");

    dictTempMap.put("0", dRootObj);

    DictObj dChildObj = new DictObj();
    for (Dict dict : dictList) {
      dChildObj = new DictObj();
      dChildObj.setAttrMap("DICT_LEVEL", dict.getDictLevel());
      dChildObj.setAttrMap("PARENT_ID", dict.getParentId());
      dChildObj.setAttrMap("DICT_ID", String.valueOf(dict.getDictId()));
      dChildObj.setAttrMap("DICT_CODE", dict.getDictCode());
      dChildObj.setAttrMap("DICT_NAME", dict.getDictName());
      dChildObj.setAttrMap("DICT_VALUE", dict.getDictValue());
      dChildObj.setAttrMap("SEQ_NUM", dict.getSeqNum());

      addChild2Root(dRootObj, dChildObj);
    }

    dictMap = dictTempMap;

    return 0;
  }

  public static Map<String, String> getDictOptionMap(String sCodeValues)
  {
    return getDictOptionMap(sCodeValues, null, false);
  }

  public static Map<String, String> getDictOptionMap(String sCodeValues, String sDictSuff, boolean isAddSpace)
  {
    Map resMap = new HashMap();

    Map dictMap = getDictMap();
    Map viewNameMap = getViewCidMap(sCodeValues);
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    DictObj dictObjTemp = null;
    Iterator it = viewNameMap.keySet().iterator();
    String sDictCode = null;
    String sDictChildDefaultVal = null;
    String sOptionText = null;

    while (it.hasNext()) {
      sDictCode = (String)it.next();
      sDictChildDefaultVal = (String)viewNameMap.get(sDictCode);
      dictObjTemp = getDictObjByCode(dictObjRoot, sDictCode);

      if (dictObjTemp != null)
      {
        sOptionText = getOptionText(dictObjTemp, sDictChildDefaultVal, isAddSpace);
        if (!"".equals(sOptionText))
        {
          if ((sDictSuff != null) && (!"".equals(sDictSuff)))
            resMap.put("DICT_SELECT_" + sDictCode + "_" + sDictSuff, sOptionText);
          else
            resMap.put("DICT_SELECT_" + sDictCode, sOptionText);
        }
      }
    }
    return resMap;
  }

  public static Map<String, String> getDictOptionMapSpace(String sCodeValues)
  {
    return getDictOptionMap(sCodeValues, null, true);
  }

  public static Map<String, String> getDictOptionMapSpace(String sCodeValues, String sDictSuff)
  {
    return getDictOptionMap(sCodeValues, sDictSuff, true);
  }

  public static Map<String, String> getDictJsonMap(String sCodeValues, boolean isAddSpace)
  {
    return getDictJsonMap(sCodeValues, null, false);
  }

  public static Map<String, String> getDictJsonMap(String sCodeValues, String sDictSuff, boolean isAddSpace)
  {
    Map resMap = new HashMap();

    Map dictMap = getDictMap();
    Map viewNameMap = getViewCidMap(sCodeValues);
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    DictObj dictObjTemp = null;
    Iterator it = viewNameMap.keySet().iterator();
    String sDictCode = null;
    String sDictChildDefaultVal = null;
    String sOptionText = null;

    while (it.hasNext()) {
      sDictCode = (String)it.next();
      sDictChildDefaultVal = (String)viewNameMap.get(sDictCode);
      dictObjTemp = getDictObjByCode(dictObjRoot, sDictCode);

      if (dictObjTemp != null)
      {
        sOptionText = getJsonText(dictObjTemp, sDictChildDefaultVal, isAddSpace);
        if (!"".equals(sOptionText))
        {
          if ((sDictSuff != null) && (!"".equals(sDictSuff)))
            resMap.put("DICT_SELECT_" + sDictCode + "_" + sDictSuff, sOptionText);
          else
            resMap.put("DICT_SELECT_" + sDictCode, sOptionText);
        }
      }
    }
    return resMap;
  }

  public static Map<String, String> getDictJsonMapSpace(String sCodeValues)
  {
    return getDictJsonMap(sCodeValues, null, true);
  }

  public static Map<String, String> getDictJsonMapSpace(String sCodeValues, String sDictSuff)
  {
    return getDictJsonMap(sCodeValues, sDictSuff, true);
  }

  public static String getJsonText(DictObj dictObj, String sDefaultVal, boolean isAddSpace)
  {
    String sRes = "";
    String sDictName = null;
    String sDictVal = null;

    if (isAddSpace) {
      sRes = sRes + "{\"id\"=\"\",\"name\"=\"" + sDictName + "\"},";
    }

    for (DictObj childDictObj : dictObj.getChildList()) {
      sDictName = childDictObj.getAttrMap("DICT_NAME");
      sDictVal = childDictObj.getAttrMap("DICT_VALUE");

      if (sDictVal == null) {
        return sRes;
      }

      if (sDictVal.equals(sDefaultVal))
        sRes = sRes + "{\"id\"=\"" + sDictVal + "\",\"name\"=\"" + sDictName + "\",\"selected\"=true},";
      else {
        sRes = sRes + "{\"id\"=\"" + sDictVal + "\",\"name\"=\"" + sDictName + "\"},";
      }
    }
    if (!"".equals(sRes)) {
      sRes = "[" + sRes.substring(0, sRes.length() - 1) + "]";
    }
    return sRes;
  }

  public static String getOptionText(DictObj dictObj, String sDefaultVal, boolean isAddSpace)
  {
    String sRes = "";
    String sDictName = null;
    String sDictVal = null;

    if (isAddSpace) {
      sRes = sRes + "<option value=''>全部</option>";
    }

    for (DictObj childDictObj : dictObj.getChildList()) {
      sDictName = childDictObj.getAttrMap("DICT_NAME");
      sDictVal = childDictObj.getAttrMap("DICT_VALUE");

      if (sDictVal == null) {
        return sRes;
      }

      if (sDictVal.equals(sDefaultVal))
      {
        sRes = sRes + "<option value=" + sDictVal + " selected='selected'>" + sDictName + "</option>";
      }
      else
      {
        sRes = sRes + "<option value=" + sDictVal + ">" + sDictName + "</option>";
      }
    }
    return sRes;
  }

  public static Map<String, DictObj> getDictMap()
  {
    return dictMap;
  }

  public static DictObj getDictObjByCode(DictObj dictObj, String sDictCode)
  {
    if (dictObj == null) {
      return null;
    }

    DictObj dictObjRes = null;
    String sDictIdTemp = dictObj.getAttrMap("DICT_CODE");
    if (sDictCode.equals(sDictIdTemp)) {
      return dictObj;
    }
    for (DictObj dictObjTemp : dictObj.getChildList()) {
      dictObjRes = getDictObjByCode(dictObjTemp, sDictCode);
      if (dictObjRes != null) {
        return dictObjRes;
      }
    }
    return dictObjRes;
  }

  public static Map<String, String> getViewCidMap(String sStr)
  {
    Map mapRes = new HashMap();
    String sCid = null;
    String sDefault = null;
    for (String sCidInfo : sStr.split(",")) {
      if (sCidInfo.indexOf(":") == -1) {
        sCid = sCidInfo;
        sDefault = null;
      } else {
        sCid = sCidInfo.split(":")[0].trim();
        sDefault = sCidInfo.split(":")[1].trim();
      }
      mapRes.put(sCid, sDefault);
    }
    return mapRes;
  }

  public static void addChild2Root(DictObj dRootObj, DictObj dChildObj)
  {
    DictObj dParentObj = findParent(dRootObj, dChildObj);
    if (dParentObj != null)
    {
      dParentObj.setChild(dChildObj);
    }
  }

  public static DictObj findParent(DictObj dRootObj, DictObj dChildObj)
  {
    DictObj dParentObj = null;

    String sParentId = dChildObj.getAttrMap("PARENT_ID");
    String sRootId = dRootObj.getAttrMap("DICT_ID");
    if ((sParentId != null) && (sParentId.equals(sRootId))) {
      return dRootObj;
    }
    for (DictObj dTempObj : dRootObj.getChildList()) {
      dParentObj = findParent(dTempObj, dChildObj);
      if (dParentObj != null)
      {
        break;
      }

    }

    return dParentObj;
  }

  public static String getDictNameChildVal(String sDictCode, String sChildDictVal)
  {
    String sRes = null;
    Map dictMap = getDictMap();
    DictObj dictObjRoot = (DictObj)dictMap.get("0");
    DictObj dictObj = getDictObjByCode(dictObjRoot, sDictCode);
    String sDictVal = null;

    if (dictObj == null) {
      return null;
    }

    for (DictObj dictObjTemp : dictObj.getChildList()) {
      sDictVal = dictObjTemp.getAttrMap("DICT_VALUE");
      if ((sChildDictVal != null) && (sChildDictVal.equals(sDictVal))) {
        sRes = dictObjTemp.getAttrMap("DICT_NAME");
        break;
      }
    }
    return sRes;
  }
}