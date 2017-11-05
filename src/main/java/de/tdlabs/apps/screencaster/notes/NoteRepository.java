package de.tdlabs.apps.screencaster.notes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface NoteRepository extends JpaRepository<Note, Long> {

  List<Note> findAllByOrderByCreatedAtAsc();
}
