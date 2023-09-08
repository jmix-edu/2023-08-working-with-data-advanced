package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Project;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Project.browse")
@UiDescriptor("project-browse.xml")
@LookupComponent("projectsTable")
public class ProjectBrowse extends StandardLookup<Project> {

    @Autowired
    private CollectionContainer<Project> projectsDc;

    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Integer newProjectsCount = dataManager.loadValue("select count(e) from Project e " +
                        "where :session_isManager = TRUE and e.status = @enum(com.company.jmixpm.entity.ProjectStatus.NEW) " +
                        "and e.manager.id = :current_user_id", Integer.class)
                .one();

        if (newProjectsCount != 0) {
            notifications.create()
                    .withCaption("New Projects")
                    .withDescription("You have projects with NEW status: " + newProjectsCount)
                    .withType(Notifications.NotificationType.TRAY)
                    .show();
        }
    }

    @Subscribe("createProjectBtn")
    public void onCreateProjectBtnClick(final Button.ClickEvent event) {
        Project project = dataManager.create(Project.class);
        project.setName("Project " + RandomStringUtils.randomAlphabetic(5));
        final User user = (User) currentAuthentication.getUser();
        project.setManager(user);

        Project saved = dataManager.unconstrained().save(project);
        projectsDc.getMutableItems().add(saved);
    }
}