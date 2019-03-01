package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.TemplateDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.model.IModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TemplateService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;

    private File getPath() {
        return new File(terraformProjectPath + "/clusterinfo/templates/");
    }


    public List<TemplateDTO> getTemplates() {
        Collection<File> files = FileUtils.listFiles(getPath(), new String[]{"json"}, false);
        return files.stream().map(this::getTemplateByFile).sorted(Comparator.comparing(TemplateDTO::getEnvPrefix)).collect(Collectors.toList());
    }

    public synchronized void save(TemplateDTO template) {
        String id = UUID.randomUUID().toString();
        if(StringUtils.isNotEmpty(template.getTemplateId())) {
            id = template.getTemplateId();
        }
        File file = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        template.setTemplateId(id);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, template);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public TemplateDTO getTemplateById(String templateId) {
        File inputFile = new File(getPath().getAbsolutePath() + "/" + templateId + ".json");
        if (!inputFile.exists()) {
            return new TemplateDTO();
        }
        return getTemplateByFile(inputFile);
    }


    private TemplateDTO getTemplateByFile(File inputFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputFile, TemplateDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        File file = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        FileUtils.deleteQuietly(file);
    }

    public ClusterConfigurationDTO mapClusterConfiguration(TemplateDTO source) {
        ClusterConfigurationDTO mapped = new ClusterConfigurationDTO();
        BeanUtils.copyProperties(source, mapped, "templateId");
        return mapped;
    }
}
