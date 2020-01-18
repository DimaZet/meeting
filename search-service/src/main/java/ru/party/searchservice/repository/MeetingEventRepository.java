package ru.party.searchservice.repository;

import java.util.List;
import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.party.searchservice.model.MeetingEvent;

@Repository
public interface MeetingEventRepository extends ElasticsearchRepository<MeetingEvent, UUID> {
    List<MeetingEvent> findAll();

    List<MeetingEvent> search(QueryBuilder var1);
}
