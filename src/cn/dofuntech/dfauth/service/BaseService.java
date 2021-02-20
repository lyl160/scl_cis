package cn.dofuntech.dfauth.service;

import java.util.List;

public abstract interface BaseService<T, E extends Exception>
{
  public abstract List<T> getListPage(T paramT)
    throws Exception;

  public abstract Integer getCount(T paramT)
    throws Exception;

  public abstract T getEntity(T paramT)
    throws Exception;

  public abstract int addEntity(T paramT)
    throws Exception;

  public abstract int modifyEntity(T paramT)
    throws Exception;

  public abstract int removeEntity(T paramT)
    throws Exception;
}