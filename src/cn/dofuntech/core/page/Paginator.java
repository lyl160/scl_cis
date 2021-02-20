/**
 * 
 */
package cn.dofuntech.core.page;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 分页器，根据pageNumber, pageSize, totalCount用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 * 
 * @author lxu
 *
 */
public class Paginator implements Serializable {

    /**
     * 
     */
    private static final long  serialVersionUID      = 8585754871607331224L;

    //-- 公共变量 --//
    public static final String ASC                   = "asc";
    public static final String DESC                  = "desc";

    /** 默认每页显示记录条数 */
    public static final int    DEFAULT_PAGE_SIZE     = 10;

    /** 默认当前页（起始页） */
    public static final int    DEFAULT_CURRENT_PAGE  = 1;

    /** 默认页码滑动数量 */
    private static final int   DEFAULT_SLIDERS_COUNT = 7;

    /** 当前页数 */
    private int                pageNumber;

    /** 每页记录数 */
    private int                pageSize              = DEFAULT_PAGE_SIZE;

    /** 排序字段, 无默认值. 多个排序字段时用','分隔. */
    protected String           orderBy               = null;

    /** 排序方向, 无默认值. */
    protected String           order                 = null;

    /** 总记录数 */
    private long               totalCount;

    /**
     * 构造函数
     */
    public Paginator() {
        super();
    }

    /**
     * 构造函数
     * 
     * @param pageNumber
     */
    public Paginator(int pageNumber) {
        super();
        this.pageNumber = pageNumber;
    }

    /**
     * 构造函数
     * 
     * @param pageNumber
     * @param pageSize
     */
    public Paginator(int pageNumber, int pageSize) {
        super();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * 构造函数
     * 
     * @param pageNumber
     * @param pageSize
     * @param totalCount
     */
    public Paginator(int pageNumber, int pageSize, long totalCount) {
        super();
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

    /**
     * 总页数
     * @return
     */
    public long getTotalPage() {
        if (pageSize == 0) {
            return 0;
        }
        return (totalCount / pageSize) + ((totalCount % pageSize == 0) ? 0 : 1);
    }

    /**
     * 获得当前起始记录数
     * 
     * @return
     */
    public int getFirstResult() {
        if (pageNumber <= 0) {
            pageNumber = DEFAULT_CURRENT_PAGE;
        }
        return (pageNumber - 1) * pageSize;
    }

    /**
     * 是否是首页（第一页）
     * 
     * @return
     */
    public boolean isFirstPage() {
        return this.pageNumber == DEFAULT_CURRENT_PAGE;
    }

    /**
     * 是否有前一页
     * 
     * @return
     */
    public boolean isPrePage() {
        return this.pageNumber > DEFAULT_CURRENT_PAGE;
    }

    /**
     * 是否有后一页
     * 
     * @return
     */
    public boolean isNextPage() {
        return getTotalPage() > 0 && pageNumber < getTotalPage();
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLastPage() {
        return pageNumber >= getTotalPage();
    }

    /**
     * 上一页的页数
     * 
     * @return
     */
    public int getPrePageNumber() {
        if (isPrePage()) {
            return pageNumber - 1;
        }
        else {
            return DEFAULT_CURRENT_PAGE;
        }
    }

    /**
     * 下一页的页数
     * 
     * @return
     */
    public int getNextPageNumber() {
        if (isNextPage()) {
            return pageNumber + 1;
        }
        else {
            return (int) getTotalPage();
        }
    }

    /**
     * 是否已设置排序字段,无默认值.
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(int pageNumber) {
        if (pageNumber <= 0) {
            this.pageNumber = DEFAULT_CURRENT_PAGE;
        }
        else {
            this.pageNumber = pageNumber;
        }
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * @return the totalCount
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 返回Pagination对象自身的setPageNumber函数, 可用于连续设置。
     *
     * @param pageNumber
     * @return
     */
    public Paginator pageNumber(int pageNumber) {
        setPageNumber(pageNumber);
        return this;
    }

    /**
     * 返回Pagination对象自身的setPageSize函数,可用于连续设置。
     *
     * @param pageSize
     * @return
     */
    public Paginator pageSize(int pageSize) {
        setPageSize(pageSize);
        return this;
    }

    /**
     * 返回Pagination对象自身的setOrderBy函数,可用于连续设置。
     *
     * @param orderBy
     * @return
     */
    public Paginator orderBy(String orderBy) {
        setOrderBy(orderBy);
        return this;
    }

    /**
     * 返回Pagination对象自身的setOrder函数,可用于连续设置。
     *
     * @param order
     * @return
     */
    public Paginator order(String order) {
        setOrder(order);
        return this;
    }

    /**
     * 页码滑动窗口，并将当前页尽可能地放在滑动窗口的中间部位。
     * @return
     */
    public Integer[] getSlider() {
        return slider(DEFAULT_SLIDERS_COUNT);
    }

    /**
     * 页码滑动窗口，并将当前页尽可能地放在滑动窗口的中间部位。
     * 注意:不可以使用 getSlider(1)方法名称，因为在JSP中会与 getSlider()方法冲突，报exception
     * @return
     */
    public Integer[] slider(int slidersCount) {
        return generateLinkPageNumbers(pageNumber, (int) getTotalPage(), slidersCount);
    }

    private static Integer[] generateLinkPageNumbers(int currentPageNumber, int lastPageNumber, int count) {
        int avg = count / 2;

        int startPageNumber = currentPageNumber - avg;
        if (startPageNumber <= 0) {
            startPageNumber = 1;
        }

        int endPageNumber = startPageNumber + count - 1;
        if (endPageNumber > lastPageNumber) {
            endPageNumber = lastPageNumber;
        }

        if (endPageNumber - startPageNumber < count) {
            startPageNumber = endPageNumber - count;
            if (startPageNumber <= 0) {
                startPageNumber = 1;
            }
        }

        java.util.List<Integer> result = new java.util.ArrayList<Integer>();
        for (int i = startPageNumber ; i <= endPageNumber ; i++) {
            result.add(new Integer(i));
        }
        return result.toArray(new Integer[result.size()]);
    }

    public String genPage(HttpServletRequest request){
    	String first = "?page="+DEFAULT_CURRENT_PAGE;
    	String pre = "?page="+getPrePageNumber();
    	String next = "?page="+getNextPageNumber();
    	String last = "?page="+getTotalPage();
    	StringBuffer sb = new StringBuffer();
    	//首页
    	sb.append("<a class='first' href='"+first+"'><span>首页</span></a>")
    		.append("<a class='prev' href='"+pre+"'><span>上一页</span></a>")
    		.append("<a ><span class='current'>"+pageNumber+"</span></a>")
    		.append("<a class='next' href='"+next+"'><span>下一页</span></a>")
    		.append("<a class='end' href='"+last+"'><span>最后一页</span></a>")
    		.append("<a><span>共--"+getTotalPage()+"--页</span></a>");
    	return sb.toString();
    }

}
