package de.tdlabs.apps.screencaster.notes;

import java.util.List;

public interface NoteService {
  Note save(Note note);

  Note findById(Long id);

  List<Note> findAll();

  void delete(Note note);
}
