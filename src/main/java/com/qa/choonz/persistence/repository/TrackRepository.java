package com.qa.choonz.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qa.choonz.persistence.domain.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

	@Query("SELECT t FROM Track t WHERE t.name = ?1")
	public Track getTrackByNameJPQL(String name);

}
