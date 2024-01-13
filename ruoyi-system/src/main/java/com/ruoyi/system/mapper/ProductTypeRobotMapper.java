package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.ProductTypeRobot;

/**
 * 品类Mapper接口
 * 
 * @author ruoyi
 * @date 2023-08-09
 */
public interface ProductTypeRobotMapper 
{
    /**
     * 查询品类
     * 
     * @param id 品类主键
     * @return 品类
     */
    public ProductTypeRobot selectProductTypeRobotById(Long id);

    /**
     * 查询品类列表
     * 
     * @param productTypeRobot 品类
     * @return 品类集合
     */
    public List<ProductTypeRobot> selectProductTypeRobotList(ProductTypeRobot productTypeRobot);

    /**
     * 新增品类
     * 
     * @param productTypeRobot 品类
     * @return 结果
     */
    public int insertProductTypeRobot(ProductTypeRobot productTypeRobot);

    /**
     * 修改品类
     * 
     * @param productTypeRobot 品类
     * @return 结果
     */
    public int updateProductTypeRobot(ProductTypeRobot productTypeRobot);

    /**
     * 删除品类
     * 
     * @param id 品类主键
     * @return 结果
     */
    public int deleteProductTypeRobotById(Long id);

    /**
     * 批量删除品类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductTypeRobotByIds(Long[] ids);
}
