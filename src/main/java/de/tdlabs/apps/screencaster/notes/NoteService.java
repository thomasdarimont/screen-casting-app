package de.tdlabs.apps.screencaster.notes;

import java.util.List;

public interface NoteService {

  NoteEntity save(NoteEntity note);

  NoteEntity findById(Long id);

  List<NoteEntity> findAll();

  void delete(NoteEntity note);

  void deleteAll();
}
