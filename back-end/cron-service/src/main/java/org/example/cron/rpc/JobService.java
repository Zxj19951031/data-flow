package org.example.cron.rpc;

import org.example.cron.commons.SystemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author zhuxj
 * @since 2021/1/12
 */
@FeignClient(name = "job-service")
public interface JobService {

    @PostMapping(value = "job/{id}/instance")
    public SystemResponse<Integer> createInstance(@PathVariable Integer id);
}
