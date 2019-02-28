package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentComponents;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.model.NodeLayoutDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.wicket.util.collections.ConcurrentHashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TerraformService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;
    @Value("${maprdeployui.terraform_binary_path}")
    private String terraformBinaryPath;
    @Value("classpath:terraform/terraformconfig.tfvars.tpl")
    private Resource terraformConfigTpl;
    @Autowired
    private ClusterConfigurationService clusterConfigurationService;

    private ConcurrentHashMap<String, Process> activeProcesses = new ConcurrentHashMap<>();
    private ConcurrentHashSet<String> abortedDeployments = new ConcurrentHashSet<>();

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(terraformProjectPath));
        processBuilder.command(terraformBinaryPath, "init");
        Process process = processBuilder.start();
        int exitVal = process.waitFor();
        if (exitVal != 0) {
            throw new IllegalStateException("Failed to run 'terraform init'");
        }
    }


    public void deleteTerraformData(ClusterConfigurationDTO clusterConfig) {
            List<String> files = new ArrayList<>();
            files.add(terraformProjectPath + "/clusterinfo/terraformconfig/" + clusterConfig.getEnvPrefix() + ".tfvars");
            files.add(terraformProjectPath + "/clusterinfo/logs/" + clusterConfig.getEnvPrefix() + ".log");
            files.add(terraformProjectPath + "/clusterinfo/states/" + clusterConfig.getEnvPrefix() + ".tfstate");
            files.add(terraformProjectPath + "/clusterinfo/states/" + clusterConfig.getEnvPrefix() + ".tfstate.backup");
            files.stream().forEach(fileName -> {
                File file = new File(fileName);
                try {
                    if(file.exists()) {
                        FileUtils.forceDelete(file);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

    }

    private void writeTerraformConfiguration(ClusterConfigurationDTO clusterConfig) {
        try {
            String template = StreamUtils.copyToString(terraformConfigTpl.getInputStream(), Charset.defaultCharset()  );
            Map<String, String> substitutes = new HashMap<>();
            substitutes.put("env_prefix", clusterConfig.getEnvPrefix());
            substitutes.put("mapr_cluster_name", clusterConfig.getClusterName());
            substitutes.put("customer", clusterConfig.getCustomerName());
            substitutes.put("aws_region", clusterConfig.getAwsRegion().getCode());
            substitutes.put("aws_av_zone", clusterConfig.getAwsAvZone());
            substitutes.put("aws_machine_type", clusterConfig.getAwsInstanceType().getInstanceCode());
            substitutes.put("domain_name", clusterConfig.getPrivateDomain());
            substitutes.put("instance_count", Integer.toString(clusterConfig.getDefaultClusterLayout().getNumberNodes()));
            substitutes.put("mapr_zookeeper", getNodesString(clusterConfig, NodeLayoutDTO::isZookeeper));
            substitutes.put("mapr_fileserver", getNodesString(clusterConfig, NodeLayoutDTO::isFileserver));
            substitutes.put("mapr_cldb", getNodesString(clusterConfig, NodeLayoutDTO::isCldb));
            substitutes.put("mapr_kafka", getNodesString(clusterConfig, NodeLayoutDTO::isKafkaClient));
            substitutes.put("mapr_kafka_ksql", getNodesString(clusterConfig, NodeLayoutDTO::isKafkaKsql));
            substitutes.put("mapr_mcs", getNodesString(clusterConfig, NodeLayoutDTO::isMcs));
            substitutes.put("mapr_resourcemanager", getNodesString(clusterConfig, NodeLayoutDTO::isResourceManager));
            substitutes.put("mapr_nodemanager", getNodesString(clusterConfig, NodeLayoutDTO::isNodeManager));
            substitutes.put("mapr_historyserver", getNodesString(clusterConfig, NodeLayoutDTO::isHistoryServer));
            substitutes.put("ext_mysql", getNodesString(clusterConfig, NodeLayoutDTO::isMySQL));
            substitutes.put("mapr_spark_yarn", getNodesString(clusterConfig, NodeLayoutDTO::isSpark));
            substitutes.put("mapr_spark_historyserver", getNodesString(clusterConfig, NodeLayoutDTO::isSparkHistoryServer));
            substitutes.put("mapr_nfs_v3", getNodesString(clusterConfig, NodeLayoutDTO::isNfs));
            substitutes.put("mapr_drill_standalone", getNodesString(clusterConfig, NodeLayoutDTO::isDrill));
            substitutes.put("mapr_flume", getNodesString(clusterConfig, NodeLayoutDTO::isFlume));
            substitutes.put("mapr_hbase_cli", getNodesString(clusterConfig, NodeLayoutDTO::isHbaseCli));
            substitutes.put("mapr_hive_cli", getNodesString(clusterConfig, NodeLayoutDTO::isHiveCli));
            substitutes.put("mapr_hive_metastore", getNodesString(clusterConfig, NodeLayoutDTO::isHiveMetaStore));
            substitutes.put("mapr_hive_server2", getNodesString(clusterConfig, NodeLayoutDTO::isHiveServer2));
            substitutes.put("mapr_collectd", getNodesString(clusterConfig, NodeLayoutDTO::isCollectd));
            substitutes.put("mapr_opentsdb", getNodesString(clusterConfig, NodeLayoutDTO::isOpenTSDB));
            substitutes.put("mapr_grafana", getNodesString(clusterConfig, NodeLayoutDTO::isGrafana));
            StringSubstitutor sub = new StringSubstitutor(substitutes);
            String result = sub.replace(template);
            FileUtils.write(new File(terraformProjectPath + "/clusterinfo/terraformconfig/" + clusterConfig.getEnvPrefix() + ".tfvars"), result, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void deploy(ClusterConfigurationDTO clusterConfiguration) {
        // This is async call and still in queue, when aborted was pressed.
        if(abortedDeployments.contains(clusterConfiguration.getEnvPrefix())) {
            abortedDeployments.remove(clusterConfiguration.getEnvPrefix());
            // Wait that JSON file is really written.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            ClusterConfigurationDTO updatedConfig = clusterConfigurationService.getClusterConfigurationByEnvPrefix(clusterConfiguration.getEnvPrefix());
            if(updatedConfig.getDeploymentStatus() != DeploymentStatus.WAIT_DEPLOY) {
                // When status WAIT_DEPLOY, race condition happened and someone tried to re-deploy.
                // All others state will be aborted.
                return;
            }
        }
        executeTerraform(clusterConfiguration, "apply", DeploymentStatus.DEPLOYING, DeploymentStatus.DEPLOYED);
    }

    private void executeTerraform(ClusterConfigurationDTO clusterConfiguration, String terraformMethod, DeploymentStatus operationStatus, DeploymentStatus targetStatus) {
        writeTerraformConfiguration(clusterConfiguration);
        updateDeploymentStatus(clusterConfiguration, operationStatus);
        FileWriter fw = null;
        BufferedReader reader = null;
        BufferedReader errorReader = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(terraformProjectPath));
            processBuilder.command(terraformBinaryPath, terraformMethod, "-state=./clusterinfo/states/" + clusterConfiguration.getEnvPrefix() + ".tfstate", "-var-file=./clusterinfo/terraformconfig/" + clusterConfiguration.getEnvPrefix() + ".tfvars", "-auto-approve", "-no-color");
            Process process = processBuilder.start();
            activeProcesses.put(clusterConfiguration.getEnvPrefix(), process);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            fw = new FileWriter(new File(terraformProjectPath + "/clusterinfo/logs/" +  clusterConfiguration.getEnvPrefix() + ".log"));
            while ((line = reader.readLine()) != null) {
                fw.append(line).append("\n");
                fw.flush();
                updateComponentStatus(clusterConfiguration, line);
            }
            fw.append("===================================================================\n");
            while ((line = errorReader.readLine()) != null) {
                fw.append(line).append("\n");
                fw.flush();
                updateComponentStatus(clusterConfiguration, line);
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                updateDeploymentStatus(clusterConfiguration, targetStatus);
            } else {
                updateDeploymentStatus(clusterConfiguration, DeploymentStatus.FAILED);
            }
        } catch (InterruptedException e) {
            if(abortedDeployments.contains(clusterConfiguration.getEnvPrefix())) {
                updateDeploymentStatus(clusterConfiguration, DeploymentStatus.ABORTED);
            } else {
                updateDeploymentStatus(clusterConfiguration, DeploymentStatus.FAILED);
            }
        } catch (IOException e) {
            updateDeploymentStatus(clusterConfiguration, DeploymentStatus.FAILED);
            throw new RuntimeException(e);
        } finally {
            abortedDeployments.remove(clusterConfiguration.getEnvPrefix());
            activeProcesses.remove(clusterConfiguration.getEnvPrefix());
            IOUtils.closeQuietly(fw);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(errorReader);
        }
    }

    private void updateComponentStatus(ClusterConfigurationDTO clusterConfiguration, String line) {
        if(line.startsWith("module.vpc.aws_vpc.mapr_vpc: Creation complete")) {
            clusterConfiguration.getDeploymentComponents().add(DeploymentComponents.VPC);
            clusterConfigurationService.saveJson(clusterConfiguration);
        } else if(line.startsWith("module.openvpn.null_resource.openvpn_setup: Creation complete")) {
            clusterConfiguration.getDeploymentComponents().add(DeploymentComponents.OPENVPN);
            clusterConfigurationService.saveJson(clusterConfiguration);
        } else if(line.startsWith("module.ansible.null_resource.run_ansible: Creation complete")) {
            clusterConfiguration.getDeploymentComponents().add(DeploymentComponents.ANSIBLE);
            clusterConfigurationService.saveJson(clusterConfiguration);
        } else if(line.startsWith("module.ec2.aws_instance.node") && line.contains("Creation complete")) {
            clusterConfiguration.getDeploymentComponents().add(DeploymentComponents.EC2);
            clusterConfigurationService.saveJson(clusterConfiguration);
        } else if(line.startsWith("Apply complete!")) {
            clusterConfiguration.getDeploymentComponents().add(DeploymentComponents.ALL);
            clusterConfigurationService.saveJson(clusterConfiguration);
        }
    }

    @Async
    public void destroy(ClusterConfigurationDTO clusterConfiguration) {
        executeTerraform(clusterConfiguration, "destroy", DeploymentStatus.DESTROYING, DeploymentStatus.DESTROYED);
    }

    private String getNodesString(ClusterConfigurationDTO clusterConfig, Function<NodeLayoutDTO, Boolean> f) {
        List<NodeLayoutDTO> nodesLayout = clusterConfig.getNodesLayout();
        return nodesLayout.stream().filter(f::apply).map(NodeLayoutDTO::getNodeIndex).map(val -> Integer.toString(val)).collect(Collectors.joining(","));
    }

    private void updateDeploymentStatus(ClusterConfigurationDTO clusterConfiguration, DeploymentStatus status) {
        clusterConfiguration.setDeploymentStatus(status);
        clusterConfigurationService.saveJson(clusterConfiguration);
    }

    public void abort(ClusterConfigurationDTO clusterConfiguration) {
        abortedDeployments.add(clusterConfiguration.getEnvPrefix());
        if(activeProcesses.containsKey(clusterConfiguration.getEnvPrefix())) {
            // if active set back to deploying, do not support cancellation of active process
            updateDeploymentStatus(clusterConfiguration, DeploymentStatus.DEPLOYING);

//            Process process = activeProcesses.get(clusterConfiguration.getEnvPrefix());
//            if(process != null) {
//                process.destroy();
//            }
//            updateDeploymentStatus(clusterConfiguration, DeploymentStatus.ABORTED);
        }
        else {
            updateDeploymentStatus(clusterConfiguration, DeploymentStatus.ABORTED);
        }
    }


}
