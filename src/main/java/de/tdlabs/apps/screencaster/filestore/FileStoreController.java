package de.tdlabs.apps.screencaster.filestore;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
class FileStoreController {

  private final FileService fileService;

  @PostMapping
  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  ResponseEntity<?> upload(MultipartFile file, HttpServletRequest request) {

    FileInfo ref = fileService.save(file);

    return ResponseEntity.ok(ref);
  }

  @GetMapping("/{id}")
  ResponseEntity<?> download(@PathVariable("id") UUID id) {

    FileEntity file = fileService.loadFile(id);

    InputStream is = fileService.getContents(file);

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
      .body(new InputStreamResource(is));
  }
}
