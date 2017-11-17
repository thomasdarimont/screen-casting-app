package de.tdlabs.apps.screencaster.notes;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDateTime;

@Data
class Note {

  Long id;

  @NotEmpty
  String html;

  String text;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;
}
