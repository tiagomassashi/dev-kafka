package br.com.nagata.dev.repository;

import br.com.nagata.dev.model.ApplicationLog;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationLogRepository extends CrudRepository<ApplicationLog, Long> {}
