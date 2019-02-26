package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.model.NodeLayoutDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MaprClusterServiceMock {
    @Autowired
    private ClusterLayoutsService clusterLayoutsService;
    @Autowired
    private AwsInfoService awsInfoService;

    public List<ClusterConfigurationDTO> getMaprClusters() {
        return Arrays.asList(
                createEntry1(),
                createEntry2(),
                createEntry3(),
                createEntry4()
        );
    }

    private ClusterConfigurationDTO createEntry1() {
        ClusterConfigurationDTO dto = new ClusterConfigurationDTO();
        dto.setDeploymentStatus(DeploymentStatus.DEPLOYING);
        dto.setAwsAvZone("eu-west-1c");
        dto.setAwsInstanceType(awsInfoService.getAwsAllowedInstances().get(0));
        dto.setAwsRegion(awsInfoService.getAwsAllowedRegions().get(0));
        dto.setClusterName("poc.mapr.com");
        dto.setCustomerName("MapR PS");
        dto.setEnvPrefix("chufe-poc");
        dto.setPrivateDomain("ps.mapr.com");
        dto.setDefaultClusterLayout(clusterLayoutsService.getPredefindedClusterLayouts().get(5));
        dto.setNodesLayout(createNodeLayout());
        return dto;
    }

    private ClusterConfigurationDTO createEntry2() {
        ClusterConfigurationDTO dto = new ClusterConfigurationDTO();
        dto.setDeploymentStatus(DeploymentStatus.DEPLOYED);
        dto.setAwsAvZone("eu-west-1c");
        dto.setAwsInstanceType(awsInfoService.getAwsAllowedInstances().get(0));
        dto.setAwsRegion(awsInfoService.getAwsAllowedRegions().get(0));
        dto.setClusterName("poc2.mapr.com");
        dto.setCustomerName("MapR PS");
        dto.setEnvPrefix("chufe-poc2");
        dto.setPrivateDomain("ps.mapr.com");
        dto.setDefaultClusterLayout(clusterLayoutsService.getPredefindedClusterLayouts().get(5));
        dto.setNodesLayout(createNodeLayout());
        return dto;
    }

    private ClusterConfigurationDTO createEntry3() {
        ClusterConfigurationDTO dto = new ClusterConfigurationDTO();
        dto.setDeploymentStatus(DeploymentStatus.DESTROYING);
        dto.setAwsAvZone("eu-west-1c");
        dto.setAwsInstanceType(awsInfoService.getAwsAllowedInstances().get(0));
        dto.setAwsRegion(awsInfoService.getAwsAllowedRegions().get(0));
        dto.setClusterName("poc3.mapr.com");
        dto.setCustomerName("MapR PS");
        dto.setEnvPrefix("chufe-poc3");
        dto.setPrivateDomain("ps.mapr.com");
        dto.setDefaultClusterLayout(clusterLayoutsService.getPredefindedClusterLayouts().get(5));
        dto.setNodesLayout(createNodeLayout());
        return dto;
    }

    private ClusterConfigurationDTO createEntry4() {
        ClusterConfigurationDTO dto = new ClusterConfigurationDTO();
        dto.setDeploymentStatus(DeploymentStatus.DESTROYED);
        dto.setAwsAvZone("eu-west-1c");
        dto.setAwsInstanceType(awsInfoService.getAwsAllowedInstances().get(0));
        dto.setAwsRegion(awsInfoService.getAwsAllowedRegions().get(0));
        dto.setClusterName("poc4.mapr.com");
        dto.setCustomerName("MapR PS");
        dto.setEnvPrefix("chufe-poc4");
        dto.setPrivateDomain("ps.mapr.com");
        dto.setDefaultClusterLayout(clusterLayoutsService.getPredefindedClusterLayouts().get(5));
        dto.setNodesLayout(createNodeLayout());
        return dto;
    }

    private List<NodeLayoutDTO> createNodeLayout() {
        return clusterLayoutsService.createNodeLayoutList(clusterLayoutsService.getPredefindedClusterLayouts().get(5));
    }

    public void deployCluster(ClusterConfigurationDTO clusterConfiguration) {
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DEPLOYING);
        // TODO persist JSON to disk
        System.out.println(clusterConfiguration);
    }
}
