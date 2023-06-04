package com.s3r1s3r2fcopy;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class S3CopyUtility implements CommandLineRunner {

    @Value("${app.s3bucket.sourceBucketName}")
    private String sourceBucketName;

    @Value("${app.s3bucket.sourceBucketRegion}")
    private String sourceBucketRegion;

    @Value("${app.s3bucket.targetBucketName}")
    private String targetBucketName;

    @Value("${app.s3bucket.targetBucketRegion}")
    private String targetBucketRegion;

    @Override
    public void run(String... args) {
        // Dummy logging
        System.out.println("Start copy");

        // S3 client (can access multiple regions)
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                                           .enableForceGlobalBucketAccess()
                                           .build();

        // Copying
        for (S3ObjectSummary objectSummary : s3.listObjects(sourceBucketName).getObjectSummaries()) {
            // Creating request for current object
            CopyObjectRequest copyRequest = new CopyObjectRequest(sourceBucketName, objectSummary.getKey(),
                                                                  targetBucketName, objectSummary.getKey());
            // Copying current object
            s3.copyObject(copyRequest);

            // Dummy logging
            System.out.printf("Copied object: %s\n", objectSummary.getKey());
        }

        // Dummy logging
        System.out.println("Finished");
    }
}
