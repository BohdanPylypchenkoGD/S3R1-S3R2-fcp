package com.s3r1s3r2fcopy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.ListIterator;

@Component
public class S3CopyUtility implements CommandLineRunner {

    @Value("${app.s3bucket.source}")
    private String sourceBucketName;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Started list");
        S3Client client = S3Client.builder().build();
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
    }

}
