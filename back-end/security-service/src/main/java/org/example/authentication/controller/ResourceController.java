package org.example.authentication.controller;

import org.example.authentication.commons.SystemResponse;
import org.example.authentication.model.Resource;
import org.example.authentication.service.ResourceService;
import org.example.authentication.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资源管理
 *
 * @author zhuxj
 * @since 2021/1/28
 */
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 获取当前用户可见菜单
     */
    @GetMapping(value = "current/user/resources")
    public SystemResponse<List<ResourceVO>> getCurrentUserResources() {

        List<Resource> resources = this.resourceService.findCurrentUserResources();

        List<ResourceVO> menus = this.resourceService.convertTree(resources);

        return SystemResponse.success(menus);
    }
}
