package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 品类对象 product_type_robot
 *
 * @author ruoyi
 * @date 2023-08-09
 */
public class ProductTypeRobot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 品类名称 */
    @Excel(name = "品类名称")
    private String name;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 溯源码数量 */
    @Excel(name = "溯源码数量")
    private Integer nums;

    /** 已经生成的码 */
    @Excel(name = "已经生成的码")
    private Integer hasNums;

    /** 机器人id */
    @Excel(name = "机器人id")
    private String robotId;

    private String saying;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaying() {
        return saying;
    }

    public void setSaying(String saying) {
        this.saying = saying;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Integer getHasNums() {
        return hasNums;
    }

    public void setHasNums(Integer hasNums) {
        this.hasNums = hasNums;
    }

    public void setRobotId(String robotId)
    {
        this.robotId = robotId;
    }

    public String getRobotId()
    {
        return robotId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("nums", getNums())
            .append("hasNums", getHasNums())
            .append("robotId", getRobotId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
