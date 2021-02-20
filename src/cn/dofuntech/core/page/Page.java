/**
 * 
 */
package cn.dofuntech.core.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 分页信息
 * 
 * @author lxu
 *
 */
public class Page<T> implements Serializable, Iterable<T> {

    /**
     * 
     */
    private static final long serialVersionUID = 8384582373156455552L;

    private List<T>           result;

    private Paginator         paginator;

    /**
     * 构造函数
     * 
     * @param paginator
     */
    public Page(Paginator paginator) {
        setPaginator(paginator);
    }

    /**
     * 构造函数
     * 
     * @param result
     * @param paginator
     */
    public Page(List<T> result, Paginator paginator) {
        setResult(result);
        setPaginator(paginator);
    }

    /**
     * @return the result
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<T> result) {
        if (result == null) {
            throw new IllegalArgumentException("'result' must be not null");
        }
        this.result = result;
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
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
    public boolean isFirstPage() {
        return paginator.isFirstPage();
    }

    /**
     * 是否有前一页
     * 
     * @return
     */
    public boolean isPrePage() {
        return paginator.isPrePage();
    }

    /**
     * 是否有后一页
     * 
     * @return
     */
    public boolean isNextPage() {
        return paginator.isNextPage();
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLastPage() {
        return paginator.isLastPage();
    }

    /**
     * 总的数据条目数量，0表示没有数据
     *
     * @return 总数量
     */
    public int getTotalCount() {
        return (int) paginator.getTotalCount();
    }

    /**
     * 每一页显示的条目数
     *
     * @return 每一页显示的条目数
     */
    public int getPageSize() {
        return paginator.getPageSize();
    }

    /**
     * 当前页的页码
     *
     * @return 当前页的页码
     */
    public int getPageNumber() {
        return paginator.getPageNumber();
    }

    /**
     * 得到数据库的第一条记录号
     * @return
     */
    public int getFirstResult() {
        return paginator.getFirstResult();
    }

    /**
     * 得到用于多页跳转的页码
     * @return
     */
    public Integer[] getLinkPageNumbers() {
        return linkPageNumbers(7);
    }

    /**
     * 得到用于多页跳转的页码
     * 注意:不可以使用 getLinkPageNumbers(10)方法名称，因为在JSP中会与 getLinkPageNumbers()方法冲突，报exception
     * @return
     */
    public Integer[] linkPageNumbers(int count) {
        return paginator.slider(count);
    }

    /**
     * 遍历
     */
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return result == null ? (Iterator<T>) Collections.emptyList().iterator() : result.iterator();
    }
}
