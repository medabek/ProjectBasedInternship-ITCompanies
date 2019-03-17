package com.ourFinalProject.turizm.repository;

import com.ourFinalProject.turizm.model.TourPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ourFinalProject.turizm.model.User;
import org.springframework.data.domain.Pageable;
import java.util.Set;

@Repository("tourRepository")
public interface TourPlaceRepository extends JpaRepository<TourPlace, Integer> {
    Set<TourPlace> findAllByUser(User user);
    TourPlace findByTitle(String title);

    @Query("select t from TourPlace t where t.title like %?1% or t.content like %?1%")
    Page<TourPlace> searchQuery(String query, Pageable pageable);


    //TourPlace create(TourPlace tourPlace);
//    @Query("select t from TourPlace t order by likes desc")
//    Set<TourPlace> findAll(Pageable pageable);

}