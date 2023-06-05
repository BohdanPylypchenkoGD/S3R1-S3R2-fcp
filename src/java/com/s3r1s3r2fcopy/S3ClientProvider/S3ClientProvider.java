package com.s3r1s3r2fcopy.S3ClientProvider;

import com.amazonaws.services.s3.AmazonS3;

public interface S3ClientProvider {

    AmazonS3 getS3Client4ListRequest();

    AmazonS3 getS3Client4CopyRequest();

}
