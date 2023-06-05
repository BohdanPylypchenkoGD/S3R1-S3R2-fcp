package com.s3r1s3r2fcopy.S3ClientProvider;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public final class ClientPerOperationS3ClientProvider implements S3ClientProvider {

    private final AmazonS3 s3Client4ListRequest;

    private final AmazonS3 s3Client4CopyRequest;

    public ClientPerOperationS3ClientProvider(String sourceBucketRegion, String targetBucketRegion) {
        s3Client4ListRequest = AmazonS3ClientBuilder.standard()
                                                    .withRegion(Regions.fromName(sourceBucketRegion))
                                                    .build();

        s3Client4CopyRequest = AmazonS3ClientBuilder.standard()
                                                    .withRegion(Regions.fromName(targetBucketRegion))
                                                    .build();
    }

    @Override
    public AmazonS3 getS3Client4ListRequest() {
        return s3Client4ListRequest;
    }

    @Override
    public AmazonS3 getS3Client4CopyRequest() {
        return s3Client4CopyRequest;
    }
}
