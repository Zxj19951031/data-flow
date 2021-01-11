package org.example.job.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.job.commons.SystemResponse;
import org.example.job.dto.DatasourceQueryDTO;
import org.example.job.mapper.JobMapper;
import org.example.job.model.Datasource;
import org.example.job.model.Job;
import org.example.job.service.DatasourceService;
import org.example.job.vo.DatasourceSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据源管理
 *
 * @author zhuxj
 * @since 2020/12/21
 */
@RestController
public class DatasourceController {

    @Autowired
    private DatasourceService datasourceService;

    /**
     * 新增数据源
     *
     * @param vo 数据源
     * @return 新增的数据源ID
     */
    @PostMapping(value = "datasource")
    public SystemResponse<Integer> save(@Valid @RequestBody DatasourceSaveVO vo) {
        Datasource datasource = vo.toDatasource();
        int id = this.datasourceService.save(datasource);
        return SystemResponse.success(id);
    }

    /**
     * 查询数据源
     *
     * @param id 要查询的数据源ID
     * @return 数据源
     */
    @GetMapping(value = "datasource/{id}")
    public SystemResponse<Datasource> get(@PathVariable Integer id) {
        Datasource datasource = this.datasourceService.findById(id);
        return SystemResponse.success(datasource);
    }

    /**
     * 删除数据源
     *
     * @param id 要删除的数据源ID
     * @return 被删除记录数
     */
    @DeleteMapping(value = "datasource/{id}")
    public SystemResponse<Integer> delete(@PathVariable Integer id) {
        int cnt = this.datasourceService.deleteById(id);
        return SystemResponse.success(cnt);
    }


    /**
     * 更新数据源
     *
     * @param id 要更新的数据源ID
     * @param vo 更新的内容
     * @return 受影响记录数
     */
    @PutMapping(value = "datasource/{id}")
    public SystemResponse<Integer> update(@PathVariable Integer id, @Valid @RequestBody DatasourceSaveVO vo) {
        Datasource datasource = vo.toDatasource();
        datasource.setId(id);
        int cnt = this.datasourceService.update(datasource);
        return SystemResponse.success(cnt);
    }

    /**
     * 查询数据源列表
     *
     * @param pageNum  页码
     * @param pageSize 单页大小
     * @param name     模糊搜索数据源名称
     * @param type     精确搜索数据源类型
     * @return 带有分页数据的记录
     */
    @GetMapping(value = "datasources")
    public SystemResponse<PageInfo<Datasource>> list(@RequestParam Integer pageNum,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) Integer type) {

        PageHelper.startPage(pageNum, pageSize, true, null, true);
        DatasourceQueryDTO params = new DatasourceQueryDTO(name, type);
        List<Datasource> datasourceList = this.datasourceService.listByParams(params);
        return SystemResponse.success(new PageInfo<>(datasourceList));
    }

    /**
     * 测试数据源连通性
     *
     * @param vo DatasourceSaveVO
     * @return true or false
     */
    @PostMapping(value = "datasource/connectivity")
    public SystemResponse<Boolean> testConnection(@Valid @RequestBody DatasourceSaveVO vo) {
        return SystemResponse.success(this.datasourceService.connectivity(vo.toDatasource()));
    }
}
