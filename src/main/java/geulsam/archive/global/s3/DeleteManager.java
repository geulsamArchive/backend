package geulsam.archive.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeleteManager {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**업로드 된 객체를 참조할 수 있는 public url*/
    @Value("${s3.public.url}")
    private String publicUrl;

    @Async
    public void deleteFile(UUID uuid, String type){
        try{
            String key = type + '-' + uuid.toString();
            amazonS3.deleteObject(bucketName, key);
        } catch (Exception e){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, e.getMessage());
        }
    }

    @Async
    public void deleteFiles(List<UUID> uuids, String type){
        try{
            List<DeleteObjectsRequest.KeyVersion> keysToDelete = uuids.stream()
                    .map(uuid -> new DeleteObjectsRequest.KeyVersion(type + '-' + uuid.toString())).toList();

            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(keysToDelete);

            amazonS3.deleteObjects(deleteRequest);
        } catch (Exception e) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, e.getMessage());
        }
    }
}
