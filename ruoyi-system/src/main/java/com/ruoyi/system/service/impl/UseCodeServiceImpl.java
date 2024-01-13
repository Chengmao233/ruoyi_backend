package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UseCodeMapper;
import com.ruoyi.system.domain.UseCode;
import com.ruoyi.system.service.IUseCodeService;

/**
 * 用码Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-10
 */
@Service
public class UseCodeServiceImpl implements IUseCodeService
{
    @Autowired
    private UseCodeMapper useCodeMapper;

    /**
     * 查询用码
     *
     * @param id 用码主键
     * @return 用码
     */
    @Override
    public UseCode selectUseCodeById(Long id)
    {
        return useCodeMapper.selectUseCodeById(id);
    }

    /**
     * 查询用码列表
     *
     * @param useCode 用码
     * @return 用码
     */
    @Override
    public List<UseCode> selectUseCodeList(UseCode useCode)
    {
        return useCodeMapper.selectUseCodeList(useCode);
    }

    /**
     * 新增用码
     *
     * @param useCode 用码
     * @return 结果
     */
    @Override
    public int insertUseCode(UseCode useCode)
    {
        useCode.setCreateTime(DateUtils.getNowDate());
        return useCodeMapper.insertUseCode(useCode);
    }

    /**
     * 修改用码
     *
     * @param useCode 用码
     * @return 结果
     */
    @Override
    public int updateUseCode(UseCode useCode)
    {
        useCode.setUpdateTime(DateUtils.getNowDate());
        return useCodeMapper.updateUseCode(useCode);
    }

    /**
     * 批量删除用码
     *
     * @param ids 需要删除的用码主键
     * @return 结果
     */
    @Override
    public int deleteUseCodeByIds(Long[] ids)
    {
        return useCodeMapper.deleteUseCodeByIds(ids);
    }

    /**
     * 删除用码信息
     *
     * @param id 用码主键
     * @return 结果
     */
    @Override
    public int deleteUseCodeById(Long id)
    {
        return useCodeMapper.deleteUseCodeById(id);
    }

    @Override
    public Integer checkUser(UseCode useCode) {
        UseCode use = useCodeMapper.selectUseCodeListByCode(useCode);
        if(use!=null){
            UseCode userInfo = useCodeMapper.selectUseCodeListByOpenidAndCode(useCode);
            if(userInfo!=null){
                return 1;
            }else{
                return 2;
            }
        }else{
            return 0;
        }
    }
}
