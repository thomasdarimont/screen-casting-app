package de.tdlabs.apps.screencaster.filestore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class SimpleFileService implements FileService {

  private final FileRepository fileRepository;

  private final FileStore fileStore;


  @Override
  public FileInfo save(MultipartFile file) {

    Path path = fileStore.save(file);

    FileEntity fileEntity = new FileEntity();
    fileEntity.setId(UUID.randomUUID().toString());
    fileEntity.setName(file.getOriginalFilename());
    fileEntity.setContentType(file.getContentType());
    fileEntity.setPath(path.normalize().toString());
    fileEntity.setSizeInBytes(file.getSize());

    FileEntity saved = fileRepository.save(fileEntity);

    return saved.toInfo();
  }

  @Override
  public FileEntity loadFile(UUID id) {
    return fileRepository.findById(id.toString()).orElse(null);
  }

  @Override
  public InputStream getContents(FileEntity file) {
    return fileStore.getContents(Paths.get(file.getPath()));
  }
}
