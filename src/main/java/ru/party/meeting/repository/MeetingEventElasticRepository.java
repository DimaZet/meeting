package ru.party.meeting.repository;

import java.util.List;
import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.party.meeting.model.ElasticMeetingEvent;

@Repository
public interface MeetingEventElasticRepository extends ElasticsearchRepository<ElasticMeetingEvent, UUID> {
    List<ElasticMeetingEvent> findAll();

    List<ElasticMeetingEvent> search(QueryBuilder var1);
}
