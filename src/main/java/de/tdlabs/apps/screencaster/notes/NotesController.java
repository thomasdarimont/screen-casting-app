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

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
class NotesController {

  private final NoteService noteService;

  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  ResponseEntity<?> createNote(Note note, UriComponentsBuilder uriBuilder, HttpServletRequest request) {

    NoteEntity saved = noteService.save(NoteEntity.valueOf(note));
    URI location = uriBuilder.path("/notes/{id}").buildAndExpand(saved.getId()).toUri();

    return ResponseEntity.created(location).build();
  }


  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  ResponseEntity<?> updateNote(@PathVariable("id") Long id, Note note, UriComponentsBuilder uriBuilder, HttpServletRequest request) {

    NoteEntity stored = noteService.findById(id);
    if (stored == null) {
      return ResponseEntity.notFound().build();
    }

    stored.setText(note.getText());

    stored = noteService.save(stored);
    return ResponseEntity.ok(stored.toNote());
  }

  @GetMapping
  ResponseEntity<List<Note>> findAll() {
    return ResponseEntity.ok(
      noteService.findAll()
        .stream()
        .map(NoteEntity::toNote)
        .collect(toList())
    );
  }

  @DeleteMapping
  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  ResponseEntity<?> deleteAll(HttpServletRequest request) {

    noteService.deleteAll();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  ResponseEntity<Note> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(noteService.findById(id).toNote());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
  ResponseEntity<?> delete(@PathVariable("id") Long id, HttpServletRequest request) {

    NoteEntity note = noteService.findById(id);
    if (note == null) {
      return ResponseEntity.notFound().build();
    }

    noteService.delete(note);

    return ResponseEntity.noContent().build();
  }
}
