package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairFileRefDTO;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jni.OS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SshKeyPairService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;

    private File getPath() {
        return new File(terraformProjectPath + "/clusterinfo/keypairs/");
    }

    public SshKeyPairDTO getSshKeyPairById(String id) {
        File inputFile = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        if (!inputFile.exists()) {
            return new SshKeyPairDTO();
        }
        SshKeyPairFileRefDTO withFileRef = getSshKeyPairByFile(inputFile);
        String privateKey = readKey(withFileRef.getPrivateKeyFile());
        String publicKey = readKey(withFileRef.getPublicKeyFile());
        SshKeyPairDTO sshKeyPairByFile = new SshKeyPairDTO();
        sshKeyPairByFile.setId(id);
        sshKeyPairByFile.setPrivateKey(privateKey);
        sshKeyPairByFile.setPublicKey(publicKey);
        sshKeyPairByFile.setName(withFileRef.getName());
        sshKeyPairByFile.setCreatedt(withFileRef.getCreatedt());
        return sshKeyPairByFile;
    }

    private String readKey(String file) {
        try {
            return FileUtils.readFileToString(new File(getPath().getAbsolutePath() + "/" + file), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SshKeyPairFileRefDTO getSshKeyPairByFile(File inputFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputFile, SshKeyPairFileRefDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SshKeyPairDTO> getSshKeyPairs() {
        Collection<File> files = FileUtils.listFiles(getPath(), new String[]{"json"}, false);
        return files.stream().map(this::getSshKeyPairByFile).map(p -> getSshKeyPairById(p.getId())).sorted(Comparator.comparing(SshKeyPairDTO::getName)).collect(Collectors.toList());
    }

    public List<SshKeyPairFileRefDTO> getSshKeyPairsFileRef() {
        Collection<File> files = FileUtils.listFiles(getPath(), new String[]{"json"}, false);
        return files.stream().map(this::getSshKeyPairByFile).sorted(Comparator.comparing(SshKeyPairFileRefDTO::getName)).collect(Collectors.toList());
    }

    public synchronized void delete(String id) {
        File sshKeyConfig = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        SshKeyPairFileRefDTO withFileRef = getSshKeyPairByFile(sshKeyConfig);
        File privateKeyFile = new File(getPath().getAbsolutePath() + "/" + withFileRef.getPrivateKeyFile());
        File publicKeyFile = new File(getPath().getAbsolutePath() + "/" + withFileRef.getPublicKeyFile());
        FileUtils.deleteQuietly(privateKeyFile);
        FileUtils.deleteQuietly(publicKeyFile);
        FileUtils.deleteQuietly(sshKeyConfig);
    }

    public synchronized void save(SshKeyPairDTO sshKeyPairDTO) {
        String id = UUID.randomUUID().toString();
        File sshKeyConfig = new File(getPath().getAbsolutePath() + "/" + id + ".json");
        SshKeyPairFileRefDTO stored = new SshKeyPairFileRefDTO();
        stored.setId(id);
        stored.setPublicKeyFile(id + ".pub");
        stored.setPrivateKeyFile(id + ".key");
        stored.setCreatedt(new Date());
        stored.setName(sshKeyPairDTO.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(sshKeyConfig, stored);
            File publicKey = new File(getPath().getAbsolutePath() + "/" + stored.getPublicKeyFile());
            FileUtils.writeStringToFile(publicKey, sshKeyPairDTO.getPublicKey(), Charset.defaultCharset());
            setPermissionForKeys(publicKey);
            File privateKey = new File(getPath().getAbsolutePath() + "/" + stored.getPrivateKeyFile());
            FileUtils.writeStringToFile(privateKey, sshKeyPairDTO.getPrivateKey(), Charset.defaultCharset());
            setPermissionForKeys(privateKey);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPermissionForKeys(File file) {
        if(isNotWindows()) {
            Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rw-------");
            try {
                Files.setPosixFilePermissions(file.toPath(), ownerWritable);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isNotWindows() {
        return !System.getProperty("os.name").toLowerCase().contains("win");
    }

}
