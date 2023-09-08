package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.ProjectStats;
import com.company.jmixpm.entity.Task;
import io.jmix.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProjectStatsService {

    private final DataManager dataManager;
    private final FetchPlans fetchPlans;
    private final FetchPlanRepository fetchPlanRepository;
    private final Metadata metadata;

    public ProjectStatsService(DataManager dataManager,
                               FetchPlans fetchPlans,
                               FetchPlanRepository fetchPlanRepository,
                               Metadata metadata) {
        this.dataManager = dataManager;
        this.fetchPlans = fetchPlans;
        this.fetchPlanRepository = fetchPlanRepository;
        this.metadata = metadata;
    }

    private FetchPlan createFetchPlanWithTasks() {
        return fetchPlans.builder(Project.class)
                .addFetchPlan(FetchPlan.INSTANCE_NAME)
                .add("tasks", fetchPlanBuilder -> fetchPlanBuilder
                        .add("estimatedEfforts")
                        .add("startDate"))
                .build();
    }

    public List<ProjectStats> fetchProjectStatistics() {
        List<Project> projects = dataManager.load(Project.class)
                .all()
//                .fetchPlan(createFetchPlanWithTasks())
                .fetchPlan(fetchPlanRepository
                        .findFetchPlan(metadata.getClass(Project.class), "project-with-tasks"))
                .list();

        return projects.stream()
                .map(project -> {
                    ProjectStats stats = dataManager.create(ProjectStats.class);
                    stats.setProjectName(project.getName());

                    List<Task> tasks = project.getTasks();
                    stats.setTaskCount(tasks.size());

                    Integer estimatedEfforts = tasks.stream()
                            .map(task ->
                                    task.getEstimatedEfforts() == null
                                            ? 0
                                            : task.getEstimatedEfforts())
                            .reduce(0, Integer::sum);
                    stats.setPlannedEfforts(estimatedEfforts);
//                    stats.setPlannedEfforts(project.getTotalEstimatedEffort());

                    stats.setActualEfforts(getActualEfforts(project.getId()));

                    return stats;
                }).collect(Collectors.toList());
    }

    private Integer getActualEfforts(UUID projectId) {
        return dataManager.loadValue("select sum(te.timeSpent) from TimeEntry te " +
                        "where te.task.project.id = :projectId", Integer.class)
                .parameter("projectId", projectId)
                .one();
    }
}