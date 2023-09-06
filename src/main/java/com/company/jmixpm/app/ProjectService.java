package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import io.jmix.core.DataManager;
import io.jmix.core.validation.group.UiComponentChecks;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@Validated
@Component
public class ProjectService {

    private final DataManager dataManager;

    public ProjectService(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Validated(value = {Default.class, UiComponentChecks.class, UiCrossFieldChecks.class})
    public Project saveProject(@NotNull Project project) {
        return dataManager.save(project);
    }
}