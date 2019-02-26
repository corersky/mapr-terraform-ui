package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.mapr.ps.cloud.terraform.maprdeployui.model.MaprClusterDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MaprClusterService {
    public List<MaprClusterDTO> getMaprClusters() {
        return Arrays.asList(
                new MaprClusterDTO("poc.mapr.com", "ps.mapr.com", "MapR PS", "chufe-poc", "eu-west-1c", "r5d.2xlarge", 5),
                new MaprClusterDTO("poc2.mapr.com", "ps2.mapr.com", "MapR PS", "chufe-poc", "eu-west-1c", "r5d.2xlarge", 5)
                );
    }

}
