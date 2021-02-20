/**
 * 
 */
package cn.dofuntech.core.entity;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 拥有自增长主键的Entity基础类
 * <p>推荐使用</p>
 * 
 * @author lxu
 *
 */
public abstract class AutoIdEntity implements Serializable {

    private static final long serialVersionUID = 6640631810787435671L;

    /** Primary Key */
    protected Long            id;

    public AutoIdEntity() {
        super();
    }

    public AutoIdEntity(Long id) {
        super();
        this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AutoIdEntity other = (AutoIdEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    private static final AtomicLong idSequence = new AtomicLong();

    /**
     * 分配Id，仅用于单机测试阶段或数据库不支持主键自动生成
     * 正式环境建议采用数据库的主键生成机制
     * 
     * @return
     */
    public long assignId() {
        this.id = idSequence.incrementAndGet();
        return this.id;
    }
}
