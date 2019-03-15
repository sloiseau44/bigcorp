package com.training.spring.bigcorp.controller;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.FixedCaptor;
import com.training.spring.bigcorp.model.Site;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.repository.MeasureDao;
import com.training.spring.bigcorp.repository.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
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
@RequestMapping(path = "/sites/{siteId}/captors/FIXED")
public class FixedCaptorController {
    @Autowired
    private SiteDao siteDao;

    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private MeasureDao measureDao;

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable String siteId, @PathVariable String id) {

        Site site = siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new);

        return new ModelAndView("captor")
                .addObject("captor",
                        captorDao.findById(id).orElseThrow(IllegalArgumentException::new))
                .addObject(site);
    }

    @GetMapping("/create")
    public ModelAndView create(@PathVariable String siteId, Model model) {

        Site site = siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new);

        return new ModelAndView("captor").addObject("captor", new FixedCaptor())
                .addObject(site);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(@PathVariable String siteId, FixedCaptor captor) {
        Site site = siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new);
        FixedCaptor captorToPersist;
        if (captor.getId() == null) {
            captorToPersist = new FixedCaptor(captor.getName(), site, captor.getDefaultPowerInWatt());
        } else {
            captorToPersist = (FixedCaptor) captorDao.findById(captor.getId()).orElseThrow(IllegalArgumentException::new);
            captorToPersist.setName(captor.getName());
            captorToPersist.setDefaultPowerInWatt(captor.getDefaultPowerInWatt());
        }
        captorDao.save(captorToPersist);
        return new ModelAndView("site").addObject("site", site);
    }

    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable String id) {
        Captor captor = captorDao.findById(id).orElseThrow(IllegalArgumentException::new);
        measureDao.deleteByCaptorId(captor.getId());
        captorDao.delete(captor);
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());

    }
}
