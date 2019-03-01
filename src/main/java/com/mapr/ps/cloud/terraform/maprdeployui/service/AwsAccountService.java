package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.AwsAccountDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairFileRefDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AwsAccountService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;

    private File getPath() {
        return new File(terraformProjectPath + "/clusterinfo/awsaccounts/");
    }

    public List<AwsAccountDTO> getAwsAccounts() {
        Collection<File> files = FileUtils.listFiles(getPath(), new String[]{"json"}, false);
        return files.stream().map(this::getAwsAccountByFile).sorted(Comparator.comparing(AwsAccountDTO::getName)).collect(Collectors.toList());
    }

    public AwsAccountDTO getAwsAccountById(String id) {
        File inputFile = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        if (!inputFile.exists()) {
            return new AwsAccountDTO();
        }
        return getAwsAccountByFile(inputFile);
    }

    public synchronized void save(AwsAccountDTO awsAccount) {
        String id = UUID.randomUUID().toString();
        File file = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        awsAccount.setId(id);
        awsAccount.setCreatedAt(new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, awsAccount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void delete(String id) {
        File file = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        FileUtils.deleteQuietly(file);
    }

    private AwsAccountDTO getAwsAccountByFile(File inputFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputFile, AwsAccountDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
