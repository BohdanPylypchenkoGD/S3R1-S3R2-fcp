package com.s3r1s3r2fcopy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
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

    @Value("${app.s3bucket.targetBucketRegion")
    private String targetBucketRegion;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Started copy");

        // Creating client
        //S3Client client = S3Client.builder()
        //                          .serviceConfiguration(S3Configuration.builder()
        //                                                               .useArnRegionEnabled(true)
        //                                                               .multiRegionEnabled(true)
        //                                                               .build())
        //                          .build();

        //S3Client client = S3Client.builder()
        //                          .useArnRegion(true)
        //                          .build();

        // Creating client
        S3Client client = S3Client.builder().region(Region.of(sourceBucketRegion)).build();

        // List request
        ListObjectsRequest request = ListObjectsRequest.builder()
                                                       .bucket(sourceBucketName)
                                                       .build();

        // Getting response
        ListObjectsResponse response = client.listObjects(request);

        // Getting objects
        List<S3Object> objects = response.contents();

        // Printing
        objects.stream().forEach((S3Object e) -> System.out.println(e.key() + " - " + e.size()));

        System.out.println("Finished");
    }

        //S3Client client = S3Client.builder().build();

        //CopyObjectRequest copyReq = CopyObjectRequest.builder()
        //        .sourceBucket(sourceBucketName)
        //        .sourceKey("test.txt")
        //        .destinationBucket(targetBucketName)
        //        .destinationKey("test.txt")
        //        .build();

        //try {
        //    CopyObjectResponse copyRes = client.copyObject(copyReq);
        //    System.out.println(copyRes.copyObjectResult().toString());

        //} catch (S3Exception e) {
        //    System.err.println(e.awsErrorDetails().errorMessage());
        //    System.exit(1);
        //}


}
