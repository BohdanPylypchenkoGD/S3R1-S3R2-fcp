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

        /*
         * Creating client for copy request
         * Uncomment one of given or write your own to test
         * Predefined options are:
         * 1. client bounded to target bucket region - will execute successfully
         * 2. client bounded to source bucket region - will execute ONLY if source and target are in same region
         * 3. client with service configuration - to show that ARN options don't have impact, only region bound matters.
         * When changing client, don't forget to change source/target properties in application.properties
         *
         * =============================================================================================================
         * Explanation:
         *
         * S3Client instance is bounded to specific region.
         * The region is either specified manually by region() method in builder,
         * or by software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain (if not specified manually)
         * So all client instance requests can operate only on buckets in specified region.
         *
         * At the same time, some requests need to operate with set of buckets those can be located
         * in different regions.
         * S3Client allows cross-region calls for such requests.
         * CopyObjectRequest is an example of multi-region request:
         * 2 buckets are required - source and destination -> buckets can be located in different regions
         *
         * In case of multi-region requests, a question arises: which region should client belong to?
         * For example, when performing copyObject, client has 2 options for region:
         * source bucket region and target bucket region.
         * If client is bounded to target bucket region, copyObject executes correctly.
         * If client is bounded to source bucket region, an error
         * “The authorization header is malformed; the region ‘source-bucket-region’ is wrong; expecting ‘target-bucket-region’”
         * arises.
         *
         * According to copyObject method (S3Client interface) documentation:
         * All copy requests must be authenticated. Additionally, you must have read access to the source object and
         * write access to the destination bucket. For more information, see
         * “https://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html”.
         * Both the Region that you want to copy the object from and the Region that you want to copy the object to must
         * be enabled for your account.
         *
         * Turns out that each S3 request contains authentication header! One of components of header is HOST field:
         * GET /photos/puppy.jpg HTTP/1.1
         * Host: awsexamplebucket1.us-west-1.s3.amazonaws.com <- here is HOST containing bucket region.
         * Date: Tue, 27 Mar 2007 19:36:42 +0000
         *
         * Conclusion:
         * Each multi-regional request ("multi-regional" in sense "communicating with buckets in different regions")
         * actually performs a SINGLE request to specific bucket? All requests for other buckets are done by AWS itself?
         * In case of copyObject, the request is going to ONLY target bucket - aws does all needed calls to source
         * bucket by itself?
         * If yes - that is why to perform multi-region copy, S3Client instance should be bound to target bucket region.
         *
         * In general case, when doing "multi-region" requests, you have to determine bucket, to those actual request
         * will be sent, and bound client to this region.
         *
         * Again: to copy between buckets in different regions, the S3Client must be bound to the target
         * bucket region - since only 1 request is made (request to the target bucket).
         *
         * There is another approach, which is based on using Multi-Region Access Points:
         * https://docs.aws.amazon.com/AmazonS3/latest/userguide/MultiRegionAccessPoints.html
         * In this case S3 client communicates with access point rather than with bucket directly.
         * As far as I understand,
         * the ability of client to use access points are defined by flags like useArnRegion, multiRegion, etc
         * (this assumption is based on documentation of the flags, I did not test flags -> access point scenarios).
         * Anyway, this approach is certainly harder to configure (since we need to create Multi-Region Access Point,
         * add buckets to it etc.).
         * It seems that Multi-Region Access Points are used for complicated inter-bucket operations.
         * Thus, Multi-Region Access Point should not be used when
         * simple file copy is required.
         */

        // client 1
        S3Client copyClient = S3Client.builder().region(Region.of(targetBucketRegion)).build();

        /* client 2
        S3Client copyClient = S3Client.builder().region(Region.of(sourceBucketRegion)).build();
         */

        /* client 3
        S3Client copyClient = S3Client.builder()
                                      .serviceConfiguration(S3Configuration.builder()
                                                                           .useArnRegionEnabled(true)
                                                                           .multiRegionEnabled(true)
                                                                           .build())
                                      .build();
         */

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
