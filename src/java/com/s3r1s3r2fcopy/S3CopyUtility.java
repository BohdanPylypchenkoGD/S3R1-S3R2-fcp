package com.s3r1s3r2fcopy;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class S3CopyUtility implements ApplicationRunner {

    @Value("${app.s3bucket.sourceBucketName}")
    private String sourceBucketName;

    @Value("${app.s3bucket.targetBucketName}")
    private String targetBucketName;

    private final AmazonS3 amazonS3;

    @Autowired
    public S3CopyUtility(AmazonS3Manager amazonS3Manager) {
        this.amazonS3 = amazonS3Manager.getAmazonS3();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Dummy logging
        System.out.println("Start copy");

        // Copying
        for (S3ObjectSummary objectSummary : amazonS3.listObjects(sourceBucketName)
                                                     .getObjectSummaries()) {
            // Creating request for current object
            CopyObjectRequest copyRequest = new CopyObjectRequest(sourceBucketName, objectSummary.getKey(),
                                                                  targetBucketName, objectSummary.getKey());
            // Copying current object
            amazonS3.copyObject(copyRequest);

            // Dummy logging
            System.out.printf("Copied object: %s\n", objectSummary.getKey());
        }

        // Dummy logging
        System.out.println("Finished");
    }

}
