package com.s3r1s3r2fcopy;

import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.s3r1s3r2fcopy.S3ClientProvider.S3ClientProvider;

public class S3CopyUtility {

    private final String sourceBucketName;

    private final String targetBucketName;

    private final S3ClientProvider s3ClientProvider;

    public S3CopyUtility(String sourceBucketName, String targetBucketName,
                         S3ClientProvider s3ClientProvider) {
        this.sourceBucketName = sourceBucketName;
        this.targetBucketName = targetBucketName;
        this.s3ClientProvider = s3ClientProvider;
    }

    public void run() {
        // Dummy logging
        System.out.println("Start copy");

        // Copying
        for (S3ObjectSummary objectSummary : s3ClientProvider.getS3Client4ListRequest()
                                                             .listObjects(sourceBucketName)
                                                             .getObjectSummaries()) {
            // Creating request for current object
            CopyObjectRequest copyRequest = new CopyObjectRequest(sourceBucketName, objectSummary.getKey(),
                                                                  targetBucketName, objectSummary.getKey());
            // Copying current object
            s3ClientProvider.getS3Client4CopyRequest().copyObject(copyRequest);

            // Dummy logging
            System.out.printf("Copied object: %s\n", objectSummary.getKey());
        }

        // Dummy logging
        System.out.println("Finished");
    }
}
