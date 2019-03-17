package com.ourFinalProject.turizm.controller;

import javax.validation.Valid;

import com.ourFinalProject.turizm.model.Role;
import com.ourFinalProject.turizm.repository.SaveTourPlaceRepository;
import com.ourFinalProject.turizm.repository.TourPlaceRepository;
import com.ourFinalProject.turizm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import com.ourFinalProject.turizm.model.User;
import com.ourFinalProject.turizm.service.UserService;


@Controller
public class LoginController {
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TourPlaceRepository tourPlaceRepository;
    @Qualifier("saveTourPlaceRepository")
    @Autowired
    private SaveTourPlaceRepository saveTourPlaceRepository;

    @RequestMapping(value={"/"}, method = RequestMethod.GET)
    public String homePage(){

        return "login";
    }

    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value={"/justin"}, method = RequestMethod.GET)
    public ModelAndView justin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("justin");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            System.out.println("exists");
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
            modelAndView.addObject("failedMessage", "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
            System.out.println("haserrors" + bindingResult.getAllErrors());
        } else {
            userService.saveUser(user);
            //System.out.println("save");
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("redirect:/login");

        }
        return modelAndView;
    }

    @RequestMapping(value="/user/home", method = RequestMethod.GET)
    public ModelAndView userHome(){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getRoles());

        final PageRequest page = new PageRequest(
                0, 4, new Sort(
                new Sort.Order(Sort.Direction.DESC, "likes")
        )
        );

        modelAndView.addObject("contents", tourPlaceRepository.findAll(page));
        modelAndView.addObject("seeAllContents", tourPlaceRepository.findAll());
        modelAndView.addObject("places", saveTourPlaceRepository.findAllByUser(user));

        System.out.println("tour size: " + tourPlaceRepository.findAll().size());



        for (Role role:user.getRoles()) {
            System.out.println(role.getRole());
            if(role.getRole().equals("USER")){
                //System.out.println("geldi");
                modelAndView.setViewName("userHome");
                return modelAndView;
            }
        }

        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("userHome");
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getRoles());
        for (Role role:user.getRoles()) {
            System.out.println(role.getRole());
            if(role.getRole().equals("ADMIN")){
                //System.out.println("geldi");
                modelAndView.setViewName("adminPage");
                return modelAndView;
            }
        }
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("adminPage");
        return modelAndView;
    }




}