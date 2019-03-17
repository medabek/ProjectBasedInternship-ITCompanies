package com.ourFinalProject.turizm.model;

//package com.elrealo.firstProject.model;

import javax.persistence.*;

@Entity
@Table(name = "save_tour_place")
public class SaveTourPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = TourPlace.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "tour_id")
    //@Column(name = "tour_id", nullable = false)
    private TourPlace tourPlace;



    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TourPlace getTourPlace() {
        return tourPlace;
    }

    public void setTourPlace(TourPlace tourPlace) {
        this.tourPlace = tourPlace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}