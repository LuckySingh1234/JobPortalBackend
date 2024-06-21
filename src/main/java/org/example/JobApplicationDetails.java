package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobApplicationDetails {
    private String jobId;
    private String userId;
    private String companyName;
    private String role;
    private String name;
    private String status;
}
