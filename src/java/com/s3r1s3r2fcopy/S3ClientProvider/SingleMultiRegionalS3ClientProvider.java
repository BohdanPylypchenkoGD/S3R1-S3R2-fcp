package com.s3r1s3r2fcopy.S3ClientProvider;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public final class SingleMultiRegionalS3ClientProvider implements S3ClientProvider {

    private final AmazonS3 s3Client;

    public SingleMultiRegionalS3ClientProvider() {
        s3Client = AmazonS3ClientBuilder.standard()
                                        .enableForceGlobalBucketAccess()
                                        .build();
    }

    @Override
    public AmazonS3 getS3Client4ListRequest() {
        return s3Client;
    }

    @Override
    public AmazonS3 getS3Client4CopyRequest() {
        return s3Client;
    }

}
