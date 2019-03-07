package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.List;

public class DefaultClusterLayoutDTO implements Serializable {
    private int numberNodes;
    private List<Integer> zookeeper;
    private List<Integer> cldb;
    private List<Integer> fileserver;
    private List<Integer> kafkaClient;
    private List<Integer> kafkaKsql;
    private List<Integer> mcs;
    private List<Integer> resourceManager;
    private List<Integer> nodeManager;
    private List<Integer> historyServer;
    private List<Integer> mySQL;
    private List<Integer> spark;
    private List<Integer> sparkHistoryServer;
    private List<Integer> gateway;
    private List<Integer> dataAccessGateway;
    private List<Integer> nfs;
    private List<Integer> drill;
    private List<Integer> flume;
    private List<Integer> hbaseCli;
    private List<Integer> hiveCli;
    private List<Integer> hiveMetaStore;
    private List<Integer> hiveServer2;
    private List<Integer> collectd;
    private List<Integer> openTSDB;
    private List<Integer> grafana;

    public List<Integer> getKafkaKsql() {
        return kafkaKsql;
    }

    public void setKafkaKsql(List<Integer> kafkaKsql) {
        this.kafkaKsql = kafkaKsql;
    }

    public int getNumberNodes() {
        return numberNodes;
    }

    public void setNumberNodes(int numberNodes) {
        this.numberNodes = numberNodes;
    }

    public List<Integer> getSparkHistoryServer() {
        return sparkHistoryServer;
    }

    public void setSparkHistoryServer(List<Integer> sparkHistoryServer) {
        this.sparkHistoryServer = sparkHistoryServer;
    }

    public List<Integer> getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(List<Integer> zookeeper) {
        this.zookeeper = zookeeper;
    }

    public List<Integer> getCldb() {
        return cldb;
    }

    public void setCldb(List<Integer> cldb) {
        this.cldb = cldb;
    }

    public List<Integer> getFileserver() {
        return fileserver;
    }

    public void setFileserver(List<Integer> fileserver) {
        this.fileserver = fileserver;
    }

    public List<Integer> getKafkaClient() {
        return kafkaClient;
    }

    public void setKafkaClient(List<Integer> kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    public List<Integer> getMcs() {
        return mcs;
    }

    public void setMcs(List<Integer> mcs) {
        this.mcs = mcs;
    }

    public List<Integer> getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(List<Integer> resourceManager) {
        this.resourceManager = resourceManager;
    }

    public List<Integer> getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(List<Integer> nodeManager) {
        this.nodeManager = nodeManager;
    }

    public List<Integer> getHistoryServer() {
        return historyServer;
    }

    public void setHistoryServer(List<Integer> historyServer) {
        this.historyServer = historyServer;
    }

    public List<Integer> getMySQL() {
        return mySQL;
    }

    public void setMySQL(List<Integer> mySQL) {
        this.mySQL = mySQL;
    }

    public List<Integer> getSpark() {
        return spark;
    }

    public void setSpark(List<Integer> spark) {
        this.spark = spark;
    }

    public List<Integer> getNfs() {
        return nfs;
    }

    public void setNfs(List<Integer> nfs) {
        this.nfs = nfs;
    }

    public List<Integer> getDrill() {
        return drill;
    }

    public void setDrill(List<Integer> drill) {
        this.drill = drill;
    }

    public List<Integer> getFlume() {
        return flume;
    }

    public void setFlume(List<Integer> flume) {
        this.flume = flume;
    }

    public List<Integer> getHbaseCli() {
        return hbaseCli;
    }

    public void setHbaseCli(List<Integer> hbaseCli) {
        this.hbaseCli = hbaseCli;
    }

    public List<Integer> getHiveCli() {
        return hiveCli;
    }

    public void setHiveCli(List<Integer> hiveCli) {
        this.hiveCli = hiveCli;
    }

    public List<Integer> getHiveMetaStore() {
        return hiveMetaStore;
    }

    public void setHiveMetaStore(List<Integer> hiveMetaStore) {
        this.hiveMetaStore = hiveMetaStore;
    }

    public List<Integer> getHiveServer2() {
        return hiveServer2;
    }

    public void setHiveServer2(List<Integer> hiveServer2) {
        this.hiveServer2 = hiveServer2;
    }

    public List<Integer> getCollectd() {
        return collectd;
    }

    public void setCollectd(List<Integer> collectd) {
        this.collectd = collectd;
    }

    public List<Integer> getOpenTSDB() {
        return openTSDB;
    }

    public void setOpenTSDB(List<Integer> openTSDB) {
        this.openTSDB = openTSDB;
    }

    public List<Integer> getGrafana() {
        return grafana;
    }

    public void setGrafana(List<Integer> grafana) {
        this.grafana = grafana;
    }

    public List<Integer> getGateway() {
        return gateway;
    }

    public void setGateway(List<Integer> gateway) {
        this.gateway = gateway;
    }

    public List<Integer> getDataAccessGateway() {
        return dataAccessGateway;
    }

    public void setDataAccessGateway(List<Integer> dataAccessGateway) {
        this.dataAccessGateway = dataAccessGateway;
    }
}
