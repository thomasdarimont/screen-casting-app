package de.tdlabs.apps.screencaster.filestore;

import org.springframework.data.repository.CrudRepository;

interface FileRepository extends CrudRepository<FileEntity, String> {
}
