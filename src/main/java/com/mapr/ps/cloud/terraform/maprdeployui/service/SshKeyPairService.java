package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.ps.cloud.terraform.maprdeployui.model.GeneratedKeyPairDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairFileRefDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SshKeyPairService {
    @Value("${maprdeployui.terraform_project_path}")
    private String terraformProjectPath;

    private File getPath() {
        return new File(terraformProjectPath + "/clusterinfo/keypairs/");
    }

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
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
        if (isNotWindows()) {
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

    public GeneratedKeyPairDTO generateKeyPair() {
        PemWriter pw = null;
        KeyPair keyPair = null;
        StringWriter privateOut = new StringWriter();
        try {
            keyPair = generateRSAKeyPair();
            pw = new PemWriter(privateOut);
            pw.writeObject(new PemObject("PRIVATE KEY", keyPair.getPrivate().getEncoded()));

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(pw);
        }
        String publicKeyEncoded = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
        String sshKey = "ssh-rsa " + publicKeyEncoded;

        GeneratedKeyPairDTO generatedKeyPairDTO = new GeneratedKeyPairDTO();
        generatedKeyPairDTO.setPublicKey(sshKey);
        generatedKeyPairDTO.setPrivateKey(privateOut.toString());
        return generatedKeyPairDTO;
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(4096);
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }
}
