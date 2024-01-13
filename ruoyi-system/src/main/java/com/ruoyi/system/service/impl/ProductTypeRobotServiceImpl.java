package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductTypeRobotMapper;
import com.ruoyi.system.domain.ProductTypeRobot;
import com.ruoyi.system.service.IProductTypeRobotService;

/**
 * 品类Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-08-09
 */
@Service
public class ProductTypeRobotServiceImpl implements IProductTypeRobotService 
{
    @Autowired
    private ProductTypeRobotMapper productTypeRobotMapper;

    /**
     * 查询品类
     * 
     * @param id 品类主键
     * @return 品类
     */
    @Override
    public ProductTypeRobot selectProductTypeRobotById(Long id)
    {
        return productTypeRobotMapper.selectProductTypeRobotById(id);
    }

    /**
     * 查询品类列表
     * 
     * @param productTypeRobot 品类
     * @return 品类
     */
    @Override
    public List<ProductTypeRobot> selectProductTypeRobotList(ProductTypeRobot productTypeRobot)
    {
        return productTypeRobotMapper.selectProductTypeRobotList(productTypeRobot);
    }

    /**
     * 新增品类
     * 
     * @param productTypeRobot 品类
     * @return 结果
     */
    @Override
    public int insertProductTypeRobot(ProductTypeRobot productTypeRobot)
    {
        productTypeRobot.setCreateTime(DateUtils.getNowDate());
        return productTypeRobotMapper.insertProductTypeRobot(productTypeRobot);
    }

    /**
     * 修改品类
     * 
     * @param productTypeRobot 品类
     * @return 结果
     */
    @Override
    public int updateProductTypeRobot(ProductTypeRobot productTypeRobot)
    {
        productTypeRobot.setUpdateTime(DateUtils.getNowDate());
        return productTypeRobotMapper.updateProductTypeRobot(productTypeRobot);
    }

    /**
     * 批量删除品类
     * 
     * @param ids 需要删除的品类主键
     * @return 结果
     */
    @Override
    public int deleteProductTypeRobotByIds(Long[] ids)
    {
        return productTypeRobotMapper.deleteProductTypeRobotByIds(ids);
    }

    /**
     * 删除品类信息
     * 
     * @param id 品类主键
     * @return 结果
     */
    @Override
    public int deleteProductTypeRobotById(Long id)
    {
        return productTypeRobotMapper.deleteProductTypeRobotById(id);
    }
}
