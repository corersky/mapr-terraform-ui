package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;

public class NodeLayoutDTO implements Serializable {
    private Integer nodeIndex;
    private boolean zookeeper;
    private boolean cldb;
    private boolean fileserver;
    private boolean kafkaClient;
    private boolean kafkaKsql;
    private boolean mcs;
    private boolean resourceManager;
    private boolean nodeManager;
    private boolean historyServer;
    private boolean mySQL;
    private boolean spark;
    private boolean sparkHistoryServer;
    private boolean nfs;
    private boolean drill;
    private boolean flume;
    private boolean hbaseCli;
    private boolean hiveCli;
    private boolean hiveMetaStore;
    private boolean hiveServer2;
    private boolean collectd;
    private boolean openTSDB;
    private boolean grafana;

    public boolean isKafkaKsql() {
        return kafkaKsql;
    }

    public void setKafkaKsql(boolean kafkaKsql) {
        this.kafkaKsql = kafkaKsql;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public boolean isSparkHistoryServer() {
        return sparkHistoryServer;
    }

    public void setSparkHistoryServer(boolean sparkHistoryServer) {
        this.sparkHistoryServer = sparkHistoryServer;
    }

    public boolean isZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(boolean zookeeper) {
        this.zookeeper = zookeeper;
    }

    public boolean isCldb() {
        return cldb;
    }

    public void setCldb(boolean cldb) {
        this.cldb = cldb;
    }

    public boolean isFileserver() {
        return fileserver;
    }

    public void setFileserver(boolean fileserver) {
        this.fileserver = fileserver;
    }

    public boolean isKafkaClient() {
        return kafkaClient;
    }

    public void setKafkaClient(boolean kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    public boolean isMcs() {
        return mcs;
    }

    public void setMcs(boolean mcs) {
        this.mcs = mcs;
    }

    public boolean isResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(boolean resourceManager) {
        this.resourceManager = resourceManager;
    }

    public boolean isNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(boolean nodeManager) {
        this.nodeManager = nodeManager;
    }

    public boolean isHistoryServer() {
        return historyServer;
    }

    public void setHistoryServer(boolean historyServer) {
        this.historyServer = historyServer;
    }

    public boolean isMySQL() {
        return mySQL;
    }

    public void setMySQL(boolean mySQL) {
        this.mySQL = mySQL;
    }

    public boolean isSpark() {
        return spark;
    }

    public void setSpark(boolean spark) {
        this.spark = spark;
    }

    public boolean isNfs() {
        return nfs;
    }

    public void setNfs(boolean nfs) {
        this.nfs = nfs;
    }

    public boolean isDrill() {
        return drill;
    }

    public void setDrill(boolean drill) {
        this.drill = drill;
    }

    public boolean isFlume() {
        return flume;
    }

    public void setFlume(boolean flume) {
        this.flume = flume;
    }

    public boolean isHbaseCli() {
        return hbaseCli;
    }

    public void setHbaseCli(boolean hbaseCli) {
        this.hbaseCli = hbaseCli;
    }

    public boolean isHiveCli() {
        return hiveCli;
    }

    public void setHiveCli(boolean hiveCli) {
        this.hiveCli = hiveCli;
    }

    public boolean isHiveMetaStore() {
        return hiveMetaStore;
    }

    public void setHiveMetaStore(boolean hiveMetaStore) {
        this.hiveMetaStore = hiveMetaStore;
    }

    public boolean isHiveServer2() {
        return hiveServer2;
    }

    public void setHiveServer2(boolean hiveServer2) {
        this.hiveServer2 = hiveServer2;
    }

    public boolean isCollectd() {
        return collectd;
    }

    public void setCollectd(boolean collectd) {
        this.collectd = collectd;
    }

    public boolean isOpenTSDB() {
        return openTSDB;
    }

    public void setOpenTSDB(boolean openTSDB) {
        this.openTSDB = openTSDB;
    }

    public boolean isGrafana() {
        return grafana;
    }

    public void setGrafana(boolean grafana) {
        this.grafana = grafana;
    }
}
