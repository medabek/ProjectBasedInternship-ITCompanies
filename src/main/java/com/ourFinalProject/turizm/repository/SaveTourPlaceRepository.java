package com.ourFinalProject.turizm.repository;


import com.ourFinalProject.turizm.model.SaveTourPlace;
import com.ourFinalProject.turizm.model.TourPlace;
import com.ourFinalProject.turizm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("saveTourPlaceRepository")
public interface SaveTourPlaceRepository extends JpaRepository<SaveTourPlace, Long> {
    Set<SaveTourPlace> findAllByUser(User user);
    Set<SaveTourPlace> findAllByTourPlaceAndUser(TourPlace tourPlace, User user);
    Set<SaveTourPlace>  findAllByTourPlace(TourPlace tourPlace);



}