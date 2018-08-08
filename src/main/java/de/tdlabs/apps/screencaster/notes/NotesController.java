package de.tdlabs.apps.screencaster.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
class NotesController {

  private final NoteService noteService;

  @PreAuthorize("@accessGuard.isStreamerRequest()")
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<?> createNote(Note note, UriComponentsBuilder uriBuilder) {

    NoteEntity saved = noteService.save(NoteEntity.valueOf(note));
    URI location = uriBuilder.path("/notes/{id}").buildAndExpand(saved.getId()).toUri();

    return ResponseEntity.created(location).build();
  }

  @PreAuthorize("@accessGuard.isStreamerRequest()")
  @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public ResponseEntity<?> updateNote(@PathVariable("id") Long id, Note note) {

    NoteEntity stored = noteService.findById(id);
    if (stored == null) {
      return ResponseEntity.notFound().build();
    }

    stored.setText(note.getText());

    stored = noteService.save(stored);
    return ResponseEntity.ok(stored.toNote());
  }

  @GetMapping
  public ResponseEntity<List<Note>> findAll() {
    return ResponseEntity.ok(
      noteService.findAll()
        .stream()
        .map(NoteEntity::toNote)
        .collect(toList())
    );
  }

  @DeleteMapping
  @PreAuthorize("@accessGuard.isStreamerRequest()")
  public ResponseEntity<?> deleteAll() {

    noteService.deleteAll();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Note> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(noteService.findById(id).toNote());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("@accessGuard.isStreamerRequest()")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {

    NoteEntity note = noteService.findById(id);
    if (note == null) {
      return ResponseEntity.notFound().build();
    }

    noteService.delete(note);

    return ResponseEntity.noContent().build();
  }
}
