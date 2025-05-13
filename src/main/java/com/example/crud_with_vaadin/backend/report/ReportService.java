package com.example.crud_with_vaadin.backend.report;

import com.example.crud_with_vaadin.backend.entities.AppUser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    public byte[] getUserReport(List<AppUser> items) throws JRException, FileNotFoundException {

        JasperReport jasperReport;

        //Load object from template
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("classpath:UserReportNew.jasper"));  }
        catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File Not Found");
        }

        //Connecting Datasource to fill report
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "User Report");

        //Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
