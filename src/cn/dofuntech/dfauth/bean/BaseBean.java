package cn.dofuntech.dfauth.bean;

import java.io.Serializable;
import java.util.Map;

import cn.dofuntech.dfauth.util.BeanUtils;

public class BaseBean
implements Serializable
{
private static final long serialVersionUID = 1L;
private Integer pageSize;
private Integer totalRows;
private Integer totalPages;
private Integer currentPages;
private Integer start;
private Integer end;
private String createUserId;
private String editUserId;
private String createDate;
private String editDate;
private String startTime;
private String endTime;
private Integer rows;
private Integer page;

public void setRows(Integer rows)
{
  setPageSize(rows);
}

public Integer getRows() {
  return this.pageSize;
}

public void setPage(Integer page) {
  setCurrentPages(page);
}

public Integer getPage() {
  return this.currentPages;
}

public String getCreateUserId()
{
  return this.createUserId == null ? "" : this.createUserId;
}

public void setCreateUserId(String createUserId)
{
  this.createUserId = createUserId;
}

public String getEditUserId()
{
  return this.editUserId == null ? "" : this.editUserId;
}

public void setEditUserId(String editUserId)
{
  this.editUserId = editUserId;
}

public String getCreateDate()
{
  return this.createDate;
}

public void setCreateDate(String createDate)
{
  this.createDate = createDate;
}

public String getEditDate()
{
  return this.editDate;
}

public void setEditDate(String editDate)
{
  this.editDate = editDate;
}

public Integer getPageSize()
{
  return this.pageSize = Integer.valueOf((this.pageSize == null) || (this.pageSize.intValue() <= 0) ? 10 : this.pageSize.intValue());
}

public void setPageSize(Integer pageSize)
{
  this.pageSize = pageSize;
}

public Integer getTotalRows()
{
  return this.totalRows;
}

public void setTotalRows(Integer totalRows)
{
  this.totalRows = totalRows;
}

public Integer getTotalPages()
{
  return this.totalPages;
}

public void setTotalPages(Integer totalPages)
{
  this.totalPages = totalPages;
}

public Integer getCurrentPages()
{
  return this.currentPages;
}

public void setCurrentPages(Integer currentPages)
{
  this.currentPages = currentPages;
}

public Integer getStart()
{
  if ((this.start == null) || (this.start.intValue() <= 0)) {
    this.currentPages = Integer.valueOf((this.currentPages == null) || (this.currentPages.intValue() <= 0) ? 1 : this.currentPages.intValue());
    this.pageSize = Integer.valueOf((this.pageSize == null) || (this.pageSize.intValue() <= 0) ? 0 : this.pageSize.intValue());
    this.start = Integer.valueOf((this.currentPages.intValue() - 1) * this.pageSize.intValue());
  }
  return this.start;
}

public void setStart(Integer start)
{
  this.start = start;
}

public Integer getEnd()
{
  if ((this.end == null) || (this.end.intValue() <= 0)) {
    this.currentPages = Integer.valueOf((this.currentPages == null) || (this.currentPages.intValue() <= 0) ? 1 : this.currentPages.intValue());
    this.pageSize = Integer.valueOf((this.pageSize == null) || (this.pageSize.intValue() <= 0) ? 0 : this.pageSize.intValue());
    this.end = Integer.valueOf(this.currentPages.intValue() * this.pageSize.intValue());
  }
  return this.end;
}

public void setEnd(Integer end)
{
  this.end = end;
}

public Map<String, Object> toMap()
  throws Exception
{
  return BeanUtils.converToMap(this);
}

public String getStartTime() {
  return this.startTime;
}

public void setStartTime(String startTime) {
  if ((startTime != null) && (startTime.length() <= 8))
    this.startTime = (startTime + "000000");
  else
    this.startTime = startTime;
}

public String getEndTime()
{
  return this.endTime;
}

public void setEndTime(String endTime) {
  if ((endTime != null) && (endTime.length() <= 8))
    this.endTime = (endTime + "235959");
  else
    this.endTime = endTime;
}
}