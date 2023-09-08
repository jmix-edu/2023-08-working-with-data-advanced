package com.company.jmixpm.screen.taskentitylogbrowse;

import com.company.jmixpm.entity.Task;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UiController("TaskEntityLogBrowse")
@UiDescriptor("task-entity-log-browse.xml")
public class TaskEntityLogBrowse extends Screen {

    @Autowired
    private EntityComboBox<UUID> taskField;

    @Subscribe(id = "tasksDc", target = Target.DATA_CONTAINER)
    public void onTasksDcCollectionChange(final CollectionContainer.CollectionChangeEvent<Task> event) {
        List<Task> tasks = event.getSource().getItems();
        taskField.setOptionsMap(tasks.stream()
                .collect(Collectors.toMap(Task::getName, Task::getId)));
    }
}