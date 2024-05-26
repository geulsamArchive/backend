package geulsam.archive.global.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * S3 연결 Configuration
 */
@Configuration
public class S3Config {
    @Value("${aws.s3.endpointUrl}")
    private String s3EndpointUrl;

    //S3 리전
    @Value("${aws.s3.region}")
    private String s3Region;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Bean
    public AmazonS3 s3Client(){

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3EndpointUrl, s3Region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
