package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.model.NodeLayoutDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaprClusterServiceMock2 {
    @Value("${maprdeployui.app_json_store_path}")
    private String jsonStoragePath;

    public List<ClusterConfigurationDTO> getMaprClusters() {
        Collection<File> files = FileUtils.listFiles(new File(jsonStoragePath), new String[]{"json"}, false);
        ObjectMapper objectMapper = new ObjectMapper();
        return files.stream().map(file -> {
            try {
                return objectMapper.readValue(file, ClusterConfigurationDTO.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

    }

    public boolean isPrefixUsed(ClusterConfigurationDTO clusterConfiguration) {
        return new File(jsonStoragePath + "/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json").exists();
    }

    public void deployCluster(ClusterConfigurationDTO clusterConfiguration) {
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DEPLOYING);
        clusterConfiguration.setDeployedAt(new Date());
        saveJson(clusterConfiguration);
    }

    public void redeployCluster(ClusterConfigurationDTO clusterConfiguration) {
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DEPLOYING);
        clusterConfiguration.setDeployedAt(new Date());
        clusterConfiguration.setDestroyedAt(null);
        saveJson(clusterConfiguration);
    }

    public void deleteCluster(ClusterConfigurationDTO clusterConfiguration) {
        try {
            FileUtils.forceDelete(new File(jsonStoragePath + "/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyCluster(ClusterConfigurationDTO clusterConfiguration) {
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DESTROYING);
        clusterConfiguration.setDestroyedAt(new Date());
        saveJson(clusterConfiguration);
    }

    private void saveJson(ClusterConfigurationDTO clusterConfiguration) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(jsonStoragePath + "/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"), clusterConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
