package org.example.authentication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.mapper.ResourceMapper;
import org.example.authentication.model.Resource;
import org.example.authentication.service.ResourceService;
import org.example.authentication.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhuxj
 * @since 2021/1/28
 */
@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> findCurrentUserResources() {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();

        return this.resourceMapper.selectByUsername(username);
    }

    @Override
    public List<ResourceVO> convertTree(List<Resource> resources) {
        if (resources == null || resources.size() == 0) {
            throw new IllegalArgumentException("resource can not be null or empty");
        }

        List<ResourceVO> list = new ArrayList<>();
        ObjectMapper om = new ObjectMapper();
        for (Resource node : resources) {
            ResourceVO resourceVO = new ResourceVO();
            resourceVO.setId(node.getId());
            resourceVO.setName(node.getName());
            resourceVO.setPid(node.getPid());
            try {
                resourceVO.setMetadata(om.readValue(node.getMetadata(), HashMap.class));
            } catch (JsonProcessingException e) {
                log.error("resource metadata format error:{}", node.getMetadata());
                throw new IllegalArgumentException("resource metadata format error");
            }
            list.add(resourceVO);
        }

        List<ResourceVO> treeList = new ArrayList<>();
        for (ResourceVO node : list) {
            if (node.getPid().equals(0)) {
                treeList.add(node);
            }
            for (ResourceVO node2 : list) {
                if (node2.getPid().equals(node.getId())) {
                    if(node.getChildren()==null){
                        node.setChildren(new ArrayList<>());
                    }
                    node.getChildren().add(node2);
                }
            }
        }
        return treeList;
    }
}
