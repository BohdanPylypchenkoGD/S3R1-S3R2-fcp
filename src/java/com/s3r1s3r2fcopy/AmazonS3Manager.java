package com.s3r1s3r2fcopy;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmazonS3Manager {

    //@Value("${app.amazonS3manager.roleArn}")
    //private String roleArn;

    //@Value("${app.amazonS3manager.roleSessionName}")
    //private String roleSessionName;

    private AWSSecurityTokenService stsClient;

    private AmazonS3 amazonS3;

    @PostConstruct
    private void initializeS3() {
        //// Create the STS client
        //stsClient = AWSSecurityTokenServiceClientBuilder.standard().build();

        //// Create the AssumeRoleRequest
        //AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest().withRoleArn(roleArn)
        //                                                             .withRoleSessionName(roleSessionName);

        //// Assume the role
        //AssumeRoleResult assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);

        //// Getting assumed credentials
        //BasicSessionCredentials awsCredentials = new BasicSessionCredentials(assumeRoleResult.getCredentials().getAccessKeyId(),
        //                                                                     assumeRoleResult.getCredentials().getSecretAccessKey(),
        //                                                                     assumeRoleResult.getCredentials().getSessionToken());

        // Initializing amazon s3
        amazonS3 = AmazonS3ClientBuilder.standard()
                                        .enableForceGlobalBucketAccess()
                                        .build();
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    @PreDestroy
    private void cleanupCredentials() {
        stsClient.shutdown();
    }

}
