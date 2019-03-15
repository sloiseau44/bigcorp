package com.training.spring.bigcorp.controller;

import com.training.spring.bigcorp.model.Site;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.repository.MeasureDao;
import com.training.spring.bigcorp.repository.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Transactional
@RequestMapping(path="/sites")
public class SiteController {
    @Autowired
    private SiteDao siteDao;

    @Autowired
    private MeasureDao measureDao;

    @Autowired
    private CaptorDao captorDao;


    @GetMapping
    public ModelAndView sitesListe() {
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable String id) {
        return new ModelAndView("site")
                .addObject("site",
                        siteDao.findById(id).orElseThrow(IllegalArgumentException::new));
    }
    @GetMapping("/create")
    public ModelAndView create(Model model){
        return new ModelAndView("site").addObject("site", new Site());
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(Site site) {
        if (site.getId() == null) {
            return new ModelAndView("site").addObject("site", siteDao.save(site));
        } else {
            Site siteToPersist =siteDao.findById(site.getId()).orElseThrow(IllegalArgumentException::new);
            siteToPersist.setName(site.getName());
            return new ModelAndView("sites").addObject("sites", siteDao.findAll());
        }
    }

    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable String id) {
        Site site = siteDao.findById(id).orElseThrow(IllegalArgumentException::new);
        site.getCaptors().forEach(c -> measureDao.deleteByCaptorId(c.getId()));
        captorDao.deleteBySiteId(id);
        siteDao.delete(site);
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }

}
