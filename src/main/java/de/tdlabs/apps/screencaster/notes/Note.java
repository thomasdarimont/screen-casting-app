package de.tdlabs.apps.screencaster.notes;

import de.tdlabs.apps.screencaster.filestore.FileInfo;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDateTime;

@Data
class Note {

  Long id;

  @NotEmpty
  String text;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;

  FileInfo fileInfo;
}
