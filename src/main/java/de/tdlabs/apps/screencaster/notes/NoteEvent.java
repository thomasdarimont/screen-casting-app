package de.tdlabs.apps.screencaster.notes;

import lombok.Data;

public interface NoteEvent {

  default String getType() {
    return getClass().getSimpleName().toLowerCase();
  }

  static NoteEvent created(Note note) {
    return new Created(note);
  }

  static NoteEvent deleted(Note note) {
    return new Deleted(note.getId());
  }

  static NoteEvent updated(Note note) {
    return new Updated(note);
  }

  @Data
  class Created implements NoteEvent {
    final Note note;
  }

  @Data
  class Deleted implements NoteEvent {
    final Long noteId;
  }

  @Data
  class Updated implements NoteEvent {
    final Note note;
  }
}
