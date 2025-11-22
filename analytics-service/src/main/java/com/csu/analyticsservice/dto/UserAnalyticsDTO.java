package com.csu.analyticsservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserAnalyticsDTO {
    private Integer totalUsers;           // 总用户数
    private Integer activeUsers;          // 活跃用户数
    private List<VIPLevelDTO> vipDistribution; // VIP等级分布

    @Data
    public static class VIPLevelDTO {
        private String level;
        private Integer count;
    }
}
