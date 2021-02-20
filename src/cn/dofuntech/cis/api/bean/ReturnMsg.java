package cn.dofuntech.cis.api.bean;

import java.util.HashMap;
import java.util.Map;

/**返回数据封装对象
 * @author luokai
 *
 */
public class ReturnMsg{
	
	  private String rspcod;
	  private String rspmsg;
	  private HashMap<String, Object> map = new HashMap<String, Object>();
	  private Object obj;
	
	  public ReturnMsg()
	  {
	  }
	
	  public ReturnMsg(Object obj)
	  {
	    this.obj = obj;
	  }
	
	  public ReturnMsg(String rspcod, String rspmsg)
	  {
	    this.rspcod = rspcod;
	    this.rspmsg = rspmsg;
	  }
	
	  public void put(String key, Object value)
	  {
	    this.map.put(key, value);
	  }
	
	  public Object getMap()
	  {
	    return this.map;
	  }
	
	  public void putAll(Map<String, Object> map) {
	    this.map.putAll(map);
	  }
	
	  public Object getObj()
	  {
	    return this.obj;
	  }
	
	  public void setObj(Object obj) {
	    this.obj = obj;
	  }
	
	  public String getRspcod() {
	    return this.rspcod == null ? "200" : this.rspcod;
	  }
	
	  public void setRspcod(String rspcod) {
	    this.rspcod = rspcod;
	  }
	
	  public String getRspmsg() {
	    return this.rspmsg == null ? "操作成功" : this.rspmsg;
	  }
	
	  public void setRspmsg(String rspmsg) {
	    this.rspmsg = rspmsg;
	  }
	
	  public ReturnMsg setFail()
	  {
	    setRspcod("201");
	    return this;
	  }
	
	  public ReturnMsg setFail(String msg)
	  {
	    setRspcod("201");
	    setRspmsg(msg);
	    return this;
	  }
	
	  public ReturnMsg setFail(String msg, String url)
	  {
	    setRspcod("201");
	    setRspmsg(msg);
	
	    return this;
	  }
	
	  public ReturnMsg setFail(String msg, Map<?, ?> data)
	  {
	    setRspcod("201");
	    setRspmsg(msg);
	
	    return this;
	  }
	
	  public ReturnMsg setFail(String msg, String url, Map<?, ?> data)
	  {
	    setRspcod("201");
	    setRspmsg(msg);
	
	    return this;
	  }
	
	  public ReturnMsg setSuccess()
	  {
	    setRspcod("200");
	    return this;
	  }
	
	  public ReturnMsg setSuccess(String msg)
	  {
	    setRspcod("200");
	    setRspmsg(msg);
	    return this;
	  }
	
	  public ReturnMsg setSuccess(String msg, String url)
	  {
	    setRspcod("200");
	    setRspmsg(msg);
	
	    return this;
	  }
	
	  public ReturnMsg setSuccess(String msg, Map<?, ?> data)
	  {
	    setRspcod("200");
	    setRspmsg(msg);
	
	    return this;
	  }
	
	  public ReturnMsg setSuccess(String msg, String url, Map<?, ?> data)
	  {
	    setRspcod("200");
	    setRspmsg(msg);
	
	    return this;
	  }
	
	  public void setMsg(String rspcod, String rspmsg)
	  {
	    this.rspcod = rspcod;
	    this.rspmsg = rspmsg;
	  }
}