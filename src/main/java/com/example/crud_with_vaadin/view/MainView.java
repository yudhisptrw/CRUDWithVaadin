package com.example.crud_with_vaadin.view;

import com.example.crud_with_vaadin.backend.UserEditor;
import com.example.crud_with_vaadin.backend.entities.AppUser;
import com.example.crud_with_vaadin.backend.report.ReportService;
import com.example.crud_with_vaadin.backend.repositories.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.util.StringUtils;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;


@Route
public class MainView extends VerticalLayout {

    final Grid<AppUser> grid;
    private final UserRepository repo;
    private final ReportService rptSvc = new ReportService();
    private final Button addNewBtn;
    private final Button printReportBtn;
    private final TextField filter;

    public MainView(UserRepository repo, UserEditor editor) {
        this.repo = repo;
        this.grid = new Grid<>(AppUser.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());
        this.printReportBtn = new Button("Print", VaadinIcon.PRINT.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, printReportBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName", "address", "job");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        // Hook logic to components
        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listOfUser(e.getValue()));

        // Connect selected User to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editUser(e.getValue(), "Edit");

        });

        // Instantiate and edit new user the new button is clicked
        addNewBtn.addClickListener(e -> editor.editUser(new AppUser("", "", "", ""), "Save"));

        rewrapPrintButton();

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listOfUser(filter.getValue());
            rewrapPrintButton();
        });

        // Initialize listing
        listOfUser(null);

    }

    void listOfUser(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repo.findUserByJob(filterText));
        } else {
            grid.setItems(repo.findAll());
        }
    }

    //Rewrap download button to refresh the repos after changes by editor
    void rewrapPrintButton () {
        try {
            byte[] reportContent = rptSvc.getUserReport(repo.findAll());
            FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                    new StreamResource("test.pdf", () -> new ByteArrayInputStream(reportContent)));
            buttonWrapper.wrapComponent(printReportBtn);
            add(buttonWrapper);
        } catch (JRException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
