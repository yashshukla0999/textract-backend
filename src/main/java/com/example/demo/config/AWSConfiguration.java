package com.example.demo.config;

// AWSConfiguration.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;

@Configuration
public class AWSConfiguration {

    // Replace these values with your AWS credentials
    private static final String AWS_ACCESS_KEY = "AKIAYJN4DFRBNNF3QMUA";
    private static final String AWS_SECRET_KEY = "rpu6qEBVMYkvT+Z0OwZO4StIt79bnIMJTqY955OL";
    private static final String AWS_REGION = "us-east-1"; // Change to your region
    
    @Bean
    public AmazonTextract amazonTextractClient() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        return AmazonTextractClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(AWS_REGION)
                .build();
    }
}
