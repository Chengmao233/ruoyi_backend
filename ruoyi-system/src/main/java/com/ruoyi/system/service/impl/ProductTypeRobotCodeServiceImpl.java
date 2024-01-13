package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductTypeRobotCodeMapper;
import com.ruoyi.system.domain.ProductTypeRobotCode;
import com.ruoyi.system.service.IProductTypeRobotCodeService;

/**
 * 品类码Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@Service
public class ProductTypeRobotCodeServiceImpl implements IProductTypeRobotCodeService
{
    @Autowired
    private ProductTypeRobotCodeMapper productTypeRobotCodeMapper;

    /**
     * 查询品类码
     *
     * @param id 品类码主键
     * @return 品类码
     */
    @Override
    public ProductTypeRobotCode selectProductTypeRobotCodeById(Long id)
    {
        return productTypeRobotCodeMapper.selectProductTypeRobotCodeById(id);
    }

    /**
     * 查询品类码列表
     *
     * @param productTypeRobotCode 品类码
     * @return 品类码
     */
    @Override
    public List<ProductTypeRobotCode> selectProductTypeRobotCodeList(ProductTypeRobotCode productTypeRobotCode)
    {
        return productTypeRobotCodeMapper.selectProductTypeRobotCodeList(productTypeRobotCode);
    }

    /**
     * 新增品类码
     *
     * @param productTypeRobotCode 品类码
     * @return 结果
     */
    @Override
    public int insertProductTypeRobotCode(ProductTypeRobotCode productTypeRobotCode)
    {
        productTypeRobotCode.setCreateTime(DateUtils.getNowDate());
        return productTypeRobotCodeMapper.insertProductTypeRobotCode(productTypeRobotCode);
    }

    /**
     * 修改品类码
     *
     * @param productTypeRobotCode 品类码
     * @return 结果
     */
    @Override
    public int updateProductTypeRobotCode(ProductTypeRobotCode productTypeRobotCode)
    {
        productTypeRobotCode.setUpdateTime(DateUtils.getNowDate());
        return productTypeRobotCodeMapper.updateProductTypeRobotCode(productTypeRobotCode);
    }

    /**
     * 批量删除品类码
     *
     * @param ids 需要删除的品类码主键
     * @return 结果
     */
    @Override
    public int deleteProductTypeRobotCodeByIds(Long[] ids)
    {
        return productTypeRobotCodeMapper.deleteProductTypeRobotCodeByIds(ids);
    }

    /**
     * 删除品类码信息
     *
     * @param id 品类码主键
     * @return 结果
     */
    @Override
    public int deleteProductTypeRobotCodeById(Long id)
    {
        return productTypeRobotCodeMapper.deleteProductTypeRobotCodeById(id);
    }

    @Override
    public int insertProductTypeRobotCodeBatch(List<ProductTypeRobotCode> list) {
        return productTypeRobotCodeMapper.insertProductTypeRobotCodeBatch(list);
    }
}
