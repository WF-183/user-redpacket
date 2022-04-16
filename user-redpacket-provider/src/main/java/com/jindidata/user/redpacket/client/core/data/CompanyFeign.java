package com.jindidata.user.redpacket.client.core.data;

import com.jindidata.core.data.provider.CompanyProvider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(value = "core-data", contextId = "CompanyFeign", fallback = CompanyFeign.DefaultFallback.class,
    path = CompanyProvider.PATH_PREFIX)
public interface CompanyFeign extends CompanyProvider {
    @Component
    class DefaultFallback extends CompanyProvider.DefaultFallback implements CompanyFeign {

    }
}
