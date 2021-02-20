/**
 * 
 */
package cn.dofuntech.core.page;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;

/**
 * 用于MyBatis的分页器，基于RowBounds <br>
 * PageBounds不提供独立的构造函数，必须包装Paginator使用<br>
 * 使用方法，根据传递过来的Paginator构造：<code>PageBounds.wrap(paginator)</code>
 * 
 * @author lxu
 * 
 */
public class PageBounds extends RowBounds implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3560082356307736535L;

    Paginator                 paginator;

    /**
     * 构造函数
     * 
     * @param paginator
     */
    private PageBounds(Paginator paginator) {
        setPaginator(paginator);
    }

    /**
     * 包装Paginator，形成PageBounds
     * 
     * @param paginator
     * @return
     */
    public static PageBounds wrap(Paginator paginator) {
        return new PageBounds(paginator);
    }

    /**
     * @return the paginator
     */
    public Paginator getPaginator() {
        return paginator;
    }

    /**
     * @param paginator the paginator to set
     */
    public void setPaginator(Paginator paginator) {
        if (paginator == null) {
            throw new IllegalArgumentException("'paginator' must be not null");
        }
        this.paginator = paginator;
    }

    /**
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        paginator.setTotalCount(totalCount);
    }

    /**
     * 获得当前页起始记录数（0为第一条记录）
     * 
     * @return
     */
    public int getOffset() {
        return paginator.getFirstResult();
    }

    /**
     * 获得每次查询的条目数
     * 
     * @return
     */
    public int getLimit() {
        return paginator.getPageSize();
    }

}
