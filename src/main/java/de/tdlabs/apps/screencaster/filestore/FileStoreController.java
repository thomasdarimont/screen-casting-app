package de.tdlabs.apps.screencaster.filestore;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
class FileStoreController {

  private final FileService fileService;

  @PostMapping
  @PreAuthorize("@accessGuard.isStreamerRequest()")
  public ResponseEntity<?> upload(MultipartFile file) {

    FileInfo ref = fileService.save(file);

    return ResponseEntity.ok(ref);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> download(@PathVariable("id") UUID id) {

    FileEntity file = fileService.loadFile(id);
    InputStream is = fileService.getContents(file);

    return ResponseEntity.ok()
      .header(CONTENT_TYPE, file.getContentType())
      .body(new InputStreamResource(is));
  }
}
