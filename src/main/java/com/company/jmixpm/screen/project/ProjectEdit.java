package com.company.jmixpm.screen.project;

import com.company.jmixpm.app.ProjectService;
import com.company.jmixpm.datatype.ProjectLabels;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.screen.user.UserBrowse;
import io.jmix.audit.snapshot.EntitySnapshotManager;
import io.jmix.core.validation.group.UiComponentChecks;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Field;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.component.Window;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {

    @Autowired
    private Field<ProjectLabels> projectLabelsField;

    @Autowired
    private Button performBeanValidationBtn;
    @Autowired
    private Button commitWithBeanValidation;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private Notifications notifications;
    @Autowired
    private EntitySnapshotManager entitySnapshotManager;

    @Subscribe
    public void onInit(final InitEvent event) {
//        setCrossFieldValidate(false);
    }

    @Subscribe
    public void onAfterCommitChanges(final AfterCommitChangesEvent event) {
        entitySnapshotManager.createSnapshot(getEditedEntity(), getEditedEntityContainer().getFetchPlan());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        performBeanValidationBtn.setEnabled(!readOnly);
        commitWithBeanValidation.setEnabled(!readOnly);
    }

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Project> event) {
        projectLabelsField.setEditable(true);

        event.getEntity().setProjectLabels(
                new ProjectLabels(List.of("bug", "enhancement", "task"))
        );
    }

    @Install(to = "participantsTable.add", subject = "screenConfigurer")
    private void participantsTableAddScreenConfigurer(final Screen screen) {
        ((UserBrowse) screen).setFilterProject(getEditedEntity());
    }

    @Subscribe("commitWithBeanValidation")
    public void onCommitWithBeanValidationClick(final Button.ClickEvent event) {
        try {
            projectService.saveProject(getEditedEntity());
            close(new StandardCloseAction(Window.CLOSE_ACTION_ID, false));
        } catch (ConstraintViolationException e) {
            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                sb.append(constraintViolation.getMessage()).append("\n");
            }

            notifications.create()
                    .withCaption(sb.toString())
                    .show();
        }
    }

    @Autowired
    private Validator validator;

    @Subscribe("performBeanValidationBtn")
    public void onPerformBeanValidationBtnClick(final Button.ClickEvent event) {
        Project editedEntity = getEditedEntity();
        Set<ConstraintViolation<Project>> violations =
                validator.validate(editedEntity, Default.class, UiComponentChecks.class, UiCrossFieldChecks.class);

        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : violations) {
            sb.append(constraintViolation.getMessage()).append("\n");
        }

        notifications.create()
                .withCaption(sb.toString())
                .show();
    }
}