package de.tdlabs.apps.screencaster.notes;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
class Note {

  private Long id;

  @NotEmpty
  private String html;

  private String text;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
