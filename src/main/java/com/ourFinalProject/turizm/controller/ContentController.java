package com.ourFinalProject.turizm.controller;

import com.ourFinalProject.turizm.model.Role;
import com.ourFinalProject.turizm.model.SaveTourPlace;
import com.ourFinalProject.turizm.model.TourPlace;
import com.ourFinalProject.turizm.model.User;
import com.ourFinalProject.turizm.repository.RoleRepository;
import com.ourFinalProject.turizm.repository.SaveTourPlaceRepository;
import com.ourFinalProject.turizm.repository.TourPlaceRepository;
import com.ourFinalProject.turizm.repository.UserRepository;
import com.ourFinalProject.turizm.service.StorageService;
import com.ourFinalProject.turizm.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ContentController {
    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StorageService storageService;

    @Autowired
    private TourPlaceRepository tourPlaceRepository;

    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;

    @Qualifier("saveTourPlaceRepository")
    @Autowired
    private SaveTourPlaceRepository saveTourPlaceRepository;

    @RequestMapping("/addContent")
    public String addStudent() {
        return "createContent";
    }

    @RequestMapping(value = "/createContent", method = RequestMethod.POST)
    public RedirectView createToutPlace(@RequestParam("file") MultipartFile file, Model model, TourPlace tourPlace, HttpServletRequest request) {
        //ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        tourPlace.setUser(user);
        try {
            storageService.store(file);
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

            tourPlace.setTour_img_name(file.getOriginalFilename());


        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }

        tourPlaceRepository.save(tourPlace);
        //modelAndView.setViewName("adminPage");

        return new RedirectView(request.getHeader("referer"));

    }


    @RequestMapping(value = {"/createContent1"}, method = RequestMethod.GET)
    public ModelAndView createTour() {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        TourPlace tour = new TourPlace();
        tour.setTitle("Bi");
        tour.setContent("Is the Best");
        tour.setUser(user);
        tourPlaceRepository.save(tour);
        modelAndView.setViewName("adminPage");

        return modelAndView;
    }


    @RequestMapping(value = {"/saveTour"}, method = RequestMethod.GET)
    public RedirectView saveTour(@RequestParam(value = "id", required = false) Integer tour_id, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        TourPlace tour = tourPlaceRepository.getOne(tour_id);
        if (tour != null) {
            if (saveTourPlaceRepository.findAllByTourPlaceAndUser(tour, user).isEmpty()) {
                SaveTourPlace stp = new SaveTourPlace();
                stp.setTourPlace(tour);
                stp.setUser(user);
                saveTourPlaceRepository.save(stp);
            }
        }

        for (SaveTourPlace saveTourPlace : saveTourPlaceRepository.findAllByUser(user)) {
            System.out.println(saveTourPlace.getTourPlace().getTitle());
        }


        return new RedirectView(request.getHeader("referer"));
    }


    @RequestMapping(value = "/seeAllContent", method = RequestMethod.GET)
    public String index(Model md) {
        md.addAttribute("seeAllContents", tourPlaceRepository.findAll());

        return "seeAllContent";
    }

    /*
        @RequestMapping("/update_content")
        public String updatedPost(ModelMap model, @RequestParam("id") Integer id)
        {
            model.addAttribute("content", tourPlaceRepository.getOne(id));
            return "content";
        }

    */
    @RequestMapping(value = "/update_content/{id}", method = RequestMethod.GET)
    public String updatePost(Model mdModel, @PathVariable Integer id) {
        TourPlace tourPlace = tourPlaceRepository.getOne(id);
        mdModel.addAttribute("id", tourPlace.getId());
        mdModel.addAttribute("title", tourPlace.getTitle());
        mdModel.addAttribute("content", tourPlace.getContent());
        return "update_content";
    }


    @RequestMapping(value = "/update_content", method = RequestMethod.POST)
    public String updatePost(@RequestParam("file") MultipartFile file,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("id") Integer id) {
        TourPlace tourPlace = tourPlaceRepository.getOne(id);
        tourPlace.setTitle(title);

        try {
            if (file != null) {
                storageService.store(file);
                tourPlace.setTour_img_name(file.getOriginalFilename());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        tourPlace.setContent(content);
        tourPlaceRepository.save(tourPlace);
        return "update_content";
    }

    @RequestMapping(value = "/seeAllUsers", method = RequestMethod.GET)
    public String seeAllUser(Model md) {

        Role userRole = roleRepository.findByRole("USER");
        md.addAttribute("seeAllUsers", userRepository.findAllByRoles(new HashSet<Role>(Arrays.asList(userRole))));

        return "seeAllUsers";
    }

    @RequestMapping(value = "/delete_content/{id}", method = RequestMethod.GET)
    public RedirectView contentDelete(@PathVariable(required = true, name = "id") int id,
                                      HttpServletRequest request) {

        TourPlace tp = tourPlaceRepository.getOne(id);

        saveTourPlaceRepository.deleteAll(saveTourPlaceRepository.findAllByTourPlace(tp));
        tourPlaceRepository.delete(tp);
        return new RedirectView(request.getHeader("referer"));
    }

    @RequestMapping(value = "/delete_user/{id}", method = RequestMethod.GET)
    public RedirectView userDelete(@PathVariable(required = true, name = "id") int id,
                                   HttpServletRequest request) {

        User us = userRepository.getOne(id);

        Set<TourPlace> tps = tourPlaceRepository.findAllByUser(us);

        Set<SaveTourPlace> sv = saveTourPlaceRepository.findAllByUser(us);

        for (TourPlace tpp : tps) {
            Set<SaveTourPlace> sv2 = saveTourPlaceRepository.findAllByTourPlace(tpp);
            saveTourPlaceRepository.deleteAll(sv2);
        }
        saveTourPlaceRepository.deleteAll(sv);

        tourPlaceRepository.deleteAll(tps);

        userRepository.delete(us);
        return new RedirectView(request.getHeader("referer"));
    }

    @RequestMapping("/userHome")
    public ModelAndView showHome(ModelAndView model) {
        model.addObject("seeAllContents", tourPlaceRepository.findAll());
        model.setViewName("seeAllContent");
        return model;
    }

    @RequestMapping("/content")
    public String showPost(ModelMap model, @RequestParam("id") Integer id) {
        model.addAttribute("content", tourPlaceRepository.getOne(id));
        return "content";
    }

    @RequestMapping(value = "/searchContent", method = RequestMethod.GET)
    public String searchStudent(@RequestParam("text") String text, ModelMap modelMap, Pageable pageable) {
        final Pageable page = new PageRequest(0, 4);
        modelMap.addAttribute("seeAllContents", tourPlaceRepository.findAll());
        modelMap.addAttribute("contents", tourPlaceRepository.searchQuery(text, page).getContent());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelMap.addAttribute("places", saveTourPlaceRepository.findAllByUser(user));

        return "userHome";
    }

    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public RedirectView like(@RequestParam("id") Integer id, ModelMap modelMap, HttpServletRequest request) {

        TourPlace tourPlace = tourPlaceRepository.getOne(id);
        tourPlace.addLike();
        tourPlaceRepository.save(tourPlace);
        return new RedirectView(request.getHeader("referer"));
    }

//    @RequestMapping(value = "/news", method = RequestMethod.GET)
//    public ModelAndView newses(ModelAndView modelAndView, Model modelMap) throws IOException {
//        String url = "http://www.codenuclear.com/category/java/";
//        Document doc = Jsoup.connect(url).get();
//
//        /***Selecting with Id ***/
//        Elements breadCrumbs = doc.select("#flash-breadcrumbs");
//        for (Element breadCrumb : breadCrumbs) {
//
//            String text = breadCrumb.text();
//
//
//
//        modelMap.addAttribute(text);
//        }
//        return modelAndView;
//    }

}
