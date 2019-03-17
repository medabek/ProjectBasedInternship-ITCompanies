package com.ourFinalProject.turizm.model;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "tour_place")
public class TourPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tour_id")
    private int id;

    @Column(name = "title", nullable = false)
    @NotEmpty(message = "*Please provide an title")
    private String title;
    @Column(name = "content")
    private String content;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "likes", nullable = false)
    private int likes = 0;
    @Column(name = "tour_img_name")
    private String tour_img_name;

    public String getTour_img_name() {
        return "/images/" + tour_img_name;
    }

    public void setTour_img_name(String tour_img_name) {
        this.tour_img_name = tour_img_name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void addLike(){
        this.likes++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }




}