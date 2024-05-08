package tech.study.urlshortener.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.study.urlshortener.entities.UrlEntity;

public interface Urlrepository extends MongoRepository<UrlEntity, String> {
}
