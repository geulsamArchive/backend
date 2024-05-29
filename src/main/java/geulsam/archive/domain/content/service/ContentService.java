package geulsam.archive.domain.content.service;

import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {

    private ContentRepository contentRepository;

    public List<ContentRes> getContents(Pageable pageable) {
        List<Content> contents = contentRepository.findAll(pageable).getContent();
        List<ContentRes> contentResList = new ArrayList<>();

        return contentResList;   //
    }

}
