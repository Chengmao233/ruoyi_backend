package com.ruoyi.web.controller.system;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.QrCodeUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.system.domain.ProductTypeRobot;
import com.ruoyi.system.service.IProductTypeRobotService;
import org.apache.commons.io.FileUtils;
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
import com.ruoyi.system.domain.ProductTypeRobotCode;
import com.ruoyi.system.service.IProductTypeRobotCodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 品类码Controller
 *
 * @author ruoyi
 * @date 2023-08-09
 */
@RestController
@RequestMapping("/system/code")
public class ProductTypeRobotCodeController extends BaseController
{
    @Autowired
    private IProductTypeRobotCodeService productTypeRobotCodeService;

    @Autowired
    private IProductTypeRobotService productTypeRobotService;


    /**
     * 查询品类码列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductTypeRobotCode productTypeRobotCode)
    {
        startPage();
        List<ProductTypeRobotCode> list = productTypeRobotCodeService.selectProductTypeRobotCodeList(productTypeRobotCode);
        return getDataTable(list);
    }

    /**
     * 导出品类码列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:export')")
    @Log(title = "品类码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductTypeRobotCode productTypeRobotCode)
    {
        List<ProductTypeRobotCode> list = productTypeRobotCodeService.selectProductTypeRobotCodeList(productTypeRobotCode);
        ExcelUtil<ProductTypeRobotCode> util = new ExcelUtil<ProductTypeRobotCode>(ProductTypeRobotCode.class);
        util.exportExcel(response, list, "品类码数据");
    }

    /**
     * 获取品类码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:code:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(productTypeRobotCodeService.selectProductTypeRobotCodeById(id));
    }

    /**
     * 新增品类码
     */
    @PreAuthorize("@ss.hasPermi('system:code:add')")
    @Log(title = "品类码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductTypeRobotCode productTypeRobotCode)
    {
        return toAjax(productTypeRobotCodeService.insertProductTypeRobotCode(productTypeRobotCode));
    }
    /**
     * 生成
     */
    @PreAuthorize("@ss.hasPermi('system:code:add')")
    @Log(title = "生成", businessType = BusinessType.INSERT)
    @PostMapping("/make")
    public AjaxResult make(@RequestBody ProductTypeRobotCode productTypeRobotCode) throws Exception {
        ProductTypeRobot typeRobot = productTypeRobotService.selectProductTypeRobotById(productTypeRobotCode.getTypeId());
        if(typeRobot.getNums().equals(0)){
            return AjaxResult.error("为0无法生成");
        }
        List<ProductTypeRobotCode> productTypeRobotCodes = productTypeRobotCodeService.selectProductTypeRobotCodeList(productTypeRobotCode);
        if(StringUtils.isNotEmpty(productTypeRobotCodes)){
            typeRobot.setHasNums(productTypeRobotCodes.size());
        }
        if(typeRobot.getNums().equals(typeRobot.getHasNums())){
            return AjaxResult.error("已经生成了");
        }
        int num = typeRobot.getNums()-typeRobot.getHasNums();
        // 上传文件路径
        String filePath = RuoYiConfig.getProfile();

        String context = "https://dianshang.upp100.com:8082?id=";
        // 上传并返回新文件名称
        List<ProductTypeRobotCode> list = new ArrayList<>();
        for(int i = 0;i<num;i++){
            ProductTypeRobotCode typeRobotCode = new ProductTypeRobotCode();
            typeRobotCode.setTypeId(productTypeRobotCode.getTypeId());
            String str = UUID.randomUUID().toString().replace("-","").toUpperCase();
            typeRobotCode.setCode(str);

//            typeRobotCode.setUrl(urlfix);
            typeRobotCode.setCreateBy(getUsername());
            typeRobotCode.setCreateTime(new Date());
//            list.add(typeRobotCode);
            productTypeRobotCodeService.insertProductTypeRobotCode(typeRobotCode);
            ProductTypeRobotCode type = productTypeRobotCodeService.selectProductTypeRobotCodeById(typeRobotCode.getId());
            String cide = type.getCode();
            String url = filePath+"/"+cide+".jpg";
            String urlfix = "/profile/"+cide+".jpg";
            QrCodeUtils.encode(context+typeRobotCode.getId(), "",url , true);
            type.setUrl(urlfix);
            productTypeRobotCodeService.updateProductTypeRobotCode(type);
        }
        return toAjax(1);
    }

    /**
     * 修改品类码
     */
    @PreAuthorize("@ss.hasPermi('system:code:edit')")
    @Log(title = "品类码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductTypeRobotCode productTypeRobotCode)
    {
        return toAjax(productTypeRobotCodeService.updateProductTypeRobotCode(productTypeRobotCode));
    }

    /**
     * 删除品类码
     */
    @PreAuthorize("@ss.hasPermi('system:code:remove')")
    @Log(title = "品类码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productTypeRobotCodeService.deleteProductTypeRobotCodeByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:code:export')")
    @Log(title = "材料批量资源下载", businessType = BusinessType.EXPORT)
    @PostMapping("/download")
    public void downloadFiles(HttpServletResponse response, ProductTypeRobotCode productTypeRobotCode) throws IOException {

        List<ProductTypeRobotCode> productTypeRobotCodes = productTypeRobotCodeService.selectProductTypeRobotCodeList(productTypeRobotCode);
        //
        String localPath = RuoYiConfig.getProfile();
        //文件地址
        List<String> pathList=new ArrayList<>();
        //文件真实名字
        List<String> names = new ArrayList<>();

        Map<String,Integer> map = new HashMap<>();
        if(StringUtils.isNotEmpty(productTypeRobotCodes)){
            for(ProductTypeRobotCode p:productTypeRobotCodes){
                String url = localPath + StrUtil.subAfter(p.getUrl(), Constants.RESOURCE_PREFIX,false);
                if(map.containsKey(p.getName())){
                    map.put(p.getName(),map.get(p.getName())+1);
                    String name = p.getName()+"("+map.get(p.getName())+")"+".jpg";
                    names.add(name);
                }else{
                    map.put(p.getName(),0);
                    String name = p.getName()+".jpg";
                    names.add(name);
                }
                pathList.add(url);
            }
        }
        response.reset();
        response.setHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode("码.zip", "UTF-8"));
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        List<File> files = new ArrayList<>();
        try(ZipOutputStream zipOutputStream=new ZipOutputStream(new BufferedOutputStream(response.getOutputStream())))
        {
            for(int m=0;m<pathList.size();m++)
            {
                File file = new File(pathList.get(m));
                String path = file.getParent()+"/";
                File fileTemp = new File(path+names.get(m));
                FileUtils.copyFile(file, fileTemp);
                System.out.println(fileTemp.getName());
//                file.renameTo(fileTemp);
                files.add(fileTemp);
                String fileName=fileTemp.getName();
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(fileTemp))){
                    byte[] bytes = new byte[1024];
                    int i=0;
                    while((i=bis.read(bytes))!=-1)
                    {
                        zipOutputStream.write(bytes,0,i);
                    }
                    zipOutputStream.closeEntry();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        for(File file:files){
            file.delete();
        }
    }


}
