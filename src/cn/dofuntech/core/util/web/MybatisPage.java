package cn.dofuntech.core.util.web;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.Page;

/**
 * <p>
 * mybatis 分页封装
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author L.Xu(@2015年4月1日)
 * @version 1.0
 * filename:MybatisPage.java 
 */
public class MybatisPage<T> extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    private Page<T>           page;

    /**
     * @param page 分页类中可以塞入各种参数用于Mybatis的查询语句中的变量替换<p><i>eg.</i><br><p><code>MybatisPage page = new MybatisPage();<br>page.put("martSign","BEPS");<br></code></p>此Page模型类中可以放入各种各样的参数用于Mybatis中各类查询的变量传参</p>
     */

    /**
     * @param page
     */
    public MybatisPage() {
        super();
    }

    /**
     * @return the page
     */
    public Page<T> getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Page<T> page) {
        this.page = page;
    }

    public List<T> getRows() {
        return page.getResult();
    }

    /**
     * 获得每页的记录数量, 默认为-1.
     */
    public long getTotal() {
        return page.getTotal();
    }

}
