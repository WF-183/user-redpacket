package com.jindidata.user.redpacket.service.impl;

import com.jindidata.user.redpacket.service.DemoService;
import org.springframework.stereotype.Service;
import com.jindidata.user.redpacket.client.core.data.CompanyFeign;

@Service
public class DemoServiceImpl implements DemoService {

    private final CompanyFeign companyFeign;

    public DemoServiceImpl(CompanyFeign companyFeign) {
        this.companyFeign = companyFeign;
    }

    @Override
    public Long findRealCgid(Long cgid) {
        return companyFeign.getRealCgidByCgid(cgid);
    }
}
