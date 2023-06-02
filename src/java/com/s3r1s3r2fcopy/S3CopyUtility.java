package com.s3r1s3r2fcopy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;

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
        System.out.println("Started copy");

        // Creating client
        S3Client listClient = S3Client.builder().region(Region.of(sourceBucketRegion)).build();

        // List request
        ListObjectsRequest request = ListObjectsRequest.builder()
                                                       .bucket(sourceBucketName)
                                                       .build();

        // Getting list response
        ListObjectsResponse listResponse = listClient.listObjects(request);

        // Getting objects
        List<S3Object> objects = listResponse.contents();

        // Creating client for copying
        S3Client copyClient = S3Client.builder().region(Region.of(targetBucketRegion)).build();

        // Copying
        objects.parallelStream()
               .forEach((S3Object s3obj) -> {
                   // Creating copy request for current object
                   CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                                                                    .sourceBucket(sourceBucketName)
                                                                    .sourceKey(s3obj.key())
                                                                    .destinationBucket(targetBucketName)
                                                                    .destinationKey(s3obj.key())
                                                                    .build();

                   // Executing
                   try {
                       CopyObjectResponse copyResponse = copyClient.copyObject(copyRequest);
                       System.out.println(copyResponse.copyObjectResult().toString());
                   } catch (S3Exception e) {
                       System.err.println(e.awsErrorDetails().errorMessage());
                       System.exit(1);
                   }
               });

        // Dummy logging
        System.out.println("Finished");
    }

}
