package com.company.jmixpm.screen.task;

import com.company.jmixpm.entity.Project;
import io.jmix.core.DataManager;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Task_.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {

    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(final InitEvent event) {
        /*Task task = dataManager.load(Task.class)
                .all()
                .one();

        Project project = task.getProject();
        String name = project.getName();*/
    }
}