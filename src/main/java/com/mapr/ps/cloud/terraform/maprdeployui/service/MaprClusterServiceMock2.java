package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.AdditionalClusterInfoDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.model.NodeLayoutDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaprClusterServiceMock2 {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;

    @PostConstruct
    public void init() throws IOException {
        FileUtils.forceMkdir(new File(terraformProjectPath));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/"));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/openvpn/"));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/logs/"));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/states/"));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/maprdeployui/"));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/additionalinfo/"));
        FileUtils.forceMkdir(new File(terraformProjectPath + "/clusterinfo/terraformconfig/"));
    }


    public List<ClusterConfigurationDTO> getMaprClusters() {
        Collection<File> files = FileUtils.listFiles(new File(terraformProjectPath + "/clusterinfo/maprdeployui/"), new String[]{"json"}, false);
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
        return new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json").exists();
    }

    public void deployCluster(ClusterConfigurationDTO clusterConfiguration) {
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DEPLOYING);
        clusterConfiguration.setDeployedAt(new Date());
        saveJson(clusterConfiguration);
    }

    public void redeployCluster(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
        checkState(clusterConfiguration, DeploymentStatus.DESTROYED, DeploymentStatus.FAILED);
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DEPLOYING);
        clusterConfiguration.setDeployedAt(new Date());
        clusterConfiguration.setDestroyedAt(null);
        saveJson(clusterConfiguration);
    }

    public void deleteCluster(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
        checkState(clusterConfiguration, DeploymentStatus.DESTROYED);
        try {
            FileUtils.forceDelete(new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClusterConfigurationDTO getClusterConfigurationByEnvPrefix(String envPrefix) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + envPrefix + "-maprdeployui.json"), ClusterConfigurationDTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read cluster configuration file. ", e);
        }
    }

    public File getOpenvpnFile(String envPrefix) {
        AdditionalClusterInfoDTO additionalClusterInfo = getAdditionalClusterInfo(envPrefix);
        if(additionalClusterInfo.isDataAvailable()) {
            return new File(terraformProjectPath + "/clusterinfo/openvpn/" + additionalClusterInfo.getOpenvpnFile());
        }
        return null;
    }

    public AdditionalClusterInfoDTO getAdditionalClusterInfo(String envPrefix) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String additionalInfoPath = terraformProjectPath + "/clusterinfo/additionalinfo/" + envPrefix + "-env.json";
            AdditionalClusterInfoDTO additionalClusterInfoDTO = objectMapper.readValue(new File(additionalInfoPath), AdditionalClusterInfoDTO.class);
            additionalClusterInfoDTO.setDataAvailable(true);
            return additionalClusterInfoDTO;
        } catch (IOException e) {
            AdditionalClusterInfoDTO additionalClusterInfoDTO = new AdditionalClusterInfoDTO();
            additionalClusterInfoDTO.setDbPassword("Not yet available");
            additionalClusterInfoDTO.setDomainName("Not yet available");
            additionalClusterInfoDTO.setEnvPrefix("Not yet available");
            additionalClusterInfoDTO.setMaprPassword("Not yet available");
            additionalClusterInfoDTO.setMaprUser("Not yet available");
            additionalClusterInfoDTO.setMcsUrl("Not yet available");
            additionalClusterInfoDTO.setDataAvailable(false);
            return additionalClusterInfoDTO;
        }
    }

    public void destroyCluster(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
        checkState(clusterConfiguration, DeploymentStatus.DEPLOYED, DeploymentStatus.FAILED);
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.DESTROYING);
        clusterConfiguration.setDestroyedAt(new Date());
        saveJson(clusterConfiguration);
    }

    private void saveJson(ClusterConfigurationDTO clusterConfiguration) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(terraformProjectPath + "/clusterinfo/maprdeployui/" +  clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"), clusterConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkState(ClusterConfigurationDTO clusterConfiguration, DeploymentStatus ...expectedStates) throws InvalidClusterStateException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClusterConfigurationDTO clusterConfigurationDTO = objectMapper.readValue(new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"), ClusterConfigurationDTO.class);
            for (DeploymentStatus expectedState : expectedStates) {
                if(clusterConfigurationDTO.getDeploymentStatus() == expectedState) {
                    return;
                }
            }
            throw new InvalidClusterStateException("Action aborted. Cluster state was changed and is " + clusterConfigurationDTO.getDeploymentStatus());
        } catch (IOException e) {
            throw new InvalidClusterStateException("Cannot read cluster configuration file. " + e.getMessage());
        }
    }
}
