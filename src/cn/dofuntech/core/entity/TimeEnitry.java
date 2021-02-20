package cn.dofuntech.core.entity;

import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author lxu(@2015年11月17日)
 * @version 1.0
 * filename:TimeEnitry.java 
 */
public class TimeEnitry extends DefaultValue {

    /**
     * 
     */
    private static final long serialVersionUID = -1156719491357287465L;
    protected Timestamp       addTime;
    protected Timestamp       editTime;

    /**
     * @return the addTime
     */
    public Timestamp getAddTime() {
        return addTime;
    }

    /**
     * @param addTime the addTime to set
     */
    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    /**
     * @return the editTime
     */
    public Timestamp getEditTime() {
        return editTime;
    }

    /**
     * @param editTime the editTime to set
     */
    public void setEditTime(Timestamp editTime) {
        this.editTime = editTime;
    }

}
