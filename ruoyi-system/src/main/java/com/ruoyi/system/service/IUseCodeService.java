package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.UseCode;

/**
 * 用码Service接口
 *
 * @author ruoyi
 * @date 2023-08-10
 */
public interface IUseCodeService
{
    /**
     * 查询用码
     *
     * @param id 用码主键
     * @return 用码
     */
    public UseCode selectUseCodeById(Long id);

    /**
     * 查询用码列表
     *
     * @param useCode 用码
     * @return 用码集合
     */
    public List<UseCode> selectUseCodeList(UseCode useCode);

    /**
     * 新增用码
     *
     * @param useCode 用码
     * @return 结果
     */
    public int insertUseCode(UseCode useCode);

    /**
     * 修改用码
     *
     * @param useCode 用码
     * @return 结果
     */
    public int updateUseCode(UseCode useCode);

    /**
     * 批量删除用码
     *
     * @param ids 需要删除的用码主键集合
     * @return 结果
     */
    public int deleteUseCodeByIds(Long[] ids);

    /**
     * 删除用码信息
     *
     * @param id 用码主键
     * @return 结果
     */
    public int deleteUseCodeById(Long id);

    Integer checkUser(UseCode useCode);
}
