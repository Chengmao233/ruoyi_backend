package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.UseCode;
import com.ruoyi.system.service.IUseCodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用码Controller
 *
 * @author ruoyi
 * @date 2023-08-10
 */
@RestController
@RequestMapping("/system/useCode")
public class UseCodeController extends BaseController
{
    @Autowired
    private IUseCodeService useCodeService;

    /**
     * 查询用码列表
     */
    @PreAuthorize("@ss.hasPermi('system:useCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(UseCode useCode)
    {
        startPage();
        List<UseCode> list = useCodeService.selectUseCodeList(useCode);
        return getDataTable(list);
    }

    /**
     * 导出用码列表
     */
    @PreAuthorize("@ss.hasPermi('system:useCode:export')")
    @Log(title = "用码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UseCode useCode)
    {
        List<UseCode> list = useCodeService.selectUseCodeList(useCode);
        ExcelUtil<UseCode> util = new ExcelUtil<UseCode>(UseCode.class);
        util.exportExcel(response, list, "用码数据");
    }

    /**
     * 获取用码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:useCode:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(useCodeService.selectUseCodeById(id));
    }

    /**
     * 新增用码
     */
    @PreAuthorize("@ss.hasPermi('system:useCode:add')")
    @Log(title = "用码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UseCode useCode)
    {
        return toAjax(useCodeService.insertUseCode(useCode));
    }

    /**
     * 修改用码
     */
    @PreAuthorize("@ss.hasPermi('system:useCode:edit')")
    @Log(title = "用码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UseCode useCode)
    {
        return toAjax(useCodeService.updateUseCode(useCode));
    }

    /**
     * 删除用码
     */
    @PreAuthorize("@ss.hasPermi('system:useCode:remove')")
    @Log(title = "用码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(useCodeService.deleteUseCodeByIds(ids));
    }
}
