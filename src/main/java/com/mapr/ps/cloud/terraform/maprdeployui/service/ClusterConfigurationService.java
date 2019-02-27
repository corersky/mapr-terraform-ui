package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClusterConfigurationService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;

    public boolean isPrefixUsed(ClusterConfigurationDTO clusterConfiguration) {
        return new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json").exists();
    }

    public List<ClusterConfigurationDTO> getMaprClusters() {
        Collection<File> files = FileUtils.listFiles(new File(terraformProjectPath + "/clusterinfo/maprdeployui/"), new String[]{"json"}, false);
        return files.stream().map(this::getClusterConfigurationByFile).collect(Collectors.toList());
    }

    public ClusterConfigurationDTO getClusterConfigurationByEnvPrefix(String envPrefix) {
        File inputFile = new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + envPrefix + "-maprdeployui.json");
        return getClusterConfigurationByFile(inputFile);
    }

    private ClusterConfigurationDTO getClusterConfigurationByFile(File inputFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Sometimes the json file is just written and invalid. Retry, up to ten times
        for (int i = 0; i < 10 ; i++) {
            try {
                return objectMapper.readValue(inputFile, ClusterConfigurationDTO.class);
            } catch (IOException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {

                }
            }
        }
        throw new RuntimeException("Was not able to read JSON files.");
    }


    public synchronized void saveJson(ClusterConfigurationDTO clusterConfiguration) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(terraformProjectPath + "/clusterinfo/maprdeployui/" +  clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"), clusterConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
