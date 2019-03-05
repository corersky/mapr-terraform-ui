env_prefix = "${env_prefix}"
mapr_cluster_name = "${mapr_cluster_name}"
customer = "${customer}"
aws_region = "${aws_region}"
aws_av_zone = "${aws_av_zone}"
machine_type = "${aws_machine_type}"
associate_public_address = true

vpn_instance_type = "t2.micro"
domain_name = "${domain_name}"

instance_count = ${instance_count}
mapr_zookeeper = "${mapr_zookeeper}"
mapr_fileserver = "${mapr_fileserver}"
mapr_cldb = "${mapr_cldb}"
mapr_kafka = "${mapr_kafka}"
mapr_kafka_ksql = "${mapr_kafka_ksql}"
mapr_mcs = "${mapr_mcs}"
mapr_resourcemanager = "${mapr_resourcemanager}"
mapr_nodemanager = "${mapr_nodemanager}"
mapr_historyserver = "${mapr_historyserver}"
ext_mysql = "${ext_mysql}"
mapr_spark_yarn = "${mapr_spark_yarn}"
mapr_spark_historyserver = "${mapr_spark_historyserver}"
mapr_nfs_v3 = "${mapr_nfs_v3}"
mapr_drill_standalone = "${mapr_drill_standalone}"
mapr_flume = "${mapr_flume}"
mapr_hbase_cli = "${mapr_hbase_cli}"
mapr_hive_cli = "${mapr_hive_cli}"
mapr_hive_metastore = "${mapr_hive_metastore}"
mapr_hive_server2 = "${mapr_hive_server2}"
mapr_collectd = "${mapr_collectd}"
mapr_opentsdb = "${mapr_opentsdb}"
mapr_grafana = "${mapr_grafana}"
ext_dsr_instance_count = ${ext_dsr_instance_count}
ext_usecase1_instance_count = ${ext_usecase1_instance_count}


ssh_public_key_file = "${ssh_public_key_file}"
ssh_private_key_file = "${ssh_private_key_file}"
ssh_mapruser_public_key_file = "${ssh_mapruser_public_key_file}"

aws_access_key = "${aws_access_key}"
aws_secret_key = "${aws_secret_key}"