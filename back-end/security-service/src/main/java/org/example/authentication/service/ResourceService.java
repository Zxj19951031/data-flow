package org.example.authentication.service;

import org.example.authentication.model.Resource;
import org.example.authentication.vo.ResourceVO;

import java.util.List;

/**
 * 资源服务
 *
 * @author zhuxj
 * @since 2021/1/28
 */
public interface ResourceService {
    /**
     * 返回当前用户见菜单
     *
     * @return 资源
     */
    List<Resource> findCurrentUserResources();

    /**
     * 转换为供前端使用的输结构
     *
     * @param resources 资源列表
     * @return 树结构的资源列表
     */
    List<ResourceVO> convertTree(List<Resource> resources);
}
