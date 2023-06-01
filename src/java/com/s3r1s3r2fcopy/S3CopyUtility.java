package com.s3r1s3r2fcopy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.ListIterator;

@Component
public class S3CopyUtility implements CommandLineRunner {

    @Value("${app.s3bucket.sourceBucket}")
    private String sourceBucketName;

    @Value("${app.s3bucket.targetBucket}")
    private String targetBucketName;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Started list");

        S3Client client = S3Client.builder().region(Region.US_EAST_1).build();
        ListObjectsRequest request = ListObjectsRequest.builder()
                                                       .bucket(sourceBucketName)
                                                       .build();
        ListObjectsResponse response = client.listObjects(request);
        List<S3Object> objects = response.contents();
        ListIterator<S3Object> listIterator = objects.listIterator();
        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            System.out.println(object.key() + " - " + object.size());
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

        System.out.println("Finished");
    }

}
