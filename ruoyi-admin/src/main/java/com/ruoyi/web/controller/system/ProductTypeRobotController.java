package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ProductTypeRobotCode;
import com.ruoyi.system.service.IProductTypeRobotCodeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
import com.ruoyi.system.domain.ProductTypeRobot;
import com.ruoyi.system.service.IProductTypeRobotService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 品类Controller
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@RestController
@RequestMapping("/system/robot")
public class ProductTypeRobotController extends BaseController
{
    @Autowired
    private IProductTypeRobotService productTypeRobotService;

    @Autowired
    private IProductTypeRobotCodeService productTypeRobotCodeService;

    /**
     * 查询品类列表
     */
    @PreAuthorize("@ss.hasPermi('system:robot:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductTypeRobot productTypeRobot)
    {
        startPage();
        List<ProductTypeRobot> list = productTypeRobotService.selectProductTypeRobotList(productTypeRobot);
        if(StringUtils.isNotEmpty(list)){
            for(ProductTypeRobot typeRobot:list){
                ProductTypeRobotCode productTypeRobotCode = new ProductTypeRobotCode();
                productTypeRobotCode.setTypeId(typeRobot.getId());
                List<ProductTypeRobotCode> productTypeRobotCodes = productTypeRobotCodeService.selectProductTypeRobotCodeList(productTypeRobotCode);
                if(StringUtils.isNotEmpty(productTypeRobotCodes)){
                    typeRobot.setHasNums(productTypeRobotCodes.size());
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 获取品类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:robot:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(productTypeRobotService.selectProductTypeRobotById(id));
    }

    /**
     * 新增品类
     */
    @PreAuthorize("@ss.hasPermi('system:robot:add')")
    @Log(title = "品类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductTypeRobot productTypeRobot)
    {
        return toAjax(productTypeRobotService.insertProductTypeRobot(productTypeRobot));
    }

    /**
     * 修改品类
     */
    @PreAuthorize("@ss.hasPermi('system:robot:edit')")
    @Log(title = "品类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductTypeRobot productTypeRobot)
    {
        return toAjax(productTypeRobotService.updateProductTypeRobot(productTypeRobot));
    }

    /**
     * 删除品类
     */
    @PreAuthorize("@ss.hasPermi('system:robot:remove')")
    @Log(title = "品类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        for(Long id:ids){
            ProductTypeRobotCode productTypeRobotCode = new ProductTypeRobotCode();
            productTypeRobotCode.setTypeId(id);
            List<ProductTypeRobotCode> productTypeRobotCodes = productTypeRobotCodeService.selectProductTypeRobotCodeList(productTypeRobotCode);
            if(StringUtils.isNotEmpty(productTypeRobotCodes)){
                return AjaxResult.error("存在码不允许删除");
            }
        }
        return toAjax(productTypeRobotService.deleteProductTypeRobotByIds(ids));
    }
}
