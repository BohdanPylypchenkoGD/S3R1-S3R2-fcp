package com.s3r1s3r2fcopy;

import com.s3r1s3r2fcopy.S3ClientProvider.ClientPerOperationS3ClientProvider;
import com.s3r1s3r2fcopy.S3ClientProvider.S3ClientProvider;
import com.s3r1s3r2fcopy.S3ClientProvider.SingleMultiRegionalS3ClientProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BeanConfiguration {

    @Bean
    @Profile("smr")
    public S3ClientProvider singleMultiRegionalS3ClientProvider() {
        return new SingleMultiRegionalS3ClientProvider();
    }

    @Bean
    @Profile("cpo")
    public S3ClientProvider clientPerOperationS3ClientProvider(@Value("${app.s3bucket.sourceBucketRegion}")
                                                               String sourceBucketRegion,
                                                               @Value("${app.s3bucket.targetBucketRegion}")
                                                               String targetBucketRegion) {
        return new ClientPerOperationS3ClientProvider(sourceBucketRegion, targetBucketRegion);
    }

    @Bean
    public S3CopyUtility s3CopyUtility(@Value("${app.s3bucket.sourceBucketName}")
                                       String sourceBucketName,
                                       @Value("${app.s3bucket.targetBucketName}")
                                       String targetBucketName,
                                       S3ClientProvider s3ClientProvider) {
        return new S3CopyUtility(sourceBucketName, targetBucketName, s3ClientProvider);
    }

    @Bean
    public ApplicationRunner s3CopyUtilityRunner(S3CopyUtility s3CopyUtility) {
        return args -> s3CopyUtility.run();
    }

}
