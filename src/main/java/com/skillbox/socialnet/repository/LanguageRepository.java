package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository  extends JpaRepository<Language, Long> {
}
