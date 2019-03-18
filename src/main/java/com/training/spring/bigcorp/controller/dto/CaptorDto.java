package com.training.spring.bigcorp.controller.dto;

import com.training.spring.bigcorp.model.*;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.repository.MeasureDao;
import com.training.spring.bigcorp.repository.SiteDao;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

public class CaptorDto {
    @Autowired
    private SiteDao siteDao;

    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private MeasureDao measureDao;

    private PowerSource powerSource;
    private String id;
    private String name;
    private String siteId;
    private String siteName;
    private Integer defaultPowerInWatt;
    private Integer minPowerInWatt;
    private Integer maxPowerInWatt;
    public CaptorDto() {
    }
    public CaptorDto(Site site, FixedCaptor fixedCaptor) {
        this.powerSource = PowerSource.FIXED;
        this.id = fixedCaptor.getId();
        this.name = fixedCaptor.getName();
        this.siteId = site.getId();
        this.siteName = site.getName();
        this.defaultPowerInWatt = fixedCaptor.getDefaultPowerInWatt();
    }
    public CaptorDto(Site site, SimulatedCaptor simulatedCaptor) {
        this.powerSource = PowerSource.SIMULATED;
        this.id = simulatedCaptor.getId();
        this.name = simulatedCaptor.getName();
        this.siteId = site.getId();
        this.siteName = site.getName();
        this.minPowerInWatt = simulatedCaptor.getMinPowerInWatt();
        this.maxPowerInWatt = simulatedCaptor.getMaxPowerInWatt();
    }
    public CaptorDto(Site site, RealCaptor realCaptor) {
        this.powerSource = PowerSource.REAL;
        this.id = realCaptor.getId();
        this.name = realCaptor.getName();
        this.siteId = site.getId();
        this.siteName = site.getName();
    }
    public Captor toCaptor(Site site) {
        Captor captor;
        switch (powerSource) {
            case REAL:
                captor = new RealCaptor(name, site);
                break;
            case FIXED:
                captor = new FixedCaptor(name, site, defaultPowerInWatt);
                break;
            case SIMULATED:
                captor = new SimulatedCaptor(name, site, minPowerInWatt,
                        maxPowerInWatt);
                break;
            default:
                throw new IllegalStateException("Unknown type");
        }
        captor.setId(id);
        return captor;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(@PathVariable String siteId, CaptorDto captorDto) {
        Site site = siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new);
        Captor captor = captorDto.toCaptor(site);
        captorDao.save(captor);
        return new ModelAndView("site").addObject("site", site);
    }

    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable String siteId, @PathVariable String id) {
        measureDao.deleteByCaptorId(id);
        captorDao.deleteById(id);
        return new ModelAndView("site")
                .addObject("site",
                        siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new));
    }

    public PowerSource getPowerSource() {
        return powerSource;
    }

    public void setPowerSource(PowerSource powerSource) {
        this.powerSource = powerSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getDefaultPowerInWatt() {
        return defaultPowerInWatt;
    }

    public void setDefaultPowerInWatt(Integer defaultPowerInWatt) {
        this.defaultPowerInWatt = defaultPowerInWatt;
    }

    public Integer getMinPowerInWatt() {
        return minPowerInWatt;
    }

    public void setMinPowerInWatt(Integer minPowerInWatt) {
        this.minPowerInWatt = minPowerInWatt;
    }

    public Integer getMaxPowerInWatt() {
        return maxPowerInWatt;
    }

    public void setMaxPowerInWatt(Integer maxPowerInWatt) {
        this.maxPowerInWatt = maxPowerInWatt;
    }
}


