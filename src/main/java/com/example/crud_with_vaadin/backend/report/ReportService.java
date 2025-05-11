package com.example.crud_with_vaadin.backend.report;

import com.example.crud_with_vaadin.backend.entities.AppUser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    public byte[] getUserReport(List<AppUser> items) throws JRException {

        JasperReport jasperReport;

        try {
            File file = ResourceUtils.getFile("classpath:UserReport.jrxml");
            jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRSaver.saveObject(jasperReport, "UserReport.jasper");
        } catch (FileNotFoundException | JRException ex) {
            throw new RuntimeException(ex);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "User Report");

        //Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
