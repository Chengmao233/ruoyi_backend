package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ProductTypeRobotCode;

/**
 * 品类码Service接口
 *
 * @author ruoyi
 * @date 2023-08-09
 */
public interface IProductTypeRobotCodeService
{
    /**
     * 查询品类码
     *
     * @param id 品类码主键
     * @return 品类码
     */
    public ProductTypeRobotCode selectProductTypeRobotCodeById(Long id);

    /**
     * 查询品类码列表
     *
     * @param productTypeRobotCode 品类码
     * @return 品类码集合
     */
    public List<ProductTypeRobotCode> selectProductTypeRobotCodeList(ProductTypeRobotCode productTypeRobotCode);

    /**
     * 新增品类码
     *
     * @param productTypeRobotCode 品类码
     * @return 结果
     */
    public int insertProductTypeRobotCode(ProductTypeRobotCode productTypeRobotCode);

    /**
     * 修改品类码
     *
     * @param productTypeRobotCode 品类码
     * @return 结果
     */
    public int updateProductTypeRobotCode(ProductTypeRobotCode productTypeRobotCode);

    /**
     * 批量删除品类码
     *
     * @param ids 需要删除的品类码主键集合
     * @return 结果
     */
    public int deleteProductTypeRobotCodeByIds(Long[] ids);

    /**
     * 删除品类码信息
     *
     * @param id 品类码主键
     * @return 结果
     */
    public int deleteProductTypeRobotCodeById(Long id);

    int insertProductTypeRobotCodeBatch(List<ProductTypeRobotCode> list);
}
