package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.AdditionalClusterInfoDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class MaprClusterService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;
    @Autowired
    private TerraformService terraformService;
    @Autowired
    private ClusterConfigurationService clusterConfigurationService;

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


    public void deployCluster(ClusterConfigurationDTO clusterConfiguration) {
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.WAIT_DEPLOY);
        clusterConfiguration.setDeployedAt(new Date());
        clusterConfigurationService.saveJson(clusterConfiguration);
        terraformService.deploy(clusterConfiguration);
    }

    public void redeployCluster(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
        checkState(clusterConfiguration, DeploymentStatus.DESTROYED, DeploymentStatus.FAILED);
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.WAIT_DEPLOY);
        clusterConfiguration.setDeployedAt(new Date());
        clusterConfiguration.setDestroyedAt(null);
        clusterConfigurationService.saveJson(clusterConfiguration);
        terraformService.deploy(clusterConfiguration);
    }

    public void deleteCluster(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
        checkState(clusterConfiguration, DeploymentStatus.DESTROYED);
        try {
            FileUtils.forceDelete(new File(terraformProjectPath + "/clusterinfo/maprdeployui/" + clusterConfiguration.getEnvPrefix() + "-maprdeployui.json"));
            terraformService.deleteTerraformData(clusterConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getOpenvpnFile(String envPrefix) {
        AdditionalClusterInfoDTO additionalClusterInfo = getAdditionalClusterInfo(envPrefix);
        if (additionalClusterInfo.isDataAvailable()) {
            return new File(terraformProjectPath + "/clusterinfo/openvpn/" + additionalClusterInfo.getOpenvpnFile());
        }
        return null;
    }

    public File getLogFile(String envPrefix) {
        return new File(terraformProjectPath + "/clusterinfo/logs/" + envPrefix + ".log");
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
            additionalClusterInfoDTO.setSshConnection("Not yet available");
            additionalClusterInfoDTO.setExtDsrUrl("Not yet available");
            additionalClusterInfoDTO.setExtDsrInstalled(1);
            additionalClusterInfoDTO.setDataAvailable(false);
            return additionalClusterInfoDTO;
        }
    }

    public void abortClusterDeployment(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
//        checkState(clusterConfiguration, DeploymentStatus.DEPLOYING, DeploymentStatus.WAIT_DEPLOY);
        checkState(clusterConfiguration, DeploymentStatus.WAIT_DEPLOY);
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.ABORTING);
        clusterConfigurationService.saveJson(clusterConfiguration);
        terraformService.abort(clusterConfiguration);
    }

    public void destroyCluster(ClusterConfigurationDTO clusterConfiguration) throws InvalidClusterStateException {
        checkState(clusterConfiguration, DeploymentStatus.DEPLOYED, DeploymentStatus.FAILED, DeploymentStatus.ABORTED);
        clusterConfiguration.setDeploymentStatus(DeploymentStatus.WAIT_DESTROY);
        clusterConfiguration.setDestroyedAt(new Date());
        clusterConfiguration.setDeploymentComponents(Collections.emptySet());
        clusterConfigurationService.saveJson(clusterConfiguration);
        terraformService.destroy(clusterConfiguration);
    }

    private void checkState(ClusterConfigurationDTO clusterConfiguration, DeploymentStatus... expectedStates) throws InvalidClusterStateException {
        ClusterConfigurationDTO clusterConfigurationDTO = clusterConfigurationService.getClusterConfigurationByEnvPrefix(clusterConfiguration.getEnvPrefix());
        for (DeploymentStatus expectedState : expectedStates) {
            if (clusterConfigurationDTO.getDeploymentStatus() == expectedState) {
                return;
            }
        }
        throw new InvalidClusterStateException("Action aborted. Cluster state was changed and is " + clusterConfigurationDTO.getDeploymentStatus());
    }

    public List<ClusterConfigurationDTO> getMaprClusters() {
        return clusterConfigurationService.getMaprClusters();
    }

    public boolean isPrefixUsed(ClusterConfigurationDTO object) {
        return clusterConfigurationService.isPrefixUsed(object);
    }

    public ClusterConfigurationDTO getClusterConfigurationByEnvPrefix(String object) {
        return clusterConfigurationService.getClusterConfigurationByEnvPrefix(object);
    }
}
