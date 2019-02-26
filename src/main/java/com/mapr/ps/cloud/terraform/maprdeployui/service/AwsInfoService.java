package com.mapr.ps.cloud.terraform.maprdeployui.service;

import com.mapr.ps.cloud.terraform.maprdeployui.model.AwsInstanceDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.AwsRegionDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsInfoService {


    public List<AwsRegionDTO> getAwsAllowedRegions() {
        return Arrays.asList(
                new AwsRegionDTO("EU (Irland)", "eu-west-1", Arrays.asList("eu-west-1a", "eu-west-1b", "eu-west-1c")),
                new AwsRegionDTO("EU (Frankfurt)", "eu-central-1", Arrays.asList("eu-central-1a", "eu-central-1b")),
                new AwsRegionDTO("USA West (North California)", "us-west-1", Arrays.asList("us-west-1a", "us-west-1b")),
                new AwsRegionDTO("USA West (Oregon)", "us-west-2", Arrays.asList("us-west-2a", "us-west-2b", "us-west-2b")),
                new AwsRegionDTO("Asia Pacific (Sydney)", "ap-southeast-2", Arrays.asList("ap-southeast-2a", "ap-southeast-2b", "ap-southeast-2c"))
        );
    }

    public List<AwsInstanceDTO> getAwsAllowedInstances() {
        return Arrays.asList(
                new AwsInstanceDTO("r5d.2xlarge", 8, 64, "300 GiB NVMe SSD", "10 Gigabit", allRegions()),
                new AwsInstanceDTO("z1d.3xlarge", 12, 96, "450 GiB NVMe SSD", "10 Gigabit", Arrays.asList("eu-west-1", "us-west-1", "us-west-2", "ap-southeast-2")),
                new AwsInstanceDTO("r5d.4xlarge", 16, 128, "600 GiB (2 * 300 GiB NVMe SSD)", "10 Gigabit", allRegions()),
                new AwsInstanceDTO("i3.8xlarge", 32, 244, "7600 GiB (4 * 1900 GiB NVMe SSD)", "10 Gigabit", allRegions())
        );
    }

    public List<String> allRegions() {
        return getAwsAllowedRegions().stream().map(AwsRegionDTO::getCode).collect(Collectors.toList());
    }
}
