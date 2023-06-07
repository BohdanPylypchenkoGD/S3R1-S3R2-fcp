package com.s3r1s3r2fcopy;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    public AmazonS3Manager() {
        amazonS3 = AmazonS3ClientBuilder.standard()
                                        .enableForceGlobalBucketAccess()
                                        .build();
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

}
