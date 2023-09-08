package com.company.jmixpm.screen.user;

import com.company.jmixpm.app.UsersService;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.User;
import com.company.jmixpm.screen.userprojectsdialog.UserProjectsDialog;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.Filter;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.TextField;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.List;

@UiController("User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
public class UserBrowse extends StandardLookup<User> {
    @Autowired
    private Filter filter;
    @Autowired
    private GroupTable<User> usersTable;

    @Autowired
    private DataManager dataManager;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ScreenBuilders screenBuilders;

    private Project filterProject;

    @Install(to = "usersDl", target = Target.DATA_LOADER)
    private List<User> usersDlLoadDelegate(final LoadContext<User> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        if (filterProject != null
                && query != null) {
            return usersService.getUsersNotInProject(filterProject,
                    query.getFirstResult(), query.getMaxResults());
        }

        return dataManager.loadList(loadContext);
    }

    public void setFilterProject(@Nullable Project filterProject) {
        this.filterProject = filterProject;
        filter.setVisible(filterProject == null);
    }

    @Subscribe("usersTable.showUserProjects")
    public void onUsersTableShowUserProjects(final Action.ActionPerformedEvent event) {
        User selected = usersTable.getSingleSelected();
        if (selected == null) {
            return;
        }

        screenBuilders.screen(this)
                .withScreenClass(UserProjectsDialog.class)
                .build()
                .withUser(selected)
                .show();
    }
}