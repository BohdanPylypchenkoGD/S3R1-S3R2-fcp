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

        // S3 client for listing (bound to source region)
        AmazonS3 listS3Client = AmazonS3ClientBuilder.standard()
                                                     .withRegion(Regions.fromName(sourceBucketRegion))
                                                     .build();

        // S3 client for copying (bound to target region)
        AmazonS3 copyS3Client = AmazonS3ClientBuilder.standard()
                                                     .withRegion(Regions.fromName(targetBucketRegion))
                                                     .build();

        // Copying
        for (S3ObjectSummary objectSummary : listS3Client.listObjects(sourceBucketName).getObjectSummaries()) {
            // Creating request for current object
            CopyObjectRequest copyRequest = new CopyObjectRequest(sourceBucketName, objectSummary.getKey(),
                                                                  targetBucketName, objectSummary.getKey());
            // Copying current object
            copyS3Client.copyObject(copyRequest);

            // Dummy logging
            System.out.printf("Copied object: %s\n", objectSummary.getKey());
        }

        // Dummy logging
        System.out.println("Finished");
    }
}
